package cn.jxufe.util;
import java.util.ArrayList;
import cn.jxufe.entity.StatisticInformation;

public class StatisticsMethod {
	/**
	 * @description: Statistics of the user-service matrix	
	 * @param qos: User-service matrix
	 * @return: StatisticInformation
	 */
	public static StatisticInformation getStatisticInformation(double[] qos)
	{
		StatisticInformation information = new StatisticInformation();
		double avg = getAvgOfArray(qos);
		double var = getVarianceOfArray(qos);
		ArrayList<Integer> indexes = getIndexOfArray(qos);
		ArrayList<Integer> missIndexes = getMissIndexOfArray(qos);
		information.setIndexes(indexes);
		information.setMissIndexs(missIndexes);
		information.setAvg(avg);
		information.setVariance(var);
		return information;
			
	}
	
	//Find the indexes of the nonzero entries of the user-service matrix
	public static ArrayList<Integer> getIndexOfArray(double[] qos)
	{
		ArrayList<Integer> indexs = new ArrayList<Integer>();
		for(int i = 0;i<qos.length;i++)
		{
			if(qos[i] > 0)
			{
				indexs.add(i);
			}
		}
		
		return indexs;
	}
	
	//Find the indexes of the missing entries of the user-service matrix
	public static ArrayList<Integer> getMissIndexOfArray(double[] qos)
	{
		ArrayList<Integer> indexs = new ArrayList<Integer>();
		for(int i = 0;i<qos.length;i++)
		{
			if(qos[i] > 0)
			{
				continue;
			}
			else {
				indexs.add(i);
			}
		}
		
		return indexs;
	}
	
	//Calculate the average of nonzero elements of array 
	public static double getAvgOfArray(double[] qos)
	{
		double avg = 0;
		int count = 0;
		for(int i = 0;i<qos.length;i++)
		{
			if(qos[i] > 0)
			{
				avg += qos[i];
				count++;
			}
		}
		
		if(count>0)
		{
			avg = (avg/count);
		}
		
		return avg;
	}
	
	//Calculate the variance of nonzero elements of array
	public static double getVarianceOfArray(double[] qos)
	{
		double variance = 0;
		int count = 0;
		double average = getAvgOfArray(qos);
		for(int i = 0;i<qos.length;i++)
		{
			if(qos[i] > 0)
			{
				variance += (qos[i]-average) * (qos[i]-average);
				count++;
			}
		}
		if(count<=1)
			return 0;
		else
			return variance/(count);
			
	}

}
