package Nqueens;

import java.util.List;

public class Crossover1 extends Crossover{
	
	@Override
	public int[] crossover(List<QueenBoard> population) {
		int a[] = new int[1];
		a[0] = (int)(Math.random()*population.get(0).board.length);
		return a;
	}

}
