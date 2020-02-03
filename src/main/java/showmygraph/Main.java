package showmygraph;

import com.tinkerpop.gremlin.groovy.jsr223.GremlinGroovyScriptEngine;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import showmygraph.model.PropertyMap;
import showmygraph.ui.GraphWindow;
import static org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.script.Bindings;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;

public class Main {

	public static void main(String[] args) throws Exception {
		
		
		
		
		
		
		
		System.out.println("I will show your graph to you.");

		GraphWindow graphWindow = new GraphWindow();

		ArgumentParser parser = ArgumentParsers.newFor("showmygraph").build()
				.description("Will show your Tinkerpop (Gremlin Server) graph.");
		parser.addArgument("--host").type(String.class).required(false).setDefault("localhost");
		parser.addArgument("--port").type(Integer.class).required(false).setDefault(8182);
		parser.addArgument("--traversal").type(String.class).required(false).setDefault("g");
		parser.addArgument("--query").type(String.class).required(false).setDefault("g.V().toList()");
		parser.addArgument("--label").type(String.class).required(false).setDefault("${label}");

		Namespace resolvedArgs;
		try {
			resolvedArgs = parser.parseArgs(args);
		} catch (ArgumentParserException e) {
			parser.handleError(e);
			return;
		}
		
		GraphTraversalSource g = traversal().withRemote(DriverRemoteConnection
				.using(resolvedArgs.getString("host"), resolvedArgs.getInt("port"), resolvedArgs.getString("traversal")));
		
		GremlinGroovyScriptEngine engine = new GremlinGroovyScriptEngine();
		Bindings bindings = engine.createBindings();
		bindings.put(resolvedArgs.getString("traversal"), g);
		Object result = engine.eval(resolvedArgs.getString("query"), bindings);

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
			return;
		}
				
		for (Vertex v : vertexes) {
			var node = graphWindow.addNode(v.id().toString());
			System.out.println(String.format("Loading node %s", v.id().toString()));
			Map<Object, Object> properties = g.V(v).valueMap().next();
			String labelPattern = resolvedArgs.getString("label");
			if ( node.getTag() instanceof PropertyMap) {
				PropertyMap tag = (PropertyMap) node.getTag();
				for (var property : properties.entrySet()) {
					tag.put(property.getKey().toString(), property.getValue());
				}
				labelPattern = labelPattern.replace("$[label]", g.V(v).label().next());
				for (String key : tag.keySet()) {
					labelPattern = labelPattern.replace(String.format("$[%s]", key), tag.get(key).toString());
				}
			}
			graphWindow.setLabelOf(node, labelPattern);
			System.out.println(String.format("\tAltogether %d properies loaded.", properties.size()));
		}
		for (Edge e : g.E().where(__.bothV().is(P.within(vertexes))).toList()) {
			var edge = graphWindow.addEdge(e.id().toString(), e.outVertex().id().toString(), e.inVertex().id().toString());
			System.out.println(String.format("Loading edge %s", e.id().toString()));
			graphWindow.setLabelOf(edge, g.E(e).label().next());
			Map<Object, Object> properties = g.E(e).valueMap().next();
			for (var property : properties.entrySet()) {
				if (edge.getTag() instanceof PropertyMap) {
					PropertyMap tag = (PropertyMap) edge.getTag();
					tag.put(property.getKey().toString(), property.getValue());
				}
			}
			System.out.println(String.format("\tAltogether %d properies loaded.", properties.size()));
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					graphWindow.show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
