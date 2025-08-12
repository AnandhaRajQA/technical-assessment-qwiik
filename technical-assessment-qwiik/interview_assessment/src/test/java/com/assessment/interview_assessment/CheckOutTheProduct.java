package com.assessment.interview_assessment;

import java.time.Duration;
import java.util.List;

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

public class CheckOutTheProduct {
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

    /**
     * Tries multiple locators one by one until it finds a visible and clickable element.
     * Throws RuntimeException if none found.
     */
    public WebElement findElementByMultipleLocators(WebDriver driver, WebDriverWait wait, By... locators) {
        for (By locator : locators) {
            try {
                WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
                if (element.isDisplayed()) {
                    return element;
                }
            } catch (Exception e) {
                // Ignore and try next locator
            }
        }
        throw new RuntimeException("None of the locators matched a clickable element.");
    }

    @Test
    public void PurchaseAProduct() throws InterruptedException {
        driver.get(baseUrl);

        // --- Login ---
        wait.until(ExpectedConditions.visibilityOf(storedata.username));
        storedata.username.sendKeys("standard_user");
        storedata.password.sendKeys("secret_sauce");
        storedata.Submit.click();

        wait.until(ExpectedConditions.urlContains("inventory.html"));

        // --- Apply "Price: High to Low" filter ---
        wait.until(ExpectedConditions.elementToBeClickable(ProductData.Filter));
        ProductData.Filter.click();

        WebElement highToLowOption = driver.findElement(By.xpath("//option[@value='hilo']"));
        highToLowOption.click();

        // Wait for prices to update after filtering
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("inventory_item_price")));

        // --- Robust retry logic to find and click Add to Cart button ---
        WebElement addToCartBtn = null;
        int attempts = 0;
        while (attempts < 3) {
            try {
                addToCartBtn = findElementByMultipleLocators(driver, wait,
                    By.id("add-to-cart-sauce-labs-fleece-jacket"),
                    By.className("btn_primary"),
                    By.xpath("//button[@id='add-to-cart-sauce-labs-fleece-jacket']"),
                    By.xpath("(//button[@id='add-to-cart-sauce-labs-fleece-jacket'])[1]"),
                    By.cssSelector("#add-to-cart-sauce-labs-fleece-jacket"),
                    By.name("add-to-cart-sauce-labs-fleece-jacket")
                );

                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", addToCartBtn);

                addToCartBtn.click();
                break;  // success: exit loop

            } catch (org.openqa.selenium.StaleElementReferenceException e) {
                System.out.println("Stale element caught, retrying...");
            } catch (Exception e) {
                System.out.println("Click attempt failed: " + e.getMessage());
                // fallback to JS click if normal click fails
                try {
                    if (addToCartBtn != null) {
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addToCartBtn);
                        break; // success: exit loop
                    }
                } catch (Exception ex) {
                    System.out.println("JS click also failed: " + ex.getMessage());
                }
            }
            Thread.sleep(1000);  // wait a bit before retrying
            attempts++;
        }

        if (addToCartBtn == null) {
            throw new RuntimeException("Failed to click Add to Cart button after retries.");
        }

        // Wait for cart badge to appear and update
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("shopping_cart_badge")));
        WebElement cartBadge = driver.findElement(By.className("shopping_cart_badge"));
        wait.until(ExpectedConditions.textToBePresentInElement(cartBadge, "1"));

        // --- Go to Cart page ---
        wait.until(ExpectedConditions.elementToBeClickable(ProductData.Cart));
        ProductData.Cart.click();

        wait.until(ExpectedConditions.urlContains("cart.html"));

        // --- Verify product in cart ---
        List<WebElement> cartItems = driver.findElements(By.className("cart_item"));
        Assert.assertTrue(cartItems.size() > 0, "Cart is empty after adding product.");

        String expectedProductName = "Sauce Labs Fleece Jacket";
        String cartProductName = cartItems.get(0).findElement(By.className("inventory_item_name")).getText();
        Assert.assertEquals(cartProductName, expectedProductName, "Product name in cart does not match.");

        System.out.println("✅ Product added to cart and verified in cart page.");

        // --- Checkout ---
        ProductData.Checkout.click();

        wait.until(ExpectedConditions.visibilityOf(ProductData.FirstName));
        ProductData.FirstName.sendKeys("Anand");
        ProductData.Lastname.sendKeys("Raj");
        ProductData.ZipCode.sendKeys("658745");
        ProductData.ContinueAddressPage.click();

        wait.until(ExpectedConditions.visibilityOf(ProductData.CheckoutOverviewPage));

        String overviewProductName = ProductData.OverviewProductName.getText();
        Assert.assertEquals(overviewProductName, expectedProductName, "Product name on checkout overview does not match.");

        String overviewProductPrice = ProductData.OverviewProductPrice.getText();
        String expectedPrice = "$49.99";
        Assert.assertEquals(overviewProductPrice, expectedPrice, "Product price on checkout overview does not match.");

        System.out.println("✅ Verified product name and price on checkout overview page");

        ProductData.Finish.click();

        wait.until(ExpectedConditions.elementToBeClickable(ProductData.BackHome));
        ProductData.BackHome.click();

        Thread.sleep(2000);

        wait.until(ExpectedConditions.elementToBeClickable(storedata.OpenMenu));
        System.out.println("OpenMenu displayed? " + storedata.OpenMenu.isDisplayed());

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", storedata.OpenMenu);

        wait.until(ExpectedConditions.elementToBeClickable(storedata.Logout));
        System.out.println("Logout displayed? " + storedata.Logout.isDisplayed());

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", storedata.Logout);

        System.out.println("✅ Logout successful");
    }
}
