package showmygraph.architecture;

public abstract class Sink<TIn extends IContext> extends Step<TIn, EmptyContext> {
	public void apply(TIn context) {
		execute(context);
	}
}
