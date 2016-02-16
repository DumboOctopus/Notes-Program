
import javax.swing.JFrame;

import javax.swing.JScrollPane;

import javax.swing.JTree;

import javax.swing.tree.TreePath;

/**

 * Utility class that can be used to retrieve and/or restore the expansion state of a JTree. 

 * @author G. Cope

 *

 */

public class TreeUtil {




    /**

     * Retrieves the expansion state as a String, defined by a comma delimited list of

     * each row node that is expanded.

     * @return

     */

    public static String getExpansionState(JTree tree){

        StringBuilder sb = new StringBuilder();

        for ( int i = 0; i < tree.getRowCount(); i++ ){

            if ( tree.isExpanded(i) ){

                sb.append(i).append(",");

            }

        }

        return sb.toString();

    }



    /**

     * Sets the expansion state based upon a comma delimited list of row indexes that

     * are expanded.

     * @param s

     */

    public static void setExpansionState(JTree tree, String s){

        String[] indexes = s.split(",");

        for ( String st : indexes ){

            int row = Integer.parseInt(st);

            tree.expandRow(row);

        }

    }

}

