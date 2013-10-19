package toonviewer;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.vecmath.Vector3d;

class Camera {

	private double latitude;
	private double longitude;
	private double distance;
	Vector3d translation;

	public Camera() {
		latitude = 0.0D;
		longitude = 1.5707963267948966D;
		distance = 5D;
		translation = new Vector3d();
	}

	public void look(GL gl) {
		gl.glLoadIdentity();
		Vector3d dir = getPosition();
		GLU glu = new GLU();
		gl.glTranslated(translation.x, translation.y, translation.z);
		glu.gluLookAt(dir.x, dir.y, dir.z, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D);
	}

	public Vector3d getPosition() {
		double cosTheta = Math.cos(latitude);
		double sinTheta = Math.sin(latitude);
		double cosPhi = Math.cos(longitude);
		double sinPhi = Math.sin(longitude);
		Vector3d dir = new Vector3d(distance * sinPhi * cosTheta, distance
				* sinPhi * sinTheta, distance * cosPhi);
		return dir;
	}

	public void translate(double x, double y, double z) {
		double scaleFactor = (distance / 5D) * 0.02D;
		translation.x += x * scaleFactor;
		translation.y += y * scaleFactor;
		translation.z += z * scaleFactor;
	}

	public void rotate(double lat, double lon) {
		latitude += lat;
		longitude += lon;
		for (; latitude < 0.0D; latitude += 6.2831853071795862D)
			;
		for (; latitude > 6.2831853071795862D; latitude -= 6.2831853071795862D)
			;
		if (longitude > 3.1415926535897931D)
			longitude = 3.1415926535897931D;
		else if (longitude <= 0.0D)
			longitude = 0.01D;
	}

	public void zoom(double change) {
		distance += change * (distance / 15D);
		if (distance <= 0.0D)
			distance = 0.050000000000000003D;
		else if (distance > 100D)
			distance = 100D;
	}

	public void setDistance(float dist) {
		distance = dist;
	}
}
