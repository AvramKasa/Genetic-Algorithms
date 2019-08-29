package GA;
import java.util.*;

public class Ga {
	
	public static final int GA_POPSIZE = 2048;	// ga population size
	public static final int GA_MAXITER = 16384;		// maximum iterations
	public static final double GA_ELITRATE = 0.1f;		// elitism rate
	public static final double GA_MUTATIONRATE = 0.2f;		// mutation rate
	public static final double GA_MUTATION = 37120 * GA_MUTATIONRATE;
	public static final String  GA_TARGET = "Hello World rise and shine";
	public static int bonus = GA_TARGET.length();
	public static int globBest;
	public static int localBest;
	public static long locGen;
	public static long gloGen;
	public static long start = System.currentTimeMillis();
	public static long localtime = System.currentTimeMillis();
	public static int prevbest;
	public static Selection select;
	public static Crossover cross;
	public static Heuristic heuristic;
	
	
	static void init_population(List<ga_struct> population, List<ga_struct>buffer){
			
		int tsize = GA_TARGET.length();
		
		for (int i = 0; i<GA_POPSIZE; i++) {
			
			ga_struct citizen = new ga_struct();
			citizen.gener = 0;
			
			for (int j = 0; j<tsize; j++)
				citizen.str += (char)((Math.random()*90) + 32);
			population.add(citizen);
			
		}
		
		globBest = 0;
		localBest = 0;
		locGen = 0;
		gloGen = 0;
		prevbest = 0;
	}

	static void calc_fitness(List<ga_struct> population){
		
		heuristic.calc_fitness(population,bonus);
	}


	static void sort_by_fitness(List<ga_struct> population){
			
		population.sort(Comparator.comparing(a -> a.fitness));

	}

	static void elitism(List<ga_struct> population, List<ga_struct> buffer, int esize){
			
		buffer.clear();
	
		for (int i = 0; i<GA_POPSIZE; i++) {
			buffer.add(population.get(i));
			
		}	
	}

	static void mutate(ga_struct member){
		
		int tsize = GA_TARGET.length();
		int ipos = (int)(Math.random() *tsize);
		int delta = (int)(Math.random()*90) + 32;
		char[] stringToCharArray = member.str.toCharArray();

		stringToCharArray[ipos] = (char)((stringToCharArray[ipos] + delta) % 122);
		member.str = String.copyValueOf(stringToCharArray);
	}

	static void mate(List<ga_struct> population, List<ga_struct> buffer){
		
		int esize = (int)(GA_POPSIZE * GA_ELITRATE);
		int tsize = GA_TARGET.length(), i1, i2;
		int[] spos; 
		elitism(population, buffer, esize);
		int elitisamlow = buffer.get(esize).fitness;
		int[] generationbound = new int[2];
		generationbound[0] = 5;
		generationbound[1] = 50;
			
		// Mate the rest
		for (int i = esize; i<GA_POPSIZE-1;i++) {
			int a[] = new int[2];
			a = select.select(population,elitisamlow,generationbound);
			i1 = a[0];
			i2 = a[1];
			spos = cross.crossover(population);
			char[] c = new char[tsize];
			char[] d = new char[tsize];
			
			
			char[] stringToCharArray = population.get(i1).str.toCharArray();
			char[] stringToCharArray1 = population.get(i2).str.toCharArray();
			
			
			for(int j=0; j<spos.length-1;j++) {
				
				if(j%2 == 0) {
					System.arraycopy(stringToCharArray ,spos[j], c, spos[j],spos[j+1]-spos[j]);
					System.arraycopy(stringToCharArray1 ,spos[j], d, spos[j],spos[j+1]-spos[j]);
				}
				else {
					System.arraycopy(stringToCharArray ,spos[j], d, spos[j],spos[j+1]-spos[j]);
					System.arraycopy(stringToCharArray1 ,spos[j], c, spos[j],spos[j+1]-spos[j]);	
				}
				
			}
			if(cross instanceof Crossover1 || cross instanceof Crossover2) {
				buffer.get(i).str = String.copyValueOf(c);
				buffer.get(i+1).str = String.copyValueOf(d);
				buffer.get(i).gener = 0;
				buffer.get(i+1).gener = 0;
				if (Math.random()*32767 < GA_MUTATION) {
					mutate(buffer.get(i));
					mutate(buffer.get(i+1));
				}
				i +=2;
			}
			else {
				buffer.get(i).str = String.copyValueOf(c);
				buffer.get(i).gener = 0;
				if (Math.random()*32767 < GA_MUTATION) {
					mutate(buffer.get(i));
				}
				
			}
			
		}
		
		updateGen(buffer);
	}
	
	static void updateGen(List<ga_struct> population) {
		for(ga_struct x: population)
			x.gener++;
	}
	
	static void print_best(List<ga_struct> gav,int i){
		
		double fitnessSum = 0;
		double fitnessAvg;
		double sd = 0;
		
		System.out.println( "    ********* Generation: " + i + " *********");
		System.out.println( "Best: " + gav.get(0).str + " (" + gav.get(0).fitness + ")" );
		
		for(ga_struct x: gav)
			fitnessSum+= x.fitness;
		
		fitnessAvg = fitnessSum/gav.size();
		
		for(ga_struct x: gav)
			sd += Math.pow((x.fitness - fitnessAvg), 2);
		
		sd = sd*(1.0/gav.size());
		sd = Math.sqrt(sd);
		
		if(gav.get(0).fitness < globBest) {
			globBest = gav.get(0).fitness;
			long a = System.currentTimeMillis();
			gloGen = a - start;
		}
		
		if(localBest > gav.get(0).fitness){
			localBest = gav.get(0).fitness;
			long b = System.currentTimeMillis();
			locGen = b - localtime;
		}
		
		else {
			if(prevbest > gav.get(0).fitness) {
				localBest = gav.get(0).fitness;
				localtime = System.currentTimeMillis();
				long b = System.currentTimeMillis();
				locGen = b - localtime;
			}
		}
		
		prevbest = gav.get(0).fitness;
			
		System.out.println( "Divergent time for Local minimum: " + locGen + "ms");
		System.out.println( "Divergent time for current maximum: " + gloGen + "ms");
		System.out.println( "Average fitness for this generation: " + fitnessAvg);
		System.out.println( "Standard deviation for this generation: " + sd);
		System.out.println();
	}

	static void swap(List<ga_struct> population, List<ga_struct> buffer){
		
		List<ga_struct> temp = new ArrayList<ga_struct>();
		
		temp = population;
		population = buffer; 
		buffer = temp;
	}

	public static void main(String[] args){
			
		List<ga_struct> pop_alpha = new ArrayList<ga_struct>();
		List<ga_struct> population = new ArrayList<ga_struct>();
		List<ga_struct>	pop_beta = new ArrayList<ga_struct>();
		List<ga_struct> buffer = new ArrayList<ga_struct>();
		
		init_population(pop_alpha, pop_beta);
		population = pop_alpha;
		long begining =0;
		long finish;
		Scanner scanner = new Scanner(System.in);
		int s,c,h;
		
		System.out.println("\n\n\t\t\t\t#####################################################\n\t\t\t\t| Welcome to Bulls and Cows Genetic Algorithm game: |\n\t\t\t\t#####################################################\n ");
		
		try
		{
		    Thread.sleep(1500);
		}
		catch(InterruptedException ex)
		{
		    Thread.currentThread().interrupt();
		}
		
		System.out.println("\nChoose Heuristic (by number) from the following Heuristics:<---\n\n1.Default Heuristic provided in class example\n2.High prize for right position lower for others");
		h = scanner.nextInt();
		System.out.println("\nChoose Selection method (by number) from the following Selections:<---\n\n1.Choose randomly two\n2.Tournament selection\n3.SUS algorithm\n4.Aging Algorithm");
		s = scanner.nextInt();
		System.out.println("\nChoose Crossover Method (by number) from the following Crossovers:<---\n\n1.Uniform crossover\n2.Two point crossover\n3.Basic two parents one child single crossover");
		c = scanner.nextInt();
		
		switch(h) {
			case 1:
				heuristic = new Heuristic1();
				break;
			default:
				heuristic = new Heuristic2();
				break;	
		}
		
		switch(s) {
			case 1:
				select = new Selection1();
				break;
			case 2:
				select = new Selection2();
				break;
			case 3:
				select = new Selection3();
				break;
			default:
				select = new Selection4();
				break;	
		}
		
		switch(c) {
			case 1:
				cross = new Crossover1();
				break;
			case 2:
				cross = new Crossover2();
				break;
			default:
				cross = new Crossover3();
				break;	
		}
		
		
			pop_alpha = new ArrayList<ga_struct>();
			population = new ArrayList<ga_struct>();
			pop_beta = new ArrayList<ga_struct>();
			buffer = new ArrayList<ga_struct>();
			
			init_population(pop_alpha, pop_beta);
			population = pop_alpha;
		for (int i = 0; i<GA_MAXITER; i++) {
			
			calc_fitness(population);		// calculate fitness
			if(i==0) {
				globBest = population.get(0).fitness;
				localBest = population.get(0).fitness;
				prevbest = population.get(0).fitness;
			}
			sort_by_fitness(population);	// sort them
			print_best(population,i);		// print the best one
			if(i>0) {
				finish = System.currentTimeMillis();
				System.out.println("Run time for generation: " + (finish - begining)+"ms\n");

			}
			
			begining = System.currentTimeMillis();
			
			if (population.get(0).fitness == 0 || i+1 == GA_MAXITER) {
				break;
			}
			
			mate(population, buffer);		// mate the population together
			swap(population, buffer);		// swap buffers
		}
		long end = System.currentTimeMillis();
		System.out.println("\nElapsed run time: " + (end - start)+"ms");
		System.out.println("\nPress Enter to close the window...");
		scanner.nextLine();
		scanner.nextLine();
		scanner.close();
	
	}

}
