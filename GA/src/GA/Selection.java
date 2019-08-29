package GA;
import java.util.List;

public abstract class Selection {
	
	public Selection() {}
	
	public abstract int[] select(List<ga_struct> population, int elitisamlow, int[] generationbound);

}
