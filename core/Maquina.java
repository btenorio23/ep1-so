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
		
		/******************************************************************/
		//comandos.addAll(Arrays.asList(escalonador.devolveProcesso()));
		
		while(!comandos.isEmpty()) {
			String c = comandos.pop();
			System.out.println("printando c: "+ c);
			switch(c) {
			
			case "E/S":
				System.out.println("É IO, interromper agora!");
				this.setChanged();
				this.notifyObservers();
				break;				
				
			case "COM":
				System.out.println("Executando comando do sistema");
				break;
				
			default: 
				System.out.println("Executando comando normal");
				return;				
			}
		}
		
		
	}
	
}
