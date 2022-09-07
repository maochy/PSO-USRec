package cn.jxufe.util;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Properties;

import cn.jxufe.entity.Param;
public class ParamMethod {

	/**
	 * @description:Read parameter setting from properties file
	 * @param path: File path
	 */
	public  Param getParamByPropertiesFile(String path)
	{
		Param param = new Param();
		Properties prop = new Properties();  
	    InputStream in = this.getClass().getResourceAsStream(path);  
	     try {  
	      prop.load(in);
	      in.close();
	      for(Object key:prop.keySet())
	      {
	    	  String propertyName = String.valueOf(key);
			try {
				PropertyDescriptor pd = new PropertyDescriptor(propertyName, param.getClass());
				Method setter = pd.getWriteMethod();
				setter.invoke(param,prop.get(key)+"");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Failed to read data from properties file");;
			}    	  
	      }
	     }
	     catch(IOException e)
	     {
	    	 e.printStackTrace();
	     }
	     return param;
	}
	
}
