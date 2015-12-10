package orph2obo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class OrphadataDownloader {
	
	/** Path of direrctory to which the files will be downloaded. */
	private String directory_path;
	
	
	public OrphadataDownloader(String dirpath) {
		if (! dirpath.endsWith("/")) {
			dirpath = dirpath + "/"; // add trailing slash.
		}
		this.directory_path = dirpath;	
	}
	
	/**
	 * This method simply downloads the file
	 * @param urlstring
	 * @param local_file_path
	 * @return true if the file is downloaded/exists
	 */
	public boolean download_file(String urlstring, String local_file_path) {
		try{
			URL url = new URL(urlstring);
			url.openConnection();
			InputStream reader = url.openStream();
			FileOutputStream writer = new FileOutputStream(local_file_path);
			byte[] buffer = new byte[153600];
			int totalBytesRead = 0;
			int bytesRead = 0;
			while ((bytesRead = reader.read(buffer)) > 0){ 
				writer.write(buffer, 0, bytesRead);
				buffer = new byte[153600];
				totalBytesRead += bytesRead;
			}
			 System.out.println("Done. " + (new Integer(totalBytesRead).toString()) + " bytes read.");
			 writer.close();
			 reader.close();
		} catch (MalformedURLException e){
			System.out.println("Could not interpret url: " + urlstring);
			  e.printStackTrace();
			  System.exit(1);
		}
		catch (IOException e){
			System.out.println("IO Exception reading from URL: " + urlstring);
		e.printStackTrace();
		System.exit(1);
		}
		
		return true;
	}

	
	/**
	 * This method downloads the disease xref file
	 * @return fullpath for the file
	 */
	public String downloadRareDiseaseXRefXML() {
		//String uri = "http://www.orphadata.org/data/xml/en_product1_withGroupDef.xml";// 	4.22 MB
		String uri = "http://www.orphadata.org/data/xml/OBO_EBI.xml";// 	4.22 MB
		String fname = "OBO_EBI.xml";
		String fullpath = directory_path + fname;
		File file = new File(fullpath);
		if (file.exists()) {
			//System.out.println("XML file " + fullpath + " already exists.");
			return fullpath;
		} else {
			//System.out.println("XML file " + fullpath + " will be downloaded.");
		}
		download_file(uri,fullpath);
		return fullpath;
	}

	
	/**
	 * This method simply downloads the Epidemiological file
	 * @return fullpath for the file
	 */
	public String downloadEpidemiologyXML() {
		String uri =  "http://www.orphadata.org/data/xml/en_product2.xml";
		String fname = "en_product2.xml";
		String fullpath = directory_path + fname;
		File file = new File(fullpath);
		if (file.exists()) {
			//System.out.println("XML file " + fullpath + " already exists.");
			return fullpath;
		} else {
			//System.out.println("XML file " + fullpath + " will be downloaded.");
		}
		download_file(uri,fullpath);
		return fullpath;
	}
	
	/**
	 * this method simply downloads the Genes file
	 * @return fullpath for the file
	 */
	public String downloadGenesXML() {
		String uri =  "http://www.orphadata.org/data/xml/en_product6.xml";
		String fname = "en_product6.xml";
		String fullpath = directory_path + fname;
		File file = new File(fullpath);
		if (file.exists()) {
			//System.out.println("XML file " + fullpath + " already exists.");
			return fullpath;
		} else {
			//System.out.println("XML file " + fullpath + " will be downloaded.");
		}
		download_file(uri,fullpath);
		return fullpath;
	}
	
	
	/** Get a URI with full path, download the file if not already there.
	 * Name the local file according to the qname.
	 * @param url of the classifiaction file
	 * @return fullpath of the file
	 */
	public String downloadClassificationXML(String url) {
		int ind = url.lastIndexOf("/");
		String fname = url.substring(ind+1);
		String fullpath = directory_path + fname;
		File file = new File(fullpath);
		if (file.exists()) {
			//System.out.println("XML file " + fullpath + " already exists.");
			return fullpath;
		} else {
			//System.out.println("XML file " + fullpath + " will be downloaded.");
		}
		download_file(url,fullpath);
		return fullpath;
	}

}
