import java.awt.*;
import javax.swing.*;
import java.lang.Math.*;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.FPSAnimator;

class CarvansOpenGL extends GLCanvas implements GLEventListener {
	private GLU glu;
	FPSAnimator fPSFPSAnimator;
	
	boolean drawOrbit = true;
	float currentYear = 2016.91f;
	float time = -(-currentYear-561f)*365.26f;
	
	Planet[] planets = new Planet[]{
		new Planet("Sol", 0.0f, 0, 2.7f, new float[]{1.0f, 1.0f, 0.0f}, 50.1f, null),
		new Planet("Mercuruis", 5.0f, 7, 0.12f, new float[]{0.5f, 0.5f, 0.5f}, 87.97f, null),
		new Planet("Venus", 6.7f, 3, 0.29f, new float[]{0.7f, 0.7f, 0.1f}, 224.70f, null),
		new Planet("Tellus", 8.3f, 0, 0.35f, new float[]{0.0f, 0.7f, 0.3f}, 365.26f, null),
		new Planet("Mars", 10.0f, 1, 0.24f, new float[]{0.8f, 0.1f, 0.1f}, 686.98f, null),
		new Planet("Jupiter", 14.4f, 1, 1.1f, new float[]{0.75f, 0.42f, 0.42f}, 4332.82f, null),
		new Planet("Saturn", 18.2f, 2, 0.8f, new float[]{0.65f, 0.45f, 0.45f}, 10755.70f, new PlanetRing[]{
																							new PlanetRing(1.3f, 1, 15, new float[]{0.4f, 0.36f, 0.48f}),
																							new PlanetRing(1.33f, 1, 15, new float[]{0.3f, 0.46f, 0.38f}),
																							new PlanetRing(1.36f, 1, 15, new float[]{0.1f, 0.26f, 0.38f}),
																							new PlanetRing(1.39f, 1, 15, new float[]{0.9f, 0.46f, 0.48f})}),
		new Planet("Uranus", 21.8f, 0, 0.6f, new float[]{0.0f, 0.4f, 0.8f}, 30687.15f, null),
		new Planet("Neptun", 25.4f, 1, 0.6f, new float[]{0.0f, 0.0f, 0.6f}, 60190.03f, null),
		new Planet("Pluto", 29.6f, 17, 0.08f, new float[]{0.4f, 0.4f, 0.4f}, 90560.0f, null)
	};
	
	
	CarvansOpenGL() {
		this.addGLEventListener(this);
	}
	
	public void init(GLAutoDrawable glDrawable) {
		GL2 gl = glDrawable.getGL().getGL2();
		glu = new GLU();
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
		
		// gl.glEnable(GL2.GL_LINE_SMOOTH);
		// gl.glEnable(GL2.GL_POLYGON_SMOOTH);
		// gl.glEnable(GL2.GL_BLEND);
		// gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		// gl.glHint(GL2.GL_LINE_SMOOTH_HINT, GL2.GL_NICEST);
		// gl.glHint(GL2.GL_POLYGON_SMOOTH_HINT, GL2.GL_NICEST);
		
		
		fPSFPSAnimator = new FPSAnimator(glDrawable, 60);
		fPSFPSAnimator.start();
	}
	
	public void reshape(GLAutoDrawable glDrawable, int x, int y, int width, int height) {
		GL2 gl = glDrawable.getGL().getGL2();
		if (height == 0) height = 1;
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(45.0, (float) width/height, 0.1, 100);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		
		gl.glLoadIdentity();
	}
	
	public void display(GLAutoDrawable glDrawable) {
		GL2 gl = glDrawable.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		
		for (int i = 0; i < planets.length;i++) {
			drawPlant(gl, planets[i].getDistanceFromCenter(),
			360.0f*time/planets[i].getOrbitalPeriod(),
			planets[i].getSize(),
			planets[i].getColor(),
			false,
			planets[i].getAngle(),
			planets[i].getPlanetRings());
		}
}
	
	public void dispose(GLAutoDrawable glDrawable) {
		
	}
	
	private void drawPlant(GL2 gl, float distanceFromCenter, float angle, float size, float[] color, boolean drawOrbit2, float inclination, PlanetRing[] planetRings) {
		gl.glLoadIdentity();
		glu.gluLookAt(0, 20, 55, 0, 0, 0, 0, 1, 0);
		
		gl.glRotated(inclination, 0, 0, 1);
		
		if (drawOrbit) {
			gl.glColor3f(0.15f, 0.15f, 0.15f);
			gl.glBegin(GL2.GL_LINE_LOOP);
			for (int a = 0; a < 361;a += 2) {
				gl.glVertex3d(Math.cos(Math.toRadians(a))*distanceFromCenter, 0.0, Math.sin(Math.toRadians(a))*distanceFromCenter);
			}
			gl.glEnd();
		}
		
		GLUT glut = new GLUT();
		gl.glColor3fv(color, 0);
		gl.glRotatef(angle, 0, 1, 0);
		gl.glTranslatef(distanceFromCenter, 0.0f, 0.0f);
		glut.glutSolidSphere(size, 40, 40);
		
		time += 0.1f;
		
		if (planetRings != null) {
			for (int i = 0; i < planetRings.length; i++) {
				gl.glRotatef(planetRings[i].getAngle(), 0, 1, 0);
				gl.glColor3fv(planetRings[i].getColor(), 0);
				gl.glBegin(GL2.GL_LINE_LOOP);
				for (int a = 0; a < 361;a+=2) {
					gl.glVertex3d(Math.cos(Math.toRadians(a))*planetRings[i].getDistanceFromCenter(), 0.0, Math.sin(Math.toRadians(a))*planetRings[i].getDistanceFromCenter());
				}
				gl.glEnd();
			}
		}
	}
}

class Planet {
	private String name;
	private float distanceFromCenter, size, orbitalPeriod;
	private int angle;
	private float[] color;
	private PlanetRing[] planetRings;
	
	public Planet(String name, float distanceFromCenter, int angle, float size, float[] color, float orbitalPeriod, PlanetRing[] planetRings) {
		this.name = name;
		this.distanceFromCenter = distanceFromCenter;
		this.angle = angle;
		this.size = size;
		this.color = color;
		this.orbitalPeriod = orbitalPeriod;
		this.planetRings = planetRings;
	}
	
	public String getName() { return name; }
	
	public float getDistanceFromCenter() { return distanceFromCenter; }
	
	public int getAngle() { return angle; }
	
	public float getSize() { return size; }
	
	public float[] getColor(){ return color; }
	
	public float getOrbitalPeriod() { return orbitalPeriod; }
	
	public PlanetRing[] getPlanetRings() { return planetRings; }
	
}

class PlanetRing {
	private int angle;
	private float[] color;
	private float distanceFromCenter;
	private int size;
	
	public PlanetRing(float distanceFromCenter, int size, int angle, float[] color) {
		this.distanceFromCenter = distanceFromCenter;
		this.size = size;
		this.angle = angle;
		this.color = color;
	}
	
	public float getDistanceFromCenter() { return distanceFromCenter; }
	
	public int getAngle() { return angle; }
	
	public float getSize() { return size; }
	
	public float[] getColor(){ return color; }
	
}

class SOL {
	public static void main(String[] args) {
		String title = "SOL";
		int width = 1800, height = 1000;
		
		GLCanvas carvans = new CarvansOpenGL();
		carvans.setPreferredSize(new Dimension(width, height));
		
		final JFrame frame = new JFrame();
		frame.getContentPane().add(carvans);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setTitle(title);
		frame.pack();
		frame.setVisible(true);
	}
}