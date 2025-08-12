package com.assessment.interview_assessment;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

public class SortProduct {
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
    public void ProductFilterFunction() throws InterruptedException {
        driver.get(baseUrl);

        // Login
        wait.until(ExpectedConditions.visibilityOf(storedata.username));
        storedata.username.sendKeys("standard_user");
        storedata.password.sendKeys("secret_sauce");
        storedata.Submit.click();

        wait.until(ExpectedConditions.urlContains("inventory.html"));
        
        
        Thread.sleep(3000);

        // --- PRINT PRICES BEFORE FILTER ---
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("inventory_item_price")));
        List<WebElement> priceElementsBefore = driver.findElements(By.className("inventory_item_price"));
        List<Double> pricesBefore = new ArrayList<>();

        System.out.println("Prices BEFORE applying filter:");
        for (WebElement priceElem : priceElementsBefore) {
            String priceText = priceElem.getText().replace("$", "").trim();
            double price = Double.parseDouble(priceText);
            pricesBefore.add(price);
            System.out.println(price);
        }

        // --- APPLY FILTER ---
        wait.until(ExpectedConditions.elementToBeClickable(ProductData.Filter));
        ProductData.Filter.click();

        // Select option "Price (low to high)"
        WebElement lowToHighOption = driver.findElement(By.xpath("//option[@value='lohi']"));
        lowToHighOption.click();

        // Wait until product prices are visible (updated)
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("inventory_item_price")));

        // --- PRINT PRICES AFTER FILTER ---
        List<WebElement> priceElementsAfter = driver.findElements(By.className("inventory_item_price"));
        List<Double> pricesAfter = new ArrayList<>();

        System.out.println("Prices AFTER applying filter:");
        for (WebElement priceElem : priceElementsAfter) {
            String priceText = priceElem.getText().replace("$", "").trim();
            double price = Double.parseDouble(priceText);
            pricesAfter.add(price);
            System.out.println(price);
        }

        // Verify pricesAfter list is sorted ascending
        List<Double> sortedPrices = new ArrayList<>(pricesAfter);
        Collections.sort(sortedPrices);

        Assert.assertEquals(pricesAfter, sortedPrices, "Products are NOT sorted by price low to high!");
        System.out.println("✅ Products are correctly sorted by price (Low to High).");
    }

    }

