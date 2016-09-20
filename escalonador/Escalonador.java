package escalonador;

import java.util.LinkedList;

//Classe responsável por escalonar programas da maquina
public class Escalonador {

	//Afinal, escalonador precisa saber o PC do processo? ou somente a referencia a ele?
	
	LinkedList<String> prontos = new LinkedList<String>();
	LinkedList<String> bloqueados = new LinkedList<String>();
	LinkedList<String> executando = new LinkedList<String>();

	public Escalonador(Integer[] processos) {
		for(Integer i : processos) {
			prontos.add(i.toString());
		}
		System.out.println("Processos prontos: " + prontos);
	}
	
	public void getProcessos() {
		for(String processoExecutando : executando) {
			String[] processoStringE = {processoExecutando, "EXECUTANDO"};
			TabelaProcessos.recebeData(processoStringE);
		}
		for(String processoPronto : prontos) {
			String[] processoStringP = {processoPronto, "PRONTO"};
			TabelaProcessos.recebeData(processoStringP);
		}
		for(String processoBloqueado : bloqueados) {
			String[] processoStringB = {processoBloqueado, "BLOQUEADO"};
			TabelaProcessos.recebeData(processoStringB);
		}		
	}
	
}
