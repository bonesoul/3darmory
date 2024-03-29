package toonviewer;

import java.io.IOException;
import java.nio.ByteBuffer;
import javax.vecmath.*;

class Vertex
{

	protected Point3f	mPosition;
	protected Vector3f	mNormal;
	protected Point2f	mTexCoord;
	short	           bones[];
	short	           weights[];

	public Vertex()
	{
		mPosition = null;
		mNormal = null;
		mTexCoord = null;
		bones = weights = null;
	}

	public void read(ByteBuffer buf) throws IOException
	{
		mPosition = new Point3f(buf.getFloat(), buf.getFloat(), buf
		                .getFloat());
		mNormal = new Vector3f(buf.getFloat(), buf.getFloat(), buf
		                .getFloat());
		mNormal.normalize();
		mTexCoord = new Point2f(buf.getFloat(), buf.getFloat());
	}

	public void readExt(ByteBuffer buf) throws IOException
	{
		mPosition = new Point3f(buf.getFloat(), buf.getFloat(), buf
		                .getFloat());
		mNormal = new Vector3f(buf.getFloat(), buf.getFloat(), buf
		                .getFloat());
		mNormal.normalize();
		bones = new short[4];
		weights = new short[4];
		for ( int i = 0; i < 4; i++ )
		{
			byte inByte = buf.get();
			bones[i] = (short) (inByte & 0xff);
		}

		for ( int i = 0; i < 4; i++ )
		{
			byte inByte = buf.get();
			weights[i] = (short) (inByte & 0xff);
		}

	}
}
