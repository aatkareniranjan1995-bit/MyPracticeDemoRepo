package UtilityPack;

import java.io.File;

import org.testng.annotations.Test;

public class FolderManager {

	@Test
	public void CreateFolderManager() {
		String[] folders = { "OutputFiles", "FinalOutputFile", "Screenshot", "HTMLReport", "AndroidAndiOSApp" };

		for (String folder : folders) {
			File dir = new File(folder);
			System.out.print("Directory Name : " + dir.getName());
			// Create folder if not exists
			if (!dir.exists()) {
				if (dir.mkdirs()) {
					System.out.println("Created folder: " + folder);
				}
			} else {
				System.out.println("Folder already exists: " + folder);
			}
		}
	}

	@Test
	private void DeleteFilesInFolder() {
		String[] folders = { "FinalOutputFile", "Screenshot", "HTMLReport" };

		for (String singleFolder : folders) {
			File dir = new File(singleFolder);

			// Clean up only files if folder exists
			if (dir.exists() && dir.isDirectory()) {
				File[] files = dir.listFiles();
				if (files != null) {
					for (File file : files) {
						if (file.isFile()) {
							if (file.delete()) {
								System.out.println("Deleted file: " + file.getName());
							} else {
								System.out.println("Failed to delete file: " + file.getName());
							}
						}
					}
				}
			}
		}
	}

}
