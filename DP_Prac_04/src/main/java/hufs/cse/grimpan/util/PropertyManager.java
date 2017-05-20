package hufs.cse.grimpan.util;

import java.util.Properties;

public interface PropertyManager {

	public abstract boolean loadProperties();

	public abstract boolean storeProperties();

	public abstract Object updateProperty(String pkey, String pvalue);

	public abstract Properties getPanProperties();

	public abstract void setPropertyFileName(String propertyFile);

}