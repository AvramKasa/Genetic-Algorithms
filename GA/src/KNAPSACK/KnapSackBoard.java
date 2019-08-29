package KNAPSACK;
public class KnapSackBoard {
	
	
	
	public int[] items = { 70,73,77,80,82,87,90,94,98,106,110,113,115,118,120};
	public int[] profits = {135,139,149,150,156,163,173,184,192,201,210,214,221,229,240};
	public boolean[] taken = new boolean[items.length];
	public int knapCap = 750;
	public int fitness = 0;
	public int weight = 0;

}
