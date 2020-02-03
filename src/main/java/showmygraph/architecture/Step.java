package showmygraph.architecture;

public abstract class Step implements IStep {
	private Step next;
	private Step previous;
	
	public Step then(Step next) {
		this.next = next;
		next.previous = this; 
		return this.next;
	}
	
	public Step before(Step previous) {
		this.previous = previous;
		previous.next = this;
		return this.previous;
	}
	
	protected abstract <TIn extends IContext, TOut extends IContext> TOut apply(TIn context);
}
