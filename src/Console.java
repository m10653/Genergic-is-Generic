import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
/**
 * 
 * @author Michael Combs
 *
 */
public class Console{
	private JFrame window;
	private JMenuBar menuBar;
	private JTextPane textArea;
	private JScrollPane scrollPane;
	private JTextField input;
	private DefaultStyledDocument document;
	private Style info, warning, error, send;
	private int parnum = 0;
	private boolean gui = Boolean.parseBoolean(Config.read("ConsoleGUI"));
	private int maxLines = Integer.parseInt(Config.read("MaxLineBuffer"));
	
	public Console() {
		if(gui){
			window = new JFrame("Package Tracking Console");
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			window.setSize((int)(screenSize.getWidth()/4),(int) (screenSize.getHeight() /1.5));
			window.setLocationRelativeTo(null);
			window.getContentPane().setLayout(new BorderLayout());
		
		
			menuBar = new JMenuBar();
			menuBar.add("test", new JLabel("Testing"));
			window.setJMenuBar(menuBar);
		
			StyleContext sc = new StyleContext();
			document = new DefaultStyledDocument(sc);
			
		
			//Styles
			
			send = sc.addStyle("(Send)", null);
			send.addAttribute(StyleConstants.Foreground, Color.CYAN);
			
			info = sc.addStyle("(Info)", null);
			info.addAttribute(StyleConstants.Foreground, Color.WHITE);
		
			warning = sc.addStyle("(Warning)", null);
			warning.addAttribute(StyleConstants.Foreground, Color.YELLOW);
		
			error = sc.addStyle("(Error)", null);
			error.addAttribute(StyleConstants.Foreground, Color.RED);
			error.addAttribute(StyleConstants.Bold, new Boolean(true));
	
			textArea = new JTextPane(document);
			textArea.setBackground(new Color(50, 50, 50));
			textArea.setEditable(true);
			textArea.setBorder(null);
			textArea.setForeground(Color.WHITE);
			
			scrollPane = new JScrollPane(textArea);
			new SmartScroller(scrollPane);
			
			window.getContentPane().add(scrollPane);
		
			input = new JTextField();
			input.setBackground(new Color(50,50,50));
			input.setForeground(Color.WHITE);
			input.setCaretColor(Color.WHITE);
			input.addActionListener(new test());
			window.getContentPane().add(input, BorderLayout.SOUTH);
		
			window.setVisible(true);
			
		}
	
	}
	private void send(String str){
		str = str + "\n";
		System.out.print(str);
		if(gui){
			try {
				clean();
				document.insertString(document.getLength(),str,null);
				document.setParagraphAttributes(parnum, str.length(), send, false);
				parnum+= str.length();
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		clean();
	}
	private void clean(){ //TODO Minor Bug last ln is not colored right
		Element root = document.getDefaultRootElement();
		while(root.getElementCount() > maxLines){
			try {
				parnum-= document.getText(0,root.getElement(0).getEndOffset()).length();
				document.remove(0,root.getElement(0).getEndOffset());
				
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void sendInfo(String str){
		str = "(Info) "+ str + "\n";
		System.out.print(str);
		if(gui){
			try {
				
				document.insertString(document.getLength(),str,null);
				document.setParagraphAttributes(parnum, str.length(), info, false);
				parnum+= str.length();
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		clean();
	}
	public void sendWarning(String str){
		str = "(Warning) "+ str + "\n";
		if(gui){
			System.out.print(str);
			try {
				
				document.insertString(document.getLength(),str,null);
				document.setParagraphAttributes(parnum, str.length(), warning, false);
				parnum+= str.length();
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		clean();
	}
	public void sendError(String str){
		str = "(Error) "+ str + "\n";
		System.out.print(str);
		if(gui){
			try {
				
				document.insertString(document.getLength(),str,null);
				document.setParagraphAttributes(parnum, str.length() ,error, false);
				parnum+= str.length();
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		clean();
	}
	
	public class test implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(!e.getActionCommand().equals("")){
				send(e.getActionCommand());
				input.setText("");
			}
		}
		
	}

}
