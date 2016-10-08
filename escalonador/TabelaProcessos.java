package escalonador;

import java.util.ArrayList;

//Classe que representa a tabela com todos os BCPs
public class TabelaProcessos {

	//bcps da tabela, aqui estão juntos processos prontos, bloqueados e o executando
	ArrayList<Integer[]> bcps;
	
	//Inicia a tabela de processo com o numero de processo que a CPU receberá (numero de arquivos)
	public TabelaProcessos(int length) {
		bcps = new ArrayList<Integer[]>();
		for(int i=0; i<length; i++) {
			//Esse é o contexto, receberá Status(Bloq, Pront, etc), nBloqueio, REGX, REGY, PC, nProcesso (ID para ver quais seus comandos)
			//Pront -> 0 | Bloq -> 1 
			Integer[] contexto = new Integer[6];
			//todos os processos iniciam com pronto (Status)
			contexto[0] = 0;
			//todo os processos tem nBloqueio = 0 (n° vezes que precisa um outro processo executar para liberar IO)
			contexto[1] = 0;
			
			//seta ID para o processo (devido a ler os comandos daquele processo)
			contexto[5] = i;
			
			/* Para não ter valores null, setamos todos como 0*/
			contexto[2] =0;
			contexto[3] =0;
			contexto[4] =0;
			
			
			//adiciona o contexto desse processo
			bcps.add(contexto);
			
			/*
			 * Quando a tabela é iniciada, todos os processos tem status pronto!
			 * */
			
			
		}
	}
	
	//retorna ID do processo que está executando
	public int getNumeroProcesso() {
		return retornaPronto()[5];
	}
	
	//busca na tabela o proximo processo pronto. Caso todos estejam bloqueados, verifica se a lista não esta vazia e decrementa IO
	public Integer[] retornaPronto() {
		
		//verificamos se ainda existem processos para serem rodados
		if(bcps.size() > 0) {
			//retorna o contexto do processo que poderá ser executado
			Integer[] contexto = bcps.get(0);
			//caso seu contexto seja status bloqueado
			if(contexto[0] == 1) {
				//enquanto ele ainda tiver status bloqueado
				while(bcps.get(0)[0] == 1) {
					//vai decrementando tempo de espera do IO
					verificaBloqueados();
				}
			}
			//o próximo processo pronto sempre será o primeiro na tabela (que é o primeiro da fila)
			return bcps.get(0);
		}
		else {
			System.out.println("Acabaram os processos!");
			System.exit(0);
			return null;
		}
	}
	
	//adiciona final da lista de prontos o último processo
	public void adicionaFinalPronto(Integer[] contexto) {
		//esse processo estava executando, removemos ele
		bcps.remove(0);
		//iteramos desde o começo na tabela até achar o 1° processo bloqueado, então adicionamos antes dele.
		int index = 0;
		//para cada contexto na tabela processos
		for(Integer[] g: bcps) {
			//caso o processo esteja bloqueado
			if(g[0] == 1) {
				//como index tem o n° do 1 processo bloqueado, adicionamos antes deste. Assim, o 1° processo
				//bloqueado recebe shift para direita e libera espaço para o último pronto
				bcps.add(index, contexto);
				return;
			}
			//como não achamos bloqueado, adicionamos mais ao index;
			index++;
		}
		//não achou nenhum processo bloqueado, logo, adicionamos ao final da fila.
		bcps.add(contexto);
		System.out.println("Contexto adicionado com sucesso no index:" + index);
	}
	
	//muda status de um processo e adiciona na fila de bloqueados
	public void bloqueia(Integer[] contexto) {
		//remove o primeiro elemento - aquele que estava executando
		bcps.remove(0);
		//adiciona ao final da fila de bloqueados
		bcps.add(contexto);
	}
	
	//verifica todos os bloqueados e adicionaFinalPronto para aquele com n°Bloqueio == 1
	public void verificaBloqueados() {
		int index = 0;
		for(Integer[] g: bcps) {
			//caso o processo esteja bloqueado e falta 1 interação para liberar IO
			if(g[0] == 1 && g[1] == 1) {
				adicionaFinalPronto(bcps.remove(index));
			}
			else if(g[0] == 1 && g[1] > 1) {
				//recupera o contexto atual dele
				Integer[] novoContexto = bcps.get(index);
				//subtrai 1 do nBloqueio
				novoContexto[1]--;
				//limpa a linha dele
				bcps.remove(index);
				//adiciona ele no mesmo indice, porem com contexto atualizado
				bcps.add(index, novoContexto);
			}
			index++;
		}
	}
	
	//remove o programa da tabela, pois este terminou
	public void removeProcesso() {
		bcps.remove(0);
	}
		
	
}
