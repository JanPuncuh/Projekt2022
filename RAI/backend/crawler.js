/*const axios = require('axios');
const cheerio = require('cheerio');


const getPostTitles = async () => {
	try {
		const { data } = await axios.get(
			'https://www.promet.si/sl/stevci-prometa'
			
		);
		const $ = cheerio.load(data);
		const postTitles = [];
		return data;

		$('div').each((_idx, el) => {
			const postTitle = $(el).text()
			postTitles.push(postTitle)
		});

		return postTitles;
	} catch (error) {
		throw error;
	}
};

getPostTitles()
.then((postTitles) => console.log(postTitles));



*/


const puppeteer = require('puppeteer');

(async function scrape() {
    const browser = await puppeteer.launch({ headless: false });

    const page = await browser.newPage();
   // await page.goto('https://quotes.toscrape.com/search.aspx');
	await page.goto('https://www.promet.si/sl/dogodki');
    //await page.waitForSelector('#author');
    //await page.select('#author', 'Albert Einstein');

    //await page.waitForSelector('#tag');
    //await page.select('#tag', 'learning');

    //await page.click('.btn');
    //await page.waitForSelector('.quote');

    // extracting information from code
	//await new Promise(r => setTimeout(r, 5000));
    let quotes = await page.evaluate(() => {

        let quotesElement = document.body.querySelectorAll('.panelwrapper flexi');
        let quotes = Object.values(quotesElement).map(x => {

            return {
                author: x.querySelector('.accordion__title moduletitle').textContent ?? null,
                quote: x.querySelector('.content').textContent ?? null,
                tag: x.querySelector('.tag').textContent ?? null,

            }
        });

        return quotes;

    });

    // logging results
    console.log(quotes);
    await browser.close();

})();
