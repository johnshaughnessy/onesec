package com.example.onesec.impl.second;

import java.util.ArrayList;
import java.util.List;


public class Cabinet {
	public static ArrayList<Second> allSeconds;	
	
	public Cabinet() {
		allSeconds = new ArrayList<Second>();
	}
	
	public static void add(Second newItem) {
		allSeconds.add(newItem);
	}
	
	public void remove(Second oldItem) {
		allSeconds.remove(oldItem);
	}
	
	public Second getSecond(int position) {
		return allSeconds.get(position);
	}
	
    public static void setAllSecondsToUnchecked() {
		for (Second sec : allSeconds) {
			sec.setChecked(false);
		}
	}

	public static void setAllSecondsToChecked() {
		for (Second sec : allSeconds) {
			sec.setChecked(true);
		}
	}
	
	public List<Second> getChecked() {
		List<Second> secondsList = new ArrayList<Second>();
		for (Second sec : allSeconds) {
			if (sec.isChecked()) {
				secondsList.add(sec);
			}
		}
		
		return secondsList;
	}
}
