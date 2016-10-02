package interrupcoes;

import escalonador.Escalonador;

public interface Interruptable {

	public Escalonador escalonador();
	public void update();
	
}
