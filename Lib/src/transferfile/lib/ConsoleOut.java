package transferfile.lib;

public class ConsoleOut {
	public static void out(Class<?> c, String message) {
		System.out.println(message + " - " + c.getName());
	}

	public static void exception(Class<?> c, Exception ex) {
		StackTraceElement l = ex.getStackTrace()[0];
		String message = l.getClassName() + "/" + l.getMethodName() + "/" + ex.getMessage() + ":" + l.getLineNumber();
		System.err.println(message + " - " + c.getName());
		ex.printStackTrace();

	}
}
