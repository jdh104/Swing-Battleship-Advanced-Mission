import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MenuPanel extends JPanel {
	
	private static final int MIN = 10, MAX = 20;
	
	private int rowsNum, colsNum, diffNum;
	private String diffString;
	
	private JPanel rowsPanel, colsPanel, diffPanel, startPanel;
	private JButton rowsDec, rowsInc, colsDec, colsInc, diffDec, diffInc, start;
	private JLabel rows, cols, diff, rowsLabel, colsLabel, diffLabel;
	
	public MenuPanel(){
		super();
		this.setLayout(new GridLayout(4,1));
		
		rowsNum = 12;
		colsNum = 12;
		diffNum = 2;
		
		rowsPanel = new JPanel();
		colsPanel = new JPanel();
		diffPanel = new JPanel();
		startPanel = new JPanel();
		
		rowsDec = new JButton("<");
		rowsInc = new JButton(">");
		colsDec = new JButton("<");
		colsInc = new JButton(">");
		diffDec = new JButton("<");
		diffInc = new JButton(">");
		start = new JButton("Start");
		
		addActionListeners();
		
		rows = new JLabel(rowsNum + "");
		cols = new JLabel(colsNum + "");
		diff = new JLabel("Normal");
		rowsLabel = new JLabel("Rows:");
		colsLabel = new JLabel("Columns:");
		diffLabel = new JLabel("Difficulty:");
		
		rowsPanel.setLayout(new FlowLayout());
		colsPanel.setLayout(new FlowLayout());
		diffPanel.setLayout(new FlowLayout());
		startPanel.setLayout(new FlowLayout());
		
		rowsPanel.add(rowsLabel);
		rowsPanel.add(rowsDec); 
		rowsPanel.add(rows);
		rowsPanel.add(rowsInc);
		
		colsPanel.add(colsLabel);
		colsPanel.add(colsDec);
		colsPanel.add(cols);
		colsPanel.add(colsInc);
		
		diffPanel.add(diffLabel);
		diffPanel.add(diffDec);
		diffPanel.add(diff);
		diffPanel.add(diffInc);
		
		startPanel.add(start);
		
		this.add(rowsPanel);
		this.add(colsPanel);
		this.add(diffPanel);
		this.add(startPanel);
	}
	
	private void verify(){
		
		while (rowsNum++ < MIN);
		while (--rowsNum > MAX);
		while (colsNum++ < MIN);
		while (--colsNum > MAX);
		while (diffNum++ < Game.EASY);
		while (--diffNum > Game.HARD);
		
		if (diffNum == 1){
			diffString = "Easy";
		} else if (diffNum == 2){
			diffString = "Normal";
		} else if (diffNum == 3){
			diffString = "Hard";
		} else {
			diffNum = 2;
			diffString = "Normal";
		}
		
		rows.setText(rowsNum + "");
		cols.setText(colsNum + "");
		diff.setText(diffString);
	}
	
	private void addActionListeners(){
		rowsDec.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				rowsNum--;
				verify();
			}
		});
		
		rowsInc.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				rowsNum++;
				verify();
			}
		});
		
		colsDec.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				colsNum--;
				verify();
			}
		});
		
		colsInc.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				colsNum++;
				verify();
			}
		});
		
		diffDec.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				diffNum--;
				verify();
			}
		});
		
		diffInc.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				diffNum++;
				verify();
			}
		});
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				Game g = Game.getInstance();
				g.setRows(rowsNum).setCols(colsNum).setDiff(diffNum);
				g.beginGameSetup();
			}
		});
	}
}