package escalonador;

import java.util.ArrayList;

public class BCP {

	ArrayList<String[]> tabelaProcessos = new ArrayList<String[]>();
	
	public BCP() {
		
	}
	
	public void adicionaTabela(int index, String[] args) {
		tabelaProcessos.add(index, args);
	}
	
}
