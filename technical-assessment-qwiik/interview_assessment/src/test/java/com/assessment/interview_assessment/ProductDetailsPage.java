package com.assessment.interview_assessment;

import java.time.Duration;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProductDetailsPage {
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

        // Initialize POM classes
        storedata = new LoginData(driver);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test(priority = 1)
    public void ProductDetailsPage() throws InterruptedException {
        driver.get(baseUrl);

        // Login
        wait.until(ExpectedConditions.visibilityOf(storedata.username));
        storedata.username.sendKeys("standard_user");
        storedata.password.sendKeys("secret_sauce");
        storedata.Submit.click();

        wait.until(ExpectedConditions.urlContains("inventory.html"));
        

     // Click product details link (
     WebElement productDetailsLink = driver.findElement(By.xpath("//div[normalize-space()='Sauce Labs Backpack']"));
     productDetailsLink.click();

     // Wait until URL contains id=4
     wait.until(ExpectedConditions.urlContains("id=4"));

     // Get current URL
     String currentUrl = driver.getCurrentUrl();

     // Assert URL contains expected product id
     Assert.assertTrue(currentUrl.contains("id=4"), "URL does not contain expected product ID");

     // print confirmation
     System.out.println("✅ Navigated to product details page with ID=4, URL: " + currentUrl);
     
     
  // Verify "Back to Products" button is visible
     WebElement backToProductsBtn = wait.until(
         ExpectedConditions.visibilityOfElementLocated(By.id("back-to-products"))
     );
     Assert.assertTrue(backToProductsBtn.isDisplayed(), "'Back to Products' button is not visible");
     System.out.println("✅ 'Back to Products' button is visible");

     // (Optional) Click "Back to Products" button
     backToProductsBtn.click();
     
     Thread.sleep(1000);

     // Wait until URL goes back to product listing page (usually inventory.html)
     wait.until(ExpectedConditions.urlContains("inventory.html"));

     // Verify you are back on products listing page
     String backUrl = driver.getCurrentUrl();
     Assert.assertTrue(backUrl.contains("inventory.html"), "Did not navigate back to products listing page");
     System.out.println("✅ Returned to products listing page");

  // Wait and verify OpenMenu presence
     wait.until(ExpectedConditions.elementToBeClickable(storedata.OpenMenu));
     System.out.println("OpenMenu displayed? " + storedata.OpenMenu.isDisplayed());

     // Use JS click to avoid interception issues
     ((JavascriptExecutor) driver).executeScript("arguments[0].click();", storedata.OpenMenu);

     // Wait and verify Logout presence
     wait.until(ExpectedConditions.elementToBeClickable(storedata.Logout));
     System.out.println("Logout displayed? " + storedata.Logout.isDisplayed());

     // JS click logout
     ((JavascriptExecutor) driver).executeScript("arguments[0].click();", storedata.Logout);

     System.out.println("✅ Logout successful");
     
    }
}