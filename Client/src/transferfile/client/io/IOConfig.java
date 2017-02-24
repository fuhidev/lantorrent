package transferfile.client.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;

import transferfile.client.config.ConfigApp;

public class IOConfig {
	public static final String PATH_CONFIG = System.getProperty("user.home") + "\\thtTorrent\\config.ini";

	public static void write() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_CONFIG));
			PrintWriter printWriter = new PrintWriter(writer);
			for (Field m : ConfigApp.class.getFields()) {
				try {
					printWriter.println(m.getName() + "=" + m.get(new String()));
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			printWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void read() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(PATH_CONFIG));
			try {
				String line = "";
				while ((line = reader.readLine()) != null) {
					if (line.equals(""))
						continue;
					if (line.charAt(0) == '#')
						continue;
					String[] split = line.split("=");
					try {
						ConfigApp.class.getField(split[0]).set(new String(), split[1]);
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchFieldException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		IOConfig.write();
		// IOConfig.read();
		// System.out.println(ConfigConstApp.AUTO_DOWNLOAD);
	}
}
