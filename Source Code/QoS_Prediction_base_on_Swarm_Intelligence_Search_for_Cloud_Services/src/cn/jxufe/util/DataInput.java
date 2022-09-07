package cn.jxufe.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class DataInput {
	/**
	 * @description:Read data from txt file to a 2d array
	 * @param fileName: The file path of the txt
	 * @param rowCount: Row size of the 2d array
	 * @param colCount: Column size of the 2d array
	 * @return: A 2d array
	 */
	public static double[][] getDataMatrixFromTxt(String fileName, int rowCount, int colCount)
	{
		double[][] matrix = new double[rowCount][colCount];
		File file = new File(fileName);
		if(file.isFile() && file.exists())
		{
			try {
				InputStreamReader readInputStream = null;
				try {
					readInputStream = new InputStreamReader(new FileInputStream(file),"utf-8");
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					System.out.println("Failed to read data from TXT");
				}
				BufferedReader reader = new BufferedReader(readInputStream);
				String lineString = null;
				try {
					int rowIndex = 0;
					while((lineString = reader.readLine()) != null)
					{
						String[] strs = lineString.split("\t");
						for(int i = 0;i<strs.length;i++)
						{
							if(i>=colCount)
								break;
							matrix[rowIndex][i] = Double.valueOf(strs[i].trim());
						}
						rowIndex++;
						if(rowIndex>=rowCount)
							break;
					}
					reader.close();
				} catch (IOException e) {
					System.out.println("Failed to read contents");
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				System.out.println("Failed to read file");
				e.printStackTrace();
			}
			
		}
		else {
			System.out.println("The file doesn't exist");
		}
		
		return matrix;
	}

}
