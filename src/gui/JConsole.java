/**
	MIS Project, JConsole Class ( Redirect streaming output to UI )
	- Coded by:
	  @group Bo.Ma.
	  @author Bovi Andrea
	  @author Marsicovetere Alessio
*/

package gui;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import javax.swing.JTextArea;

public class JConsole extends JTextArea {
	
	private static final long serialVersionUID = 1L;
	private PrintStream printStream;

	public JConsole() {
		printStream = new PrintStream(new ConsolePrintStream());
	}

	public PrintStream getPrintStream() {
		return printStream;
	}

	private class ConsolePrintStream extends ByteArrayOutputStream {
		public synchronized void write(byte[] b, int off, int len) {
			setCaretPosition(getDocument().getLength());
			String str = new String(b);
			append(str.substring(off, len));
		}
	}
}