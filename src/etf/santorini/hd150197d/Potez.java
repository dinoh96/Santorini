package etf.santorini.hd150197d;

public class Potez {
	private Koordinata start;
	private Koordinata move;
	private Koordinata build;
	private int procena;
	private Figura figura;
	
	public Potez() {
		super();
	}
	public Potez(Koordinata start, Koordinata move, Koordinata build) {
		super();
		this.start = start;
		this.move = move;
		this.build = build;
	}
	public Potez(Koordinata start, Koordinata move, Koordinata build, int procena) {
		super();
		this.start = start;
		this.move = move;
		this.build = build;
		this.procena = procena;
	}
	public Koordinata getStart() {
		return start;
	}
	public void setStart(Koordinata start) {
		this.start = start;
	}
	public Koordinata getMove() {
		return move;
	}
	public void setMove(Koordinata move) {
		this.move = move;
	}
	public Koordinata getBuild() {
		return build;
	}
	public void setBuild(Koordinata build) {
		this.build = build;
	}
	public int getProcena() {
		return procena;
	}
	public void setProcena(int procena) {
		this.procena = procena;
	}
	public Figura getFigura() {
		return figura;
	}
	public void setFigura(Figura figura) {
		this.figura = figura;
	}		
}

