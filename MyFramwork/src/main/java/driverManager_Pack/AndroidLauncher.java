package driverManager_Pack;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.Optional;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;

public class AndroidLauncher {
    public AndroidDriver mobileDriver;
    private boolean noResetEnabled;
    private boolean fullResetEnabled;
    private boolean installApp;
    private boolean uninstallApp;

    public AndroidLauncher(boolean noResetEnabled, boolean fullResetEnabled, boolean installApp, boolean uninstallApp) {
        this.noResetEnabled = noResetEnabled;
        this.fullResetEnabled = fullResetEnabled;
        this.installApp = installApp;
        this.uninstallApp = uninstallApp;
    }

    public void launch(@Optional String platformName, @Optional String deviceName, @Optional String udid,
                       @Optional String appPackage, @Optional String appActivity, @Optional String systemPort,
                       @Optional String AndroidAndiOSAppPath) throws MalformedURLException, IOException, InterruptedException {

        // Resolve dynamic path from AndroidAndiOSApp folder
        String appFolderPath = resolveApkPath(AndroidAndiOSAppPath);

        // Handle app lifecycle before driver launch
        handleAndroidAppLifecycle(udid, appFolderPath, appPackage);

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", platformName);
        caps.setCapability("deviceName", deviceName);
        caps.setCapability("udid", udid);
        caps.setCapability("appPackage", appPackage);
        caps.setCapability("appActivity", appActivity);
        caps.setCapability("noReset", noResetEnabled);
        caps.setCapability("fullReset", fullResetEnabled);
        caps.setCapability("systemPort", systemPort);

        if (appFolderPath != null && !appFolderPath.isEmpty()) {
            caps.setCapability("app", appFolderPath);
        }

        // Local Appium server only
        mobileDriver = new AndroidDriver(new URL("http://127.0.0.1:" + systemPort + "/wd/hub"), caps);

    }

    /** Check if Android app is installed or not : return true means app installed */
    private boolean isAndroidAppInstalled(String udid, String appPackage) throws IOException, InterruptedException {
        Process checkProcess = new ProcessBuilder("adb", "-s", udid, "shell", "pm", "path", appPackage).start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(checkProcess.getInputStream()));
        String output = reader.readLine();
        checkProcess.waitFor();

        // If app is installed, pm path returns something like: package:/data/app/...
        return output != null && output.startsWith("package:");
    }


    /** Android lifecycle */
    private void handleAndroidAppLifecycle(String udid, String appFolderPath, String appPackage) throws IOException, InterruptedException {
        String apkPath = resolveApkPath(appFolderPath);
        boolean appPresent = isAndroidAppInstalled(udid, appPackage);

        if (uninstallApp) { //if uninstallApp=true comes from testNG
            if (appPresent) { //if app is installed already then appPresent=true
                System.out.println("Uninstalling Android app: " + appPackage);
                Process uninstallProcess = new ProcessBuilder("adb", "-s", udid, "uninstall", appPackage).start(); // uninstall app
                uninstallProcess.waitFor();
                appPresent = false;
            } else {
                System.out.println("App not present in device for uninstall");
            }
        }

        if (installApp) { // if installApp=true comes from testNG
            if (appPresent) {//if app is installed already then appPresent=true
                System.out.println("App already installed in device");
            } else {
                System.out.println("Installing Android app from: " + apkPath);
                Process installProcess = new ProcessBuilder("adb", "-s", udid, "install", apkPath).start(); // install app
                installProcess.waitFor();
            }
        }
    }

    /** Resolve APK path dynamically from AndroidAndiOSApp folder */
    private String resolveApkPath(String appFolderPath) {
        File folder = new File(appFolderPath);

        // If folder path is invalid, do nothing
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Invalid APK folder path: " + appFolderPath);
            return null;
        }

        // Filter only .apk files
        File[] apkFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".apk"));

        // If no APK files found, do nothing
        if (apkFiles == null || apkFiles.length == 0) {
            System.out.println("No APK files found in folder: " + appFolderPath);
            return null;
        }

        // Pick the latest modified APK file
        File latestApk = Arrays.stream(apkFiles)
                .max(Comparator.comparingLong(File::lastModified))
                .orElse(null);

        return latestApk != null ? latestApk.getAbsolutePath() : null;
    }
}
