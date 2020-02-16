package showmygraph.architecture;

public final class EmptyContext implements IContext {
	public static final EmptyContext instance = new EmptyContext();
	
	private EmptyContext() { }
}
