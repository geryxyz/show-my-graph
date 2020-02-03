package showmygraph.architecture;

public class Increment extends Step<SingleValueContext<Integer>, SingleValueContext<Integer>> {

	@Override
	protected SingleValueContext<Integer> execute(SingleValueContext<Integer> context) {
		return new SingleValueContext<Integer>(context.getValue() + 1);
	}
}
