import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class TelaCarregando extends JWindow {
	// http://www.javaworld.com/article/2077467/core-java/java-tip-104--make-a-splash-with-swing.html
	
	private static final long serialVersionUID = 1L;

	public TelaCarregando(int waitTime) {
		super();
		setAlwaysOnTop(true); // http://stackoverflow.com/questions/297938/always-on-top-windows-with-java
		JLabel l = new JLabel(new ImageIcon(TelaTabuleiro.class.getClassLoader().getResource("ShallowBlue.png")));
		getContentPane().add(l, BorderLayout.CENTER);
		pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension labelSize = l.getPreferredSize();
		setLocation(screenSize.width / 2 - (labelSize.width / 2),
				screenSize.height / 2 - (labelSize.height / 2));
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				setVisible(false);
				dispose();
			}
		});
		final int pause = waitTime;
		final Runnable closerRunner = new Runnable() {
			public void run() {
				setVisible(false);
				dispose();
			}
		};
		Runnable waitRunner = new Runnable() {
			public void run() {
				try {
					Thread.sleep(pause);
					SwingUtilities.invokeAndWait(closerRunner);
				} catch (Exception excecao) {
					excecao.printStackTrace();
					// can catch InvocationTargetException
					// can catch InterruptedException
				}
			}
		};
		setVisible(true);
		Thread splashThread = new Thread(waitRunner);
		splashThread.start();
	}
	
}