package showmygraph.architecture;

public class SingleValueContext<TValue> implements IContext {
	private TValue value;

	public TValue getValue() {
		return value;
	}
	
	public SingleValueContext(TValue value) {
		this.value = value;
	}	
	
	@Override
	public String toString() {
		return value.toString();
	}
}
