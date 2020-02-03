package showmygraph.architecture;

public interface IStep<TIn extends IContext, TOut extends IContext>  {
	TOut apply(TIn context);
}
