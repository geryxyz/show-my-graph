package showmygraph.architecture;

public abstract class Source<TOut extends IContext> extends Step<EmptyContext, TOut> {
	public TOut apply() {
		return execute(EmptyContext.instance);
	}
	
	public void pump() {
		Step<?, ?> current = this.next;
		IContext context = apply();
		while (current != null) {
			context = current.unsafeExecute(context);
			current = current.next;
		}
	}
}
