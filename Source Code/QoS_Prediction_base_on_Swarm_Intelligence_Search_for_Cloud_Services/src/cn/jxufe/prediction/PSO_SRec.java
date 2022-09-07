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

public class PSO_SRec {
	/**
	 * @method: PSO_IRec method
	 * @description: Search-based prediction method from the service perspective
	 * @param trainDataPath: The path of training dataset file
	 * @param param: Parameter setting
	 * @return: QoS prediction result
	 */
	public static double[][] pso_SRec_method(double[][] tMatrix, Param param)
	{
		
		double[][] tranpose_Matrix1 = MatrixMethod.matrixTranspose(tMatrix);
		//Prediction result
		double[][] pres = new double[tranpose_Matrix1.length][tranpose_Matrix1[0].length];
		
		//Array of service objects
		QoSObject[] item_objs = DataInstall.getAllQosObject(tranpose_Matrix1);
		
		//Array of user objects
		QoSObject[] user_objs = DataInstall.getAllQosObject(tMatrix);
		
		//Similarity calculation of services
		double[][] sims = SimilarityMethod.calWeightPccForAllObjs(item_objs);
		
		//Similarity calculation of services
		double[][] item_pres_upcc = MatrixMethod.matrixTranspose(CFMethods.upcc_method(tMatrix,param));//upcc result
		double[][] item_pres_ipcc = MatrixMethod.matrixTranspose(CFMethods.ipcc_method(tMatrix,param));//ipcc result
				
		//Make prediction
		for (int i = 0; i < item_objs.length; i++) {
			//Make prediction for service i
			pres[i] = pso_IRec_prediction_for_service(item_objs,user_objs, sims,item_pres_upcc[i],item_pres_ipcc[i],i,param);
			System.out.println("service:" + i);
		}		
		return MatrixMethod.matrixTranspose(pres);
	}
	
	/**
	 * @description: Search the missing QoS values for current service
	 * @param user_objs: User set
	 * @param item_objs: Service set
	 * @param sims: Similarity 
	 * @param user_pre_upcc: UPCC result
	 * @param user_pre_ipcc: IPCC result
	 * @param index: The index of current service
	 * @param param: Parameter setting
	 * @return: Prediction result
	 */
	private static double[] pso_IRec_prediction_for_service(QoSObject[] item_objs,
			QoSObject[] user_objs, double[][] sims, double[] item_pre_upcc,
			double[] item_pre_ipcc, int index, Param param) {
		int topK = param.getIntTopK();////The number of the neighbors selected for prediction
		int maxIteration = param.getIntMaxIteration();////Maximum number of iterations
		int pNum = param.getIntPNum();//Size of swarm		
		//Find the topK most similar neighbors for current service
		Map<Integer, Double> tempMap= ToolUtil.Array2Map(sims[index]);
		Map<Integer,Double> simItems = ToolUtil.mapSortByValue(tempMap);		
		//Initialize the swarm
		double[][] ps = new double[pNum][item_objs[index].getQos().length];
		ps[0] = item_pre_ipcc.clone();//UPCC particle
		ps[1] = item_pre_upcc.clone();//IPCC particle
		//UIPCC particles
		for (int i = 2; i < 21; i++) {
			ps[i] = GenerateParMethod.generateParByUIPCC(item_pre_upcc, item_pre_ipcc, (i-1)*0.05);
		}		
		//Random particles
		for (int i = 21; i < ps.length; i++) {
			ps[i] = GenerateParMethod.generateRandomParticleForPsoIRec(item_objs[index], user_objs);
		}
		//Global best
		double[] gbest = new double[ps[0].length];
		double gFit = Double.MAX_VALUE;		
		//Personal best
		double[][] pbest = new double[ps.length][ps[0].length];
		double[] pFits = new double[ps.length];		
		for (int i = 0; i < ps.length; i++) {
			// Fitness value of particle
			double fit = FitnessMethod.calFitnessForParByDeviationFromMean(item_objs,user_objs, item_objs[index], ps[i], simItems,topK);
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
				updateParticle(ps[j],pbest[j],gbest,item_objs[index],user_objs);
				//Calculate fitness value of particle
				double fitness =FitnessMethod.calFitnessForParByDeviationFromMean(item_objs, user_objs, item_objs[index], ps[j], simItems,topK);
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
	public static void updateParticle(double[] qos,double[] pBest,double[] gBest, QoSObject currentItem, QoSObject[] user_objs)
	{
		ArrayList<Integer> missingIndexlist = currentItem.getStInformation().getMissIndexs();
		for(int i = 0;i<missingIndexlist.size();i++)
		{
			int missIndex = missingIndexlist.get(i);
			double rad1 = Math.random();
			double rad2 = Math.random();
			double temp = 0.8*qos[missIndex]+2*rad1*(pBest[missIndex]-qos[missIndex])+2*rad2*(gBest[missIndex]-qos[missIndex]);
			//Outlier control
			if(temp<=0.000001 || StatisticsMethod.isAbn(currentItem, user_objs[missIndex], temp))
				temp = currentItem.getStInformation().getAvg();
			qos[missIndex] = temp;
		}
	}
	
}
