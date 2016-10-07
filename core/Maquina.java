package core;

import java.util.Observable;
import java.util.Observer;

import escalonador.Escalonador;

//Classe responsável por armazenar os registradores, gerenciar todos os "programas" e
//gerenciar o Escalonador
public class Maquina extends Observable implements Observer {

	//Registradores da máquina
	int REGX, REGY, PC;
	int quantumVez = 1;
	boolean flagIO = false;
	
	//Instância responsável por repassar o próximo comando dos programas
	LeitorArquivos leitorArq = new LeitorArquivos();
	//Instância do escalonador que interage com a maquina (CPU)
	Escalonador escalonador;
	
	public Maquina() {
		//Escalonador recebe o leitor com o indice para "ler os programas" e recebe o quantum
		escalonador = new Escalonador(leitorArq.retornaNumeroProgramas(), leitorArq.getQuantum());
		
		//Escalonador estará de olho na CPU para casos de interrupções de I/O
		this.addObserver(escalonador);
		escalonador.addObserver(this);
		escalonador.setTamanhoTabela(leitorArq.retornaNumeroProgramas().length);
		
	}
	
	public void setaContexto(String[] contx) {
		if(contx == null) {
			this.REGX = 0; this.REGY = 0; this.PC = 0;
			return;
		}
		this.REGX = Integer.parseInt(contx[0]);
		this.REGY = Integer.parseInt(contx[1]);
		this.PC = Integer.parseInt(contx[2]);
	}
	
	//Inicia a máquina e roda processos
	public void iniciaMaquina() {		
		
		//numero do processo atual
		int processoVez = 0;
		
		//o programa só acaba quando todos os processos forem executados
		while(true) {
			
			processoVez = escalonador.devolveProcesso(quantumVez);
			System.out.println("PC -> "+this.PC);
			String proxComando = leitorArq.proximoComando(processoVez, this.PC);
			System.out.println("Processo numero: "+ (processoVez));			
						
			
			//Sleep só para testes ***************
			try {
				System.out.println("Quantum da vez: "+quantumVez);
				
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			//acrescenta pc
			this.PC +=1;
			
			switch(proxComando) {
			
			case "E/S":
				flagIO = true;
				System.out.println("Executando E/S");
				System.out.println("*************************");
				this.setChanged();
				String contexto[] = {this.REGX+"", this.REGY+"", (this.PC)+""};
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

	@Override
	public void update(Observable o, Object arg) {
		
		//Depois do processo 5 ou 6, para de salvar contexto;
		
		String contexto[] = {this.REGX+"", this.REGY+"", (this.PC)+""};
		if(!flagIO) {
			System.out.println("salvou contexto");
			escalonador.salvaContexto(contexto);
			flagIO = false;
		}
		setaContexto((String[]) arg);
		quantumVez = 1;
		
	}
	
}
