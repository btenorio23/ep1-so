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
	
	public void removeTabela(int index) {
		tabelaProcessos.remove(index);
	}
	
	public String[] getContexto(int processoVez) {
		return tabelaProcessos.get(processoVez);
	}
	
	public void setContexto(String[] contexto, int processoVez) {
		System.out.println("Adicionando contexto em: "+ processoVez);
		System.out.println("CONTEXTO:");
		for(String s: contexto) {
			System.out.print(" "+ s);
		}
		System.out.print("\n");
		tabelaProcessos.add(processoVez, contexto);
	}
	
	
	
}
