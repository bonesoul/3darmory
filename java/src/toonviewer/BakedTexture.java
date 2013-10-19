package toonviewer;

import java.io.IOException;
import java.nio.ByteBuffer;

class BakedTexture implements Comparable {

	byte mRegion;
	byte mLayer;
	String mTextureName;
	Material mMaterial;

	public BakedTexture() {
		mRegion = mLayer = 0;
		mTextureName = null;
		mMaterial = null;
	}

	public BakedTexture(Model model, int region, int layer, String texture) {
		mRegion = (byte) region;
		mLayer = (byte) layer;
		mTextureName = texture;
		mMaterial = new Material(mTextureName);
		load(model);
	}

	public void read(ByteBuffer buf) throws IOException {
		mRegion = buf.get();
		mLayer = buf.get();
		mTextureName = Model.readString(buf);
		if (mTextureName.length() > 0)
			mMaterial = new Material(mTextureName);
	}

	public void load(Model model) {
		if (mMaterial == null) {
			return;
		} else {
			FileLoader fl = new FileLoader(2, model, mMaterial,
					("textures/" + mMaterial.mFilename).toLowerCase());
			fl.start();
			return;
		}
	}

	public int compareTo(Object o) {
		BakedTexture t = (BakedTexture) o;
		if (mLayer < t.mLayer)
			return -1;
		return mLayer != t.mLayer ? 1 : 0;
	}
}
