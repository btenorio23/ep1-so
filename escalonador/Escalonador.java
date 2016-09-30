package escalonador;

import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

//Classe responsável por escalonar programas da maquina
public class Escalonador implements Observer{

	private int quantum;
	private int clock;
	
	//Afinal, escalonador precisa saber o PC do processo? ou somente a referencia a ele?
	LinkedList<String> prontos = new LinkedList<String>();
	LinkedList<String> bloqueados = new LinkedList<String>();
	LinkedList<String> executando = new LinkedList<String>();

	private void setQuantum(int q) {
		quantum = q;
	}
	
	public Escalonador(Integer[] processos, int q) {
		for(Integer i : processos) {
			prontos.add(i.toString());
		}
		setQuantum(q);	
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
	
	//método que executa o Round Robin
	private void proximoPrograma() {
		//enquanto o programa ainda puder rodar.
		while(!(clock >= quantum)) {
			clock++;
			
		}
	}
	
	//TODO
	/*************************************************************************
	public String[] devolveProcesso(String processo) {
		if(executando.isEmpty()) {
			
		}
		else {
			return prontos.removeFirst();
		}
	}
	*/

	//TODO
	//Método é chamado quando a CPU executa um IO.
	@Override
	public void update(Observable o, Object arg) {
		//Adiciona processo a lista de bloqueados
		bloqueados.addLast(executando.removeFirst());
		
		//Pega primeiro processo do prontos e o executa.
		//devolveProcesso(prontos.removeFirst());	
	}

}
