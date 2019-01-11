package etf.santorini.hd150197d;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Choice;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.Scanner;

public class Meni extends Frame{
	private static int sirinaEkrana = 1366;//1920*3/4; 
	
	private TextField imeP1, imeP2;
	
	private Color colorP1 = Color.RED, colorP2 = Color.BLUE;
	
	private Choice dubina[] = new Choice[2];
	private Choice tezina[] = new Choice[2];
	
	private Checkbox modIgre[] = new Checkbox[3];
	
	private Color[] boje = {Color.RED, Color.BLUE, Color.YELLOW, Color.PINK, Color.GREEN, Color.ORANGE};
	private String[] nazivBoja = {"Crvena", "Plava", "Zuta", "Roze", "Zelena", "Narandzasta"};
	
	private File pocetnoStanje;
	private FileDialog fd;
	
	public Meni() {
		int width = 600;
		int height = 670;
		
		int hgap = 10, vgap = 5;
		
		Button play = new Button("Zapocni igru");
		play.setSize(50, 20);
		play.setEnabled(false);
		
		setBounds((sirinaEkrana-600)/2, 0, 600, 703);
		setResizable(false);
		
		setBackground(new Color(186, 191, 198));
		
		Panel pnl1 = new Panel();
		Panel pnl2 = new Panel();
		Panel pnl3 = new Panel();
		Panel pnl4 = new Panel();
		Panel pnl5 = new Panel();
		
		int x = 0;
		int y = 26;
		
		add(pnl4); add(pnl2); add(pnl3); add(pnl1); add(pnl5);
		
		int pnl1Height = height/10;
		pnl1.setBounds(x, y, width, pnl1Height);
		y += pnl1Height;
		
		Panel tmp = new Panel();
		
		tmp.add(new Label("Izaberite igrace:", Label.LEFT));
		
		CheckboxGroup mod = new CheckboxGroup();
		modIgre[0] = new Checkbox("Igrac - Igrac", true, mod);
		modIgre[1] = new Checkbox("Igrac - Racunar", false, mod);
		modIgre[2] = new Checkbox("Racunar - Racunar", false, mod);
		
		for(Checkbox t: modIgre) {
			tmp.add(t);
			t.addItemListener(k -> enabling());
		}
		pnl1.add(tmp);
		
		int pnl2Height = height/6;
		pnl2.setBounds(x, y, width, pnl2Height);
		y += pnl2Height;
		
		tmp = new Panel(new GridLayout(3,1, hgap, vgap));
		tmp.add(new Label("Naziv igraca", Label.LEFT));
		imeP1 = new TextField("Igrac 1", 40); tmp.add(imeP1);
		imeP2 = new TextField("Igrac 2", 40); tmp.add(imeP2);
		imeP1.addTextListener(t -> {
			if (!imeP1.getText().equals("") && (!imeP2.getText().equals("")) && pocetnoStanje != null) 
				play.setEnabled(true);
		});
		pnl2.add(tmp);
		
		tmp = new Panel(new GridLayout(3, 1, hgap, vgap));
		tmp.add(new Label("Tezina", Label.LEFT));
		for(int i = 0; i < 2; i++) {
			tezina[i] = new Choice();
			tezina[i].add("Easy");
			tezina[i].add("Medium");
			tezina[i].add("Hard");
			tezina[i].setEnabled(false);
			tmp.add(tezina[i]);
		}
		pnl2.add(tmp);
		
		tmp = new Panel(new GridLayout(3,1, hgap, vgap));
		tmp.add(new Label("Boja igraca", Label.LEFT));
		
		Choice bojaP1 = new Choice();
		Choice bojaP2 = new Choice();
		
		for(int i = 0; i < nazivBoja.length; i++) {
			bojaP1.add(nazivBoja[i]);
			if (i != 0) bojaP2.add(nazivBoja[i]);
		}
		bojaP1.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				String boja = arg0.getItem().toString();
				bojaP2.removeAll();
				for(int i = 0; i < nazivBoja.length; i++)
					if (!nazivBoja[i].equals(boja)) bojaP2.add(nazivBoja[i]);
					else colorP1 = boje[i];
			}
			
		});
		bojaP2.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				String boja = arg0.getItem().toString();
				bojaP1.removeAll();
				for(int i = 0; i < nazivBoja.length; i++)
					if (!nazivBoja[i].equals(boja)) bojaP1.add(nazivBoja[i]);
					else colorP2 = boje[i];
			}
			
		});
		tmp.add(bojaP1);
		tmp.add(bojaP2);
		pnl2.add(tmp);
		
		tmp = new Panel(new GridLayout(3,1, hgap, vgap));
		tmp.add(new Label("Dubina", Label.LEFT));
		
		for(int i = 0; i < 2; i++) {
			dubina[i] = new Choice();
			dubina[i].setEnabled(false);
			for(int j = 2; j <= 10; j++) 
				dubina[i].add(Integer.toString(j));
			tmp.add(dubina[i]);
		}
		pnl2.add(tmp);

		int pnl3Height = height/10;
		pnl3.setBounds(x, y, width, pnl3Height);
		y += pnl3Height;
		
		Button browse = new Button("Izaberite pocetno stanje igre");
		browse.setSize(50, 20);
		pnl3.add(browse);
		TextField fileName = new TextField(70);
		fileName.setEditable(false);
		
		pnl3.add(browse); pnl3.add(fileName);
		
		fd = new FileDialog(this, "Izaberite pocetno stanje igre", FileDialog.LOAD);
		fd.setDirectory("..\\Santorini");
		fd.setFilenameFilter(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				if (name != null && name.endsWith(".txt")) {
					fileName.setText(fd.getDirectory() + "\\" + name);
					pocetnoStanje = new File(fd.getDirectory() + "\\" + name);		
					if (!imeP1.getText().equals("") && (!imeP2.getText().equals(""))) play.setEnabled(true);
					return true;
				}
				pocetnoStanje = null;
				fileName.setText("***GRESKA*** Pogresna datoteka, molimo izaberite tekstualnu datoteku.");
				return false;
			}
		});
		browse.addActionListener(t -> fd.setVisible(true));
		
		int pnl4Height = height/5;
		pnl4.setBounds(x, y, width, pnl4Height);
		pnl4.add(play);
		play.addActionListener(t -> play());
		
		
		
		addWindowListener(new WindowAdapter() {
			 public void windowClosing(WindowEvent we) {
			 dispose();
			 invalidate();
			 }});
		setVisible(true);
	}
	
	private void play() {
		Igrac P1;
		Igrac P2;
		String mod = "PvP";
		if(modIgre[0].getState()) {
			P1 = new Igrac();
			P2 = new Igrac();
		}else if(modIgre[1].getState()) {
			P1 = new Igrac();
			switch (tezina[1].getSelectedIndex()){
			case 1: P2 = new RacunarMedium(); break;
			case 2: P2 = new RacunarHard(); break;
			default: P2 = new RacunarEasy(); break;
			}
			mod = "PvC";
			P2.setOtherPlayer(P1);
			P2.setDubina(dubina[1].getSelectedIndex() + 2);
		}else if(modIgre[2].getState()) {
			switch (tezina[0].getSelectedIndex()){
			case 1: P1 = new RacunarMedium(); break;
			case 2: P1 = new RacunarHard(); break;
			default: P1 = new RacunarEasy(); break;
			}
			switch (tezina[1].getSelectedIndex()){
			case 1: P2 = new RacunarMedium(); break;
			case 2: P2 = new RacunarHard(); break;
			default: P2 = new RacunarEasy(); break;
			}
			P1.setOtherPlayer(P2);
			P1.setDubina(dubina[0].getSelectedIndex() + 2);
			P2.setOtherPlayer(P1);
			P2.setDubina(dubina[1].getSelectedIndex() + 2);
			mod = "CvC";
		}else {
			dispose();
			return;
		}
		P1.setIme(imeP1.getText());
		P2.setIme(imeP2.getText());
		P1.setBoja(colorP1);
		P2.setBoja(colorP2);
		
		new Log(pocetnoStanje, P1, P2, sirinaEkrana, mod);
				
		dispose();
	}
	
	private void enabling() {
		if (modIgre[0].getState()) {
			dubina[0].setEnabled(false);
			dubina[1].setEnabled(false);
			tezina[0].setEnabled(false);
			tezina[1].setEnabled(false);
		}else if (modIgre[1].getState()) {
			dubina[0].setEnabled(false);
			dubina[1].setEnabled(true);
			tezina[0].setEnabled(false);
			tezina[1].setEnabled(true);
		}else if (modIgre[2].getState()) {
			dubina[0].setEnabled(true);
			dubina[1].setEnabled(true);
			tezina[0].setEnabled(true);
			tezina[1].setEnabled(true);
		}
	}
	

	public static void main(String[] args) {
		new Meni();
	}

}
