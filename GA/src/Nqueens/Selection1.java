package Nqueens;

import java.util.List;

public class Selection1 extends Selection {
	
	
	
	@Override
	public int[] select(List<QueenBoard> population , int ga) {
		int[] a = new int[2];
		
		a[0] = (int)(Math.random()*ga);
		a[1] = (int)(Math.random()*ga);
		return a;
	}

}
