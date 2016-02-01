import java.util.ArrayList;

public class Console {
	private static ArrayList<ConsoleWindow> windowList = new  ArrayList<ConsoleWindow>();
	public static void addConsoleWindow(ConsoleWindow c){
		windowList.add(c);
	}
	/**
	 * Removes the Console Window at index of 0
	 */
	public static void removeConsoleWindow(){
		removeConsoleWindow(0);
	}
	/**
	 *  Removes the Console Window at index of i
	 * @param i	Index of ConsoleWindow to remove
	 */
	public static void removeConsoleWindow(int i){
		windowList.remove(i);
	}
	/**
	 * Removes the Console Window passed in the constructor
	 * @param c  ConsoleWindow object to be removed
	 */
	public static void removeConsoleWindow(ConsoleWindow c){
		windowList.remove(c);
	}
	/**
	 * 
	 * @param str
	 */
	public static void sendInput(String str){
		System.out.println(str);
		sendLine(str,0);
	}
	/**
	 * 
	 * @param str
	 */
	public static void sendInfo(String str){
		str = "(Info) "+ str;
		sendLine(str,1);
	}
	/**
	 * 
	 * @param str
	 */
	public static void sendWarning(String str){
		str = "(Warning) "+ str;
		sendLine(str,2);
	}
	/**
	 * 
	 * @param str
	 */
	public static void sendError(String str){
		str = "(Error) "+ str;
		sendLine(str,3);
	}
	private static void sendLine(String str, int severity){
		for(ConsoleWindow window: windowList){
			window.addLine(str, severity);
		}
	}
	

}
