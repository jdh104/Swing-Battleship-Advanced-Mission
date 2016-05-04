import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class GamePanel extends JPanel {
	
	private Grid leftGrid, rightGrid;
	
	private int rows, cols;
	
	public GamePanel(){
		super();
		this.setLayout(new GridLayout(1,2));
	}
	
	public Grid getPlayerGrid(){
		return leftGrid;
	}
	
	public Grid getEnemyGrid(){
		return rightGrid;
	}
	
	public GamePanel setRows(int newRows){
		this.rows = newRows;
		return this;
	}
	
	public GamePanel setCols(int newCols){
		this.cols = newCols;
		return this;
	}
	
	public void initialize(){
		leftGrid = new Grid(rows, cols, true);    // Player's Grid
		rightGrid = new Grid(rows, cols, false);  // Enemy's Grid
		leftGrid.setBorder(new EmptyBorder(5,5,5,5));
		rightGrid.setBorder(new EmptyBorder(5,5,5,5));
		leftGrid.setBackground(Color.BLUE);
		rightGrid.setBackground(Color.RED);
		this.add(leftGrid);
		this.add(rightGrid);
	}
}