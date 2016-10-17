package nahama.ofalenmod.handler;

import cpw.mods.fml.common.registry.GameRegistry;
import nahama.ofalenmod.Util;
import nahama.ofalenmod.core.OfalenModBlockCore;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map.Entry;

public class OfalenModAnniversaryHandler {

	private static final String urlAnniversaryList = "https://dl.dropboxusercontent.com/s/wmx074ypp4yhv7c/OfalenModAnniversary.txt";
	/**
	 * マルチプレイの場合は、プレイヤー名と最後にプレゼントを渡した日付のマップ。
	 * シングルプレイの場合は、ワールド名と最後にプレゼントを渡した日付のマップ。
	 */
	private static HashMap<String, String> presentedDate = new HashMap<>();
	/** サーバー起動時の日付。 */
	private static String today;
	/** todayを含んだ記念日イベントの期間。 */
	private static String[] dates;
	/** プレゼントの配列。 */
	private static ItemStack[] presents;
	/** 二回目にあけたときのプレゼント。 */
	private static ItemStack[] anotherPresents;
	/** シングルプレイかどうか。 */
	public static boolean isSinglePlay;
	/** テクスチャが特別かどうか。 */
	public static boolean isTextureSpecial;
	/** 二回開けられる記念日かどうか。 */
	public static boolean isTwice;

	/** 初期化処理。 */
	public static void init() {
		presentedDate.clear();
		presents = new ItemStack[54];
		anotherPresents = new ItemStack[54];
		Calendar cal = Calendar.getInstance();
		int year = cal.get(cal.YEAR);
		int month = cal.get(cal.MONTH);
		int date = cal.get(cal.DATE);
		today = year + "/" + (month + 1) + "/" + date;
		//		today = "2016/11/21";
		loadPresentedDate:
		{
			try {
				// ConfigフォルダのOfalenModPresentedDate.txtを読み込む。
				File file = new File(new File(".\\").getParentFile(), "config\\OfalenModPresentedDate.txt");
				if (!file.exists() || !file.isFile() || !file.canRead()) {
					// ファイルが読み込めない状態なら、プレゼントの読み込みに移行。
					break loadPresentedDate;
				}
				BufferedReader br = new BufferedReader(new FileReader(file));
				String s;
				while ((s = br.readLine()) != null) {
					String[] array = s.split(",");
					presentedDate.put(array[0], array[1]);
				}
				br.close();
			} catch (Exception e) {
				Util.error("Error on loading OfalenModPresentedDate.txt.", "OfalenModAnniversaryHandler");
				e.printStackTrace();
			}
		}
		try {
			HttpURLConnection connect = (HttpURLConnection) new URL(urlAnniversaryList).openConnection();
			connect.setRequestMethod("GET");
			InputStream in = connect.getInputStream();
			String str;
			while ((str = readString(in)) != null) {
				if (str.charAt(0) != '+') {
					dates = null;
					isTwice = false;
					isTextureSpecial = false;
					continue;
				}
				str = str.substring(1);
				if (str.charAt(0) == '+') {
					isTwice = true;
					str = str.substring(1);
				}
				if (str.charAt(0) == '-') {
					isTextureSpecial = true;
					str = str.substring(1);
				}
				dates = str.split(",");
				for (String date1 : dates) {
					if (!date1.equals(today))
						continue;
					loadPresents(in);
					in.close();
					connect.disconnect();
					return;
				}
			}
			dates = null;
			in.close();
			connect.disconnect();
		} catch (Exception e) {
			Util.error("Error on getting presents.", "OfalenModAnniversaryHandler");
			e.printStackTrace();
		}
	}

	/** 一行を読み込み、一つの文字列として返す。 */
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
			Util.error("Error on reading string.", "OfalenModUpdateCheckCore");
			return null;
		}
	}

	/** プレゼントのデータを読み込み、presentsを設定する。 */
	private static void loadPresents(InputStream in) {
		String str;
		for (int i = 0; i < 54; i++) {
			str = readString(in);
			if (str == null || str.charAt(0) == '+') {
				presents = new ItemStack[54];
				return;
			}
			try {
				String[] array = str.split(",");
				if (array[0].equals("null")) {
					i += Integer.valueOf(array[1]) - 1;
					continue;
				}
				Item item = GameRegistry.findItem(array[0], array[1]);
				if (item != null) {
					presents[i] = new ItemStack(item, 1, Integer.valueOf(array[2]));
				}
			} catch (Exception e) {
				presents[i] = null;
				Util.error("Error on loading presents.", "OfalenModAnniversaryHandler");
				e.printStackTrace();
			}
		}
		if (!isTwice)
			return;
		for (int i = 0; i < 54; i++) {
			str = readString(in);
			if (str == null || str.charAt(0) == '+') {
				anotherPresents = new ItemStack[54];
				return;
			}
			try {
				String[] array = str.split(",");
				if (array[0].equals("null")) {
					i += Integer.valueOf(array[1]) - 1;
					continue;
				}
				Item item = GameRegistry.findItem(array[0], array[1]);
				if (item != null) {
					anotherPresents[i] = new ItemStack(item, 1, Integer.valueOf(array[2]));
				}
			} catch (Exception e) {
				anotherPresents[i] = null;
				Util.error("Error on loading second presents.", "OfalenModAnniversaryHandler");
				e.printStackTrace();
			}
		}
	}

	/** presentedDateのデータをOfalenModPresentedDate.txtに保存する。 */
	public static void save() {
		try {
			File file = new File(new File(".\\").getParentFile(), "config\\OfalenModPresentedDate.txt");
			if (!file.exists() || !file.isFile() || !file.canWrite()) {
				file.createNewFile();
			}
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			for (Entry<String, String> entry : presentedDate.entrySet()) {
				bw.write(entry.getKey() + "," + entry.getValue());
				bw.newLine();
			}
			bw.close();
		} catch (Exception e) {
			Util.error("Error on saving OfalenModPresentedDate.txt.", "OfalenModAnniversaryHandler");
			e.printStackTrace();
		}
	}

	/** プレイヤーにプレゼントボックスを渡していたかどうかを確認し、渡していなければプレゼントボックスをドロップする。 */
	public static void checkPlayer(EntityPlayer player) {
		String name = player.getCommandSenderName();
		if (isSinglePlay)
			name = player.worldObj.getSaveHandler().getWorldDirectoryName();
		// リストに載っているなら終了。
		if (presentedDate.containsKey(name))
			return;
		// プレゼントボックスを渡していなかったら渡す。
		EntityItem entity = new EntityItem(player.worldObj, player.posX, player.posY, player.posZ, new ItemStack(OfalenModBlockCore.boxPresentOfalen));
		player.worldObj.spawnEntityInWorld(entity);
		presentedDate.put(name, "0/0/0");
	}

	/** プレゼントの配列をコピーして返す。 */
	public static ItemStack[] getPresents(EntityPlayer player) {
		String name = player.getCommandSenderName();
		if (isSinglePlay)
			name = player.worldObj.getSaveHandler().getWorldDirectoryName();
		if (!presentedDate.containsKey(name) || dates == null)
			return null;
		String s = presentedDate.get(name);
		// 同じ記念日で一回も開けていないかどうか。
		boolean flag = true;
		for (String date : dates) {
			// 既に同じ記念日で渡していたならnull。
			if (date.equals(s))
				return null;
			if (s.equals(date + '-'))
				flag = false;
		}
		presentedDate.put(name, today);
		if (isTwice && flag) {
			// 二回開けられる記念日で、一回目なら'-'をつける。
			presentedDate.put(name, today + '-');
		}
		ItemStack[] result = new ItemStack[presents.length];
		for (int i = 0; i < presents.length; i++) {
			if (presents[i] != null)
				result[i] = presents[i].copy();
		}
		if (isTwice && !flag) {
			// 二回開けられる記念日で、二回目なら、anotherPresentsを返す。
			result = new ItemStack[anotherPresents.length];
			for (int i = 0; i < anotherPresents.length; i++) {
				if (anotherPresents[i] != null)
					result[i] = anotherPresents[i].copy();
			}
		}
		return result;
	}

}
