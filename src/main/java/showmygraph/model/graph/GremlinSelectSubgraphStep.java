package showmygraph.model.graph;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;

import showmygraph.architecture.SingleValueContext;
import showmygraph.architecture.Step;

public abstract class GremlinSelectSubgraphStep extends Step<SingleValueContext<GraphTraversalSource>, SubgraphContext> {

	protected String selector;
	protected String traversalName;
	
	public GremlinSelectSubgraphStep(String selector, String traversalName) {
		this.selector = selector;
		this.traversalName = traversalName;
	}

}
