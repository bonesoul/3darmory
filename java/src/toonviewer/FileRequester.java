package toonviewer;

import java.io.InputStream;

interface FileRequester {

	public abstract void RequestComplete(Object obj, int i,
			InputStream inputstream, String s);

	public abstract void RequestFailed(Object obj, int i);
}
