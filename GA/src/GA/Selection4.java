package GA;

import java.util.List;

//Aging

public class Selection4 extends Selection{
	
	public static final int GA_POPSIZE = 2048;
	
	@Override
	public int[] select(List<ga_struct> population, int elitisamlow, int[] generationbound) {
		
		int b[] = new int[2];
		
		//sort by generation
		
		
		
		while(true) {
			b[0] = (int)(Math.random()*GA_POPSIZE);
			if((population.get(b[0]).gener > generationbound[0] && population.get(b[0]).gener < generationbound[1]) || population.get(b[0]).fitness >= elitisamlow)
				break;	
		}
		while(true) {
			b[1] = (int)(Math.random()*GA_POPSIZE);
			if((population.get(b[1]).gener > generationbound[0] && population.get(b[1]).gener < generationbound[1]) || population.get(b[1]).fitness >= elitisamlow)
				break;	
		}
		
		
		
		return b;
		
	}

}