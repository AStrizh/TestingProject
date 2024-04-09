package org.example;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class GuestTester {
    private WebDriver driver;
    private String mainWindow;
    private static final String USER = "astrizhevskiy4134@eagle.fgcu.edu";
    private static final String PASS = "L5KYQB!W5Gbywri";

    private static final String URL = "https://www.theregister.com/";

    private static final String SSDIRECTORY = "";

    @BeforeClass
    void initiate_driver() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\abstr\\Documents\\FGCU\\Spring2024\\Testing\\Drivers\\ChromeDriver\\chromedriver.exe");
        driver = new ChromeDriver();

        driver.get(URL);
        driver.manage().window().maximize();

        //set main window
        mainWindow = driver.getWindowHandle();
    }

    @Test(priority = 2)
    void open_headline() throws InterruptedException {

        List<WebElement> elements = driver.findElements(By.cssSelector("#index_page > div"));
        WebElement link = elements.get(0).findElements(By.tagName("a")).get(0);
        System.out.println(link.getAttribute("href"));
        link.click();

        Thread.sleep(1000);
        System.out.println(driver.findElement(By.className("header_right")).getText());
    }


    @Test(priority = 3)
    void inspect_author() throws InterruptedException {

        WebElement authorLink = driver.findElement(By.className("byline"));
        System.out.println(authorLink.getText());
        String authorName = authorLink.getText();
        System.out.println(authorLink.getAttribute("href"));

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.open(arguments[0]);", authorLink.getAttribute("href"));


        for (String windowHandle : driver.getWindowHandles()){
            if (!mainWindow.contentEquals((windowHandle))) {
                driver.switchTo().window(windowHandle);
            }
        }

        Thread.sleep(1000);
        System.out.println("Title :" + driver.getTitle() + " URL: " + driver.getCurrentUrl());

        Assert.assertTrue(driver.getTitle().contains(authorName));

        try{
            System.out.println(driver.findElement(By.className("columnist_blurb")).getText());
        }catch (NoSuchElementException e){
            System.out.println("This author does not have a bio.");
        }


        WebElement authorHeadlines = driver.findElement(By.className("headlines"));

        // Find all article elements within authorHeadlines
        List<WebElement> articles = authorHeadlines.findElements(By.tagName("article"));

        if(articles.size()>5){
            for(int i = 0; i<5; i++){
                String articleText = articles.get(i).findElement(By.className("article_text_elements")).getText();
                System.out.println(articleText);
            }
        }
        else {
            for (WebElement article : articles) {
                // For each article, find the div with class 'article_text_elements' and get its text
                String articleText = article.findElement(By.className("article_text_elements")).getText();
                System.out.println(articleText);
            }
        }

        driver.close();
        //switch focus back to main window
        driver.switchTo().window(mainWindow);
    }

    @Test(priority = 4)
    void inspect_commenter() throws InterruptedException {

        Thread.sleep(1000);
        WebElement commentsLink = driver.findElement(By.className("comment_count"));
        System.out.println(commentsLink.getText());
        System.out.println(commentsLink.getAttribute("href"));

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.open(arguments[0]);", commentsLink.getAttribute("href"));


        for (String windowHandle : driver.getWindowHandles()){
            if (!mainWindow.contentEquals((windowHandle))) {
                driver.switchTo().window(windowHandle);
            }
        }

        String commentsHandle = driver.getWindowHandle();

        Thread.sleep(5000);
        System.out.println("Title :" + driver.getTitle() + " URL: " + driver.getCurrentUrl());

        WebElement commentAuthors = driver.findElements(By.className("author")).get(0);
        System.out.println(commentAuthors.getText());
        commentAuthors.click();

        if(driver.getWindowHandles().size() > 2){
            for (String windowHandle : driver.getWindowHandles()){
                if (!mainWindow.contentEquals((windowHandle))) {
                    driver.switchTo().window(windowHandle);
                }
            }
            driver.close();
        }

        driver.switchTo().window(commentsHandle);

        Thread.sleep(1000);
        WebElement comments = driver.findElements(By.className("content")).get(0);
        System.out.println(comments.getText());

        Thread.sleep(1000);
        driver.close();


        //switch focus back to main window
        driver.switchTo().window(mainWindow);
        Thread.sleep(1000);
    }


    @Test(priority = 5)
    void search_test() throws InterruptedException {
        driver.findElement(By.cssSelector(".nav_search.topnav_elem")).click();

        //Types in the search term "testing" and submits the search
        Thread.sleep(1000);
        driver.findElement(By.id("q")).sendKeys("testing");
        driver.findElement(By.id("q")).submit();

        //Gets the current URL
        String current = driver.getCurrentUrl();
        System.out.println("Current URL: " + current);

        //Gets the title
        String title = driver.getTitle();
        System.out.println("Title Name: " + title);

        Thread.sleep(1000);

        Assert.assertTrue(title.contains("Search results"));

        driver.findElement(By.className("story_link")).click();
        Thread.sleep(3000);
    }


    //close web browser
    @Test(priority = 50)
    void close_driver() {
        driver.quit();
    }

}
