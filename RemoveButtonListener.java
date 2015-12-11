
import java.awt.event.*;


public class RemoveButtonListener implements ActionListener
{
	private NotesWindow window;
	private DynamicFileTree tree;

	public RemoveButtonListener(NotesWindow centralControl, DynamicFileTree tree)
	{
		super();
		this.window = centralControl;
		this.tree = tree;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(window.popUpYesOrNo("Are you sure you wish to deleted the selected file or folder?", "Are You Sure"))
		{
			tree.removeCurentSelectedFEFromTree();
		}
	}
}