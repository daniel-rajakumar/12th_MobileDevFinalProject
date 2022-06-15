// https://github.com/puppeteer/puppeteer/issues/4752#issuecomment-586599843
// https://github.com/puppeteer/puppeteer/issues/665#issuecomment-395287755

const { encrypt, decrypt } = require('./crypto');
const fs = require('fs')
const puppeteer = require('puppeteer')
const express = require("express");

const app = express()
const PORT = 8080;
const HOST = "localhost";

app.use(express.json())

app.listen(PORT, () => console.log(`http://${HOST}:${PORT}`))

const users = new Map()
const startURL = 'https://students.sbschools.org/genesis/sis/view?gohome=true'


// make sure this directory has "images" folder.
const dataPATH = `${require('os').homedir()}/Documents/data/`



let map = new Map();

setUpUsers().then(r => {})

// restores the saved user's data when restarting the server.
async function setUpUsers(){
    const path = `${dataPATH}/users.txt`
    const lines = fs.readFileSync(path, 'utf-8')
                    .split('\n')
                    .filter(Boolean);
    for (const line of lines) {
        let username = line.substring(0, line.indexOf(','))
        let password = decrypt(JSON.parse(line.substring(line.indexOf(',')+2)))
        map.set(username, password)
    }

    map.forEach((value, key) => {
        addNewUser({
            username: key,
            password: value,
        })
    })
}


// checks whether the login is valid
app.post('/checkFirstTimeLogin', async (req, res) => {
    let user =  {
        username: req.body.username,
        password: req.body.password,
    }

    if (checkUser(user)) {
        res.status(200).send()
    } else {
        res.status(404).send();
    }
})



// performs logout action. Removes every data about that user from server and database.
app.post('/logout', async (req, res) => {
    let user =  {
        username: req.body.username,
        password: req.body.password,
    }

    try {
        await getUser(user).browser.close()
        removeUser(user)
        updateLocalDatabase()
        res.status(200).send()
    } catch (e) {
        console.log(e)
        res.status(404).send()
    }
})


// perform login action. if the user doesn't exist in the database, add the user.
app.post('/login', async (req, res) => {
    console.log("logging...")

    // an object with username and password.
    let user =  {
        username: req.body.username,
        password: req.body.password,
    }

    // if user already exists in database then don't do any action. Wait and return success status to front-end saying the user exists.
    if (checkUser(user)) {
        console.log(" user already exists")
        user = getUser(user)

        try {
            process.stdout.write(`page... STARTING...`);
            await waitTillLoadPage(user)
        } catch (e) {
            console.log(e)
            res.status(400).send()
        }

        console.log(`DONE`);

        res.status(200).send();
    } else {
        console.log("adding user...")
        // adds a new user to server and returns appropriate code.
        let code = await addNewUser(user)
        res.status(code).send()
    }
})

// check if this user exists
function checkUser(user){
    for (let e of Array.from(users.keys())){
        if (e.password === user.password && e.username === user.username)
            return true
    }
    return false
}


// remove user from the server
function removeUser(user){
    for (let e of Array.from(users.keys())){
        if (e.password === user.password && e.username === user.username){
            users.delete(e)


            try {
                fs.unlinkSync(`${dataPATH}/images/user_${e.username.substring(0, 8)}.png`)
            } catch(err) {
                console.error(err)
            }


        }
    }
}


// add new user to database.
async function addNewUser(user){
    users.set(user, {});

    // A PART OF NEW TECHNOLOGY (WEB SCRAPPING)

    // launch a Chromium browser in headless mode.
    users.get(user).browser = await puppeteer.launch( { headless: true })

    users.get(user).pages = [
        {tab: await users.get(user).browser.newPage(), isLoaded: false},
        {tab: await users.get(user).browser.newPage(), isLoaded: false},
        {tab: await users.get(user).browser.newPage(), isLoaded: false},
        {tab: await users.get(user).browser.newPage(), isLoaded: false},
        {tab: await users.get(user).browser.newPage(), isLoaded: false},
        {tab: await users.get(user).browser.newPage(), isLoaded: false},
    ]

    // setup
    await users.get(user).pages.forEach(page => {
        page.tab.setUserAgent('Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36')
    })

    // go to genesis login page
    await users.get(user).pages[users.get(user).pages.length - 1].tab.goto(startURL, {timeout: 60 * 1000})
    // enter username and password
    await users.get(user).pages[users.get(user).pages.length - 1].tab.type("#j_username", user.username)
    await users.get(user).pages[users.get(user).pages.length - 1].tab.type("#j_password", user.password)
    // click the button
    await users.get(user).pages[users.get(user).pages.length - 1].tab.click(".saveButton");

    // wait for the html > body to load in the new page.
    await users.get(user).pages[users.get(user).pages.length - 1].tab.waitForSelector("body")

    // if the page (url) did not change, meaning the login failed.
    if (await users.get(user).pages[users.get(user).pages.length - 1].tab.url() === startURL) {
        console.log("IS VALID: LOGGING FAIL")
        // delete the invalid user from server and close the browser
        await users.get(user).browser.close()
        users.delete(user)
        // send 404 repose back to frontend meaning login failed
        return 404

    } else {
        console.log("IS VALID: LOGGED IN")
        // add the valid user to database and update it.
        updateLocalDatabase()

        // closing some unwanted tabs
        const pages = await users.get(user).browser.pages()
        await pages[0].close()
        await pages[pages.length - 1].close()

        // open these pages in per tabs
        await Promise.all([
            // calendar
            users.get(user).pages[4].tab.goto(`https://sbhs.sbschools.org/calendar`, {timeout: 600 * 1000}),
            // picture
            users.get(user).pages[3].tab.goto(`https://students.sbschools.org/genesis/sis/photos?type=student&studentID=${user.username.substring(0, 8)}`, {timeout: 600 * 1000}),
            // grade
            users.get(user).pages[2].tab.goto(`https://students.sbschools.org/genesis/parents?tab1=studentdata&tab2=gradebook&tab3=weeklysummary&action=form&studentid=${user.username.substring(0, 8)}`, {timeout: 600 * 1000}),
            // main
            users.get(user).pages[1].tab.goto(`https://students.sbschools.org/genesis/parents?tab1=studentdata&tab2=studentsummary&action=form&studentid=${user.username.substring(0, 8)}`, {timeout: 600 * 1000}),
            // work
            users.get(user).pages[0].tab.goto(`https://students.sbschools.org/genesis/parents?tab1=studentdata&tab2=gradebook&tab3=listassignments&action=form&studentid=${user.username.substring(0, 8)}`, {timeout: 600 * 1000}),
        ])


        // in calendar tab, click and change the monthly view (default) to monthly list.
        await users.get(user).pages[4].tab.click(".CAL-dropdownButton.CAL-button.js-nav-system__link.fd_default");
        await users.get(user).pages[4].tab.click("#options_eventviewdropdown_460399 > li:nth-child(4)");

        // switch focus to picture tab of the user
        await users.get(user).pages[3].tab.bringToFront()

        // take a screenshot of the user and store it locally in the database.
        console.log("starting to take screenshot...")
        await users.get(user).pages[3].tab.waitForSelector(`body > img`)
        const element = await users.get(user).pages[3].tab.$(`body > img`)
        await element.screenshot({path: `${dataPATH}/images/user_${user.username.substring(0, 8)}.png`})
        console.log("...finished screenshot")

        // after taking the screenshot and storing it in database, close the tab.
        await users.get(user).pages[3].tab.close()


        // set some tabs that they are finished loaded.
        for (const e of [0, 1, 2]) {
            users.get(user).pages[e].isLoaded = true
        }

        // return a success status
        return 200
    }
}



// update database with the latest data
function updateLocalDatabase(){
    fs.writeFileSync(`${require('os').homedir()}/Documents/data/users.txt`, "");

    users.forEach((value, key) => {
        let data = `${key.username}, ${JSON.stringify(encrypt(Buffer.from(key.password, 'utf8')))}\n`
        fs.appendFileSync(`${require('os').homedir()}/Documents/data/users.txt`, data);
    })
}




// respond to "list" request send by the frontend.
app.post("/list", async (req, res) => {
    let user =  {
        username: req.body.username,
        password: req.body.password,
    }

    process.stdout.write(`list data: sending...`);
    await getListData(user)
        .then(data => {
            res.status(200).send( { data: data })
            console.log("sent")
        })
        .catch(err => {res.status(404).send();
            console.log(err) });

})


// respond to "event" request send by the frontend.
app.post("/event", async (req, res) => {
    let user =  {
        username: req.body.username,
        password: req.body.password,
    }

    process.stdout.write(`calendar data: sending...`);
    await getEventsData(user)
        .then(data => {
            res.status(200).send( { data: data })
            console.log("sent")
        })
        .catch(err => {res.status(404).send();
            console.log(err) });

})



// respond to "schedule" request send by the frontend.
app.post("/schedule", async (req, res) => {
    let user =  {
        username: req.body.username,
        password: req.body.password,
    }
    process.stdout.write(`schedule data: sending...`);
    await getScheduleData(user)
        .then(data => {
            res.status(200).send( { data: data })
            console.log("sent")
        })
        .catch(err => {res.status(404).send();
            console.log(err) });

})


// respond to "profile" request send by the frontend.
app.post("/profile", async (req, res) => {
    let user =  {
        username: req.body.username,
        password: req.body.password,
    }
    process.stdout.write(`profile data: sending...`);
    await getProfileData(user)
        .then(data => {
            res.status(200).send( { data: data })
            console.log("sent")
        })
        .catch(err => {res.status(404).send();
            console.log(err) });

})




// respond to "grade" request send by the frontend.
app.post("/grade", async (req, res) => {
    let user =  {
        username: req.body.username,
            password: req.body.password,
        }
    process.stdout.write(`grade data: sending...`);
    await getGradeData(user)
                 .then(data => {
                     res.status(200).send( { data: data })
                     console.log("sent")
                 })
                 .catch(err => {res.status(404).send();
                     console.log(err) });

 })



// web-scrape and get schedule data. (Like class times)
async function getScheduleData(users){
    user = getUser(users)
    const page = user.pages[1].tab
    console.log(users.username)
    let arr = []

    await page.reload()
    await waitTillHTMLRendered(page, 1000)

    try {

        let num = 20;
        for (let i = 0; i < num; i++){
            // prev section
            await waitTillHTMLRendered(page, 25)
            await page.evaluate(async () => {
                await document.querySelector("body > form:nth-child(9) > table > tbody > tr:nth-child(2) > td > table > tbody > tr > td:nth-child(2) > table:nth-child(5) > tbody > tr > td:nth-child(1) > img").click()
            })
        }


        while (true){
            for (let i = 2; i <= 3; i++) {
                await waitTillHTMLRendered(page, 25)
                await page.evaluate(data => {
                    document.querySelector(`#divScheduleDatePicker${data[0]} > table > tbody > tr > td:nth-child(${data[1]})`).click()
                }, [users.username.substring(0, 8), i])

                await waitTillHTMLRendered(page, 100)
                await page.waitForSelector(`#divBellScheduleContainer${users.username.substring(0, 8)} > table > tbody > tr`)
                await page.waitForSelector(`#divScheduleDatePicker${users.username.substring(0, 8)} > table > tbody > tr > td:nth-child(${i})`)
                let day = await page.evaluate(async (outer) => {
                    let data, date

                    await Promise.all([
                        data = [].slice.call(document.querySelectorAll(`#divBellScheduleContainer${outer[0]} > table > tbody > tr`), 1),
                        date = document.querySelector(`#divScheduleDatePicker${outer[0]} > table > tbody > tr > td:nth-child(${outer[1]})`)
                    ])

                    let classes = []

                    let dates = [
                        date.innerText.split("\n")[0],
                        date.innerText.split("\n")[1]
                    ]

                    for (let i = 0; i < data.length; i++){
                        classes.push([
                            data[i].innerText.split("\n")[0],
                            data[i].innerText.split("\n")[1],
                            data[i].innerText.split("\n")[2],
                            data[i].innerText.split("\n")[4],
                            data[i].innerText.split("\n")[5],
                            data[i].innerText.split("\n")[6],
                        ])
                    }

                    if (dates[1] === "School Closed")
                        return { head: dates, body: [ "no school" ] }
                    else
                        return { head: dates, body: classes }

                }, [users.username.substring(0, 8), i])

                arr.push(day)
            }

            // next section
            await page.evaluate(() => {
                document.querySelector("body > form:nth-child(9) > table > tbody > tr:nth-child(2) > td > table > tbody > tr > td:nth-child(2) > table:nth-child(5) > tbody > tr > td:nth-child(3) > img").click()
            })

            console.log(arr)
        }

    } catch (e){
        await page.reload()
        await waitTillHTMLRendered(page, 100)
        return arr
    }
}




// web-scrape and get events data. (school events like holidays)
async function getEventsData(user){
    user = getUser(user)
    const page = user.pages[4].tab
    let arr = []

    await page.reload()
    await waitTillHTMLRendered(page, 100)
    await page.evaluate(() => {
        document.querySelector(".CAL-dropdownButton.CAL-button.js-nav-system__link.fd_default").click()
    })
    await page.evaluate(() => {
        document.querySelector("#options_eventviewdropdown_460399 > li:nth-child(4)").click()
    })

    for (let i = 0; i < 12; i++){
        await waitTillHTMLRendered(page, 100)
        await page.evaluate(() => {
            document.querySelector(".CAL-button.CAL-square.js-nav-system__link.prevButton").click()
        })
    }

    await waitTillHTMLRendered(page, 50)
    for (let i = 0; i < 24; i++){
        await page.evaluate(() => {
            document.querySelector(".CAL-button.CAL-square.js-nav-system__link.nextButton").click()
        })
        await waitTillHTMLRendered(page, 200)
        let day = await page.evaluate(async () => {
            let name, date;

            await  Promise.all([
                name = document.querySelectorAll(".CAL-V-LV-name"),
                date = document.querySelectorAll(".CAL-V-LV-time")
            ])

            let day = []
            // document.querySelector(".CAL-button.CAL-square.js-nav-system__link.prevButton").click()
            for (let i = 0; i < name.length; i++) {
                try {
                    day.push([
                        name[i].innerText.split("\n")[1],
                        date[i].innerText
                    ])
                } catch {
                }
            }

            return day
        })


        day.forEach(e => {
            arr.push(e)
        })

        // console.log(arr)
    }


    return arr
}





// web-scrape and get list data. (school work lists - projects, tests, CW, HW)
async function getListData(user){
    user = getUser(user)
    const page = user.pages[0].tab

    await page.reload()
    await waitTillHTMLRendered(page, 100)

    return await page.evaluate(async () => {
        let course, due, category, assignment, mp, grade, comment

        await Promise.all([
            mp = [].slice.call(document.querySelectorAll(".list > tbody > tr > td:nth-child(1)"), 1),
            course = [].slice.call(document.querySelectorAll(".list > tbody > tr > td:nth-child(3)"), 1),
            due = [].slice.call(document.querySelectorAll(".list > tbody > tr > td:nth-child(2)"), 1),
            category = [].slice.call(document.querySelectorAll(".list > tbody > tr > td:nth-child(4)"), 1),
            assignment = [].slice.call(document.querySelectorAll(".list > tbody > tr > td:nth-child(5)"), 1),
            grade = [].slice.call(document.querySelectorAll(".list > tbody > tr > td:nth-child(6)"), 1),
            comment = [].slice.call(document.querySelectorAll(".list > tbody > tr > td:nth-child(7)"), 1),
        ])

        let arr = []
        for (let i = 0; i < course.length; i++) {
            arr.push([
                mp[i].innerText,
                course[i].innerText.split("\n")[0],
                course[i].innerText.split("\n")[1],
                due[i].innerText.replaceAll("\n", " "),
                category[i].innerText,
                assignment[i].innerText,
                grade[i].innerText.replaceAll("\n", "\n"),
                comment[i].innerText,
            ])
        }

        return arr
    })
}









// get user object based on given variable containing username and password
function getUser(user){
    for (let e of Array.from(users.keys())){
        if (e.password === user.password && e.username === user.username)
            return users.get(e)
    }
    return undefined
}



// wait till some pages fully loaded
async function waitTillLoadPage(user){
    for (const e of [0, 1, 2]) {
        if (!user.pages[e].isLoaded){
            await user.pages[e].tab.waitForSelector(".list")
            await waitTillHTMLRendered(user.pages[e].tab, 1000)
            user.pages[e].isLoaded = true
        }
    }
}



// web-scrape and get grade data.
async function getGradeData(users){
    user = getUser(users)
    const page = user.pages[2].tab

    await page.reload()
    await waitTillHTMLRendered(page, 100)

    let arr = []

    for (let i of ["MP1", "MP2", "MP3", "MP4", "FG"]) {
        await page.goto(`https://students.sbschools.org/genesis/parents?tab1=studentdata&tab2=gradebook&tab3=weeklysummary&studentid=${users.username.substring(0, 8)}&action=form&mpToView=${i}`)
        await waitTillHTMLRendered(page)

        let mp = await page.evaluate(async () => {
            let course, teacher, grade, teacherEmail

            do {
                await Promise.all([
                    course = [].slice.call(document.querySelectorAll(".list > tbody > tr > td:nth-child(1)"), 1),
                    teacher = [].slice.call(document.querySelectorAll(".list > tbody > tr > td:nth-child(2)"), 1),
                    grade = [].slice.call(document.querySelectorAll(".list > tbody > tr > td:nth-child(3)"), 1),
                    teacherEmail = document.querySelectorAll(".list > tbody > tr > td:nth-child(2) > a")
                ])
            } while (!isSomeDataMissing([course, teacher, grade, teacherEmail]))

            function isSomeDataMissing(arr) {
                return arr.every((val, i, arr) => val.length === arr[0].length)
            }

            let mp = []
            for (let i = 0; i < course.length; i++) {
                mp.push([
                    course[i].innerText,
                    teacher[i].innerText.replaceAll("\n", "").replaceAll("Email: ", ""),
                    grade[i].innerText.replaceAll("\n", "").replaceAll("\t", "-"),
                    teacherEmail[i].getAttribute("href")
                ])
            }

            return mp
        })

        arr.push(mp)
    }

    return arr;
}





// convert image to base64 string.
function base64_encode(file) {
    let bitmap = fs.readFileSync(file);
    return new Buffer(bitmap).toString('base64');
}

// web-scrape and get profile data (like name, id, locker infos).
async function getProfileData(users){
    user = getUser(users)
    const page = user.pages[1].tab

    // await page.reload()
    await waitTillHTMLRendered(page, 100)

    const user_img = base64_encode(`${dataPATH}/images/user_${users.username.substring(0, 8)}.png`);

    let arr  = await page.evaluate(async (user_img) => {
        let data


        await Promise.all([
            data = document.querySelectorAll(".list:nth-child(1) > tbody > tr > td")
        ])
        // body > form:nth-child(9) > table > tbody > tr:nth-child(2) > td > table > tbody > tr > td:nth-child(2) > table:nth-child(1) > tbody > tr:nth-child(1) > td:nth-child(1)
        let arr = []
        for (let i = 0; i < data.length; i++) {
            arr.push(data[i].innerText)
        }


        arr = []
        arr.push({
            image: user_img,
            name: data[12].innerText,
            grade: data[13].innerText.split("\n")[1],
            id: data[14].innerText.split("|")[1]
                .split(":")[1]
                .trim(),
            counselor: data[3].innerText.split("\n")[1],
            locker: data[11].innerText,
            lunch: data[9].innerText
        })

        return arr
    }, user_img)
    console.log(arr)
    return arr
}



// wait till html fully loads
const waitTillHTMLRendered = async (page, cps, shouldCheck, timeout = 600000) => {
    const checkDurationMsecs = cps;
    const maxChecks = timeout / checkDurationMsecs;
    let lastHTMLSize = 0;
    let checkCounts = 1;
    let countStableSizeIterations = 0;
    const minStableSizeIterations = 3;

    while(checkCounts++ <= maxChecks){
        let html = await page.content();
        let currentHTMLSize = html.length;

        let bodyHTMLSize = await page.evaluate(() => document.body.innerHTML.length);

        console.log('last: ', lastHTMLSize, ' <> curr: ', currentHTMLSize, " body html size: ", bodyHTMLSize);

        if(lastHTMLSize != 0 && currentHTMLSize == lastHTMLSize)
            countStableSizeIterations++;
        else
            countStableSizeIterations = 0; //reset the counter

        if(countStableSizeIterations >= minStableSizeIterations) {
            console.log("Page rendered fully..");
            break;
        }

        lastHTMLSize = currentHTMLSize;
        await page.waitForTimeout(checkDurationMsecs);
    }
};


