package mat.unical.it.embasp.samegame;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("cell")
public class Cell {

	@Param(0)
	private int riga;
	
	@Param(1)
	private int colonna;
	
	@Param(2)
	private char colore;
	
	public Cell() {}

	public Cell(int riga, int colonna, char colore) {
		super();
		this.riga = riga;
		this.colonna = colonna;
		this.colore = colore;
	}
	
	public int getRiga() {
		return riga;
	}

	public void setRiga(int riga) {
		this.riga = riga;
	}

	public int getColonna() {
		return colonna;
	}

	public void setColonna(int colonna) {
		this.colonna = colonna;
	}

	public char getColore() {
		return colore;
	}

	public void setColore(char colore) {
		this.colore = colore;
	}
	
}
