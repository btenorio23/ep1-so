package escalonador;

import java.util.ArrayList;

public class BCP {

	ArrayList<Integer[]> tabelaProcessos;
	
	//Inicia a tabela de processo com o numero de processo que a CPU receberá
	public BCP(int length) {
		tabelaProcessos = new ArrayList<Integer[]>();
		for(int i=0; i<length; i++) {
			Integer[] contexto = new Integer[5];
			//todos os processos iniciam com pronto
			contexto[0] = 0;
			//todo os processos tem nBloqueio = 0
			contexto[1] = 0;
			tabelaProcessos.add(contexto);
		}
	}
	
	//busca na tabela o proximo processo pronto
	public Integer[] retornaPronto() {
		//o proximo processo pronto sempre será o primeiro na tabela
		return tabelaProcessos.get(0);
	}
	
	//adiciona final da lista de prontos o ultimo processo
	public void adicionaFinalPronto(Integer[] contexto) {
		tabelaProcessos.remove(0);
		int index = tabelaProcessos.size()-1;
		//para cada contexto na tabela processos
		for(Integer[] g: tabelaProcessos) {
			//caso o processo esteja bloqueado
			if(g[0] == 1) {
				//como index tem o n° do 1 processo bloqueado, adicionamos antes deste
				tabelaProcessos.add(index-1, contexto);
			}
			index++;
		}
	}
	
	//muda status de um processo e adiciona na fila de bloqueados
	public void bloqueia(Integer[] contexto) {
		//remove o primeiro elemento - aquele que estava executando
		tabelaProcessos.remove(0);
		//adiciona ao final da fila de bloqueados
		tabelaProcessos.add(contexto);
	}
	
	//verifica todos os bloqueados e adicionaFinalPronto para aquele com n°Bloqueio == 1
	public void verificaBloqueados() {
		int index = 0;
		for(Integer[] g: tabelaProcessos) {
			//caso o processo esteja bloqueado e falta 1 interação para liberar IO
			if(g[0] == 1 && g[1] == 1) {
				adicionaFinalPronto(tabelaProcessos.remove(index));				
			}
			else if(g[0] == 1 && g[1] > 1) {
				//recupera o contexto atual dele
				Integer[] novoContexto = tabelaProcessos.get(index);
				//subtrai 1 do nBloqueio
				novoContexto[1]--;
				//limpa a linha dele
				tabelaProcessos.remove(index);
				//adiciona ele no mesmo indice, porem com contexto atualizado
				tabelaProcessos.add(index, novoContexto);
			}
			index++;
		}
	}
	
	//remove o programa da tabela, pois este terminou
	public void removeProcesso() {
		tabelaProcessos.remove(0);
	}
		
	
}
