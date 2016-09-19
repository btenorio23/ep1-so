package escalonador;

import java.util.LinkedList;

//Classe responsável por escalonar programas da maquina
public class Escalonador {

	LinkedList<String> prontos = new LinkedList<String>();
	LinkedList<String> bloqueados = new LinkedList<String>();
	LinkedList<String> executando = new LinkedList<String>();

	public String[] getProcessos() {
		
		//realiza merge de todos
		prontos.addAll(bloqueados);
		prontos.addAll(executando);
		
		return prontos.toArray(new String[prontos.size()]);		
	}
	
}
