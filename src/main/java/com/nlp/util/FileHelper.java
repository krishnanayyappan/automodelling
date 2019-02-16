package com.nlp.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileHelper extends BaseHelper {
	
	private static final Logger log = LoggerFactory.getLogger(FileHelper.class);
	
	public void saveToFile(String content, String baseDir, String fileName, String encoding) {

		verifyFileNameHasValue(fileName, "File name is null");
		if (content == null) {
			logErrorAndThrowException("content is null");
		}

		File file = makeAbsoluteFile(baseDir, fileName);
		verifyFileExists(file);
		
		try {
			FileUtils.writeStringToFile(file, content, encoding, true);
		} catch (IOException|UnsupportedCharsetException e) {
			logErrorAndThrowException("Couldn't save input text to file: ", e);
		}
	}	
	
	public void verifyFileExists(File file) {
		
		if (! file.isFile()) {
			logErrorAndThrowException("File does not exist: " + file.getPath()); // + " (Absolutt sti: " + file.getAbsolutePath() + ")");
		}
	}

	public void verifyFileNameHasValue(String fileName, String errorMsg) {
		
		if (StringUtils.isBlank(fileName)) {
			logErrorAndThrowException(errorMsg);
		}
	}
	
	public File makeAbsoluteFile(String baseDir, String fileName) {
		
		File absBaseDir = makeAbsoluteFile(baseDir);
		
		File f = new File(absBaseDir+fileName);
		boolean bool = false;
		try {
			bool = f.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("\n\nFile created: "+bool+"\n\n");
		 
		return f;
		
	}

	public File makeAbsoluteFile(String folderName ) {
		
		if (folderName == null) {
			folderName = "";
		}
		
		Path currentRelativePath = Paths.get("");
		String absolutePath = currentRelativePath.toAbsolutePath().toString();
		System.out.println("Current relative path is: " + absolutePath);
		
		final File outputRootDir = new File(absolutePath+"/resources/"+folderName);
		return outputRootDir;
	}

}
