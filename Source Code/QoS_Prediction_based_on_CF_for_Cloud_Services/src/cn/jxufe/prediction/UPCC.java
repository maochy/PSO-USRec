package cn.jxufe.prediction;

import java.util.Iterator;
import java.util.Map;

import cn.jxufe.entity.Param;
import cn.jxufe.entity.QoSObject;
import cn.jxufe.util.DataInstall;
import cn.jxufe.util.SimilarityMethod;
import cn.jxufe.util.ToolUtil;

public class UPCC {
	/**
	 * @method: user-based method
	 * @description: Utilize the existing QoSs of the similar users to predict the empty records for the active user
	 * @param tMatrix: Historical QoS records(user-service QoS matrix)
	 * @param param: Parameter setting
	 * @return: QoS prediction result
	 */
	public static double[][] upcc_method(double[][] tMatrix, Param param)
	{
		double[][] pres = new double[tMatrix.length][tMatrix[0].length];//Prediction result
		QoSObject[] user_objs = DataInstall.getAllQosObject(tMatrix);
		//Similarity Calculation
		double[][] sims = SimilarityMethod.calNormalPccForAllObjs(user_objs);
		int topK = param.getIntTopK();//The Number of the neighbors selected for prediction
		for (int i = 0; i < pres.length; i++) {
			//Select the topK most similar neighbors for the user i 
			Map<Integer, Double> tempMap= ToolUtil.Array2Map(sims[i]);
			Map<Integer,Double> simUsers = ToolUtil.mapSortByValue(tempMap);
			//Predict the missing QoSs for user i 
			pres[i] = upccPredictionForOne(user_objs, user_objs[i],simUsers,topK);
		}		
		return pres;
	}

	/**
	 * @description: Predict the missing QoSs for current user
	 * @param user_objs: User set
	 * @param cUser_obj: current user
	 * @param simUsers: Similar Neighbors
	 * @param topK:The Number of the neighbors selected for prediction 
	 * @return: Prediction result
	 */
	private static double[] upccPredictionForOne(QoSObject[] user_objs,
			QoSObject cUser_obj, Map<Integer, Double>simUsers, int topK) {
		int len = cUser_obj.getQos().length;
		double[] pre = new double[len];		
		for (int sIndex = 0; sIndex < pre.length; sIndex++) {
			if(cUser_obj.getQos()[sIndex] > 0)
			{
				pre[sIndex] = cUser_obj.getQos()[sIndex];
			}
			else {
				pre[sIndex] = predictionValue(user_objs, cUser_obj, simUsers, sIndex, topK);
			}
		}
		return pre;
	}
	
	/**
	 * @description: Predict the missing QoS value of specified service for current user
	 * @param user_objs: User set
	 * @param cUser_obj: current user
	 * @param simUsers: Similar Neighbors
	 * @param topK:The Number of the neighbors selected for prediction
	 * @param sIndex: The index of the sIndex missing QoS of current user 
	 * @return: Prediction result
	 */
	public static double predictionValue(QoSObject[] user_objs,
			QoSObject cUser_obj, Map<Integer, Double>simUsers, int sIndex, int topK)
	{
		double pccSum = 0;
		double preValue = 0;
		Iterator<Map.Entry<Integer, Double>> it = simUsers.entrySet().iterator();
		while(topK>0 && it.hasNext())
		{
			Map.Entry<Integer, Double> entry = it.next();
			int sUserId = entry.getKey();
			double sim = entry.getValue();	
			if(sim<=0)
				break;
			QoSObject sObj = user_objs[sUserId];
			if(sObj.getQos()[sIndex] <=0)
				continue;

			topK--;
			pccSum += sim;
			preValue += (sim)*(sObj.getQos()[sIndex] - sObj.getStInformation().getAvg());
		}
		
		if(pccSum <= 0)
			preValue = cUser_obj.getStInformation().getAvg();
		else {
			preValue = preValue/pccSum + cUser_obj.getStInformation().getAvg();
		}
		if(preValue <= 0)
			preValue = cUser_obj.getStInformation().getAvg();
		return preValue;
	}

}
