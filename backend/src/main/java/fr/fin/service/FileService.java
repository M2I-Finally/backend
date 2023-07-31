package fr.fin.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

	private final String[] authorizedExtensions = { "png", "jpg" };
	private final String FOLDER_NAME = "images";
	private final Path root = Paths.get(FOLDER_NAME);
	
	/**
	 * Creates an image from a MultipartFile
	 * @param file	The MultipartFile to process
	 * @return	A String with the relative URL name
	 */
	public String createImage(MultipartFile file) throws IOException {
		
		// Creates the images folder if it doesn't exist
		if(Paths.get(FOLDER_NAME) == null) {
			this.createFolder();
		}
			
		// Check if the file is null before processing datas
		if(file != null) {		
			String originFileName = file.getOriginalFilename();
			
			// Check for correct extensions before continuing
			if(!hasCorrectExtensions(originFileName)) {
				throw new IllegalArgumentException("Bad file extensions");
			}
			
			try {
				String fileName = UUID.randomUUID().toString();
				Files.copy(file.getInputStream(), this.root.resolve(fileName));
				return generateRelativeURL(fileName, FilenameUtils.getExtension(originFileName));
			} catch (IOException e) {
				System.out.println("Could not process image");
			}

		}
		
		throw new NullPointerException("File is null and cannot be processed");
	}
	
	/**
	 * Generate a relative URL that will be saved into database
	 * @param imageName	
	 * @param fileExtension
	 * @return	A String formatted as following /FOLDER_NAME/imageName.fileExtension
	 */
	private String generateRelativeURL(String imageName, String fileExtension) {
		return String.format("/%s/%s.%s", FOLDER_NAME, imageName, fileExtension.toLowerCase());
	}
	
	/**
	 * Check for correct file extensions
	 * @param fileName Filename to check
	 * @return	True if the extensions is part of the authorized extensions
	 */
	private boolean hasCorrectExtensions(String fileName) {
		for(String extension : authorizedExtensions) {
			if(extension.equals(FilenameUtils.getExtension(fileName))) {
				return true;
			}
		}
		return false;	
	}
	/**
	 * Create a folder in the server if it doesn't exist
	 * @throws IOException 
	 */
	private void createFolder() throws IOException {
		Files.createDirectories(Paths.get(FOLDER_NAME));
		System.out.println("Created image folder");
	}
}