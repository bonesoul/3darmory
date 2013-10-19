package toonviewer;

import java.io.IOException;
import java.nio.ByteBuffer;

class FacialHair {
	class FacialHairTexture {

		String lowerTexture;
		String upperTexture;

		public FacialHairTexture() {
			lowerTexture = null;
			upperTexture = null;
		}
	}

	int geoset1;
	int geoset2;
	int geoset3;
	FacialHairTexture hair[];

	public FacialHair() {
		geoset1 = 0;
		geoset2 = 0;
		geoset3 = 0;
		hair = null;
	}

	public void read(ByteBuffer buf, int numColors) throws IOException {
		geoset1 = buf.getInt();
		geoset2 = buf.getInt();
		geoset3 = buf.getInt();
		hair = new FacialHairTexture[numColors];
		for (int i = 0; i < numColors; i++) {
			hair[i] = new FacialHairTexture();
			hair[i].lowerTexture = Model.readString(buf);
			hair[i].upperTexture = Model.readString(buf);
		}

	}
}
