import javax.swing.JTree;

import javax.swing.tree.TreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import java.util.InputMismatchException;
import java.io.File;

import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/* 	This Class simply expresses a folder and its content as a JTree,
	it does have ability to update but only when called by another class.
*/
public class StaticFileTree extends JTree 
{
	protected final DefaultMutableTreeNode root;
	private final FileElement rootFE;

	public StaticFileTree (DefaultMutableTreeNode in_root) throws InputMismatchException
	{
		super(in_root);

		//sets up tree nodes
		this.root = in_root;
		Object rootObj = in_root.getUserObject();

		//if person put node who didn't contain a FileElement
		if( !(rootObj instanceof FileElement) )
			throw new InputMismatchException("StaticFileTree 30: root.getUserObject() not of type FileElement");

		getFilesInFiles((FileElement)rootObj, this.root);
		this.rootFE = (FileElement)in_root.getUserObject();

		//general tree stuff
		this.setShowsRootHandles(true);
	    this.setRootVisible(false);
	    this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION); //only one element selcted at a time
		this.setExpandedState(new TreePath(root.getPath()), true);
		this.setEditable(false);
	}


	private void getFilesInFiles (File folder, DefaultMutableTreeNode curNode) 
	{
		//checks if this even makes sense
		if(!folder.exists())
		{
			System.out.println("ERROR: StaticFileTree did not construct propery");
		}

		//list files inside folder
		File[] contentsOfFolder = folder.listFiles();

		if(contentsOfFolder != null)
		{
			for (File f : contentsOfFolder) {

				if (f.isFile() && !f.getName().equals(".DS_Store")) {
					//if its a file
					try {
						//creates new node and adds it to curNode
						FileElement fAsFE = new FileElement(f);
						DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(fAsFE);
						curNode.add(newNode);
					} catch (Exception e) {
						//File Element constructors throws File not found
						e.printStackTrace();
					}
				} else if (f.isDirectory() && !f.getName().equals(".DS_Store")) {
					//ifs its a directory
					try {
						//creates new node and adds to curr node
						FileElement fAsFE = new FileElement(f);
						DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(fAsFE);
						curNode.add(newNode);

						//recursion :), curr node is now the node just created
						getFilesInFiles(f, newNode);

						if(newNode.getChildCount() == 0)
							newNode.add(new DefaultMutableTreeNode(""));
					} catch (Exception e) {
						//impossible to reach by logic but we have to have it
						e.printStackTrace();
					}
				}
			}
		}

	}

	public void update()
	{
		//clearing children (root must always stay same so not nessary to destroy tree)
		this.root.removeAllChildren();
		//recreates children
		getFilesInFiles( this.rootFE, this.root);

		//makes tree model based on new root
		TreeModel newModel = new DefaultTreeModel(this.root);

		//model = new model
		setModel(newModel);
	}

	public DefaultMutableTreeNode getRoot()
	{
		return this.root;
	}

}