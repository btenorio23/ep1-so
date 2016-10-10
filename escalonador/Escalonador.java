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
	
	List<Integer> prontos;
	List<Integer> bloqueados;
	int quantum;
	int tempoBloqueado = 2;
	
	public Escalonador(){
		
		maquina = new Maquina(0, 0, 1);
		
		leitor = new LeitorArquivos();
		quantum = leitor.getQuantum();

		//tabelaProcessos = );
		prontos = new LinkedList<Integer>();
		bloqueados = new LinkedList<Integer>();
		
		tabelaProcessos = leitor.getTabela();
		
		for(int i = 1; i <= tabelaProcessos.size(); i++){
			prontos.add(i);
			System.out.println(i);
		}
	}
	
	
	public void run(){
			
			boolean sinalFinal = false;
			boolean flagIO = false;
			while(tabelaProcessos.size()>0){
				
				int proc = 1;
				
				
				while (prontos.size() == 0) {  
					diminuiBloqueados();
				}
				//Remove o primeiro processo na fila de prontos
				proc = prontos.remove(0);
				
				maquina.setPC(tabelaProcessos.get(proc).getPC());
				maquina.setREGX(tabelaProcessos.get(proc).getREGX());
				maquina.setREGY(tabelaProcessos.get(proc).getREGY());
				
				//Seta o processo atual como Executando
				tabelaProcessos.get(proc).setEstado(2);
				
				while(quantumAtual < quantum){
					quantumAtual++;
					
					System.out.println(tabelaProcessos.get(proc).getNomeProcesso());
					
					String proxComando = tabelaProcessos.get(proc).codProg[maquina.getPC()];
					
					System.out.println(proxComando);
					maquina.setPC(maquina.getPC()+1);
					
					switch(proxComando) {
					
					
					case "E/S":
						flagIO = true;
						sinalFinal = true;
						System.out.println("Executando E/S");
						System.out.println("*************************");
						
						break;
						
					case "COM":
						System.out.println("Executando COM");
						System.out.println("*************************");
						
						break;
						
					case "SAIDA":
						
						System.out.println("Encerrando o Programa");
						sinalFinal = true;
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
						if(sinalFinal == true)
							break;
		
			}
				
				//System.out.println("I'm Here!");
				if(sinalFinal != true){
					trocaProcesso(proc);
				}
				
				if(flagIO == true){
					bloqueiaProcesso(proc);
					flagIO = false;
				}
				quantumAtual = 0;
				sinalFinal = false;
				

			}
			 


	}
	
	
	//Método auxiliar para diminuir o tempo dos processos bloqueados
	void diminuiBloqueados(){
		int processo;
		List<Integer> aux = new LinkedList<Integer>();
		for(int g : bloqueados){
			processo = g;
			tabelaProcessos.get(processo).decTempBloq();
			if(tabelaProcessos.get(processo).gettDesbloq() == 0){
				tabelaProcessos.get(processo).setEstado(1);
				aux.add(processo);
				
				prontos.add(processo);
				
			}
		}
		bloqueados.removeAll(aux);
	}
	//Bloqueia um processo e salva seu contexto
	void bloqueiaProcesso(int processo){
		salvaContexto(processo);
		tabelaProcessos.get(processo).setEstado(0);
		tabelaProcessos.get(processo).setTempoBloq(tempoBloqueado);
		bloqueados.add(processo);
		
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
		tabelaProcessos.get(processo).setEstado(1);
		prontos.add(processo);
		
		
	}
	
	void encerraProcesso(int processo){
		tabelaProcessos.remove(processo);
		
		
		
		
	}

	


}
