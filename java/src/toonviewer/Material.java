package toonviewer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import javax.imageio.ImageIO;

class Material implements FileRequester {

	protected String mFilename;
	protected int mTextureId;
	protected BufferedImage mBufferedImage;
	protected int mSpecialTexture;
	protected boolean mSkip;

	public void RequestComplete(Object origin, int id, InputStream stream,
			String path) {
		try {
			mBufferedImage = ImageIO.read(stream);
			Model model = (Model) origin;
			if (id == 1)
				model.mLoadedTextures.add(this);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void RequestFailed(Object origin, int id) {
		mSkip = true;
	}

	public Material() {
		mFilename = null;
		mTextureId = 0;
		mBufferedImage = null;
		mSkip = false;
	}

	public Material(String tex) {
		mFilename = tex;
		mSpecialTexture = -1;
	}

	public void read(ByteBuffer buf) throws IOException {
		mFilename = Model.readString(buf);
		mSpecialTexture = buf.getInt();
	}
}
