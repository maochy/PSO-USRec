package cn.jxufe.prediction;

import java.util.ArrayList;
import java.util.Map;
import cn.jxufe.entity.Param;
import cn.jxufe.entity.QoSObject;
import cn.jxufe.util.CFMethods;
import cn.jxufe.util.DataInstall;
import cn.jxufe.util.FitnessMethod;
import cn.jxufe.util.GenerateParMethod;
import cn.jxufe.util.MatrixMethod;
import cn.jxufe.util.SimilarityMethod;
import cn.jxufe.util.StatisticsMethod;
import cn.jxufe.util.ToolUtil;

public class PSO_URec {
	/**
	 * @method: PSO_URec method
	 * @description: Search-based prediction method from the user perspective
	 * @param tMatrix: Historical QoS records(user-service QoS matrix)
	 * @param param: Parameter setting
	 * @return: QoS prediction result
	 */
	public static double[][] pso_URec_method(double[][] tMatrix,Param param)
	{
		int uNum = param.getIntUserNum();// The number of the users
		int sNum = param.getIntServiceNum();// The number of the cloud services
		double[][] pres = new double[uNum][sNum];//Prediction result
		
		//Array of user objects
		QoSObject[] user_objs = DataInstall.getAllQosObject(tMatrix);
		
		//Array of service objects
		QoSObject[] item_objs = DataInstall.getAllQosObject(MatrixMethod.matrixTranspose(tMatrix));
		
		//Similarity calculation of users
		double[][] sims = SimilarityMethod.calWeightPccForAllObjs(user_objs);
		
		double[][] user_pres_upcc = CFMethods.upcc_method(tMatrix,param);//upcc result
		double[][] user_pres_ipcc = CFMethods.ipcc_method(tMatrix,param);//ipcc result
	
		//Make prediction 
		for (int i = 0; i < pres.length; i++) {
			//Make prediction for user i
			pres[i] = pso_URec_prediction_for_user(user_objs,item_objs,sims,user_pres_upcc[i],user_pres_ipcc[i],i,param);
			System.out.println("user:" + i);
		}
		
		return pres;
	}

	/**
	 * @description: Search the missing QoS values for current user
	 * @param user_objs: User set
	 * @param item_objs: Service set
	 * @param sims: Similarity 
	 * @param user_pre_upcc: UPCC result
	 * @param user_pre_ipcc: IPCC result
	 * @param index: The index of current user
	 * @param param: Parameter setting
	 * @return: Prediction result
	 */
	private static double[] pso_URec_prediction_for_user(QoSObject[] user_objs,
			QoSObject[] item_objs, double[][] sims,double[] user_pre_upcc,double[]user_pre_ipcc,  int index, Param param) {
		int topK = param.getIntTopK();//The number of the neighbors selected for prediction
		int maxIteration = param.getIntMaxIteration();//Maximum number of iterations
		int pNum = param.getIntPNum();//Size of swarm				
		//Find the topK most similar neighbors for current user
		Map<Integer, Double> tempMap= ToolUtil.Array2Map(sims[index]);
		Map<Integer,Double> simUsers = ToolUtil.mapSortByValue(tempMap);		
		//Initialize the swarm
		double[][] ps = new double[pNum][user_pre_upcc.length];
		ps[0] = user_pre_ipcc.clone();//UPCC particle
		ps[1] = user_pre_upcc.clone();//IPCC particle		
		//UIPCC particles
		for (int i = 2; i < 21; i++) {
			ps[i] = GenerateParMethod.generateParByUIPCC(user_pre_upcc, user_pre_ipcc, (i-1)*0.05);
		}		
		//Random particles
		for (int i = 21; i < ps.length; i++) {
			ps[i] = GenerateParMethod.generateRandomParticleForPsoURec(user_objs[index], item_objs);
		}		
		//Global best
		double[] gbest = new double[ps[0].length];
		double gFit = Double.MAX_VALUE;		
		//Personal best
		double[][] pbest = new double[ps.length][ps[0].length];
		double[] pFits = new double[ps.length];		
		for (int i = 0; i < ps.length; i++) {
			// Fitness value of particle
			double fit = FitnessMethod.calFitnessForParByDeviationFromMean(user_objs, item_objs, user_objs[index], ps[i], simUsers,topK);
			pFits[i] = fit;
			pbest[i] = ps[i].clone();
			if(fit<gFit)
			{
				gFit = fit;
				gbest = ps[i].clone();
			}
			
		}

		//Optimum solution searching
		int iteration = 0;
		while(iteration<maxIteration)
		{
			for(int j = 0;j<ps.length;j++)
			{
				//Update particle
				updateParticle(ps[j],pbest[j],gbest,user_objs[index],item_objs);				
				//Calculate fitness value of particle
				double fitness = FitnessMethod.calFitnessForParByDeviationFromMean(user_objs, item_objs, user_objs[index], ps[j], simUsers,topK);
				if(fitness<pFits[j])
				{
					pbest[j] = ps[j].clone();
					pFits[j] = fitness;
				}
				
			}
			int pIndex = StatisticsMethod.getIndexOfMinValueForArray(pFits);
			if(pFits[pIndex] < gFit)
			{
				gFit = pFits[pIndex];
				gbest = pbest[pIndex].clone();
				
			}
			iteration++;
		}
		
		return gbest;
	}	
	
	//Update particle
	public static void updateParticle(double[] qos,double[] pBest,double[] gBest, QoSObject currentUser,QoSObject[] items)
	{
		ArrayList<Integer> missingIndexlist = currentUser.getStInformation().getMissIndexs();
		for(int i = 0;i<missingIndexlist.size();i++)
		{
			int missIndex = missingIndexlist.get(i);
			double rad1 = Math.random();
			double rad2 = Math.random();
			double temp = 0.8*qos[missIndex]+2*rad1*(pBest[missIndex]-qos[missIndex])+2*rad2*(gBest[missIndex]-qos[missIndex]);
			//Outlier control
			qos[missIndex] = StatisticsMethod.getAbnHandleValue(currentUser, items[missIndex], temp);
		}
	}	
}
