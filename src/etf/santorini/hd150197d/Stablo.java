package etf.santorini.hd150197d;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Queue;
import java.util.Scanner;

import org.omg.CORBA.Current;

public class Stablo {
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
	private class Node{
		public Koordinata start;
		public Koordinata move;
		public Koordinata build;
		
		public Node(Koordinata start, Koordinata move, Koordinata build) {
			super();
			this.start = start;
			this.move = move;
			this.build = build;
		}

		public Node() {
			super();
		}
		
	}
	private Igrac P1, P2;
	private Igrac trenutni;
	
	private boolean gameOver;
	
	private class Polje {
		public int x;
		public int y;
		
		public int nivo;
		public Figura f;
		public Polje(int x, int y) {
			super();
			this.x = x;
			this.y = y;
			nivo = 0;
			f = null;
		}
			
	}
	
	private Node root = new Node();
	
	public static int WIDTH = 5;
	public static int HEIGHT = 5;
	
	private Polje[][] tabla = new Polje[HEIGHT][WIDTH];
	
	private File pocetnoStanje;
	
	public Stablo(File pocetnoStanje, Igrac P1, Igrac P2) {
		super();
		this.pocetnoStanje = pocetnoStanje;
		this.P1 = P1;
		this.P2 = P2;
		trenutni = P1;
		
		for(int i = 0; i < HEIGHT; i++)
			for(int j = 0; j < HEIGHT; j++)
				tabla[i][j] = new Polje(j, i);
		
		init();
	}
	
	private void init() {
		int errorCode = 0;
		String poruka;
		try {
			Scanner sc = new Scanner(pocetnoStanje);
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				
				if (line.length() > 10) {
					errorCode = -1;
					break;
				}
				if (line.length() < 2) {
					errorCode = -2;
					break;
				}
				int startY = line.charAt(0) - 'A';
				int startX = line.charAt(1) - '0';
				
				if (!checkCoord(startX) || !checkCoord(startY)) {
					errorCode = -5;
					break;
				}
				
				if (line.length() < 5) {
					errorCode = -3;
					break;
				}
				int moveY = line.charAt(3) - 'A';
				int moveX = line.charAt(4) - '0';
				
				if (!checkCoord(moveX) || !checkCoord(moveY)) {
					errorCode = -5;
					break;
				}
				
				if (line.length() < 8) {
					if (tabla[moveY][moveX].nivo < 3 || isValid(trenutni, startX, startY, moveX, moveY) != null) {
						errorCode = -4;
						break;
					}
					
					tabla[moveY][moveX].f = tabla[startY][startY].f;
					tabla[startY][startY].f = null;
					gameOver = true;
					
					break;

				}
				int buildY = line.charAt(6) - 'A';
				int buildX = line.charAt(7) - '0';
				
				if (!checkCoord(buildX) || !checkCoord(buildY)) {
					errorCode = -5;
					break;
				}	
				
				if (!trenutni.has(tabla[startY][startX].f)) {
					errorCode = -6;
					break;
				}
				
				if ((poruka = isValid(trenutni, startX, startY, moveX, moveY, buildX, buildY)) != null) {
					errorCode = -7;
					break;
				}
				
				tabla[moveY][moveX].f = tabla[startY][startY].f;
				tabla[startY][startY].f = null;
				tabla[buildY][buildX].nivo++;
				
				if (tabla[moveY][moveX].nivo == 3) {
					gameOver = true;
					break;
				}
				
				trenutni = (trenutni == P1) ? P2 : P1;
				
			}
			
			sc.close();
		} catch (FileNotFoundException e) {
			errorCode = -8;
		}
				
		if (gameOver == true) {
			return;
		}else switch(errorCode) {
		case -1: poruka = "Dugacka komanda.";
				break;
		case -2: poruka = "Kratka komanda.";
				break;
		case -3: poruka = "Nedostaje koordinata pomeraja.";
				break;
		case -4: poruka = "Nedostaje koordinata izgradnje.";
				break;
		case -5: poruka = "Ilegalna koordinata.";
				break;
		case -6: poruka = "Nije igrac na potezu.";
				break;
		case -7: break;
		case -8: poruka = "Nepostojeci fajl.";
				break;
		default: poruka = "Nepoznata greska.";
			
		}

	
	}
	
	private String isValid(Igrac current, int startX, int startY, int moveX, int moveY,  int buildX, int buildY) {
		if (Math.abs(startX-moveX) > 1 || Math.abs(startY-moveY) > 1) return "Nisu susedna polja pri pomeranju figure.";
		if (Math.abs(buildX-moveX) > 1 || Math.abs(buildY-moveY) > 1) return "Nisu susedna polja pri izgradnji zgrade.";
		
		if (!current.has(tabla[startY][startX].f)) return "Nije igrac na potezu.";
		
		if (tabla[moveY][moveX].f != null || tabla[buildY][buildX].f != null) return "Polje je zauzeto.";
		
		if (tabla[moveY][moveX].nivo - tabla[startY][startX].nivo > 1) return "Polje je nedostizno.";
		
		if (tabla[buildY][buildX].nivo == 4 || tabla[moveY][moveX].nivo == 4) return "Polje ima izgradjenu kupolu.";
			
		return null;
	}
	
	private String isValid(Igrac current, int startX, int startY, int moveX, int moveY) {
		if (Math.abs(startX-moveX) > 1 || Math.abs(startY-moveY) > 1) return "Nisu susedna polja pri pomeranju figure.";
		
		if (!current.has(tabla[startY][startX].f)) return "Nije igrac na potezu.";
		
		if (tabla[moveY][moveX].f != null) return "Polje je zauzeto.";
		
		if (tabla[moveY][moveX].nivo - tabla[startY][startX].nivo > 1) return "Polje je nedostizno.";
		
		if (tabla[moveY][moveX].nivo == 4) return "Polje ima izgradjenu kupolu.";
			
		return null;
	}
	
	private boolean checkCoord(int a) {
		if (a < 0) return false;
		if (a > 4) return false;
		return true;
	}
	
	private boolean createTree() {		
		
		return true;
	}

}
