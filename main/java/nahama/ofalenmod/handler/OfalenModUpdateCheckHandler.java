package nahama.ofalenmod.handler;

import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.util.Util;
import net.minecraft.util.StatCollector;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class OfalenModUpdateCheckHandler {
	/** 新しいバージョンがリリースされているか。 */
	public static boolean isNewVersionAvailable;
	/** 最新版のバージョン番号。 */
	public static String versionLatest = "";
	/** 告知済みのプレイヤーの名前。 */
	public static ArrayList<String> namesNotified = new ArrayList<>();

	/** 新しいバージョンがリリースされているか確認する。 */
	public static void checkUpdate() {
		try {
			// ネット上のファイルに接続し、テキストを取得する。
			HttpURLConnection connect = (HttpURLConnection) new URL(OfalenModCore.meta.updateUrl).openConnection();
			connect.setRequestMethod("GET");
			InputStream inputStream = connect.getInputStream();
			// 一行ずつ読み込み、比較していく。
			while (true) {
				String str = Util.readString(inputStream);
				if (str == null)
					break;
				if (compareVersion(str))
					break;
			}
			inputStream.close();
			// 接続を切断する。
			connect.disconnect();
		} catch (Exception e) {
			Util.error("Error on checking update.", "OfalenModUpdateCheckCore");
			e.printStackTrace();
		}
	}

	/** 新しいバージョンがあるかどうか。 */
	private static boolean compareVersion(String str) {
		String[] array0 = str.split(":");
		// MODIDが違うなら終了。
		if (!array0[0].equals(OfalenModCore.MODID))
			return false;
		String version = array0[1];
		int index = version.indexOf(']');
		// Minecraftのバージョンが違うなら終了。
		if (!OfalenModCore.MCVERSION.equals(version.substring(1, index)))
			return false;
		String versionOfalenMod = version.substring(index + 1);
		String[] latest = versionOfalenMod.split(Pattern.quote("."));
		String[] using = OfalenModCore.MODVERSION.split(Pattern.quote("."));
		for (int i = 0; i < latest.length && i < using.length; i++) {
			try {
				int iLatest = Integer.parseInt(latest[i]);
				int iUsing = Integer.parseInt(using[i]);
				// 使用中のバージョンの方が新しいなら終了。
				if (iLatest < iUsing)
					return false;
				// この桁の数が同じなら次の桁へ。
				if (iLatest == iUsing)
					continue;
				isNewVersionAvailable = true;
				versionLatest = version;
				return true;
			} catch (NumberFormatException e) {
				Util.error("Error on comparing version.", "OfalenModUpdateCheckCore");
			}
		}
		// 使用中のバージョンより桁数が多いならtrue。
		return latest.length > using.length;
	}

	public static String getMessage() {
		String s1 = StatCollector.translateToLocal("info.ofalen.using");
		String s2 = StatCollector.translateToLocal("info.ofalen.latest");
		return "  " + s1 + " : " + OfalenModCore.VERSION + "    " + s2 + " : " + versionLatest;
	}
}
