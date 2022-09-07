package cn.jxufe.util;

public class ERRMetrics {
	
	//Mean absolute error for all
	public static double calMaeForAll(double[][] pres,double[][] realData)
	{
		double mae = 0;
		int count = 0;
		for(int i = 0;i<realData.length;i++)
		{
			for(int j = 0;j<realData[i].length;j++)
			{
				if(realData[i][j]>0.000001){
					mae += Math.abs(realData[i][j] - pres[i][j]);
					count++;
				}
				
			}
		}
		if(count == 0)
			return 0;
		return mae/count;
	}
	
	//Mean absolute error for one
	public static double calMaeForOne(double[] preArray,double[] real)
	{
		double mae = 0;
		int count = 0;
		for(int i = 0;i<real.length;i++)
		{
			if(real[i]>0.000001)
			{
				mae += Math.abs(preArray[i] - real[i]);
				count++;
			}
		}
		return mae/count;
	}
	
	//Root squared error for all	
	public static double calRmseForAll(double[][] pre,double[][] real){
		double rMse = 0;
		int count = 0;
		for(int i = 0;i<real.length;i++)
		{
			for(int j = 0;j<real[i].length;j++)
			{
				if(real[i][j]>0.000001){
					rMse +=  Math.pow(real[i][j] - pre[i][j],2);
					count++;
				}
				
			}
		}
		if(count == 0)
			return 0;
		return Math.sqrt(rMse/count); 
	}
	
	//Root squared error for one
	public static double calRmseForOne(double[] preArray,double[] real)
	{
		double rMse = 0;
		int count = 0;
		for(int i = 0;i<real.length;i++)
		{
			if(real[i]>0.000001)
			{
				rMse += Math.pow((preArray[i] - real[i]),2);
				count++;
			}
		}
		
		return Math.sqrt(rMse/count);
	}

}
