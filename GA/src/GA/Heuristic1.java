package GA;
import java.util.List;

//Default Heuristic provided in class example

public class Heuristic1 extends Heuristic{
	
	public static final int GA_POPSIZE = 2048;	// ga population size
	public static final String  GA_TARGET = "Hello World rise and shine";
	
	@Override
	public void calc_fitness(List<ga_struct> population, int bonus) {
		String target = GA_TARGET;
		int tsize = target.length();
		int fitness;
		char[] targetToCharArray = target.toCharArray();

		for (int i = 0; i<GA_POPSIZE; i++) {
			fitness = 0;
			char[] stringToCharArray = population.get(i).str.toCharArray();
			for (int j = 0; j<tsize; j++) {
				fitness += Math.abs((int)(stringToCharArray[j] - targetToCharArray[j]));
			}

			population.get(i).fitness = fitness;
		}
	}

}
