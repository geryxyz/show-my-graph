package showmygraph.architecture;

public abstract class Source extends Step {
	public <TOut extends IContext> TOut apply() {
		return apply(EmptyContext.instance);
	}
}
