package escalonador;

import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class TabelaProcessosGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//Todos os processos que devem ser listados
	public static LinkedList<String[]> processos;
	
	JTable tabela;
	static DefaultTableModel dtm;
	String[] nomeColunas = {
			"Nome Processo",
			"PC",
			"Status",
        };
	
	public TabelaProcessosGUI() {
		super();
		processos = new LinkedList<String[]>();
		dtm = new DefaultTableModel(0, 0);
		dtm.setColumnIdentifiers(nomeColunas);
	}
		
	
	//Receberá o nome do processo e seus status
	public static void recebeData(String[] informacoesProcesso) {
		if(!processos.contains(informacoesProcesso)) {
			processos.add(informacoesProcesso);
		}
		dtm.addRow(informacoesProcesso);
	}
	
	public void criaTabela() {
		String[][] data = new String[processos.size()][];
		data = processos.toArray(data);
		tabela = new JTable(data, nomeColunas);
		tabela.setBounds(50,50,200,230);
		tabela.setModel(dtm);
		JScrollPane js = new JScrollPane(tabela);
		this.add(js);
		this.setSize(300,400);
		this.setVisible(true);
		
	}
	
}
