package driverManager_Pack;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.Optional;

public class WebLauncher {
	public WebDriver webDriver;

	public WebDriver launch(@Optional String platformName, @Optional String browserName) {
		// Only local browser launch supported now
		launchLocalBrowser(browserName);
		return webDriver;
	}

	private void launchLocalBrowser(String browserName) {
		switch (browserName.toLowerCase()) {
		case "chrome":
			WebDriverManager.chromedriver().setup();
			webDriver = new ChromeDriver();
			break;
		case "firefox":
			WebDriverManager.firefoxdriver().setup();
			webDriver = new FirefoxDriver();
			break;
		case "edge":
			WebDriverManager.edgedriver().setup();
			webDriver = new EdgeDriver();
			break;
		default:
			throw new IllegalArgumentException("Unsupported browser: " + browserName);
		}
		webDriver.manage().window().maximize();
	}
}
