package com.example.onesec;

import java.util.ArrayList;

import com.example.onesec.impl.cake.Batter;
import com.example.onesec.impl.cake.Cake;
import com.example.onesec.impl.second.Second;

public class Kitchen {
	public static ArrayList<Second> allSeconds;	
	public static ArrayList<Batter> allBatters;
	public static ArrayList<Cake>   allCakes;
	
	public Kitchen(){
		allCakes = new ArrayList<Cake>();
		allBatters = new ArrayList<Batter>();
		allSeconds = new ArrayList<Second>();
	}
	
	public static Cake getCake(String id){
		for (Cake cake : allCakes ) {
			if (cake.getId().equals(id)){
				return cake;
			}
		}
		return null;
	}
	
	public static Batter getBatter(String id){
		for (Batter batter : allBatters ) {
			if (batter.getId().equals(id)){
				return batter;
			}
		}
		return null;
	}
	
	public static Second getSecond(String id){
		// TODO this is inefficient
		for (Second second : allSeconds ) {
			if (second.getId().equals(id)){
				return second;
			}
		}
		return null;
	}
}
