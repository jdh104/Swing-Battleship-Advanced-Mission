import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public final class Enemy {
	
	private Random generator;
	
	private Game game;
	private ArrayList<Cell> possibleShipLocations;
	private Cell nextTarget, lastHit, cellPatternBase;
	private Weapon nextWeapon;
	private int nextOrientation;
	
	private Grid grid;
	private int level,
				destroyerX, destroyerY,
				battleshipX, battleshipY,
				submarineX, submarineY,
				carrierX, carrierY;
	
	public Enemy(int difficulty){
		this.level = difficulty;
		generator = new Random();
		game = Game.getInstance();
		nextWeapon = null;
		nextTarget = null;
		possibleShipLocations = new ArrayList<>();
	}
	
	public void configureFleet(){
		grid = game.getEnemyGrid();
		int orientation;
		
		orientation = Game.getRandomOrientation(generator);
		game.setSelectedShipButton(Game.CARRIER);
		
		do {
			if (orientation == Game.VERTICAL){
				carrierY = generator.nextInt(game.getRows() - 5);
				carrierX = generator.nextInt(game.getCols());
			} else if (orientation == Game.DIAGONAL_FORWARD){
				carrierX = generator.nextInt(game.getCols() - 5) + 5;
				carrierY = generator.nextInt(game.getRows());
			} else if (orientation == Game.HORIZONTAL){
				carrierY = generator.nextInt(game.getRows() - 5) + 5;
				carrierX = generator.nextInt(game.getCols());
			} else if (orientation == Game.DIAGONAL_BACKWARD){
				carrierX = generator.nextInt(game.getCols() - 5);
				carrierY = generator.nextInt(game.getRows());
			}
		} while (!game.placeSelectedShipOn(game.getSelectedShip().getProjection(grid.getCol(carrierX).get(carrierY), orientation)));
		
		orientation = Game.getRandomOrientation(generator);
		game.setSelectedShipButton(Game.DESTROYER);
		
		do {
			if (orientation == Game.VERTICAL){
				destroyerY = generator.nextInt(game.getRows() - 3);
				destroyerX = generator.nextInt(game.getCols());
			} else if (orientation == Game.DIAGONAL_FORWARD){
				destroyerX = generator.nextInt(game.getCols() - 3) + 3;
				destroyerY = generator.nextInt(game.getRows());
			} else if (orientation == Game.HORIZONTAL){
				destroyerY = generator.nextInt(game.getRows() - 3) + 3;
				destroyerX = generator.nextInt(game.getCols());
			} else if (orientation == Game.DIAGONAL_BACKWARD){
				destroyerX = generator.nextInt(game.getCols() - 3);
				destroyerY = generator.nextInt(game.getRows());
			}
		} while (!game.placeSelectedShipOn(game.getSelectedShip().getProjection(grid.getCol(destroyerX).get(destroyerY), orientation)));
		
		orientation = Game.getRandomOrientation(generator);
		game.setSelectedShipButton(Game.SUBMARINE);
		
		do {
			if (orientation == Game.VERTICAL){
				submarineY = generator.nextInt(game.getRows() - 3);
				submarineX = generator.nextInt(game.getCols());
			} else if (orientation == Game.DIAGONAL_FORWARD){
				submarineX = generator.nextInt(game.getCols() - 3) + 3;
				submarineY = generator.nextInt(game.getRows());
			} else if (orientation == Game.HORIZONTAL){
				submarineY = generator.nextInt(game.getRows() - 3) + 3;
				submarineX = generator.nextInt(game.getCols());
			} else if (orientation == Game.DIAGONAL_BACKWARD){
				submarineX = generator.nextInt(game.getCols() - 3);
				submarineY = generator.nextInt(game.getRows());
			}
		} while (!game.placeSelectedShipOn(game.getSelectedShip().getProjection(grid.getCol(submarineX).get(submarineY), orientation)));
		
		orientation = Game.getRandomOrientation(generator);
		game.setSelectedShipButton(Game.BATTLESHIP);
		
		do {
			if (orientation == Game.VERTICAL){
				battleshipY = generator.nextInt(game.getRows() - 4);
				battleshipX = generator.nextInt(game.getCols());
			} else if (orientation == Game.DIAGONAL_FORWARD){
				battleshipX = generator.nextInt(game.getCols() - 4) + 4;
				battleshipY = generator.nextInt(game.getRows());
			} else if (orientation == Game.HORIZONTAL){
				battleshipY = generator.nextInt(game.getRows() - 4) + 4;
				battleshipX = generator.nextInt(game.getCols());
			} else if (orientation == Game.DIAGONAL_BACKWARD){
				battleshipX = generator.nextInt(game.getCols() - 4);
				battleshipY = generator.nextInt(game.getRows());
			}
		} while (!game.placeSelectedShipOn(game.getSelectedShip().getProjection(grid.getCol(battleshipX).get(battleshipY), orientation)));
	}
	
	public void targetAndFire(){
		
		boolean rememberLastHit = false;
		int tries = 1;
		
		if (nextTarget == null || nextWeapon == null){
			if (level == Game.EASY){
				while (nextTarget == null || nextTarget.isHit()){
					nextTarget = game.getPlayerGrid().getRandomCell(generator);
				}
				while (!game.setSelectedShipButton(Game.getRandomShipType(generator)));
				nextWeapon = game.getSelectedWeapon();
			} else if (level == Game.NORMAL){
				boolean usingMethodology = false;
				
				while (!game.setSelectedShipButton(Game.getRandomShipType(generator)));
				nextWeapon = game.getSelectedWeapon();
				
				do {
					if (generator.nextInt(100) == 0){
						List<Cell> allCells = Arrays.asList(game.getPlayerGrid().getAllCells());
						Collections.shuffle(allCells);
						for (Cell cell : allCells){
							if (cell.hasShip() && !cell.isHit()){
								nextTarget = cell;
								break;
							}
						}
						break;
					}
					
					usingMethodology = false;
					tries = generator.nextInt(3) + 1;
					for (int i=0; i<tries; i++){
						
						while (nextTarget == null || nextTarget.isHit()){
							nextTarget = game.getPlayerGrid().getRandomCell(generator);
						}
						while (!game.setSelectedShipButton(Game.getRandomShipType(generator)));
						nextWeapon = game.getSelectedWeapon();
						
						List<Cell> allCells = Arrays.asList(game.getPlayerGrid().getAllCells());
						Collections.shuffle(allCells);
						
						for (Cell cell : allCells){
							if (cell.isHit() && cell.hasShip()){
								nextTarget = cell.getRandomAdjacentCell();
								if (!nextTarget.isHit()){
									usingMethodology = true;
									break;
								}
							}
						}
						
						if (lastHit != null){
							usingMethodology = true;
							if (game.setSelectedShipButton(Game.CARRIER)){
								nextWeapon = game.getSelectedWeapon();
								nextTarget = lastHit;
								nextOrientation = Game.DIAGONAL_FORWARD;
							} else {
								rememberLastHit = true;
								game.setSelectedShipButton(Game.MISSILE);
								nextWeapon = game.getSelectedWeapon();
								int r=0;
								do {
									nextTarget = lastHit.getRandomAdjacentCell();
									r++;
									if (r > 10){
										nextTarget = game.getPlayerGrid().getRandomCell(generator);
										usingMethodology = false;
										rememberLastHit = false;
										break;
									}
								} while (nextTarget == null || nextTarget.isHit());
							}
						}
						
						if ((nextTarget.hasShip() || nextTarget.isHit()) && !usingMethodology) break;
					}
				} while (nextTarget == null || (nextTarget.isHit() && !usingMethodology));
			} else if (level == Game.HARD){
				for (int i=0; i<2; i++){
					if (possibleShipLocations.size() > 0){
						nextTarget = possibleShipLocations.get(generator.nextInt(possibleShipLocations.size()));
					} else {
						if (cellPatternBase == null){
							cellPatternBase = game.getPlayerGrid().getRandomCell(new Random());
							nextTarget = cellPatternBase;
						} else {
							do {
								nextTarget = cellPatternBase.getRandomDiagonalCell();
							} while (nextTarget.isHit());
						}
					}
					if (nextTarget.hasShip()) break;
				}
				possibleShipLocations = new ArrayList<>();
				nextOrientation = Game.getRandomOrientation(generator);
				game.setSelectedShipButton(Game.getRandomShipType(generator));
			}
		}
		
		if (game.getSelectedWeapon() == null){
			game.setSelectedShipButton(Game.MISSILE);
		}
		nextWeapon = game.getSelectedWeapon();
		Cell[] targets = nextWeapon.getTargets(nextTarget, nextOrientation);
		boolean hit = false;
		for (Cell target : targets){
			if (target != null && target.hasShip() && !target.isHit()){
				hit = true;
				lastHit = target;
			}
		}
		
		if (!rememberLastHit && !hit){
			lastHit = null;
		}
		
		game.setTargets(targets);
		game.setWeapon(nextWeapon);
		JOptionPane.showMessageDialog(null, "Sir, incoming enemy " + nextWeapon.getName() + ", sir!");
		game.beginFiringSequence();
		
		nextTarget = null;
		nextWeapon = null;
		
		for (Cell cell : game.getPlayerGrid().getAllCells()){
			if (cell.hasShip() && cell.isHit() && !cell.getShip().isSunk()){
				Cell subject;
				boolean methodology = false;
				
				subject = cell.getNorthCell();
				if (subject != null && subject.hasShip() && subject.isHit()){
					possibleShipLocations.add(cell.getSouthCell());
					possibleShipLocations.add(subject.getNorthCell());
					methodology = true;
				}
				subject = cell.getEastCell();
				if (subject != null && subject.hasShip() && subject.isHit()){
					possibleShipLocations.add(cell.getWestCell());
					possibleShipLocations.add(subject.getEastCell());
					methodology = true;
				}
				subject = cell.getWestCell();
				if (subject != null && subject.hasShip() && subject.isHit()){
					possibleShipLocations.add(cell.getEastCell());
					possibleShipLocations.add(subject.getWestCell());
					methodology = true;
				}
				subject = cell.getSouthCell();
				if (subject != null && subject.hasShip() && subject.isHit()){
					possibleShipLocations.add(cell.getNorthCell());
					possibleShipLocations.add(subject.getSouthCell());
					methodology = true;
				}
				
				if (!methodology){
					possibleShipLocations.add(cell.getNorthCell());
					possibleShipLocations.add(cell.getSouthCell());
					possibleShipLocations.add(cell.getWestCell());
					possibleShipLocations.add(cell.getEastCell());
				}
			}
		}
		
		Iterator<Cell> it = possibleShipLocations.iterator();
		while (it.hasNext()){
			Cell possible = it.next();
			if (possible == null || possible.isHit()){
				it.remove();
			}
		}
	}
}