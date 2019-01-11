package etf.santorini.hd150197d;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Polje {
	public static final int platnoWidth = 88;
	public static final int platnoHeight = 88;
	private static int prevId = 0;
	private int ID = prevId++;
	
	private Zgrada z;
	private Figura f;
	
	private int row;
	private int col;
	
	private Platno platno;
	
	private Panel glavni;
	
	private List<Panel> obod;
	private Color obodBoja;
	private Panel pnl = new Panel();
		
	public Polje(int row, int col) {
		super();
		
		this.z = new Zgrada(this);
		this.row = row;
		this.col = col;
		
		pnl.setLayout(new BorderLayout());
		pnl.setBackground(Color.LIGHT_GRAY);
		
		glavni = new Panel();
		
		obod = new ArrayList<Panel>();
		Panel tmp = new Panel(); pnl.add(tmp, BorderLayout.NORTH); obod.add(tmp); tmp.setBackground(null);
		tmp = new Panel(); pnl.add(tmp, BorderLayout.SOUTH); obod.add(tmp); tmp.setBackground(null);
		tmp = new Panel(); pnl.add(tmp, BorderLayout.EAST); obod.add(tmp); tmp.setBackground(null);
		tmp = new Panel(); pnl.add(tmp, BorderLayout.WEST); obod.add(tmp); tmp.setBackground(null);
		pnl.add(glavni, BorderLayout.CENTER);

		glavni.setBackground(null);
		platno = new Platno();
		platno.setBounds(0, 0, platnoWidth, platnoHeight);
		platno.setZ(z);
		
		glavni.add(platno);
		
	}

	public Zgrada getZ() {
		return z;
	}

	public void setZ(Zgrada z) {
		this.z = z;
	}

	public int getID() {
		return ID;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public Figura getF() {
		return f;
	}

	public void setF(Figura f) {
		this.f = f;
		platno.setF(f);
		repaint();
	}
	
	public void repaint() {
		platno.repaint();
	}

	public Panel getPnl() {
		return pnl;
	}

	public void setPnl(Panel pnl) {
		this.pnl = pnl;
	}
	
	public void changeColor(Color boja) {
		obod.forEach(t -> t.setBackground(boja));
		obodBoja = boja;
	}
	
	public Color getObodBoja() {
		return obodBoja;
	}
	
	public void addMouseListener(MouseListener ml) {
		platno.addMouseListener(ml);
		glavni.addMouseListener(ml);
		pnl.addMouseListener(ml);
		obod.forEach(t -> t.addMouseListener(ml));
	}

	public void setProcena(int procena) {
		platno.setProcena(procena);
	}
	
	public int getProcena() {
		return platno.getProcena();
	}
	
	public void enableProcena() {
		platno.setProceni(true);
	}
	
	public void disableProcena() {
		platno.setProceni(false);
	}
	
}
