package cn.jxufe.main;

import java.io.IOException;

import cn.jxufe.entity.Param;
import cn.jxufe.prediction.PSO_URec;
import cn.jxufe.util.DataInput;
import cn.jxufe.util.ERRMetrics;
import cn.jxufe.util.ParamMethod;

public class Test {
	public static void main(String[] args) throws IOException {
		Param param = new ParamMethod().getParamByPropertiesFile("/param.properties");
		int uNum = param.getIntUserNum();
		int sNum = param.getIntServiceNum();
		String trainDataPath = System.getProperty("user.dir") +"\\expData\\density_20\\rtAttr_20_train.txt";
		String realDataPath = System.getProperty("user.dir") +"\\expData\\density_20\\rtAttr_20_real.txt";
		//Read data from the external txt files		
		double[][] tMatrix = DataInput.getDataMatrixFromTxt(trainDataPath, uNum, sNum);
		double[][] rMatrix = DataInput.getDataMatrixFromTxt(realDataPath, uNum, sNum);
		
		double[][] pres_u = PSO_URec.pso_URec_method(tMatrix, param);
		System.out.println("pres_u_mae:" + ERRMetrics.calMaeForAll(pres_u, rMatrix));
		System.out.println("pres_u_rmse:" + ERRMetrics.calRmseForAll(pres_u, rMatrix));
		System.out.println();
		
	}
}
