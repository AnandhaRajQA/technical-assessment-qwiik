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

public class AddTocart_In_ProductdetailsPage {
    WebDriver driver;
    private final String baseUrl = "https://www.saucedemo.com/";
    private LoginData storedata;
    private Product Productdata;
    private WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Initialize POM classes
        storedata = new LoginData(driver);
        Productdata = new Product(driver);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test(priority = 1)
    public void AddToCart_IN_ProductDetailsPage() throws InterruptedException {
        driver.get(baseUrl);

        // Login
        wait.until(ExpectedConditions.visibilityOf(storedata.username));
        storedata.username.sendKeys("standard_user");
        storedata.password.sendKeys("secret_sauce");
        storedata.Submit.click();

        // Wait until redirected to products listing page
        wait.until(ExpectedConditions.urlContains("inventory.html"));

        // --- FIX: Correct locator for product details link ---
        // You mistakenly used driver.findElement(By.id("//div[...]")) which is invalid
        // Use XPath or another appropriate locator for clicking the product name to open details
        WebElement productDetailsLink = driver.findElement(By.xpath("//div[normalize-space()='Sauce Labs Backpack']"));
        productDetailsLink.click();

        // Wait until product details page is loaded by checking URL contains product id (usually "id=4" for backpack)
        wait.until(ExpectedConditions.urlContains("id=4"));

 

        // Get initial cart count (before adding)
        int initialCount = 0;
        try {
            String countText = Productdata.CartCountIcon.getText();
            initialCount = Integer.parseInt(countText);
        } catch (Exception e) {
            System.out.println("ℹ No items in cart initially.");
        }

        // Click Add to Cart button on product details page
        wait.until(ExpectedConditions.elementToBeClickable(Productdata.AddToCart));
        Productdata.AddToCart.click();

        // Wait until Add to Cart button changes to Remove button (check visibility)
        wait.until(ExpectedConditions.visibilityOf(Productdata.AddToCartRemove));

        // Verify button text changed to Remove
        Assert.assertEquals(Productdata.AddToCartRemove.getText().toLowerCase(), "remove", "Button text did not change to Remove");
        System.out.println("✅ Button text changed to Remove");

        // Wait until cart badge updates count to initialCount + 1
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
            By.className("shopping_cart_badge"),
            String.valueOf(initialCount + 1)
        ));

        // Verify cart count incremented
        int updatedCount = Integer.parseInt(Productdata.CartCountIcon.getText());
        Assert.assertEquals(updatedCount, initialCount + 1, "Cart count did not increase as expected");
        System.out.println("✅ Cart count increased from " + initialCount + " to " + updatedCount);
        
        
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
