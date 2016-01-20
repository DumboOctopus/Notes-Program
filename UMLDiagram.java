import javax.swing.*;
import java.awt.Insets;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.FlowLayout;
import javax.swing.text.JTextComponent;
import javax.swing.border.*;
import javax.swing.border.EtchedBorder;

import java.util.ArrayList;

public class UMLDiagram extends JComponent implements ComponentListener
{
	private boolean isBeingDragged = false;
	private boolean selected = false;

	private static boolean allowShrinking = false;
	private int fontSize = 12;

	//storing relationship info so Display can do stuff :)
	private UMLDiagram classExtending;
	private ArrayList<UMLDiagram> interfacesImplementing;
	private UMLDiagram[] classesUsing;

	//Diagrams stuff
	private JTextField classTypeLabel;
	private JTextField classNameLabel;
	private JTextArea classFieldSummary;
	private JTextArea classMethodSummary;

	//handle at top
	private int sizeOfHandle = 20;

	//changing color when selected
	private Border unselectedBorder;
	private Border selectedBorder;
	private Color unselectedColor = Color.BLUE;
	private Color selectedColor = new Color(153, 204, 255);

	public UMLDiagram(int x, int y)
	{
		super();
		selectedBorder = BorderFactory.createMatteBorder(2,2,2,2, selectedColor);
		unselectedBorder = BorderFactory.createMatteBorder(2,2,2,2, unselectedColor);

		setBounds(x,y, 204, 400); //TO CHANGE
    	setBorder(unselectedBorder);

    	//interface
    	interfacesImplementing = new ArrayList<UMLDiagram>();

    	//REPLACE WITH SPRING LAYOUT :)
    	SpringLayout layout = new SpringLayout();
    	setLayout(layout);
    	setUpLabels(204, layout);
    	setOpaque(true);
	}
	//================HELp set up constructor: )================
	public void setUpLabels(int width, SpringLayout layout)
	{
    	int columns = width/fontSize; 

    	classNameLabel = new JTextField(columns);
    	classNameLabel.setBorder(BorderFactory.createMatteBorder(2,2,2,2, unselectedColor));
    	classNameLabel.addComponentListener(this);
    	classNameLabel.setHorizontalAlignment(JTextField.CENTER);

    	classTypeLabel = new JTextField((columns));
    	classTypeLabel.setBorder(BorderFactory.createMatteBorder(0,2,2,2, unselectedColor));
    	classTypeLabel.addComponentListener(this);
    	classTypeLabel.setHorizontalAlignment(JTextField.CENTER);


    	classFieldSummary = new JTextArea(1, columns);
    	classFieldSummary.setBorder(BorderFactory.createMatteBorder(2,2,2,2, unselectedColor));
    	classFieldSummary.setLineWrap(false);
    	classFieldSummary.addComponentListener(this);

    	classMethodSummary = new JTextArea(2, columns	);
    	classMethodSummary.setBorder(BorderFactory.createMatteBorder(2,2,2,2, unselectedColor));
    	classMethodSummary.setLineWrap(false);
    	classMethodSummary.addComponentListener(this);

    	add(classNameLabel);
    	add(classTypeLabel);
    	add(classFieldSummary);
    	add(classMethodSummary);


    	//className Label
    	layout.putConstraint(SpringLayout.WEST, classNameLabel,
    				0,
    				SpringLayout.WEST, this);
    	layout.putConstraint(SpringLayout.NORTH, classNameLabel,
                    sizeOfHandle,
                    SpringLayout.NORTH, this);
    	//classTypeLabel
    	// layout.putConstraint(SpringLayout.WEST, classTypeLabel,
    	// 			0,
    	// 			SpringLayout.WEST, this);
    	layout.putConstraint(SpringLayout.NORTH, classTypeLabel,
                    0,
                    SpringLayout.SOUTH, classNameLabel);
    	//classFieldSummary
    	layout.putConstraint(SpringLayout.WEST, classFieldSummary,
    				0,
    				SpringLayout.WEST, this);
    	layout.putConstraint(SpringLayout.NORTH, classFieldSummary,
                    0,
                    SpringLayout.SOUTH, classTypeLabel);
    	//class Method Summary
    	layout.putConstraint(SpringLayout.WEST, classMethodSummary,
    				0,
    				SpringLayout.WEST, this);
    	layout.putConstraint(SpringLayout.NORTH, classMethodSummary,
                    0,
                    SpringLayout.SOUTH, classFieldSummary);
	}

	//=====================GETTERS=========================
	//------Used by mousePressed call in UMLDiagramDisplay
	public int getRight(){return getX() + getWidth();}
	public int getLeft() {return getX();}
	public int getTop() {return getY();}
	public int getBottom () {return getWidth() + getY();}
	public boolean getIsBeingDragged(){return isBeingDragged;}

	//------For the JtextAreas
	public String getClassMethodSummary(){return classMethodSummary.getText();}
	public String getClassFeildSummary(){return classFieldSummary.getText();}
	public String getClassNameLabel(){return classNameLabel.getText();}

	//------For extendings
	public UMLDiagram getClassExtending(){return classExtending;}
	public ArrayList<UMLDiagram> getInterfacesImplementing() {return interfacesImplementing;}


	//=====================SETTERS================================
	//------Used by mousePressed call in UMLDiagramDisplay
	public void setIsBeingDragged(boolean a){isBeingDragged = a;}
	public void setSelected(boolean a){
		if(selected == a) return;
		selected = a;
		if(selected) setBorder(selectedBorder);
		else setBorder(unselectedBorder);
	}
	public void setX(int x){setBounds(x, getY(), getWidth(), getHeight());}
	public void setY(int y){setBounds(getX(), y, getWidth(), getHeight());}

	//-----for the JtextAreas stuf
	public void setClassMethods(String s){classMethodSummary.setText(s);resizeUMLDiagram();}
	public void setClassFields(String s){classFieldSummary.setText(s);resizeUMLDiagram();}
	public void setClassName(String s){classNameLabel.setText(s);resizeUMLDiagram();}
	public void setClassType(String s){classTypeLabel.setText(s);resizeUMLDiagram();}
	//------For extendings
	public void setClassExtending(UMLDiagram s){classExtending = s;}
	public void addInterfacesImplementing(UMLDiagram s) {interfacesImplementing.add(s);}

	///================PAINTING==============================
	protected void paintComponent(Graphics g)
	{
		if(selected) g.setColor(selectedColor);
		else g.setColor(unselectedColor);
		g.fillRect(0, 0, getWidth()-1, getHeight() -1);
		//then it paints its children, the JTextAreas and Jlabel
	}
	public void resizeUMLDiagram()
	{
		int newHeight = classMethodSummary.getHeight() + classFieldSummary.getHeight() + classNameLabel.getHeight() + classTypeLabel.getHeight() + sizeOfHandle + 10; 
        int newWidth = Math.max(classNameLabel.getColumns()/fontSize, Math.max(classFieldSummary.getWidth(), classMethodSummary.getWidth()));
        newWidth = Math.max(newWidth, classTypeLabel.getColumns()/fontSize);
        if(!allowShrinking) newWidth = Math.max(getSize().width, newWidth);


        setBounds(getX(), getY(), newWidth + 1, newHeight);


        int newColumns = newWidth/fontSize;
        classTypeLabel.setColumns(newColumns);
        classMethodSummary.setColumns(newColumns);
        classFieldSummary.setColumns(newColumns);
        classNameLabel.setColumns(newColumns);

	}

	//=======================COMPONENT LISTENERS=================
	public void componentHidden(ComponentEvent e) {}
    public void componentMoved(ComponentEvent e) {}
    public void componentShown(ComponentEvent e) {}
    public void componentResized(ComponentEvent e) {
        resizeUMLDiagram();
    }


	//======================ToSTRING=================================
	public String toString()
	{
		return "UMLDiagram:["+getX()+", "+getWidth() +";" + getRight() + ", "+getBottom()+"]";
	}
}