package etf.santorini.hd150197d;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Igrac {
	
	protected static int prevId = 0;
	protected int ID = prevId++;
	
	protected List<Figura> figure;
	
	protected String ime;
	
	protected Color boja;
	
	public Igrac() {
		this.figure = new ArrayList<Figura>();
	}
	
	public Igrac(Color boja) {
		ime = "Player" + ID;
		this.boja = boja;
		this.figure = new ArrayList<Figura>();
	}

	public Igrac(List<Figura> figure, Color boja) {
		this(boja);
		this.figure = figure;
		figure.forEach(t -> t.setP(this));
	}
	
	public Igrac (Figura f1, Figura f2, Color boja) {
		this(boja);
		this.figure.add(f1);
		this.figure.add(f2);
		f1.setP(this);
		f2.setP(this);
	}
	
	public Igrac(String ime) {
		this.ime = ime;
	}
	
	public boolean has(Figura f) {
		Iterator<Figura> it = figure.iterator();
		while (it.hasNext())
			if (it.next() == f) return true;
		return false;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public int getID() {
		return ID;
	}

	public List<Figura> getFigure() {
		return figure;
	}

	public void setFigure(List<Figura> figure) {
		this.figure = figure;
		figure.forEach(t -> t.setP(this));
	}
	
	public void addFigura(Figura f) {
		this.figure.add(f);
		f.setP(this);
	}

	public Color getBoja() {
		return boja;
	}

	public void setBoja(Color boja) {
		this.boja = boja;
	}
	
	
	
}
