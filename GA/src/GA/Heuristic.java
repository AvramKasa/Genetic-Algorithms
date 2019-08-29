package GA;
import java.util.List;


public abstract class Heuristic {
	
	public Heuristic() {}
	
	public abstract void calc_fitness(List<ga_struct> population, int bonus);
		
}

