import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ControlPanel extends JPanel {
	
	private JButton destroyerButton, submarineButton, battleshipButton, 
	                carrierButton, missileButton, rotateButton, resetButton;
	
	public ControlPanel(){
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		destroyerButton = new JButton("Destroyer");
		submarineButton = new JButton("Submarine");
		battleshipButton = new JButton("Battleship");
		carrierButton = new JButton("Aircraft Carrier");
		missileButton = new JButton("Missile");
		rotateButton = new JButton("Rotate");
		resetButton = new JButton("Reset");
		
		this.addActionListeners();
		
		this.add(destroyerButton);
		this.add(submarineButton);
		this.add(battleshipButton);
		this.add(carrierButton);
		this.add(missileButton);
		this.add(rotateButton);
		this.add(resetButton);
		
		missileButton.setVisible(false);
	}
	
	public void showMissileButton(){
		missileButton.setVisible(true);
	}
	
	public void updateAmmoDisplay(){
		Game g = Game.getInstance();
		destroyerButton.setText("Destroyer (x" + g.getPlayerApaAmmo() + ") ");
		submarineButton.setText("Submarine (x" + g.getPlayerTorAmmo() + ") ");
		battleshipButton.setText("Battleship (x" + g.getPlayerTomAmmo() + ") ");
		carrierButton.setText("Aircraft Carrier (x" + g.getPlayerExoAmmo() + ") ");
		destroyerButton.repaint();
		submarineButton.repaint();
		battleshipButton.repaint();
		carrierButton.repaint();
	}
	
	private void addActionListeners(){
		destroyerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae){
				Game.getInstance().setSelectedShipButton(Game.DESTROYER);
			}
		});
		battleshipButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae){
				Game.getInstance().setSelectedShipButton(Game.BATTLESHIP);
			}
		});
		submarineButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae){
				Game.getInstance().setSelectedShipButton(Game.SUBMARINE);
			}
		});
		carrierButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae){
				Game.getInstance().setSelectedShipButton(Game.CARRIER);
			}
		});
		missileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae){
				Game.getInstance().setSelectedShipButton(Game.MISSILE);
			}
		});
		rotateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae){
				Game.getInstance().rotateOrientation(1);
			}
		});
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae){
				Game.reset();
			}
		});
	}
}