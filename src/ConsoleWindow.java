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
public class ConsoleWindow{
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
	
	public ConsoleWindow() {
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
			info.addAttribute(StyleConstants.Foreground, Color.BLUE);
		}
	
	}
	/**
	 * 
	 * @param c
	 */
	public void setInputTextColor(Color c){
		send.addAttribute(StyleConstants.Foreground, c);
	}
	/**
	 * 
	 * @param c
	 */
	public void setInfoTextColor(Color c){
		info.addAttribute(StyleConstants.Foreground, c);
	}
	/**
	 * 
	 * @param c
	 */
	public void setWarningTextColor(Color c){
		warning.addAttribute(StyleConstants.Foreground, c);
	}
	/**
	 * 
	 * @param c
	 */
	public void setErrorTextColor(Color c){
		error.addAttribute(StyleConstants.Foreground, c);
	}
	/**
	 * 
	 * @param str
	 * @param severity
	 */
	public void addLine(String str,int severity){
		Style style = null;
		switch(severity){
		case 0:
			str = str + "\n";
			style = send;
			break;
		case 1:
			str = "(Info) "+ str + "\n";
			style = send;
			break;
		case 2:
			str = "(Warning) "+ str + "\n";
			style = send;
			break;
		case 3:
			str = "(Error)" + str + "\n";
			style = send;
			break;
		}
		try {
			clean();
			document.insertString(document.getLength(),str,null);
			document.setParagraphAttributes(parnum, str.length(), style, false);
			parnum+= str.length();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	
	public class test implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(!e.getActionCommand().equals("")){
				Console.sendInput(e.getActionCommand());
				input.setText("");
			}
		}
		
	}

}
