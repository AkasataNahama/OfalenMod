package nahama.ofalenmod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log {

	private static Logger logger = LogManager.getLogger(OfalenModCore.MODID);

	public static void info(String msg) {
		logger.info(msg);
	}

	public static void info(String msg, String data, boolean flag) {
		if (flag)
			info(msg + " [" + data + "]");
	}

	public static void error(String msg) {
		logger.error(msg);
	}

	public static void error(String msg, String data, boolean flag) {
		if (flag)
			error(msg + " [" + data + "]");
	}

}
