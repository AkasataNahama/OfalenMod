package nahama.ofalenmod.handler;

import java.util.HashMap;

import net.minecraft.world.World;

public class OfalenTeleportHandler {

	/** ディメンションIDと対応したインスタンスのマップ。 */
	private static HashMap<Integer, OfalenTeleportHandler> instanceList = new HashMap<Integer, OfalenTeleportHandler>();
	/** 対応するディメンションID */
	private int id;
	/** テレポートマーカーのチャンネルと座標のマップ。 */
	private HashMap<Integer, int[]> markerList = new HashMap<Integer, int[]>();

	private OfalenTeleportHandler(int id) {
		this.id = id;
	}

	/** 初期化処理。 */
	public static void init() {
		// インスタンスのリストをリセットする。
		instanceList.clear();
	}

	/** ディメンションに対応したマネージャーのインスタンスを返す。 */
	public static OfalenTeleportHandler getInstance(World world) {
		if (world.isRemote)
			return null;
		// ディメンションIDを取得し、そのディメンションに対応するマネージャーがないなら生成して返す。
		int id = world.provider.dimensionId;
		if (!instanceList.containsKey(id)) {
			instanceList.put(id, new OfalenTeleportHandler(id));
		}
		return instanceList.get(id);
	}

	/** チャンネルが登録されているどうか。 */
	public boolean isChannelValid(int channel) {
		if (channel < 1)
			return false;
		return markerList.containsKey(channel);
	}

	/** チャンネルに対応した座標を返す。 */
	public int[] getCoord(int channel) {
		if (channel < 1)
			return null;
		return markerList.get(channel);
	}

	/** チャンネルと対応した座標を登録する。 */
	public void registerMarker(int channel, int[] marker) {
		// すでにチャンネルが登録されていたら登録しない。
		if (this.isChannelValid(channel) || channel < 1)
			return;
		markerList.put(channel, marker);
	}

	/** チャンネルを無効にする。 */
	public void removeMarker(int channel) {
		markerList.remove(channel);
	}

}
