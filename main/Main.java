package main;

import escalonador.Escalonador;

public class Main {

	public static void main(String[] args) {
		Escalonador escalonador = new Escalonador();
		escalonador.run();
		escalonador.getMediaTrocas();
		escalonador.getMediaInstrucoes();
		escalonador.getQuantum();
	}

}
