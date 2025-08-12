package com.assessment.interview_assessment;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginData {
	
	WebDriver driver;
	
	@FindBy(xpath="//input[@id='user-name']")
	WebElement username;
	
	@FindBy(xpath="//input[@id='password']")
	WebElement password;
	
	@FindBy(xpath="//input[@id='login-button']")
	WebElement Submit;
	
	@FindBy(xpath="//button[text()='Open Menu']")
	WebElement OpenMenu;
	
	@FindBy(xpath="//a[@id='logout_sidebar_link']")
	WebElement Logout;
	
	public LoginData(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
				
	}
	
}
