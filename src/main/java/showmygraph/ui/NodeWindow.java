package showmygraph.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.graphstream.graph.Node;
import java.awt.Window.Type;

public class NodeWindow {

	private JFrame frame;
	private Node selected;
	private JTable nodePropertiesTable;

	public void show() {
		this.frame.setVisible(true);
	}

	public NodeWindow(Node selected) {
		this.selected = selected;
		this.initialize();
	}

	private void initialize() {
		this.frame = new JFrame();
		frame.setType(Type.UTILITY);
		this.frame.setBounds(100, 100, 450, 300);
		this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		this.nodePropertiesTable = new JTable();
		this.frame.getContentPane().add(nodePropertiesTable);
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
				return "no attribute";
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
	}

}
