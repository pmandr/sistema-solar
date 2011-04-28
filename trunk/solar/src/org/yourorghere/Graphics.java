package org.yourorghere;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.GLUT;

public class Graphics implements GLEventListener, KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

    private GLU glu;
    private GLUT glut;
    private int width, height;
    private ArrayList<Planeta> characters;
    // Keyboard input
    private boolean[] keys;
    // Camera variables
    private Camera camera;
    private float zoom = 10.0f;
    private float cameraXPosition = 0.0f;
    private float cameraYPosition = 0.0f;
    private float cameraZPosition = 10.0f;
    private float cameraLXPosition = cameraXPosition;
    private float cameraLYPosition = cameraYPosition;
    private float cameraLZPosition = cameraZPosition - zoom;
    private float lastMouseX = 0;
    private float lastMouseY = 0;
    private float actualMouseX = 0;
    private float actualMouseY = 0;
    private Planeta planeta1;
    private Planeta planeta2;

    public Graphics() {
        // boolean array for keyboard input
        keys = new boolean[256];

        // Initialize the user camera
        camera = new Camera();
        camera.yawLeft(2.5);
        camera.pitchDown(0.3);
        camera.moveForward(-10);
        camera.look(10);

        characters = new ArrayList<Planeta>();


    }

    public void init(GLAutoDrawable drawable) {
        width = drawable.getWidth();
        height = drawable.getHeight();

        GL gl = drawable.getGL();
        glu = new GLU();
        glut = new GLUT();

        gl.setSwapInterval(0); 												// Refreshes screen at 60fps   

        float light_ambient[] = {0.2f, 0.2f, 0.2f, 1.0f};
        float light_diffuse[] = {0.8f, 0.8f, 0.8f, 1.0f};
        float light_specular[] = {0.5f, 0.5f, 0.5f, 1.0f};

        /* light_position is NOT default value */
        float light_position[] = {1.0f, 1.0f, 1.0f, 0.0f};


        gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, light_ambient, 0);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, light_diffuse, 0);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, light_specular, 0);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, light_position, 0);


        gl.glEnable(GL.GL_LIGHTING);
        gl.glEnable(GL.GL_LIGHT0);

        gl.glShadeModel(GL.GL_SMOOTH);
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glClearDepth(1.0f);												// Depth Buffer Setup
        gl.glEnable(GL.GL_DEPTH_TEST);										// Enables Depth Testing
        gl.glDepthFunc(GL.GL_LEQUAL);										// The Type Of Depth Test To Do
        gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);			// Really Nice Perspective Calculations
        planeta1 = new Planeta(0.0, 0.0, 0.0);
        planeta2 = new Planeta(4.0, 4.0, -8.0);
        characters.add(planeta1);
        characters.add(planeta2);
        glu.gluPerspective(70.0, (float) width / (float) height, 1, 50);
        glu.gluLookAt(camera.getXPos(), camera.getYPos(), camera.getZPos(), camera.getXLPos(), camera.getYLPos(), camera.getZLPos(), 0.0, 1.0, 0.0);
    }

    public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        mouseMove();
        width = drawable.getWidth();
        height = drawable.getHeight();

        keyboardChecks();													// Responds to keyboard input

        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);			// Clear the colour and depth buffer

        gl.glViewport(0, 0, width, height);									// Reset The Current Viewport

        gl.glMatrixMode(GL.GL_PROJECTION);									// Select The Projection Matrix
        gl.glLoadIdentity();												// Reset The Projection Matrix

        glu.gluPerspective(70.0, (float) width / (float) height, 1, 50);
        glu.gluLookAt(camera.getXPos(), camera.getYPos(), camera.getZPos(),
                camera.getXLPos(), camera.getYLPos(), camera.getZLPos(), 0.0, 1.0, 0.0);

        gl.glMatrixMode(GL.GL_MODELVIEW);									// Select The Modelview Matrix
        gl.glLoadIdentity();												// Reset The Modelview Matrix     

        drawScene(drawable);												// Draw the scene
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int w2, int h2) {
        GL gl = drawable.getGL();

        w2 = drawable.getWidth();
        h2 = drawable.getHeight();

        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();

        // perspective view
        gl.glViewport(10, 10, width - 20, height - 20);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        //glu.gluPerspective(45.0f,(float)width/(float)height,0.1f,100.0f);
        glu.gluPerspective(70.0, (float) width / (float) height, 1, 50);
        glu.gluLookAt(camera.getXPos(), camera.getYPos(), camera.getZPos(), camera.getXLPos(), camera.getYLPos(), camera.getZLPos(), 0.0, 1.0, 0.0);
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    public void drawScene(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();

        for (Planeta c : characters) {
            c.draw(drawable, glut);
        }
    }

    @Override
    public void keyPressed(KeyEvent key) {
        try {
            char i = key.getKeyChar();
            keys[(int) i] = true;
        } catch (Exception e) {
        }
        ;


    }

    @Override
    public void keyReleased(KeyEvent key) {
        try {
            char i = key.getKeyChar();
            keys[(int) i] = false;
        } catch (Exception e) {
        }
        ;
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseDragged(MouseEvent arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseMoved(MouseEvent arg0) {
        actualMouseX = arg0.getX();
        actualMouseY = arg0.getY();

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent arg0) {
        // TODO Auto-generated method stub
    }

    public void keyboardChecks() {
        if (keys['w']) {
            cameraZPosition -= 0.1;
            cameraLZPosition -= 0.1;
            planeta1.moveForward(0.1);
            planeta2.moveForward(0.1);
            camera.moveForward(0.1);
            camera.look(10);

        }

        if (keys['s']) {
            cameraZPosition += 0.1;
            cameraLZPosition += 0.1;
            planeta1.moveForward(-0.1);
            planeta2.moveForward(-0.1);
            camera.moveForward(-0.1);
            camera.look(10);
        }

        if (keys['j']) {
            camera.pitchUp(0.05);
            camera.look(10);
        }

        if (keys['k']) {
            camera.pitchDown(0.05);
            camera.look(10);
        }

        if (keys['q']) {
            camera.yawLeft(0.01);
            camera.look(10);
        }

        if (keys['e']) {
            camera.yawRight(0.01);
            camera.look(10);
        }

        if (keys['a']) {
            camera.strafeLeft(0.1);
            camera.look(10);
        }

        if (keys['d']) {
            camera.strafeRight(0.1);
            camera.look(10);
        }
    }

    public void mouseMove() {
        float difY = Math.abs(actualMouseY - this.lastMouseY);
        float difX = Math.abs(actualMouseX - this.lastMouseX);

        System.out.println("dif = " + difY);

        if (difY > 2) {
            if (difY > 30) difY = 30;
            if (actualMouseY < this.lastMouseY) {
                camera.pitchUp(difY*0.005);
                camera.look(10);
            } else {
                camera.pitchDown(difY*0.005);
                camera.look(10);
            }
        }

        if (difX > 2) {
            if (difX > 30) difX = 30;
            if (actualMouseX < this.lastMouseX) {
                camera.yawLeft(difX*0.001);
                camera.look(10);
            } else {
                camera.yawRight(difX*0.001);
                camera.look(10);
            }
        }

        this.lastMouseX = actualMouseX;
        this.lastMouseY = actualMouseY;
    }
}
