package hufs.ces.grimpan.core;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class XMLPropertyManager {
	private Properties panProperties = null;
	private String propertyFile = null;
	
	public XMLPropertyManager(){
		this.panProperties = new Properties();
	}
	public XMLPropertyManager(String filename){
		this();
		this.propertyFile = filename;

		if (!loadPropertiesFromXML()){
			System.out.println("***Properties File Load Failed***");
		}
	}
	public boolean loadPropertiesFromXML(){
		boolean success = true;
		try {
			panProperties.loadFromXML(getClass().getResourceAsStream(propertyFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			success = false;
		} catch (IOException e) {
			e.printStackTrace();
			success = false;
		}
		return success;
	}
	public boolean storePropertiesToXML(){
		boolean success = true;
		try {
			panProperties
			.storeToXML(new FileOutputStream(getClass().getResource(propertyFile).getFile()), null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			success = false;
		} catch (IOException e) {
			e.printStackTrace();
			success = false;
		}
		return success;
	}
	public Object updatePropertyOfXML(String pkey, String pvalue){
		Object result =  panProperties.setProperty(pkey, pvalue);
		storePropertiesToXML();
		return result;
	}
	public Properties getPanProperties() {
		return panProperties;
	}
	public void setPropertyFileNameOfXML(String propertyFile) {
		this.propertyFile = propertyFile;
		loadPropertiesFromXML();
	}

}
