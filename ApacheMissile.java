
public final class ApacheMissile implements Weapon {

	@Override
	public Cell[] getTargets(Cell target, int orientation) {
		if (orientation == Game.VERTICAL){
			return new Cell[] {
					target.getNorthCell(),
					target,
					target.getSouthCell()
			};
			
		} else if (orientation == Game.DIAGONAL_FORWARD) {
			return new Cell[] {
					target.getNorthEastCell(),
					target,
					target.getSouthWestCell()
			};
			
		} else if (orientation == Game.HORIZONTAL) {
			return new Cell[] {
					target.getWestCell(),
					target,
					target.getEastCell()
			};
			
		} else {
			return new Cell[] {
					target.getNorthWestCell(),
					target,
					target.getSouthEastCell()
			};
		}
	}

	@Override
	public String getName() {
		return "Apache Missile";
	}
}
