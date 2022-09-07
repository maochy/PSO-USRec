package cn.jxufe.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import cn.jxufe.entity.QoSObject;

public class FitnessMethod {	
	
	//Fitness function
	public static double calFitnessForParByDeviationFromMean(QoSObject[] user_objs,QoSObject[] item_objs, QoSObject cUser_obj,double[] par,Map<Integer, Double> simUsers,int topK)
	{
		double fit = 0;
		ArrayList<Integer> missIndexs = cUser_obj.getStInformation().getMissIndexs();
		for (int i = 0; i < missIndexs.size(); i++) {
			int sIndex = missIndexs.get(i);
			fit += tempFitnessForParByDeviationFromMean(user_objs,item_objs, cUser_obj,par,simUsers,sIndex,topK);
		}
		return fit/missIndexs.size();
	}
	
	public static double tempFitnessForParByDeviationFromMean(QoSObject[] user_objs,  QoSObject[] item_objs,QoSObject cUser_obj,double[] par, Map<Integer, Double> simUsers,int sIndex, int topK)
	{
		double fit = 0;
		double pccSum = 0;
		Iterator<Map.Entry<Integer, Double>> it = simUsers.entrySet().iterator();
		while(topK>0 && it.hasNext())
		{
			Map.Entry<Integer, Double> entry = it.next();
			int simUserId = entry.getKey();
			double sim = entry.getValue();			
			if(sim<=0)
				break;
			QoSObject simUser = user_objs[simUserId];
			if(simUser.getQos()[sIndex]<1e-10)
				continue;
			//Outlier control
			if(StatisticsMethod.isAbnAttr(simUser, item_objs[sIndex], simUser.getQos()[sIndex]))
				continue;
			topK--;
			double temp = Math.abs((simUser.getQos()[sIndex] - simUser.getStInformation().getAvg())
					      -(par[sIndex]-cUser_obj.getStInformation().getAvg()));
			fit += sim * temp;
			pccSum += sim;
		}
		if(pccSum>0)
			fit = fit/pccSum;
		return fit;
	}
	
		
	
}
