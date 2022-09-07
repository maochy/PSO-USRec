package cn.jxufe.util;
import cn.jxufe.entity.QoSObject;
public class GenerateParMethod {
	//Generate random particle for PSO_URec
	public static double[] generateRandomParticleForPsoURec(QoSObject user, QoSObject[] items)
	{
		double[] particle = new double[user.getQos().length];
		for(int i = 0;i<particle.length;i++)
		{
			if(user.getQos()[i]>1e-10)
				particle[i] = user.getQos()[i];
			else
			{
				double avg = user.getStInformation().getAvg();
				double var = user.getStInformation().getVariance();
				if(Math.abs(avg)<=1e-10 &&  Math.abs(var)<=1e-10)
				{
					particle[i] = user.getStInformation().getAvg();
					continue;
				}
				//Generate normally distributed random number
				double value = StatisticsMethod.getMemberOfNorm(avg, var);
				//Outlier control
				particle[i] = StatisticsMethod.getAbnHandleValue(user, items[i], value);
			}
		}
		
		return particle;
	}
	
	
	
    //generateRandomPaticle for PSO_IRec
	public static double[] generateRandomParticleForPsoIRec(QoSObject item, QoSObject[] user_objs)
	{
		double[] particle = new double[item.getQos().length];
		for(int i = 0;i<particle.length;i++)
		{
			if(item.getQos()[i]>1e-10)
				particle[i] = item.getQos()[i];
			else
			{
				double avg = item.getStInformation().getAvg();
				double var = item.getStInformation().getVariance();
				if(Math.abs(avg)<=1e-10 &&  Math.abs(var)<=1e-10)
				{
					particle[i] = item.getStInformation().getAvg();
					continue;
				}
				double value = StatisticsMethod.getMemberOfNorm(avg, var);
				particle[i] = StatisticsMethod.getAbnHandleValue(item, user_objs[i], value);
				
			}
		}
		
		return particle;
	}
	

	//uipcc method
	public static double[] generateParByUIPCC(double[] pre_upcc,double[] pre_ipcc, double lamada)
	{
		double[] par = new double[pre_upcc.length];
		for (int i = 0; i < par.length; i++) {
			par[i] = lamada*pre_upcc[i] + (1-lamada)*pre_ipcc[i];
		}
		return par;
	}
}
