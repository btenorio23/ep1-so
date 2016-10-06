package core;

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
	//Instância do escalonador que interage com a maquina (CPU)
	Escalonador escalonador;
	
	public Maquina() {
		//Escalonador recebe o leitor com o indice para "ler os programas" e recebe o quantum
		escalonador = new Escalonador(leitorArq.retornaNumeroProgramas(), leitorArq.getQuantum());
		
		//Escalonador estará de olho na CPU para casos de interrupções de I/O
		this.addObserver(escalonador);
	}
	
	//Inicia a máquina e roda processos
	public void iniciaMaquina() {
		
		int quantumVez = 0;
		
		//o programa só acaba quando todos os processos forem executados
		while(true) {
			
			String proxComando = leitorArq.proximoComando(escalonador.devolveProcesso(quantumVez), PC);
			System.out.println("Proximo comando do programa: "+ escalonador.getProcessoVez());
			
			switch(proxComando) {
			
			case "E/S":
				this.setChanged();
				this.notifyObservers();
				quantumVez = 0;
				continue;
				
			case "COM":
				System.out.println("Executando COM");
				break;
				
			case "SAIDA":
				System.out.println("Encerrando o Programa");
				escalonador.encerraPrograma(escalonador.getProcessoVez());
				break;
				
			default:
				
				if(proxComando.contains("=")) {
					System.out.println("Trabalhando com atribuição");
					if(proxComando.contains("X")) {
						this.REGX = proxComando.charAt(proxComando.length()-1);
						System.out.println("Atributo de X: "+this.REGX);
					}
					else {
						this.REGY = proxComando.charAt(proxComando.length()-1);
						System.out.println("Atributo de Y: "+this.REGY);
					}
				}
			 
			
			}
			
			
		}
		
	}
	
	/*
	public void executaProgramas() {
		//Pega o próximo processo do escalonador e o coloca em uma pilha
		int quantumVez = 0;
		
		//executa o processo enquanto não encontrar I/O
		while(true) {
			
			System.out.println(leitorArq.proximoComando(escalonador.getProcessoVez(), this.PC));
			
			if(quantumVez == 3) {
				escalonador.devolveProcesso(quantumVez);
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
				quantumVez = 0;
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
			
			
			quantumVez++;
		}
		
	}
	*/
	
}
