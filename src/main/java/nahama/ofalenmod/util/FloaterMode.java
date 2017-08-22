package nahama.ofalenmod.util;

import java.util.Arrays;

public class FloaterMode {
	private final String name;
	private final byte formFlight;
	private final float factor;
	private final float[] params;

	public FloaterMode(String name, byte formFlight, float factor, float... params) {
		this.name = name;
		this.formFlight = formFlight;
		this.factor = factor;
		this.params = new float[getRequiredParamAmount(formFlight)];
		for (int i = 0; i < this.params.length; i++) {
			if (params.length > i) {
				this.params[i] = params[i];
			} else {
				this.params[i] = 0.0F;
			}
		}
	}

	public static int getRequiredParamAmount(int modeFlight) {
		switch (modeFlight) {
		case 1:
			return 3;
		case 2:
			return 1;
		case 3:
			return 2;
		}
		return 0;
	}

	public String getName() {
		return name;
	}

	public byte getFlightForm() {
		return formFlight;
	}

	public float getFactor() {
		return factor;
	}

	public float getParam(int i) {
		if (params.length <= i)
			return 0.0F;
		return params[i];
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FloaterMode) {
			FloaterMode mode = (FloaterMode) obj;
			return this.name.equals(mode.name) && this.formFlight == mode.formFlight && this.factor == mode.factor && Arrays.equals(this.params, mode.params);
		}
		return false;
	}
}
