

public class Maquina {
	int REGX, REGY, PC;
	//representa qual quantum a maquina esta executando
	int quantumVez = 1;

	public Maquina(int REGX, int REGY) {
		this.REGX = REGX;
		this.REGY = REGY;
		
	}

	public int getREGX() {
		return REGX;
	}

	public void setREGX(int rEGX) {
		REGX = rEGX;
	}

	public int getREGY() {
		return REGY;
	}

	public void setREGY(int rEGY) {
		REGY = rEGY;
	}
	
	
	
}
