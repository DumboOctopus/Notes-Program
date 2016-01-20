import javax.swing.*;

public class MainForTestExtendingJComp extends JFrame
{
	public MainForTestExtendingJComp()
	{

		UMLDiagram walkable = new UMLDiagram(300, 300);
		walkable.setClassType("<<interface>>");

		UMLDiagram animal = new UMLDiagram(200,200);
		animal.setClassName("Animal");
		animal.setClassType("<<abstract>>");
		animal.addInterfacesImplementing(walkable);
		UMLDiagram dog = new UMLDiagram(100, 100);
		dog.setClassName("Dog");
		dog.setClassExtending(animal);

		UMLDiagramDisplay c = new UMLDiagramDisplay(new UMLDiagram[]{animal, dog, walkable});
		c.setOpaque(true);
		c.setSize(500, 500);
		add(c);

		setVisible(true);
		setSize(600, 600);

    	setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}
	public static void main(String[] args) {
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          new MainForTestExtendingJComp();
        }
    });
}
}