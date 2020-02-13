package showmygraph.architecture;

public abstract class Step<TIn extends IContext, TOut extends IContext> implements IStep {
	protected Step<?, ?> next = null;
	protected Step<?, ?> previous = null;
	
	public Step<?, ?> then(Step<?, ?> next) {
		this.next = next;
		next.previous = this; 
		return this.next;
	}
	
	public Source<?> endWith(Sink<?> sink) {
		then(sink);
		return getSource();
	}
	
	public Step<?, ?> before(Step<?, ?> previous) {
		this.previous = previous;
		previous.next = this;
		return this.previous;
	}
		
	public Source<?> getSource() {
		if (this.previous == null) {
			return (Source<?>) this;
		} else {
			return this.previous.getSource();
		}
	}
	
	protected abstract TOut execute(TIn context);
	
	@SuppressWarnings("unchecked")
	@Override
	public IContext unsafeExecute(IContext context) {
		return this.execute((TIn) context);
	}
}
