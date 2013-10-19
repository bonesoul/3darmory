package toonviewer;

import java.io.IOException;
import java.nio.ByteBuffer;

class Animation {

	String name;
	int duration;
	int elapsed;
	int numAnimData;
	AnimatedVec3D trans[];
	AnimatedVec3D scale[];
	AnimatedQuat rot[];
	boolean loop;

	Animation(int num) {
		numAnimData = num;
		name = null;
		duration = elapsed = 0;
		trans = scale = null;
		rot = null;
		loop = true;
	}

	void read(ByteBuffer buf) throws IOException {
		duration = buf.getInt();
		trans = new AnimatedVec3D[numAnimData];
		rot = new AnimatedQuat[numAnimData];
		scale = new AnimatedVec3D[numAnimData];
		if (numAnimData > 0) {
			for (int i = 0; i < numAnimData; i++) {
				trans[i] = new AnimatedVec3D();
				rot[i] = new AnimatedQuat();
				scale[i] = new AnimatedVec3D();
				trans[i].read(buf);
				rot[i].read(buf);
				scale[i].read(buf);
			}

		}
	}
}
