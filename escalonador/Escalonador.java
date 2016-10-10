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
	
	//responsáve por ler os arquivos 
	LeitorArquivos leitor;
	
	//CPI
	Maquina maquina;
	
	//QUANTUM ATUAL PARA SABER QUANDO REALIZAR PREEPCAO
	int quantumAtual = 0;
	
	//TABELA DE PROCESSOS BASEADAS NO ID DO PROCESSO
	TreeMap<Integer,BCP> tabelaProcessos = new TreeMap<Integer,BCP>();
	
	//FILA DE PROCESSOS PRONTOS
	List<Integer> prontos;
	
	//FILA DE PROCESSOS BLOQUEADOS
	List<Integer> bloqueados;
	
	//VALOR DO QUANTUM
	int quantum;
	
	//TEMPO QUE UM PROCESSO PRECISA FICAR BLOQUEADO
	int tempoBloqueado = 2;
	
	//CONTADORES USADOS EM LOGFILE
	int contadorTrocas = 0;
	int contadorInstrucoes = 0;
	int qtdProcessos = 0;
	
	public Escalonador(){
		
		//INICIA O LEITOR E REGISTRA VALOR DO QUANTUM
		leitor = new LeitorArquivos();
		quantum = leitor.getQuantum();
		
		//AQUI EH CRIADO O LOG FILE
		BufferedWriter log = null;
		File logFile;
		Path arquivos;
		
		//usamos o if para escrever "0" para numeros < 10
		if(quantum < 10) {
			logFile = new File("src/log/log"+ "0" +quantum+ ".txt");
			logFile.getParentFile().mkdirs();	
			arquivos= Paths.get("src/log/log"+ "0" +quantum  +".txt");
		}
		else {
			logFile = new File("src/log/log"+quantum+ ".txt");
			logFile.getParentFile().mkdirs();	
			arquivos= Paths.get("src/log/log" +quantum  +".txt");
		}
		 
		try {
			log = Files.newBufferedWriter(arquivos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//INICIA A MAQUINA
		maquina = new Maquina(0, 0, 1);
		
		//INICIAMOS AS FILAS 
		prontos = new LinkedList<Integer>();
		bloqueados = new LinkedList<Integer>();
		
		//RECUPERAMOS A TABELA DE PROCESSO, CRIADA NO LEITOR-ARQUIVOS
		tabelaProcessos = leitor.getTabela();
		
		//DETERMINAMOS QTD DE PROCESSOS
		qtdProcessos = tabelaProcessos.size();
		
		//ADICIONAMOS TODOS COMO PRONTOS
		for(int i = 1; i <= tabelaProcessos.size(); i++){
			prontos.add(i);
			//System.out.println(i);
		}
	}
	
	//FUNCAO QUE ESCREVE UMA DETERMINADA STRING NO LOG FILE
	public void escreveLog(String s) {
		try {
			if(quantum < 10) {
				Files.write(Paths.get("src/log/log"+"0" +quantum +".txt"), (s+"\n").getBytes(), StandardOpenOption.APPEND);
			}
			else {
				Files.write(Paths.get("src/log/log"+ quantum +".txt"), (s+"\n").getBytes(), StandardOpenOption.APPEND);
			}
		}catch (IOException e) {
		    //exception handling left as an exercise for the reader
		}
	}
	
	//METODO PRINCIPAL DO ESCALONADOR
	public void run(){
			
			//FLAG PARA QUANDO UM PROGRAMA ACABAR
			boolean sinalFinal = false;
			
			//FLAG PARA REGISTRAR IO
			boolean flagIO = false;
			
			//REPETIMOS ESSE PROCESSO ENQUANTO EXISTIR ALGUM PROGRAMA PARA RODAR
			while(tabelaProcessos.size()>0){
				
				//NUMERO DO PROCESSO QUE SERA EXECUTADO
				int proc = 1;
				
				//VERIFICAMOS SE EXISTE ALGUM PROCESSO PRONTO, CASO NEGATIVO, DIMINUIMOS O NUMERO DE BLOQUEADO
				while (prontos.size() == 0) {  
					diminuiBloqueados();
				}
				//Remove o primeiro processo na fila de prontos
				proc = prontos.remove(0);
				
				//SETAMOS O CONTEXTO DE ACORDO COM A TABELA DE PROCESSOS
				maquina.setPC(tabelaProcessos.get(proc).getPC());
				maquina.setREGX(tabelaProcessos.get(proc).getREGX());
				maquina.setREGY(tabelaProcessos.get(proc).getREGY());
				
				//Seta o processo atual como Executando
				tabelaProcessos.get(proc).setEstado(2);
				
				//REPETIMOS ENQUANTO O PROCESSO NAO RODAR O NUMERO DE VEZES DO QUANTUM RECUPERADO EM QUANTUM.TXT
				while(quantumAtual < quantum){
					
					//SOMAMOS UM
					quantumAtual++;
					
					//System.out.println(tabelaProcessos.get(proc).getNomeProcesso());
					
					//RECUPERAMOS O PROX COMANDO DE UM PROCESSO
					String proxComando = tabelaProcessos.get(proc).codProg[maquina.getPC()];
					
					//AUMENTAMOS O PC
					maquina.setPC(maquina.getPC()+1);
					
					//ESCREVEMOS NO LOG QUE USAMOS UMA INSTRUCAO
					contadorInstrucoes++;
					escreveLog("Executando TESTE-"+proc);
					
					//VERIFICAMOS QUAL O PROXIMO COMANDO
					switch(proxComando) {
					
					
					case "E/S":
						//SETAMOS AS FLAGS
						flagIO = true;
						sinalFinal = true;
						
						//ESCREVEMOS LOG
						escreveLog("E/S iniciada em TESTE"+proc);
						//System.out.println("*************************");
						
						break;
						
					case "COM":
						//System.out.println("Executando COM");
						//System.out.println("*************************");
						
						break;
						
					case "SAIDA":
						
						//ESCREVEMOS NO LOG
						escreveLog("TESTE-"+proc+ " terminado." + "X: "+maquina.getREGX() + " Y: "+maquina.getREGY());						
						
						//SETAMOS FLAG
						sinalFinal = true;
		
						//ENCERRAMOS O PROCESSO REMOVENDO-O DA TABELA
						encerraProcesso(proc);
						break;
						
					default:
						
						//SETAMOS UM REG DE ACORDO COM O VALORES APOS O '='
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
						//VERIFICAMOS SE TODOS OS PROGRAMAS RODARAM PARA SAIR DO WHILE
						if(sinalFinal == true)
							break;
		
			}
				
				//TROCAMOS O PROCESSO CASO SEJA UM SINALFINAL
				if(sinalFinal != true){
					trocaProcesso(proc, quantumAtual);
				}
				
				//BLOQUEAMOS CASO SEJA UM IO
				if(flagIO == true){
					bloqueiaProcesso(proc, quantumAtual);
					flagIO = false;
				}
				
				//ZERAMOS O QUANTUM ATUAL
				quantumAtual = 0;
				
				//ZERAMOS O SINALFINAL
				sinalFinal = false;
				

			}
			 


	}
	
	//METODO QUE AJUDA NA LOGFILE
	public void getMediaTrocas() {
		escreveLog("MEDIA DE TROCAS: "+contadorTrocas/qtdProcessos);
	}
	
	//METODO QUE AJUDA NA LOGFILE
	public void getMediaInstrucoes() {
		escreveLog("MEDIA DE INSTRUCOES: "+ contadorInstrucoes/contadorTrocas);
	}
	
	//METODO QUE AJUDA NA LOGFILE
	public void getQuantum() {
		escreveLog("QUANTUM: "+quantum);
	}
	
	//Meodo auxiliar para diminuir o tempo dos processos bloqueados
	void diminuiBloqueados(){
		int processo;
		//LISTA AUXILIAR PARA SABER QUAIS PROCESSOS BLOQUEADOS, DESBLOQUEAREMOS
		List<Integer> aux = new LinkedList<Integer>();
		for(int g : bloqueados){
			processo = g;
			tabelaProcessos.get(processo).decTempBloq();
			//VERIFICA SE JA PASSOU TODO O SEU TEMPO COMO BLOQUEADO
			if(tabelaProcessos.get(processo).gettDesbloq() == 0){
				tabelaProcessos.get(processo).setEstado(1);
				aux.add(processo);
				
				//ADICIONAMOS NA FILA DE PRONTOS
				prontos.add(processo);
				
			}
		}
		
		//REMOVEMOS COMO BLOQUEADOS TODOS OS QUE FORAM ADICIONADOS NA FILA DE PRONTOS
		bloqueados.removeAll(aux);
	}
	//Bloqueia um processo e salva seu contexto
	void bloqueiaProcesso(int processo, int quantumAtual){
		contadorTrocas++;
		escreveLog("Interrompendo TESTE"+processo+ "após"+  quantumAtual +"instruções");
		//SALVAMOS O CONTEXTO DA CPU
		salvaContexto(processo);
		
		//MUDAMOS O STATUS NA TABELA DE PROCESSO
		tabelaProcessos.get(processo).setEstado(0);
		
		//ADICIONAMOS O TEMPO DE BLOQUEADO
		tabelaProcessos.get(processo).setTempoBloq(tempoBloqueado);
		
		//ADICIONAMOS NA FILA DE BLOQUEADOS
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
