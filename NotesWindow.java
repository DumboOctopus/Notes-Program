import java.awt.*;

import javax.swing.*;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class NotesWindow extends JFrame
{

	private JTextArea taNotes;
	private JScrollPane scroll;
	private DefaultMutableTreeNode root;
	private JButton bu_add;
	private JButton bu_remove;
	private DynamicFileTree tree;



	//=================CONSTRUCTOR
	public NotesWindow()
	{
		super("NNotes Programm");

		Container cp = getContentPane();
	    cp.setLayout(new BorderLayout(50, 10));

	    //----CURRENT NOTE
	    taNotes = new JTextArea(5, 20);
	    scroll = new JScrollPane(taNotes);
	    cp.add(scroll, BorderLayout.CENTER);
	    try{
		   	//----TREE
		    DefaultMutableTreeNode root = new DefaultMutableTreeNode(new FileElement("All Notes"));
			tree = new DynamicFileTree(root, this);

			tree.addTreeSelectionListener(new TreeSelectionListener(){
		       public void valueChanged(TreeSelectionEvent e)
			   {
				   setUpNextAndSaveCurrentNote();
		       }
		    });

	    	cp.add(tree, BorderLayout.LINE_START);
	    	//---BUTTONS
		    bu_add = new JButton("New");
		    bu_remove = new JButton("Remove");

		    AddButtonListener l = new AddButtonListener(this, tree);
		    bu_add.addActionListener(l);

		    RemoveButtonListener lr = new RemoveButtonListener(this, tree);
		    bu_remove.addActionListener(lr);

		    cp.add(bu_add, BorderLayout.NORTH);
		    cp.add(bu_remove, BorderLayout.SOUTH);
	    } catch(Exception e)
	    {
    	   	JLabel label = new JLabel(e.getMessage());
         	cp.add(label, BorderLayout.LINE_START);
         	System.out.println(e.getMessage());
  		}

		//window closing
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				FileElement currFile = tree.getSelectedFileElement();
				if(currFile != null)
					currFile.setBody(taNotes.getText());
			}
		});

		setSize(800, 600);
		setVisible(true);

	}

	public boolean popUpYesOrNo (String message, String title)
	{
		Object[] options = {"Yes", "No"};
		int n = JOptionPane.showOptionDialog(
			this,
		    message,
		    title,
		    JOptionPane.YES_NO_OPTION,
		    JOptionPane.QUESTION_MESSAGE,
		    null,
		    options,
		    options[1]);
		return n == 0;
	}

	public void showError (String message, String title)
	{
		JOptionPane.showMessageDialog(this,
			    message,
			    title,
			    JOptionPane.ERROR_MESSAGE);
	}
	public static void makeNewWindow()
	{
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new NotesWindow(); // Let the constructor do the job
			}
		});
	}
	public void setUpNextAndSaveCurrentNote()
	{
		FileElement currFile = tree.getSelectedFileElement();
		FileElement pastFile = tree.getPastSelectedFileElement();
		if(pastFile != null)
			pastFile.setBody(taNotes.getText());

		taNotes.setText("");
		if(currFile != null)
			taNotes.setText(currFile.getBody());
	}

}