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
	
	//de acordo com o quantum da vez, retorna o processo que deve ser executado.
	public int devolveProcesso(int nQuantum) {
		if(executando.isEmpty()) {
			executando.add(prontos.removeFirst());
			//precisamos tirar o programa bloqueado e passa-lo para pronto
			prontos.addLast(bloqueados.removeFirst());
			return Integer.parseInt(executando.getFirst());
		}
		else {
			//Precisamos verificar se esse é o 4° quantum. Ou seja, acabou a vez do processo rodar
			if(nQuantum > 3) {
				//Colocamos o processo executando atual para bloqueado
				bloqueados.addLast(executando.removeFirst());
				//Adicionamos o proximo processo pronto como executando
				executando.addFirst(prontos.removeFirst());
				prontos.addLast(bloqueados.removeFirst());
				return Integer.parseInt(executando.getFirst());
			}
			prontos.addLast(bloqueados.removeFirst());
			return Integer.parseInt(executando.getFirst());
		}		
	}

	//TODO
	//Método é chamado quando a CPU executa um IO.
	@Override
	public void update(Observable o, Object arg) {
		//Adiciona processo a lista de bloqueados
		bloqueados.addLast(executando.removeFirst());
		
		System.out.println("Argumento passado por" + o + " é: " + arg);
		
		//Pega primeiro processo do prontos e o executa.
		//devolveProcesso(prontos.removeFirst());	
	}

}
