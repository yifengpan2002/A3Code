/*
 *  ============================================================================================
 *  A3.java : Extends JFrame and contains a panel where shapes move around on the screen.
 *  YOUR UPI: ypan179
 *  ============================================================================================
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.ArrayList;
import javax.swing.tree.*;

public class A3  extends JFrame {
	private AnimationViewer panel;  // panel for bouncing area
	JButton fillButton, addNodeButton, removeNodeButton;
	JTree tree;
	JList jlist;
	JComboBox<ShapeType> shapesComboBox;
	JComboBox<PathType> pathComboBox;
	JTextField heightTextField, widthTextField, textTextField;
	JComboBox<ShapeType> InnerShapesComboBox;
	/** main method for A1 */
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new A3();
			}
		});
	}
	/** constructor to initialise components */
	public A3() {
		super("Bouncing Application");
		JPanel mainPanel = setUpMainPanel();
		add(mainPanel, BorderLayout.CENTER);
		add(setUpToolsPanel(), BorderLayout.NORTH);
		addComponentListener(
			new ComponentAdapter() { // resize the frame and reset all margins for all shapes
				public void componentResized(ComponentEvent componentEvent) {
					panel.resetMarginSize();
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = getSize();
		//setLocation((d.width - frameSize.width) / 2, (d.height - frameSize.height) / 2);
		pack();
		setVisible(true);
	}
	public JPanel setUpMainPanel() {
		JPanel mainPanel = new JPanel();
		panel = new AnimationViewer(true);
		panel.setPreferredSize(new Dimension(Shape.DEFAULT_MARGIN_WIDTH, Shape.DEFAULT_MARGIN_HEIGHT));
		JPanel dataPanel = setUpDataPanel();
		dataPanel.setPreferredSize(new Dimension(Shape.DEFAULT_MARGIN_WIDTH, Shape.DEFAULT_MARGIN_HEIGHT));
		JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, dataPanel, panel);
		mainSplitPane.setResizeWeight(0.5);
		mainSplitPane.setOneTouchExpandable(true);
		mainSplitPane.setContinuousLayout(true);
		mainPanel.add(mainSplitPane);
		return mainPanel;
	}
	/** Set up the tools panel
		* @return toolsPanel		the Panel */
	public JPanel setUpDataPanel() {
		JPanel treePanel = new JPanel(new BorderLayout());
		treePanel.setPreferredSize(new Dimension(Shape.DEFAULT_MARGIN_WIDTH, Shape.DEFAULT_MARGIN_HEIGHT));

		//uncomment the following for part 2
		tree = new JTree(panel);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setShowsRootHandles(true);
		tree.addTreeSelectionListener(new MyTreeSelectionListener());

		JScrollPane treeScrollpane = new JScrollPane(tree);
		treePanel.add(treeScrollpane,BorderLayout.CENTER);
		JPanel treeButtonsPanel = new JPanel();

		//uncomment the following for part 2
		addNodeButton = new JButton("Add Node");
		removeNodeButton = new JButton("Remove Node");
		addNodeButton.addActionListener( new AddActionListener());
		removeNodeButton.addActionListener( new RemoveActionListener());
		treeButtonsPanel.add(addNodeButton);
		treeButtonsPanel.add(removeNodeButton); 
		treePanel.add(treeButtonsPanel,BorderLayout.NORTH);
		JPanel dataPanel = new JPanel();
		dataPanel.add(treePanel);
		return dataPanel;
	}
	//add the MyTreeSelectionListener inner class
	class MyTreeSelectionListener implements TreeSelectionListener{
		public void valueChanged(TreeSelectionEvent e){
			Object node = tree.getLastSelectedPathComponent();
			if(node != null){
				tree.expandPath(tree.getSelectionPath());
			}
		}
	}
	//add the RemoveActionListener inner class
	class RemoveActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			Object node = tree.getLastSelectedPathComponent();
			if(node != panel.getRoot() && node != null){
				panel.removeNodeFromParent((Shape) node);
			}else if(node == panel.getRoot()){
				JOptionPane.showMessageDialog(null, "ERROR: Must not remove the root.");
			}else{
				JOptionPane.showMessageDialog(null, "ERROR: No node selected.");
			}
		}
	}
	//add the AddActionListener inner class
	class AddActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			Object node = tree.getLastSelectedPathComponent();
			if(node instanceof NestedShape){
				panel.addShapeNode((NestedShape) tree.getLastSelectedPathComponent());
			}else if(node instanceof Shape){
				JOptionPane.showMessageDialog(null, "ERROR: Must select a NestedShape node.");
				
			}else{
				JOptionPane.showMessageDialog(null, "ERROR: No node selected.");
			}
		}
	}


	/** Set up the tools panel
	* @return toolsPanel		the Panel */
	public JPanel setUpToolsPanel() {
		shapesComboBox = new JComboBox<ShapeType>();
		shapesComboBox.setModel(new DefaultComboBoxModel<ShapeType>(ShapeType.values()));
		shapesComboBox.setToolTipText("Set shape");
		shapesComboBox.addActionListener( new ShapeActionListener()) ;
		pathComboBox = new JComboBox<PathType>();
		pathComboBox.setModel(new DefaultComboBoxModel<PathType>(PathType.values()));
		pathComboBox.addActionListener( new PathActionListener());
		//Set up the height TextField
		heightTextField = new JTextField(""+ Shape.DEFAULT_HEIGHT);
		heightTextField.setToolTipText("Set Height");
		heightTextField.addActionListener( new HeightActionListener());
		//Set up the width TextField
		widthTextField = new JTextField(""+ Shape.DEFAULT_WIDTH);
		widthTextField.setToolTipText("Set Width");
		widthTextField.addActionListener( new WidthActionListener());
		//Set up the text TextField
		textTextField = new JTextField("" + Shape.DEFAULT_TEXT);
		textTextField.setToolTipText("Set Text");
		textTextField.addActionListener( new TextActionListener());
		//Set up the fill colour button
		fillButton = new JButton("Fill");
		fillButton.setToolTipText("Set Fill Color");
		fillButton.setForeground(panel.getCurrentColor());
		fillButton.addActionListener( new FillActionListener());

		JPanel toolsPanel = new JPanel();
		toolsPanel.setLayout(new BoxLayout(toolsPanel, BoxLayout.X_AXIS));
		toolsPanel.add(new JLabel(" Shape: ", JLabel.RIGHT));
		toolsPanel.add(shapesComboBox);
		toolsPanel.add(new JLabel(" Path: ", JLabel.RIGHT));
		toolsPanel.add(pathComboBox);
		toolsPanel.add(new JLabel(" Width: ", JLabel.RIGHT));
		toolsPanel.add(widthTextField);
		toolsPanel.add( new JLabel(" Height: ", JLabel.RIGHT));
		toolsPanel.add(heightTextField);
		toolsPanel.add( new JLabel(" Text: ", JLabel.RIGHT));
		toolsPanel.add(textTextField);
		toolsPanel.add(fillButton);
		return toolsPanel;
	}
	class ShapeActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			panel.setCurrentShapeType(shapesComboBox.getSelectedIndex());
		}
	}
	class PathActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			panel.setCurrentPathType(pathComboBox.getSelectedIndex());
		}
	}
	class WidthActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				int newValue = Integer.parseInt(widthTextField.getText());
				if (newValue > 0 && newValue < Shape.DEFAULT_MARGIN_WIDTH/2 ) // if the value is valid, then change the current height
					panel.setCurrentWidth(newValue);
				else
					widthTextField.setText(panel.getCurrentWidth()+""); //undo the changes
			} catch (Exception ex) {
				widthTextField.setText(panel.getCurrentWidth()+""); //if the number entered is invalid, reset it
			}
		}
	}
	class HeightActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				int newValue = Integer.parseInt(heightTextField.getText());
				if (newValue > 0 && newValue < Shape.DEFAULT_MARGIN_HEIGHT/2 ) // if the value is valid, then change the current height
					panel.setCurrentHeight(newValue);
				else
					heightTextField.setText(panel.getCurrentHeight()+""); //undo the changes
			} catch (Exception ex) {
				heightTextField.setText(panel.getCurrentHeight()+""); //if the number entered is invalid, reset it
			}
		}
	}
	class TextActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				String text = textTextField.getText();
				panel.setCurrentText(text);
			} catch (Exception ex) {
				textTextField.setText(panel.getCurrentText()+""); //if the number entered is invalid, reset it
			}
		}
	}
	class FillActionListener implements ActionListener {
		public void actionPerformed( ActionEvent e) {
			Color newColor = JColorChooser.showDialog(panel, "Fill Color", panel.getCurrentColor());
			if ( newColor != null) {
				fillButton.setForeground(newColor);
				panel.setCurrentColor(newColor);
			}
		}
	}
}