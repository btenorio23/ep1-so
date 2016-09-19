package core;

import escalonador.Escalonador;

//Classe responsável por armazenar os registradores, gerenciar todos os "programas" e
//gerenciar o Escalonador
public class Maquina {

	int REGX, REGY, PC;
	Escalonador escalonador = new Escalonador();
	LeitorArquivos leitorArq = new LeitorArquivos();
	
	public void iniciaMaquina() {
		String[] comandos;
		Integer processoDaVez = 1;
		while((comandos = leitorArq.leArquivo(processoDaVez.toString())) != null) {
			processoDaVez++;
		}		
	}
	
	public String[] devolveProcessos() {
		return escalonador.getProcessos();
	}
	
}
