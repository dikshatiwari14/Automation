package com.nagarro.seleniumAssignment.Base;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.*;
import com.nagarro.seleniumAssignment.Utilities.ConfigReader;
import com.nagarro.seleniumAssignment.Utilities.ExcelReader;

public class Base {

	public static ConfigReader PropertiesFileReader = new ConfigReader();
	public static WebDriver driver;
	public ExcelReader xls = new ExcelReader(System.getProperty("user.dir") + "\\src\\test\\resources\\TestData\\Data.xlsx");
	public static Logger log = Logger.getLogger("devpinoyLogger");

	public Base() {

	}

	public Base(WebDriver driver) {

		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void browserSetup() {
		String browser = PropertiesFileReader.getProperty("browser");
		System.out.println("Browser Name is :" + browser);

		if (browser.equals("chrome")) {
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
			log.info("Launching Chrome Browser");
		}

		else if (browser.equals("firefox")) {

			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
			log.info("Launching Firefox Browser");
			
		} else if (browser.equals("ie")) {

			WebDriverManager.iedriver().setup();
			driver = new InternetExplorerDriver();
			log.info("Launching InternetExplorer Browser");
			
		} else {
			System.err.println("Please Enter Correct Browser");
		}

		// Maximize the browser window
		driver.manage().window().maximize();

		// Applying Implicit Wait
		int ImplicitWait = Integer.parseInt(PropertiesFileReader
				.getProperty("implicitwait"));
		driver.manage().timeouts()
				.implicitlyWait(ImplicitWait, TimeUnit.SECONDS);
		System.out.println(ImplicitWait);
	}

	public void navigateToUrl() {
		driver.get(PropertiesFileReader.getProperty("url"));
	}

	@BeforeTest (groups = {"Regression", "Sanity"})
	public void initiate() {
		browserSetup();
		navigateToUrl();
		log.info("Navigating to " + PropertiesFileReader.getProperty("url")+ " URL");
	}

	@AfterTest (groups = {"Regression", "Sanity"})
	public void tearDown() {

		if (driver != null) {
			driver.quit();
			log.info("Closing Browser");
		}
	}
}
