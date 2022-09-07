package cn.jxufe.entity;
/** 
 * @description:Information of one user or one service
*/
public class QoSObject {
       //Index of one user or one service
		private int objNo;
		
		//QoS values of one user or one service
		private double[] qos;
		
		//Statistic information of the QoS values
		private StatisticInformation stInformation;

		public double[] getQos() {
			return qos;
		}

		public void setQos(double[] qos) {
			this.qos = qos;
		}

		public StatisticInformation getStInformation() {
			return stInformation;
		}

		public void setStInformation(StatisticInformation stInformation) {
			this.stInformation = stInformation;
		}

		public int getObjNo() {
			return objNo;
		}

		public void setObjNo(int objNo) {
			this.objNo = objNo;
		}	
		
		
		//test
		public void printQoSInformation()
		{
			System.out.print(this.getObjNo()+":");
			for(int i = 0; i<this.getStInformation().getIndexes().size();i++)
			{
				int index = this.getStInformation().getIndexes().get(i);
				System.out.print("("+index + ")" + this.getQos()[index]+"\t");
			}
			System.out.println();
		}
}
