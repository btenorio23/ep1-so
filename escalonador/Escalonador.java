package escalonador;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import core.LeitorArquivos;
import core.Maquina;

//Classe responsável por escalonar programas da maquina
public class Escalonador implements Runnable {
	LeitorArquivos leitor;
	Maquina maquina;
	int quantumAtual = 0;
	TreeMap<Integer,BCP> tabelaProcessos = new TreeMap<Integer,BCP>();
	
	List<Integer> prontos;
	List<Integer> bloqueados;
	int quantum;
	int tempoBloqueado = 2;
	int contadorTrocas = 0;
	int contadorInstrucoes = 0;
	int qtdProcessos = 0;
	
	public Escalonador(){
		leitor = new LeitorArquivos();
		quantum = leitor.getQuantum();
		
		BufferedWriter log = null;
		File logFile = new File("src/log/log"+quantum+ ".txt"); 
		logFile.getParentFile().mkdirs();	
		Path arquivos = Paths.get("src/log/log.txt");
		try {
			log = Files.newBufferedWriter(arquivos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		maquina = new Maquina(0, 0, 1);
		//tabelaProcessos = );
		prontos = new LinkedList<Integer>();
		bloqueados = new LinkedList<Integer>();
		
		tabelaProcessos = leitor.getTabela();
		qtdProcessos = tabelaProcessos.size();
		for(int i = 1; i <= tabelaProcessos.size(); i++){
			prontos.add(i);
			//System.out.println(i);
		}
	}
	
	public void escreveLog(String s) {
		try {
		    Files.write(Paths.get("src/log/log.txt"), (s+"\n").getBytes(), StandardOpenOption.APPEND);
		}catch (IOException e) {
		    //exception handling left as an exercise for the reader
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
					
					//System.out.println(tabelaProcessos.get(proc).getNomeProcesso());
					
					String proxComando = tabelaProcessos.get(proc).codProg[maquina.getPC()];
					
					maquina.setPC(maquina.getPC()+1);
					contadorInstrucoes++;
					escreveLog("Executando TESTE-"+proc);
					switch(proxComando) {
					
					
					case "E/S":
						flagIO = true;
						sinalFinal = true;
						escreveLog("E/S iniciada em TESTE"+proc);
						//System.out.println("*************************");
						
						break;
						
					case "COM":
						//System.out.println("Executando COM");
						//System.out.println("*************************");
						
						break;
						
					case "SAIDA":
						escreveLog("TESTE-"+proc+ " terminado." + "X: "+maquina.getREGX() + " Y: "+maquina.getREGY());						
						sinalFinal = true;
						encerraProcesso(proc);
						break;
						
					default:
						
						if(proxComando.contains("=")) {
							//System.out.println("Trabalhando com atribuição");
							if(proxComando.contains("X")) {
								maquina.setREGX(Integer.parseInt(proxComando.split("")[2]));
								//System.out.println("Atributo de X: " + maquina.getREGX());
								
							}
							else {
								maquina.setREGY(Integer.parseInt(proxComando.split("")[2]));
								//System.out.println("Atributo de Y: "+maquina.getREGY());
								
							}
						}
						//System.out.println("*************************");
					 
					}
						if(sinalFinal == true)
							break;
		
			}
				
				if(sinalFinal != true){
					trocaProcesso(proc, quantumAtual);
				}
				
				if(flagIO == true){
					bloqueiaProcesso(proc, quantumAtual);
					flagIO = false;
				}
				quantumAtual = 0;
				sinalFinal = false;
				

			}
			 


	}
	
	public void getMediaTrocas() {
		escreveLog("MEDIA DE TROCAS: "+contadorTrocas/qtdProcessos);
	}
	
	public void getMediaInstrucoes() {
		escreveLog("MEDIA DE INSTRUCOES: "+ contadorInstrucoes/contadorTrocas);
	}
	
	public void getQuantum() {
		escreveLog("QUANTUM: "+quantum);
	}
	
	//M�todo auxiliar para diminuir o tempo dos processos bloqueados
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
	void bloqueiaProcesso(int processo, int quantumAtual){
		contadorTrocas++;
		escreveLog("Interrompendo TESTE"+processo+ "após"+  quantumAtual +"instruções");
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
	
	//Faz a preempcao do processo
	void trocaProcesso(int processo, int quantumAtual){
		escreveLog("Interrompendo TESTE"+processo+ "após"+  quantumAtual +"instruções");
		contadorTrocas++;
		salvaContexto(processo);
		tabelaProcessos.get(processo).setEstado(1);
		prontos.add(processo);
		
		
	}
	
	void encerraProcesso(int processo){
		tabelaProcessos.remove(processo);
		
		
		
		
	}

	


}
