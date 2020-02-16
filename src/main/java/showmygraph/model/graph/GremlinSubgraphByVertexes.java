package showmygraph.model.graph;

import java.util.ArrayList;
import java.util.List;

import javax.script.Bindings;
import javax.script.ScriptException;

import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import com.tinkerpop.gremlin.groovy.jsr223.GremlinGroovyScriptEngine;

import showmygraph.architecture.SingleValueContext;
import showmygraph.util.EU;

public class GremlinSubgraphByVertexes extends GremlinSelectSubgraphStep {


	public GremlinSubgraphByVertexes(String selector, String traversalName) {
		super(selector, traversalName);
	}

	@SuppressWarnings("restriction")
	@Override
	protected SubgraphContext execute(SingleValueContext<GraphTraversalSource> context) {
		GremlinGroovyScriptEngine engine = new GremlinGroovyScriptEngine();
		Bindings bindings = engine.createBindings();
		bindings.put(this.traversalName, context.getValue());
		Object result = EU.tryUnchecked(() -> engine.eval(this.selector, bindings));

		List<Vertex> vertexes = new ArrayList<>();
		if (result instanceof List<?>) {
			for (Object item : (List<?>)result) {
				if (item instanceof Vertex) {
					vertexes.add((Vertex) item);
				}
			}		
			System.out.println(String.format("Altogether %d vertex loaded.", vertexes.size()));
		} else {
			System.err.println("Query does not yield a list.");
		}
		List<Edge> edges = context.getValue().E().where(__.bothV().is(P.within(vertexes))).toList();
		return new SubgraphContext(vertexes, edges, context.getValue());
	}

}
