package nahama.ofalenmod.util;

import java.util.HashMap;

public class OfalenTimer {
	private static HashMap<String, Long> timeMap = new HashMap<String, Long>();

	public static void start(String name) {
		timeMap.remove(name);
		timeMap.put(name, System.nanoTime());
	}

	public static void start(String name, boolean log) {
		if (log)
			OfalenLog.debuggingInfo(name + " start!", "OfalenTimer");
		start(name);
	}

	private static long getTime(String name) {
		return System.nanoTime() - timeMap.get(name);
	}

	public static void watchAndLog(String name) {
		watchAndLog(name, 0);
	}

	public static void watchAndLog(String name, double min) {
		double time = ((double) getTime(name)) / 1000000;
		if (time < min)
			return;
		OfalenLog.debuggingInfo(name + " ... " + time + " ms. Current Time : " + System.currentTimeMillis(), "OfalenTimer");
	}

	public static void watchAndLogNano(String name) {
		watchAndLogNano(name, 0);
	}

	public static void watchAndLogNano(String name, long min) {
		long time = getTime(name);
		if (time >= min)
			OfalenLog.debuggingInfo(name + " ... " + time + " ns. Current Time : " + System.currentTimeMillis(), "OfalenTimer");
	}
}
