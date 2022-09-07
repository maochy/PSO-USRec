package cn.jxufe.main;

import java.io.IOException;

import cn.jxufe.entity.Param;
import cn.jxufe.prediction.UIPCC;
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
		double[][] pres = UIPCC.uipcc_method(tMatrix, param);
		System.out.println("pres_mae:" + ERRMetrics.calMaeForAll(pres, rMatrix));
		System.out.println("pres_rmse:" + ERRMetrics.calRmseForAll(pres, rMatrix));
		
		
	}
}
