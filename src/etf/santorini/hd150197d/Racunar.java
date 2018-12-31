package etf.santorini.hd150197d;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class Racunar extends Igrac {
	private class Koordinata{
		public int x;
		public int y;
		
		public Koordinata() {
			super();
		}
		public Koordinata(int x, int y) {
			super();
			this.x = x;
			this.y = y;
		}	
	}
	private class Potez{
		public Koordinata start;
		public Koordinata move;
		public Koordinata build;
		public int procena;
		
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
		
		
	}
	
	private Tabla tabla;	
	private Igrac otherPlayer;
	private int dubina = 1;
	
	public Racunar() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Racunar(Color boja) {
		super(boja);
		// TODO Auto-generated constructor stub
	}

	public Racunar(Figura f1, Figura f2, Color boja) {
		super(f1, f2, boja);
		// TODO Auto-generated constructor stub
	}

	public Racunar(List<Figura> figure, Color boja) {
		super(figure, boja);
		// TODO Auto-generated constructor stub
	}

	public Racunar(String ime) {
		super(ime);
		// TODO Auto-generated constructor stub
	}

	public Tabla getTabla() {
		return tabla;
	}

	public void setTabla(Tabla tabla) {
		this.tabla = tabla;
	}

	public Igrac getOtherPlayer() {
		return otherPlayer;
	}

	public void setOtherPlayer(Igrac otherPlayer) {
		this.otherPlayer = otherPlayer;
	}
	
	public int getDubina() {
		return dubina;
	}

	public void setDubina(int dubina) {
		this.dubina = dubina;
	}

	private int rastojanje(Koordinata p, Koordinata q) {
		//manhattan
		
		return Math.abs(p.x-q.x) + Math.abs(p.y-q.y);
	}
	
	private int izracunajM(Potez potez) {
		int m = 0;
		
		m = tabla.getTabla()[potez.move.y][potez.move.x].getZ().getNivo();
		
		return m;
	}
	
	private int izracunajL(Potez potez) {
		int l = 0;
		
		for(Figura f: figure) l += rastojanje(potez.build, new Koordinata(f.getCol(), f.getRow()));
		for(Figura f: otherPlayer.getFigure()) l += rastojanje(potez.build, new Koordinata(f.getCol(), f.getRow()));
		
		return l;
	}

	private int izracunajFunkciju(Potez potez) {
		//f = m + l
		
		return izracunajM(potez) + izracunajL(potez);
	}
	
	//private PriorityQueue<Potez> napraviListuPoteza() {
	private LinkedList<Potez> napraviListuPoteza() {
		//PriorityQueue<Potez> potezi = new PriorityQueue<>((a, b) ->  {return Integer.compare(a.procena, b.procena);});
		LinkedList<Potez> potezi = new LinkedList<>();
		
		for(Figura f: figure) {
			int x = f.getCol();
			int y = f.getRow();
			Koordinata start = new Koordinata(x, y);
			
			for (int j = ((y-1 < 0) ? 0 : (y-1)); j <= ((y+1 > 4) ? 4 : (y+1)); j++)
				for(int i = ((x-1 < 0) ? 0 : (x-1)); i <= ((x+1 > 4) ? 4 : (x+1)); i++)	{
					if (!tabla.isValid(this, f, y, x, j, i)) continue;
					if (!tabla.isValidMove(this, f, y, x, j, i)) continue;
					
					Potez move = new Potez(start, new Koordinata(i, j), null);
					int M = izracunajM(move);
					
					for (int k = ((j-1 < 0) ? 0 : (j-1)); k <= ((j+1 > 4) ? 4 : (j+1)); k++)
						for(int m = ((i-1 < 0) ? 0 : (i-1)); m <= ((i+1 > 4) ? 4 : (i+1)); m++) {
							if (!tabla.isValidBuild(this, f, y, x, k, m)) continue;
							
							Potez potez = new Potez(start, new Koordinata(i, j), new Koordinata(m, k));
							potez.procena = M + izracunajL(potez);
							
							potezi.add(potez); // ovo je kad se pravi stablo
							// ako ne pravim stablo, ovde samo uporedjujem sa lokalnim min/max 
						}
				}
		}
		return potezi;
	}
	
	private void napraviStablo() {
		
	}
}
