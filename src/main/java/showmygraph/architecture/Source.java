package showmygraph.architecture;

public abstract class Source<TOut extends IContext> extends Step<EmptyContext, TOut> {
	public TOut apply() {
		return execute(EmptyContext.instance);
	}
}
