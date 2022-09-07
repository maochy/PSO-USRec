package cn.jxufe.util;

import cn.jxufe.entity.QoSObject;
import cn.jxufe.entity.StatisticInformation;

public class DataInstall {
	/**
	 * @description:Process QoS matrix as objects
	 * @param qos:User-Service matrix
	 * @return:An Array of QoSObject
	 */
	public static QoSObject[] getAllQosObject(double[][] qos)
	{
		QoSObject[] objs = new QoSObject[qos.length];
		for(int i = 0; i<qos.length;i++)
		{
			QoSObject obj = getQosObject(qos[i], i);
			objs[i] = obj;
			
		}
		return objs;
	}
	
	
	/**
	 * @description:Process the QoSs of one user or one service as a QoS object
	 * @param qos:QoSs of one user or one service
	 * @return: A QoSObject
	 */
	public static QoSObject getQosObject(double[] qos, int index)
	{
		QoSObject rowObj = new QoSObject();
		rowObj.setObjNo(index);
		rowObj.setQos(qos);
		//Obtain statistics of the current QoS object
		StatisticInformation stInformation = StatisticsMethod.getStatisticInformation(qos);
		rowObj.setStInformation(stInformation);
		return rowObj;
	}
}
