package toonviewer;

import java.io.IOException;
import java.nio.ByteBuffer;
import javax.vecmath.Point3f;

class Mesh {

	protected boolean show;
	protected int geoset;
	protected short geosetId;
	protected char mIndexStart;
	protected char mIndexCount;
	protected short mMaterial;
	protected boolean transparent;
	protected boolean swrap;
	protected boolean twrap;
	protected boolean noZWrite;
	protected boolean envmap;
	protected boolean unlit;
	protected boolean billboard;
	protected short blendmode;
	protected Point3f color;
	protected float opacity;
	protected short colorIndex;
	protected short opacityIndex;
	protected short texAnim;

	public Mesh() {
		geoset = 0;
		mIndexStart = mIndexCount = '\0';
		mMaterial = 0;
		transparent = swrap = twrap = noZWrite = envmap = unlit = billboard = show = false;
		blendmode = 1;
		color = null;
		opacity = 1.0F;
		colorIndex = opacityIndex = texAnim = 0;
	}

	public void read(ByteBuffer buf) throws IOException {
		byte inByte = 0;
		inByte = buf.get();
		show = inByte != 0;
		geoset = buf.getInt();
		geosetId = buf.getShort();
		mMaterial = buf.getShort();
		mIndexStart = buf.getChar();
		mIndexCount = buf.getChar();
		inByte = buf.get();
		transparent = inByte != 0;
		blendmode = buf.getShort();
		inByte = buf.get();
		swrap = inByte != 0;
		inByte = buf.get();
		twrap = inByte != 0;
		inByte = buf.get();
		noZWrite = inByte != 0;
		inByte = buf.get();
		envmap = inByte != 0;
		inByte = buf.get();
		unlit = inByte != 0;
		inByte = buf.get();
		billboard = inByte != 0;
		color = new Point3f(buf.getFloat(), buf.getFloat(), buf.getFloat());
		opacity = buf.getFloat();
	}

	public void readExt(ByteBuffer buf) throws IOException {
		texAnim = buf.getShort();
	}
}
