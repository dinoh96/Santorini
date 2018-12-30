package etf.santorini.hd150197d;

import java.awt.Color;
import java.awt.Frame;

public class Igra extends Frame {
	private static int prevId = 0;
	private int ID = prevId++;
	
	private static int sirinaEkrana = 1366;//1920*3/4;
	
	private Tabla tabla;
	
	private Igrac Igrac1, Igrac2;
	
	public Igra(){
		super();
		
		/*Figura f11 = new Figura('A', 4);
		Figura f12 = new Figura('E', 0);
		Figura f21 = new Figura('E', 4);
		Figura f22 = new Figura('A', 0);
		
		Igrac1 = new Igrac(f11, f12, Color.RED);
		Igrac2 = new Igrac(f21, f22, Color.BLUE);

		tabla = new Tabla(Igrac1, Igrac2, null);
		*/

	}
	/*
	public static void main(String[] args) {
		new Igra();
	}*/

}
