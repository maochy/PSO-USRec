package cn.jxufe.prediction;
import cn.jxufe.entity.Param;
public class IMean {
	/**
	 * @method:IMEAN
	 * @description:Predict the missing QoS record by the mean of the service
	 *             observed by different users for the active user
	 * @param tMatrix: Historical QoS records(user-service QoS matrix)           
	 * @param param: Parameter setting
	 * @return: QoS prediction result
	 */
	public static double[][] iMean_method(double[][] tMatrix, Param param) {
		int uNum = param.getIntUserNum();// The number of the users
		int sNum = param.getIntServiceNum();// The number of the cloud services		
		double[] iMeans = new double[sNum];//The array iMeans used to record the average QoS value observed by different users for each cloud service
		double gMean = 0;//The variable gMean used to record the global average 
		int count = 0;
        int totalCount = 0;
        ////Calculate gMean and iMeans
		for (int s = 0; s < sNum; s++) {
			for (int u = 0; u < uNum; u++) {
				if(tMatrix[u][s]>0)
				{
					iMeans[s] += tMatrix[u][s];
					gMean += tMatrix[u][s];
					count++;
					totalCount++;
				}
			}
			if(count>0)
				iMeans[s] /= count;
			count = 0;
		}
		gMean /= totalCount;
		//Make prediction
		double[][] pres = new double[uNum][sNum];
		for (int i = 0; i < uNum; i++) {
			for (int j = 0; j < sNum; j++) {
				if(tMatrix[i][j]<=0)
				{
					if(iMeans[j] <= 0)						
						pres[i][j] = gMean;
					else {
						pres[i][j] = iMeans[j];
					}
					count++;
				}
			}
		}
		return pres;
	}

}
