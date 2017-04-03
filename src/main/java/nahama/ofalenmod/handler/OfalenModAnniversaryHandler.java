package nahama.ofalenmod.handler;

import cpw.mods.fml.common.registry.GameRegistry;
import nahama.ofalenmod.core.OfalenModBlockCore;
import nahama.ofalenmod.util.OfalenLog;
import nahama.ofalenmod.util.OfalenTimer;
import nahama.ofalenmod.util.OfalenUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class OfalenModAnniversaryHandler {
	private static final String URL_ANNIVERSARY_LIST = "https://dl.dropboxusercontent.com/s/wmx074ypp4yhv7c/OfalenModAnniversary.txt";
	/**
	 * マルチプレイの場合は、プレイヤー名と最後にプレゼントを渡した日付のマップ。
	 * シングルプレイの場合は、ワールド名と最後にプレゼントを渡した日付のマップ。
	 */
	private static HashMap<String, String> presentedDate = new HashMap<String, String>();
	/** サーバー起動時の日付。 */
	private static String today;
	/** todayを含んだ記念日イベントの期間。 */
	private static String[] dates;
	/** プレゼントの配列。 */
	private static ItemStack[][] presents;
	/** テクスチャが特別かどうか。 */
	public static boolean isTextureSpecial;
	/** 二回開けられる記念日かどうか。 */
	private static boolean isTwice;

	/** 初期化処理。 */
	public static void init() {
		OfalenTimer.start("OfalenModAnniversaryHandler.init");
		presents = new ItemStack[2][54];
		// 日付を取得する。
		Calendar cal = Calendar.getInstance();
		String now = cal.get(Calendar.YEAR) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DATE);
		// 		now = "2016/11/21";
		loadPresentedDates();
		if (today == null || !today.equals(now)) {
			getPresents();
			today = now;
		}
		OfalenTimer.watchAndLog("OfalenModAnniversaryHandler.init");
	}

	/** presentedDateを初期化する。 */
	private static void loadPresentedDates() {
		presentedDate.clear();
		try {
			// ConfigフォルダのOfalenModPresentedDate.txtを読み込む。
			File file = new File(new File(".\\").getParentFile(), "config\\OfalenModPresentedDate.txt");
			if (!file.exists() || !file.isFile() || !file.canRead()) {
				// ファイルが読み込めない状態なら終了。
				return;
			}
			BufferedReader br = new BufferedReader(new FileReader(file));
			String s;
			while ((s = br.readLine()) != null) {
				String[] array = s.split(",");
				presentedDate.put(array[0], array[1]);
			}
			br.close();
		} catch (Exception e) {
			OfalenLog.error("Error on loading OfalenModPresentedDate.txt.", "OfalenModAnniversaryHandler");
			e.printStackTrace();
		}
	}

	/** プレゼントを取得する。 */
	private static void getPresents() {
		try {
			// ネット上のファイルに接続し、テキストを取得する。
			HttpURLConnection connect = (HttpURLConnection) new URL(URL_ANNIVERSARY_LIST).openConnection();
			connect.setRequestMethod("GET");
			// メモ：HttpURLConnection.getInputStream()に時間がかかる。
			InputStream inputStream = connect.getInputStream();
			List<String> list = OfalenUtil.readAll(inputStream);
			inputStream.close();
			connect.disconnect();
			for (String str : list) {
				if (str == null)
					break;
				if (str.charAt(0) != '+') {
					// 日付指定の行でないなら初期化して次へ。
					continue;
				}
				str = str.substring(1);
				if (str.charAt(0) == '+') {
					// "+"が二つ並んでいたら二重プレゼントを有効に。
					isTwice = true;
					str = str.substring(1);
				} else {
					isTwice = false;
				}
				if (str.charAt(0) == '-') {
					// 次に"-"があったら特殊テクスチャを有効に。
					isTextureSpecial = true;
					str = str.substring(1);
				} else {
					isTextureSpecial = false;
				}
				dates = str.split(",");
				for (String date1 : dates) {
					if (date1.equals(today)) {
						// 今日が対象の日なら読み込む。
						loadPresents(inputStream, 0);
						// 二重プレゼントがあるならもう一度。
						if (isTwice)
							loadPresents(inputStream, 1);
						return;
					}
				}
			}
			dates = null;
			isTwice = false;
			isTextureSpecial = false;
		} catch (Exception e) {
			OfalenLog.error("Error on getting presents.", "OfalenModAnniversaryHandler");
			e.printStackTrace();
		}
	}

	/** プレゼントのデータを読み込み、presentsを設定する。 */
	private static void loadPresents(InputStream inputStream, int num) {
		String str;
		for (int i = 0; i < 54; i++) {
			str = OfalenUtil.readLine(inputStream);
			if (str == null || str.charAt(0) == '+') {
				presents[num] = new ItemStack[54];
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
					presents[num][i] = new ItemStack(item, 1, Integer.valueOf(array[2]));
				}
			} catch (Exception e) {
				presents[num][i] = null;
				OfalenLog.error("Error on loading presents.", "OfalenModAnniversaryHandler");
				e.printStackTrace();
			}
		}
	}

	/** presentedDateのデータをOfalenModPresentedDate.txtに保存する。 */
	public static void save() {
		try {
			File file = new File(new File(".\\").getParentFile(), "config\\OfalenModPresentedDate.txt");
			if (!file.exists() || !file.isFile() || !file.canWrite()) {
				if (!file.createNewFile()) {
					OfalenLog.error("Failed to create new file : OfalenModPresentedDate.txt", "OfalenModAnniversaryHandler");
					return;
				}
			}
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			for (Entry<String, String> entry : presentedDate.entrySet()) {
				bw.write(entry.getKey() + "," + entry.getValue());
				bw.newLine();
			}
			bw.close();
		} catch (Exception e) {
			OfalenLog.error("Error on saving OfalenModPresentedDate.txt.", "OfalenModAnniversaryHandler");
			e.printStackTrace();
		}
	}

	/** プレイヤーにプレゼントボックスを渡していたかどうかを確認し、渡していなければプレゼントボックスをドロップする。 */
	public static void checkPlayer(EntityPlayer player) {
		String name = player.getCommandSenderName();
		if (OfalenUtil.isClient())
			name = player.worldObj.getSaveHandler().getWorldDirectoryName();
		// リストに載っているなら終了。
		if (presentedDate.containsKey(name))
			return;
		// プレゼントボックスを渡していなかったら渡す。
		OfalenUtil.dropItemStackNearEntity(new ItemStack(OfalenModBlockCore.boxPresent), player);
		presentedDate.put(name, "0/0/0");
	}

	/** プレゼントの配列をコピーして返す。 */
	public static ItemStack[] getPresents(EntityPlayer player) {
		String name = player.getCommandSenderName();
		if (OfalenUtil.isClient())
			name = player.worldObj.getSaveHandler().getWorldDirectoryName();
		if (!presentedDate.containsKey(name) || dates == null)
			return null;
		String s = presentedDate.get(name);
		// 同じ記念日で一回も開けていないかどうか。
		boolean flag = true;
		for (String date : dates) {
			// 既に同じ記念日で渡していたならnull。
			if (s.equals(date))
				return null;
			if (s.equals(date + '-'))
				flag = false;
		}
		presentedDate.put(name, today);
		if (!isTwice || flag) {
			if (isTwice) {
				// 二回開けられる記念日で、一回目なら'-'をつける。
				presentedDate.put(name, today + '-');
			}
			return OfalenUtil.copyItemStacks(presents[0]);
		}
		// 二回開けられる記念日で、二回目なら別のプレゼントを返す。
		return OfalenUtil.copyItemStacks(presents[1]);
	}
}