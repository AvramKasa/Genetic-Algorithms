package KNAPSACK;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class KNAPSACK {
	
	public static final int ITEMS = 15; 
	public static final int GA_POPSIZE = 1000;
	public static final int GA_MAXITER = 10000;
	public static final double GA_ELITRATE = 0.1f;	
	public static final double GA_MUTATIONRATE = 0.9;		// mutation rate
	public static final double GA_MUTATION = 37120 * GA_MUTATIONRATE;
	public static int best = 0;
	public static int bestGener = 0;
	
	
	public static void calc_fitness(List<KnapSackBoard> population) {
		for(int i = 0; i<GA_POPSIZE; i++) {
			calc_fitness_for_each(population.get(i));
		}
	}
	
	public static void calc_fitness_for_each(KnapSackBoard KnSa) {
		
		int sum = 0;
		int profits = 0;
		for(int i=0 ; i<ITEMS ; i++) {
			if(KnSa.taken[i] == true) {
				sum += KnSa.items[i];
				profits += KnSa.profits[i];
		}
		}
		
		KnSa.fitness = profits;
		KnSa.weight = sum;
		
		while(KnSa.weight > KnSa.knapCap) {
			mutate(KnSa);
			
		}
	}
	
	static List<KnapSackBoard> sort_by_fitness(List<KnapSackBoard> population){
		
		List<KnapSackBoard> temp = new ArrayList<KnapSackBoard>();
		population.sort(Comparator.comparing(a -> a.fitness));
		for(int i=GA_POPSIZE-1;i>=0;i--)
			temp.add(population.get(i));
		
		return temp;
	
	}
	
	static void init_population(List<KnapSackBoard> population, List<KnapSackBoard> buffer){
		
		for (int i = 0; i<GA_POPSIZE; i++) {
			
			KnapSackBoard citizen = new KnapSackBoard();
			
			for(int j = 0; j<ITEMS;j++) {
				if(Math.random()>0.5) {
					citizen.taken[j] = true;
				}
				else
					citizen.taken[j] = false;
			}
	
			population.add(citizen);
		}

	}
	
	static void print_best(List<KnapSackBoard> gav,int i){
		
		int sum = 0;
		for(int j=0; j<ITEMS ; j++)
			if(gav.get(0).taken[j]==true)
				sum+= gav.get(0).profits[j];
		
		if(sum > best) {
			best = sum;
			bestGener = i;
		}
		System.out.println( "    ********* Generation: " + i + " *********");
		System.out.println( "Profits: " + sum );
		System.out.println( "Found on Generation: " + bestGener);
		System.out.println( "Best: " + Arrays.toString(gav.get(0).taken) + " (" + gav.get(0).fitness + ")" );
		System.out.println();
		
	}
	
	static void swap(List<KnapSackBoard> population, List<KnapSackBoard> buffer){
		
		List<KnapSackBoard> temp = new ArrayList<KnapSackBoard>();
		
		temp = population;
		population = buffer; 
		buffer = temp;
	}
	
	public static void print_pop(List<KnapSackBoard> population) {
		
		for (int i = 0; i<GA_POPSIZE; i++) {
			System.out.println(Arrays.toString(population.get(i).taken));
			
		}
	}
	
	static void mutate(KnapSackBoard member){
				
		int delta = (int)(Math.random()*member.taken.length);
		
		for(int i=0 ;i<10 ;i++) {
			if(member.taken[delta] == true) {
				member.taken[delta] = false;
				member.weight -= member.items[delta];
				member.fitness -= member.profits[delta];
			}
			else {
				member.taken[delta] = true;
				member.weight += member.items[delta];
				member.fitness += member.profits[delta];
			}
			delta = (int)(Math.random()*member.taken.length);
		}
		
	}
	
	static void elitism(List<KnapSackBoard> population, List<KnapSackBoard> buffer, int esize){
		
		buffer.clear();
	
		for (int i = 0; i<GA_POPSIZE; i++) {
			buffer.add(population.get(i));
			
		}
		
	}

	static void mate(List<KnapSackBoard> population, List<KnapSackBoard> buffer){
	
		int esize = (int)(GA_POPSIZE * GA_ELITRATE);
		int tsize = population.get(0).taken.length, i1, i2;
		int spos;
		elitism(population, buffer, esize);

		// Mate the rest
		for (int i = esize; i<GA_POPSIZE;i++) {
			
			i1 = (int)(Math.random()*GA_POPSIZE);
			i2 = (int)(Math.random()*GA_POPSIZE);
			spos = (int)(Math.random()*tsize);
			
			boolean[] c = new boolean[tsize];
			
			for(int j=0; j<spos ;j++)
				c[j] = population.get(i1).taken[j];
			
			for(int j=spos; j<tsize ;j++)
				c[j] = population.get(i2).taken[j];
			
			population.get(i).taken = c;
			
			if (Math.random()*37120 < GA_MUTATION) {
				mutate(population.get(i));
			}
			
		}
}
	
	public static void main(String[] args) {
		
		List<KnapSackBoard> population = new ArrayList<KnapSackBoard>();
		List<KnapSackBoard> buffer = new ArrayList<KnapSackBoard>();
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("\n\n\t\t\t\t##############################\n\t\t\t\t|  Welcome to KnapSack game: |\n\t\t\t\t##############################\n ");
		
		try
		{
		    Thread.sleep(1500);
		}
		catch(InterruptedException ex)
		{
		    Thread.currentThread().interrupt();
		}
		init_population(population,buffer);
		
		for (int i = 0; i<GA_MAXITER; i++) {
	
			calc_fitness(population);		// calculate fitness		
			population = sort_by_fitness(population);	// sort them
		
			print_best(population,i);		// print the best one

			mate(population, buffer);		// mate the population together
			swap(population, buffer);		// swap buffers
		}

		System.out.println("\nPress Enter to close the window...");
		scanner.nextLine();
		scanner.close();
		
	}

}
