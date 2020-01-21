package showmygraph;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import com.tinkerpop.gremlin.groovy.jsr223.GremlinGroovyScriptEngine;

import showmygraph.ui.MainWindow;

import static org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal;

import java.awt.EventQueue;

import javax.script.Bindings;

import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;


public class Main {

	public static void main(String[] args) throws Exception {
		System.out.println("I will show your graph to you.");
		
		Graph graph = new SingleGraph("localhost");
		graph.addAttribute("ui.antialias");

		GraphTraversalSource g = traversal().withRemote(DriverRemoteConnection.using("localhost", 8182, "g"));
		for (Vertex v : g.V().toList()) {
			var node = graph.addNode(v.id().toString());
			node.addAttribute("ui.label", g.V(v).label().next());
		}
		for (Edge e : g.E().toList()) {
			var edge = graph.addEdge(e.id().toString(), e.outVertex().id().toString(), e.inVertex().id().toString());
			edge.addAttribute("ui.label", g.E(e).label().next());
		}
		
		GremlinGroovyScriptEngine engine = new GremlinGroovyScriptEngine();
		Bindings bindings = engine.createBindings();
		bindings.put("g", g);
		var result = engine.eval("g.V().toList()", bindings);		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow(graph);
					window.show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
}

}
