package showmygraph.model.wrapper;

import showmygraph.architecture.IContext;

public class SingleValueContext<TValue> implements IContext {
	
	private TValue value;

	public TValue getValue() {
		return value;
	}
	
	public SingleValueContext(TValue value) {
		this.value = value;
	}

}
