package hufs.cse.grimpan.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class SimplePropertyManager implements PropertyManager {
	private Properties panProperties = null;
	private String propertyFile = null;
	
	public SimplePropertyManager(){
		this.panProperties = new Properties();
	}
	public SimplePropertyManager(String filename){
		this();
		this.propertyFile = filename;

		if (!loadProperties()){
			System.out.println("***Properties File Load Failed***");
		}
	}
	/* (non-Javadoc)
	 * @see hufs.cse.primpan.util.PropertyManager1#loadProperties()
	 */
	@Override
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
	/* (non-Javadoc)
	 * @see hufs.cse.primpan.util.PropertyManager1#storeProperties()
	 */
	@Override
	public boolean storeProperties(){
		boolean success = true;
		try {
			panProperties.store(new FileOutputStream(propertyFile), null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			success = false;
		} catch (IOException e) {
			e.printStackTrace();
			success = false;
		}
		return success;
	}
	/* (non-Javadoc)
	 * @see hufs.cse.primpan.util.PropertyManager1#updateProperty(java.lang.String, java.lang.String)
	 */
	@Override
	public Object updateProperty(String pkey, String pvalue){
		Object result =  panProperties.setProperty(pkey, pvalue);
		storeProperties();
		return result;
	}
	/* (non-Javadoc)
	 * @see hufs.cse.primpan.util.PropertyManager1#getPanProperties()
	 */
	@Override
	public Properties getPanProperties() {
		return panProperties;
	}
	/* (non-Javadoc)
	 * @see hufs.cse.primpan.util.PropertyManager1#setPropertyFileName(java.lang.String)
	 */
	@Override
	public void setPropertyFileName(String propertyFile) {
		this.propertyFile = propertyFile;
		loadProperties();
	}
}
