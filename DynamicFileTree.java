
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;


import javax.swing.event.TreeSelectionEvent;

import javax.swing.tree.TreePath;

import java.util.Enumeration;

/*
*	This Class is a fully equipped file tree that can respond to edits in name,
	file content and file tree structure. It updates the files and then calls
	StaticFileTree.update() to update its tree model
*/
public class DynamicFileTree extends StaticFileTree implements TreeModelListener
{
	//============ATTRIBUTES===========
	private FileElement currFESelected = null;
	private FileElement pastFESelected = null;
	private final NotesWindow window;
	//============CONSTRUCTOR===========
	public DynamicFileTree(DefaultMutableTreeNode root, NotesWindow inWindow)
	{
		super(root);

		//so its actually usable
		this.setEditable(true);


	    //very important. By registering self as listener, this can respond to changes
		this.treeModel.addTreeModelListener(this);

		//window
		this.window = inWindow;

	}

	//========GETTERS AND SETTINGS===========
	public FileElement getSelectedFileElement()
	{
		return currFESelected;
	}
	public FileElement getPastSelectedFileElement()
	{
		return pastFESelected;
	}

	//=============OVERRIDES========
	//the ones i don't use
	public void treeNodesInserted(TreeModelEvent e) {}
    public void treeNodesRemoved(TreeModelEvent e) {}
    public void treeStructureChanged(TreeModelEvent e) {}

	//When a tree nodes's name is modified this is called
	@Override
    public void treeNodesChanged(TreeModelEvent e)
    {

     	DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) getLastSelectedPathComponent();
    	//System.out.println("tree nodes changed: + " + selectedNode.getUserObject() instanceof String);

    	System.out.println(selectedNode.getUserObject().toString());
    	if(!currFESelected.setName(selectedNode.getUserObject().toString()))
    	{
    		window.showError("Dude, there's already a file called this....", "File Already Exists");
    	}
    	update();

    	currFESelected = null;
    }

    /*	when selection changes this is called we could use valueChanged but
    	the problem is we must update pastFE and currFe before the listeners
    	receive the event.
    */
	@Override
	protected void fireValueChanged(TreeSelectionEvent e)
	{
		pastFESelected = currFESelected;
		if(getLastSelectedPathComponent() != null)
		{
			DefaultMutableTreeNode curNode = ( (DefaultMutableTreeNode) getLastSelectedPathComponent() );
			//System.out.println(curNode.getUserObject() instanceof FileElement);
			//System.out.println(curNode.getUserObject());
			if(curNode.getUserObject() instanceof FileElement)
			{
				currFESelected = (FileElement)curNode.getUserObject();
			}
		} else
		{
			currFESelected = null;
		}
		super.fireValueChanged(e);
	}

	@Override
	public void update()
	{
		String expandedStuff = TreeUtil.getExpansionState(this); //TODO: understand how to include all expanded rows
		super.update();

		//registered to old tree but not new one made in super.update()
		this.treeModel.addTreeModelListener(this);

		//expand paths that were previously expanded
		TreeUtil.setExpansionState(this, expandedStuff);


	}

	//=======================OTHER METHODS=================

	public void addFileElementToTree(DefaultMutableTreeNode parent, String name, int typeId) throws Exception
	{
		FileElement newFile = null;

		if(parent == null) throw new Exception("No Folder is selected");
		if( parent.getUserObject() == null) throw new Exception("Parent has no userObject");
		System.out.println("asdfasdf");
		newFile = new FileElement((FileElement)parent.getUserObject(), "5"+name, (byte)typeId, (byte) 5);
		update();
	}

	public boolean removeCurrentSelectedFEFromTree()
	{
		DefaultMutableTreeNode nodeToDelete = (DefaultMutableTreeNode) getLastSelectedPathComponent();
		FileElement feToDelete = (FileElement) nodeToDelete.getUserObject();
		System.out.println("What is this"+ feToDelete.toString());

		try{
			feToDelete.deleteThisFile();
		}catch(Exception e){
			System.out.println("COOOOOOOLLLLL");
		}
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) nodeToDelete.getParent();
		if(parent != null)
		{
			parent.remove(nodeToDelete);

		}
		update();
		return true;
	}


}