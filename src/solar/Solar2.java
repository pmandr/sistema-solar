package solar;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.GLUT;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureCoords;
import com.sun.opengl.util.texture.TextureData;
import com.sun.opengl.util.texture.TextureIO;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;


public class Solar2 implements GLEventListener, KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

    static float pitch = 0, rot = 45.0f;
    static float xdist = 0, ydist = 0.0f, zdist = 20.0f;
    static float xlook = 0.0f, ylook = 0.0f, zlook = 30.0f;
    static float xup = 0.0f, yup = 0.0f, zup = 0.0f;
    static int lastx = 0, lasty = 0;
    protected ArrayList<Planeta> planets;
    private Estrela sun;
    static boolean show_orbit = false;
    protected GLU glu;
    protected GLUT glut;

    // Lighting values
    FloatBuffer ambientLight0 = FloatBuffer.wrap( new float[]{0.5f, 0.5f, 0.5f, 1.0f});
    FloatBuffer ambientLight1 = FloatBuffer.wrap( new float[]{0.1f, 0.1f, 0.1f, 1.0f});
    FloatBuffer diffuseLight = FloatBuffer.wrap( new float[]{0.7f, 0.7f, 0.7f, 1.0f});
    FloatBuffer specular = FloatBuffer.wrap( new float[]{1.0f, 1.0f, 1.0f, 1.0f});
    FloatBuffer specref = FloatBuffer.wrap( new float[]{1.0f, 1.0f, 1.0f, 1.0f});
    FloatBuffer lightPos = FloatBuffer.wrap( new float[]{ 0.0f, 0.0f, 0.0f, 1.0f } );
    FloatBuffer emissionSun = FloatBuffer.wrap( new float[]{15.9f, 0.0f, 0.0f, 0.0f});
    FloatBuffer nullv = FloatBuffer.wrap( new float[]{0.0f, 0.0f, 0.0f, 1.0f});

    private Texture sunTexture;
    private String filename;
    private Texture bgTexture;
    private TextureCoords bgTexCoords;


void setupLighting(GL gl)
{
	gl.glEnable(GL.GL_LIGHTING);

	gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, ambientLight0);
	gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, diffuseLight);
	gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, specular);

	gl.glLightfv(GL.GL_LIGHT1, GL.GL_AMBIENT, ambientLight1);
	gl.glLightfv(GL.GL_LIGHT1, GL.GL_DIFFUSE, diffuseLight);
	gl.glLightfv(GL.GL_LIGHT1, GL.GL_SPECULAR, specular);

	gl.glEnable(GL.GL_COLOR_MATERIAL);

	gl.glColorMaterial(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE);

}

    public static void main(String[] args) {
        Frame frame = new Frame("Sistema Solas");
        GLCanvas canvas = new GLCanvas();
        Solar2 solar2 = new Solar2();
        canvas.addGLEventListener(new Solar2());
        frame.add(canvas);
        frame.setSize(800, 600);
        final Animator animator = new Animator(canvas);
        canvas.addKeyListener(solar2);
        canvas.addMouseMotionListener(solar2);

        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                new Thread(new Runnable() {

                    public void run() {
                        animator.stop();
                        System.exit(0);
                    }
                }).start();
            }
        });
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        animator.start();
    }

    public void init(GLAutoDrawable drawable) {

        glu = new GLU();
        glut = new GLUT();
        GL gl = drawable.getGL();
        planets = Planeta.create_planets();
        System.err.println("INIT GL IS: " + gl.getClass().getName());
        load_textures();
        setupLighting(gl);
        gl.setSwapInterval(1);
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glShadeModel(GL.GL_SMOOTH);
    }

    public void initbg() {
        try {
            InputStream stream = getClass().getResourceAsStream("/texturas/mw.jpg");
            TextureData data = TextureIO.newTextureData(stream, false, "jpg");
            bgTexture = TextureIO.newTexture(data);
        } catch (IOException exc) {
            System.out.println("Falha ao carregar a textura de mw.jpg");
            System.exit(1);
        }
         bgTexCoords = bgTexture.getImageTexCoords();
    }

    public void drawbg(GL gl, GLU glu) {

    gl.glPushMatrix();

    gl.glLoadIdentity();

        glu.gluLookAt(0, 0, 0, xlook/100, ylook/100, zlook/100, xup, yup, zup);

    gl.glPushAttrib(GL.GL_ENABLE_BIT);
    gl.glEnable(GL.GL_TEXTURE_2D);
    gl.glDisable(GL.GL_DEPTH_TEST);
    gl.glDisable(GL.GL_LIGHTING);
    gl.glDisable(GL.GL_BLEND);

    gl.glColor4f(1,1,1,1);

        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
     bgTexture.enable();

      bgTexture.bind();
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
      gl.glBegin(GL.GL_QUADS);

      gl.glTexCoord2f(bgTexCoords.left(), bgTexCoords.bottom());
      gl.glVertex3f(-2.0f, -2.0f, 2.0f);
      gl.glTexCoord2f(10, bgTexCoords.bottom());
      gl.glVertex3f(2.0f, -2.0f, 2.0f);
      gl.glTexCoord2f(10, 10);
      gl.glVertex3f(2.0f, 2.0f, 2.0f);
      gl.glTexCoord2f(bgTexCoords.left(), 10);
      gl.glVertex3f(-2.0f, 2.0f, 2.0f);
      gl.glEnd();
      gl.glBegin(GL.GL_QUADS);

      gl.glTexCoord2f(10, bgTexCoords.bottom());
      gl.glVertex3f(-2.0f, -2.0f, -2.0f);
      gl.glTexCoord2f(10, 10);
      gl.glVertex3f(-2.0f, 2.0f, -2.0f);
      gl.glTexCoord2f(bgTexCoords.left(), 10);
      gl.glVertex3f(2.0f, 2.0f, -2.0f);
      gl.glTexCoord2f(bgTexCoords.left(), bgTexCoords.bottom());
      gl.glVertex3f(2.0f, -2.0f, -2.0f);
      gl.glEnd();
      gl.glBegin(GL.GL_QUADS);

      gl.glTexCoord2f(bgTexCoords.left(), 10);
      gl.glVertex3f(-2.0f, 2.0f, -2.0f);
      gl.glTexCoord2f(bgTexCoords.left(), bgTexCoords.bottom());
      gl.glVertex3f(-2.0f, 2.0f, 2.0f);
      gl.glTexCoord2f(10, bgTexCoords.bottom());
      gl.glVertex3f(2.0f, 2.0f, 2.0f);
      gl.glTexCoord2f(10, 10);
      gl.glVertex3f(2.0f, 2.0f, -2.0f);
      gl.glEnd();
      gl.glBegin(GL.GL_QUADS);

      gl.glTexCoord2f(10, 10);
      gl.glVertex3f(-2.0f, -2.0f, -2.0f);
      gl.glTexCoord2f(bgTexCoords.left(), 10);
      gl.glVertex3f(2.0f, -2.0f, -2.0f);
      gl.glTexCoord2f(bgTexCoords.left(), bgTexCoords.bottom());
      gl.glVertex3f(2.0f, -2.0f, 2.0f);
      gl.glTexCoord2f(10, bgTexCoords.bottom());
      gl.glVertex3f(-2.0f, -2.0f, 2.0f);
      gl.glEnd();
      gl.glBegin(GL.GL_QUADS);

      gl.glTexCoord2f(10, bgTexCoords.bottom());
      gl.glVertex3f(2.0f, -2.0f, -2.0f);
      gl.glTexCoord2f(10, 10);
      gl.glVertex3f(2.0f, 2.0f, -2.0f);
      gl.glTexCoord2f(bgTexCoords.left(), 10);
      gl.glVertex3f(2.0f, 2.0f, 2.0f);
      gl.glTexCoord2f(bgTexCoords.left(), bgTexCoords.bottom());
      gl.glVertex3f(2.0f, -2.0f, 2.0f);
      gl.glEnd();
      gl.glBegin(GL.GL_QUADS);

      gl.glTexCoord2f(bgTexCoords.left(), bgTexCoords.bottom());
      gl.glVertex3f(-2.0f, -2.0f, -2.0f);
      gl.glTexCoord2f(10, bgTexCoords.bottom());
      gl.glVertex3f(-2.0f, -2.0f, 2.0f);
      gl.glTexCoord2f(10, 10);
      gl.glVertex3f(-2.0f, 2.0f, 2.0f);
      gl.glTexCoord2f(bgTexCoords.left(), 10);
      gl.glVertex3f(-2.0f, 2.0f, -2.0f);

      gl.glEnd();
    gl.glPopAttrib();
    gl.glDisable(GL.GL_TEXTURE_2D);
    gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glEnable(GL.GL_LIGHTING);
    gl.glEnable(GL.GL_BLEND);
    gl.glPopMatrix();
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL gl = drawable.getGL();

        if (height <= 0) { // avoid a divide by zero error!

            height = 1;
        }
        final float h = (float) width / (float) height;
        gl.glViewport(0, 0, width, height);
        gl.glEnable(GL.GL_DEPTH_TEST) ;
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(40.0f, h, 1.0, 600.0);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void display(GLAutoDrawable drawable) {
        //System.out.println("xdist: " + xdist + " ydist: " + ydist + " zdist: " + zdist);
        //System.out.println("xup: " + xup + " yup: " + yup + " zup: " + zup);
        //System.out.println("xlook: " + xlook + " ylook: " + ylook + " zlook: " + zlook);
        //System.out.println("pitch: " + pitch + " rot: " + rot);


        GL gl = drawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        drawbg(gl, glu);
        Time.rotate();
        //GetKey();



        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();

        gl.glEnable(GL.GL_LIGHT0);

        glu.gluLookAt(xdist, ydist, zdist, xlook, ylook, zlook, xup, yup, zup);

        gl.glPushMatrix(); 
        gl.glTranslatef(0.0f, 0.0f, -0.0f); 

        gl.glColor4f(1.0f, 0.8f, 0.0f, 1.0f);
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, specref);
        gl.glMateriali(GL.GL_FRONT, GL.GL_SHININESS, 3);
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_EMISSION, emissionSun); 

        gl.glLightfv(GL.GL_LIGHT1, GL.GL_POSITION, lightPos); 
        sunTexture.bind();

        GLUquadric sunny = glu.gluNewQuadric();
        glu.gluQuadricTexture(sunny, true);
        glu.gluQuadricDrawStyle(sunny, GLU.GLU_FILL);
        glu.gluQuadricNormals(sunny, GLU.GLU_FLAT);
        glu.gluQuadricOrientation(sunny, GLU.GLU_OUTSIDE);


        sunTexture.enable();

        glu.gluSphere(sunny, 9.0, 64, 64);
        glu.gluDeleteQuadric(sunny);

        gl.glPushAttrib(GL.GL_LIGHTING_BIT);
        gl.glDisable(GL.GL_LIGHT0);
        gl.glEnable(GL.GL_LIGHT1);

        gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, nullv);
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_EMISSION, nullv);

        //glut.glutSolidSphere(0.05, 5, 4);


    Iterator<Planeta> itr = planets.iterator();
    while (itr.hasNext()) {
      Planeta planet = itr.next();
        gl.glPushMatrix();
        planet.draw(gl, glu);
        gl.glPopMatrix();

    }


        gl.glFlush();
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == e.VK_UP){
		xdist += (Math.cos(rot*3.1415926535/180)* Math.cos(pitch*3.1415926535/180))/2;
		zdist += -(Math.sin(rot*3.1415926535/180) * Math.cos(pitch*3.1415926535/180))/2;
		ydist += Math.sin(pitch*3.1415926535/180);
        }
        if (e.getKeyCode() == e.VK_DOWN){
		xdist -= (Math.cos(rot*3.1415926535/180)* Math.cos(pitch*3.1415926535/180))/2;
		zdist -= -(Math.sin(rot*3.1415926535/180) * Math.cos(pitch*3.1415926535/180))/2;
		ydist -= Math.sin(pitch*3.1415926535/180);
        }
        if (e.getKeyCode() == e.VK_LEFT){
		xdist += (Math.cos((rot+90)*3.1415926535/180))/2;
		zdist += -(Math.sin((rot+90)*3.1415926535/180))/2;

        }
        if (e.getKeyCode() == e.VK_RIGHT){
		xdist += (Math.cos((rot-90)*3.1415926535/180))/2;
		zdist += -(Math.sin((rot-90)*3.1415926535/180))/2;

        }
        if (e.getKeyCode() == e.VK_A){
		xdist += (Math.cos(rot*3.1415926535/180)* Math.cos((pitch+90)*3.1415926535/180));
		zdist += -(Math.sin(rot*3.1415926535/180) * Math.cos((pitch+90)*3.1415926535/180));
		ydist += Math.sin((pitch+90)*3.1415926535/180);
        }
        if (e.getKeyCode() == e.VK_D){
		xdist -= (Math.cos(rot*3.1415926535/180)* Math.cos((pitch+90)*3.1415926535/180));
		zdist -= -(Math.sin(rot*3.1415926535/180) * Math.cos((pitch+90)*3.1415926535/180));
		ydist -= Math.sin((pitch+90)*3.1415926535/180);
        }
        if (e.getKeyCode() == e.VK_Q){
		Time.decreaseAcc();
        }
        if (e.getKeyCode() == e.VK_W){
		Time.increaseAcc();
        }

	xlook = (float) (xdist + (Math.cos(rot * 3.1415926535 / 180) * Math.cos(pitch * 3.1415926535 / 180)));
	zlook = (float) (zdist - (Math.sin(rot * 3.1415926535 / 180) * Math.cos(pitch * 3.1415926535 / 180)));
	ylook = (float) (ydist + Math.sin(pitch * 3.1415926535 / 180));

	xup = (float) (Math.cos(rot * 3.1415926535 / 180) * Math.cos((pitch + 90) * 3.1415926535 / 180));
	zup = (float) (-Math.sin(rot * 3.1415926535 / 180) * Math.cos((pitch + 90) * 3.1415926535 / 180));
	yup = (float) Math.sin((pitch+90)*3.1415926535/180);
    }
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == e.VK_O){
		if(show_orbit) show_orbit = false;
                else show_orbit = true;
        }
    }

    public void mouseClicked(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mousePressed(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseReleased(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseEntered(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseExited(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseDragged(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseMoved(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
	if( (lastx - x) >50 || (lastx - x) <-50 ||
		(lasty - y) >50 || (lasty - y) <-50 )
	{
		lastx = x;
		lasty = y;
	}

	rot += (((float)lastx - (float)x)/2);
	lastx = x;

	if(rot>360)
		rot-=360;
	if(rot<0)
		rot+=360;

	pitch += (((float)lasty - (float)y))/2;
	lasty = y;

	if(pitch > 90)
		pitch = 90.0f;
	if(pitch<-90)
		pitch = -90.0f;

	xlook = (float) (xdist + (Math.cos(rot * 3.1415926535 / 180) * Math.cos(pitch * 3.1415926535 / 180)));
	zlook = (float) (zdist - (Math.sin(rot * 3.1415926535 / 180) * Math.cos(pitch * 3.1415926535 / 180)));
	ylook = (float) (ydist + Math.sin(pitch * 3.1415926535 / 180));

	xup = (float) (Math.cos(rot * 3.1415926535 / 180) * Math.cos((pitch + 90) * 3.1415926535 / 180));
	zup = (float) (-Math.sin(rot * 3.1415926535 / 180) * Math.cos((pitch + 90) * 3.1415926535 / 180));
	yup = (float) Math.sin((pitch+90)*3.1415926535/180);
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void load_textures() {
        initbg();
        try {
            InputStream stream = getClass().getResourceAsStream("/texturas/sun.jpg");
            TextureData data = TextureIO.newTextureData(stream, false, "jpg");
            sunTexture = TextureIO.newTexture(data);
        }
        catch (IOException exc) {
            exc.printStackTrace();
            System.exit(1);
    }
    }
}

