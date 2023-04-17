package mat.unical.it.embasp.samegame;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("rimuovi")
public class Rimuovi {

	@Param(0)
	private int riga;
	
	@Param(1)
	private int colonna;
	
	@Param(2)
	private String colore;
	
	public Rimuovi() {}

	public Rimuovi(int riga, int colonna, String colore) {
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

	public String getColore() {
		return colore;
	}

	public void setColore(String colore) {
		this.colore = colore;
	}
	
	
	
}
