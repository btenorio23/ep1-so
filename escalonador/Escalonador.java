package escalonador;

import java.util.Observable;
import java.util.Observer;

//Classe responsável por escalonar programas da maquina
public class Escalonador extends Observable implements Observer{

	//Tamanho do quantum será armazenado aqui
	private int quantum;
	
	//Não sei o quanto isso é necessário
	//private int processoVez;	
	
	//Escalonador guarda referencia a tabela de processos que tem todos os bcps
	TabelaProcessos tabelaProcessos;

	public Escalonador(Integer[] processos, int q) {
		//inicia a tabela com o numero de processos certos
		tabelaProcessos = new TabelaProcessos(processos.length);
		//setamos o quantum que o escalonador irá usar
		setQuantum(q);
		//setProcessoVez(0);
	}
	
	public int getProcesso() {
		return tabelaProcessos.getNumeroProcesso();
	}
	
	//Remove totalmente o programa da tabela de processos
	public void encerraPrograma() {
		tabelaProcessos.removeProcesso();
	}
	
	//seta o quantum do escalonador
	private void setQuantum(int q) {
		this.quantum = q;
	}

	//recupera o quantum do escalonador
	public int getQuantum() {
		return quantum;
	}
	
	//verifica todos os processos que estão bloqueado e ve quais podem voltar para "prontos" e quais não
	public void verificaBloqueados() {
		tabelaProcessos.verificaBloqueados();
	}	
	
	//Método realiza o algoritmo round-robin, recebe como argumento quanto tempo ainda tem.
	public void devolveProcesso(int nQuantum) {
		//nQuantum é quantos quantums um processo já rodou na CPU
		
		//Significa que já rodou todo seu tempo, precisamos salvar contexto
		if(nQuantum == (getQuantum()+1)) {
			System.out.println("Acabou quantum do processo, trocando...");
			
			//Notifica a CPU de que será trocado o contexto, logo, CPU salva contexto antigo através do método update
			this.setChanged();
			this.notifyObservers(tabelaProcessos.getNumeroProcesso());
			
		}
		//Caso o nQuantum seja menor, o processo ainda pode rodar outras vezes
	}
	
	//adiciona/atualiza o contexto do argumento na tabela de processos
	public void salvaContexto(Integer[] arg) {
		tabelaProcessos.adicionaFinalPronto(arg);
	}
	
	//retorna o processo do programa que estará executando
	public Integer[] devolveContexto() {
		return tabelaProcessos.retornaPronto();
	}

	//Salva o contexto
	//Método é chamado quando a CPU executa um IO
	@Override
	public void update(Observable o, Object arg) {
		System.out.println("->->Salvando contexto maquina<-<-");
		
		//adiciona no bcp o contexto
		tabelaProcessos.bloqueia((Integer[])arg);
		//quando executamos o método bloqueia, ele ja atualiza a tabela para selecionar qual o proximo processo que rodará
		System.out.println("PC: "+ ((Integer[])arg)[2]);
		
		System.out.println("CONTEXTO:");
		for(Integer x: (Integer[])arg) {
			System.out.print(x + " ");
		}
		System.out.print("\n");
		
		//talvez não precise disso
		//this.setChanged();
		//this.notifyObservers();
	}

}
