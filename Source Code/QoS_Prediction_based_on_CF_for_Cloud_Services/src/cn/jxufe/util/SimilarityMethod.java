package cn.jxufe.util;

import java.util.ArrayList;

import cn.jxufe.entity.QoSObject;

public class SimilarityMethod {
		
	/**
	 * @description:Pearson correlation coefficient between sObj and tObj
	 * @param sObj
	 * @param tObj
	 * @return:Similarity
	 */
	public static double calNormalPccForTwoObjs(QoSObject sObj,QoSObject tObj)
	{
		//The indexes of services that both two sObj and tObj have rated
		ArrayList<Integer> comSet = ToolUtil.getComSetOfTwoObj(sObj, tObj);
		if(comSet.size()<2)
			return -1;
		double member = 0;
		double Denominator_objectUser = 0;
		double Denominator_sourceUser  = 0;
		double avg_source = sObj.getStInformation().getAvg();
		double avg_target = tObj.getStInformation().getAvg();

		for(int i = 0;i<comSet.size();i++)
		{
			int index = comSet.get(i);
			member += (sObj.getQos()[index]-avg_source)*(tObj.getQos()[index]-avg_target);
			Denominator_objectUser += (sObj.getQos()[index]-avg_source)*(sObj.getQos()[index]-avg_source);
			Denominator_sourceUser += (tObj.getQos()[index]-avg_target)*(tObj.getQos()[index]-avg_target);
		}
		if(Math.abs(member)<0.000001)
			return 0;
		return member/Math.sqrt(Denominator_objectUser)/Math.sqrt(Denominator_sourceUser);
		
	}	
	
	/**
	 * @description:Calculate Similarities for objs by Pearson correlation coefficient
	 * @param objs
	 * @return:A Similarity Matrix 
	 */
	public static double[][] calNormalPccForAllObjs(QoSObject[] objs)
	{
		double[][] sims = new double[objs.length][objs.length];
		for (int i = 0; i < sims.length; i++) {
			sims[i][i] = -2;
			for (int j = i+1; j < sims.length; j++) {
				sims[i][j] = calNormalPccForTwoObjs(objs[i], objs[j]);
				sims[j][i] = sims[i][j];
			}
		}		
		return sims;
	}	

}
