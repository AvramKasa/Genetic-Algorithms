package GA;
import java.util.List;

public class Selection1 extends Selection{
	
	public static final int GA_POPSIZE = 2048;
	
	//choose randomly two
	
	@Override
	public int[] select(List<ga_struct> population, int elitisamlow, int[] generationbound) {
		
		int a[] = new int[2];
		
		
		a[0]= (int)(Math.random()*(GA_POPSIZE));
		a[1]= (int)(Math.random()*(GA_POPSIZE));
		
		
		return a;
		
	}

}
