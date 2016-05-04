
public final class ExocetMissile implements Weapon {

	@Override
	public Cell[] getTargets(Cell target, int orientation) {
		if (orientation == Game.VERTICAL || orientation == Game.HORIZONTAL){
			return new Cell[] {
					target.getNorthWestCell(),
					target.getNorthEastCell(),
					target,
					target.getSouthWestCell(),
					target.getSouthEastCell()
			};
		} else {
			return new Cell[] {
					target.getNorthCell(),
					target.getWestCell(),
					target,
					target.getEastCell(),
					target.getSouthCell()
			};
		}
	}

	@Override
	public String getName() {
		return "Exocet Missile";
	}
}