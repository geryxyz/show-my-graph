package showmygraph.architecture;

public abstract class Sink implements IStep {
	public <TIn extends IContext> void apply(TIn context) {
		apply(context);
	}
}
