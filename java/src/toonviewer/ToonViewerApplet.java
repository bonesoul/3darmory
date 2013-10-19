package toonviewer;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.j2d.TextRenderer;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;
import java.applet.Applet;
import java.applet.AppletContext;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;
import java.awt.image.BufferedImage;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JComboBox;

public class ToonViewerApplet extends Applet implements GLEventListener,
		MouseListener, MouseMotionListener, MouseWheelListener,  ActionListener , ItemListener, FileRequester {

	static final long serialVersionUID = 0x21eacL;
	static String argv[];
	Animator animator;
	static Panel appletPanel;
	static boolean fileDownloading = false;
	Texture logoTexture;
	InputStream logoTextureStream;
	private int prevMouseX;
	private int prevMouseY;
	private boolean mouseRButtonDown;
	private boolean mouseLButtonDown;
	private boolean mouseFromLButton;
	static Camera cam;
	static Model model;
	static String newModel = null;
	static String modelFile = null;
	static String equipList = null;
	static int modelType = 0;
	public static String contentPath = null;
	static String bgColor = null;
	
	Color color;
	static int hairColor;
	static int hairType;
	static int faceType;
	static int skinColor;
	static int facialHairType;
	static int facialHairColor;
	static int globalTime = 0;
	static long lastTime = 0L;
	static long delta = 0L;
	public static int mesh = 0;
	private static ArrayList downloads;
	private static TextRenderer mTextRender;
	static int canvasWidth;
	static int canvasHeight;
	double xDelta;
	double yDelta;
	public static final int MT_ITEM = 1;
	public static final int MT_HELM = 2;
	public static final int MT_SHOULDER = 4;
	public static final int MT_NPC = 8;
	public static final int MT_CHAR = 16;
	public static final int MT_HUMAN_NPC = 32;
	public static final int MT_OBJECT = 64;
	
	
	BufferedImage img_screenshot=null;
	static Boolean take_screenshot=false;
	static Boolean options_rendered=false;  
	static GL screenshot_gl=null;
	static AppletContext applet_context=null;
	
	JPanel ctrl_panel;
	static JButton btn_screenshot;
	
	/* 
	static JComboBox cmb_hair_styles;
	static JComboBox cmb_hair_colors;
	static JComboBox cmb_face_types;
	static JComboBox cmb_skin_colors;
	static JComboBox cmb_facial_hairs;
	static JComboBox cmb_facial_hair_colors;
	*/
	
	static JComboBox cmb_animations;
	
	static String toon_id;
	
	public ToonViewerApplet() {
		logoTexture = null;
		logoTextureStream = null;
		mouseRButtonDown = false;
		mouseLButtonDown = false;
		mouseFromLButton = false;
		color = null;
		xDelta = 0.0D;
		yDelta = 0.0D;
	}

	public void init() {
		appletPanel = this;
		applet_context=getAppletContext();
		setLayout(new BorderLayout());

		GLCapabilities caps = new GLCapabilities();
		caps.setSampleBuffers(true);
		caps.setNumSamples(4);
		caps.setHardwareAccelerated(true);
		caps.setDoubleBuffered(true);
		GLCanvas canvas = new GLCanvas(caps);
		canvas.addGLEventListener(new ToonViewerApplet());
		canvas.setSize(getSize());
		add(canvas, "Center");
		
		contentPath = "http://static.wowhead.com/modelviewer/";  // //getParameter("contentPath");
		newModel = getParameter("model");
		bgColor = getParameter("bgColor"); 
		equipList =  getParameter("equipList");  
		modelType = Integer.parseInt(getParameter("modelType"));
		hairType = getParameter("ha") == null ?  0 : Integer.parseInt(getParameter("ha"));
		hairColor = getParameter("hc") == null ? 0 : Integer.parseInt(getParameter("hc"));
		faceType = getParameter("fa") == null ? 0 : Integer.parseInt(getParameter("fa"));
		skinColor = getParameter("sk") == null ? 0 : Integer.parseInt(getParameter("sk"));
		facialHairType = getParameter("fh") == null ? 0 : Integer.parseInt(getParameter("fh"));
		facialHairColor = getParameter("fc") == null ? 0 : Integer.parseInt(getParameter("fc"));
		
		toon_id=getParameter("t"); // for screenshot uploading - the current toons db id


		downloads = new ArrayList(8);
		mTextRender = new TextRenderer(new Font("Arial", 1, 12));
		
		// control panel
		ctrl_panel = new JPanel();
		ctrl_panel.setBackground(Color.decode(bgColor));
		
		//animations
		cmb_animations=new JComboBox();
		cmb_animations.addItem("Idle");
		//cmb_animations.addItem("Cheer");
		//cmb_animations.addItem("Dance");
		//cmb_animations.addItem("Cower");
		cmb_animations.addItem("Walk");
		cmb_animations.addItemListener(this);
		//cmb_animations.setEnabled(false);
		this.ctrl_panel.add(cmb_animations);
	

		
		/*
		// model options
		cmb_hair_styles = new JComboBox();
		cmb_hair_styles.addItem("Hair Style");
		cmb_hair_styles.addItemListener(this);
		//cmb_hair_styles.setEnabled(false);
		this.ctrl_panel.add(cmb_hair_styles);
		
		cmb_hair_colors=new JComboBox();
		cmb_hair_colors.addItem("Hair Color");
		cmb_hair_colors.addItemListener(this);
		//cmb_hair_colors.setEnabled(false);
		this.ctrl_panel.add(cmb_hair_colors);
		
		cmb_face_types=new JComboBox();
		cmb_face_types.addItem("Face Type");
		cmb_face_types.addItemListener(this);
		//cmb_face_types.setEnabled(false);
		this.ctrl_panel.add(cmb_face_types);
		
		cmb_skin_colors=new JComboBox();
		cmb_skin_colors.addItem("Skin Color");
		cmb_skin_colors.addItemListener(this);
		//cmb_skin_colors.setEnabled(false);
		this.ctrl_panel.add(cmb_skin_colors);
		
		cmb_facial_hairs=new JComboBox();
		cmb_facial_hairs.addItem("Facial Hair");
		cmb_facial_hairs.addItemListener(this);
		//cmb_facial_hairs.setEnabled(false);
		this.ctrl_panel.add(cmb_facial_hairs);
		
		cmb_facial_hair_colors=new JComboBox();
		cmb_facial_hair_colors.addItem("Facial Hair Color");
		cmb_facial_hair_colors.addItemListener(this);
		//cmb_facial_hair_colors.setEnabled(false);
		this.ctrl_panel.add(cmb_facial_hair_colors);
		*/
		
		// screenshot button
		btn_screenshot=new JButton("Screenshot");
		btn_screenshot.addActionListener(this);
		ctrl_panel.add(btn_screenshot);
		
		add(ctrl_panel,"South");
		
		animator = new Animator(canvas);
		animator.setRunAsFastAsPossible(false);
	}
	
	public void itemStateChanged(ItemEvent ie) {
		int val;
		
		/*
		if(ie.getSource()==cmb_hair_styles)
		{
			try {val=Integer.parseInt(ie.getItem().toString());}
			catch(Exception e) { val=0;}
			setHairStyle(val);
		}
		else if(ie.getSource()==cmb_hair_colors)
		{
			try {val=Integer.parseInt(ie.getItem().toString());}
			catch(Exception e) { val=0;}
			setHairColor(val);
		}
		else if(ie.getSource()==cmb_face_types)
		{
			try {val=Integer.parseInt(ie.getItem().toString());}
			catch(Exception e) { val=0;}
			setFaceType(val);
		}
		else if(ie.getSource()==cmb_skin_colors)
		{
			try {val=Integer.parseInt(ie.getItem().toString());}
			catch(Exception e) { val=0;}
			setSkinColor(val);
		}
		else if(ie.getSource()==cmb_facial_hairs)
		{
			try {val=Integer.parseInt(ie.getItem().toString());}
			catch(Exception e) { val=0;}
			setFacialHairStyle(val);
		}
		else if(ie.getSource()==cmb_facial_hair_colors)
		{
			try {val=Integer.parseInt(ie.getItem().toString());}
			catch(Exception e) { val=0;}
			setFacialHairColor(val);
		}
		*/
		if(ie.getSource()==cmb_animations)
		{
			if(model.mActiveAnims!=null && model.mActiveAnims.length > 0) // if we animations are loaded
			{
				String anim=ie.getItem().toString();
				if(anim=="Idle")
					this.animStop();
				//else if(anim=="Cheer")
				//	this.animCheer();
				//else if(anim=="Dance")
				//	this.animDance();
				//else if(anim=="Cower")
				//	this.animCower();
				else if(anim=="Walk")
					this.animWalk();
			}
		} 
	}

	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource()==btn_screenshot)
			take_screenshot=true;
	}
	
	/*
	public void load_complete()
	{
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	// render config options
            	
            
            	if(getHairStyles()>0)
            	{
            		//cmb_hair_styles.setEnabled(true);
            		for(int i=0;i<getHairStyles();i++)
            			cmb_hair_styles.addItem(i);
            	}
            	
            	if(getHairColors()>0)
            	{
            		//cmb_hair_colors.setEnabled(true);
            		for(int i=0;i<getHairColors();i++)
            			cmb_hair_colors.addItem(i);
            	}

            	if(getFaceTypes()>0)
            	{
            		//cmb_face_types.setEnabled(true);
            		for(int i=0;i<getFaceTypes();i++)
            			cmb_face_types.addItem(i);
            	}
            	
            	if(getSkinColors()>0)
            	{
            		//cmb_skin_colors.setEnabled(true);
            		for(int i=0;i<getSkinColors();i++)
            			cmb_skin_colors.addItem(i);
            	}
            	
            	if(getFacialHairStyles()>0)
            	{
            		//cmb_facial_hairs.setEnabled(true);
            		for(int i=0;i<getFacialHairStyles();i++)
            			cmb_facial_hairs.addItem(i);
            	}
            	
            	if(getFacialHairColors()>0)
            	{
            		//cmb_facial_hair_colors.setEnabled(true);
            		for(int i=0;i<getFacialHairColors();i++)
            			cmb_facial_hair_colors.addItem(i);
            	}
            	
            	//cmb_animations.setEnabled(true);
            }
          });
		this.options_rendered=true;
	}
	*/
	
    
	public void start() {
		animator.start();
	}

	public void stop() {
		if (animator != null)
			animator.stop();
	}

	public void RequestComplete(Object origin, int id, InputStream stream,
			String path) {
		if (id == 1)
			logoTextureStream = stream;
	}

	public void RequestFailed(Object obj, int i) {
	}

	public void AddStuff(String _equip) {
		if (model == null)
			System.out.println("Adding items while model is null");
		if (_equip != null) {
			String equip[] = _equip.split(",");
			if (equip != null && equip.length % 2 == 0) {
				boolean mainhandEquipped = false;
				for (int i = 0; i < equip.length; i += 2) {
					int slot = Integer.parseInt(equip[i]);
					if (slot == 13 || slot == 17 || slot == 21)
						if (mainhandEquipped)
							equip[i] = "23";
						else
							mainhandEquipped = true;
					AddArmor(equip[i], equip[i + 1]);
				}

			}
		}
	}

	public void AddArmor(String slotStr, String idStr) {
		if (model != null) {
			int slot = Integer.parseInt(slotStr);
			FileLoader fl = null;
			switch (slot) {
			case 1: // '\001'
			case 3: // '\003'
				model.AttachModel("armor/" + slot + "/" + idStr, slot);
				break;

			case 4: // '\004'
			case 5: // '\005'
			case 6: // '\006'
			case 7: // '\007'
			case 8: // '\b'
			case 9: // '\t'
			case 10: // '\n'
			case 16: // '\020'
			case 19: // '\023'
			case 20: // '\024'
				fl = new FileLoader(1001, model, ("models/armor/" + slot + "/"
						+ idStr + ".sis").toLowerCase());
				fl.start();
				break;

			case 13: // '\r'
			case 14: // '\016'
			case 15: // '\017'
			case 17: // '\021'
			case 21: // '\025'
			case 22: // '\026'
			case 23: // '\027'
			case 25: // '\031'
				model.AttachModel("item/" + idStr, slot);
				break;

			case 2: // '\002'
			case 11: // '\013'
			case 12: // '\f'
			case 18: // '\022'
			case 24: // '\030'
			default:
				System.out.println("Unhandled slot: " + slot);
				break;
			}
		}
	}

	public void ClearSlot(String slotStr) {
		if (model != null)
			model.ClearSlot(Integer.parseInt(slotStr));
	}

	public void ClearSlots(String slotStr) {
		if (model != null) {
			String slots[] = slotStr.split(",");
			if (slots == null)
				return;
			for (int i = 0; i < slots.length; i++)
				model.ClearSlot(Integer.parseInt(slots[i]));

		}
	}

	public void ClearAllSlots() {
		if (model != null)
			model.ClearAllSlots();
	}

	public void setModel(String type, String mod) {
		newModel = mod;
		modelType = Integer.parseInt(type);
		setModel();
	}

	public void animStop() {
		if (model != null)
			model.SetAnimation("idle");
	}

	public void animCheer() {
		if (model != null)
			model.SetAnimation("Cheer");
	}

	public void animDance() {
		if (model != null)
			model.SetAnimation("Dance1");
	}

	public void animCower() {
		if (model != null)
			model.SetAnimation("Cower");
	}

	public void animWalk() {
		if (model != null)
			model.SetAnimation("Walk");
	}

	public void setHairStyle(int hs) {
		if (hs == hairType)
			return;
		hairType = hs;
		if (model != null)
			model.RefreshAppearance();
	}

	public void setHairColor(int hc) {
		if (hc == hairColor)
			return;
		hairColor = hc;
		if (model != null)
			model.RefreshAppearance();
	}

	public void setFaceType(int fa) {
		if (fa == faceType)
			return;
		faceType = fa;
		if (model != null)
			model.RefreshAppearance();
	}

	public void setSkinColor(int sk) {
		if (sk == skinColor)
			return;
		skinColor = sk;
		if (model != null)
			model.RefreshAppearance();
	}

	public void setFacialHairStyle(int fh) {
		if (fh == facialHairType)
			return;
		facialHairType = fh;
		if (model != null)
			model.RefreshAppearance();
	}

	public void setFacialHairColor(int fc) {
		if (fc == facialHairColor)
			return;
		facialHairColor = fc;
		if (model != null)
			model.RefreshAppearance();
	}

	public void setAppearance(int hs, int hc, int fa, int sk, int fh, int fc) {
		hairType = hs;
		hairColor = hc;
		faceType = fa;
		skinColor = sk;
		facialHairType = fh;
		facialHairColor = fc;
		if (model != null)
			model.RefreshAppearance();
	}

	public int getHairStyles() {
		if (model != null && model.mHairStyles != null)
			return model.mHairStyles.length;
		else
			return 0;
	}

	public int getHairColors() {
		if (model != null)
			return model.mNumHairColors;
		else
			return 0;
	}

	public int getFaceTypes() {
		if (model != null)
			return model.mNumFaceTypes;
		else
			return 0;
	}

	public int getSkinColors() {
		if (model != null && model.mSkinColors != null)
			return model.mSkinColors.length;
		else
			return 0;
	}

	public int getFacialHairStyles() {
		if (model != null && model.mFacialHairs != null)
			return model.mFacialHairs.length;
		else
			return 0;
	}

	public int getFacialHairColors() {
		if (model != null)
			return model.mNumFacialHairColors;
		else
			return 0;
	}

	public boolean isLoaded() {
		if (model != null)
			return model.mLoaded;
		else
			return false;
	}

	public int numMeshes() {
		if (model != null)
			return model.mMeshes.length;
		else
			return 0;
	}

	public void toggleMesh(int mesh) {
		if (model != null) {
			for (int i = 0; i < model.mMeshes.length; i++)
				if (model.mMeshes[i].geoset == mesh)
					model.mMeshes[i].show = !model.mMeshes[i].show;

		}
	}

	public void toggleFreeze() {
		if (model != null)
			model.mFreeze = !model.mFreeze;
	}

	public void dumpTranslation() {
		if (model != null)
			model.dumpTranslation();
	}

	public void dumpRotation() {
		if (model != null)
			model.dumpRotation();
	}

	public void dumpScale() {
		if (model != null)
			model.dumpScale();
	}

	void setModel() {
		try {
			String modelPath = "models/";
			switch (modelType) {
			case 1: // '\001'
				modelPath = modelPath + "item/" + newModel + ".mum";
				break;

			case 2: // '\002'
				modelPath = modelPath + "armor/1/" + newModel + "_1_0.mum";
				break;

			case 4: // '\004'
				modelPath = modelPath + "armor/3/" + newModel + "_1.mum";
				break;

			case 8: // '\b'
				modelPath = modelPath + "npc/" + newModel + ".mum";
				break;

			case 16: // '\020'
				modelPath = modelPath + "char/" + newModel + ".mom";
				break;

			case 32: // ' '
				modelPath = modelPath + "npc/" + newModel + ".sis";
				break;

			case 64: // '@'
				modelPath = modelPath + "obj/" + newModel + ".mum";
				break;

			default:
				System.out.println("Unhandled model type: " + modelType);
				break;
			}
			FileLoader loader = null;
			if (modelType == 32)
				loader = new FileLoader(1002, model, modelPath.toLowerCase());
			else if (modelType == 16)
				loader = new FileLoader(1000, model, modelPath.toLowerCase());
			else
				loader = new FileLoader(1004, model, modelPath.toLowerCase());
			modelFile = newModel;
			boolean loaded = model.mLoaded;

			loader.start();
			if (!loaded)
				AddStuff(equipList);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		newModel = null;
	}

	public static InputStream downloadWithProgress(URL url) throws IOException {
		URLConnection uc = url.openConnection();
		uc.connect();
		InputStream is = uc.getInputStream();
		int contentLength = uc.getContentLength();
		int curMax = 16384;
		byte mainBuf[] = new byte[curMax];
		int amountRead = 0;
		int count = is.read(mainBuf, 0, curMax);
		FileDownloadEntry entry = new FileDownloadEntry();
		if (contentLength != -1)
			entry.length = contentLength;
		else
			entry.length = curMax * 10;
		downloads.add(entry);
		for (; count != -1; count = is.read(mainBuf, amountRead, curMax
				- amountRead)) {
			if (amountRead + count == curMax) {
				curMax *= 2;
				byte tmp[] = new byte[curMax];
				System.arraycopy(mainBuf, 0, tmp, 0, amountRead + count);
				mainBuf = tmp;
			}
			amountRead += count;
			entry.progress = amountRead;
		}

		downloads.remove(entry);
		InputStream returnStream = new ByteArrayInputStream(mainBuf, 0,
				amountRead);
		return returnStream;
	}

	public static ByteBuffer bufferData(InputStream is) throws IOException {
		int curMax = 16384;
		byte mainBuf[] = new byte[curMax];
		int offset = 0;
		for (int count = is.read(mainBuf, 0, curMax); count != -1; count = is
				.read(mainBuf, offset, curMax - offset)) {
			if (offset + count == curMax) {
				curMax *= 3;
				byte tmp[] = new byte[curMax];
				System.arraycopy(mainBuf, 0, tmp, 0, offset + count);
				mainBuf = tmp;
			}
			offset += count;
		}

		return ByteBuffer.wrap(mainBuf, 0, offset).order(
				ByteOrder.LITTLE_ENDIAN);
	}

	public void init(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		gl.setSwapInterval(1);
		gl.glTexEnvi(8960, 8704, 34160);
		gl.glShadeModel(7425);
		gl.glHint(3154, 4354);
		gl.glEnable(3553);
		gl.glBlendFunc(770, 771);
		gl.glEnable(3008);
		gl.glAlphaFunc(518, 0.7F);
		gl.glDepthFunc(515);
		gl.glEnable(2929);
		gl.glLightModeli(2898, 1);
		float ambient[] = { 0.35F, 0.35F, 0.35F, 1.0F };
		float diffuse[] = { 1.0F, 1.0F, 1.0F, 1.0F };
		float diffuse1[] = { 0.65F, 0.65F, 0.65F, 1.0F };
		float specular[] = { 0.9F, 0.9F, 0.9F, 1.0F };
		gl.glLightfv(16384, 4608, ambient, 0);
		gl.glLightfv(16384, 4609, diffuse, 0);
		gl.glLightfv(16384, 4610, specular, 0);
		gl.glLightfv(16385, 4608, ambient, 0);
		gl.glLightfv(16385, 4609, diffuse1, 0);
		gl.glLightfv(16385, 4610, specular, 0);
		gl.glLightfv(16386, 4608, ambient, 0);
		gl.glLightfv(16386, 4609, diffuse1, 0);
		gl.glLightfv(16386, 4610, specular, 0);
		float pos1[] = { 5F, -5F, 5F, 1.0F };
		float pos2[] = { 5F, 5F, 5F, 0.0F };
		float pos3[] = { -5F, 5F, 5F, 1.0F };
		gl.glLightfv(16384, 4611, pos1, 0);
		gl.glLightfv(16385, 4611, pos2, 0);
		gl.glLightfv(16386, 4611, pos3, 0);
		gl.glEnable(16384);
		gl.glEnable(16385);
		gl.glEnable(16386);
		if (gl.isExtensionAvailable("GL_ARB_multisample"))
			gl.glEnable(32925);
		try {
			color = Color.decode(bgColor);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			color = Color.black;
		}
		gl.glClearColor((float) color.getRed() / 255F,
				(float) color.getGreen() / 255F,
				(float) color.getBlue() / 255F, 1.0F);
		drawable.addMouseListener(this);
		drawable.addMouseMotionListener(this);
		drawable.addMouseWheelListener(this);
		FileLoader fl = new FileLoader(1, this, this, "http://static.3darmory.com/images/3darmory.png",true);
		fl.start();
		cam = new Camera();
		model = new Model();
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GL gl = drawable.getGL();
		GLU glu = new GLU();
		canvasWidth = width;
		canvasHeight = height;
		gl.glMatrixMode(5889);
		gl.glLoadIdentity();
		glu.gluPerspective(45D, (float) width / (float) height,
				0.10000000149011612D, 150D);
		gl.glMatrixMode(5888);
		gl.glLoadIdentity();
		gl.glTranslatef(0.0F, 0.0F, -3F);
	}

	public void display(GLAutoDrawable drawable) {
		
		GL gl = drawable.getGL();
		
		/*
		// if we're going to take a screenshot, change background color to white, as zazzy uses white for transparency
		if(take_screenshot)
		{
			this.color=color.white;
			gl.glClearColor((float) this.color.getRed() / 255F,(float) this.color.getGreen() / 255F,(float) this.color.getBlue() / 255F, 1.0F);
		}
		*/
		
		if ((drawable instanceof GLJPanel)
				&& !((GLJPanel) drawable).isOpaque()
				&& ((GLJPanel) drawable)
						.shouldPreserveColorBufferIfTranslucent())
			gl.glClear(256);
		else
			gl.glClear(16640);

		if (lastTime == 0L)
			lastTime = System.currentTimeMillis();
		delta = System.currentTimeMillis() - lastTime;
		globalTime = (int) ((long) globalTime + delta);
		lastTime = System.currentTimeMillis();

		if (newModel != null)
			setModel();
		if (model != null && model.mLoaded && !model.boundsSet)
			model.calcBounds();
		double thetaX = 12.566370614359172D * (xDelta / (double) canvasWidth);
		double thetaY = 12.566370614359172D * (yDelta / (double) canvasHeight);
		if (mouseRButtonDown) {
			cam.translate(xDelta, -yDelta, 0.0D);
			xDelta = yDelta = 0.0D;
		} else if (mouseFromLButton)
			if (Math.abs(xDelta) > 0.10000000000000001D
					|| Math.abs(yDelta) > 0.0D) {
				cam.rotate(-thetaX, -thetaY);
				xDelta /= 1.1499999999999999D;
				yDelta = 0.0D;
			} else {
				if (Math.abs(xDelta) < 0.10000000000000001D) {
					xDelta = 0.0D;
					mouseFromLButton = mouseLButtonDown;
				}
				yDelta = 0.0D;
			}
		if (mouseLButtonDown)
			xDelta = yDelta = 0.0D;
		cam.look(gl);
		try {
			if (logoTextureStream != null) {
				logoTexture = TextureIO.newTexture(logoTextureStream, true,
						"png");
				logoTextureStream = null;
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		gl.glEnable(2896);
		gl.glEnable(3042);
		gl.glBlendFunc(770, 771);
		if (model != null)
			model.render(gl, cam, true);
		gl.glDisable(2896);
		gl.glBlendFunc(770, 771);
		gl.glMatrixMode(5889);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glOrtho(0.0D, canvasWidth, canvasHeight, 0.0D, -1D, 1.0D);
		gl.glMatrixMode(5888);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glEnable(3553);
		gl.glDisable(3008);
		gl.glEnable(3042);
		
		boolean got_progress=false;
		if (downloads.size() > 0) {
			int totalSize = 0;
			int totalProgress = 0;
			for (int i = 0; i < downloads.size(); i++) {
					try 
					{
						FileDownloadEntry entry = (FileDownloadEntry) downloads.get(i);
						if(entry!=null)
						{
							totalProgress += entry.progress;
							totalSize += entry.length;
							got_progress=true;
						}
					}
					catch(Exception e)
					{
						got_progress=false;
					}
			}

			int colAvg = (this.color.getRed() + this.color.getGreen() + this.color
					.getBlue()) / 3;
			float color = 0.0F;
			if (colAvg < 96)
				color = 1.0F;
			
			if(got_progress)
			{
				mTextRender.beginRendering(drawable.getWidth(), drawable.getHeight());
				mTextRender.setColor(color, color, color, 1.0F);
				mTextRender.draw("Loading your toon.. "+ Math.floor(((double) totalProgress / (double) totalSize) * 100D)+ "%", 10, 10);
				mTextRender.endRendering();
			}
		}
		if (logoTexture != null && (!take_screenshot))  // hide logo if we take a screenshot
		{
			logoTexture.enable();
			logoTexture.bind();
			gl.glColor4f(1.0F, 1.0F, 1.0F, 0.7F);
			gl.glBegin(7);
			gl.glTexCoord2i(0, 0);
			//gl.glVertex2i(canvasWidth - logoTexture.getWidth(), canvasHeight - logoTexture.getHeight());
			gl.glVertex2i(0, 0);
			gl.glTexCoord2i(0, 1);
			//gl.glVertex2i(canvasWidth - logoTexture.getWidth(), canvasHeight);
			gl.glVertex2i(0, 0 );
			gl.glTexCoord2i(1, 1);
			//gl.glVertex2i(canvasWidth, canvasHeight);
			gl.glVertex2i(0, 0 );
			gl.glTexCoord2i(1, 0);
			//gl.glVertex2i(canvasWidth, canvasHeight - logoTexture.getHeight());
			gl.glVertex2i(0, 0);
			gl.glEnd();
			gl.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			logoTexture.disable();
		}

		gl.glPopMatrix();
		gl.glMatrixMode(5889);
		gl.glPopMatrix();
		gl.glMatrixMode(5888);
		
		/* 
		if(model.mLoaded && (!options_rendered))
			load_complete();
		*/
		
		// check if we need to a screenshot
		if(take_screenshot)
		{
			// take the screenshot
			//http://processing.org/discourse/yabb_beta/YaBB.cgi?board=Syntax;action=display;num=1212658813;start=15#15
			
			screenshot_gl=gl;
			btn_screenshot.setEnabled(false);
			
	    	img_screenshot=Screenshot.readToBufferedImage(screenshot_gl, canvasWidth, canvasHeight); // get screenshot
	    	DataUpload upload=new DataUpload(); // upload class
	    	String img_file="3DArmory" + this.toon_id + ".png";
	    	//img_file = img_file.replaceAll(" ", "%20");
	    	boolean ret=upload.UploadImage(img_file, img_screenshot);
	    	if(ret)
	    	{
	    		//int response_code=upload.GetResponseCode();
	    		CommitScreenshot.commit(upload.GetServerFeedback(),this.toon_id,this.applet_context);
	    	}
	    	else
	    	System.out.println("DataUpload, upload error");
	    			
	    	btn_screenshot.setEnabled(true);
	            
			/*
			// chage color back to original
			try {
				this.color = Color.decode(this.bgColor);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				this.color = Color.black;
			}
			gl.glClearColor((float) this.color.getRed() / 255F,(float) this.color.getGreen() / 255F,(float) this.color.getBlue() / 255F, 1.0F);
			*/
	    	
			take_screenshot=false;
		}
			
		if ((float) delta < 0.016667F)
			try {
				Thread.sleep((long) ((0.016667F - (float) delta) * 1000F));
			} catch (InterruptedException interruptedexception) {
			}
	}
	
	


	
	public void displayChanged(GLAutoDrawable glautodrawable, boolean flag,
			boolean flag1) {
	}

	public void mouseEntered(MouseEvent mouseevent) {
	}

	public void mouseExited(MouseEvent mouseevent) {
	}

	public void mousePressed(MouseEvent e) {
		prevMouseX = e.getX();
		prevMouseY = e.getY();
		if ((e.getModifiers() & 0x10) != 0) {
			mouseLButtonDown = true;
			mouseFromLButton = true;
		}
		if ((e.getModifiers() & 4) != 0)
			mouseRButtonDown = true;
	}

	public void mouseReleased(MouseEvent e) {
		if ((e.getModifiers() & 0x10) != 0)
			mouseLButtonDown = false;
		if ((e.getModifiers() & 4) != 0)
			mouseRButtonDown = false;
	}

	public void mouseClicked(MouseEvent mouseevent) {
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		cam.zoom(e.getWheelRotation());
	}
	
	public void mouseDragged(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		xDelta = x - prevMouseX;
		yDelta = y - prevMouseY;
		prevMouseX = x;
		prevMouseY = y;
	}

	public void mouseMoved(MouseEvent mouseevent) {
	}
}


