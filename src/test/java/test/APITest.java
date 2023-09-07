package test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class APITest {

	static String homePageurl = "https://www.saucedemo.com/v1/";
	static String loggedInurl = "https://www.saucedemo.com/v1/inventory.html";
	static String testResults = "\nTest Results\n\n";
	WebDriver driver;
	static int count = 1;

	@org.testng.annotations.Test
	public void loginTest() throws InterruptedException {
		addToTestHeadingOfResult("Test #" + count + ": loginTest");
		addCreds();
		Thread.sleep(4000);
		validateAndLogin();
	}

	@org.testng.annotations.Test
	public void loginValidationTest() throws InterruptedException, IOException {
		addToTestHeadingOfResult("Test #" + count + ": loginValidationTest");
		loginDetails();
		Thread.sleep(4000);
		validate();
		System.out.println("Test Successfull");
	}

	@org.testng.annotations.Test
	public void addRemoveTest() throws InterruptedException, IOException {
		addToTestHeadingOfResult("Test #" + count + ": addRemoveTest");
		addCreds();
		Thread.sleep(4000);
		WebElement addToCartBtn = driver
				.findElement(By.xpath("//*[@id='inventory_container']/div/div[1]/div[3]/button"));
		addToCartBtn.click();
		WebElement validationMessageElement = driver.findElement(By.xpath("//*[text()='REMOVE']"));
		if (validationMessageElement != null) {
			System.out.println("Validated with Remove button shown");
			addToTestPassResult("Validated with Remove button shown");
			Assert.assertTrue(true);
		} else {
			System.out.println("NOT Validated with Remove NOT button shown");
			addToTestPassResult("NOT Validated with Remove NOT button shown");
			Assert.assertTrue(false);
		}
		System.out.println("Test Successfull");
		driver.close();
	}

	@When(value = "^user types username/pass")
	public void addCreds() throws InterruptedException {
		System.setProperty("webdriver.chrome.driver", "chromedriver");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get(homePageurl);
		WebElement username = driver.findElement(By.id("user-name"));
		WebElement password = driver.findElement(By.id("password"));
		WebElement login = driver.findElement(By.id("login-button"));
		username.sendKeys("standard_user");
		password.sendKeys("secret_sauce");
		login.click();
	}

	@When(value = "^User successful login with correct creds")
	public void validateAndLogin() throws InterruptedException {
		String actualUrl = loggedInurl;
		String expectedUrl = driver.getCurrentUrl();
		Assert.assertEquals(expectedUrl, actualUrl);
		driver.close();
		System.out.println("Test Successfull");

	}

	@When(value = "^user logs in using Username as \"([^\"]*)\" without Password")
	public void loginDetails() {
		System.setProperty("webdriver.chrome.driver", "chromedriver");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get(homePageurl);
		WebElement username = driver.findElement(By.id("user-name"));
		WebElement login = driver.findElement(By.id("login-button"));
		username.sendKeys("standard_user");
		login.click();
	}

	@Then(value = "Password is required error message should show up")
	public void validate() throws IOException {
		WebElement validationMessageElement = driver.findElement(By.xpath("//*[text()='Password is required']"));
		if (validationMessageElement != null) {
			String validationMessage = validationMessageElement.getText();
			if (validationMessage.contains("Password is required")) {
				System.out.println("Validated with error message: " + validationMessage);
				addToTestPassResult("Validated with error message: " + validationMessage);
				Assert.assertTrue(true);
			} else {
				System.out.println("Not Validated with an error message");
				addToTestPassResult("Not Validated with an error message");
				Assert.assertTrue(false);
			}
		} else {
			System.out.println("No Validation error message shown");
			addToTestPassResult("No Validation error message shown");
			Assert.assertTrue(false);
		}
		driver.close();
		writeToFile();
		printResults();
	}

	private static void addToTestHeadingOfResult(String heading) {
		testResults += "\n\n"
				+ heading
				+ "-----------------------------------------------------------------------"
				+ "\n";
	}

	private static void addToTestPassResult(String result) {
		testResults += "Test #"
				+ count +
				" [PASS]: " +
				result
				+ "\n\n";
		count += 1;
	}

	private static void addToTestFailResult(String result) {
		testResults += "Test #"
				+ count + " [FAIL]: "
				+ result +
				"\n\n";
		count += 1;
	}

	private static void printResults() {
		System.out.println();
		System.out.println(testResults);
		System.out.println();
	}

	private static void writeToFile() throws IOException {

		// Defining the file name of the file
		Path fileName = Path.of("Report.txt");

		// Writing into the file
		Files.writeString(fileName, testResults);

	}

}
