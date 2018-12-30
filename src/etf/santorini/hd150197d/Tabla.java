package etf.santorini.hd150197d;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.awt.Dialog;

public class Tabla extends Frame{
	public final static Color blue = new Color(92, 116, 237);
	
	private static int prevId = 0;
	private int ID = prevId++;
	
	public static int WIDTH = 5;
	public static int HEIGHT = 5;
	
	private Polje[][] tabla = new Polje[HEIGHT][WIDTH];
	
	private Igrac P1;
	private Igrac P2;
	private Label naPotezu;
	
	private Igrac currentPlayer = P2;
	
	private boolean izbor = false;
	private boolean pomeraj = false;
	private boolean gradnja = true;
	
	private Polje[] potez;
	private int index = 0;
	private Figura figura;
	
	private boolean gameOver = false;
	private int brojPoteza = 0;
	
	private Log log;
	
	private void init() {
		setLayout(new BorderLayout(0, 20));
		
		Panel p1 = new Panel();
		add(p1, BorderLayout.NORTH);
		
		Panel p2 = new Panel();
		add(p2, BorderLayout.CENTER);
		
		p1.setLayout(new GridLayout(1, 3));
		p2.setLayout(new BorderLayout());
		
		p1.add(new Label(P1.getIme(), Label.RIGHT));
		p1.add(naPotezu);
		p1.add(new Label(P2.getIme(), Label.LEFT));
		
		Panel tmp = new Panel(new GridLayout(5, 5, 1, 1));
		tmp.setBackground(Color.BLACK);
		for(int i = 0; i < HEIGHT; i++)
			for(int j = 0; j < WIDTH; j++) {
				Polje polje = tabla[i][j];
				Panel pnl = polje.getPnl();
				tmp.add(pnl);
				MouseListener mListener = new MouseListener() {

					@Override
					public void mouseClicked(MouseEvent arg0) {						
					}

					@Override
					public void mouseEntered(MouseEvent arg0) {						
					}

					@Override
					public void mouseExited(MouseEvent arg0) {	
					}

					@Override
					public void mousePressed(MouseEvent arg0) {
						if (index == 0) {
							potez[index] = polje;
							index++;
							figura = polje.getF();
							polje.changeColor(Color.RED);
						}else if (index == 1) {
							if (polje.getObodBoja() == Color.CYAN) {
								index = 0;
								potez[index] = null;
								polje.changeColor(Color.WHITE);
								gradnja = true;
								pomeraj = false;
								currentPlayer = (currentPlayer == P1) ? P2 : P1;
								figura = null;
								pripremiPotez();
								return;
							}
							
							potez[index] = polje;
							index++;
							
							move(currentPlayer, figura, potez[0].getRow(), potez[0].getCol(), potez[1].getRow(), potez[1].getCol());
							
						}else if (index == 2) {
							potez[index] = polje;
							index = 0;
							
							build(currentPlayer, figura, potez[1].getRow(), potez[1].getCol(), potez[2].getRow(), potez[2].getCol());
							
							log.append(Log.parseRow(potez[0].getRow())).append(Log.parseCol(potez[0].getCol())).append(' ');
							log.append(Log.parseRow(potez[1].getRow())).append(Log.parseCol(potez[1].getCol())).append(' ');
							log.append(Log.parseRow(potez[2].getRow())).append(Log.parseCol(potez[2].getCol()));
							log.append('\n');
							
							for(int i = 0; i < 3; i++) potez[i] = null;
						}else 
							return;
						pripremiPotez();
					}

					@Override
					public void mouseReleased(MouseEvent arg0) {
					}
					
				};
				polje.addMouseListener(mListener);
				polje.repaint();
			}
		p2.add(tmp, BorderLayout.CENTER);
		
		tmp = new Panel(new GridLayout(1, 5));
		Panel tmp2 = new Panel(new GridLayout(1, 5));
		
		tmp.add(new Label("1", Label.CENTER)); tmp2.add(new Label("1", Label.CENTER));  
		tmp.add(new Label("2", Label.CENTER)); tmp2.add(new Label("2", Label.CENTER));  
		tmp.add(new Label("3", Label.CENTER)); tmp2.add(new Label("3", Label.CENTER));  
		tmp.add(new Label("4", Label.CENTER)); tmp2.add(new Label("4", Label.CENTER));  
		tmp.add(new Label("5", Label.CENTER)); tmp2.add(new Label("5", Label.CENTER));
		p2.add(tmp, BorderLayout.NORTH);
		p2.add(tmp2, BorderLayout.SOUTH);
		
		tmp = new Panel(new GridLayout(5, 1));
		tmp2 = new Panel(new GridLayout(5, 1));
		
		tmp.add(new Label("A", Label.CENTER)); tmp2.add(new Label("A", Label.CENTER));  
		tmp.add(new Label("B", Label.CENTER)); tmp2.add(new Label("B", Label.CENTER));  
		tmp.add(new Label("C", Label.CENTER)); tmp2.add(new Label("C", Label.CENTER));  
		tmp.add(new Label("D", Label.CENTER)); tmp2.add(new Label("D", Label.CENTER));  
		tmp.add(new Label("E", Label.CENTER)); tmp2.add(new Label("E", Label.CENTER));
		
		p2.add(tmp, BorderLayout.EAST);
		p2.add(tmp2, BorderLayout.WEST);
		
		addWindowListener(new WindowAdapter() {
			 public void windowClosing(WindowEvent we) {
				 log.dispose();
				 dispose();
			 }});
	}
	
	public Tabla(Igrac P1, Igrac P2, File pocetnoStanje, boolean gameOver, int errorCode, String poruka, Polje[][] tabla, Log log, Igrac currentPlayer) {
		this.P1 = P1;
		this.P2 = P2;
		this.tabla = tabla;
		this.log = log;
		
		this.currentPlayer = (currentPlayer == P2) ? P1 : P2;
		naPotezu = new Label("==>", Label.CENTER);
		potez = new Polje[3];
		init();
		/*
		P1.getFigure().forEach((f) -> {
			int i = f.getRow()-'A', j = f.getCol();
			tabla[i][j].setF(f);
			});
		P2.getFigure().forEach((f) -> {
			int i = f.getRow()-'A', j = f.getCol()
					;
			tabla[i][j].setF(f);
			});
		*/
		pripremiPotez(); 
	}
	
	public Tabla(Igrac P1, Igrac P2, File pocetnoStanje, boolean gameOver, int errorCode, String poruka) {
		for(int i = 0; i < HEIGHT; i++)
			for(int j = 0; j < WIDTH; j++)
				tabla[i][j] = new Polje(i, j);
	
		this.P1 = P1;
		this.P2 = P2;
		
		this.currentPlayer = P2;
		naPotezu = new Label("==>", Label.CENTER);
		potez = new Polje[3];
		init();
		
		P1.getFigure().forEach((f) -> {
			int i = f.getRow(), j = f.getCol();
			tabla[i][j].setF(f);
			});
		P2.getFigure().forEach((f) -> {
			int i = f.getRow(), j = f.getCol()
					;
			tabla[i][j].setF(f);
			});
		
		pripremiPotez();
		
	}
	
	public int getID() {
		return ID;
	}

	public Polje[][] getTabla() {
		return tabla;
	}

	public void setTabla(Polje[][] tabla) {
		this.tabla = tabla;
	}

	public Igrac getP1() {
		return P1;
	}

	public void setP1(Igrac p1) {
		P1 = p1;
	}

	public Igrac getP2() {
		return P2;
	}

	public void setP2(Igrac p2) {
		P2 = p2;
	}
	
	public boolean isValidMove(Igrac p, Figura f, int y1, int x1, int y2, int x2) {
		if (tabla[y2][x2].getZ().getNivo() - tabla[y1][x1].getZ().getNivo() > 1) return false;
		
		return true;		
	}
	
	public boolean move(Igrac p, Figura f, int y1, int x1, int y2, int x2) {
//		if (!isValidMove(p, f, S1, S2, D1, D2)) return false;
		
		tabla[y1][x1].setF(null);
		tabla[y2][x2].setF(f);
		
		f.setCol(x2);
		f.setRow(y2);
		
		if (tabla[y2][x2].getZ().getNivo() == 3) gameOver = true;
		
		return true;
	}

	public boolean isValid(Igrac p, Figura f, int y1, int x1, int y2, int x2){
		if (!p.has(f)) return false;
		
		Polje S = tabla[y1][x1];
		
		if (S.getF() == null) return false;
		if (S.getF().getP() != p) return false;
		
		Polje D = tabla[y2][x2];
		
		if (D.getF() != null) return false;
		if (D.getZ() != null && D.getZ().isKupola()) return false;
		return true;
	}
	
	public boolean isValidBuild(Igrac p, Figura f, int y1, int x1, int y2, int x2) {
		if (Math.abs(x1-x2) > 1 || Math.abs(y1-y2) > 1) return false;
		
		return true;
	}
	
	public boolean build(Igrac p, Figura f, int y1, int x1, int y2, int x2) {
//		if (!isValidBuild(p, f, S1, S2, D1, D2)) return false;
		
		Polje D = tabla[y2][x2];
		D.getZ().incNivo();
		
		return true;
	}
	
	public void pripremiPotez() {
		if (izbor) {
			izbor = false;		//index == 1
			pomeraj = true;
		}else if (pomeraj) {
			pomeraj = false; 	// index == 2
			gradnja = true;
			
			if (gameOver) {
				gameOver();
				return;
			}
			
		}else if (gradnja) {
			gradnja = false;	//index == 0
			izbor = true;
			if (currentPlayer == P1) {
				currentPlayer = P2;
				naPotezu.setText("==>");
			}else {
				currentPlayer = P1;
				naPotezu.setText("<==");
			}
			
		}
		
		Color boja = (izbor || pomeraj) ? Color.GREEN : blue;		
		
		for(int i = 0; i < HEIGHT; i++)
			for(int j = 0; j < WIDTH; j++) {
				Polje polje = tabla[i][j];
				polje.changeColor(null);
				polje.getPnl().setEnabled(false);
			}
		brojPoteza = 0;
		int brojPotezaF[] = new int[2];
		int trenutnaFigura = 0;
		for (Figura t: currentPlayer.getFigure()){
			if (izbor || t == figura){
				int x = t.getCol();
				int y = t.getRow();
					
				for(int i = y-1; i <= y+1; i++)
					for(int j = x-1; j <= x+1; j++) {
						if (x == j && y == i) continue;
						if (i < 0 || i > 4 || j < 0 || j > 4) continue;
						Polje p = tabla[i][j];

						if (!isValid(currentPlayer, t, y, x, i ,j)) continue;
						if ((izbor || pomeraj) && !isValidMove(currentPlayer, t, y, x, i, j)) continue;
						if (gradnja && !isValidBuild(currentPlayer, t, y, x, i, j)) continue;		
										
						//if (p.getF() != null || (p.getZ() != null && p.getZ().isKupola())) continue;
						//if ((pomeraj || izbor) && Math.abs(p.getZ().getNivo()-tabla[t.getRow()-'A'][t.getCol()].getZ().getNivo()) > 1) continue;
						
						p.changeColor(boja);
						brojPotezaF[trenutnaFigura]++;
						if (pomeraj || gradnja) p.getPnl().setEnabled(true);
					}
				if (brojPotezaF[trenutnaFigura] != 0) {
					tabla[y][x].changeColor((!izbor) ? Color.CYAN : Color.WHITE);
					tabla[y][x].getPnl().setEnabled(!gradnja ? true : false);
				}else {
					tabla[y][x].changeColor(null);
					tabla[y][x].getPnl().setEnabled(false);
				}
				brojPoteza += brojPotezaF[trenutnaFigura++];
				
			}
		}
		
		if (brojPoteza == 0) gameOver();
		
	}
	
	public void gameOver() {
		for(int i = 0; i < HEIGHT; i++)
			for(int j = 0; j < WIDTH; j++) {
				tabla[i][j].getPnl().setEnabled(false);
				tabla[i][j].changeColor(null);
			}
		StringBuilder poruka = new StringBuilder("Pobednik je ");

		if (brojPoteza > 0) poruka.append(currentPlayer.getIme());
		else poruka.append((currentPlayer == P1) ? P2.getIme() : P1.getIme());
		log.append('\n').append(poruka).append('\n');
		
		Date datum = new Date();
		Calendar kalendar = new GregorianCalendar();
		kalendar.setTime(datum);
		String datumString = "" + kalendar.get(Calendar.YEAR) + '.' + kalendar.get(Calendar.MONTH) + '.' + kalendar.get(Calendar.DAY_OF_MONTH) +
				 ". - " + kalendar.get(Calendar.HOUR_OF_DAY) + '_' + kalendar.get(Calendar.MINUTE) + '_' + kalendar.get(Calendar.SECOND);
		String izlazniString = "..\\Santorini\\" + P1.getIme() + " vs " + P2.getIme() + " - " + datumString + ".txt";
		
		final Dialog greska = new Dialog(this);
		boolean greskaLog = false;
		File izlaznaDatoteka = new File(izlazniString);
		try {
			FileWriter fw = new FileWriter(izlaznaDatoteka);
			BufferedWriter bw = new BufferedWriter(fw);
			
			bw.write(log.getLog().toString());
			bw.flush();
			
			
			fw.close();
			
		} catch (IOException e1) {
			e1.printStackTrace();
			greskaLog = true;
			int gHeight = 100, gWidth = 200;
			greska.setLayout(new GridLayout(2, 1));
			greska.setBounds(this.getX() + (this.getWidth()-gWidth)/2, this.getY() + (this.getHeight()-gHeight)/2-50, gWidth, gHeight);
			greska.add(new Label("Doslo je do greske pri cuvanju istorije.", Label.CENTER));
			
			Button dugme = new Button("Izadji");
			dugme.addActionListener(t ->{
				greska.dispose();
			});
			greska.add(dugme);
			greska.addWindowListener(new WindowAdapter() { 
				 public void windowClosing(WindowEvent we) {
					 greska.dispose();			 
				 }});
		}
		
		Dialog dialog = new Dialog(this);
		dialog.setAlwaysOnTop(true);
		int dHeight = 200, dWidth = 400;
		dialog.setBounds(this.getX() + (this.getWidth()-dWidth)/2, this.getY() + (this.getHeight()-dHeight)/2-50, dWidth, dHeight);
		dialog.setLayout(new GridLayout(2, 1));
		dialog.add(new Label(poruka.toString(), Label.CENTER));
		
		Button novaIgra = new Button("Nova igra");
		novaIgra.addActionListener(e -> {
			log.dispose();
			dispose();
			invalidate();
			new Meni();
		});
		
		Button izadji = new Button("Izadji");
		izadji.setSize(50, 100);
		izadji.addActionListener(e -> {
			log.dispose();
			dialog.dispose();
			dispose();
			invalidate();
		});
		
		Button pogledajTablu = new Button("Pogledaj tablu");
		pogledajTablu.addActionListener(e -> {
			dialog.dispose();
		});
		
		dialog.add(novaIgra);
		dialog.add(pogledajTablu);
		dialog.add(izadji);
		
		dialog.addWindowListener(new WindowAdapter() {
			 public void windowClosing(WindowEvent we) {
				 log.dispose();
				 dialog.dispose();
				 dispose();
				 invalidate();
				 new Meni();
			 }});
		dialog.setVisible(true);
		if (greskaLog) greska.setVisible(true);
	}
	
	
}
