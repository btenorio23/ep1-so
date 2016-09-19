package main;

import core.Maquina;
import escalonador.TabelaProcessos;

//Classe responsável por iniciar o programa, mostrar todos os arquivos que devem ser lidos, etc
public class Main {

	public static void main(String[] args) {
		Maquina maquina = new Maquina();
		maquina.iniciaMaquina();
		
		TabelaProcessos table = new TabelaProcessos();
		table.criaTabela();
		try {
			while(true) {
				Thread.sleep(2000);
				table.recebeProcessos(maquina.devolveProcessos()); 
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
