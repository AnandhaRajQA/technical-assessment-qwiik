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

public class AddToCard {
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

    @Test(priority = 1)
    public void testAddSingleProductToCart() throws InterruptedException {
        driver.get(baseUrl);

        // Login
        wait.until(ExpectedConditions.visibilityOf(storedata.username));
        storedata.username.sendKeys("standard_user");
        storedata.password.sendKeys("secret_sauce");
        storedata.Submit.click();

        wait.until(ExpectedConditions.urlContains("inventory.html"));

        int initialCount = 0;
        try {
            String countText = ProductData.Cart.getText();
            initialCount = Integer.parseInt(countText);
        } catch (Exception e) {
            System.out.println("ℹ No items in cart initially.");
        }

        // Add single product
        WebElement firstAddToCartButton = driver.findElement(By.id("add-to-cart-sauce-labs-backpack"));
        firstAddToCartButton.click();

        // Wait for cart count to update
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
            By.className("shopping_cart_badge"), String.valueOf(initialCount + 1)
        ));

        // Verify cart count incremented by 1
        WebElement updatedCartBadge = driver.findElement(By.className("shopping_cart_badge"));
        int updatedCount = Integer.parseInt(updatedCartBadge.getText());

        Assert.assertEquals(updatedCount, initialCount + 1, "Cart count did not increase as expected");
        System.out.println("✅ Cart count increased from " + initialCount + " to " + updatedCount);
        
        //Logout websites
        storedata.OpenMenu.click();
        storedata.Logout.click();
    }

    @Test(priority = 2)
    public void testAddMultipleProductsToCart() throws InterruptedException {
        driver.get(baseUrl);

        // Login
        wait.until(ExpectedConditions.visibilityOf(storedata.username));
        storedata.username.sendKeys("standard_user");
        storedata.password.sendKeys("secret_sauce");
        storedata.Submit.click();

        wait.until(ExpectedConditions.urlContains("inventory.html"));

        int initialCount = 0;
        try {
            String countText = ProductData.Cart.getText();
            initialCount = Integer.parseInt(countText);
        } catch (Exception e) {
            System.out.println("ℹ No items in cart initially.");
        }

        // List of product IDs to add
        String[] productIds = {
            "add-to-cart-sauce-labs-backpack",
            "add-to-cart-sauce-labs-bike-light",
            "add-to-cart-sauce-labs-bolt-t-shirt"
        };

        for (String productId : productIds) {
            WebElement addButton = driver.findElement(By.id(productId));
            addButton.click();
            Thread.sleep(3000);  // 
        }

        int expectedCount = initialCount + productIds.length;

        // Wait for cart badge update
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
            By.className("shopping_cart_badge"), String.valueOf(expectedCount)
        ));

        WebElement updatedCartBadge = driver.findElement(By.className("shopping_cart_badge"));
        int updatedCount = Integer.parseInt(updatedCartBadge.getText());

        Assert.assertEquals(updatedCount, expectedCount, "Cart count did not increase as expected");
        System.out.println("✅ Cart count increased from " + initialCount + " to " + updatedCount);
        //Logout websites
        storedata.OpenMenu.click();
        storedata.Logout.click();
        
    }

        
       
        
    
}

