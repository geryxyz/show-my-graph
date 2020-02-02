package showmygraph.ui;

import javax.swing.JFrame;

import org.apache.commons.lang.NotImplementedException;

import com.yworks.yfiles.geometry.PointD;
import com.yworks.yfiles.geometry.RectD;
import com.yworks.yfiles.graph.GraphItemTypes;
import com.yworks.yfiles.graph.IEdge;
import com.yworks.yfiles.graph.IGraph;
import com.yworks.yfiles.graph.ILabel;
import com.yworks.yfiles.graph.IModelItem;
import com.yworks.yfiles.graph.INode;
import com.yworks.yfiles.graph.styles.DefaultLabelStyle;
import com.yworks.yfiles.layout.organic.OrganicLayout;
import com.yworks.yfiles.utils.IEventListener;
import com.yworks.yfiles.view.GraphComponent;
import com.yworks.yfiles.view.Pen;
import com.yworks.yfiles.view.input.GraphViewerInputMode;
import com.yworks.yfiles.view.input.HoveredItemChangedEventArgs;
import com.yworks.yfiles.view.input.ItemClickedEventArgs;
import com.yworks.yfiles.view.input.ItemHoverInputMode;

import showmygraph.model.PropertyMap;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;

public class GraphWindow {

	private JFrame frame;
	private GraphComponent component = new GraphComponent();
	private IGraph graph;

	public GraphWindow() {
		initialize();
	}

	private PropertyHoverWindow hoverWindow = null;
	
	private void initialize() {
		GraphViewerInputMode inputMode = new GraphViewerInputMode();
		inputMode.setToolTipItems(GraphItemTypes.LABEL_OWNER);
		inputMode.setClickableItems(GraphItemTypes.NODE.or(GraphItemTypes.EDGE));
		inputMode.setFocusableItems(GraphItemTypes.NONE);
		inputMode.setSelectableItems(GraphItemTypes.NONE);
		inputMode.setMarqueeSelectableItems(GraphItemTypes.NONE);
		
		inputMode.addItemLeftClickedListener(new IEventListener<ItemClickedEventArgs<IModelItem>>() {
			
			@Override
			public void onEvent(Object source, ItemClickedEventArgs<IModelItem> args) {
					if (args.getItem().getTag() instanceof PropertyMap) {
						PropertyMap properties = (PropertyMap) args.getItem().getTag();
						PropertyWindow window = new PropertyWindow(properties);
						window.show();
					}
			}
		});
		
		ItemHoverInputMode hoverMode = new ItemHoverInputMode();
		hoverMode.setEnabled(true);
		hoverMode.setHoverItems(GraphItemTypes.EDGE.or(GraphItemTypes.NODE));
		hoverMode.setInvalidItemsDiscardingEnabled(false);
		hoverMode.addHoveredItemChangedListener(new IEventListener<HoveredItemChangedEventArgs>() {
			
			@Override
			public void onEvent(Object source, HoveredItemChangedEventArgs args) {
				if (args.getItem() != null) {
					PropertyMap properties = (PropertyMap) args.getItem().getTag();
					if (hoverWindow != null) {
						hoverWindow.close();
					}
					hoverWindow = new PropertyHoverWindow(properties);
					hoverWindow.show();
				} else {
					if (hoverWindow != null) {
						hoverWindow.close();
					}					
				}
			}
		});
		inputMode.setItemHoverInputMode(hoverMode);
		
		component.setInputMode(inputMode);
		
		this.graph = component.getGraph();
		
		frame = new JFrame();
		frame.setBounds(new Rectangle(0, 0, 500, 500));
		frame.setTitle("Your graph");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());		
		frame.getContentPane().add(this.component, BorderLayout.CENTER);		
	}

	public void show() {
		OrganicLayout layout = new OrganicLayout();
		layout.setNodeSizeConsiderationEnabled(true);
		layout.setMinimumNodeDistance(100);
		graph.applyLayout(layout);
		this.frame.setVisible(true);
		component.fitGraphBounds();
		
		this.frame.getContentPane().addHierarchyBoundsListener(new HierarchyBoundsListener() {
			
			@Override
			public void ancestorResized(HierarchyEvent e) {
				component.fitGraphBounds();				
			}
			
			@Override
			public void ancestorMoved(HierarchyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
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
