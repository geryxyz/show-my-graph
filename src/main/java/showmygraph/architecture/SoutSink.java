package showmygraph.architecture;

public class SoutSink extends Sink<IContext> {

	@Override
	protected EmptyContext execute(IContext context) {
		System.out.println(context.toString());
		return EmptyContext.instance;
	}

}
