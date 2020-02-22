package showmygraph.model.graph;

import java.awt.Color;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import com.yworks.yfiles.geometry.PointD;
import com.yworks.yfiles.geometry.RectD;
import com.yworks.yfiles.graph.DefaultGraph;
import com.yworks.yfiles.graph.IEdge;
import com.yworks.yfiles.graph.IGraph;
import com.yworks.yfiles.graph.ILabel;
import com.yworks.yfiles.graph.INode;
import com.yworks.yfiles.graph.styles.DefaultLabelStyle;
import com.yworks.yfiles.view.Pen;

import showmygraph.architecture.SingleValueContext;
import showmygraph.architecture.Step;
import showmygraph.model.PropertyMap;

public class SubgraphToIGraph extends Step<SubgraphContext, SingleValueContext<IGraph>> {

	private IGraph graph;
	private String labelingPropertyName;
	
	public SubgraphToIGraph(String labelingPropertyName) {
		this.labelingPropertyName = labelingPropertyName;
	}

	@Override
	protected SingleValueContext<IGraph> execute(SubgraphContext context) {
		this.graph = new DefaultGraph();
		for (Vertex v : context.getVertexes()) {
			var node = this.addNode(v.id().toString());
			System.out.println(String.format("Loading node %s", v.id().toString()));
			Map<Object, Object> properties = context.getSource().V(v).valueMap().next();
			String labelPattern = this.labelingPropertyName;
			if ( node.getTag() instanceof PropertyMap) {
				PropertyMap tag = (PropertyMap) node.getTag();
				for (var property : properties.entrySet()) {
					tag.put(property.getKey().toString(), property.getValue());
				}
				labelPattern = labelPattern.replace("$[label]", context.getSource().V(v).label().next());
				for (String key : tag.keySet()) {
					labelPattern = labelPattern.replace(String.format("$[%s]", key), tag.get(key).toString());
				}
			}
			this.setLabelOf(node, labelPattern);
			System.out.println(String.format("\tAltogether %d properies loaded.", properties.size()));
		}
		for (Edge e : context.getEdges()) {
			var edge = this.addEdge(e.id().toString(), e.outVertex().id().toString(), e.inVertex().id().toString());
			System.out.println(String.format("Loading edge %s", e.id().toString()));
			this.setLabelOf(edge, context.getSource().E(e).label().next());
			Map<Object, Object> properties = context.getSource().E(e).valueMap().next();
			for (var property : properties.entrySet()) {
				if (edge.getTag() instanceof PropertyMap) {
					PropertyMap tag = (PropertyMap) edge.getTag();
					tag.put(property.getKey().toString(), property.getValue());
				}
			}
			System.out.println(String.format("\tAltogether %d properies loaded.", properties.size()));
		}
		return new SingleValueContext<>(this.graph);
	}
	
	public INode addNode(String id) {
		INode node = graph.createNode();
		var tag = new PropertyMap(id);
		node.setTag(tag);
		return node;
	}
	
	public void setLabelOf(INode node, String text) {
		ILabel label = graph.addLabel(node, text);
		graph.setNodeLayout(node, new RectD(new PointD(0, 0), label.getPreferredSize()));
	}

	public void setLabelOf(IEdge edge, String text) {
		ILabel label = graph.addLabel(edge, text);
		DefaultLabelStyle style = new DefaultLabelStyle();
		style.setBackgroundPaint(new Color(.83f, .83f, .83f, .8f));
		style.setBackgroundPen(new Pen(new Color(.66f, .66f, .66f, .8f)));
		graph.setStyle(label, style);
	}

	public IEdge addEdge(String id, String from, String to) {
		INode fromNode = null;
		INode toNode = null;
		for (var node : graph.getNodes()) {
				if (node.getTag() instanceof PropertyMap) {
					PropertyMap tag = (PropertyMap) node.getTag();
					if (from.equals(tag.getID())) {
						fromNode = node;
					}
					if (to.equals(tag.getID())) {
						toNode = node;
					}
				}
		}
		if (fromNode != null && toNode != null) {
			IEdge edge = graph.createEdge(fromNode, toNode);
			var tag = new PropertyMap(id);
			edge.setTag(tag);
			return edge;
		} else {
			throw new NotImplementedException("need to create an exception structure");
		}
	}
}
