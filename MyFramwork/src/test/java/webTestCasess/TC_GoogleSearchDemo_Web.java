package webTestCasess;

import UtilityPack.BaseClass;
import driverManager_Pack.WebLauncher;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.apache.poi.ss.usermodel.*;

public class TC_GoogleSearchDemo_Web {

	private WebDriver DriverWeb;
	private BaseClass BasclassObj;
	protected Properties prop;

	// Use actual class name instead of superclass
	String simpleClassName = getClass().getSimpleName();
	String[] parts = simpleClassName.split("_");
	String ClassName = parts[1];
	String ClassPlatformName = parts[2];
	String ClassName_Platformname=ClassName+"_"+ClassPlatformName;

	public String PlatFormName = "";
	public String BrowserName = "";

	String FinalUrl = "";
	String Steps = "";
	String PageLoadTime = "";
	String Status = "";
	String Remark = "";
	String ErrorScreenShotFilePath = "";
	String Scenario = "";
	String ProductName = "";
	String Date = "";

	@BeforeTest
	@Parameters({ "platformName", "browserName" })
	public void gobleVariableSet(@Optional String platformName, @Optional String browserName) {
		this.PlatFormName = platformName;
		this.BrowserName = browserName;
	}

	@BeforeClass
	@Parameters({ "platformName", "browserName" })
	public void CreateDriver(@Optional String platformName, @Optional String browserName) throws Exception {
		WebLauncher WebManger = new WebLauncher();
		DriverWeb = WebManger.launch(platformName, browserName);

	}

	@BeforeMethod
	public void CreateObj() throws IOException {
		BasclassObj = new BaseClass(DriverWeb);
		prop = new Properties();
		FileInputStream fis = new FileInputStream("src/test/java/propertyFile/XpathPropertyFile.prop");
		prop.load(fis);
	}

	@Test
	public void googleSearchFlow(ITestContext context) throws IOException {
		ITestResult result = org.testng.Reporter.getCurrentTestResult();
		DataFormatter formatter = new DataFormatter();

		String OutputFolderPath = System.getProperty("user.dir") + "/OutputFiles/" + ClassName_Platformname + ".xlsx";
		String TestDataFilePath = System.getProperty("user.dir") + "/src/main/resources/TestDataFolder/GoogleTestDataSheet.xlsx";

		Date = BasclassObj.getDateTimeKolkata("date");
		BasclassObj.createExcelFile(OutputFolderPath, Date);

		FileInputStream FileTestDataSheet = new FileInputStream(TestDataFilePath);
		Workbook workbook = new XSSFWorkbook(FileTestDataSheet);
		Sheet TestDataSheet = workbook.getSheet("GoogleTestData");
		Row TestDataRow = TestDataSheet.getRow(1);

		ProductName = formatter.formatCellValue(TestDataRow.getCell(0));
		Scenario = formatter.formatCellValue(TestDataRow.getCell(1));

		String GoogleSearchValue = formatter.formatCellValue(TestDataRow.getCell(2));

		long startTime = System.currentTimeMillis();
		boolean urlHit = BasclassObj.HitURLInToBrowser("https://www.google.com");
		long TotalTime = System.currentTimeMillis() - startTime;
		if (urlHit && DriverWeb.getTitle().contains("Google")) {
			{
				FinalUrl = String.valueOf(DriverWeb.getCurrentUrl());
				Steps = "Launch Google Homepage";
				PageLoadTime = String.valueOf(TotalTime);
				Status = "PASS";
				Remark = "";
				BasclassObj.cfnWriteReportInExcel(OutputFolderPath, FinalUrl, PlatFormName, Steps, PageLoadTime, Status, Remark, Date, Scenario, result);
			}
			if (BasclassObj.Validate_Element("SearchBox")) {
				{
					FinalUrl = String.valueOf(DriverWeb.getCurrentUrl());
					Steps = "Validate Search Box";
					PageLoadTime = String.valueOf("NA");
					Status = "PASS";
					Remark = "";
					BasclassObj.cfnWriteReportInExcel(OutputFolderPath, FinalUrl, PlatFormName, Steps, PageLoadTime, Status, Remark, Date, Scenario, result);
				}

				if (BasclassObj.Enter_TextInInputBox("SearchBox", GoogleSearchValue)) {
					{
						FinalUrl = String.valueOf(DriverWeb.getCurrentUrl());
						Steps = "Enter 'facebook";
						PageLoadTime = String.valueOf("NA");
						Status = "PASS";
						Remark = "";
						BasclassObj.cfnWriteReportInExcel(OutputFolderPath, FinalUrl, PlatFormName, Steps, PageLoadTime, Status, Remark, Date, Scenario, result);
					}

					if (BasclassObj.Validate_Element("SearchIcon")) {
						{
							FinalUrl = String.valueOf(DriverWeb.getCurrentUrl());
							Steps = "Validate Search Button";
							PageLoadTime = String.valueOf("NA");
							Status = "PASS";
							Remark = "";
							BasclassObj.cfnWriteReportInExcel(OutputFolderPath, FinalUrl, PlatFormName, Steps, PageLoadTime, Status, Remark, Date, Scenario, result);
						}

						if (BasclassObj.Click_Element("SearchIcon")) {
							{
								FinalUrl = String.valueOf(DriverWeb.getCurrentUrl());
								Steps = "Click on Search Button";
								PageLoadTime = String.valueOf("NA");
								Status = "PASS";
								Remark = "";
								BasclassObj.cfnWriteReportInExcel(OutputFolderPath, FinalUrl, PlatFormName, Steps, PageLoadTime, Status, Remark, Date, Scenario, result);
							}

							
						} else {
							{
								FinalUrl = String.valueOf(DriverWeb.getCurrentUrl());
								Steps = "Click on Search Button";
								PageLoadTime = String.valueOf("NA");
								Status = "FAIL";
								Remark = "Unable To Click on Search Button";
								BasclassObj.cfnWriteReportInExcel(OutputFolderPath, FinalUrl, PlatFormName, Steps, PageLoadTime, Status, Remark, Date, Scenario, result);
							}
						}	
					} else {
						{
							FinalUrl = String.valueOf(DriverWeb.getCurrentUrl());
							Steps = "Validate Search Button";
							PageLoadTime = String.valueOf("NA");
							Status = "FAIL";
							Remark = "Unable To Validate Search Button";
							BasclassObj.cfnWriteReportInExcel(OutputFolderPath, FinalUrl, PlatFormName, Steps, PageLoadTime, Status, Remark, Date, Scenario, result);
						}
					}
				} else {
					{
						FinalUrl = String.valueOf(DriverWeb.getCurrentUrl());
						Steps = "Enter 'facebook";
						PageLoadTime = String.valueOf("NA");
						Status = "FAIL";
						Remark = "Unable To Enter 'facebook";
						BasclassObj.cfnWriteReportInExcel(OutputFolderPath, FinalUrl, PlatFormName, Steps, PageLoadTime, Status, Remark, Date, Scenario, result);
					}
				}
			} else {
				{
					FinalUrl = String.valueOf(DriverWeb.getCurrentUrl());
					Steps = "Validate Search Box";
					PageLoadTime = String.valueOf("NA");
					Status = "FAIL";
					Remark = "Unable To Validate Search Box";
					BasclassObj.cfnWriteReportInExcel(OutputFolderPath, FinalUrl, PlatFormName, Steps, PageLoadTime, Status, Remark, Date, Scenario, result);
				}
			}
		}
	}

	@AfterClass
	public void tearDown() {
		if (DriverWeb != null) {
			DriverWeb.quit();
		}
	}
}
