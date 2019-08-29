package GA;

import java.util.List;

public class Crossover3 extends Crossover{
	
	//Basic two parents one child single crossover
	
	@Override 
	public int[] crossover(List<ga_struct> population) {
		
		int[] a = new int[3];
		
		a[0] = 0;
		a[1] = (int)(Math.random()*population.get(0).str.length());
		a[2] = population.get(0).str.length();
		
		return a;
		
	}

}
