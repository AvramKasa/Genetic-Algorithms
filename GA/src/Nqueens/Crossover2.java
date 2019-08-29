package Nqueens;

import java.util.List;

public class Crossover2 extends Crossover{
	
	@Override
	public int[] crossover(List<QueenBoard> population) {
		
		int a[] = new int[population.get(0).board.length];
		int counter = 0;
		
		while(counter < a.length/2) {
		
			if(Math.random() > 0.5) {
				a[(int)(Math.random()*a.length)] = 1;
				counter++;
			}
			if(counter == a.length/2)
				return a;
		
		}
		return a;
	}

}
