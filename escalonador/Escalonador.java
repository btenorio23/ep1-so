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

	public Escalonador(Integer[] processos, int q) {
		for(Integer i : processos) {
			prontos.add(i.toString());
		}
		setQuantum(q);
		taBloqueado = new int[processos.length];
		System.out.println("Qual o primeiro a sair da fila? "+ prontos.getFirst());
		setProcessoVez(0);
	}
	
	public void setTamanhoTabela(int numero) {
		tabelaProcessos.iniciaTabela(numero);
	}
	
	private void setQuantum(int q) {
		this.quantum = q;
	}
	
	public int getQuantum() {
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
		tabelaProcessos.removeTabela(processoVez);
		processoVez = Integer.parseInt(prontos.removeFirst());
	}
	
	//verifica status de um processo bloqueado 
	public boolean verificaProxBloqueado() {
		if(bloqueados.isEmpty())
			return false;
		int status = taBloqueado[Integer.parseInt(bloqueados.getFirst())];
		if(status == 1) {
			//reseta ele
			taBloqueado[Integer.parseInt(bloqueados.getFirst())] = 0;
			prontos.addLast(bloqueados.removeFirst());
		}
		if(status > 1) {
			taBloqueado[Integer.parseInt(bloqueados.getFirst())]--;
		}
		return true;
	}
	
	//verifica status de todos processos bloqueados
	public boolean verificaBloqueado() {
		for(int i: taBloqueado) {
			//esse processo poderá rodar na proxima interação
			if(i == 1) {
				taBloqueado[i] = 0;
				prontos.add(bloqueados.remove(i));
				processoVez = Integer.parseInt(prontos.getFirst());
				return true;
			}
			else {
				if(i > 1) {
					taBloqueado[i]--;
				}
			}
		}
		return false;
	}
	
	
	
	//Método realiza o algoritmo round-robin, recebe como argumento quanto tempo ainda tem 
	public int devolveProcesso(int nQuantum) {
		
		//Significa que já rodou todo seu tempo
		if(nQuantum == (getQuantum()+1)) {
			prontos.addLast(processoVez + "");
			
			if(prontos.isEmpty()) {
				if(verificaBloqueado()) {
					
				}
				else {
					System.out.println("Não existe mais programas!");
					return -1;
				}
			}
			else {
				processoVez = Integer.parseInt(prontos.removeFirst());
			}
			verificaProxBloqueado();
			return processoVez;
		}
		
		//Significa que ainda pode rodar mais
		else {
			verificaProxBloqueado();
			return processoVez;
		}
		
	}
	
	public void salvaContexto(String[] arg) {
		System.out.println("***** Contexto *****");
		for(String teste: (String[])arg) {
			System.out.print("contx: " + teste + " -> ");
		}
		tabelaProcessos.setContexto(arg, processoVez);
	}
	
	public String[] devolveContexto() {
		return tabelaProcessos.getContexto(processoVez);
	}

	//Salva o contexto
	//Método é chamado quando a CPU executa um IO ou troca de processo
	@Override
	public void update(Observable o, Object arg) {
		try {
			Thread.sleep(2000);
			System.out.println("***** Contexto *****");
			for(String teste: (String[])arg) {
				System.out.print("contx: " + teste + " -> ");
			}
			tabelaProcessos.setContexto((String[])arg, processoVez);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Ocorreu um I/O!");
		bloqueados.addLast(processoVez + "");
		processoVez = Integer.parseInt(prontos.removeFirst());
	}

}
