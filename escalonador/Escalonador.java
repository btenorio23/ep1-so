package escalonador;

import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

//Classe responsável por escalonar programas da maquina
public class Escalonador implements Observer{

	//Tamanho do quantum será armazenado aqui
	private int quantum;
	private int processoVez;
	
	private int[] taBloqueado;
	
	BCP tabelaProcessos = new BCP();
	
	//Lista de processos prontos e bloqueados
	LinkedList<String> prontos = new LinkedList<String>();
	LinkedList<String> bloqueados = new LinkedList<String>();

	private void setQuantum(int q) {
		this.quantum = q;
	}
	
	private int getQuantum() {
		return quantum;
	}
	
	public int getProcessoVez() {
		return this.processoVez;
	}
	
	public void setProcessoVez(int i) {
		this.processoVez = i;
	}
	
	public void encerraPrograma(int i) {
		prontos.removeFirst();
		processoVez = Integer.parseInt(prontos.removeFirst());
	}
	
	public void verificaBloq() {
		//verificamos por todos que foram bloqueados
		for(int i : taBloqueado) {
			//caso ele tenha 0, significa que não foi bloqueado
			if(i == 0) {
				//verificamos se ja saiu da lista de bloqueados:
				for(String s: bloqueados) {
					//Significa que ainda esta bloqueado, então desbloqueamos
					if (Integer.parseInt(s) == i) {
						prontos.addLast(bloqueados.remove(i));
					}
					else {
						continue;
					}
				}
			}
			//significa que ja foi bloqueado, mas ainda deve ficar como bloqueado
			else {
				i--;
			}
		}
	}
	
	public Escalonador(Integer[] processos, int q) {
		for(Integer i : processos) {
			prontos.add(i.toString());
		}
		setQuantum(q);
		taBloqueado = new int[processos.length];
		System.out.println("Qual o primeiro a sair da fila? "+ prontos.getFirst());
		setProcessoVez(0);
	}
	
	//Método realiza o algoritmo round-robin, recebe como argumento quanto tempo ainda tem 
	public int devolveProcesso(int nQuantum) {
		
		//Significa que já rodou todo seu tempo
		if(nQuantum == getQuantum()) {
			prontos.addLast(processoVez + "");
			processoVez = Integer.parseInt(prontos.removeFirst());
			verificaBloq();
			return processoVez;
		}
		//Significa que ainda pode rodar mais
		else {
			verificaBloq();
			return processoVez;
		}
		
	}

	//Método é chamado quando a CPU executa um IO.
	@Override
	public void update(Observable o, Object arg) {
		System.out.println("Ocorreu um I/O!");
		bloqueados.addLast(processoVez + "");
		processoVez = Integer.parseInt(prontos.removeFirst());
	}

}
