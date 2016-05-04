
public class Torpedo implements Weapon {

	@Override
	public Cell[] getTargets(Cell target, int orientation) {
		if (orientation == Game.VERTICAL || orientation == Game.DIAGONAL_FORWARD){
			return target.getCol().getAll();
		} else {
			return target.getRow().getAll();
		}
	}

	@Override
	public String getName() {
		return "Torpedo";
	}
}
