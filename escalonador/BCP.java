

public class BCP {
	//Contador de Programa
	int PC = 0;
	
	// Estado Bloqueado = 0, Pronto = 1, Executando = 2
	int estado = 1;
	
	//Referência ao código na memória
	String[] codProg = new String[21];
	
	//Nome do processo
	String nomeProcesso;
	
	//Estado dos registradores X e Y
	int REGX;
	int REGY;
	
	public BCP(int pC, int estado, String[] codProg, String nomeProcesso, int rEGX, int rEGY) {
		
		PC = pC;
		this.estado = estado;
		this.codProg = codProg;
		this.nomeProcesso = nomeProcesso;
		REGX = rEGX;
		REGY = rEGY;
	}
	public int getPC() {
		return PC;
	}
	public void setPC() {
		PC = PC+1;
	}
	public int getEstado() {
		return estado;
	}
	public void setEstado(int estado) {
		this.estado = estado;
	}

	public String getNomeProcesso() {
		return nomeProcesso;
	}
	public void setNomeProcesso(String nomeProcesso) {
		this.nomeProcesso = nomeProcesso;
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
	public String[] getCodProg() {
		return codProg;
	}
	public void setCodProg(String[] codProg) {
		this.codProg = codProg;
	}
	
	

}
