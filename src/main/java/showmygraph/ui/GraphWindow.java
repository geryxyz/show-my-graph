package showmygraph.ui;

import javax.swing.JFrame;

import com.yworks.yfiles.graph.GraphItemTypes;
import com.yworks.yfiles.graph.IGraph;
import com.yworks.yfiles.graph.IModelItem;
import com.yworks.yfiles.layout.organic.OrganicLayout;
import com.yworks.yfiles.utils.IEventListener;
import com.yworks.yfiles.view.GraphComponent;
import com.yworks.yfiles.view.input.GraphViewerInputMode;
import com.yworks.yfiles.view.input.HoveredItemChangedEventArgs;
import com.yworks.yfiles.view.input.ItemClickedEventArgs;
import com.yworks.yfiles.view.input.ItemHoverInputMode;

import showmygraph.architecture.EmptyContext;
import showmygraph.architecture.SingleValueContext;
import showmygraph.architecture.Sink;
import showmygraph.model.PropertyMap;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;

public class GraphWindow extends Sink<SingleValueContext<IGraph>> {

	private JFrame frame;
	private PropertyHoverWindow hoverWindow = null;
	private GraphComponent component = new GraphComponent();

	public void show(IGraph graph) {
		component.setGraph(graph);

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
		
		frame = new JFrame();
		frame.setBounds(new Rectangle(0, 0, 500, 500));
		frame.setTitle("Your graph");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());		
		frame.getContentPane().add(this.component, BorderLayout.CENTER);		

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

	@Override
	protected EmptyContext execute(SingleValueContext<IGraph> context) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					show(context.getValue());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		return EmptyContext.instance;
	}
}
