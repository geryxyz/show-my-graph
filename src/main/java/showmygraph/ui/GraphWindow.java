package showmygraph.ui;

import javax.swing.JFrame;

import org.graphstream.graph.Graph;
import org.graphstream.ui.swingViewer.View;
import org.graphstream.ui.swingViewer.Viewer;
import org.graphstream.ui.swingViewer.ViewerListener;
import org.graphstream.ui.swingViewer.ViewerPipe;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GraphWindow {

	private JFrame frame;
	private Graph graph;

	public GraphWindow(Graph graph) {
		this.graph = graph;
		//this.graph.addAttribute("ui.stylesheet", "node { size: 10pt, 15pt; }");
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(new Rectangle(0, 0, 500, 500));
		frame.setTitle("Your graph");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());
		
		Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
		viewer.enableAutoLayout();
		View view = viewer.addDefaultView(false);
		view.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				var element = view.findNodeOrSpriteAt(e.getX(), e.getY());
				System.out.println(element);
			}
		});
		frame.getContentPane().add(view, BorderLayout.CENTER);
	}

	public void show() {
		this.frame.setVisible(true);
	}
}
