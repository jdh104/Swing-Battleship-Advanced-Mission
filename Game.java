import java.awt.Color;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public final class Game {
	
	public static final char UP_ARROW = '\u2191',
			                 LEFT_ARROW = '\u2190',
			                 DOWN_ARROW = '\u2193',
			                 RIGHT_ARROW = '\u2192';
	
	public static final int EASY = 1,
			                NORMAL = 2,
			                HARD = 3,
			                VERTICAL = 0,
			                DIAGONAL_FORWARD = 1,
			                HORIZONTAL = 2,
			                DIAGONAL_BACKWARD = 3,
			                DESTROYER = 1,
			                SUBMARINE = 2,
			                BATTLESHIP = 3,
			                CARRIER = 4,
			                MISSILE = 5,
			                SETUP = 0,
			                PLAYING = 1,
			                PLAYER_TURN = 0,
			                ENEMY_TURN = 1;
	
	private int rows, cols, diff, orientation, status, turn;
	private int playerTomAmmo, playerApaAmmo, playerExoAmmo, playerTorAmmo,
			    enemyTomAmmo, enemyApaAmmo, enemyExoAmmo, enemyTorAmmo,
			    enemyHealth, playerHealth;
	private static Game game;
	
	private GameFrame gf;
	private Enemy enemy;
	private Ship selectedShip;
	private Weapon selectedWeapon;
	private Cell[] currentTargets, lastTargets;
	private Grid playerGrid, enemyGrid;
	
	private Timer timer;
	
	private boolean destroyerSet, battleshipSet, submarineSet, carrierSet;
	
	private Game(){
		super();
		lastTargets = new Cell[] {null};
		currentTargets = new Cell[] {null};
		selectedWeapon = null;
		
		playerHealth = 15;
		enemyHealth = 15;
		
		destroyerSet = false;
		battleshipSet = false;
		submarineSet = false;
		carrierSet = false;
		
		timer = new Timer();
	}
	
	public static Game getInstance(){
		return game;
	}
	
	public static int getRandomOrientation(Random gen){
		return gen.nextInt(4);
	}
	
	public static int getRandomShipType(Random gen){
		return gen.nextInt(5) + 1;
	}
	
	public Game setRows(int newRows){
		rows = newRows;
		return this;
	}
	
	public Game setCols(int newCols){
		cols = newCols;
		return this;
	}
	
	public boolean isPlayerTurn(){
		return (turn == PLAYER_TURN);
	}
	
	public Grid getPlayerGrid(){
		return playerGrid;
	}
	
	public Grid getEnemyGrid(){
		return enemyGrid;
	}
	
	public int getPlayerHealth(){
		return playerHealth;
	}
	
	public int getEnemyHealth(){
		return enemyHealth;
	}
	
	public int getRows(){
		return rows;
	}
	
	public int getCols(){
		return cols;
	}
	
	public int getStatus(){
		return status;
	}
	
	public int getPlayerTomAmmo() {
		return playerTomAmmo;
	}

	public int getPlayerApaAmmo() {
		return playerApaAmmo;
	}

	public int getPlayerTorAmmo() {
		return playerTorAmmo;
	}

	public int getPlayerExoAmmo() {
		return playerExoAmmo;
	}

	public int getEnemyTomAmmo() {
		return enemyTomAmmo;
	}

	public int getEnemyApaAmmo() {
		return enemyApaAmmo;
	}

	public int getEnemyTorAmmo() {
		return enemyTorAmmo;
	}

	public int getEnemyExoAmmo() {
		return enemyExoAmmo;
	}

	public Game setDiff(int newDiff){
		diff = newDiff;
		enemy = new Enemy(diff);
		return this;
	}
	
	public void rotateOrientation(int notches){
		orientation += notches;
		if (orientation < 0) orientation += 4;
		orientation %= 4;
		updateTargets();
	}
	
	public boolean setSelectedShipButton(int shipType){
		if (status == SETUP){
			if (shipType == MISSILE){
				gf.displayMessage("Cannot use that now");
				return false;
			} else if (shipType == BATTLESHIP){
				if (!battleshipSet){
					selectedShip = new Battleship();
					gf.displayMessage("Battleship Selected");
					return true;
				} else {
					gf.displayMessage("Battleship already set");
					return false;
				}
			} else if (shipType == DESTROYER){
				if (!destroyerSet){
					selectedShip = new Destroyer();
					gf.displayMessage("Destroyer Selected");
					return true;
				} else {
					gf.displayMessage("Destroyer already set");
					return false;
				}
			} else if (shipType == SUBMARINE){
				if (!submarineSet){
					selectedShip = new Submarine();
					gf.displayMessage("Submarine Selected");
					return true;
				} else {
					gf.displayMessage("Submarine already set");
					return false;
				}
			} else if (shipType == CARRIER){
				if (!carrierSet){
					selectedShip = new Carrier();
					gf.displayMessage("Carrier Selected");
					return true;
				} else {
					gf.displayMessage("Carrier already set");
					return false;
				}
			} else {
				return false;
			}
		} else {
			if (shipType == MISSILE){
				selectedWeapon = new Missile();
				gf.displayMessage("Missile armed");
				return true;
			} else if (shipType == BATTLESHIP){
				if (turn == PLAYER_TURN && playerTomAmmo > 0 && playerGrid.hasBattleship()){
					selectedWeapon = new TomahawkMissile();
					gf.displayMessage("Tomahawk Missile armed");
					return true;
				} else if (turn == ENEMY_TURN && enemyTomAmmo > 0 && enemyGrid.hasBattleship()){
					selectedWeapon = new TomahawkMissile();
					return true;
				} else {
					gf.displayMessage("Sir, the Tomahawk Missile is not available, sir!");
					return false;
				}
			} else if (shipType == DESTROYER){
				if (turn == PLAYER_TURN && playerApaAmmo > 0 && playerGrid.hasDestroyer()){
					selectedWeapon = new ApacheMissile();
					gf.displayMessage("Apache Missile armed");
					return true;
				} else if (turn == ENEMY_TURN && enemyApaAmmo > 0 && enemyGrid.hasDestroyer()){
					selectedWeapon = new ApacheMissile();
					return true;
				} else {
					gf.displayMessage("Sir, the Apache Missile is not available, sir!");
					return false;
				}
			} else if (shipType == SUBMARINE){
				if (turn == PLAYER_TURN && playerTorAmmo > 0 && playerGrid.hasSubmarine()){
					selectedWeapon = new Torpedo();
					gf.displayMessage("Torpedo armed");
					return true;
				} else if (turn == ENEMY_TURN && enemyTorAmmo > 0 && enemyGrid.hasSubmarine()){
					selectedWeapon = new Torpedo();
					return true;
				} else {
					gf.displayMessage("Sir, the Torpedo is not available, sir!");
					return false;
				}
			} else if (shipType == CARRIER){
				if (turn == PLAYER_TURN && playerExoAmmo > 0 && playerGrid.hasCarrier()){
					selectedWeapon = new ExocetMissile();
					gf.displayMessage("Exocet Missile armed");
					return true;
				} else if (turn == ENEMY_TURN && enemyExoAmmo > 0 && enemyGrid.hasCarrier()){
					selectedWeapon = new ExocetMissile();
					return true;
				} else {
					gf.displayMessage("Sir, the Exocet Missile is not available, sir!");
					return false;
				}
			} else {
				return false;
			}
		}
	}
	
	public Weapon getSelectedWeapon(){
		return selectedWeapon;
	}
	
	public Ship getSelectedShip(){
		return selectedShip;
	}
	
	public int getOrientation() {
		return orientation;
	}
	
	public void setTargets(Cell ... cells){
		currentTargets = cells;
		this.updateTargets();
	}

	public void setWeapon(Weapon nextWeapon) {
		selectedWeapon = nextWeapon;
	}
	
	public void updateTargets(){
		
		for (Cell oldTarget : lastTargets){
			if (oldTarget != null){
				oldTarget.setBackground(new JButton().getBackground());
				oldTarget.setText("");
			}
		}
		
		for (Cell newTarget : currentTargets){
			if (newTarget != null){
				newTarget.setBackground(Color.ORANGE);
			}
		}
		
		lastTargets = currentTargets;
	}
	
	public boolean isClearedToFireOn(Cell target0) {
		return !(target0.getGrid().isFriendly());
	}

	public void beginFiringSequence() {
		if (selectedWeapon == null && turn == ENEMY_TURN){
			selectedWeapon = new Missile();
		}
		if (selectedWeapon != null){
			if (selectedWeapon instanceof Torpedo){
				if (orientation == Game.VERTICAL || orientation == Game.DIAGONAL_BACKWARD){
					for (int i=currentTargets.length-1; i>=0; i--){
						if (currentTargets[i].hasShip() && !currentTargets[i].isHit()){
							if (turn == PLAYER_TURN){
								enemyHealth--;
							} else {
								playerHealth--;
							}
						}
						currentTargets[i].hit();
						currentTargets[i].setBackground(null);
						currentTargets[i].repaint();
						if (currentTargets[i].hasShip()){
							break;
						}
					}
				} else {
					for (int i=0; i<currentTargets.length; i++){
						if (currentTargets[i].hasShip() && !currentTargets[i].isHit()){
							if (turn == PLAYER_TURN){
								enemyHealth--;
							} else {
								playerHealth--;
							}
						}
						currentTargets[i].hit();
						currentTargets[i].setBackground(null);
						currentTargets[i].repaint();
						if (currentTargets[i].hasShip()){
							break;
						}
					}
				}
				if (turn == PLAYER_TURN){
					playerTorAmmo--;
				} else {
					enemyTorAmmo--;
				}
			} else {
				for (Cell target : currentTargets){
					if (target != null){
						if (target.hasShip() && !target.isHit()){
							if (turn == PLAYER_TURN){
								enemyHealth--;
							} else {
								playerHealth--;
							}
						}
						target.hit();
						target.setBackground(null);
						target.repaint();
					}
				}
				if (turn == PLAYER_TURN){
					if (selectedWeapon instanceof ApacheMissile) playerApaAmmo--;
					if (selectedWeapon instanceof TomahawkMissile) playerTomAmmo--;
					if (selectedWeapon instanceof ExocetMissile) playerExoAmmo--;
				} else {
					if (selectedWeapon instanceof ApacheMissile) enemyApaAmmo--;
					if (selectedWeapon instanceof TomahawkMissile) enemyTomAmmo--;
					if (selectedWeapon instanceof ExocetMissile) enemyExoAmmo--;
				}
			}
			
			if (playerHealth == 0){
				JOptionPane.showMessageDialog(null, "Sir, our fleet has been destroyed!!");
				Game.reset();
				return;
			} else if (enemyHealth == 0){
				JOptionPane.showMessageDialog(null, "Sir, the enemy fleet has been destroyed!!");
				Game.reset();
				return;
			}
			
			turn = (turn + 1) % 2;
			if (turn == ENEMY_TURN){
				timer.schedule(new TimerTask() {
					@Override
					public void run(){
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run(){
								enemy.targetAndFire();
							}
						});
					}
				}, 1000);
			}
			
			selectedWeapon = null;
			gf.updateAmmoDisplay();
		}
	}
	
	public boolean placeSelectedShipOn(Cell[] targets) {
		for (Cell target : targets){
			if (target == null){
				gf.displayMessage("Sir, those waters are uncharted, sir!");
				return false;
			} else if (target.hasShip()){
				gf.displayMessage("Sir, you cannot put ships on top of ships, sir!");
				return false;
			} else if (!(target.getGrid().isFriendly())){
				gf.displayMessage("Danger! Foreign waters!");
				return false;
			}
		}
		
		for (Cell target : targets){
			target.setShip(selectedShip);
		}
		
		selectedShip.set(targets, orientation);
		targets[0].getGrid().addShip(selectedShip);
		if (selectedShip instanceof Battleship) battleshipSet = true;
		if (selectedShip instanceof Carrier) carrierSet = true;
		if (selectedShip instanceof Destroyer) destroyerSet = true;
		if (selectedShip instanceof Submarine) submarineSet = true;
		selectedShip = null;
		
		gf.displayMessage("Sir, Ship successfully placed, sir!");
		
		if (battleshipSet && carrierSet && destroyerSet && submarineSet){
			if (turn == PLAYER_TURN) {
				int o = JOptionPane.showConfirmDialog(null, "Sir, Fleet configuration is complete. Begin Game, sir?");
				if (o == JOptionPane.YES_OPTION){
					turn = ENEMY_TURN;
					carrierSet = false;
					destroyerSet = false;
					battleshipSet = false;
					submarineSet = false;
					enemy.configureFleet();
					gf.showMissileButton();
					gf.displayMessage("Awaiting orders, sir...");
				} else {
					Game.reset();
				}
			} else {
				status = PLAYING;
				turn = PLAYER_TURN;
				gf.updateAmmoDisplay();
			}
		}
		
		return true;
	}
	
	public void beginGameSetup(){
		gf.buildGamePanel(rows, cols);
		gf.showGame();
		playerGrid = gf.getPlayerGrid();
		enemyGrid = gf.getEnemyGrid();
		gf.displayMessage("All ships awaiting configuration orders, sir.");
		status = SETUP;
		
		int totalCells = rows*cols;
		
		playerTomAmmo = 1; 
		playerApaAmmo = 2; 
		playerExoAmmo = 2; 
		playerTorAmmo = 2;
		enemyTomAmmo = 1; 
		enemyApaAmmo = 2; 
		enemyExoAmmo = 2; 
		enemyTorAmmo = 2;
		
		if (totalCells > 100){
			playerApaAmmo++;
			enemyApaAmmo++;
		}
		if (totalCells > 110){
			playerApaAmmo++;
			enemyApaAmmo++;
		}
		if (totalCells > 120){
			playerApaAmmo++;
			enemyApaAmmo++;
		}
		if (totalCells > 130){
			playerApaAmmo++;
			enemyApaAmmo++;
		}
		if (totalCells > 150){
			playerExoAmmo++;
			enemyExoAmmo++;
		}
		if (totalCells > 170){
			playerApaAmmo++;
			enemyApaAmmo++;
		}
		if (totalCells > 190){
			playerApaAmmo++;
			enemyApaAmmo++;
		}
		if (totalCells > 200){
			playerExoAmmo++;
			enemyExoAmmo++;
			playerTorAmmo++;
			enemyTorAmmo++;
		}
		if (totalCells > 225){
			playerApaAmmo++;
			enemyApaAmmo++;
		}
		if (totalCells > 250){
			playerExoAmmo++;
			enemyExoAmmo++;
			playerTomAmmo++;
			enemyTomAmmo++;
		}
		if (totalCells > 300){
			playerApaAmmo++;
			enemyApaAmmo++;
		}
		if (totalCells > 325){
			playerApaAmmo++;
			enemyApaAmmo++;
			playerExoAmmo++;
			enemyExoAmmo++;
		}
		if (totalCells > 350){
			playerTorAmmo++;
			enemyTorAmmo++;
		}
		if (totalCells > 375){
			playerApaAmmo++;
			enemyApaAmmo++;
			playerExoAmmo++;
			enemyExoAmmo++;
		}
		if (totalCells == 400){
			playerTorAmmo++;
			enemyTorAmmo++;
			playerApaAmmo++;
			enemyApaAmmo++;
			playerExoAmmo++;
			enemyExoAmmo++;
			playerTomAmmo++;
			enemyTomAmmo++;
		}
	}
	
	public void displayMessage(String message){
		gf.displayMessage(message);
	}
	
	public static void reset(){
		getInstance().gf.dispose();
		game = new Game();
		game.run();
	}
	
	public void run(){
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run(){
				gf = new GameFrame();
				gf.showMenu();
				gf.setLocationRelativeTo(null);
				gf.setDefaultCloseOperation(GameFrame.EXIT_ON_CLOSE);
				gf.setVisible(true);
			}
		});
	}
	
	public static void main(String[] args) {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException ulafe){
			
		} catch (IllegalAccessException iae){
			
		} catch (InstantiationException ie){
			
		} catch (ClassNotFoundException cnfe){
			
		}
		
		game = new Game();
		game.run();
	}
}