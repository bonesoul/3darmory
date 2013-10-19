package toonviewer;

import java.io.IOException;
import java.nio.ByteBuffer;
import javax.media.opengl.GL;
import javax.vecmath.*;

class Bone {

	int parent;
	int index;
	Point3f pivot;
	Point3f transPivot;
	float mat[];
	Matrix4f matrix;
	Matrix4f rotMatrix;
	Quat4f currRot;
	Point3f currTranslation;
	Point3f currScale;
	boolean calc;
	Model model;

	public Bone(Model m, int thisIndex) {
		parent = -1;
		model = m;
		index = thisIndex;
		pivot = null;
		transPivot = null;
		mat = null;
		calc = false;
		matrix = null;
		rotMatrix = null;
	}

	void dumpTranslation() {
		if (currTranslation != null)
			System.out.println("Bone " + index + " translation: "
					+ currTranslation);
	}

	void dumpRotation() {
		if (currRot != null)
			System.out.println("Bone " + index + " rotation: " + currRot);
	}

	void dumpScale() {
		if (currScale != null)
			System.out.println("Bone " + index + " scale: " + currScale);
	}

	void calcMatrix(GL gl, int time) {
		if (calc)
			return;
		calc = true;
		Matrix4f m = new Matrix4f();
		Matrix4f tmpMat = null;
		Quat4f q = null;
		Animation currentAnim = model.mCurrentAnim;
		if (currentAnim.trans.length == 0 || currentAnim.rot.length == 0
				|| currentAnim.scale.length == 0) {
			if (matrix == null) {
				matrix = new Matrix4f();
				rotMatrix = new Matrix4f();
				matrix.setIdentity();
				rotMatrix.setIdentity();
			}
			currTranslation = currScale = null;
			currRot = null;
			return;
		}
		boolean doesSomething = currentAnim.trans[index].used
				|| currentAnim.rot[index].used || currentAnim.scale[index].used;
		if (doesSomething) {
			m.setIdentity();
			m.setTranslation(new Vector3f(pivot));
			if (currentAnim.trans[index].used) {
				Point3f tr = currentAnim.trans[index].getValue(time);
				tmpMat = new Matrix4f();
				tmpMat.setIdentity();
				tmpMat.setTranslation(new Vector3f(tr));
				m.mul(tmpMat);
				currTranslation = tr;
			} else {
				currTranslation = null;
			}
			if (currentAnim.rot[index].used) {
				q = currentAnim.rot[index].getValue(time);
				tmpMat = new Matrix4f();
				tmpMat.setIdentity();
				tmpMat.setRotation(q);
				tmpMat.transpose();
				m.mul(tmpMat);
				currRot = q;
			} else {
				currRot = null;
			}
			if (currentAnim.scale[index].used) {
				Point3f sc = currentAnim.scale[index].getValue(time);
				if (sc.x > 10F)
					sc.x = 1.0F;
				if (sc.y > 10F)
					sc.y = 1.0F;
				if (sc.z > 10F)
					sc.z = 1.0F;
				tmpMat = new Matrix4f();
				tmpMat.setIdentity();
				tmpMat.m00 = sc.x;
				tmpMat.m11 = sc.y;
				tmpMat.m22 = sc.z;
				tmpMat.m33 = 1.0F;
				tmpMat.setScale(sc.x);
				m.mul(tmpMat);
				currScale = sc;
			} else {
				currScale = null;
			}
			Point3f unpivot = new Point3f(pivot);
			unpivot.scale(-1F);
			tmpMat = new Matrix4f();
			tmpMat.setIdentity();
			tmpMat.setTranslation(new Vector3f(unpivot));
			m.mul(tmpMat);
		} else {
			currTranslation = currScale = null;
			currRot = null;
			m.setIdentity();
		}
		if (parent > -1) {
			model.mBones[parent].calcMatrix(gl, time);
			matrix = new Matrix4f(model.mBones[parent].matrix);
			matrix.mul(m);
		} else {
			matrix = m;
		}
		if (currentAnim.rot[index].used) {
			if (parent > -1) {
				rotMatrix = new Matrix4f(model.mBones[parent].rotMatrix);
				Matrix4f mat = new Matrix4f();
				mat.setIdentity();
				mat.setRotation(q);
				mat.transpose();
				rotMatrix.mul(mat);
			} else {
				rotMatrix = new Matrix4f();
				rotMatrix.setIdentity();
				rotMatrix.setRotation(q);
				rotMatrix.transpose();
			}
		} else {
			rotMatrix = new Matrix4f();
			rotMatrix.setIdentity();
		}
		transPivot.set(pivot);
		matrix.transform(transPivot);
	}

	public void read(ByteBuffer buf) throws IOException {
		parent = buf.getInt();
		pivot = new Point3f(buf.getFloat(), buf.getFloat(), buf.getFloat());
		transPivot = new Point3f(buf.getFloat(), buf.getFloat(), buf.getFloat());
		mat = new float[16];
		for (int i = 0; i < 16; i++)
			mat[i] = buf.getFloat();

	}
}
