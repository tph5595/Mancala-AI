public class Tuple {
	public final int value;
	public final String move;

	public Tuple(int x, String y) {
		this.value = x;
		this.move = y;
	}

	static public Tuple min(Tuple t1, Tuple t2) {
		return (t1.value < t2.value) ? t1 : t2;
	}
	static public Tuple max(Tuple t1, Tuple t2) {
		return (t1.value > t2.value) ? t1 : t2;
	}
}