
import java.awt.event.*;
import java.io.File;
import javax.swing.*;

import javax.swing.tree.DefaultMutableTreeNode;


public class AddButtonListener implements ActionListener
{
	private NotesWindow window;
	private DynamicFileTree tree;

	public AddButtonListener(NotesWindow centralControl, DynamicFileTree tree)
	{
		super();
		this.window = centralControl;
		this.tree = tree;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		int choice = askForNoteOrFolder();
		switch(choice)
		{
			case 0:
				break;
			case 1:
				makeFolder();
				break;
			case 2:
				makeNote();
				break;
			case 3:
				NotesWindow.makeNewWindow();
		}
	}
	private int askForNoteOrFolder()
	{
		Object[] options = {"Cancel","Folder", "Note", "Window"};
		int n = JOptionPane.showOptionDialog(
			this.window,
		    "What would you like to create?",
		    "New Something",
		    JOptionPane.YES_NO_CANCEL_OPTION,
		    JOptionPane.QUESTION_MESSAGE,
		    null,
		    options,
		    options[0]);
		return n;
	}

	private void makeFolder()
	{
		makeFileElement("Name for new Folder: ", FileElement.FOLDER);
	}
	private void makeNote()
	{
		makeFileElement("Name for new Note: ", FileElement.NOTE);
	}

	private void makeFileElement(String dialog, int typeOfFE)
	{
		String newNameOfFolder = JOptionPane.showInputDialog(dialog);
		if(newNameOfFolder == null)
		{
			window.showError("Thats not exactly a name", "Error");
			return;
		}

		DefaultMutableTreeNode parentNode = ( (DefaultMutableTreeNode) tree.getLastSelectedPathComponent() );
		if(parentNode == null)
		{
			parentNode = tree.getRoot();
		}

		FileElement parentFile;
		try{

			parentFile = (FileElement) parentNode.getUserObject();
		} catch (Exception e)
		{
			window.showError(e.getMessage(), "I tried...");
			//e.printStackTrace();
			return;
		}

		File tmp = new File(parentFile, "5"+newNameOfFolder);
		System.out.println(tmp.exists());
		if(tmp.exists())
		{
			window.showError("File name is already used in the current folder, please choose another name", "File Already Exists");

			return;
		}

		if(!parentFile.isDirectory())
		{
			window.showError(parentFile.getName()+ " is not a folder...and you can only make a note under a folder", "Error");
			return;
		}
		try {
			tree.addFileElementToTree(parentNode, newNameOfFolder, typeOfFE);
		} catch(Exception e)
		{
			window.showError(e.getMessage(), "I Tried...");
		}
	}

}
