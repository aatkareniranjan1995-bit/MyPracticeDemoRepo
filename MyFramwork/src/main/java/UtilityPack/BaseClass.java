package UtilityPack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;

public class BaseClass {

	protected WebDriver driver;
	protected Properties prop;
	String ScreenShotName = "";

	// comman code start
	public BaseClass(WebDriver driver) throws IOException {
		this.driver = driver;
		prop = new Properties();
		FileInputStream fis = new FileInputStream("src/test/java/propertyFile/XpathPropertyFile.prop");
		prop.load(fis);
	}

	public boolean HitURLInToBrowser(String url) {
		try {
			driver.get(url);
			driver.manage().window().maximize();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String getDateTimeKolkata(String mode) {
		ZoneId kolkataZone = ZoneId.of("Asia/Kolkata");
		ZonedDateTime nowInKolkata = ZonedDateTime.now(kolkataZone);

		DateTimeFormatter formatter;

		switch (mode.toLowerCase()) {
		case "date":
			formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
			break;
		case "time:":
			formatter = DateTimeFormatter.ofPattern("hh:mm"); // 12-hour format
			break;
		case "time: am/pm":
			formatter = DateTimeFormatter.ofPattern("hh:mm a"); // 12-hour with AM/PM
			break;
		case "date-time-":
			formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-hh-mm"); // 12-hour format
			break;
		case "date- time: am/pm":
			formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a"); // 12-hour with AM/PM
			break;
		default:
			formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a"); // fallback
		}

		return nowInKolkata.format(formatter);
	}

	public String captureScreenshot(WebDriver driver, ITestResult result) {
		try {
			String screenshotName = result.getMethod().getMethodName() + "_" + getDateTimeKolkata("date-time-") + ".png";
			File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			File destDir = new File("Screenshot");
			if (!destDir.exists()) {
				destDir.mkdirs();
			}
			File destFile = new File(destDir, screenshotName);
			FileUtils.copyFile(srcFile, destFile);
			return screenshotName;
		} catch (IOException e) {
			System.err.println("Failed to capture screenshot: " + e.getMessage());
			return "";
		}
	}

	// Method to get response code
	public String getResponseCode(String pageUrl) {
		try {
			if (pageUrl != null || pageUrl.length() != 0) {
				HttpURLConnection connection = (HttpURLConnection) new URL(pageUrl).openConnection();
				connection.setRequestMethod("GET");
				connection.connect();
				return String.valueOf(connection.getResponseCode());
			} else {
				return "NA";
			}
		} catch (Exception e) {
			return "NA";
		}
	}

	// Method to get response message
	public String getResponseMessage(String pageUrl) {
		try {
			if (pageUrl != null || pageUrl.length() != 0) {
				HttpURLConnection connection = (HttpURLConnection) new URL(pageUrl).openConnection();
				connection.setRequestMethod("GET");
				connection.connect();
				return connection.getResponseMessage();
			} else {
				return "NA";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "NA";
		}
	}

	public void createExcelFile(String filePath, String CurrentDate) {
		System.out.println(filePath);
		try (Workbook workbook = new File(filePath).exists() ? new XSSFWorkbook(new FileInputStream(filePath)) : new XSSFWorkbook()) {

			Sheet sheet = workbook.getSheet(CurrentDate);
			if (sheet == null) {
				sheet = workbook.createSheet(CurrentDate);
			}

			int startColIndex = 0;
			Row titleRow = sheet.getRow(1);
			if (titleRow != null) {
				startColIndex = titleRow.getLastCellNum() + 1;
			} else {
				titleRow = sheet.createRow(0);
			}

			Row titleRow0 = sheet.getRow(0);
			Cell titleCell = titleRow0.createCell(startColIndex);
			String executionTitle = "Execution Report of " + getDateTimeKolkata("date- time: am/pm");
			titleCell.setCellValue(executionTitle);

			sheet.addMergedRegion(new CellRangeAddress(0, 0, startColIndex, startColIndex + 10));

			String[] headers = { "Step Number", "Platform Name", "Scenario Name", "Steps", "Page URL", "Page Load Time", "Status", "Remarks", "Response Code", "Response Message", "Screenshot Name" };
			Row headerRow = sheet.getRow(1);
			if (headerRow == null) {
				headerRow = sheet.createRow(1);
			}
			for (int i = 0; i < headers.length; i++) {
				headerRow.createCell(startColIndex + i).setCellValue(headers[i]);
			}

			try (FileOutputStream fos = new FileOutputStream(filePath)) {
				workbook.write(fos);
				System.out.println("Excel file updated/created: " + filePath);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static int findLastEmptyRow(Sheet sheet, int columnIndex) {
		int lastRowNum = sheet.getLastRowNum();
		DataFormatter format = new DataFormatter();

		for (int i = 0; i <= lastRowNum; i++) {
			Row row = sheet.getRow(i);
			if (row == null || format.formatCellValue(row.getCell(columnIndex)) == null || format.formatCellValue(row.getCell(columnIndex)) == "" || format.formatCellValue(row.getCell(columnIndex)).isEmpty()) {

				return i;
			}
		}
		return lastRowNum + 1;
	}

	public int findLastColumnOfLastTable(Row headerRow) {
		if (headerRow != null) {
			return headerRow.getLastCellNum();
		}
		return -1;
	}

	public int findFirstColumnOfLastTable(Row headerRow) {
		DataFormatter format = new DataFormatter();
		int GetFirstColIndex = headerRow.getFirstCellNum();
		String ColName = format.formatCellValue(headerRow.getCell(GetFirstColIndex));

		if (headerRow != null) {
			for (int j = headerRow.getLastCellNum() - 1; j >= 0; j--) {
				Cell cell = headerRow.getCell(j);
				if (cell != null && cell.getCellType() == CellType.STRING && cell.getStringCellValue().equalsIgnoreCase(ColName)) {
					return j;
				}
			}
		}
		return -1;
	}

	public void cfnWriteReportInExcel(String OutputfileCreationPath, String finalUrl, String Platform, String Steps, String PageLoadTime, String Status, String Remark, String Current_Date, String Scenario, ITestResult result) throws IOException {
		String ScreenShotName = "";
		FileInputStream FileInput = new FileInputStream(OutputfileCreationPath);
		Workbook Filein = new XSSFWorkbook(FileInput);
		Sheet sheet = Filein.getSheet(Current_Date);

		int startingCellNumber = findFirstColumnOfLastTable(sheet.getRow(1));
//		int LastCellNo = findLastColumnOfLastTable(sheet.getRow(1));
		int LastRowNum = findLastEmptyRow(sheet, startingCellNumber);

		Row newRow = sheet.getRow(LastRowNum);
		if (newRow == null) {
			newRow = sheet.createRow(LastRowNum);
		}

		int stepNumber = LastRowNum - 1;

		Row LastStepRowNum = sheet.getRow(LastRowNum - 1);
		if (LastStepRowNum != null) {
			String LastSteps = LastStepRowNum.getCell(startingCellNumber + 3).toString();
			if (Status.equalsIgnoreCase("Fail")) {
				Remark = LastSteps + " - " + Remark;
				ScreenShotName = captureScreenshot(driver, result);
			}
		}

		String timeValue = PageLoadTime.equals("NA") ? "NA" : String.format("%.2f s", Double.parseDouble(PageLoadTime) / 1000.0);

		newRow.createCell(startingCellNumber).setCellValue(stepNumber);
		newRow.createCell(startingCellNumber + 1).setCellValue(Platform);
		newRow.createCell(startingCellNumber + 2).setCellValue(Scenario);
		newRow.createCell(startingCellNumber + 3).setCellValue(Steps);
		newRow.createCell(startingCellNumber + 4).setCellValue(finalUrl);
		newRow.createCell(startingCellNumber + 5).setCellValue(timeValue);
		newRow.createCell(startingCellNumber + 6).setCellValue(Status);
		newRow.createCell(startingCellNumber + 7).setCellValue(Remark);

		String responseCode = getResponseCode(finalUrl);
		String responseMessage = getResponseMessage(finalUrl);
		newRow.createCell(startingCellNumber + 8).setCellValue(responseCode);
		newRow.createCell(startingCellNumber + 9).setCellValue(responseMessage);
		newRow.createCell(startingCellNumber + 10).setCellValue(ScreenShotName);

		try (FileOutputStream fileout = new FileOutputStream(OutputfileCreationPath)) {
			Filein.write(fileout);
			writeLogToHTML(stepNumber + " : " + Steps + " : " + Status + " : " + Remark);
			System.out.println(stepNumber + " : " + Steps + " : " + Status + " : " + Remark);
		}

		Filein.close();
		Remark = "";
		ScreenShotName = "";
	}

	public void writeLogToHTML(String logMessage) {
		try {
			// Fixed HTML folder path
			File htmlDir = new File("F:\\Niranjan\\NiranjanWorkspace\\MyFramwork\\HTMLReport");
			if (!htmlDir.exists()) {
				htmlDir.mkdirs();
			}

			// Log file path
			File logFile = new File(htmlDir, "ExecutionLog.html");

			// If file doesn't exist, create with basic HTML structure
			if (!logFile.exists()) {
				try (FileOutputStream fos = new FileOutputStream(logFile)) {
					String header = "<html><head><title>Execution Log</title></head><body>";
					header += "<h2>Automation Execution Log</h2><table border='1' cellspacing='0' cellpadding='5'>";
					header += "<tr><th>Timestamp</th><th>Message</th></tr>";
					fos.write(header.getBytes());
				}
			}

			// Append new log entry
			try (FileOutputStream fos = new FileOutputStream(logFile, true)) {
				String timestamp = getDateTimeKolkata("date- time: am/pm");
				String row = "<tr><td>" + timestamp + "</td><td>" + logMessage + "</td></tr>";
				fos.write(row.getBytes());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// comman code end

	// journey code start

	public boolean Validate_Element(String Xpath) {
		try {
			driver.findElement(By.xpath(prop.getProperty(Xpath)));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean Enter_TextInInputBox(String Xpath, String GoogleSearchValue) {
		try {
			WebElement searchBox = driver.findElement(By.xpath(prop.getProperty(Xpath)));
			searchBox.sendKeys(GoogleSearchValue);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	public boolean Click_Element(String Xpath) {
		try {
			driver.findElement(By.xpath(prop.getProperty(Xpath)));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// journey code end

	// coman code start

	public String getElementValue(WebElement element) {
		String[] strategies = { "getText", "value", "innerText", "textContent", "placeholder", "domValue", "accessibleName", "ariaRole", "cssColor", "cssBackground", "cssFontSize", "size", "location", "rect", "isDisplayed", "isEnabled", "isSelected" };

		for (String strategy : strategies) {
			String result = null;

			switch (strategy) {
			case "getText":
				result = element.getText();
				break;
			case "value":
				result = element.getAttribute("value");
				break;
			case "innerText":
				result = element.getAttribute("innerText");
				break;
			case "textContent":
				result = element.getAttribute("textContent");
				break;
			case "placeholder":
				result = element.getAttribute("placeholder");
				break;
			case "domValue":
				result = element.getDomProperty("value");
				break;
			case "accessibleName":
				result = element.getAccessibleName();
				break;
			case "ariaRole":
				result = element.getAriaRole();
				break;
			case "cssColor":
				result = element.getCssValue("color");
				break;
			case "cssBackground":
				result = element.getCssValue("background-color");
				break;
			case "cssFontSize":
				result = element.getCssValue("font-size");
				break;
			case "size":
				Dimension size = element.getSize();
				result = "Width=" + size.getWidth() + ", Height=" + size.getHeight();
				break;
			case "location":
				Point point = element.getLocation();
				result = "X=" + point.getX() + ", Y=" + point.getY();
				break;
			case "rect":
				Rectangle rect = element.getRect();
				result = "X=" + rect.getX() + ", Y=" + rect.getY() + ", Width=" + rect.getWidth() + ", Height=" + rect.getHeight();
				break;
			case "isDisplayed":
				result = String.valueOf(element.isDisplayed());
				break;
			case "isEnabled":
				result = String.valueOf(element.isEnabled());
				break;
			case "isSelected":
				result = String.valueOf(element.isSelected());
				break;
			}

			if (result != null && !result.trim().isEmpty()) {
				return result.trim();
			}
		}
		return null;
	}

	public void highlightWebElement(WebDriver driver, String XpathVeriable) {
		try {
			WebElement element = driver.findElement(By.xpath(prop.getProperty(XpathVeriable)));
			JavascriptExecutor js = (JavascriptExecutor) driver;

			for (int i = 0; i < 3; i++) {
				js.executeScript("arguments[0].style.border='3px solid red'", element);
				Thread.sleep(200);

				js.executeScript("arguments[0].style.border=''", element);
				Thread.sleep(200);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean Validate_WebElement(String XpathVeriable) {
		try {
			driver.findElement(By.xpath(prop.getProperty(XpathVeriable)));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean EnterTextInTo_WebElement(String XpathVeriable, String TestData) {
		try {
			WebElement element = driver.findElement(By.xpath(prop.getProperty(XpathVeriable)));
			for (int i = 0; i < TestData.length(); i++) {
				element.sendKeys(String.valueOf(TestData.charAt(i)));
				Thread.sleep(500);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean ClickOn_WebElement(String XpathVeriable) {
		try {
			WebElement element = driver.findElement(By.xpath(prop.getProperty(XpathVeriable)));
			try {
				element.click();
				return true;
			} catch (Exception e1) {
				try {
					Actions actions = new Actions(driver);
					actions.moveToElement(element).click().perform();
					return true;
				} catch (Exception e2) {
					try {
						JavascriptExecutor js = (JavascriptExecutor) driver;
						js.executeScript("arguments[0].click();", element);
						return true;
					} catch (Exception e3) {
						return false;
					}
				}
			}
		} catch (Exception e) {
			return false;
		}
	}

	public List<String> getTextFromWebElements(String xpathVariable) {
		List<String> texts = new ArrayList<>();
		try {
			List<WebElement> elements = driver.findElements(By.xpath(prop.getProperty(xpathVariable)));
			for (WebElement element : elements) {
				String text = getElementValue(element);
				if (text != null && !text.isEmpty()) {
					texts.add(text.trim());
				}
			}
			return texts;
		} catch (Exception e) {
			return texts;
		}
	}

	public List<WebElement> getListOfWebElements(String XpathVeriable) {
		try {
			List<WebElement> elements = driver.findElements(By.xpath(prop.getProperty(XpathVeriable)));
			return elements;
		} catch (Exception e) {
			return null;
		}
	}

	public int getSizeWebElements(String XpathVeriable) {
		try {
			List<WebElement> elements = driver.findElements(By.xpath(prop.getProperty(XpathVeriable)));
			int SizeOfElements = elements.size();
			return SizeOfElements;
		} catch (Exception e) {
			return 0;
		}
	}

	public void clickRandomWebElement(String XpathVeriable) {
		List<WebElement> elements = driver.findElements(By.xpath(prop.getProperty(XpathVeriable)));
		int randomIndex = getRandomValue(0, elements.size() - 1);
		elements.get(randomIndex).click();
	}

	public int getRandomValue(int min, int max) {
		Random random = new Random();
		int RandamValue =min + random.nextInt(max - min + 1);
		return RandamValue;
	}

	public List<String> generateDOBsUsingMinAndMaxAge(int minAge, int maxAge) {
		Random random = new Random();
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("ddMMyyyy");
		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("ddMMyyyy");

		String todayDate = getDateTimeKolkata("date");
		LocalDate baseDate = LocalDate.parse(todayDate, inputFormatter);

		LocalDate dobGreaterMax = baseDate.minusYears(maxAge + 1 + random.nextInt(30));
		LocalDate dobLessMin = baseDate.minusYears(random.nextInt(minAge));
		int randomAge = minAge + random.nextInt(maxAge - minAge + 1);
		LocalDate dobBetween = baseDate.minusYears(randomAge);

		String[] dobs = { dobGreaterMax.format(outputFormatter), dobLessMin.format(outputFormatter), dobBetween.format(outputFormatter) };

		return Arrays.asList(dobs);
	}
	// coman code end

}
