package toonviewer;

import java.awt.image.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javax.media.opengl.GL;

public class ModelTexture {

	public ModelTexture() {
	}

	private static int createTextureID(GL gl) {
		int tmp[] = new int[1];
		gl.glGenTextures(1, tmp, 0);
		return tmp[0];
	}

	public static int getTexture(GL gl, BufferedImage image) throws IOException {
		int tex = getTexture(gl, image, 3553, 6408, 9729, 9729);
		return tex;
	}

	public static int getTexture(GL gl, BufferedImage image, int target,
			int dstPixelFormat, int minFilter, int magFilter)
			throws IOException {
		int srcPixelFormat = 0;
		int textureID = createTextureID(gl);
		gl.glBindTexture(target, textureID);
		if (image.getColorModel().hasAlpha())
			srcPixelFormat = 6408;
		else
			srcPixelFormat = 6407;
		ByteBuffer textureBuffer = convertImageData(image);
		if (textureBuffer == null)
			return 0;
		if (target == 3553) {
			gl.glTexParameteri(target, 10241, minFilter);
			gl.glTexParameteri(target, 10240, magFilter);
		}
		gl.glTexImage2D(target, 0, dstPixelFormat, image.getWidth(), image
				.getHeight(), 0, srcPixelFormat, 5121, textureBuffer);
		return textureID;
	}

	private static ByteBuffer convertImageData(BufferedImage bufferedImage) {
		ByteBuffer imageBuffer = null;
		int byteSize = bufferedImage.getWidth() * bufferedImage.getHeight() * 4;
		imageBuffer = ByteBuffer.allocateDirect(byteSize);
		imageBuffer.order(ByteOrder.nativeOrder());
		if (bufferedImage.getType() == 0) {
			byte data[] = ((DataBufferByte) bufferedImage.getRaster()
					.getDataBuffer()).getData();
			if (bufferedImage.getColorModel().hasAlpha()) {
				imageBuffer.put(data, 0, data.length);
			} else {
				for (int i = 0; i < data.length; i += 3) {
					imageBuffer.put(data[i]);
					imageBuffer.put(data[i + 1]);
					imageBuffer.put(data[i + 2]);
				}

			}
		} else if (bufferedImage.getType() == 3) {
			int data[] = ((DataBufferInt) bufferedImage.getRaster()
					.getDataBuffer()).getData();
			for (int i = 0; i < data.length; i++) {
				byte a = (byte) ((data[i] & 0xff000000) >> 24);
				byte r = (byte) ((data[i] & 0xff0000) >> 16);
				byte g = (byte) ((data[i] & 0xff00) >> 8);
				byte b = (byte) (data[i] & 0xff);
				imageBuffer.put(r);
				imageBuffer.put(g);
				imageBuffer.put(b);
				imageBuffer.put(a);
			}

		} else {
			System.out.println(bufferedImage.getRaster().getDataBuffer()
					.getClass());
			System.out.println(bufferedImage.getType());
			System.out.println(bufferedImage.getColorModel().hasAlpha());
			return null;
		}
		imageBuffer.flip();
		return imageBuffer;
	}
}
