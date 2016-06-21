package nahama.ofalenmod.handler;

import nahama.ofalenmod.Log;

import java.util.HashMap;

public class OfalenTeleportHandler {

	/** テレポートマーカーのチャンネルと座標のマップ。 */
	private static HashMap<Integer, MarkerPos> markerList = new HashMap<>();

	/** 初期化処理。 */
	public static void init() {
		// マーカーのリストをリセットする。
		markerList.clear();
	}

	/** チャンネルが登録されているどうか。 */
	public static boolean isChannelValid(int channel) {
		return channel >= 1 && markerList.containsKey(channel);
	}

	/** チャンネルに対応した座標を返す。 */
	public static MarkerPos getCoord(int channel) {
		if (channel < 1)
			return null;
		return markerList.get(channel);
	}

	/** チャンネルと対応した座標を登録する。 */
	public static boolean registerMarker(int channel, byte id, int x, int y, int z) {
		if (channel < 1)
			return false;
		MarkerPos marker = markerList.get(channel);
		// すでにチャンネルが登録されていたら登録しない。
		if (marker != null)
			return marker.equals(new MarkerPos(id, x, y, z));
		markerList.put(channel, new MarkerPos(id, x, y, z));
		return true;
	}

	/** チャンネルを無効にする。 */
	public static void removeMarker(int channel) {
		markerList.remove(channel);
	}

	public static class MarkerPos {
		private byte id;
		private int x, y, z;

		public MarkerPos(byte id, int x, int y, int z) {
			this.id = id;
			this.x = x;
			this.y = y;
			this.z = z;
		}

		public byte getId() {
			return id;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public int getZ() {
			return z;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null)
				return false;
			if (super.equals(obj))
				return true;
			if (!(obj instanceof MarkerPos))
				return false;
			MarkerPos pos = (MarkerPos) obj;
			return id == pos.id && x == pos.x && y == pos.y && z == pos.z;
		}
	}

}
