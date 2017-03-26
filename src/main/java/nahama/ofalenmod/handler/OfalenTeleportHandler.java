package nahama.ofalenmod.handler;

import java.util.HashMap;

public class OfalenTeleportHandler {
	/** テレポートマーカーのチャンネルと座標のマップ。 */
	private static HashMap<Short, MarkerPos> listMarker = new HashMap<Short, MarkerPos>();

	/** 初期化処理。 */
	public static void init() {
		// マーカーのリストをリセットする。
		listMarker.clear();
	}

	/** チャンネルが登録されているどうか。 */
	public static boolean isChannelValid(int channel) {
		return channel >= 1 && listMarker.containsKey((short)channel);
	}

	/** チャンネルに対応した座標を返す。 */
	public static MarkerPos getCoord(short channel) {
		if (channel < 1)
			return null;
		return listMarker.get(channel);
	}

	/** チャンネルと対応した座標を登録する。 */
	public static boolean registerMarker(short channel, byte id, short x, short y, short z) {
		if (channel < 1)
			return false;
		MarkerPos marker = listMarker.get(channel);
		// すでにチャンネルが登録されていたら登録しない。
		if (marker != null)
			return marker.equals(new MarkerPos(id, x, y, z));
		listMarker.put(channel, new MarkerPos(id, x, y, z));
		return true;
	}

	/** チャンネルを無効にする。 */
	public static void removeMarker(short channel) {
		listMarker.remove(channel);
	}

	public static class MarkerPos {
		private byte id;
		private short x, y, z;

		public MarkerPos(byte id, short x, short y, short z) {
			this.id = id;
			this.x = x;
			this.y = y;
			this.z = z;
		}

		public byte getId() {
			return id;
		}

		public short getX() {
			return x;
		}

		public short getY() {
			return y;
		}

		public short getZ() {
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
