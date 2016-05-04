import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public abstract class Ship {
	
	private Cell[] cells;
	private int length, orientation, health;
	private String name;
	private boolean sunk;
	private JButton button;
	
	public Ship(int length){
		this.length = length;
		this.health = length;
		this.sunk = false;
		name = "Ship";
	}
	
	public boolean isSunk(){
		return sunk;
	}
	
	protected void setName(String newName){
		name = newName;
	}
	
	public int getLength(){
		return length;
	}
	
	public int getOrientation(){
		return orientation;
	}
	
	public void set(Cell[] cells, int orientation){
		this.cells = cells;
		this.orientation = orientation;
		if (cells[0].getGrid().isPlayerGrid()) {
			for (Cell cell : cells){
				cell.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, getColor()));
			}
		}
	}
	
	public Cell[] getOccupiedCells(){
		return cells;
	}
	
	public void updateHealth(){
		int life = 0;
		for (Cell cell : cells){
			if (!(cell.isHit())) life++;
		}
		health = life;
		
		if (health == 0){
			if (!sunk){
				sunk = true;
				if (cells[0].getGrid().isPlayerGrid()){
					JOptionPane.showMessageDialog(null, "Sir, our " + name + " has been sunk, sir!");
				} else {
					JOptionPane.showMessageDialog(null, "Sir, the enemy " + name + " has been sunk, sir!");
				}
				
				for (Cell cell : cells){
					cell.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, getColor()));
				}
			}
		}
	}
	
	public abstract Cell[] getProjection(Cell target, int orientation);
	public abstract Color getColor();
}
