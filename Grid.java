
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JPanel;

public class Grid extends JPanel{
	
	private boolean friendly;
	private ArrayList<Ship> ships;
	private ArrayList<Row> rows;
	private ArrayList<Col> cols;
	private int rowsNum, colsNum;
	
	public Grid(int rowsNum, int colsNum, boolean friendly){
		super();
		this.rowsNum = rowsNum;
		this.colsNum = colsNum;
		this.friendly = friendly;
		this.rows = new ArrayList<>(colsNum);
		this.cols = new ArrayList<>(rowsNum);
		this.ships = new ArrayList<>();
		
		for (int i=0; i<rowsNum; i++){
			rows.add(i, new Row(colsNum));
		}
		for (int i=0; i<colsNum; i++){
			cols.add(i, new Col(rowsNum));
		}
		
		this.setLayout(new GridLayout(rowsNum, colsNum));
		for (int i=0; i<rowsNum; i++){
			for (int j=0; j<colsNum; j++){
				Cell nextCell = new Cell(rows.get(i),cols.get(j), this);
				rows.get(i).add(j, nextCell);
				cols.get(j).add(i, nextCell);
			    this.add(nextCell);
			}
		}
		
		this.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				Game.getInstance().rotateOrientation(e.getWheelRotation());
				Game.getInstance().updateTargets();
			}
		});
	}	
	
	public boolean hasBattleship(){
		for (Ship ship : ships){
			if (ship instanceof Battleship){
				if (ship.isSunk()){
					return false;
				} else {
					return true;
				}
			}
		} return false;
	}
	
	public boolean hasCarrier(){
		for (Ship ship : ships){
			if (ship instanceof Carrier){
				if (ship.isSunk()){
					return false;
				} else {
					return true;
				}
			}
		} return false;
	}
	
	public boolean hasDestroyer(){
		for (Ship ship : ships){
			if (ship instanceof Destroyer){
				if (ship.isSunk()){
					return false;
				} else {
					return true;
				}
			}
		} return false;
	}
	
	public boolean hasSubmarine(){
		for (Ship ship : ships){
			if (ship instanceof Submarine){
				if (ship.isSunk()){
					return false;
				} else {
					return true;
				}
			}
		} return false;
	}
	
	public Cell[] getAllCells(){
		ArrayList<Cell> result = new ArrayList<>();
		for (Row row : rows){
			for (Cell cell : row.getAll()){
				result.add(cell);
			}
		}
		return result.toArray(new Cell[result.size()]);
	}
	
	public boolean isPlayerGrid() {
		return friendly;
	}
	
	public boolean isFriendly(){
		return (friendly == Game.getInstance().isPlayerTurn());
	}
	
	public void addShip(Ship newShip){
		ships.add(newShip);
	}
	
	public Grid setGridBackground(Color c){
		for (Row row : rows){
			for (int i=0; i<row.size(); i++){
				row.get(i).setBackground(c);
			}
		}
		return this;
	}
	
	public Cell getRandomCell(Random rand){
		return rows.get(rand.nextInt(rows.size())).get(rand.nextInt(cols.size()));
	}
	
	public Row getRow(int index){
		return rows.get(index);
	}
	
	public Col getCol(int index){
		return cols.get(index);
	}
	
	public int indexOf(Row row){
		return rows.indexOf(row);
	}
	
	public int indexOf(Col col){
		return cols.indexOf(col);
	}
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
	}
}