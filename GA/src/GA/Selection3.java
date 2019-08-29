package GA;
import java.util.List;

//sus algorithm 

public class Selection3 extends Selection{
	
	public static final int GA_POPSIZE = 2048;
	public static final int CONS = 2;
	
	@Override
	public int[] select(List<ga_struct> population, int elitisamlow, int[] generationbound) {
	
        double fitness = 0.0;
        for (ga_struct x : population) {
            fitness += x.fitness;
        }
       
        double p = fitness / CONS;
     
        double start = Math.random() * p;
      
        int[] choosen = new int[CONS];
        
        for (int i = 0; i < CONS; i++) {
        	
            choosen[i] = (int)(start + i * p)%GA_POPSIZE;
               
        } 
        
        //System.out.println(Arrays.toString(choosen));
        return choosen;
		
	}

}
