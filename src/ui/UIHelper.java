/**
 * Provides methods for classes to interact with UIFrame
 * 
 * @author Yan Chan Min Oo
 */
package ui;

public class UIHelper {
	
	private static UIFrame frame;
	
	/**
	 * Returns a string of the user input. Triggered on enter press.
	 */
	public static String getUserInput(){
		String inputString = frame.getInputText();
		frame.setInputText("");
		
		return inputString;
	}
	
	public static void createUI(){
		frame = new UIFrame();
		 /* Set the Nimbus look and feel */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(UIFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UIFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UIFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UIFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                frame.setVisible(true);
            }
        });
	}
	
	/**
	 * Destroys the UI
	 */
	public static void destroyUI(){
		frame.dispose();
	}
	
	public static void appendOutput(String s){
		frame.setOutputText(frame.getOutputText() + s + System.lineSeparator());
	}
	
	public static void setOutput(String s){
		frame.setOutputText(s);
	}
	
	public static void setInput(String s){
		frame.setInputText(s);
	}

}
