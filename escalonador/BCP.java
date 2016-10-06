package escalonador;

import java.util.ArrayList;

public class BCP {

	ArrayList<String[]> tabelaProcessos;
	
	public BCP() {
		
	}
	
	public void iniciaTabela(int numero) {
		tabelaProcessos = new ArrayList<String[]>();
		for(int i=0; i<numero; i++) {
			tabelaProcessos.add(null);
		}
	}
	
	//sepa seja um método duplicado, analisar dps
	public void adicionaTabela(int index, String[] args) {
		tabelaProcessos.add(index, args);
	}
	
	public void removeTabela(int index) {
		tabelaProcessos.remove(index);
	}
	
	public String[] getContexto(int processoVez) {
		return tabelaProcessos.get(processoVez);
	}
	
	public void setContexto(String[] contexto, int processoVez) {
		tabelaProcessos.add(processoVez, contexto);
	}
	
	
	
}
