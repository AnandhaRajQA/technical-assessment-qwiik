package com.assessment.interview_assessment;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class Product {
	
	WebDriver driver;
	
	@FindBy(xpath="//div[@id='shopping_cart_container']")
	WebElement Cart;
	
	@FindBy(xpath="//select[@class='product_sort_container']")
	WebElement Filter;
	
	@FindBy(xpath="//button[@id='add-to-cart']")
	WebElement AddToCart;
	
	@FindBy(className = "shopping_cart_badge")
	WebElement CartCountIcon;
	
	@FindBy(xpath="//button[@id='remove']")
	WebElement AddToCartRemove;
	
	@FindBy(xpath="//button[@id='checkout']")
	WebElement Checkout;
	
	@FindBy(xpath="//button[text()='Open Menu']")
	WebElement OpenMenu;
	
	@FindBy(xpath="//a[@id='logout_sidebar_link']")
	WebElement Logout;
	
	@FindBy(xpath="//a[@id='reset_sidebar_link']")
	WebElement ResetAppState;
	
	@FindBy(xpath="//a[@id='inventory_sidebar_link']")
	WebElement AllItem;
	
	
	
	@FindBy(css = ".cart_item .inventory_item_name")
	WebElement OverviewProductName;

	@FindBy(css = ".cart_item .inventory_item_price")
	WebElement OverviewProductPrice;
	
	
	@FindBy(xpath="//button[@id='continue-shopping']")
	WebElement ContinueShopping;
	
	@FindBy(xpath="//input[@id='continue']")
	WebElement ContinueAddressPage;
	
	@FindBy(xpath="//button[@id='cancel']")
	WebElement CancelAddresspage;
	
	@FindBy(xpath="//button[text()='Finish']")
	WebElement Finish;
	
	@FindBy(xpath="//button[@id='back-to-products']")
	WebElement BackHome;
	
	@FindBy(id = "checkout_summary_container")
	WebElement CheckoutOverviewPage;

	
	
	//address
	
	@FindBy(xpath="//input[@id='first-name']")
	WebElement FirstName;
	
	@FindBy(xpath="//input[@id='last-name']")
	WebElement Lastname;
	
	@FindBy(xpath="//input[@id='postal-code']")
	WebElement ZipCode;
	
	public Product(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
				
	}
	
}
