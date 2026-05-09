package driverManager_Pack;

import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.Optional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

public class IOSLauncher {
	public IOSDriver mobileDriver;
	private boolean noResetEnabled;
	private boolean fullResetEnabled;
	private boolean installApp;
	private boolean uninstallApp;

	public IOSLauncher(boolean noResetEnabled, boolean fullResetEnabled, boolean installApp, boolean uninstallApp) {
		this.noResetEnabled = noResetEnabled;
		this.fullResetEnabled = fullResetEnabled;
		this.installApp = installApp;
		this.uninstallApp = uninstallApp;
	}

	public void launch(@Optional String platformName, @Optional String deviceName, @Optional String udid, @Optional String appPackage, @Optional String appActivity, @Optional String cloudProvider, @Optional String cloudUser, @Optional String cloudKey, @Optional String systemPort, @Optional String AndroidAndiOSAppPath) throws MalformedURLException, IOException, InterruptedException {

		// Resolve dynamic path from AndroidAndiOSApp folder
		String appLocation = resolveIOSAppPath(AndroidAndiOSAppPath);

		// Handle app lifecycle before driver launch
		handleIOSAppLifecycle(udid, appLocation, appPackage);

		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setCapability("platformName", platformName);
		caps.setCapability("deviceName", deviceName);
		caps.setCapability("udid", udid);
		caps.setCapability("appPackage", appPackage);
		caps.setCapability("appActivity", appActivity);
		caps.setCapability("noReset", noResetEnabled);
		caps.setCapability("fullReset", fullResetEnabled);
		caps.setCapability("systemPort", systemPort);

		if (appLocation != null && !appLocation.isEmpty()) {
			caps.setCapability("app", appLocation);
		}

		// Only local execution supported
		mobileDriver = new IOSDriver(new URL("http://127.0.0.1:" + systemPort + "/wd/hub"), caps);
	}

	/** Check if iOS app is installed or not : return true means app installed */
	private boolean isIOSAppInstalled(String udid, String appBundleId) throws IOException, InterruptedException {
		Process checkProcess = new ProcessBuilder("ios-deploy", "--id", udid, "--list_installed").start();
		BufferedReader reader = new BufferedReader(new InputStreamReader(checkProcess.getInputStream()));
		String line;
		boolean found = false;

		while ((line = reader.readLine()) != null) {
			if (line.trim().equalsIgnoreCase(appBundleId)) {
				found = true;
				break;
			}
		}
		checkProcess.waitFor();
		return found;
	}

	/** iOS lifecycle */
	private void handleIOSAppLifecycle(String udid, String appLocation, String appPackage) throws IOException, InterruptedException {
		boolean appPresent = isIOSAppInstalled(udid, appPackage);

		if (uninstallApp) { // if uninstallApp=true comes from testNG
			if (appPresent) { // if app is installed already then appPresent=true
				System.out.println("Uninstalling iOS app...");
				Process uninstallProcess = new ProcessBuilder("ios-deploy", "--id", udid, "--uninstall_only").start();// uninstall app
				uninstallProcess.waitFor();
				appPresent = false;
			} else {
				System.out.println("App not present in device for uninstall");
			}
		}

		if (installApp) { // if installApp=true comes from testNG
			if (appPresent) {// if app is installed already then appPresent=true
				System.out.println("App already installed in device");
			} else {
				System.out.println("Installing iOS app: " + appLocation);
				Process installProcess = new ProcessBuilder("ios-deploy", "--id", udid, "--bundle", appLocation).start();
				installProcess.waitFor();
			}
		}
	}

	/** Resolve IPA/app path dynamically from AndroidAndiOSApp folder */
	private String resolveIOSAppPath(String appFolderPath) {
		File folder = new File(appFolderPath);
		if (!folder.exists() || !folder.isDirectory()) {
			System.out.println("Invalid iOS app folder path: " + appFolderPath);
			return null;
		}

		File[] iosFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".ipa") || name.toLowerCase().endsWith(".app"));
		if (iosFiles == null || iosFiles.length == 0) {
			System.out.println("No iOS app files (.ipa/.app) found in folder: " + appFolderPath);
			return null;
		}

		// Pick the latest IPA/app by modified date
		File latestApp = Arrays.stream(iosFiles).max(Comparator.comparingLong(File::lastModified)).orElseThrow(null);

		return latestApp != null ? latestApp.getAbsolutePath() : null;
	}

}
