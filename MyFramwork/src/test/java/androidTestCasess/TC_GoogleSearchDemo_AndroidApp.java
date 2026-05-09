//package androidTestCasess;
//
//import UtilityPack.BaseClass;
//import UtilityPack.FolderManager;
//import UtilityPack.ScreenshotListener;
//import driverManager_Pack.AndroidLauncher;
//
//import java.io.File;
//
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.testng.ITestContext;
//import org.testng.ITestResult;
//import org.testng.annotations.*;
//
//public class TC_GoogleSearchDemo_AndroidApp {
//
//	private WebDriver DriverApp;
//	private BaseClass BasclassObj;
//	private FolderManager FolderManagerDriver;
//	private ScreenshotListener SSObject;
//
//	// Use actual class name instead of superclass
//	String simpleClassName = getClass().getSimpleName();
//	String[] parts = simpleClassName.split("_");
//	String ClassName = parts.length > 1 ? parts[1] : simpleClassName;
//	String ClassPlatformName = parts[parts.length - 1];
//
//
//	@BeforeClass
//	@Parameters({ "platformName", "deviceName", "udid", "appPackage", "appActivity", "appLocation", "cloudProvider", "cloudUser", "cloudKey", "requiredAndroidVersion", "currentAndroidVersion" ,"systemPort"})
//	public void setUpAndroid(@Optional String platformName, @Optional String deviceName, @Optional String udid, @Optional String appPackage, @Optional String appActivity, @Optional String appLocation, @Optional String cloudProvider, @Optional String cloudUser, @Optional String cloudKey, @Optional String requiredAndroidVersion, @Optional String currentAndroidVersion,@Optional String systemPort) throws Exception {
//
//		// Example flags for reset/install/uninstall
//		boolean noResetEnabled = true;
//		boolean fullResetEnabled = false;
//		boolean installApp = true;
//		boolean uninstallApp = false;
//		String AndroidAndiOSAppPath = System.getProperty("user.dir") + File.separator + "AndroidAndiOSApp";
//		AndroidLauncher androidLauncher = new AndroidLauncher( noResetEnabled,  fullResetEnabled,  installApp,  uninstallApp);
//		androidLauncher.launch(  platformName,   deviceName,   udid,   appPackage,   appActivity, systemPort,AndroidAndiOSAppPath); // capture driver here
//	}
//
//	@BeforeMethod
//	public void CreateDriver() {
//		BasclassObj = new BaseClass(DriverApp);
//		FolderManagerDriver = new FolderManager();
//		SSObject = new ScreenshotListener(DriverApp);
//	}
//
//	@Test
//	public void googleSearchFlow(ITestContext context) {
//		ITestResult result = org.testng.Reporter.getCurrentTestResult();
//		String outputFolderPath = System.getProperty("user.dir") + File.separator + "Output" + File.separator + ClassName + ".xlsx";
//
//		String Date = BasclassObj.getDateTimeKolkata("date");
//		BasclassObj.createExcelFile(outputFolderPath, Date);
//
//		// Step 1: Launch Google
//		long startTime = System.currentTimeMillis();
//		boolean urlHit = BasclassObj.HitURLInToBrowser("https://www.google.com");
//		long loadTime = System.currentTimeMillis() - startTime;
//
//		if (urlHit && DriverApp.getTitle().contains("Google")) {
//			BasclassObj.writeDataToExcel(outputFolderPath, context, "GoogleSearchDemo", "Launch Google Homepage", DriverApp.getCurrentUrl(), loadTime + " ms", "Passed", "NA", "N/A", Date);
//
//			// Step 2: Validate search box
//			WebElement searchBox = DriverApp.findElement(By.name("q"));
//			if (searchBox != null && searchBox.isDisplayed()) {
//				BasclassObj.writeDataToExcel(outputFolderPath, context, "GoogleSearchDemo", "Validate Search Box", DriverApp.getCurrentUrl(), "N/A", "Passed", "NA", "N/A", Date);
//
//				// Step 3: Enter "facebook" in search box
//				try {
//					searchBox.sendKeys("facebook");
//					BasclassObj.writeDataToExcel(outputFolderPath, context, "GoogleSearchDemo", "Enter 'facebook' in Search Box", DriverApp.getCurrentUrl(), "N/A", "Passed", "NA", "N/A", Date);
//
//					// Step 4: Validate search button
//					WebElement searchBtn = DriverApp.findElement(By.name("btnK"));
//					if (searchBtn != null && searchBtn.isDisplayed()) {
//						BasclassObj.writeDataToExcel(outputFolderPath, context, "GoogleSearchDemo", "Validate Search Button", DriverApp.getCurrentUrl(), "N/A", "Passed", "NA", "N/A", Date);
//
//						// Step 5: Click search button
//						try {
//							searchBtn.click();
//							BasclassObj.writeDataToExcel(outputFolderPath, context, "GoogleSearchDemo", "Click Search Button", DriverApp.getCurrentUrl(), "N/A", "Passed", "NA", "N/A", Date);
//						} catch (Exception e) {
//							SSObject.onTestFailure(result);
//							BasclassObj.writeDataToExcel(outputFolderPath, context, "GoogleSearchDemo", "Click Search Button", DriverApp.getCurrentUrl(), "N/A", "Failed", "NA", BasclassObj.CreateScreenShotName(result), Date);
//						}
//					} else {
//						SSObject.onTestFailure(result);
//						BasclassObj.writeDataToExcel(outputFolderPath, context, "GoogleSearchDemo", "Validate Search Button", DriverApp.getCurrentUrl(), "N/A", "Failed", "NA", BasclassObj.CreateScreenShotName(result), Date);
//					}
//				} catch (Exception e) {
//					SSObject.onTestFailure(result);
//					BasclassObj.writeDataToExcel(outputFolderPath, context, "GoogleSearchDemo", "Enter 'facebook' in Search Box", DriverApp.getCurrentUrl(), "N/A", "Failed", "NA", BasclassObj.CreateScreenShotName(result), Date);
//				}
//			} else {
//				SSObject.onTestFailure(result);
//				BasclassObj.writeDataToExcel(outputFolderPath, context, "GoogleSearchDemo", "Validate Search Box", DriverApp.getCurrentUrl(), "N/A", "Failed", "NA", BasclassObj.CreateScreenShotName(result), Date);
//			}
//		} else {
//			SSObject.onTestFailure(result);
//			BasclassObj.writeDataToExcel(outputFolderPath, context, "GoogleSearchDemo", "Launch Google Homepage", DriverApp.getCurrentUrl(), loadTime + " ms", "Failed", "NA", BasclassObj.CreateScreenShotName(result), Date);
//		}
//	}
//
//	@AfterClass
//	public void tearDown() {
//		if (DriverApp != null) {
//			DriverApp.quit();
//		}
//	}
//}
