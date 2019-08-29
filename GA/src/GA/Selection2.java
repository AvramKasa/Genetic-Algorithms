package GA;
import java.util.List;

public class Selection2 extends Selection{
	
	public static final int GA_POPSIZE = 2048;
	public static final int TourSize = 40;
	
	
	//Tournament selection
	
	@Override
	public int[] select(List<ga_struct> population, int elitisamlow, int[] generationbound) {
		
		int a[] = new int[3];
		
		
		int temp = (int)(Math.random()*(GA_POPSIZE/2));
		
		for(int i =0; i<TourSize; i++) {
			a[0]= (int)(Math.random()*(GA_POPSIZE/2));
			
			if(population.get(a[0]).fitness < population.get(temp).fitness)
				temp = a[0];
		}
		
		a[0] = temp;
		temp = (int)(Math.random()*(GA_POPSIZE/2));
		
		for(int i =0; i<TourSize; i++) {
			a[1]= (int)(Math.random()*(GA_POPSIZE/2));
			
			if(population.get(a[1]).fitness < population.get(temp).fitness)
				temp = a[1];
		}
		
		a[1] = temp;
		
		
		
		return a;
	}

}
