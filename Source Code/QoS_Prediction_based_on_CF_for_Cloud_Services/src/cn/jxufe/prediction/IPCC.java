package cn.jxufe.prediction;

import cn.jxufe.entity.Param;
import cn.jxufe.util.MatrixMethod;

public class IPCC {
	/**
	 * @method: item-based method
	 * @description: Employ the known QoSs of the similar services to make prediction
	 * @param tMatrix: Historical QoS records(user-service QoS matrix)
	 * @param param: Parameter setting
	 * @return: QoS prediction result
	 */
	public static double[][] ipcc_method(double[][] tMatrix, Param param)
	{
		//Take the transpose of the user-service QoS matrix, then employ upcc to make prediction
		double[][] tranMatrix = MatrixMethod.matrixTranspose(tMatrix);
		double[][] tPres = UPCC.upcc_method(tranMatrix, param);
		return MatrixMethod.matrixTranspose(tPres);// Take the transpose of the result matrix
	}

}
