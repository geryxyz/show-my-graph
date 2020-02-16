package showmygraph.model.graph;

import static org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal;

import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;

import showmygraph.architecture.EmptyContext;
import showmygraph.architecture.SingleValueContext;
import showmygraph.architecture.Source;

public class GremlinTraversalSource extends Source<SingleValueContext<GraphTraversalSource>> {

	private GraphTraversalSource g;
	
	public GremlinTraversalSource(String host, int port, String traversal) {
		this.g = traversal().withRemote(DriverRemoteConnection.using(host, port, traversal));
	}
	
	@Override
	protected SingleValueContext<GraphTraversalSource> execute(EmptyContext context) {
		return new SingleValueContext<GraphTraversalSource>(this.g);
	}


}
