package showmygraph.architecture;

public class ZeroSource extends Source<SingleValueContext<Integer>> {

	@Override
	protected SingleValueContext<Integer> execute(EmptyContext context) {
		return new SingleValueContext<Integer>(0);
	}

}
