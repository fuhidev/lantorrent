package transferfile.gui;

import java.awt.EventQueue;

import transferfile.client.main.ClientProcess;
import transferfile.gui.view.MainFrame;

public class MainGUI {
	public static void main(String[] args) {
		MainFrame frame = new MainFrame();
		ClientProcess clientProcess = new ClientProcess();
		frame.setController(clientProcess);
		clientProcess.addView(frame);
		clientProcess.createFolder();
		clientProcess.init();
		clientProcess.run();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
