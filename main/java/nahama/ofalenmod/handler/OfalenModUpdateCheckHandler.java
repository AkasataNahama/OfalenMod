package nahama.ofalenmod.handler;

import nahama.ofalenmod.Log;
import nahama.ofalenmod.OfalenModCore;
import net.minecraft.util.StatCollector;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class OfalenModUpdateCheckHandler {

	public static boolean isAvailableNewVersion;
	public static String latestVersion;
	public static ArrayList<String> notifiedNames = new ArrayList<>();

	/** 新しいバージョンがリリースされているか確認する。 */
	public static void checkUpdate() {
		try {
			HttpURLConnection connect = (HttpURLConnection) new URL(OfalenModCore.meta.updateUrl).openConnection();
			connect.setRequestMethod("GET");
			InputStream in = connect.getInputStream();
			String str = readString(in);
			while (str != null) {
				if (compareVersion(str))
					break;
				str = readString(in);
			}
			in.close();
			connect.disconnect();
		} catch (Exception e) {
			Log.error("Error on checking update!", "OfalenModUpdateCheckCore", true);
		}
	}

	private static String readString(InputStream in) {
		try {
			int l, a;
			byte b[] = new byte[2048];
			a = in.read();
			if (a < 0)
				return null;
			l = 0;
			while (a > 10) {
				if (a >= ' ') {
					b[l] = (byte) a;
					l++;
				}
				a = in.read();
			}
			return new String(b, 0, l);
		} catch (IOException e) {
			Log.error("Error on reading string!", "OfalenModUpdateCheckCore", true);
			return null;
		}
	}

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
		String omversion = version.substring(index + 1);
		String[] array1 = splitVersion(omversion);
		String[] array2 = splitVersion(OfalenModCore.MODVERSION);
		for (int i = 0; i < array1.length && i < array2.length; i++) {
			try {
				if (Integer.parseInt(array1[i]) > Integer.parseInt(array2[i])) {
					isAvailableNewVersion = true;
					latestVersion = version;
					return true;
				}
			} catch (NumberFormatException e) {
				Log.error("Error on comparing version!", "OfalenModUpdateCheckCore", true);
			}
		}
		return false;
	}

	public static String[] splitVersion(String version) {
		String[] result = new String[3];
		int index = 0;
		for (int i = 0; i < version.length(); i++) {
			char c = version.charAt(i);
			if (c == '.') {
				index++;
			} else {
				if (result[index] == null)
					result[index] = "";
				result[index] += c;
			}
		}
		return result;
	}

	public static String getMessage() {
		String s1 = StatCollector.translateToLocal("info.OfalenMod.Now");
		String s2 = StatCollector.translateToLocal("info.OfalenMod.New");
		return s1 + " " + OfalenModCore.VERSION + s2 + " " + latestVersion;
	}

}
