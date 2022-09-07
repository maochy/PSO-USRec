package cn.jxufe.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import cn.jxufe.entity.QoSObject;

public class ToolUtil {
	
	//Get Indexes of the common entries that both sObj and tObj have rated
	@SuppressWarnings("unchecked")
	public static ArrayList<Integer> getComSetOfTwoObj(QoSObject sObj,QoSObject tObj)
	{
		ArrayList<Integer> comSet = new ArrayList<Integer>();
		comSet = (ArrayList<Integer>) sObj.getStInformation().getIndexes().clone();
		comSet.retainAll(tObj.getStInformation().getIndexes());
		return comSet;
	}
	
	//Get common indexes set of objs
	public static ArrayList<Integer> getComsetOfLists(QoSObject[] objs,Map<Integer, Double> simUsers)
	{
		ArrayList<Integer> comSet = new ArrayList<Integer>();
		Iterator<Map.Entry<Integer, Double>> it = simUsers.entrySet().iterator();
		if(it.hasNext())
		{
			Map.Entry<Integer, Double> en = it.next();
			comSet = objs[en.getKey()].getStInformation().getMissIndexs();
		}
		while(it.hasNext())
		{
			Map.Entry<Integer, Double> entry = it.next();
			int simUserId = entry.getKey();
			comSet.retainAll(objs[simUserId].getStInformation().getMissIndexs());	
		}
		return comSet;
	}
	
	// Convert array to map
	public static Map<Integer, Double> Array2Map(double[] array)
	{
		Map<Integer,Double> map = new HashMap<>();
		for(int i = 0;i<array.length;i++)
		{
			map.put(i, array[i]);
		}
		return map;
	}
	
	//Sort the map by value and take the maximum topK elements
	public static <K, V extends Comparable<? super V>> Map<K, V> mapSortByValue(Map<K, V> map,int topK) {
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return -(o1.getValue()).compareTo(o2.getValue());
            }
        });
        
        Map<K, V> result = new LinkedHashMap<>();
        int count = 0;
        for (Map.Entry<K, V> entry : list) {
        	if(((Double)entry.getValue())>0)
        	{
        		result.put(entry.getKey(), entry.getValue());
        		count++;
        		if(count>=topK)
        			break;
        	}
        	else
        	{ 
        		break;
        	}
        }
        return result;
    }
	
	//Sort the map by value 
	public static <K, V extends Comparable<? super V>> Map<K, V> mapSortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return -(o1.getValue()).compareTo(o2.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
