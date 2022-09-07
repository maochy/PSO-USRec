package cn.jxufe.prediction;
import cn.jxufe.entity.Param;

public class UMean {
	/**
	 * @method:UMEAN
	 * @description:Take the average value of existing QoSs observed by the active user 
	 *                    as the predicted value to his/her unknown QoSs
	 * @param tMatrix: Historical QoS records(user-service QoS matrix)
	 * @param param: Parameter setting
	 * @return: QoS prediction result
	 */
	public static double[][] uMean_method(Double[][] tMatrix, Param param) {
		int uNum = param.getIntUserNum();// The number of the users
		int sNum = param.getIntServiceNum();// The number of the cloud services		
		double[] uMeans = new double[uNum];//The array uMeans used to record the average QoS value for each user
		int count = 0;
		//Calculate uMeans
		for (int i = 0; i < uNum; i++) {
			for (int j = 0; j < sNum; j++) {
				if(tMatrix[i][j]>0)
				{
					uMeans[i] += tMatrix[i][j];
					count++;
				}
			}
			uMeans[i] /= count;
			count = 0;
		}
		
		//Make prediction
		double[][] pres = new double[uNum][sNum];
		for (int i = 0; i < uNum; i++) {
			for (int j = 0; j < sNum; j++) {
				if(tMatrix[i][j] <= 0)
				{
					pres[i][j] = uMeans[i];
				}
			}
		}
		return pres;
	}

}
