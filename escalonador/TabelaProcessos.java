package escalonador;

import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class TabelaProcessos extends JFrame {

	LinkedList<String> processos;
	
	public TabelaProcessos() {
		super();
		processos = new LinkedList<String>();
	}
	
	JTable tabela;
	
	String[] nomeColunas = {
				"Nome Processo",
				"PC",
				"Status",
            };
	
	String[][] data = 
	{
			{"01.txt", "1", "Bloqueado"},
			{"03.txt", "2", "Pronto"},
			{"02.txt", "5", "Executando"}	
	};
	
	public void recebeProcessos(String[] processos) {
		for(String processo: processos) {
			this.processos.add(processo);
		}
		
	}
	
	public void criaTabela() {
		
		tabela = new JTable(data, nomeColunas);
		tabela.setBounds(50,50,200,230);
		JScrollPane js = new JScrollPane(tabela);
		this.add(js);
		this.setSize(300,400);
		this.setVisible(true);
		
	}
	
}
