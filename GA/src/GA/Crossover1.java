package GA;

import java.util.*;
import java.util.List;

public class Crossover1 extends Crossover{
	
	public static final int CROSS_CONST = 10;
	
	//Uniform crossover

	@Override 
	public int[] crossover(List<ga_struct> population) {
		
		int[] a = new int[CROSS_CONST+2];
		
		a[0] = 0;
		a[CROSS_CONST+1] = population.get(0).str.length();
		for(int i=0; i<CROSS_CONST; i++)
			a[i+1] = (int)(Math.random()*population.get(0).str.length());
		
		Arrays.sort(a);
		
		
		
		
		return a;
		
	}
}
