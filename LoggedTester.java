package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;


public class LoggedTester {

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

}
