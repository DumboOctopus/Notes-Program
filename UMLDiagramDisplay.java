import javax.swing.*;
import java.awt.Insets;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.FlowLayout;
import java.util.ArrayList;

/*
replace UMLDiagram[] arr with call to addComponent in JComponenet class.
Then fix all the mouseHanding stuff
and the renderring
and UMLDiagram class
and pretty much everythign
*/
public class UMLDiagramDisplay extends JComponent
{

    private UMLDiagram currDragged= null;
    private UMLDiagram currSelected = null;
	private boolean pastsInit = false;
	

	public UMLDiagramDisplay(UMLDiagram[] arr)
	{
        this();
		for(UMLDiagram s: arr)
		{
			if(s != null) add(s);
		}

	}
	public UMLDiagramDisplay()
	{
		super();
        setBorder(BorderFactory.createMatteBorder(1,5,5,1, Color.BLUE));
		setUpMouseListener();
	}

	public void setUpMouseListener()
	{
		addMouseMotionListener(new MouseMotionListener() {
			private int pastmx = -1;
			private int pastmy = -1;

		    @Override
		    public void mouseDragged(MouseEvent e) {
		      	if(currDragged != null)
		      	{
		      		if(pastsInit)
		      		{
		      			currDragged.setX(currDragged.getX() + (e.getXOnScreen() - pastmx));
		      			currDragged.setY(currDragged.getY() + (e.getYOnScreen() - pastmy));
		      			pastmx = e.getXOnScreen();
		      			pastmy = e.getYOnScreen();

		      			//THIS IS INNNEFFICENT
		      			//	currDragged.repaint();
		      			UMLDiagramDisplay.this.repaint();
		      		}
		      		else
		      		{

		      			pastmx = e.getXOnScreen();
		      			pastmy = e.getYOnScreen();
		      			pastsInit = true;
		      		}
		       	} else
		       	{
		       		pastsInit = false;
		       	}

	    	}

		    @Override
		    public void mouseMoved(MouseEvent e) { }
		});
		addMouseListener(new MouseListener() {
			private boolean oldMousePressed = false;
	     	@Override
	     	public void mouseClicked(MouseEvent e) { 
	     		for(int i = 0, c = getComponentCount(); i < c; i++)
      	  		{
      	  			if(!(getComponent(i) instanceof UMLDiagram)) continue;
      	  			UMLDiagram s = (UMLDiagram) getComponent(i);
      	  			int mx = e.getXOnScreen();
      	  			int my = e.getYOnScreen();

      	  			Point mousePoint = new Point(mx, my);
      	  			SwingUtilities.convertPointFromScreen(mousePoint, UMLDiagramDisplay.this);

      	  			if(s.getLeft() < mousePoint.getX() && mousePoint.getX() < s.getRight() && s.getTop() < mousePoint.getY() && mousePoint.getY() < s.getBottom())
      	  			{
      	  				if(currSelected != null) currSelected.setSelected( false);
      	  				s.setSelected(true);
      	  				currSelected = s;
      	  				return; //there can only be one element selected at once duRR.
      	  			}
      	  		}
	     	}

	      	@Override
	     	public void mousePressed(MouseEvent e) {
	     		if(!oldMousePressed)
	     		{
	     			System.out.println(getComponentCount());
	      	  		for(int i = 0, c = getComponentCount(); i < c; i++)
	      	  		{
	      	  			if(!(getComponent(i) instanceof UMLDiagram)) continue;
	      	  			UMLDiagram s = (UMLDiagram) getComponent(i);
	      	  			int mx = e.getXOnScreen();
	      	  			int my = e.getYOnScreen();

	      	  			Point mousePoint = new Point(mx, my);
	      	  			SwingUtilities.convertPointFromScreen(mousePoint, UMLDiagramDisplay.this);

	      	  			if(s.getLeft() < mousePoint.getX() && mousePoint.getX() < s.getRight() && s.getTop() < mousePoint.getY() && mousePoint.getY() < s.getBottom())
	      	  			{
	      	  				s.setIsBeingDragged(true);
	      	  				currDragged = s;

	      	  				return; //there can only be one element dragged at once duRR.
	      	  			}
	      	  		}
	      	  	}
	      	  	oldMousePressed = true;
	      	}

	      	@Override
	      	public void mouseReleased(MouseEvent e) {
	      		//more effiecent that going over array and setting all to false;
	      		if(currDragged != null) currDragged.setIsBeingDragged(false);
	      		currDragged = null;
	      		oldMousePressed = false;
	      		pastsInit = false;
	      	}

	      	@Override
	      	public void mouseEntered(MouseEvent e) { }

	      	@Override
	      	public void mouseExited(MouseEvent e) { }
	    });
	}

	//===========================PAINTING=============================
	@Override
	protected void paintComponent(Graphics g)
	{
		if(isOpaque())
		{
			g.setColor(getBackground());
			g.fillRect(0, 0, getWidth(), getHeight());
		}
		for(int i = 0, c = getComponentCount(); i < c; i++)
  		{
  			if(!(getComponent(i) instanceof UMLDiagram)) continue;
  			UMLDiagram s = (UMLDiagram) getComponent(i);

  			if(s.getClassExtending() != null) drawSolidLine(g, s, s.getClassExtending());
  			if(s.getInterfacesImplementing() != null) drawDashedLine(g, s, s.getInterfacesImplementing());
  		}
	}
	
	/*
		@param mode: 0 = draw extending arrow, 1 means draw implementing arrow, 2 means draw 
		uses arrow
	*/
	public void drawSolidLine(Graphics g, JComponent from, JComponent to)
	{
		int fromX = from.getX()+from.getWidth()/2;
		int fromY = from.getY()+from.getHeight()/2;

		int toX = to.getX()+to.getWidth()/2;
		int toY = to.getY()+to.getHeight()/2;

		g.setColor(Color.BLACK);
		g.drawLine(fromX, fromY, toX, fromY);
		g.drawLine(toX, fromY, toX, toY);
		g.drawPolygon(new int[]{toX - 10, toX, toX + 10},new int[]{to.getY() - 10, to.getY(), to.getY() - 10},3);
	}

	public void drawDashedLine(Graphics g, JComponent from, ArrayList<UMLDiagram> implementing)
	{
		for(int k = 0, len = implementing.size(); k < len; k++)
		{
			UMLDiagram currTo = implementing.get(k);
			int fromX = from.getX()+from.getWidth()/2;
			int fromY = from.getY()+from.getHeight()/2;

			int toX = currTo.getX()+currTo.getWidth()/2;
			int toY = currTo.getY()+currTo.getHeight()/2;

			g.setColor(Color.BLACK);
			if(toX > fromX)
			{
				for(int i = 0; i < toX-fromX-10; i += 20)
					g.drawLine(fromX + i, fromY, fromX+10 + i, fromY);
			}else
			{
				for(int i = 0; i < fromX-toX-10; i += 20)
					g.drawLine(toX + i, fromY, toX+10 + i, fromY);
			}
			if(toY > fromY)
			{
				for(int i = 0; i < toY-fromY-10; i += 20)
					g.drawLine(toX, fromY + i, toX, fromY + 10 + i);
			} else
			{
				for(int i = 0; i < fromY-toY-10; i += 20)
					g.drawLine(toX, toY + i, toX, toY + 10 + i);
			}
			
			g.drawPolygon(new int[]{toX - 10, toX, toX + 10},new int[]{currTo.getY() - 10, currTo.getY(), currTo.getY() - 10},3);
		}
	}

}