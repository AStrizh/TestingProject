package org.example;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClassTesting {
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

    @Test(priority = 6)
    void login_test() throws InterruptedException {
        //go to log in link
        driver.findElement(By.id("mob_user_link")).click();
        //enter username
        driver.findElement(By.id("email")).sendKeys(USER);
        Thread.sleep(1000);
        //enter password
        driver.findElement(By.name("password")).sendKeys(PASS);
        driver.findElement(By.name("login")).click();
        Thread.sleep(500);

        String toastResponse = driver.findElement(By.className("toast_content")).getText();
        Assert.assertTrue(toastResponse.contains("Thank you"));
    }


    @Test(priority = 7)
    void open_article() throws InterruptedException {
        driver.get("https://www.theregister.com/");
        List<WebElement> links = driver.findElements(By.tagName("a"));
        ArrayList<String> archived_links = new ArrayList();
        for (WebElement link : links) {
            archived_links.add(link.getAttribute("href"));
            System.out.println(links.indexOf(link));
            System.out.println(link.getAttribute("href"));
        }
        int attempt = 83;
        links.get(attempt).click();
        Thread.sleep(2000);

        boolean comments_exist = false;
        while (!comments_exist) {
            try {
                driver.findElement(By.tagName("strong"));
                comments_exist = true;
            } catch (Exception e) {
                System.out.println("Failed to find comments.");
                attempt += 1;
                System.out.println(attempt);
                driver.get(archived_links.get(attempt));
                Thread.sleep(2000);
            }
        }

        Thread.sleep(2000);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,1000)", "");
        Thread.sleep(1000);
        js.executeScript("window.scrollBy(0,1000)", "");
        Thread.sleep(1000);
        js.executeScript("window.scrollBy(0,1000)", "");
        Thread.sleep(1000);
        js.executeScript("window.scrollBy(0,-3000)", "");
    }

    @Test(priority = 8)
    void comment_testing() throws InterruptedException {
        String comments = driver.findElement(By.className("comment_count")).getAttribute("href");
        driver.get(comments);
        driver.findElement(By.className("up")).click();
        Thread.sleep(2000);
        driver.findElement(By.linkText("Reply")).click();
        Thread.sleep(2000);
        driver.findElement(By.id("body")).sendKeys("I agree!");
        Thread.sleep(2000);
        //driver.findElement(By.name("post")).click();
        Thread.sleep(2000);
        driver.get(comments);
        Thread.sleep(2000);
        driver.findElement(By.linkText("Post your comment")).click();
        Thread.sleep(2000);
        driver.findElement(By.id("title")).sendKeys("My Comment");
        Thread.sleep(2000);
        driver.findElement(By.id("body")).sendKeys("This was interesting!");
        Thread.sleep(2000);
        //driver.findElement(By.name("post")).click();
        Thread.sleep(2000);
        driver.get(comments);
        Thread.sleep(2000);
    }

    @Test(priority = 9)
    void account_test() throws InterruptedException {
        //go to log in link
        driver.get(URL);
        Thread.sleep(1000);
        driver.findElement(By.id("mob_user_link")).click();

        WebElement element;
        String testString = "";

        //My Details
        testString = "My Details";
        element = driver.findElement(By.xpath("//*[@id=\"settings_tab_nav\"]/li[1]/input"));
        element.click();
        Thread.sleep(1000);
        WebElement detailsHeader = driver.findElement(By.cssSelector(".right_account h3"));
        String details = detailsHeader.getText();
        System.out.println(details);
        Assert.assertEquals(details,testString);

        //Newsletters
        testString = "Newsletters";
        element = driver.findElement(By.xpath("//*[@id=\"settings_tab_nav\"]/li[2]/input"));
        element.click();
        Thread.sleep(1000);
        WebElement newsHeader = driver.findElement(By.cssSelector(".right_account h3"));
        String newsHeaderText = newsHeader.getText();
        Assert.assertEquals(newsHeaderText,testString);

        //My Comments
        testString = "My Comments";
        element = driver.findElement(By.xpath("//*[@id=\"settings_tab_nav\"]/li[3]/input"));
        element.click();
        Thread.sleep(1000);
        WebElement commHeader = driver.findElement(By.cssSelector(".right_account h3"));
        String commHeaderText = commHeader.getText();
        Assert.assertEquals(commHeaderText,testString);

        //My Settings
        testString = "My Settings";
        element = driver.findElement(By.xpath("//*[@id=\"settings_tab_nav\"]/li[4]/input"));
        element.click();
        Thread.sleep(1000);
        WebElement settHeader = driver.findElement(By.cssSelector(".right_account h3"));
        String settHeaderText = settHeader.getText();
        Assert.assertEquals(settHeaderText,testString);

        //My Alerts
        testString = "My Alerts";
        element = driver.findElement(By.xpath("//*[@id=\"settings_tab_nav\"]/li[5]/input"));
        element.click();
        Thread.sleep(1000);
        WebElement alertHeader = driver.findElement(By.cssSelector(".right_account h3"));
        String alertHeaderText = alertHeader.getText();
        Assert.assertEquals(alertHeaderText,testString);

        //Change Password
        testString = "Change Password";
        element = driver.findElement(By.xpath("//*[@id=\"settings_tab_nav\"]/li[6]/input"));
        element.click();
        Thread.sleep(1000);
        WebElement passHeader = driver.findElement(By.cssSelector(".right_account h3"));
        String passHeaderText = passHeader.getText();
        Assert.assertEquals(passHeaderText,testString);

        //Delete Account
        testString = "Delete account";
        element = driver.findElement(By.xpath("//*[@id=\"settings_tab_nav\"]/li[8]/input"));
        element.click();
        Thread.sleep(1000);
        WebElement deleteHeader = driver.findElement(By.cssSelector(".right_account h3"));
        String deleteHeaderText = deleteHeader.getText();
        Assert.assertEquals(deleteHeaderText,testString);

        Thread.sleep(1000);
    }

    //close web browser
    @Test(priority = 50)
    void close_driver() {
        driver.quit();
    }

    public static boolean isEmptyString(String string) {
        return string == null || string.isEmpty();
    }
}
