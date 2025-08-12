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

public class ProductListing {
    WebDriver driver;
    private final String baseUrl = "https://www.saucedemo.com/";
    private LoginData storedata;
    private Product ProductData;
    private WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Initialize POM classes
        storedata = new LoginData(driver);
        ProductData = new Product(driver);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void ListingProducts() throws InterruptedException {
        driver.get(baseUrl);

        // Wait and perform login
        wait.until(ExpectedConditions.visibilityOf(storedata.username));
        storedata.username.sendKeys("standard_user");
        storedata.password.sendKeys("secret_sauce");
        storedata.Submit.click();

     // Wait until the Products label is visible after login
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='title']")));

        WebElement productsLabel = driver.findElement(By.xpath("//span[@class='title']"));

        if (productsLabel.isDisplayed()) {
            System.out.println("✅ Login successful - 'Products Listing page is displayed");
        } else {
            System.out.println("❌ Login failed - 'Products Listing page is not displayed");
        }

        // Assert based on the Products label visibility
        Assert.assertTrue(productsLabel.isDisplayed(), "Login was not successful - Products Listing page is missing");

        // Verify Sidebar Menu Icon
        wait.until(ExpectedConditions.visibilityOf(ProductData.OpenMenu));
        Assert.assertTrue(ProductData.OpenMenu.isDisplayed(), "Sidebar menu icon is not visible");
        System.out.println("✅ Sidebar menu icon is visible");

        // Verify Cart Icon
        wait.until(ExpectedConditions.visibilityOf(ProductData.Cart));
        Assert.assertTrue(ProductData.Cart.isDisplayed(), "Cart icon is not visible");
        System.out.println("✅ Cart icon is visible");

        // Verify Filter Icon
        wait.until(ExpectedConditions.visibilityOf(ProductData.Filter));
        Assert.assertTrue(ProductData.Filter.isDisplayed(), "Filter icon is not visible");
        System.out.println("✅ Filter icon is visible");

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
