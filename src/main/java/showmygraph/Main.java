package showmygraph;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import showmygraph.model.graph.GremlinSubgraphByVertexes;
import showmygraph.model.graph.GremlinTraversalSource;
import showmygraph.model.graph.SubgraphToIGraph;
import showmygraph.ui.GraphWindow;

public class Main {

	public static void main(String[] args) throws Exception {		
		System.out.println("I will show your graph to you.");

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
		
//		GraphTraversalSource g = traversal().withRemote(DriverRemoteConnection
//				.using(resolvedArgs.getString("host"), resolvedArgs.getInt("port"), resolvedArgs.getString("traversal")));
		
		
		var mainChain = new GremlinTraversalSource(resolvedArgs.getString("host"), resolvedArgs.getInt("port"), resolvedArgs.getString("traversal"))
				.then(new GremlinSubgraphByVertexes(resolvedArgs.getString("query"), resolvedArgs.getString("traversal")))
				.then(new SubgraphToIGraph(resolvedArgs.getString("label")))
				.endWith(new GraphWindow());
		mainChain.pump();
	}
}
