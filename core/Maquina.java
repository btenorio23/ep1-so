package core;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Observable;

import escalonador.Escalonador;
//Classe responsável por armazenar os registradores, gerenciar todos os "programas" e
//gerenciar o Escalonador
public class Maquina extends Observable{

	//Registradores da máquina
	int REGX, REGY, PC;
	
	//Instância responsável por repassar o próximo comando dos programas
	LeitorArquivos leitorArq = new LeitorArquivos();
	Escalonador escalonador;
	
	public Maquina() {
		escalonador = new Escalonador(leitorArq.retornaNumeroProgramas(), leitorArq.getQuantum());
		this.addObserver(escalonador);
	}
	
	//Inicia a máquina
	public void iniciaMaquina() {
			executaProgramas();
	}
	
	//Devolve todos os processos para serem apresentado pela tabela de processos.
	//Os processos são sempre devolvidos na ordem EXECUTANDO|PRONTOS|BLOQUEADOS
	public void devolveProcessos() {
		escalonador.getProcessos();
	}
	
	public void executaProgramas() {
		//Pega o próximo processo do escalonador e o coloca em uma pilha
		LinkedList<String> comandos = new LinkedList<String>();
		int quantum = 0;
		PC = 0;
		/******************************************************************/
		//comandos.addAll(Arrays.asList(escalonador.devolveProcesso()));
		
		while(true) {
			
			if(quantum == 3) {
				escalonador.devolveProcesso(quantum);
				//pc vai ser definido por devolveProcesso(). O numero do processo também!
				//PC = 
			}
			
			//recebe linhas que o programa vai executar:
			//De acordo com o valor do if de cima, a string é definida com o proximo comando.
			//String comandos = leitorArq.proximoComando(nPrograma, PC)
			String comando = null;
			switch(comando) {
			
			case "E/S":
				System.out.println("É IO, interromper agora!");
				//notifica que o programa foi interrumpido por IO, o escalonador irá lidar com isso.
				this.setChanged();
				this.notifyObservers(1);
				quantum = 0;
				//repete conteudo do if (que é a troca de contexto)
				break;				
			
			//comando normal da cpu que não causa interrupção nem troca de contexto
			case "COM":
				System.out.println("Executando comando do sistema");
				break;
				
			//ainda não definido
			default: 
				System.out.println("Executando comando normal");
				return;				
			}
			
			
			quantum++;
		}
		
	}
	
}
