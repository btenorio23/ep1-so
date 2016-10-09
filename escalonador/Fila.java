import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Fila implements Iterable<Integer> {

	private List<Integer> fila = new LinkedList<Integer>();
	
	public void insere(int valor){
		fila.add(valor);
	}
	
	public Integer remove(){
		return fila.remove(0);
	}
	
	public boolean vazia(){
		return fila.size() == 0;
	}
	
	
	public int getNext(){
		return this.fila.get(0);
	}
	
	public int size(){
		return this.fila.size();
	}

	@Override
	public Iterator<Integer> iterator() {
		return fila.iterator();
	}
	
}