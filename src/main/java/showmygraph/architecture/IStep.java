package showmygraph.architecture;

public interface IStep {
	IContext unsafeExecute(IContext context);
}
