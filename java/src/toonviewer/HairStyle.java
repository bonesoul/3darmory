package toonviewer;

import java.io.IOException;
import java.nio.ByteBuffer;

class HairStyle {
	class HairTexture {

		String texture;
		String lowerTexture;
		String upperTexture;

		public HairTexture() {
			texture = null;
			lowerTexture = null;
			upperTexture = null;
		}
	}

	int geoset;
	int index;
	HairTexture hair[];

	public HairStyle() {
		geoset = 0;
		index = 0;
		hair = null;
	}

	public void read(ByteBuffer buf, int numColors) throws IOException {
		geoset = buf.getInt();
		index = buf.getInt();
		hair = new HairTexture[numColors];
		for (int i = 0; i < numColors; i++) {
			hair[i] = new HairTexture();
			hair[i].texture = Model.readString(buf);
			hair[i].lowerTexture = Model.readString(buf);
			hair[i].upperTexture = Model.readString(buf);
		}

	}
}
