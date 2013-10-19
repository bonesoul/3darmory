package toonviewer;

import java.awt.image.*;
import java.io.*;
import java.nio.*;
import javax.imageio.*;
import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.ImageUtil;

public class Screenshot {

	private Screenshot() {

		super();

	}

	public static BufferedImage readToBufferedImage(GL gl, int width, int height)
			throws GLException {

		return readToBufferedImage(gl, width, height, false);

	}

	public static BufferedImage readToBufferedImage(GL gl, int width,
			int height, boolean alpha) throws GLException {

		return readToBufferedImage(gl, 0, 0, width, height, alpha);

	}

	public static BufferedImage readToBufferedImage(GL gl, int x, int y,
			int width, int height, boolean alpha) throws GLException {

		int bufImgType = (alpha ? BufferedImage.TYPE_4BYTE_ABGR
				: BufferedImage.TYPE_3BYTE_BGR);

		int readbackType = (alpha ? GL.GL_ABGR_EXT : GL.GL_BGR);

		if (alpha) {

			checkExtABGR();

		}

		BufferedImage image = new BufferedImage(width, height, bufImgType);

		PixelStorageModes psm = new PixelStorageModes();

		psm.save(gl);

		gl.glReadPixels(x, y, width, height, readbackType, GL.GL_UNSIGNED_BYTE,
				ByteBuffer.wrap(((DataBufferByte) image.getRaster()
						.getDataBuffer()).getData()));

		psm.restore(gl);

		ImageUtil.flipImageVertically(image);

		return image;

	}

	public static void writeToFile(GL gl, File file, int width, int height)
			throws IOException, GLException {

		writeToFile(gl, file, width, height, false);

	}

	public static void writeToFile(GL gl, File file, int width, int height,
			boolean alpha) throws IOException, GLException {

		writeToFile(gl, file, 0, 0, width, height, alpha);

	}

	public static void writeToFile(GL gl, File file, int x, int y, int width,
			int height, boolean alpha) throws IOException, GLException {

		String fileSuffix = "png"; // FileUtil.getFileSuffix(file);

		if (alpha && (fileSuffix.equals("jpg") || fileSuffix.equals("jpeg"))) {

			alpha = false;

		}

		BufferedImage image = readToBufferedImage(gl, x, y, width, height,
				alpha);

		if (!ImageIO.write(image, fileSuffix, file)) {

			throw new IOException("Unsupported file format " + fileSuffix);

		}

	}

	private static int glGetInteger(GL gl, int pname, int[] tmp) {

		gl.glGetIntegerv(pname, tmp, 0);

		return tmp[0];

	}

	private static void checkExtABGR() {

		GL gl = GLU.getCurrentGL();

		if (!gl.isExtensionAvailable("GL_EXT_abgr")) {

			throw new IllegalArgumentException(
					"Saving alpha channel requires GL_EXT_abgr");

		}

	}

	static class PixelStorageModes {

		int packAlignment;

		int packRowLength;

		int packSkipRows;

		int packSkipPixels;

		int packSwapBytes;

		int[] tmp = new int[1];

		void save(GL gl) {

			packAlignment = glGetInteger(gl, GL.GL_PACK_ALIGNMENT, tmp);

			packRowLength = glGetInteger(gl, GL.GL_PACK_ROW_LENGTH, tmp);

			packSkipRows = glGetInteger(gl, GL.GL_PACK_SKIP_ROWS, tmp);

			packSkipPixels = glGetInteger(gl, GL.GL_PACK_SKIP_PIXELS, tmp);

			packSwapBytes = glGetInteger(gl, GL.GL_PACK_SWAP_BYTES, tmp);

			gl.glPixelStorei(GL.GL_PACK_ALIGNMENT, 1);

			gl.glPixelStorei(GL.GL_PACK_ROW_LENGTH, 0);

			gl.glPixelStorei(GL.GL_PACK_SKIP_ROWS, 0);

			gl.glPixelStorei(GL.GL_PACK_SKIP_PIXELS, 0);

			gl.glPixelStorei(GL.GL_PACK_SWAP_BYTES, 0);

		}

		void restore(GL gl) {

			gl.glPixelStorei(GL.GL_PACK_ALIGNMENT, packAlignment);

			gl.glPixelStorei(GL.GL_PACK_ROW_LENGTH, packRowLength);

			gl.glPixelStorei(GL.GL_PACK_SKIP_ROWS, packSkipRows);

			gl.glPixelStorei(GL.GL_PACK_SKIP_PIXELS, packSkipPixels);

			gl.glPixelStorei(GL.GL_PACK_SWAP_BYTES, packSwapBytes);

		}

	}

}
