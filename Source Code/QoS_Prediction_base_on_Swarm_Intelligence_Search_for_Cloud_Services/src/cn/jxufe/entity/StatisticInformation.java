package cn.jxufe.entity;

import java.util.ArrayList;
/**
 *@description: Statistics of a sequence
*/
public class StatisticInformation {
	
	private double avg;//Average of the sequence
	private double variance;//Variance of the sequence
	private ArrayList<Integer> indexes;//Indexes of the nonzero terms
	private ArrayList<Integer> missIndexes;//Indexes of the zero terms

	public double getAvg() {
		return avg;
	}

	public void setAvg(double avg) {
		this.avg = avg;
	}

	public double getVariance() {
		return variance;
	}

	public void setVariance(double variance) {
		this.variance = variance;
	}

	public ArrayList<Integer> getIndexes() {
		return indexes;
	}

	public void setIndexes(ArrayList<Integer> indexes) {
		this.indexes = indexes;
	}

	public ArrayList<Integer> getMissIndexs() {
		return missIndexes;
	}

	public void setMissIndexs(ArrayList<Integer> missIndexes) {
		this.missIndexes = missIndexes;
	}
	
	

}
