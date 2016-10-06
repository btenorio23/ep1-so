package core;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.TreeMap;

//Classe responsável por ler todos os arquivos e repassar para a maquina
public class LeitorArquivos {

	//Caminho para encontrar os arquivos
	Path arquivos;
	
	//Estrutura de mapa para guardar no Integer o n° do programa e no String[] todo o conteudo dele
	public TreeMap<Integer,String[]> programas;
	
	public LeitorArquivos() {
		
		//Para iniciarmos a maquina, precisamos ler todos os arquivos.
		programas = new TreeMap<Integer,String[]>();
		
		//Variável temporária para guardar o conteudo total do arquivo
		String[] comandos;
		//Contador para verificar qual arquivo devemos ler
		Integer processoDaVez = 1;
		
		//Enquanto o nosso método não retornar null (não consegui ler o arquivo) continuamos
		while((comandos = this.leArquivos(processoDaVez.toString())) != null) {
			//Adiciona programa com código dele e seus comandos
			programas.put(processoDaVez, comandos);
			processoDaVez++;
		}	
		
	}
	
	//Le um arquivo por completo e retorna seu conteúdo na forma de String[]
	public String[] leArquivos(String nomeArquivo) {
		//System.out.println("nomeArquivo: " + nomeArquivo);
		
		ArrayList<String> comandos = new ArrayList<String>(); 
		arquivos = Paths.get("src/arquivos/"+nomeArquivo + ".txt");
		try(BufferedReader arquivo = Files.newBufferedReader(arquivos)) {
			String line = null;
			while((line = arquivo.readLine()) != null) {
				comandos.add(line);
			}
		} catch(IOException ioe) {
			System.out.println("Erro tentando ler o arquivo: " + nomeArquivo);
			return null;
		}
		return comandos.toArray(new String[comandos.size()]);
	}
	
	//Le o próximo comando de um programa, dado seu n° e seu PC
	public String proximoComando(int numeroPrograma, int PC) {
		numeroPrograma +=1;
		PC += 1;
		String[] programaDesejado = programas.get(new Integer(numeroPrograma));
		return programaDesejado[PC];		
	}
	
	public Integer[] retornaNumeroProgramas() {
		return programas.keySet().toArray(new Integer[programas.size()]);
	}
	
	public int getQuantum() {
		arquivos = Paths.get("src/arquivos/quantum.txt");
		try(BufferedReader arquivo = Files.newBufferedReader(arquivos)) {
			String line = null;
			while((line = arquivo.readLine()) != null) {
				return Integer.parseInt(line);
			}
		} catch(IOException ioe) {
			System.out.println("Erro tentando ler o arquivo do quantum ");
			return -1;
		}
		return -1;
	}
	
	
}
