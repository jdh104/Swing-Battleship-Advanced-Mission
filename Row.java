
import java.util.ArrayList;

public class Row {
	
	private ArrayList<Cell> cellList;
	private int length;
	
	public Row(int length){
		this.length = length;
		cellList = new ArrayList<>(length);
	}
	
	public void add(int index, Cell newCell){
		cellList.add(index, newCell);
	}
	
	public Cell get(int index){
		return cellList.get(index);
	}
	
	public Cell[] getAll(){
		return cellList.toArray(new Cell[length]);
	}
	
	public int size(){
		return length;
	}
	
	public Cell getCellWestOf(Cell cell) {
		try {
			return cellList.get(cellList.indexOf(cell) - 1);
		} catch (IndexOutOfBoundsException ioobe){
			return null;
		}
	}
	
	public Cell getCellEastOf(Cell cell) {
		try {
			return cellList.get(cellList.indexOf(cell) + 1);
		} catch (IndexOutOfBoundsException ioobe){
			return null;
		}
	}
}
