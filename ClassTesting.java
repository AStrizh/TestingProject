package org.example;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ClassTesting {
    private WebDriver driver;
    private String mainWindow;
    private static final String USER = "astrizhevskiy4134@eagle.fgcu.edu";
    private static final String PASS = "L5KYQB!W5Gbywri";


    //initiate driver
    @Test(priority = 1)
    void initiate_driver() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\abstr\\Documents\\FGCU\\Spring2024\\Testing\\Drivers\\ChromeDriver\\chromedriver.exe");
        driver = new ChromeDriver();
    }

    //This test logs into The Register by entering texts into textboxes and submitting information.
    @Test(priority = 2)
    void register_test() throws InterruptedException {
        driver.get("https://www.theregister.com/");
        driver.manage().window().maximize();

        //set main window
        mainWindow = driver.getWindowHandle();
        Thread.sleep(1000);
    }

    @Test(priority = 3)
    void search_test() throws InterruptedException {
        driver.findElement(By.cssSelector(".nav_search.topnav_elem")).click();

        //Types in the search term "testing" and submits the search
        Thread.sleep(2000);
        driver.findElement(By.id("q")).sendKeys("testing");
        driver.findElement(By.id("q")).submit();

        //Gets the current URL
        String current = driver.getCurrentUrl();
        System.out.println("Current URL: " + current);

        //Gets the title
        String title = driver.getTitle();
        System.out.println("Title Name: " + title);

        //Prints the total number of elements
        List<WebElement> elements = driver.findElements(By.tagName("a"));
        System.out.println("Number of Web Elements: " + elements.size());

        System.out.println("Web Elements listed below: ");
        System.out.println("\n");
        System.out.println("----------------------------------------------------------");

        //Prints the names of all the elements
        //Blank check is necessary because there were a lot of elements that just return blank lines
        for (WebElement l:elements)
        {
            if(!isEmptyString(l.getText()) )
                System.out.println(l.getText());
        }

        Thread.sleep(2000);

        driver.findElement(By.className("story_link")).click();
        Thread.sleep(3000);
    }

    @Test(priority = 4)
    void login_test() throws InterruptedException {
        //go to log in link
        driver.findElement(By.id("mob_user_link")).click();
        //enter username
        driver.findElement(By.id("email")).sendKeys(USER);
        Thread.sleep(1000);
        //enter password
        driver.findElement(By.name("password")).sendKeys(PASS);
        driver.findElement(By.name("login")).click();
        Thread.sleep(1000);
    }

    @Test(priority = 5)
    void open_headline() throws InterruptedException {

        List<WebElement> elements = driver.findElements(By.cssSelector("#index_page > div"));
        WebElement link = elements.get(0).findElements(By.tagName("a")).get(0);
        System.out.println(link.getAttribute("href"));
        link.click();

        Thread.sleep(1000);
        System.out.println(driver.findElement(By.className("header_right")).getText());
    }

    
    @Test(priority = 6)
    void open_article() throws InterruptedException {
        List<WebElement> links = driver.findElements(By.tagName("a"));
        int count = 0;
        for (WebElement link : links) {
            System.out.println(links.indexOf(link));
            System.out.println(link.getAttribute("href"));
        }
        links.get(76).click();
        Thread.sleep(1000);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,1000)", "");
        Thread.sleep(1000);
        js.executeScript("window.scrollBy(0,1000)", "");
        Thread.sleep(1000);
        js.executeScript("window.scrollBy(0,1000)", "");
        Thread.sleep(1000);
        js.executeScript("window.scrollBy(0,-3000)", "");
    }

     @Test(priority = 7)
    void comment_testing() throws InterruptedException {
        String comments = driver.findElement(By.className("comment_count")).getAttribute("href");
        driver.get(comments);
        Thread.sleep(1000);
        driver.findElement(By.linkText("Post your comment")).click();
        Thread.sleep(1000);
        driver.findElement(By.id("title")).sendKeys("My Comment");
        Thread.sleep(1000);
        driver.findElement(By.id("body")).sendKeys("This was interesting!");
        Thread.sleep(1000);
        //driver.findElement(By.name("post")).click();
        Thread.sleep(10000);
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
