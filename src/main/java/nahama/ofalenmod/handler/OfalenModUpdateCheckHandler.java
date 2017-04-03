package nahama.ofalenmod.handler;

import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.util.OfalenLog;
import nahama.ofalenmod.util.OfalenTimer;
import nahama.ofalenmod.util.OfalenUtil;
import nahama.ofalenmod.util.VersionUtil;
import nahama.ofalenmod.util.VersionUtil.VersionString;
import net.minecraft.util.StatCollector;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class OfalenModUpdateCheckHandler {
	/** 新しいバージョンがリリースされているか。 */
	public static boolean isNewVersionAvailable;
	/** 最新版のバージョン番号。 */
	private static String versionLatest = "";
	/** 告知済みのプレイヤーの名前。 */
	public static ArrayList<String> namesNotified = new ArrayList<String>();

	/** 新しいバージョンがリリースされているか確認する。 */
	public static void checkUpdate() {
		OfalenTimer.start("OfalenModUpdateCheckHandler.checkUpdate");
		try {
			// ネット上のファイルに接続し、テキストを取得する。
			HttpURLConnection connect = (HttpURLConnection) new URL(OfalenModCore.meta.updateUrl).openConnection();
			connect.setRequestMethod("GET");
			InputStream inputStream = connect.getInputStream();
			// 一行ずつ読み込み、比較していく。
			while (true) {
				String str = OfalenUtil.readLine(inputStream);
				if (str == null)
					break;
				if (compareVersion(str))
					break;
			}
			inputStream.close();
			// 接続を切断する。
			connect.disconnect();
		} catch (Exception e) {
			OfalenLog.error("Error on checking update.", "OfalenModUpdateCheckCore");
			e.printStackTrace();
		}
		OfalenTimer.watchAndLog("OfalenModUpdateCheckHandler.checkUpdate");
	}

	/** 新しいバージョンがあるかどうか。 */
	private static boolean compareVersion(String str) {
		VersionString latest = new VersionString(str);
		if (!VersionUtil.compareVersion(OfalenModCore.VERSION_STRING, latest))
			return false;
		isNewVersionAvailable = true;
		versionLatest = latest.getVersion();
		return true;
	}

	public static String getMessage() {
		String s1 = StatCollector.translateToLocal("info.ofalen.using");
		String s2 = StatCollector.translateToLocal("info.ofalen.latest");
		return "  " + s1 + " : " + OfalenModCore.VERSION + "    " + s2 + " : " + versionLatest;
	}
}
