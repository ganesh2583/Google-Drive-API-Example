package com.gck.gd.constants;

import java.util.HashMap;

/**
 * 
 * This class has the MIME mapping.
 * 
 * @author Ganesh Chaitanya Kale
 *
 */
public class MIMETypes {
	
	private static HashMap<String,String> mimesMap = new HashMap<String,String>();
	
	public static HashMap<String,String> getMimeTypes() {
		mimesMap.put("html", "text/html");
		mimesMap.put("txt", "text/plain");
		mimesMap.put("rtf", "application/rtf");
		mimesMap.put("doc", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		mimesMap.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		mimesMap.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		mimesMap.put("xls", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		mimesMap.put("json", "application/vnd.google-apps.script+json");
		mimesMap.put("csv", "text/csv");
		mimesMap.put("tsv", "text/tab-separated-values");
		mimesMap.put("jpg", "image/jpeg");
		mimesMap.put("jpeg", "image/jpeg");
		mimesMap.put("png", "image/png");
		mimesMap.put("svg", "image/svg+xml");
		mimesMap.put("pdf", "application/pdf");
		mimesMap.put("ppt", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
		return mimesMap;
	}
	

}
