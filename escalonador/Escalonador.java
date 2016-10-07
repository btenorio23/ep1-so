package escalonador;

import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

//Classe responsável por escalonar programas da maquina
public class Escalonador extends Observable implements Observer{

	//Tamanho do quantum será armazenado aqui
	private int quantum;
	
	//Não sei o quanto isso é necessário
	//private int processoVez;	
	
	BCP tabelaProcessos;

	public Escalonador(Integer[] processos, int q) {
		tabelaProcessos = new BCP(processos.length);
		setQuantum(q);
		//setProcessoVez(0);
	}
	
	public void encerraPrograma(int i) {
		tabelaProcessos.removeProcesso();
	}
	
	private void setQuantum(int q) {
		this.quantum = q;
	}

	public int getQuantum() {
		return quantum;
	}
	
	/*public int getProcessoVez() {
		return this.processoVez;
	}*/
	
	/*public void setProcessoVez(int i) {
		this.processoVez = i;
	}*/
	
	//verifica status de todos processos bloqueados
	/*public boolean verificaBloqueado() {
		boolean achouProc = false;;
		for(int i: taBloqueado) {
			if(i == 1) {
				taBloqueado[i] = 0;
				prontos.add(bloqueados.remove(i));
				if(!achouProc) {
					processoVez = Integer.parseInt(prontos.getFirst());
					achouProc = true;
				}
			}
			else {
				if(i > 1) {
					taBloqueado[i]--;
				}
			}
		}
		return achouProc;
	}*/
	
	public void verificaBloqueados() {
		tabelaProcessos.verificaBloqueados();
	}	
	
	//Método realiza o algoritmo round-robin, recebe como argumento quanto tempo ainda tem 
	public int devolveProcesso(int nQuantum) {
		
		//Significa que já rodou todo seu tempo, precisamos salvar contexto
		if(nQuantum == (getQuantum()+1)) {
			System.out.println("Acabou quantum do processo, trocando...");
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
				System.out.println("Processo atual: "+processoVez);
				this.setChanged();
				this.notifyObservers(tabelaProcessos.getContexto(processoVez));
				processoVez = Integer.parseInt(prontos.removeFirst());
				System.out.println("Proximo processo: "+processoVez);
				verificaBloqueado();
			}
			return processoVez;
		}
		
		//Significa que ainda pode rodar mais
		else {
			verificaBloqueado();
			return processoVez;
		}
		
	}
	
	public void salvaContexto(String[] arg) {
		tabelaProcessos.setContexto((String[])arg, processoVez);
	}
	
	public String[] devolveContexto() {
		return tabelaProcessos.getContexto(processoVez);
	}

	//Salva o contexto
	//Método é chamado quando a CPU executa um IO ou troca de processo
	@Override
	public void update(Observable o, Object arg) {
		System.out.println("->->Salvando contexto maquina<-<-");
		
		//adiciona no bcp o contexto
		tabelaProcessos.setContexto((String[])arg, processoVez);
		//adiciona no final da lista de bloqueados
		bloqueados.addLast(processoVez + "");
		//adiciona 2 tempos para processo ficar bloqueado
		taBloqueado[processoVez] = 2;
		
		System.out.println("PC: "+ ((String[])arg)[2]);
		
		System.out.println("PROCESSO: "+ processoVez);
		System.out.println("CONTEXTO:");
		for(String s: tabelaProcessos.getContexto(processoVez)) {
			System.out.print(s + " ");
		}
		System.out.print("\n");
		
		//como irá pro "else" do devolveProcesso, atualizamos o 
		//processo da vez aqui
		if(prontos.isEmpty()) {
			if(verificaBloqueado()) {
				
			}
			else {
				System.out.println("Não existe mais programas!");
				System.exit(0);
			}
		}
		else {
			processoVez = Integer.parseInt(prontos.removeFirst());
		}
		
		this.setChanged();
		this.notifyObservers();
	}

}
