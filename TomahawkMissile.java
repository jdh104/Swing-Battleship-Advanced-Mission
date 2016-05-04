
public class TomahawkMissile implements Weapon {

	@Override
	public Cell[] getTargets(Cell target, int orientation) {
		return new Cell[] {
		target.getNorthWestCell(),
		target.getNorthCell(),
		target.getNorthEastCell(),
		target.getWestCell(),
		target,
		target.getEastCell(),
		target.getSouthWestCell(),
		target.getSouthCell(),
		target.getSouthEastCell()
		};
	}

	@Override
	public String getName() {
		return "Tomahawk Missile";
	}
}
