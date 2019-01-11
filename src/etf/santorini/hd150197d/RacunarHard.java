package etf.santorini.hd150197d;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import etf.santorini.hd150197d.RacunarEasy.Node;
import etf.santorini.hd150197d.RacunarEasy.Polje;

public class RacunarHard extends RacunarEasy {
	
	public RacunarHard() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RacunarHard(Color boja) {
		super(boja);
		// TODO Auto-generated constructor stub
	}

	public RacunarHard(Figura f1, Figura f2, Color boja) {
		super(f1, f2, boja);
		// TODO Auto-generated constructor stub
	}

	public RacunarHard(List<Figura> figure, Color boja) {
		super(figure, boja);
		// TODO Auto-generated constructor stub
	}

	public RacunarHard(String ime) {
		super(ime);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Potez napraviStablo() {
		 if (!preparedPotez) minimax = 0;
		alpha = Integer.MIN_VALUE;
		beta = Integer.MAX_VALUE;
		Node root = new Node();
		int dubina = this.dubina;
		
		int max = Integer.MIN_VALUE;
		Potez next = null;
		
		if (!preparedPotez){
			long start = System.currentTimeMillis();
			
			Polje[][] tabla = cloneTabla();
			root.children = napraviListuPoteza(tabla, this, 0);	
			
			
			for(Node t : root.children) {

				t.info.setProcena(minimax(t, tabla, 1));

				if (max < t.info.getProcena()) {
					max = t.info.getProcena();
					next = t.info;
				}
				
				if (t.gameOver) {
					System.out.println(minimax + " : " + (System.currentTimeMillis()-start));
					gameOver = false;
					
					vreme = System.currentTimeMillis()-start;
					System.out.println(minimax + " : " + vreme);
					
					return next;				
				}
				
				vreme = System.currentTimeMillis()-start;
				
			}
		}else{
			for(Node t : this.potezi) {

				if (max < t.info.getProcena()) {
					max = t.info.getProcena();
					next = t.info;
				}
				
				if (t.gameOver) {
					System.out.println(minimax + " : " + vreme);
					gameOver = false;

					return next;				
				}
				
			}

		}
		System.out.println(minimax + " : " + vreme);
		gameOver = false;

		this.potezi = null;
		preparedPotez = false;

		return next;
		
	}

	@Override
	protected int minimax(Node child, Polje[][] tabla, int dubina) {
		return minimaxAlphaBeta(child, tabla, dubina);
	}
	
	protected int minimaxAlphaBeta(Node child, Polje[][] tabla, int dubina) {
		minimax++;
		
		Igrac ig = ((dubina & 2) == 0) ? otherPlayer : this;
		boolean thisPlayer = ig == this;
		boolean gameOver = tabla[child.info.getMove().getY()][child.info.getMove().getX()].nivo == 3;
		if (gameOver || dubina == this.dubina) 
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
			int val = minimaxAlphaBeta(tmp, tabla, tmp.dubina);
			
			if (thisPlayer && tmp.gameOver) {
				this.gameOver = true;
				ret = val;
				break;
			}
			
			if (thisPlayer) {
				if (val > ret) {
					ret = val;
					if (val >= beta) break;
					alpha = Integer.max(alpha, ret);
				}
			}
			else {
				if (val < ret) {
					ret = val;
					if (val <= alpha) break;
					beta = Integer.min(beta, ret);
				}
			}
		}
		

		setPos(tabla, child.info.getFigura(), child.info.getStart().getY(), child.info.getStart().getX());

		return ret;
	}
	
	@Override
	protected int izracunajM(Potez potez, Polje[][] tabla) {
		int m = super.izracunajM(potez, tabla);
		return 2*m;
	}
	
	@Override
	public void izracunajProcene(LinkedList<Potez> potezi) {
		long start = System.currentTimeMillis();
		
		Polje[][] tabla = cloneTabla();
		LinkedList<Node> cvorovi = napraviListuPoteza(tabla, this, 0);	
		
		alpha = Integer.MIN_VALUE;
		beta = Integer.MAX_VALUE;
		
		for(Node t : cvorovi)
			t.info.setProcena(minimax(t, tabla, 1));
		
		vreme = System.currentTimeMillis() - start;
		
		this.potezi = cvorovi;
		preparedPotez = true;
		
		if (potezi != null) cvorovi.forEach(t -> potezi.add(t.info));
	}
	
	@Override
	protected int izracunajFunkciju(Potez potez, Polje[][] tabla) {
		int f = super.izracunajFunkciju(potez, tabla);
		if (tabla[potez.getMove().getY()][potez.getMove().getX()].nivo == 3) 
			if (this.has(potez.getFigura()))
				f = Math.abs(f)*2;
			else f = -Math.abs(f)*2;
		
		return f;
	}
	
}