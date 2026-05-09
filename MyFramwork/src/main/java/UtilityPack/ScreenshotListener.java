//package UtilityPack;
//
//import java.io.File;
//import java.io.IOException;
//import java.time.ZoneId;
//import java.time.ZonedDateTime;
//import java.time.format.DateTimeFormatter;
//
//import org.apache.commons.io.FileUtils;
//import org.openqa.selenium.OutputType;
//import org.openqa.selenium.TakesScreenshot;
//import org.openqa.selenium.WebDriver;
//import org.testng.ITestListener;
//import org.testng.ITestResult;
//
//public class ScreenshotListener implements ITestListener {
//
//	private static WebDriver driver;
//
//	public static void setDriver(WebDriver driverInstance) {
//		driver = driverInstance;
//	}
//
//	@Override
//	public void onTestFailure(ITestResult result) {
////		String className = result.getTestClass().getRealClass().getSimpleName();
////		String methodName = result.getMethod().getMethodName();
////		System.err.println("Test fail : " + className + " -> " + methodName);
//
//		if (driver != null) {
//			captureScreenshot(driver, result);
//		}
//	}
//
////	@Override
////	public void onTestSuccess(ITestResult result) {
////		String className = result.getTestClass().getRealClass().getSimpleName();
////		String methodName = result.getMethod().getMethodName();
////		System.err.println("Test pass: " + className + " -> " + methodName);
////	}
////
////	@Override
////	public void onTestSkipped(ITestResult result) {
////		String className = result.getTestClass().getRealClass().getSimpleName();
////		String methodName = result.getMethod().getMethodName();
////		System.err.println("Test skipped: " + className + " -> " + methodName);
////	}
//
//	public String captureScreenshot(WebDriver driver, ITestResult result) {
//		try {
//			String screenshotName = result.getMethod().getMethodName() + "_" + getDateTimeKolkata("date-time-") + ".png";
//			File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
//			File destDir = new File("Screenshot");
//			if (!destDir.exists()) {
//				destDir.mkdirs();
//			}
//			File destFile = new File(destDir, screenshotName);
//			FileUtils.copyFile(srcFile, destFile);
//			System.out.println("Screenshot saved: " + destFile.getAbsolutePath());
//			return destFile.getAbsolutePath();
//		} catch (IOException e) {
//			System.err.println("Failed to capture screenshot: " + e.getMessage());
//			return "";
//		}
//	}
//
//	public String getDateTimeKolkata(String mode) {
//		ZoneId kolkataZone = ZoneId.of("Asia/Kolkata");
//		ZonedDateTime nowInKolkata = ZonedDateTime.now(kolkataZone);
//
//		DateTimeFormatter formatter;
//
//		switch (mode.toLowerCase()) {
//		case "date":
//			formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
//			break;
//		case "time:":
//			formatter = DateTimeFormatter.ofPattern("hh:mm"); // 12-hour format
//			break;
//		case "time: am/pm":
//			formatter = DateTimeFormatter.ofPattern("hh:mm a"); // 12-hour with AM/PM
//			break;
//		case "date-time-":
//			formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-hh-mm"); // 12-hour format
//			break;
//		case "date- time: am/pm":
//			formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a"); // 12-hour with AM/PM
//			break;
//		default:
//			formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a"); // fallback
//		}
//
//		return nowInKolkata.format(formatter);
//	}
//}
