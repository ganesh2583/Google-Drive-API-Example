package com.gck.gd.controllers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gck.gd.exceptions.GDException;
import com.gck.gd.models.GDFileUpload;
import com.gck.gd.services.DriveService;
import com.google.api.services.drive.model.File;

/**
 * 
 * This class defines all the endpoints to 
 * 
 * 	Get all Files and Folders.
 * 	Download a File.
 * 	Create Folder.
 * 	Upload a File in Folder.
 * 
 * @author Ganesh Chaitanya Kale
 *
 */
@RestController
@CrossOrigin
public class GDController {

	@Autowired
	DriveService driveService;
	
	@GetMapping(value="/gdFiles")
	public ResponseEntity<List<File>> getAllFilesInGD(@RequestParam(name="folderName", required=false) String folderName) throws IOException, GeneralSecurityException, GDException {
		return new ResponseEntity<List<File>>(driveService.getFiles(folderName), HttpStatus.OK);
	}
	
	@GetMapping(value="/download/{fileName}")
	public ResponseEntity<Resource> downloadFile(@PathVariable("fileName") String fileName) throws IOException, GeneralSecurityException, GDException {
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + fileName + "").body(driveService.downloadFile(fileName));
	}
	
	@PostMapping(value="/gdFolders")
	public ResponseEntity<String> createFolderInGD(@RequestBody GDFileUpload gdFileUpload) throws IOException, GeneralSecurityException, GDException {
		return new ResponseEntity<String>(driveService.createFolder(gdFileUpload.getFolderName()), HttpStatus.OK);
	}
	
	@PostMapping(value="/gdFiles")
	public ResponseEntity<String> createFileInFolderInGD(@RequestParam("file") MultipartFile file, @RequestParam("folder") String folderName) throws IOException, GeneralSecurityException, GDException {
		java.io.File convFile = new java.io.File( file.getOriginalFilename() );
        FileOutputStream fos = new FileOutputStream( convFile );
        fos.write( file.getBytes() );
        fos.close();
		driveService.createFileInFolder(folderName, convFile);
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
}
