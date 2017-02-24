package transferfile.client.main;

import java.io.File;
import java.util.Scanner;

import transferfile.lib.model.FileInfo;

public class MainClient {
	public static void main(String[] args) {
		
		ClientProcess process = new ClientProcess();
		process.createFolder();
		process.init();
		process.run();
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		while (true) {
			String line = scanner.nextLine();
			if (line.startsWith("add")) {
				String[] s = line.split("@");
				process.addFile(new File(s[1]));
			}
			else if (line.startsWith("request")) {
				String[] s = line.split("@");
				process.request(new FileInfo(s[1], Long.parseLong(s[2])));
			}
			else{
				System.out.println("Không đúng cú pháp");
			}
		}
	}
}
