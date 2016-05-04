import java.awt.Color;

public final class Carrier extends Ship {
	
	private Color color = new Color(125, 0, 125);
	
	public Carrier(){
		super(5);
		this.setName("Aircraft Carrier");
	}

	@Override
	public Cell[] getProjection(Cell target, int orientation) {
		if (orientation == Game.VERTICAL){
			try {
				return new Cell[] {
						target,
						target.getSouthCell(),
						target.getSouthCell().getSouthCell(),
						target.getSouthCell().getSouthCell().getSouthCell(),
						target.getSouthCell().getSouthCell().getSouthCell().getSouthCell()
				};
			} catch (NullPointerException npe){
				return new Cell[] {null};
			}
		} else if (orientation == Game.DIAGONAL_FORWARD){
			try {
				return new Cell[] {
						target,
						target.getEastCell(),
						target.getEastCell().getEastCell(),
						target.getEastCell().getEastCell().getEastCell(),
						target.getEastCell().getEastCell().getEastCell().getEastCell()
				};
			} catch (NullPointerException npe){
				return new Cell[] {null};
			}
		} else if (orientation == Game.HORIZONTAL){
			try {
				return new Cell[] {
						target,
						target.getNorthCell(),
						target.getNorthCell().getNorthCell(),
						target.getNorthCell().getNorthCell().getNorthCell(),
						target.getNorthCell().getNorthCell().getNorthCell().getNorthCell()
				};
			} catch (NullPointerException npe){
				return new Cell[] {null};
			}
		} else {
			try {
				return new Cell[] {
						target,
						target.getWestCell(),
						target.getWestCell().getWestCell(),
						target.getWestCell().getWestCell().getWestCell(),
						target.getWestCell().getWestCell().getWestCell().getWestCell()
				};
			} catch (NullPointerException npe){
				return new Cell[] {null};
			}
		}
	}

	@Override
	public Color getColor() {
		return color;
	}
}
