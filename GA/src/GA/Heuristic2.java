package GA;
import java.util.List;

//Actual Bulls and Cows Heuristic

public class Heuristic2  extends Heuristic{
	public static final int GA_POPSIZE = 2048;	// ga population size
	public static final String  GA_TARGET = "Hello World rise and shine";
	
	@Override
	public void calc_fitness(List<ga_struct> population, int bonus) {
			
		String target = GA_TARGET;
		int addmax = GA_TARGET.length()*10;
		int tsize = target.length();
		int fitness;
		char[] targetToCharArray = target.toCharArray();

		for (int i = 0; i<GA_POPSIZE; i++) {
			fitness = addmax;
			char[] stringToCharArray = population.get(i).str.toCharArray();
			for (int j = 0; j<tsize; j++) {
				fitness += Math.abs((int)(stringToCharArray[j] - targetToCharArray[j]));
			}

			population.get(i).fitness = fitness;
		}
		
		for (int i = 0; i<GA_POPSIZE; i++) {
			char[] stringToCharArray = population.get(i).str.toCharArray();
			for (int j = 0; j<tsize; j++) {
				if(stringToCharArray[j] == targetToCharArray[j])
					population.get(i).fitness = population.get(i).fitness-10;
				else {
					for(int k = 0; k<tsize; k++) {
						if(stringToCharArray[j] == targetToCharArray[k]&&bonus>0) {
							population.get(i).fitness--;
							bonus--;
						}
					}
				}
			}

			 
		}
		
		
	}
}
