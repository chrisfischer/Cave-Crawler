

public class Tuple<V, K> {
	
	V o1;
	K o2;
	
	public Tuple(V o1, K o2) {
		this.o1 = o1;
		this.o2 = o2;
	}
	
	public V getFirst() {
		return o1;
	}
	public K getSecond() {
		return o2;
	}
}
