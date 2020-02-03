package showmygraph.architecture;

public abstract class Step<TIn extends IContext, TOut extends IContext> implements IStep<TIn, TOut> {
	private Step<? extends TOut, ? extends IContext> next;
	private Step<? extends IContext, ? extends TIn> previous;
	
	public Step<? extends TOut, ? extends IContext> then(Step<? extends TOut, ? extends IContext> next) {
		this.next = next;
		next.previous = this; 
		return this.next;
	}
	
	public Step<? extends IContext, ? extends TIn> before(Step<? extends IContext, ? extends TIn> previous) {
		this.previous = previous;
		previous.next = this;
		return this.previous;
	}
}
