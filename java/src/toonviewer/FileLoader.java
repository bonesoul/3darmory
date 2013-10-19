package toonviewer;

import java.net.URL;

class FileLoader extends Thread {

	String file;
	FileRequester requester;
	int id;
	Object origin;
	Boolean got_full_url;

	FileLoader(int i, FileRequester r, String f) {
		origin = r;
		requester = r;
		file = f;
		id = i;
		got_full_url=false;
	}

	FileLoader(int i, Object o, FileRequester r, String f) {
		origin = o;
		requester = r;
		file = f;
		id = i;
		got_full_url=false;
	}
	
	FileLoader(int i,Object o,FileRequester r,String f,Boolean _full_url)
	{
		origin = o;
		requester = r;
		file = f;
		id = i;
		got_full_url=_full_url;
	}

	public void run() {
		try {
			String urlFile = file.replaceAll(" ", "%20");
			URL url;
			if(!got_full_url)
				url = new URL(ToonViewerApplet.contentPath + urlFile);
			else
				url=new URL(urlFile);
			requester.RequestComplete(origin, id, ToonViewerApplet.downloadWithProgress(url), file);
		} catch (Exception ex) {
			requester.RequestFailed(origin, id);
			ex.printStackTrace();
		}
	}
}
