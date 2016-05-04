import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

public final class GameFrame extends JFrame {
	
	private JPanel contentPane, messagePanel, messagePanelContainer;
	private JLabel messageLabel, playerStatusLabel, enemyStatusLabel;
	private MenuPanel menuPanel;
	private GamePanel gamePanel;
	private ControlPanel controlPanel;
	
	public GameFrame(){
		super("Battle-Ship");
		menuPanel = new MenuPanel();
		gamePanel = new GamePanel();
		controlPanel = new ControlPanel();
		contentPane = ((JPanel) this.getContentPane());
		messagePanel = new JPanel();
		messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.X_AXIS));
		messageLabel = new JLabel("Welcome to Advanced Battleship");
		playerStatusLabel = new JLabel();
		enemyStatusLabel = new JLabel();
		messagePanel.add(playerStatusLabel);
		messagePanel.add(Box.createHorizontalGlue());
		messagePanel.add(messageLabel);
		messagePanel.add(Box.createHorizontalGlue());
		messagePanel.add(enemyStatusLabel);
	}
	
	public GameFrame showMenu(){
		contentPane.removeAll();
		contentPane.add(menuPanel);
		this.setSize(300, 200);
		return refresh();
	}
	
	public GameFrame showGame(){
		contentPane.removeAll();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		contentPane.add(gamePanel);
		contentPane.add(controlPanel);
		contentPane.add(messagePanel);
		this.setSize(1000, 500);
		this.setLocationRelativeTo(null);
		return refresh();
	}
	
	public GameFrame refresh(){
		this.getContentPane().validate();
		this.getContentPane().repaint();
		this.validate();
		this.repaint();
		return this;
	}
	
	public GameFrame buildGamePanel(int rows, int cols){
		gamePanel.setRows(rows).setCols(cols);
		gamePanel.initialize();
		return this;
	}
	
	public void showMissileButton(){
		controlPanel.showMissileButton();
	}
	
	public void displayMessage(String message){
		messageLabel.setText(message);
		playerStatusLabel.setText("Your Fleet Health: " + Game.getInstance().getPlayerHealth());
		enemyStatusLabel.setText("Enemy Fleet Health: " + Game.getInstance().getEnemyHealth());
	}
	
	public Grid getPlayerGrid(){
		return gamePanel.getPlayerGrid();
	}
	
	public Grid getEnemyGrid(){
		return gamePanel.getEnemyGrid();
	}

	public void updateAmmoDisplay() {
		controlPanel.updateAmmoDisplay();
		this.validate();
		this.repaint();
	}
}