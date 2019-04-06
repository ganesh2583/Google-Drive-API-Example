package com.gck.gd.services;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.gck.gd.constants.MIMETypes;
import com.gck.gd.exceptions.GDException;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

/**
 * 
 * Service class to handle 
 * 
 * 	Connecting to Google Drive.
 * 	Authenticating to Google Drive.
 * 	Create a Folder.
 * 	Upload a File to a Folder.
 * 	List all Files and Folders.
 * 	
 * @author gchaitan
 *
 */
@Service
public class DriveService {
	
    private static final String APPLICATION_NAME = "Google Drive API Java Quickstart";
    private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final String MIME_TYPE_FOLDER = "application/vnd.google-apps.folder";
    
    private static final Logger logger = LoggerFactory.getLogger(DriveService.class);
    
    @Value("${gda.proxy.host:#{null}}")
    private String proxyHostname;
    
    @Value("${gda.proxy.port:#{null}}")
    private String proxyPort;
    
    private Drive driveService;

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static HashMap<String, String> mimesMap = MIMETypes.getMimeTypes();

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = DriveService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8084).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }
    
    /**
     * 
     * This method executed after the server 
     * start up and established connection to the 
     * Google Drive.
     * 
     * 
     * @throws IOException
     */
    @EventListener(ApplicationReadyEvent.class)
    public void createDriveService() throws IOException {
    	NetHttpTransport httpTransport = null;
        // Build a new authorized API client service.
    	if(proxyHostname != null && proxyPort != null) {
    		final Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHostname, new Integer(proxyPort)));
    		httpTransport = new NetHttpTransport.Builder().setProxy(proxy).build();
    	}
    	else {
    		httpTransport = new NetHttpTransport.Builder().build();
    	}
       // final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    	driveService = new Drive.Builder(httpTransport, JSON_FACTORY, getCredentials(httpTransport))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /**
     * 
     * Get all Files in Google drive or
     * get all files in Folder.
     * 
     * @param folderName
     * @return
     * @throws IOException
     * @throws GeneralSecurityException
     * @throws GDException
     */
    public List<File> getFiles(String folderName) throws IOException, GeneralSecurityException, GDException {
        FileList result = driveService.files().list()
                .setFields("nextPageToken, files(id, name, fileExtension, mimeType, parents, trashed, version, modifiedTime)")
                .execute();
        List<File> files = result.getFiles();
        if(folderName == null || "".equalsIgnoreCase(folderName)) {
        	Set<String> nameSet = new HashSet<>();
            List<File> outFileListDist = files.stream().filter( file -> {
            	System.out.println("File names: "+file.getName());
            	return nameSet.add(file.getName());
            }).collect(Collectors.toList());
            return outFileListDist;
        } else {
        	String folderId = getFolderIdByName(files, folderName);
        	System.out.println("Folder Id: "+folderId);
            if("".equalsIgnoreCase(folderId)) {
            	throw new GDException("No Folder with the name present");
            }
            List<File> outFileList = new ArrayList<File>();
            if (files == null || files.isEmpty()) {
                logger.info("No files found.");
                return outFileList;
            } else {
                logger.info("Files:");
                for (File file : files) {
                    System.out.printf("%s (%s)\n", file.getName(), file.getId());
                }
            }
            files.stream().forEach(file -> {
            	List<String> parents = file.getParents();
            	if(parents != null && !parents.isEmpty() && parents.contains(folderId)) {
            		outFileList.add(file);
            	}
            } );
           return outFileList;
        }
    }
    
    /**
     * 
     * Download a file from a folder.
     * 
     * @param fileName
     * @return
     * @throws IOException
     * @throws GDException
     */
	public Resource downloadFile(String fileName) throws IOException, GDException {
		FileList result = driveService.files().list()
                .setFields("nextPageToken, files(id, name, fileExtension, mimeType, parents)")
                .execute();
        List<File> files = result.getFiles();
        File wantedFile = getFileByName(files, fileName);
		String fileId = wantedFile.getId();
		if("".equalsIgnoreCase(fileId)) {
        	throw new GDException("No File with the name present");
        }
		OutputStream out = new FileOutputStream("./"+fileName);
		driveService.files().get(fileId).executeMediaAndDownloadTo(out);
		out.close();
	    return new FileSystemResource(new java.io.File("./"+fileName));
	}
	
	/**
	 * 
	 * Create a file in Google Drive.
	 * 
	 * @param folderName
	 * @return
	 * @throws IOException
	 */
	public String createFolder(String folderName) throws IOException {
		File fileMetadata = new File();
		fileMetadata.setName(folderName);
		fileMetadata.setMimeType("application/vnd.google-apps.folder");
		File file = driveService.files().create(fileMetadata)
		    .setFields("id")
		    .execute();
		logger.info("Folder ID: " + file.getId());
		return file.getId();
	}
	
	/**
	 * 
	 * Create a file in folder, if folder is not present, it creates a folder.
	 * 
	 * @param folderId
	 * @param fileToBeUploaded
	 * @return
	 * @throws IOException
	 */
	private String createFile(String folderId, java.io.File fileToBeUploaded) throws IOException {
		File fileMetadata = new File();
		fileMetadata.setName(fileToBeUploaded.getName());
		fileMetadata.setParents(Collections.singletonList(folderId));
		java.io.File filePath = new java.io.File(fileToBeUploaded.getAbsolutePath());
		String ext = Arrays.stream(fileToBeUploaded.getAbsolutePath().split("\\.")).reduce((a,b) -> b).orElse(null);
		FileContent mediaContent = new FileContent(mimesMap.get(ext), filePath);
		File file = driveService.files().create(fileMetadata, mediaContent)
		    .setFields("id, parents")
		    .execute();
		logger.info("File ID: " + file.getId());
		return file.getId();
	}
	
	/**
	 * 
	 * Creates a file in Folder.
	 * 
	 * @param folderName
	 * @param fileToBeUploaded
	 * @throws IOException
	 */
	public void createFileInFolder(String folderName, java.io.File fileToBeUploaded) throws IOException {
		FileList result = driveService.files().list()
                .setFields("nextPageToken, files(id, name, fileExtension, mimeType, parents, trashed, version, modifiedTime)")
                .execute();
		String folderId = getFolderIdByName(result.getFiles(), folderName);
		if("".equalsIgnoreCase(folderId)) {
			logger.info("No folder present. Creating new folder.");
		}
		folderId = createFolder(folderName);
		createFile(folderId, fileToBeUploaded);
	}

	/**
	 * 
	 * Get folder id by name
	 * 
	 * @param files
	 * @param folderName
	 * @return
	 */
	public String getFolderIdByName(List<File> files, String folderName) {
		files = files.stream().filter( file -> file.getMimeType().equalsIgnoreCase(MIME_TYPE_FOLDER) && file.getName().equalsIgnoreCase(folderName)).collect(Collectors.toList());
		if(files.isEmpty()) {
			return "";
		}
		File fodler =  Collections.max(files, Comparator.comparing(file -> file.getVersion()));
		return fodler.getId();
	}

	/**
	 * 
	 * Get File id by name.
	 * 
	 * @param files
	 * @param fileName
	 * @return
	 */
	public File getFileByName(List<File> files, String fileName) {
		File wantedFile = null;
		for (Iterator<File> iterator = files.iterator(); iterator.hasNext();) {
			File file = (File) iterator.next();
			if(file.getName().equalsIgnoreCase(fileName)) {
				wantedFile= file;
				break;
        	}
		}
		return wantedFile;
	}
}