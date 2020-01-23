package showmygraph.ui;

import javax.swing.JFrame;

import org.graphstream.graph.Graph;
import org.graphstream.ui.swingViewer.View;
import org.graphstream.ui.swingViewer.Viewer;
import org.graphstream.ui.swingViewer.ViewerListener;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.Dimension;
import javax.swing.border.BevelBorder;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import javax.swing.JTable;
import java.awt.Window.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.awt.Frame;

public class MainWindow {

	private JFrame frmYourGraph;
	private Graph graph;
	private JTable nodePropertiesTable;
	private JPanel graphPanel;

	/**
	 * Create the application.
	 * @param graph 
	 */
	public MainWindow(Graph graph) {
		this.graph = graph;
		this.graph.addAttribute("ui.stylesheet", "node { size: 10pt, 15pt; }");
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmYourGraph = new JFrame();
		frmYourGraph.setTitle("Your graph");
		frmYourGraph.setBounds(100, 100, 450, 300);
		frmYourGraph.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmYourGraph.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel inspectPanel = new JPanel();
		inspectPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		inspectPanel.setPreferredSize(new Dimension(300, 100));
		inspectPanel.setMinimumSize(new Dimension(100, 100));
		inspectPanel.setLayout(new BorderLayout());
		frmYourGraph.getContentPane().add(inspectPanel, BorderLayout.EAST);
		
		var selected = graph.getNode(0);
		
		nodePropertiesTable = new JTable();
		inspectPanel.add(new JScrollPane(nodePropertiesTable), BorderLayout.CENTER);
		nodePropertiesTable.setRowSelectionAllowed(false);
		nodePropertiesTable.setModel(new TableModel() {
			
			@Override
			public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void removeTableModelListener(TableModelListener l) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				List<String> attributeKeys = new ArrayList<String>((Collection<? extends String>) selected.getAttributeKeySet());
				String key = attributeKeys.get(rowIndex);
				if (columnIndex == 0) {
					return key;
				} else if (columnIndex == 1) {
					return selected.getAttribute(key);
				}
				return "hello";
			}
			
			@Override
			public int getRowCount() {
				return selected.getAttributeCount();
			}
			
			@Override
			public String getColumnName(int columnIndex) {
				switch (columnIndex) {
					case 0:
						return "name";
					case 1:
						return "value";
				}
				return null;
			}
			
			@Override
			public int getColumnCount() {
				return 2;
			}
			
			@Override
			public Class<?> getColumnClass(int columnIndex) {
				return getValueAt(0, columnIndex).getClass();
			}
			
			@Override
			public void addTableModelListener(TableModelListener l) {
				// TODO Auto-generated method stub				
			}
		});
		
		graphPanel = new JPanel();
		graphPanel.setLayout(new BorderLayout());
		frmYourGraph.getContentPane().add(graphPanel, BorderLayout.CENTER);
		Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
		viewer.enableAutoLayout();
		
		View view = viewer.addDefaultView(false);
		graphPanel.add(view, BorderLayout.CENTER);
	}

	public void show() {
		this.frmYourGraph.setVisible(true);
	}
}

class Clicks implements ViewerListener {

	@Override
	public void viewClosed(String viewName) { }

	@Override
	public void buttonPushed(String id) { }

	@Override
	public void buttonReleased(String id) {	}
	
}
