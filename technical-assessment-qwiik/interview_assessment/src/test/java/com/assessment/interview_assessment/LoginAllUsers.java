package com.assessment.interview_assessment;

import java.time.Duration;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginAllUsers {
    WebDriver driver;
    private final String baseUrl = "https://www.saucedemo.com/";
    private LoginData storedata;
    private WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
       
    	
    	driver = new ChromeDriver();


        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Initialize POM
        storedata = new LoginData(driver);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testValidLogin() throws InterruptedException {
        driver.get(baseUrl);

        // Wait and perform login
        wait.until(ExpectedConditions.visibilityOf(storedata.username));
        storedata.username.sendKeys("standard_user");
        storedata.password.sendKeys("secret_sauce");
        storedata.Submit.click();

     // Verify successful login
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("inventory.html"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("inventory_list")));

        if (driver.getCurrentUrl().contains("inventory.html")) {
            System.out.println("✅ Login successful - URL contains inventory.html");
        } else {
            System.out.println("❌ Login failed - URL does not contain inventory.html");
        }

        Assert.assertTrue(driver.getCurrentUrl().contains("inventory.html"), "Login was not successful");

        
        Thread.sleep(3000);
        
        
        storedata = new LoginData(driver);

        // Wait until the Open Menu button is clickable
        wait.until(ExpectedConditions.elementToBeClickable(storedata.OpenMenu));
        storedata.OpenMenu.click();

        // Wait until Logout is clickable
        wait.until(ExpectedConditions.elementToBeClickable(storedata.Logout));
        storedata.Logout.click();

        System.out.println("✅ Logout successful");
    }

    @Test 
    public void testInvalidLogin() {
        driver.get(baseUrl);

        // Wait and perform login with invalid credentials
        wait.until(ExpectedConditions.visibilityOf(storedata.username));
        storedata.username.sendKeys("standard_user");
        storedata.password.sendKeys("21212");
        storedata.Submit.click();

        // Wait until error message is visible
        WebElement errorMessage = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h3[contains(text(),'Epic sadface: Username and password do not match a')]")
            )
        );

        // Verify and print message
        if (errorMessage.isDisplayed()) {
            System.out.println("Invalid verification completed");
        } else {
            System.out.println("Invalid verification failed");
    }
       
}
    @Test 
    public void locked_out_user() {
    	
    	driver.get(baseUrl);
    	
    	 wait.until(ExpectedConditions.visibilityOf(storedata.username));
         storedata.username.sendKeys("locked_out_user");
         storedata.password.sendKeys("secret_sauce");
         storedata.Submit.click();
         
         
         // Wait until error message is visible
         WebElement errorMessage = wait.until(
             ExpectedConditions.visibilityOfElementLocated(
                 By.xpath("//h3[contains(text(),'Epic sadface: Sorry, this user has been locked out')]")
             )
         );

         // Verify and print message
         if (errorMessage.isDisplayed()) {
             System.out.println("LockOut User verification completed");
         } else {
             System.out.println("LockOut User verification failed");
             
         }
    	
    }
         
         @Test
         public void validLoginPerformanceTest() {
             driver.get(baseUrl);

            
             // Start timer
            

             storedata.username.sendKeys("performance_glitch_user");
             storedata.password.sendKeys("secret_sauce");
             storedata.Submit.click();
             
             long startTime = System.currentTimeMillis();

             // Wait until an element on the home page is visible
             WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
             wait.until(ExpectedConditions.urlContains("inventory.html"));

             // Stop timer
             long endTime = System.currentTimeMillis();

             // Calculate load time
             long loadTime = endTime - startTime;

             // Print result
             System.out.println("[PERFORMANCE TEST] Page Load Time: " + loadTime + " ms");

             // Verify performance is within acceptable limit 
             Assert.assertTrue(loadTime < 3000, "Page load took too long: " + loadTime + " ms");
         }
}
