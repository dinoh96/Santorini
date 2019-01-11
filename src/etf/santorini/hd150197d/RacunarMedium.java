package etf.santorini.hd150197d;

import java.util.LinkedList;

import etf.santorini.hd150197d.RacunarEasy.Node;

public class RacunarMedium extends RacunarEasy {

	private Integer alpha, beta;
	
	
	
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
		
		if (child.gameOver || dubina == this.dubina) {
			int f;
			if (!child.gameOver) f = izracunajFunkciju(child.info);
			else {
				f = thisPlayer ? Integer.MAX_VALUE : Integer.MIN_VALUE;
			}
			return f;
		}
			
		//tabla[child.info.start.y][child.info.start.x].f = false;
		//tabla[child.info.move.y][child.info.move.x].f = true;

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
			
			if (thisPlayer) {
				if (val > ret) {
					ret = val;
					if (val >= beta) return val;
					alpha = Integer.max(alpha, ret);
				}
			}
			else {
				if (val < ret) {
					ret = val;
					if (val <= alpha) return val;
					beta = Integer.min(beta, ret);
				}
			}
		}
		
		//tabla[child.info.start.y][child.info.start.x].f = true;
		//tabla[child.info.move.y][child.info.move.x].f = false;

		setPos(tabla, child.info.getFigura(), child.info.getStart().getY(), child.info.getStart().getX());

		return ret;
	}
	
}
