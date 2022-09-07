package cn.jxufe.prediction;

import cn.jxufe.entity.Param;

public class PSO_USRec {
	/**
	 * @method: PSO_UIRec method
	 * @description: Search-based prediction method from the user perspective
	 * @param tMatrix: Historical QoS records(user-service QoS matrix)
	 * @param param: Parameter setting
	 * @return: QoS prediction result
	 */
	public static double[][] pso_USRec_method(double[][] tMatrix,Param param)
	{
		int uNum = param.getIntUserNum();// The number of the users
		int sNum = param.getIntServiceNum();// The number of the cloud services	
		double[][] pres_us = new double[uNum][sNum];//QoS prediction result
		double[][] pres_u = PSO_URec.pso_URec_method(tMatrix, param);//PSO_URec
		double[][] pres_s = PSO_SRec.pso_SRec_method(tMatrix, param);//PSO_URec
		for (int i = 0; i < uNum; i++) {
			for (int j = 0; j < sNum; j++) {
				pres_us[i][j] = param.getDoubleLamada()*pres_u[i][j] + (1-param.getDoubleLamada())*pres_s[i][j];
			}
		}
		
		return pres_us;
	}

}
