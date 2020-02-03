package showmygraph.architecture;

public abstract class Sink<TIn extends IContext> extends Step<TIn, EmptyContext> {
	public void apply(TIn context) {
		execute(context);
	}
	
	@Override
	public Step<IContext, IContext> then(Step<IContext, IContext> next) {
		super.then(next);
		return getSource();
	}
}
