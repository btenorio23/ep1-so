import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

//Classe responsÃ¡vel por escalonar programas da maquina
public class Escalonador implements Runnable {
	LeitorArquivos leitor;
	Maquina maquina;
	TreeMap<Integer,BCP> tabelaProcessos = new TreeMap<Integer,BCP>();
	boolean flagIO = false;
	Fila prontos;
	Fila bloqueados;
	int quantum;
	int tempoBloqueado = 2;
	
	public Escalonador(){
		
		maquina = new Maquina(0, 0, 1);
		
		leitor = new LeitorArquivos();
		quantum = leitor.getQuantum();

		//tabelaProcessos = );
		prontos = new Fila();
		bloqueados = new Fila();
		
		tabelaProcessos = leitor.getTabela();
		
		for(int i = 1; i <= tabelaProcessos.size(); i++){
			prontos.insere(i);
		}
	}
	
	
	public void run(){
			while(tabelaProcessos.size() > 0){
				int quantumAtual = 1;
				int proc = 1;
				maquina.setPC(1);
				
				while (prontos.size() == 0) {  
					diminuiBloqueados();
				}
				//Remove o primeiro processo na fila de prontos
				proc = prontos.remove();
				
				//Seta o processo atual como Executando
				tabelaProcessos.get(proc).setEstado(2);
				while(quantumAtual < quantum){
	
					System.out.println(tabelaProcessos.get(proc).getNomeProcesso());
					
					String proxComando = tabelaProcessos.get(proc).codProg[maquina.getPC()+1];
					
					System.out.println(proxComando);
					maquina.setPC(maquina.getPC()+1);
					
					switch(proxComando) {
					
					case "E/S":
						flagIO = true;
						
						System.out.println("Executando E/S");
						System.out.println("*************************");
						
						break;
						
					case "COM":
						System.out.println("Executando COM");
						System.out.println("*************************");
						
						break;
						
					case "SAIDA":
						System.out.println("Encerrando o Programa");
						encerraProcesso(proc);
	
						break;
						
					default:
						
						if(proxComando.contains("=")) {
							//System.out.println("Trabalhando com atribuiÃ§Ã£o");
							if(proxComando.contains("X")) {
								maquina.setREGX(Integer.parseInt(proxComando.split("")[2]));
								System.out.println("Atributo de X: " + maquina.getREGX());
								
							}
							else {
								maquina.setREGY(Integer.parseInt(proxComando.split("")[2]));
								System.out.println("Atributo de Y: "+maquina.getREGY());
								
							}
						}
						System.out.println("*************************");
					 
					}
					if(flagIO == true){
						bloqueiaProcesso(proc);
						trocaProcesso(proc);
					}
					quantumAtual++;
					System.out.println("Q atual " + quantumAtual);
					System.out.println("Q  " + quantum);
				}
				trocaProcesso(proc);
				diminuiBloqueados();
				
			}
			


	}
	
	
	//Método auxiliar para diminuir o tempo dos processos bloqueados
	void diminuiBloqueados(){
		int processo;
		List<Integer> aux = new LinkedList<Integer>();
		while(bloqueados.iterator().hasNext()){
			processo = bloqueados.iterator().next();
			tabelaProcessos.get(processo).decTempBloq();
			if(tabelaProcessos.get(processo).gettDesbloq() == 0){
				aux.add(processo);
				tabelaProcessos.get(processo).setEstado(1);
				prontos.insere(processo);
				
			}
		}
		bloqueados.removeAll(aux);
	}
	//Bloqueia um processo e salva seu contexto
	void bloqueiaProcesso(int processo){
		salvaContexto(processo);
		tabelaProcessos.get(processo).setEstado(0);
		tabelaProcessos.get(processo).setTempoBloq(tempoBloqueado);
		bloqueados.insere(processo);
		
	}
	// Salva o contexto atual do processo
	void salvaContexto(int processo){
		tabelaProcessos.get(processo).setPC(maquina.getPC());
		tabelaProcessos.get(processo).setREGX(maquina.getREGX());
		tabelaProcessos.get(processo).setREGY(maquina.getREGY());
		
	}
	
	//Faz a preempção do processo
	void trocaProcesso(int processo){
		salvaContexto(processo);
		prontos.insere(processo);
		
		
	}
	
	void encerraProcesso(int processo){
		tabelaProcessos.remove(processo);
		
		
	}

	


}
