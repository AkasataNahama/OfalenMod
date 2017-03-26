package nahama.ofalenmod.util;

import nahama.ofalenmod.OfalenModCore;

import java.util.regex.Pattern;

public class VersionUtil {
	public static boolean compareVersion(VersionString using, VersionString latest) {
		return canCompareVersion(using, latest) && isNewVersionAvailable(using.versionMod, latest.versionMod);
	}

	/** ModIdとMinecraftVersionが一致しているか。 */
	public static boolean canCompareVersion(VersionString using, VersionString latest) {
		return using.idMod.equals(latest.idMod) && using.versionMinecraft.equals(latest.versionMinecraft);
	}

	/**
	 * 新しいバージョンがあるかどうか。
	 * @return versionModUsingよりversionModLatestの方が新しいならtrue。
	 */
	public static boolean isNewVersionAvailable(String versionModUsing, String versionModLatest) {
		String[] arrayLatest = versionModLatest.split(Pattern.quote("."));
		String[] arrayUsing = versionModUsing.split(Pattern.quote("."));
		for (int i = 0; i < arrayLatest.length && i < arrayUsing.length; i++) {
			try {
				int iLatest = Integer.parseInt(arrayLatest[i]);
				int iUsing = Integer.parseInt(arrayUsing[i]);
				if (iLatest < iUsing) {
					// 使用中のバージョンの方が古い。
					return false;
				} else if (iLatest > iUsing) {
					// 使用中のバージョンの方が新しい。
					return true;
				}
				// この桁の数が同じなら次の桁へ。
			} catch (NumberFormatException e) {
				OfalenLog.error("Error on comparing version.", "OfalenModUpdateCheckCore");
				if (OfalenModCore.IS_DEBUGGING)
					throw e;
			}
		}
		// 使用中のバージョンより桁数が多いならtrue。
		return arrayLatest.length > arrayUsing.length;
	}

	public static class VersionString {
		public final String idMod;
		public final String versionMinecraft;
		public final String versionMod;

		public VersionString(String string) {
			this(convertToModId(string), convertToVersion(string));
		}

		public VersionString(String idMod, String version) {
			this(idMod, convertToMinecraftVersion(version), convertToModVersion(version));
		}

		public VersionString(String idMod, String versionMinecraft, String versionMod) {
			this.idMod = idMod;
			this.versionMinecraft = versionMinecraft;
			this.versionMod = versionMod;
		}

		public String getVersion() {
			return "[" + versionMinecraft + "]" + versionMod;
		}

		private static String convertToModId(String string) {
			return string.substring(0, string.indexOf(':'));
		}

		private static String convertToVersion(String string) {
			return string.substring(string.indexOf(':') + 1);
		}

		private static String convertToMinecraftVersion(String version) {
			return version.substring(version.indexOf('[') + 1, version.indexOf(']'));
		}

		private static String convertToModVersion(String version) {
			return version.substring(version.indexOf(']') + 1);
		}
	}
}
