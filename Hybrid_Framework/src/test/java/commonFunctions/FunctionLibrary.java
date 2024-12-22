package commonFunctions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

public class FunctionLibrary {
	public static Properties conpro;
	public static WebDriver driver;
	//method for launching browser
	public static WebDriver startBrowser()throws Throwable
	{
		conpro = new Properties();
		//load property file
		conpro.load(new FileInputStream("./PropertiesFiles\\Environment.properties"));
		if(conpro.getProperty("Browser").equalsIgnoreCase("chrome"))
		{
			driver = new ChromeDriver();
			driver.manage().window().maximize();
		}
		else if(conpro.getProperty("Browser").equalsIgnoreCase("firefox"))
		{
			driver = new FirefoxDriver();
		}
		else
		{
			Reporter.log("Browser Value is Not Matching",true);
		}
		return driver;
	}
	//method for launch url
	public static void openUrl()
	{
		driver.get(conpro.getProperty("Url"));
	}
	//method for wait for any element
	public static void waitForElement(String LocatorType,String LocatorValue,String wait)
	{
		WebDriverWait mywait = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(wait)));
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			//wait unti element is visible
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(LocatorValue)));
		}
		if(LocatorType.equalsIgnoreCase("name"))
		{
			//wait unti element is visible
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.name(LocatorValue)));
		}
		if(LocatorType.equalsIgnoreCase("id"))
		{
			//wait unti element is visible
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.id(LocatorValue)));
		}

	}
	public static void typeAction(String LocatorType,String LocatorValue,String TestData)
	{
		if(LocatorType.equalsIgnoreCase("name"))
		{
			driver.findElement(By.name(LocatorValue)).clear();
			driver.findElement(By.name(LocatorValue)).sendKeys(TestData);
		}
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			driver.findElement(By.xpath(LocatorValue)).clear();
			driver.findElement(By.xpath(LocatorValue)).sendKeys(TestData);
		}
		if(LocatorType.equalsIgnoreCase("id"))
		{
			driver.findElement(By.id(LocatorValue)).clear();
			driver.findElement(By.id(LocatorValue)).sendKeys(TestData);
		}
	}
	//method for buttons,links,radiobutton,checkboxes and images
	public static void clickAction(String LocatorType,String LocatorValue)
	{
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			driver.findElement(By.xpath(LocatorValue)).click();
		}
		if(LocatorType.equalsIgnoreCase("name"))
		{
			driver.findElement(By.name(LocatorValue)).click();
		}
		if(LocatorType.equalsIgnoreCase("id"))
		{
			driver.findElement(By.id(LocatorValue)).sendKeys(Keys.ENTER);
		}
	}
	//method for validateTitle
	public static void validateTitle(String Expected_Title)
	{
		String Actual_Title =driver.getTitle();
		try {
			Assert.assertEquals(Actual_Title, Expected_Title,"Title is Not Matching");
		} catch (AssertionError e) {
			System.out.println(e.getMessage());
		}
	}
	public static void closeBrowser()
	{
		driver.quit();
	}
	//method for any listbox
	public static void dropDownAction(String LocatorType,String LocatorValue,String TestData)
	{
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			//convert testdata into integer
			int value =Integer.parseInt(TestData);
			Select element = new Select(driver.findElement(By.xpath(LocatorValue)));
			element.selectByIndex(value);

		}
		if(LocatorType.equalsIgnoreCase("name"))
		{
			//convert testdata into integer
			int value =Integer.parseInt(TestData);
			Select element = new Select(driver.findElement(By.name(LocatorValue)));
			element.selectByIndex(value);

		}
		if(LocatorType.equalsIgnoreCase("id"))
		{
			//convert testdata into integer
			int value =Integer.parseInt(TestData);
			Select element = new Select(driver.findElement(By.id(LocatorValue)));
			element.selectByIndex(value);

		}
	}
	//capture stock number and write into notepad under CaptureData Folder
	public static void captureStock(String LocatorType,String LocatorValue)throws Throwable
	{
		String stockNumber="";
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			stockNumber=driver.findElement(By.xpath(LocatorValue)).getAttribute("value");
		}
		if(LocatorType.equalsIgnoreCase("name"))
		{
			stockNumber=driver.findElement(By.name(LocatorValue)).getAttribute("value");
		}
		if(LocatorType.equalsIgnoreCase("id"))
		{
			stockNumber=driver.findElement(By.id(LocatorValue)).getAttribute("value");
		}
		//create noteppad and write stocknumber
		FileWriter fw = new FileWriter("./CaptureData/stoccknum.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(stockNumber);
		bw.flush();
		bw.close();

	}
	//method for verify stock number in table
	public static void stockTable()throws Throwable
	{
		//read stock number from notepad
		FileReader fr = new FileReader("./CaptureData/stocknum.txt");
		BufferedReader br = new BufferedReader(fr);
		String Exp_Data = br.readLine();
		if(!driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).isDisplayed())
			driver.findElement(By.xpath(conpro.getProperty("search-panel"))).click();
		driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).clear();
		driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).sendKeys(Exp_Data);
		driver.findElement(By.xpath(conpro.getProperty("search-button"))).click();
		Thread.sleep(3000);
		//capture stock nyumber from table
		String Act_data =driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[8]/div/span/span")).getText();
		Reporter.log(Exp_Data+"         "+Act_data,true);
		try {
			Assert.assertEquals(Act_data, Exp_Data, "Stock Number NNot Matching");
		} catch (AssertionError e) {
			System.out.println(e.getMessage());
		}

	}
}
