
public interface Weapon {
	
	public abstract String getName();
	public abstract Cell[] getTargets(Cell target, int orientation);
	
}
