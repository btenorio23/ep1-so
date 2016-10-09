import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

//Classe responsÃ¡vel por escalonar programas da maquina
public class Escalonador implements Runnable {
	LeitorArquivos leitor;
	Maquina maquina;
	int quantumAtual = 0;
	TreeMap<Integer,BCP> tabelaProcessos = new TreeMap<Integer,BCP>();
	
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
			System.out.println(i);
		}
	}
	
	
	public void run(){
			
			boolean flag = false;
			boolean flagIO = false;
			while(tabelaProcessos.size() > 0){
				
				int proc = 1;
				
				
				//while (prontos.size() == 0) {  
				//	diminuiBloqueados();
				//}
				//Remove o primeiro processo na fila de prontos
				proc = prontos.remove();
				maquina.setPC(tabelaProcessos.get(proc).getPC());
				maquina.setREGX(tabelaProcessos.get(proc).getREGX());
				maquina.setREGY(tabelaProcessos.get(proc).getREGY());
				
				//Seta o processo atual como Executando
				tabelaProcessos.get(proc).setEstado(2);
				
				while(quantumAtual <= quantum){
					quantumAtual++;
					System.out.println(tabelaProcessos.get(proc).getNomeProcesso());
					
					String proxComando = tabelaProcessos.get(proc).codProg[maquina.getPC()];
					
					System.out.println(proxComando);
					maquina.setPC(maquina.getPC()+1);
					
					switch(proxComando) {
					
					
					case "E/S":
						flagIO = true;
						flag = true;
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
						if(flag == true)
							break;
		
			}
				
				//System.out.println("I'm Here!");
				if(!flag){
					trocaProcesso(proc);
				}
				
				if(flagIO == true){
					bloqueiaProcesso(proc);
					flagIO = false;
				}
				flag = false;
				quantumAtual = 0;
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
