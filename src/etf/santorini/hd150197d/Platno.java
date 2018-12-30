package etf.santorini.hd150197d;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

public class Platno extends Canvas {
	private Figura f;
	private Zgrada z;
	
	public static final int hSpace = 4;
	public static final int vSpace = 1;
	
	public Platno(Figura f, Zgrada z) {
		super();
		this.f = f;
		this.z = z;
	}
	
	public Platno() {
		super();
	}
	
	
	public Figura getF() {
		return f;
	}


	public void setF(Figura f) {
		this.f = f;
	}


	public Zgrada getZ() {
		return z;
	}


	public void setZ(Zgrada z) {
		this.z = z;
	}


	public synchronized void paint(Graphics g) {
		int w = getWidth(), h = getHeight();
		g.translate(0, h-6);
		int brElem = 0;
		if (z != null && (brElem = z.getNivo()) > 0) {
			g.setColor(Color.WHITE);
			for(int i = 0; i < (z.isKupola() ? 3 : brElem); i++) 
				g.fillRect((i+1)*hSpace-1, -((i+1)*h/4-1-vSpace), w-2*(i+1)*hSpace, h/4-2*vSpace);
			
			if (z.isKupola()) {
				g.setColor(new Color(109, 142, 242));
				g.fillArc(3*hSpace-1, -(h-6), w-2*3*hSpace, (h-2*6)/2-2*vSpace, 0, 180);
				return;
			}
		}
		if (f != null) {
			g.setColor(f.getP().getBoja());
			g.fillOval(w/2 - (h/8-3) - 1, -(((brElem+1)*h/4-4)-1), h/4-6, h/4-6); // 3*h/4-1-vSpace
		}
	}
	
	
}
