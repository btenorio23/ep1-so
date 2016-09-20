package core;

import escalonador.Escalonador;

//Classe responsável por armazenar os registradores, gerenciar todos os "programas" e
//gerenciar o Escalonador
public class Maquina {

	//Registradores da máquina
	int REGX, REGY, PC;
	
	//Instância responsável por repassar o próximo comando dos programas
	LeitorArquivos leitorArq = new LeitorArquivos();
	Escalonador escalonador;
	
	public Maquina() {
		escalonador = new Escalonador(leitorArq.retornaNumeroProgramas());
	}
	
	//Inicia a máquina
	public void iniciaMaquina() {
			System.out.println(leitorArq.proximoComando(2, 1));
	}
	
	//Devolve todos os processos para serem apresentado pela tabela de processos.
	//Os processos são sempre devolvidos na ordem EXECUTANDO|PRONTOS|BLOQUEADOS
	public void devolveProcessos() {
		escalonador.getProcessos();
	}
	
}
