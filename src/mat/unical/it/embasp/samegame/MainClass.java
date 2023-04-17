package mat.unical.it.embasp.samegame;

import java.awt.Color;
import java.util.Random;
import javax.swing.*;
import it.unical.mat.embasp.base.Handler;
import it.unical.mat.embasp.base.InputProgram;
import it.unical.mat.embasp.base.Output;
import it.unical.mat.embasp.languages.IllegalAnnotationException;
import it.unical.mat.embasp.languages.ObjectNotValidException;
import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import it.unical.mat.embasp.languages.asp.ASPMapper;
import it.unical.mat.embasp.languages.asp.AnswerSet;
import it.unical.mat.embasp.languages.asp.AnswerSets;
import it.unical.mat.embasp.platforms.desktop.DesktopHandler;
import it.unical.mat.embasp.specializations.dlv2.desktop.DLV2DesktopService;

public class MainClass {

	private MainClass() {}

	private static boolean finished = false;
	private static JFrame frame;
	private static int punteggio = 0;
	private static JTextField[][] matrix;
	
	private static char[][] samegame = new char[10][20];
	
	private static String encodingResource = "encodings/samegame.dl";
	
	private static Handler handler;
	
	public static void main(String[] args) {
		
		Random r = new Random();
		
		handler = new DesktopHandler(new DLV2DesktopService("lib/dlv2"));
		
		try {
			ASPMapper.getInstance().registerClass(Cell.class);
			ASPMapper.getInstance().registerClass(Rimuovi.class);
		} catch (ObjectNotValidException | IllegalAnnotationException e) {
			e.printStackTrace();
		}

		
		for (int i = 0; i < samegame.length; ++i)
			for (int j = 0; j < samegame[0].length; ++j) {
				int colore =  r.nextInt(0, 4);
				switch (colore) {
				case 0: samegame[i][j] = 'b'; break;
				case 1: samegame[i][j] = 'g'; break;
				case 2: samegame[i][j] = 'r'; break;
				case 3: samegame[i][j] = 'v'; break;
				}
			}
		
		
		InputProgram variableProgram = new ASPInputProgram();
		
		for (int i = 0; i < samegame.length; ++i) 
			for (int j = 0; j < samegame[0].length; ++j) {
				try {
					if (samegame[i][j] != '0') variableProgram.addObjectInput(new Cell(i, j, samegame[i][j]));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		
		handler.addProgram(variableProgram);
		
		InputProgram encoding = new ASPInputProgram();
		encoding.addFilesPath(encodingResource);
		
		handler.addProgram(encoding);
		
		
		while (true) {
			Output o = handler.startSync();
			AnswerSets answerSets = (AnswerSets) o;
			if (answerSets.getAnswersets().isEmpty()) { 
				finished = true;
				stampaTabella(samegame);
				break;
			}
			stampaTabella(samegame);
			int cont = 0;
			AnswerSet a = answerSets.getAnswersets().get(0);
			try {
				for (Object obj : a.getAtoms()) {
					if (!(obj instanceof Rimuovi)) continue;
					++cont;
					Rimuovi cell = (Rimuovi) obj;
					samegame[cell.getRiga()][cell.getColonna()] = '0';
				}
				punteggio += (int)Math.pow(cont-1, 2);
			} catch (Exception e) {
				e.printStackTrace();
			}
			cambiaMatrice(samegame);
			variableProgram.clearAll();
			for (int i = 0; i < samegame.length; ++i) 
				for (int j = 0; j < samegame[0].length; ++j) {
					try {
						if (samegame[i][j] != '0') variableProgram.addObjectInput(new Cell(i, j, samegame[i][j]));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	} // main
	
	private static void cambiaMatrice(char[][] tabella) {
		for (int i = 0; i < tabella.length; ++i) {
			for (int j = 0; j < tabella[0].length; ++j) {
				if (tabella[i][j] == '0') aggiornaColonna(samegame, j);
				if (colonnaVuota(tabella, j))
					for (int col = j; col < tabella[0].length-1; ++col)
						copiaColonna(samegame, col, col+1);
			}
		}
	} // cambiaMatrice
	
	private static boolean colonnaVuota(char[][] tabella, int j) {
		for (int i = 0; i < tabella.length; ++i)
			if (tabella[i][j] != '0') return false;
		return true;
	} // colonnaVuota
	
	private static void copiaColonna(char[][] tabella, int k, int j) {
		char[] tmp = new char[tabella.length];
		for (int i = 0; i < tabella.length; ++i) { tmp[i] = tabella[i][j]; tabella[i][j] = '0'; }
		for (int i = 0; i < tmp.length; ++i) tabella[i][k] = tmp[i]; 
	} // copiaColonna

	private static void aggiornaColonna(char[][] tabella, int j) {
		char[] tmp = new char[tabella.length];
		int c = 0;
		for (int i = 0; i < tabella.length; ++i)
			if (tabella[i][j] != '0') tmp[c++] = tabella[i][j];
		for (; c < tmp.length; ++c) tmp[c] = '0';
		for (int i = 0; i < tabella.length; ++i) tabella[i][j] = tmp[i];
	}
	
	private static void stampaTabella(char[][] tab) {
		frame = new JFrame("Samegame");
		frame.getContentPane().setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println(e);
			System.exit(0);
		}
		frame.setVisible(true);
		frame.setBounds(240, 100, 700, 360);
		matrix = new JTextField[tab.length][tab[0].length];
		for (int i = 0; i < matrix.length; ++i) {
			for (int j = 0; j < matrix[0].length; ++j) {
				if (tab[i][j] != '0') {
					JTextField text = new JTextField("");
					frame.getContentPane().add(text);
					text.setBounds(50+30*j, 60+20*i, 30, 20);
					text.setHorizontalAlignment(JTextField.CENTER);
					text.setEditable(false);
					switch(tab[i][j]) {
					case 'g': text.setBackground(Color.YELLOW); break;
					case 'r': text.setBackground(Color.RED); break;
					case 'b': text.setBackground(Color.BLUE); break;
					case 'v': text.setBackground(Color.GREEN); break;
					}
				}
			}
		}
		if (finished) {
			JFrame subFrame = new JFrame("Punteggio");
			subFrame.getContentPane().setLayout(null);
			subFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				System.out.println(e);
				System.exit(0);
			}
			subFrame.setVisible(true);
			subFrame.setBounds(240, 475, 500, 200);
			JTextArea message = new JTextArea("Il punteggio finale è: "+punteggio+". Complimenti!");
			message.setBounds(120, 75, 275, 20);
			message.setEditable(false);
			subFrame.getContentPane().add(message);
		}
	} // stampaTabella

} // MainClass
