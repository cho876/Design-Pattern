/**
 * Created on 2015. 5. 26.
 * @author cskim -- hufs.ac.kr, Dept of CSE
 * Copy Right -- Free for Educational Purpose
 */
package hufs.cse.grimpan.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * @author cskim
 *
 */
public class XMLPropertyManager implements PropertyManager {
	private Properties panProperties = null;
	private String propertyFile = null;
	
	public XMLPropertyManager(){
		this.panProperties = new Properties();
	}
	public XMLPropertyManager(String filename){
		this();
		this.propertyFile = filename;

		if (!loadProperties()){
			System.out.println("***Properties File Load Failed***");
		}
	}
	public boolean loadProperties(){
		boolean success = true;
		try {
			Resource resource = new ClassPathResource(propertyFile);
			panProperties = PropertiesLoaderUtils.loadProperties(resource);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			success = false;
		} catch (IOException e) {
			e.printStackTrace();
			success = false;
		}
		return success;
	}
	public boolean storeProperties(){
		boolean success = true;
		try {
			panProperties.storeToXML(new FileOutputStream(propertyFile), null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			success = false;
		} catch (IOException e) {
			e.printStackTrace();
			success = false;
		}
		return success;
	}
	public Object updateProperty(String pkey, String pvalue){
		Object result =  panProperties.setProperty(pkey, pvalue);
		storeProperties();
		return result;
	}
	public Properties getPanProperties() {
		return panProperties;
	}
	public void setPropertyFileName(String propertyFile) {
		this.propertyFile = propertyFile;
		loadProperties();
	}

}
