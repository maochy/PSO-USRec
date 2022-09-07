package cn.jxufe.util;
import java.util.ArrayList;
import java.util.Random;
import cn.jxufe.entity.QoSObject;
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
	
	/**
	 * @description: Generate random numbers that are normally distributed according to a given mean and varance
	 * @param average
	 * @param variance
	 * @return: A random number
	 */
	public static double getMemberOfNorm(double average, double variance)
	{
		Random rom = new Random();
		double data = 0;
		while(true)
		{
			data = rom.nextGaussian()*Math.sqrt(variance) + average;
			if(data>0)
				break;
		}
		return data;
	}
	
	//Find the index of the minimum element of array
	public static int getIndexOfMinValueForArray(double[] array)
	{
		int index = 0;
		double min = array[0];
		for(int i = 0;i<array.length;i++)
		{
			if(min>array[i])
			{
				min = array[i];
				index = i;
			}
		}
		
		return index;
	}
	
	/**
	 * @description: Outlier checking for the given value
	 */
	public static boolean isAbn(QoSObject user,QoSObject item,double value)
	{
		double mean1 = user.getStInformation().getAvg();
		double mean2 = item.getStInformation().getAvg();
		double var1 = Math.sqrt(user.getStInformation().getVariance());
		double var2 = Math.sqrt(item.getStInformation().getVariance());
		if(((value<mean1-3*var1)||(value>mean1+3*var1))&&((value<mean2-3*var2)||(value>mean2+3*var2)))
		{
			return true;
		}
		else {
			return false;
		}
				
	}

	////Outlier handler
	public static double getAbnHandleValue(QoSObject user,QoSObject item,double value)
	{
		double mean1 = user.getStInformation().getAvg();
		double mean2 = item.getStInformation().getAvg();
		double var1 = Math.sqrt(user.getStInformation().getVariance());
		double var2 = Math.sqrt(item.getStInformation().getVariance());
		double max = (mean1+3*var1>mean2+3*var2)?(mean1+3*var1):(mean2+3*var2);
		double min = (mean1-3*var1>mean2-3*var2)?(mean1-3*var1):(mean2-3*var2);
		if(value<0)
		{
			if(min>1e-10)
				return min;
			else {

				return user.getStInformation().getAvg();
			}
		}
		else if(value>max)
		{
			return max;
		}
		else if(value<min){
			return min;
		}
		else {
			return value;
		}
				
	}

	//Outlier checking
	public static boolean isAbnAttr(QoSObject user,QoSObject item,double value)
	{
		double mean1 = user.getStInformation().getAvg();
		double mean2 = item.getStInformation().getAvg();
		double var1 = Math.sqrt(user.getStInformation().getVariance());
		double var2 = Math.sqrt(item.getStInformation().getVariance());
		double max = (mean1+3*var1>mean2+3*var2)?(mean1+3*var1):(mean2+3*var2);
		double min = (mean1-3*var1>mean2-3*var2)?(mean1-3*var1):(mean2-3*var2);
		if(value>max || value<min)
		{
			return true;
		}		
		else {
			return false;
		}
				
	}
	

}
