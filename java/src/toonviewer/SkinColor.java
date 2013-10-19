package toonviewer;

import java.io.IOException;
import java.nio.ByteBuffer;

class SkinColor {
	class FaceTexture {

		String lowerTexture;
		String upperTexture;

		public FaceTexture() {
			lowerTexture = null;
			upperTexture = null;
		}
	}

	String baseTexture;
	String furTexture;
	String pantiesTexture;
	String braTexture;
	FaceTexture face[];

	public SkinColor() {
		baseTexture = null;
		furTexture = null;
		pantiesTexture = null;
		braTexture = null;
		face = null;
	}

	public void read(ByteBuffer buf, int numFaces) throws IOException {
		baseTexture = Model.readString(buf);
		furTexture = Model.readString(buf);
		pantiesTexture = Model.readString(buf);
		braTexture = Model.readString(buf);
		face = new FaceTexture[numFaces];
		for (int i = 0; i < numFaces; i++) {
			face[i] = new FaceTexture();
			face[i].lowerTexture = Model.readString(buf);
			face[i].upperTexture = Model.readString(buf);
		}

	}
}
