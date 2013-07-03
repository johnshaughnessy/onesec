package com.example.onesec.impl.cake;

import java.util.ArrayList;
import java.util.List;

import com.example.onesec.impl.second.Second;

public class Batter {

	private List<Integer> idList;
	
	public Batter()
	{
		idList = new ArrayList<Integer>();
	}
	
	public void addSecond(Second second)
	{
		idList.add(second.getId());
	}
	
	public void removeSecond(Second second)
	{
		idList.remove(second);
	}
	
	public void move(int start, int end)
	{
		int idToMove = idList.get(start);
		// Shift all the seconds forward
		for (int i = start; i < end; ++i){
			idList.set(i, idList.get(i+1));
		}
		idList.set(end, idToMove);
	}
}
