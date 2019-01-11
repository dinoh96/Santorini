package etf.santorini.hd150197d;

public class Figura {
	private static int prevId = 0;
	private int ID = prevId++;
	
	private int row;
	private int col;
	
	private Igrac p;

	public Figura() {
		super();
	}	

	public Figura(int row, int col) {
		super();
		this.row = row;
		this.col = col;
	}

	public Figura(Igrac p) {
		super();
		this.p = p;
	}

	public Figura(int row, int col, Igrac p) {
		super();
		this.row = row;
		this.col = col;
		this.p = p;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public Igrac getP() {
		return p;
	}

	public void setP(Igrac p) {
		this.p = p;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}
	
	public void setPos(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	
	

}
