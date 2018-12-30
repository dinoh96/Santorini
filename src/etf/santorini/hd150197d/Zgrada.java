package etf.santorini.hd150197d;

public class Zgrada {
	private static int prevId = 0;
	private int ID = prevId++;
	
	private int nivo;
	private boolean kupola;
	
	private Polje p;
	
	public Zgrada(Polje p) {
		super();
		this.p = p;
	}

	public int getNivo() {
		return nivo;
	}

	public void incNivo() {
		this.nivo++;
		if (this.nivo == 4) this.kupola = true;
		p.repaint();
	}

	public boolean isKupola() {
		return kupola;
	}
	
	public int getID() {
		return ID;
	}
	
	
	
	
}
