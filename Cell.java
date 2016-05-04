
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Random;

import javax.swing.JButton;

public class Cell extends JButton {
	
	private Ship ship;
	private Grid grid;
	private Row row;
	private Col col;
	private boolean hasShip, hit;
	
	public Cell(Row row, Col col, Grid grid){
		super();
		this.setContentAreaFilled(false);
		this.grid = grid;
		this.row = row;
		this.col = col;
		hasShip = false;
		
		this.addMouseListener(new MouseListener() {
			@Override
			public void mouseEntered(MouseEvent e){
				if (Game.getInstance().getStatus() == Game.SETUP){
					showShip();
				} else {
					target();
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				Game g = Game.getInstance();
				try {
					if (g.getStatus() == Game.SETUP){
						g.placeSelectedShipOn(g.getSelectedShip().getProjection(Cell.this, g.getOrientation()));
					} else {
						if (g.isClearedToFireOn(Cell.this)){
							g.beginFiringSequence();
						}
					}
				} catch (NullPointerException npe){
					g.displayMessage("Please select a Ship");
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
		});
		
		this.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				Game.getInstance().rotateOrientation(e.getWheelRotation());
				
				if (Game.getInstance().getStatus() == Game.SETUP){
					showShip();
				} else {
					target();
				}
			}
		});
	}
	
	public void setShip(Ship ship){
		this.ship = ship;
		hasShip = true;
	}
	
	public boolean isHit(){
		return hit;
	}
	
	public boolean hasShip(){
		return hasShip;
	}
	
	public Grid getGrid(){
		return grid;
	}
	
	public Row getRow(){
		return row;
	}
	
	public Col getCol(){
		return col;
	}
	
	public Cell getRandomDiagonalCell(){
		Random rand = new Random();
		
		int myRowIndex = this.getGrid().indexOf(this.getRow());
		int myColIndex = this.getGrid().indexOf(this.getCol());
		int newRowIndex = (myRowIndex + rand.nextInt(100)) % this.getCol().size();
		int newColIndex;
		int rowDiff = Math.abs(newRowIndex - myRowIndex);
		
		if (rowDiff == 0){
			newColIndex = (myColIndex + rand.nextInt(100) * 3 + 3) % this.getRow().size();
		} else if (rowDiff % 3 == 0){
			newColIndex = (myColIndex + rand.nextInt(100) * 3) % this.getRow().size();
		} else if (rowDiff % 3 == 1){
			newColIndex = (myColIndex + rand.nextInt(100) * 3 + 1) % this.getRow().size();
		} else {
			newColIndex = (myColIndex + rand.nextInt(100) * 3 + 2) % this.getRow().size();
		}
		
		return this.getGrid().getRow(newRowIndex).get(newColIndex);
	}
	
	public Cell getRandomAdjacentCell(){
		Random rand = new Random();
		int i = rand.nextInt(4);
		
		if (i == 0){
			return getNorthCell();
		} else if (i == 1){
			return getEastCell();
		} else if (i == 2){
			return getSouthCell();
		} else {
			return getWestCell();
		}
	}
	
	public Ship getShip(){
		return ship;
	}
	
	public Cell getNorthCell() {
		return col.getCellNorthOf(this);
	}
	
	public Cell getSouthCell() {
		return col.getCellSouthOf(this);
	}
	
	public Cell getWestCell() {
		return row.getCellWestOf(this);
	}
	
	public Cell getEastCell() {
		return row.getCellEastOf(this);
	}
	
	public Cell getNorthEastCell(){
		try {
			return col.getCellNorthOf(this).getEastCell();
		} catch (NullPointerException npe){
			return null;
		}
	}
	
	public Cell getSouthEastCell(){
		try {
			return col.getCellSouthOf(this).getEastCell();
		} catch (NullPointerException npe){
			return null;
		}
	}
	
	public Cell getSouthWestCell(){
		try {
			return col.getCellSouthOf(this).getWestCell();
		} catch (NullPointerException npe){
			return null;
		}
	}
	
	public Cell getNorthWestCell(){
		try {
			return col.getCellNorthOf(this).getWestCell();
		} catch (NullPointerException npe){
			return null;
		}
	}
	
	public void hit(){
		this.hit = true;
		if (hasShip){
			ship.updateHealth();
		}
	}
	
	private void target(){
		Weapon weap = Game.getInstance().getSelectedWeapon();
		if (weap != null){
			int orientation = Game.getInstance().getOrientation();
			if (weap instanceof Torpedo){
				Game.getInstance().setTargets(weap.getTargets(this, orientation));
				if (orientation == Game.VERTICAL){
					this.setText(Game.UP_ARROW + "");
				} else if (orientation == Game.DIAGONAL_FORWARD){
					this.setText(Game.DOWN_ARROW + "");
				} else if (orientation == Game.HORIZONTAL){
					this.setText(Game.RIGHT_ARROW + "");
				} else if (orientation == Game.DIAGONAL_BACKWARD){
					this.setText(Game.LEFT_ARROW + "");
				}
			} else {
				Game.getInstance().setTargets(weap.getTargets(this, orientation));
			}
		}
	}
	
	private void showShip(){
		Ship ship = Game.getInstance().getSelectedShip();
		if (ship != null){
			int orientation = Game.getInstance().getOrientation();
			Game.getInstance().setTargets(ship.getProjection(this, orientation));
		}
	}
	
	@Override
	protected void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(this.getBackground());
		g2.fillRect(1, 1, this.getWidth()-2, this.getHeight()-2);
		
		if (hit){
			g2.setColor(Color.BLACK);
			g2.setStroke(new BasicStroke(Math.min(this.getHeight()/5, this.getWidth()/5)));
			
			if (hasShip){
				g2.drawOval(10, 10, this.getWidth()-20, this.getHeight()-20);
			} else {
				g2.drawLine(4, 4, this.getWidth()-4, this.getHeight()-4);
				g2.drawLine(this.getWidth()-4, 4, 4, this.getHeight()-4);
			}
		}
		
		super.paintComponent(g);
	}
}