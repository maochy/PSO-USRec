package cn.jxufe.util;

public class MatrixMethod {
	/**
	 * @description: Take the transpose of a matrix
	 * @param matrix: The original matrix
	 * @return:Matrix transpose
	 */
	public static double[][] matrixTranspose(double[][] matrix)
	{
		double[][] tranQos = new double[matrix[0].length][matrix.length];
		for(int i = 0;i<matrix.length;i++)
		{
			for(int j = 0;j<matrix[i].length;j++)
			{
				tranQos[j][i] = matrix[i][j];
			}
		}
		
		return tranQos;
	}
}
