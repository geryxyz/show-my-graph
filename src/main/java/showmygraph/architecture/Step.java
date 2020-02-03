package showmygraph.architecture;

public abstract class Step<TIn extends IContext, TOut extends IContext> implements IStep {
	private Step<IContext, IContext> next = null;
	private Step<IContext, IContext> previous = null;
	
	@SuppressWarnings("unchecked")
	public Step<IContext, IContext> then(Step<IContext, IContext> next) {
		this.next = next;
		next.previous = (Step<IContext, IContext>) this; 
		return this.next;
	}
	
	@SuppressWarnings("unchecked")
	public Step<IContext, IContext> before(Step<IContext, IContext> previous) {
		this.previous = previous;
		previous.next = (Step<IContext, IContext>) this;
		return this.previous;
	}
	
	//TODO: return Source type
	public Step<IContext, IContext> getSource() {
		if (this.previous == null) {
			return (Step<IContext, IContext>) this;
		} else {
			return this.getSource();
		}
	}
	
	protected abstract TOut execute(TIn context);
}
