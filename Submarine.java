import java.awt.Color;

public class Submarine extends Ship {
	
	public Submarine(){
		super(3);
		this.setName("Submarine");
	}
	
	@Override
	public Cell[] getProjection(Cell target, int orientation) {
		if (orientation == Game.VERTICAL){
			try {
				return new Cell[] {
						target,
						target.getSouthCell(),
						target.getSouthCell().getSouthCell()
				};
			} catch (NullPointerException npe){
				return new Cell[] {null};
			}
		} else if (orientation == Game.DIAGONAL_FORWARD){
			try {
				return new Cell[] {
						target,
						target.getEastCell(),
						target.getEastCell().getEastCell()
				};
			} catch (NullPointerException npe){
				return new Cell[] {null};
			}
		} else if (orientation == Game.HORIZONTAL){
			try {
				return new Cell[] {
						target,
						target.getNorthCell(),
						target.getNorthCell().getNorthCell()
				};
			} catch (NullPointerException npe){
				return new Cell[] {null};
			}
		} else {
			try {
				return new Cell[] {
						target,
						target.getWestCell(),
						target.getWestCell().getWestCell()
				};
			} catch (NullPointerException npe){
				return new Cell[] {null};
			}
		}
	}

	@Override
	public Color getColor() {
		return Color.GREEN;
	}
}
