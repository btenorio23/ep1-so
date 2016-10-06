package core;

import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

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
		escalonador.setTamanhoTabela(leitorArq.retornaNumeroProgramas().length);
		
	}
	
	public void setaContexto() {
		String[] contx = escalonador.devolveContexto();
		this.REGX = Integer.parseInt(contx[0]);
		this.REGY = Integer.parseInt(contx[1]);
		this.PC = Integer.parseInt(contx[2]);
	}
	
	//Inicia a máquina e roda processos
	public void iniciaMaquina() {
		
		int quantumVez = 1;
		int processoVez, processoVezProx = 0;
		//o programa só acaba quando todos os processos forem executados
		while(true) {
			
			//Antes de pedir o proximo comando ao escalonador, caso quantumVez == 3, salva contexto
			/*
			if(quantumVez == escalonador.getQuantum()+1) {
				String contexto[] = {this.REGX+"", this.REGY+"", (this.PC+1)+""};
				escalonador.salvaContexto(contexto);
				quantumVez = 1;
			}
			*/
			
			//************************************************************************************
			//PROBLEMA: quando lermos proximoComando, escalonador ira user pc antigo, não o novo.
			
			String proxComando;
			
			processoVez = escalonador.devolveProcesso(quantumVez);
			System.out.println("Processo numero: "+ processoVez);
			//trocou de programa, então recupera contexto
			if(processoVezProx != processoVez) {
				String contexto[] = {this.REGX+"", this.REGY+"", (this.PC+1)+""};
				escalonador.salvaContexto(contexto);
				setaContexto();
				proxComando = leitorArq.proximoComando(processoVez, this.PC);
				processoVezProx = processoVez;
				quantumVez = 1;
			}
			else {
				proxComando = leitorArq.proximoComando(processoVez, this.PC);
			}
			
			
			//Sleep só para testes ***************
			try {
				System.out.println("Quantum da vez: "+quantumVez);
				
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			//acrescenta pc
			this.PC +=1;
			
			switch(proxComando) {
			
			case "E/S":
				System.out.println("Executando E/S");
				System.out.println("*************************");
				this.setChanged();
				String contexto[] = {this.REGX+"", this.REGY+"", (this.PC+1)+""};
				this.notifyObservers(contexto);
				quantumVez = 1;
				continue;
				
			case "COM":
				System.out.println("Executando COM");
				System.out.println("*************************");
				quantumVez++;
				break;
				
			case "SAIDA":
				System.out.println("Encerrando o Programa");
				escalonador.encerraPrograma(escalonador.getProcessoVez());
				quantumVez = 1;
				break;
				
			default:
				
				if(proxComando.contains("=")) {
					//System.out.println("Trabalhando com atribuição");
					if(proxComando.contains("X")) {
						this.REGX = Integer.parseInt(proxComando.split("")[2]);
						System.out.println("Atributo de X: "+this.REGX);
					}
					else {
						this.REGY = Integer.parseInt(proxComando.split("")[2]);
						System.out.println("Atributo de Y: "+this.REGY);
					}
				}
				System.out.println("*************************");
				quantumVez++;
			 
			}
			
			
		}
		
	}
	
}
