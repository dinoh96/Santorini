package etf.santorini.hd150197d;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import etf.santorini.hd150197d.RacunarEasy.Node;
import etf.santorini.hd150197d.RacunarEasy.Polje;

public class RacunarMedium extends RacunarEasy {
	public RacunarMedium() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RacunarMedium(Color boja) {
		super(boja);
		// TODO Auto-generated constructor stub
	}

	public RacunarMedium(Figura f1, Figura f2, Color boja) {
		super(f1, f2, boja);
		// TODO Auto-generated constructor stub
	}

	public RacunarMedium(List<Figura> figure, Color boja) {
		super(figure, boja);
		// TODO Auto-generated constructor stub
	}

	public RacunarMedium(String ime) {
		super(ime);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Potez napraviStablo() {
		alpha = Integer.MIN_VALUE;
		beta = Integer.MAX_VALUE;
		return super.napraviStablo();
	}

	@Override
	protected int minimax(Node child, Polje[][] tabla, int dubina) {
		return minimaxAlphaBeta(child, tabla, dubina);
	}
	
	protected int minimaxAlphaBeta(Node child, Polje[][] tabla, int dubina) {
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
			int val = minimaxAlphaBeta(tmp, tabla, tmp.dubina);
			
			if (thisPlayer && tmp.gameOver) {
				gameOver = true;
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
	
}
