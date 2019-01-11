package etf.santorini.hd150197d;

import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.TextArea;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Log extends Frame{
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
	private boolean stepByStep;
	
	public static int WIDTH = 5;
	public static int HEIGHT = 5;
	
	private Polje[][] tabla = new Polje[HEIGHT][WIDTH];
	
	private File pocetnoStanje;
	
	private StringBuilder log = new StringBuilder();
	private TextArea logTextArea = new TextArea();
	private int sirinaEkrana;
	
	private String poruka;
	private String mod;
	private int errorCode;

	public static int parseCol(int x){
		return x + 1;
	}

	public static char convertCol(int x){
		return (char)('1' + x);
	}

	public static char parseRow(int x){
		return (char)('A' + x);
	}
	
	public Log(File pocetnoStanje, Igrac P1, Igrac P2, int sirinaEkrana, String mod) {
		super();
		this.pocetnoStanje = pocetnoStanje;
		this.P1 = P1;
		this.P2 = P2;
		this.sirinaEkrana = sirinaEkrana;
		this.mod = mod;
		this.trenutni = P1;
		stepByStep = false;
		for(int i = 0; i < HEIGHT; i++)
			for(int j = 0; j < WIDTH; j++)
				tabla[i][j] = new Polje(i, j);
		
		addWindowListener(new WindowAdapter() {
			 public void windowClosing(WindowEvent we) {
			 setVisible(false);
			 }});
		 
		init();
		
		logTextArea.setText(log.toString());
		add(logTextArea);
		logTextArea.setEditable(false);
		logTextArea.setCaretPosition(log.length()-1);
		logTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
		
		
		setBounds((sirinaEkrana-600)/2 + 600, 0, 300, 200);
		setResizable(false);
		
		Tabla tabla = new Tabla(P1, P2, pocetnoStanje, gameOver, errorCode, poruka, this.tabla, this, trenutni, mod);
		
		tabla.setBounds((sirinaEkrana-600)/2, 0, 600, 703);
		tabla.setResizable(false);
		tabla.setVisible(true);
		setVisible(true);
	}
	
	private void init() {
		poruka = null;
		String line;
		try (Scanner sc = new Scanner(pocetnoStanje)){
			
			trenutni = P1;
			line = sc.nextLine();
			if ((errorCode = checkMove(line, true)) != 0) {
				preparePoruka();
				return;
			}
			log.append(line).append('\n');
			logTextArea.setText(log.toString());
			
			trenutni = P2;
			line = sc.nextLine();
			if ((errorCode = checkMove(line, true)) != 0) {
				preparePoruka();
				return;
			}
			log.append(line).append('\n');
			logTextArea.setText(log.toString());
			
			trenutni = P1;
			
			while (sc.hasNextLine()) {
				line = sc.nextLine();
				if ((errorCode = checkMove(line, false)) != 0) break;
				
				trenutni = (trenutni == P1) ? P2 : P1;
			}
			
		} catch (FileNotFoundException e) {
			errorCode = -8;
		}
				
		if (gameOver == true) return;
		else preparePoruka();

	
	}
	
	private void preparePoruka() {
		switch(errorCode) {
		case 0 : return;
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
		log.append(poruka).append('\n');
		logTextArea.setText(log.toString());
		
	}
	
	private String isValid(Igrac current, int startX, int startY, int moveX, int moveY,  int buildX, int buildY) {
		if (Math.abs(startX-moveX) > 1 || Math.abs(startY-moveY) > 1) return "Nisu susedna polja pri pomeranju figure.";
		if (Math.abs(buildX-moveX) > 1 || Math.abs(buildY-moveY) > 1) return "Nisu susedna polja pri izgradnji zgrade.";
		
		if (!current.has(tabla[startY][startX].getF())) return "Nije igrac na potezu.";
		
		if (tabla[moveY][moveX].getF() != null || (tabla[buildY][buildX].getF() != null && tabla[buildY][buildX] != tabla[startY][startX])) 
			return "Polje je zauzeto.";
		
		if (tabla[moveY][moveX].getZ().getNivo() - tabla[startY][startX].getZ().getNivo() > 1) return "Polje je nedostizno.";
		
		if (tabla[buildY][buildX].getZ().getNivo() == 4 || tabla[moveY][moveX].getZ().getNivo() == 4) return "Polje ima izgradjenu kupolu.";
			
		return null;
	}
	
	private String isValid(Igrac current, int startX, int startY, int moveX, int moveY) {
		if (Math.abs(startX-moveX) > 1 || Math.abs(startY-moveY) > 1) return "Nisu susedna polja pri pomeranju figure.";
		
		if (!current.has(tabla[startY][startX].getF())) return "Nije igrac na potezu.";
		
		if (tabla[moveY][moveX].getF() != null) return "Polje je zauzeto.";
		
		if (tabla[moveY][moveX].getZ().getNivo() - tabla[startY][startX].getZ().getNivo() > 1) return "Polje je nedostizno.";
		
		if (tabla[moveY][moveX].getZ().getNivo() == 4) return "Polje ima izgradjenu kupolu.";
			
		return null;
	}
	
	private boolean checkCoord(int a) {
		if (a < 0 || a > 4) return false;
		return true;
	}
	
	private int checkMove(String line, boolean init) {
		
		if (line.length() > 10) return -1;
	
		if (line.length() < 2) return -2;
		
		int startY = line.charAt(0) - 'A';
		int startX = line.charAt(1) - '1';
		
		if (!checkCoord(startX) || !checkCoord(startY)) return -5;
	
		
		if (line.length() < 5) return -3;

		int moveY = line.charAt(3) - 'A';
		int moveX = line.charAt(4) - '1';
		
		if (!checkCoord(moveX) || !checkCoord(moveY)) return -5;
		
		if (init) {
			Figura f = new Figura(startY, startX, trenutni);
			trenutni.addFigura(f);
			tabla[startY][startX].setF(f);
			
			f = new Figura(moveY, moveX, trenutni);
			trenutni.addFigura(f);
			tabla[moveY][moveX].setF(f);
			
			return 0;
		}
		
		if (line.length() < 8) {
			if (tabla[moveY][moveX].getZ().getNivo() < 3 || isValid(trenutni, startX, startY, moveX, moveY) != null) return -4;
			
			tabla[moveY][moveX].setF(tabla[startY][startY].getF());
			tabla[startY][startX].setF(null);
			gameOver = true;
			log.append(line.substring(0, 4)).append('\n');
			logTextArea.setText(log.toString());
			return 0;

		}
		int buildY = line.charAt(6) - 'A';
		int buildX = line.charAt(7) - '1';
		
		if (!checkCoord(buildX) || !checkCoord(buildY)) return -5;

		
		Figura f = tabla[startY][startX].getF();
		
		if (!trenutni.has(f)) return -6;
	
		
		if ((poruka = isValid(trenutni, startX, startY, moveX, moveY, buildX, buildY)) != null) return -7;
		
		
		
		tabla[moveY][moveX].setF(f);
		f.setCol(moveX);
		f.setRow(moveY);
		tabla[startY][startX].setF(null);
		tabla[buildY][buildX].getZ().incNivo();
		
		log.append(line).append('\n');
		logTextArea.setText(log.toString());
		
		
		return 0;
	}

	public boolean isStepByStep() {
		return stepByStep;
	}

	public void setStepByStep(boolean stepByStep) {
		this.stepByStep = stepByStep;
	}
	
	public StringBuilder append(String s) {
		log.append(s);
		logTextArea.setText(log.toString());
		logTextArea.setCaretPosition(log.length());
		return log;
	}
	
	public StringBuilder append(char s) {
		log.append(s);
		logTextArea.setText(log.toString());
		logTextArea.setCaretPosition(log.length());
		return log;
	}
	public StringBuilder append(int s) {
		log.append(s);
		logTextArea.setText(log.toString());
		logTextArea.setCaretPosition(log.length());
		return log;
	}

	public StringBuilder getLog() {
		return log;
	}
	
	
	
}
