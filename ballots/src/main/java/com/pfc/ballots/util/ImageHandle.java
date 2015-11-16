package com.pfc.ballots.util;

import java.io.File;

import org.apache.tapestry5.upload.services.UploadedFile;

public class ImageHandle {

	private String path = "/srv/ballotfiles/";
	private String urlbase="http://elesol.usal.es/files";
	
	public String saveImage(UploadedFile file){
		if (file != null){
			String filepath = path + file.getFileName();
			File copied = new File (filepath);
			file.write(copied);
			System.out.println("Debería haber almacenado la ruta correcta: "+filepath);
			return file.getFileName();
		}
		return "";
	}
	
	public String getImage(String filename){
		
		return urlbase+filename;
		
	}
	
	
}

