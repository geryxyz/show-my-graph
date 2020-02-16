package showmygraph.model.graph;

import java.util.Collections;
import java.util.List;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import showmygraph.architecture.IContext;

public class SubgraphContext implements IContext {
	private List<Vertex> vertexes;
	private List<Edge> edges;
	private GraphTraversalSource source;
	
	public List<Vertex> getVertexes() {
		return vertexes;
	}
	public List<Edge> getEdges() {
		return edges;
	}
	public GraphTraversalSource getSource() {
		return source;
	}

	public SubgraphContext(List<Vertex> vertexes, List<Edge> edges, GraphTraversalSource source) {
		this.vertexes = Collections.unmodifiableList(vertexes);
		this.edges = Collections.unmodifiableList(edges);
		this.source = source;
	}
}
