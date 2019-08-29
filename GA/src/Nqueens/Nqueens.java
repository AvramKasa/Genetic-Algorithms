package Nqueens;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Nqueens {
	
	public static final int GA_POPSIZE = 4000;
	public static final int GA_MAXITER = 20000;
	public static final double GA_ELITRATE = 0.3;	
	public static final double GA_MUTATIONRATE = 0.2;		// mutation rate
	public static final double GA_MUTATION = 37120 * GA_MUTATIONRATE;
	public static Selection select;
	public static Crossover cross;
	public static boolean mutateSel = false;
	
	
	public static void calc_fitness(List<QueenBoard> population) {
		for(int i = 0; i<GA_POPSIZE; i++) {
			population.get(i).fitness = calc_fitness_for_each(population.get(i));
		}
	}
	
	public static int calc_fitness_for_each(QueenBoard citizen) {
		int fitness = 0;
		
		for(int i=0 ; i<citizen.board.length ; i++) {
			int j = i-1;
			
			int t = 1;
			while(j > -1) {
				if(citizen.board[j] == citizen.board[i]) {
					fitness ++;
				}
				if(citizen.board[j] == citizen.board[i]+t) {
					fitness++;
				}
				if(citizen.board[j] == citizen.board[i]-t) {
					fitness++;
				}
				j--;
				t++;
			}
			
			j = i+1;
			t = 1;
			while(j < citizen.board.length) {
				if(citizen.board[j] == citizen.board[i]) {
					fitness ++;
				}
				if(citizen.board[j] == citizen.board[i]-t) {
					fitness++;
				}
				if(citizen.board[j] == citizen.board[i]+t) {
					fitness++;
				}
				j++;
				t++;
			}
			
		}
		return fitness;
	}
	
	static void sort_by_fitness(List<QueenBoard> population){
		
		population.sort(Comparator.comparing(a -> a.fitness));
		
	}
	
	static void init_population(List<QueenBoard> population, List<QueenBoard> buffer, int t){
		
		Random random = new Random();
		
		for (int i = 0; i<GA_POPSIZE; i++) {
			
			QueenBoard citizen = new QueenBoard();
			citizen.board = new int[t];
			
			for(int j = 0; j<t;j++) {
				citizen.board[j] = random.nextInt(t);	
			}
			
			population.add(citizen);
		}

	}
	
	static void print_best(List<QueenBoard> gav,int i){
		
		double fitnessSum = 0;
		double fitnessAvg;
		double sd = 0;
		
		System.out.println( "    ********* Generation: " + i + " *********");
		System.out.println( "Best: " + Arrays.toString(gav.get(0).board) + " (" + gav.get(0).fitness + ")" );
		
		for(QueenBoard x: gav)
			fitnessSum+= x.fitness;
		
		fitnessAvg = fitnessSum/gav.size();
		
		for(QueenBoard x: gav)
			sd += Math.pow((x.fitness - fitnessAvg), 2);
		
		sd = sd*(1.0/gav.size());
		
		sd = Math.sqrt(sd);
		
		System.out.println( "Average fitness for this generation: " + fitnessAvg);
		System.out.println( "Standard deviation for this generation: " + sd);
		System.out.println();
	}
	
	static void swap(List<QueenBoard> population, List<QueenBoard> buffer){
		
		List<QueenBoard> temp = new ArrayList<QueenBoard>();
		
		temp = population;
		population = buffer; 
		buffer = temp;
	}
	
	public static void print_pop(List<QueenBoard> population) {
		
		for (int i = 0; i<GA_POPSIZE; i++) {
			System.out.println(Arrays.toString(population.get(i).board));	
		}
		
	}
	
	static void mutate(QueenBoard member){
		
		int tsize = member.board.length;
		int ipos, jpos, temp, start, end;
		Random random = new Random();
		
		ipos = random.nextInt(tsize);
		jpos = random.nextInt(tsize);
		
		if(mutateSel) {						// exchange mutation
			temp = member.board[ipos];
			member.board[ipos] = member.board[jpos];
			member.board[jpos] = temp;
		}
		
		else {						//inversion mutation
			if(jpos >= ipos) {
				start = ipos;
				end = jpos;
			}
			else {
				start = jpos;
				end = ipos;
			}
			int[] a = new int[end-start+1];
			for(int i=0;i<end-start+1;i++) {
				a[i] = member.board[i+start];
			}
			
			for(int i=start,j=a.length;i<start+a.length;i++,j--) {
				member.board[i] = a[j-1];
			}
			
		}
		
	}
	
	static void elitism(List<QueenBoard> population, List<QueenBoard> buffer, int esize){
		
		buffer.clear();
	
		for (int i = 0; i<GA_POPSIZE; i++) {
			buffer.add(population.get(i));
			
		}
		
	}

	static void mate(List<QueenBoard> population, List<QueenBoard> buffer){
		
		Selection select = new Selection1();
		Crossover cross = new Crossover2();
		int esize = (int)(GA_POPSIZE * GA_ELITRATE);
		int tsize = population.get(0).board.length, i1, i2;
		int[] spos;
		elitism(population, buffer, esize);

		// Mate the rest
		for (int i = esize; i<GA_POPSIZE;i++) {
			
			int a[] = new int[2];
			a = select.select(population, GA_POPSIZE);
			i1 = a[0];
			i2 = a[1];
			spos = cross.crossover(population);
			
			if(cross instanceof Crossover1) {
				int temp1;
				int temp2;
				int[] citizen1 = new int[tsize];
				int[] citizen2 = new int[tsize]; 
				
				for(int j=0;j<tsize;j++) {
					citizen1[j] = population.get(i1).board[j];
					citizen2[j] = population.get(i2).board[j];
				}
				
				temp1 = population.get(i1).board[spos[0]];
				temp2 = population.get(i2).board[spos[0]];
				boolean s = true, t = true;
				
				for(int j=0;j<tsize;j++) {
					if( population.get(i1).board[j] == temp2 && s) {
						citizen1[j] = temp1;
						s = false;
					}
					if( population.get(i2).board[j] == temp1 && t) {
						citizen2[j] = temp2;
						t = false;
					}
						
				}
				if(s) {
					citizen1[(int)(Math.random()*tsize)] = temp1;
				}
				if(t) {
					citizen2[(int)(Math.random()*tsize)] = temp2;
				}
				
				citizen1[spos[0]] = temp2;
				citizen2[spos[0]] = temp1;
				
				buffer.get(i).board = citizen1;
				buffer.get(i+1).board = citizen2;
				
				if (Math.random()*37120 < GA_MUTATION) {
					mutate(buffer.get(i));
					mutate(buffer.get(i+1));
				}
				
				i++;
				
				
				
			}
			else {
				
				int[] citizen = new int[tsize];
								
				for(int j=0;j<tsize;j++) {
					
					if(spos[j]==1)
						citizen[j] = population.get(i1).board[j];
					else
						citizen[j] = population.get(i2).board[j];
				}
				
				buffer.get(i).board = citizen;
				
				if (Math.random()*37120 < GA_MUTATION) {
					mutate(buffer.get(i));
				}
				
			
			}
			
		}
	}
	
	public static void solvMinConflicts(QueenBoard citizen, int t) {
		
		ArrayList<Integer> candidates = new ArrayList<Integer>();
		Random random = new Random();
		int moves = 0;
        
        while (true) {

            int maxconflicts = 0;
            candidates.clear();
            for (int i = 0; i < citizen.board.length; i++) {
                int conflicts = calcConflicts(citizen, i);
                if (conflicts == maxconflicts) {
                    candidates.add(i);
                } else if (conflicts > maxconflicts) {
                    maxconflicts = conflicts;
                    candidates.clear();
                    candidates.add(i);
                }
            }
            
           
            if (maxconflicts == 0) {
            	System.out.println(Arrays.toString(citizen.board));
                return;
            }

            int worstQueenColumn =
                    candidates.get(random.nextInt(candidates.size()));

            int minConflicts = citizen.board.length;
            candidates.clear();
            for (int i = 0; i < citizen.board.length; i++) {
                int conflicts = calcConflicts(citizen, worstQueenColumn);
                if (conflicts == minConflicts) {
                    candidates.add(i);
                } else if (conflicts < minConflicts) {
                    minConflicts = conflicts;
                    candidates.clear();
                    candidates.add(i);
                }
            }

            if (!candidates.isEmpty()) {
                citizen.board[worstQueenColumn] =
                    candidates.get(random.nextInt(candidates.size()));
            }
            
            moves++;
            if (moves == citizen.board.length * 10000) {
                semi_init_citizen(citizen);
                moves = 0;
            }
		
        }
	}
	
	public static int calcConflicts(QueenBoard citizen,int pos) {
		
		int conflicts = 0;
		
		for(int i=pos ; i<pos+1 ; i++) {
	
			int j = i-1;
			
			int t = 1;
			while(j > -1) {
				if(citizen.board[j] == citizen.board[i]) {
					conflicts++;
				}
				if(citizen.board[j] == citizen.board[i]+t) {
					conflicts++;
				}
				if(citizen.board[j] == citizen.board[i]-t) {
					conflicts++;
				}
				j--;
				t++;
			}
			
			j = i+1;
			t = 1;
			while(j < citizen.board.length) {
				if(citizen.board[j] == citizen.board[i]) {
					conflicts++;
				}
				if(citizen.board[j] == citizen.board[i]-t) {
					conflicts++;
				}
				if(citizen.board[j] == citizen.board[i]+t) {
					conflicts++;
				}
				j++;
				t++;
			}
			
		}
		
		return conflicts;
	}
	
	
	static void init_citizen(QueenBoard citizen, int i) {
		
		citizen.board = new int[i];
		
		for(int j = 0; j<citizen.board.length;j++) 
			citizen.board[j] = (int)(Math.random()*citizen.board.length);
		
	}
	
	static void semi_init_citizen(QueenBoard citizen) {
		
		
		
		for(int j = 0; j<citizen.board.length;j++) 
			citizen.board[j] = (int)(Math.random()*citizen.board.length);
		
	}
	
	
	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		int a,s,m,c;
		select = new Selection1();
		
		System.out.println("\n\n\t\t\t\t##############################\n\t\t\t\t|  Welcome to N-Queens game: |\n\t\t\t\t##############################\n ");
		
		try
		{
		    Thread.sleep(1500);
		}
		catch(InterruptedException ex)
		{
		    Thread.currentThread().interrupt();
		}
		System.out.println("\nChoose how to play (by number) from the following two:<---\n\n1.Genetic Algorithm\n2.Minimal Conflicts");
		a = scanner.nextInt();
		System.out.println("\nChoose number of queens");
		s = scanner.nextInt();
		if(a == 1) {
			System.out.println("\nChoose Mutation method (by number) from the following Mutations:<---\n\n1.Exchange mutation\n2.Inversion mutation");
			m = scanner.nextInt();
			System.out.println("\nChoose Crossover Method (by number) from the following Crossovers:<---\n\n1.PMX – Partially Matched crossover\n2.OX – Ordered crossover");
			c = scanner.nextInt();
			
			
			if(m == 1)
				mutateSel = true;
			
			switch(c) {
				case 1:
					cross = new Crossover1();
					break;
				default:
					cross = new Crossover2();
					break;	
			}
				
			List<QueenBoard> population = new ArrayList<QueenBoard>();
			List<QueenBoard> buffer = new ArrayList<QueenBoard>();
			init_population(population,buffer,s);
			long time = System.currentTimeMillis();
			
			for (int i = 0; i<GA_MAXITER; i++) {
				
				calc_fitness(population);		// calculate fitness
				sort_by_fitness(population);	// sort them
				print_best(population,i);		// print the best one
				
				if (population.get(0).fitness == 0) {
					break;
				}
				
				mate(population, buffer);		// mate the population together
				swap(population, buffer);		// swap buffers
			}
			
			String totalTime = Long.toString(System.currentTimeMillis() - time);
			System.out.println("Total time to solve: " + totalTime + "ms");
		}
		
		else {
			
				QueenBoard citizen = new QueenBoard();
				init_citizen(citizen,s);
				long time = System.currentTimeMillis();
				solvMinConflicts(citizen,s);
				String totalTime = Long.toString(System.currentTimeMillis() - time);
				System.out.println("Total time to solve: " + totalTime + "ms");
				
		}
		System.out.println("\nPress Enter to close the window...");
		scanner.nextLine();
		scanner.nextLine();
		scanner.close();
		
	}

}