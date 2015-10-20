/**
 * Provides methods for classes to interact with UIFrame
 * 
 * @author Yan Chan Min Oo
 */
package ui;


import java.util.logging.Logger;


public class UIHelper {
	
	/*
	private static UIFrame frame;
	private static Logger log = Logger.getLogger("log");
	*/
	
	private static UI user_interface;
	
	/**
	 * Returns a string of the user input. Triggered on enter press.
	 */
	
	public static String getUserInput(){
		/*String inputString = frame.getInputText();
		frame.setInputText("");
		
		return inputString;*/
		return "";
	}
	
	
	public static void createUI() {
		/*
		if(frame != null){ // Dont allow multiple UI instances TODO: Allow recreation after destroy
			return;
		}
		frame = new UIFrame();

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            log.log(java.util.logging.Level.SEVERE, "UIHelper: ",ex);
        } catch (InstantiationException ex) {
        	log.log(java.util.logging.Level.SEVERE, "UIHelper: ",ex);
        } catch (IllegalAccessException ex) {
        	log.log(java.util.logging.Level.SEVERE, "UIHelper: ",ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
        	log.log(java.util.logging.Level.WARNING, "UIHelper: ",ex);
        }
        //</editor-fold>

        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                frame.setVisible(true);
            }
        });
        */
		if(user_interface == null){
			user_interface = new UI();
			user_interface.createUI();
		}
	}
	
	
	
	/**
	 * Destroys the UI
	 */
	
	public static void destroyUI(){
		//frame.dispose();
	}
	
	public static void appendOutput(String appendString){
		//frame.setOutputText(frame.getOutputText() + appendString + System.lineSeparator());
	}
	
	public static void setOutput(String out){
		//frame.setOutputText(out);
	}
	
	public static void setUserInput(String in){
		//frame.setInputText(in);
	}
	
}
