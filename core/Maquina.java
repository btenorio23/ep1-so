package core;

import java.util.Observable;
import java.util.Observer;

import escalonador.Escalonador;

//Classe responsável por armazenar os registradores, gerenciar todos os "programas" e
//gerenciar o Escalonador
public class Maquina extends Observable implements Observer {

	//Registradores da máquina
	int REGX, REGY, PC;
	//representa qual quantum a maquina esta executando
	int quantumVez = 1;
	//representa se maquina realizou IO ou não
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
		
	}
	
	//estabelece contexto da maquin
	public void setaContexto(Integer[] contx) {
		if(contx == null) {
			this.REGX = 0; this.REGY = 0; this.PC = 0;
			return;
		}
		this.REGX = contx[2];
		this.REGY = contx[3];
		this.PC = contx[4];
	}
	
	//Inicia a máquina e roda processos
	public void iniciaMaquina() {		
		
		//numero do processo atual
		int processoVez = 0;
		
		//o programa só acaba quando todos os processos forem executados
		while(true) {
			
			//Realiza round-robin (caso seja necessário)
			escalonador.devolveProcesso(quantumVez);
			
			//ID do processo que executará o proximo programa
			processoVez = escalonador.getProcesso();
			System.out.println("PC -> "+this.PC);
			
			//Recebe o próximo comando de acordo com o ID do programa e o PC deste;
			String proxComando = leitorArq.proximoComando(processoVez, this.PC);
			System.out.println("Processo numero: "+ (processoVez));			
			System.out.println("Quantum da vez: "+ quantumVez);
			
			//Sleep só para testes ***************
			//try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
			
			
			//Incrementa PC, assim, caso salve o contexto com IO, será salvo para iniciar o programa na proxima linha pós IO
			this.PC +=1;
			
			switch(proxComando) {
			
			case "E/S":
				flagIO = true;
				System.out.println("Executando E/S");
				System.out.println("*************************");
				this.setChanged();
				Integer contexto[] = {1, 2, this.REGX, this.REGY, this.PC, processoVez};
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
				escalonador.encerraPrograma();
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

	//Esse método é chamado para notificar Maquina que o contexto precisa ser salvo e o novo, trocado
	@Override
	public void update(Observable o, Object arg) {
		System.out.println("Trocando processos!");
		//arg -> numeroProcesso | ID do processo
		
		//recuperamos o contexto atual
		//como sempre é executado com programas prontos, nuca muda status
		Integer contexto[] = {0,0,this.REGX, this.REGY, this.PC, (Integer)arg};
		escalonador.salvaContexto(contexto);
		
		//mudamos o contexto para o novo processo que irá rodar
		setaContexto(escalonador.devolveContexto());
		//comecamos um novo processo, logo
		quantumVez = 1;
		
		/*
		if(!flagIO) {
			System.out.println("salvou contexto");
			escalonador.salvaContexto(contexto);
			flagIO = false;
		}
		setaContexto((String[]) arg);
		quantumVez = 1;
		flagIO = false;
		*/
	}
	
}
