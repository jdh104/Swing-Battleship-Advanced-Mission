
public class Missile implements Weapon {
	@Override
	public Cell[] getTargets(Cell target, int orientation) {
		return new Cell[] {target};
	}

	@Override
	public String getName() {
		return "Missile";
	}
}