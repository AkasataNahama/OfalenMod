package nahama.ofalenmod.util;

import nahama.ofalenmod.OfalenModCore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OfalenLog {
	private static Logger logger = LogManager.getLogger(OfalenModCore.MOD_ID);

	public static void info(String msg) {
		logger.info(msg);
	}

	public static void info(String msg, String name) {
		info(msg + " [" + name + "]");
	}

	public static void info(String msg, String name, boolean flag) {
		if (flag)
			info(msg, name);
	}

	public static void error(String msg) {
		logger.error(msg);
	}

	public static void error(String msg, String name) {
		error(msg + " [" + name + "]");
	}

	public static void error(String msg, String name, boolean flag) {
		if (flag)
			error(msg, name);
	}

	public static void debuggingInfo(String msg) {
		if (OfalenModCore.IS_DEBUGGING)
			logger.info(msg + " [DEBUG]");
	}

	public static void debuggingInfo(String msg, String name) {
		debuggingInfo(msg + " [" + name + "]");
	}

	public static void debuggingInfo(String msg, String name, boolean flag) {
		if (flag)
			debuggingInfo(msg, name);
	}
}
