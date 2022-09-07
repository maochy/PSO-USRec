package cn.jxufe.entity;

/**
 * @description: Parameter setting
 *
 */
public class Param {
	
	private String pNum;//Size of swarm
	
	private String maxIteration;//Maximum number of iterations
	
	private String topK;//Number of the neighbors selected for prediction
	
	private String userNum;//Number of the users 
	
	private String serviceNum;//Number of the cloud services
	
	private String trainDataPath;//The training data path
	
	private String trainDataName;//File name of the training data
	
	private String realDataPath;//The real data path
	
	private String realDataName;//File name of the real data	
	
	private String lamada;//Combining weight
	

	public String getLamada() {
		return lamada;
	}

	public void setLamada(String lamada) {
		this.lamada = lamada;
	}

	public String getpNum() {
		return pNum;
	}

	public void setpNum(String pNum) {
		this.pNum = pNum;
	}

	public String getRealDataPath() {
		return realDataPath;
	}

	public void setRealDataPath(String realDataPath) {
		this.realDataPath = realDataPath;
	}

	public String getRealDataName() {
		return realDataName;
	}

	public void setRealDataName(String realDataName) {
		this.realDataName = realDataName;
	}

	public String getPNum() {
		return pNum;
	}

	public void setPNum(String pNum) {
		this.pNum = pNum;
	}

	public String getMaxIteration() {
		return maxIteration;
	}

	public void setMaxIteration(String maxIteration) {
		this.maxIteration = maxIteration;
	}

	public String getTopK() {
		return topK;
	}

	public void setTopK(String topK) {
		this.topK = topK;
	}

	public String getUserNum() {
		return userNum;
	}

	public void setUserNum(String userNum) {
		this.userNum = userNum;
	}

	public String getServiceNum() {
		return serviceNum;
	}

	public void setServiceNum(String serviceNum) {
		this.serviceNum = serviceNum;
	}

	public String getTrainDataPath() {
		return trainDataPath;
	}

	public void setTrainDataPath(String trainDataPath) {
		this.trainDataPath = trainDataPath;
	}

	public String getTrainDataName() {
		return trainDataName;
	}

	public void setTrainDataName(String trainDataName) {
		this.trainDataName = trainDataName;
	}

	public int getIntPNum()
	{
		return Integer.parseInt(pNum);
	}
	
	public int getIntMaxIteration()
	{
		return Integer.parseInt(maxIteration);
	}
	
	public int getIntUserNum()
	{
		return Integer.parseInt(userNum);
	}
	
	public int getIntServiceNum()
	{
		return Integer.parseInt(serviceNum);
	}
	
	public int getIntTopK()
	{
		return Integer.parseInt(topK);
	}
	
	@Override
	public String toString() {
		return "Param [pNum=" + pNum + ", maxIteration=" + maxIteration
				+ ", topK=" + topK + ", userNum=" + userNum + ", serviceNum="
				+ serviceNum + ", trainDataPath=" + trainDataPath
				+ ", trainDataName=" + trainDataName + ", realDataPath="
				+ realDataPath + ", realDataName=" + realDataName + ", lamada="
				+ lamada + "]";
	}

	public double getDoubleLamada()
	{
		return Double.parseDouble(lamada);
	}
	
	

}
