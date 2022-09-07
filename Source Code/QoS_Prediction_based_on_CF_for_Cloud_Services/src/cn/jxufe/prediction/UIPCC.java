package cn.jxufe.prediction;

import java.io.IOException;
import cn.jxufe.entity.Param;

public class UIPCC {
	/**
	 * @method: Hybrid method
	 * @description: Combine the UPCC and IPCC by a balanced weight parameter
	 * @param tMatrix: Historical QoS records(user-service QoS matrix)
	 * @param param: Parameter setting
	 * @return: QoS prediction result
	 */
	public static double[][] uipcc_method(double[][] tMatrix, Param param) throws IOException {
		double[][] pres_upcc = UPCC.upcc_method(tMatrix, param);//upcc method
		double[][] pres_ipcc = IPCC.ipcc_method(tMatrix,param);//ipcc method
		double[][] pres_uipcc = new double[339][5825];
		for (int i = 0; i < pres_uipcc.length; i++) {
			for (int j = 0; j < pres_uipcc[i].length; j++) {
				//Combine the prediction results of upcc and ipcc
				pres_uipcc[i][j] = param.getDoubleLamada()*pres_upcc[i][j] + (1-param.getDoubleLamada())*pres_ipcc[i][j];
			}
		}
		return pres_uipcc;
	}

}
