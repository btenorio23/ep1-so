package core;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

//Classe responsável por ler todos os arquivos e repassar para a maquina
public class LeitorArquivos {

	Path arquivos;
	
	public String[] leArquivo(String nomeArquivo) {
		
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
	
	
}
