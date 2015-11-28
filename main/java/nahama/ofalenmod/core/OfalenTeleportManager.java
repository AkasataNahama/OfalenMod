package nahama.ofalenmod.core;

import java.util.HashMap;
import java.util.Iterator;

import net.minecraft.world.World;

public class OfalenTeleportManager {

	private static HashMap<Integer, OfalenTeleportManager> instanceList = new HashMap<Integer, OfalenTeleportManager>();
	private int id;
	private HashMap<Integer, int[]> markerList = new HashMap<Integer, int[]>();

	private OfalenTeleportManager(int id) {
		this.id = id;
	}

	public static void init() {
		Iterator<OfalenTeleportManager> iterator = instanceList.values().iterator();
		while (iterator.hasNext()) {
			iterator.next().markerList.clear();
		}
		instanceList.clear();
	}

	/** ディメンションに対応したマネージャーのインスタンスを返す。 */
	public static OfalenTeleportManager getInstance(World world) {
		if (world.isRemote)
			return null;
		// ディメンションIDを取得し、そのディメンションに対応するマネージャーがないなら生成して返す。
		int id = world.provider.dimensionId;
		if (!instanceList.containsKey(id)) {
			instanceList.put(id, new OfalenTeleportManager(id));
		}
		return instanceList.get(id);
	}

	public boolean isChannelValid(int channel) {
		if (channel < 1)
			return false;
		return markerList.containsKey(channel);
	}

	public int[] getCoord(int channel) {
		if (channel < 1)
			return null;
		return markerList.get(channel);
	}

	public void registerMarker(int channel, int[] marker) {
		if (this.isChannelValid(channel) || channel < 1)
			return;
		markerList.put(channel, marker);
	}

	public void removeMarker(int channel) {
		markerList.remove(channel);
	}

}
