package cn.jxufe.prediction;
import cn.jxufe.entity.Param;

public class GMean{
	/**
	 * @method:GMEAN
	 * @description:Employ the average value of the whole dataset as the predicted value
	 * @param tMatrix: Historical QoS records(user-service QoS matrix)
	 * @param param: Parameter setting
	 * @return: QoS prediction result
	 */
	public static double[][] gMean_method(double[][] tMatrix, Param param) {
		int uNum = param.getIntUserNum();// The number of the users
		int sNum = param.getIntServiceNum();// The number of the cloud services
		double gMean = 0;//The variable gMean used to record the global average 
		int count = 0;
		//Calculate gMean
		for (int i = 0; i < uNum; i++) {
			for (int j = 0; j < sNum; j++) {
				if(tMatrix[i][j]>0)
				{
					gMean += tMatrix[i][j];
					count++;
				}
			}
		}
		gMean /= count;
		//Make prediction
		double[][] pres = new double[uNum][sNum];
		for (int i = 0; i < uNum; i++) {
			for (int j = 0; j < sNum; j++) {
				if(tMatrix[i][j]<=0)
				{
					pres[i][j] = gMean;
				}
			}
		}
		return pres;
	}

}
