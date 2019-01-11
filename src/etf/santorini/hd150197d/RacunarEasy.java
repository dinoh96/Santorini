package etf.santorini.hd150197d;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Vector;

public class RacunarEasy extends Igrac {
	protected static int prevId = 0;
	protected int ID = prevId++;
	
	protected int minimax = 0;
	
	public static int WIDTH = 5;
	public static int HEIGHT = 5;
	
		
	protected class Node{
		public int dubina;
		public Potez info;
		public LinkedList<Node> children;
		public boolean gameOver = false;
		
		public Node(Potez info) {
			super();
			this.info = info;
		}
		public Node(Potez info, int dubina) {
			super();
			this.info = info;
			this.dubina = dubina;
		}
		public Node() {
			super();
			children = new LinkedList<>();
		}
	}
	protected class Polje{
		public int x;
		public int y;
		public boolean f;
		public int nivo;
		public Polje(int x, int y) {
			super();
			this.x = x;
			this.y = y;
		}
		public Polje(int x, int y, int nivo) {
			super();
			this.x = x;
			this.y = y;
			this.nivo = nivo;
		}
		public Polje(int x, int y, boolean f, int nivo) {
			super();
			this.x = x;
			this.y = y;
			this.f = f;
			this.nivo = nivo;
		}
	}
	
	protected etf.santorini.hd150197d.Polje[][] tabla;	
	protected Igrac otherPlayer;
	protected int dubina = 5;
	protected Node koren;
	
	protected boolean gameOver = false;
	protected Integer alpha, beta;
	
	protected boolean preparedPotez;
	protected LinkedList<Node> potezi;
	
	protected long vreme;
	
	public RacunarEasy() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RacunarEasy(Color boja) {
		super(boja);
		// TODO Auto-generated constructor stub
	}

	public RacunarEasy(Figura f1, Figura f2, Color boja) {
		super(f1, f2, boja);
		// TODO Auto-generated constructor stub
	}

	public RacunarEasy(List<Figura> figure, Color boja) {
		super(figure, boja);
		// TODO Auto-generated constructor stub
	}

	public RacunarEasy(String ime) {
		super(ime);
		// TODO Auto-generated constructor stub
	}

	public etf.santorini.hd150197d.Polje[][] getTabla() {
		return tabla;
	}

	public void setTabla(etf.santorini.hd150197d.Polje[][] tabla) {
		this.tabla = tabla;
	}

	public Igrac getOtherPlayer() {
		return otherPlayer;
	}

	@Override
	public void setOtherPlayer(Igrac otherPlayer) {
		this.otherPlayer = otherPlayer;
	}
	
	public int getDubina() {
		return dubina;
	}

	public void setDubina(int dubina) {
		this.dubina = dubina;
	}

	public Node getKoren() {
		return koren;
	}

	public void setKoren(Node koren) {
		this.koren = koren;
	}
	
	private int rastojanje(Koordinata p, Koordinata q) {
		//manhattan
		
		return Math.abs(p.getX()-q.getX()) + Math.abs(p.getY()-q.getY());
		//return (int)Math.sqrt(Math.pow(p.getX()-q.getX(), 2) + Math.pow(p.getY()-q.getY(), 2));
	}
	
	protected int izracunajM(Potez potez, Polje[][] tabla) {
		int m = 0;
		
		m = tabla[potez.getMove().getY()][potez.getMove().getX()].nivo;
		
		return m;
	}
	
	private int izracunajL(Potez potez, Polje[][] tabla) {
		int protivnikoveFigure = 0;
		int mojeFigure = 0;
		for(Figura f: figure) protivnikoveFigure += rastojanje(potez.getBuild(), new Koordinata(f.getCol(), f.getRow()));
		for(Figura f: otherPlayer.getFigure()) {
			mojeFigure += rastojanje(potez.getBuild(), new Koordinata(f.getCol(), f.getRow()));
		}
		return (mojeFigure - protivnikoveFigure)*tabla[potez.getBuild().getY()][potez.getBuild().getX()].nivo;
	}

	protected int izracunajFunkciju(Potez potez, Polje[][] tabla) {
		//f = m + l
		int m = izracunajM(potez, tabla);
		int l = izracunajL(potez, tabla);
		
		return m + l;
	}
	
	protected LinkedList<Node> napraviListuPoteza(Polje[][] tabla, Igrac ig, int dubina) {
		LinkedList<Node> potezi = new LinkedList<>();
		
		for(Figura f: ig.getFigure()) {
			int x = f.getCol();
			int y = f.getRow();
			Koordinata start = new Koordinata(x, y);
			
			if (x == 4 && y == 0) {
				x = 4;
			}
					
			for (int j = ((y-1 < 0) ? 0 : (y-1)); j <= ((y+1 > 4) ? 4 : (y+1)); j++)
				for(int i = ((x-1 < 0) ? 0 : (x-1)); i <= ((x+1 > 4) ? 4 : (x+1)); i++)	{
					if (!isValid(this, tabla, f, y, x, j, i)) continue;
					if (!isValidMove(this, tabla, f, y, x, j, i)) continue;
					tabla[y][x].f = false;
 					tabla[j][i].f = true;
					Koordinata move = new Koordinata(i, j);
					for (int k = ((j-1 < 0) ? 0 : (j-1)); k <= ((j+1 > 4) ? 4 : (j+1)); k++)
						for(int m = ((i-1 < 0) ? 0 : (i-1)); m <= ((i+1 > 4) ? 4 : (i+1)); m++)	{
							if (!isValid(this, tabla, f, j, i, k, m)) continue;
							if (!isValidBuild(this, tabla, f, j, i, k, m)) continue;
							Koordinata build = new Koordinata(m, k);
							Potez potez = new Potez(start, move, build);
							potez.setProcena((dubina % 2 == 1) ? Integer.MAX_VALUE : Integer.MIN_VALUE);	
							potez.setFigura(f);
							potezi.add(new Node(potez, dubina + 1));
						}
					tabla[j][i].f = false;
					tabla[y][x].f = true;
				}
		}
		return potezi;
	}	
	protected Polje[][] cloneTabla() {
		Polje[][] cloneTabla = new Polje[HEIGHT][WIDTH];
		
		for(int j = 0; j < HEIGHT; j++)
			for(int i = 0; i < WIDTH; i++) 
				cloneTabla[j][i] = new Polje(i, j, tabla[j][i].getF() != null, tabla[j][i].getZ().getNivo());
		return cloneTabla;
	}	
	
	public boolean isValid(Igrac p, Polje[][] tabla, Figura f, int y1, int x1, int y2, int x2){		
		Polje D = tabla[y2][x2];
		
		if (D.f) return false;
		if (D.nivo == 4) return false;
		return true;
	}
	
	public boolean isValidBuild(Igrac p, Polje[][] tabla,  Figura f, int y1, int x1, int y2, int x2) {
		if (Math.abs(x1-x2) > 1 || Math.abs(y1-y2) > 1) return false;
		
		return true;
	}
	public boolean isValidMove(Igrac p, Polje[][] tabla, Figura f, int y1, int x1, int y2, int x2) {
		if (tabla[y2][x2].nivo - tabla[y1][x1].nivo > 1) return false;
		
		return true;		
	}
	
	@Override
	public Potez napraviStablo() {
		if (!preparedPotez) minimax = 0;
		Node root = new Node();
		int dubina = this.dubina;
		
		
		Potez next = null;
		int max = Integer.MIN_VALUE;
		if (!preparedPotez) {
			long start = System.currentTimeMillis();
			
			Polje[][] tabla = cloneTabla();
			root.children = napraviListuPoteza(tabla, this, 0);	

			for(Node t : root.children) {
	
				t.info.setProcena(minimax(t, tabla, 1));
	
				if (max < t.info.getProcena()) {
					max = t.info.getProcena();
					next = t.info;
				}
				
			}
			
			vreme = System.currentTimeMillis()-start;
		}else {
			for(Node t: this.potezi)
				if (max < t.info.getProcena()) {
					max = t.info.getProcena();
					next = t.info;
				}
		}
		System.out.println(minimax + " : " + vreme);
		gameOver = false;
		
		this.potezi = null;
		preparedPotez = false;
		
		return next;
		
	}
	
	@Override
	public void izracunajProcene(LinkedList<Potez> potezi) {
		long start = System.currentTimeMillis();
		
		Polje[][] tabla = cloneTabla();
		LinkedList<Node> cvorovi = napraviListuPoteza(tabla, this, 0);	
		
		for(Node t : cvorovi)
			t.info.setProcena(minimax(t, tabla, 1));
		
		vreme = System.currentTimeMillis() - start;
		
		this.potezi = cvorovi;
		preparedPotez = true;
		
		if (potezi != null) cvorovi.forEach(t -> potezi.add(t.info));
	}

	protected int minimax(Node child, Polje[][] tabla, int dubina) {
		minimax++;
		
		Igrac ig = ((dubina & 2) == 0) ? otherPlayer : this;
		boolean thisPlayer = ig == this;
		
		if (child.gameOver || dubina == this.dubina) 
			return izracunajFunkciju(child.info, tabla);

		setPos(tabla, child.info.getFigura(), child.info.getMove().getY(), child.info.getMove().getX());

		int ret;
		
		if (thisPlayer) ret = Integer.MIN_VALUE;
		else ret = Integer.MAX_VALUE;
		
		LinkedList<Node> deca = napraviListuPoteza(tabla, ig, dubina);
		
		for(Node tmp : deca) {
			if (tmp.info.getStart().getX() == child.info.getStart().getX() && tmp.info.getStart().getY() == child.info.getStart().getY() && 
				tmp.info.getMove().getX() == child.info.getMove().getX() && tmp.info.getMove().getY() == child.info.getMove().getY() && 
				tmp.info.getBuild().getX() == child.info.getBuild().getX() && tmp.info.getBuild().getY() == child.info.getBuild().getY()) continue;
			if (tabla[tmp.info.getMove().getY()][tmp.info.getMove().getX()].nivo == 3) tmp.gameOver = true;
			int val = minimax(tmp, tabla, tmp.dubina);
			if (thisPlayer && tmp.gameOver) {
				gameOver = true;
				ret = val;
				break;
			}
			if (thisPlayer) {
				ret = Integer.max(val, ret);
			}
			else ret = Integer.min(val, ret);
		}
		setPos(tabla, child.info.getFigura(), child.info.getStart().getY(), child.info.getStart().getX());

		return ret;
	}

	protected void setPos(Polje[][] tabla, Figura f, int y, int x){
		int row = f.getRow();
		int col = f.getCol();

		tabla[row][col].f = false;
		tabla[y][x].f = true;
		f.setPos(y, x);
	}
}
