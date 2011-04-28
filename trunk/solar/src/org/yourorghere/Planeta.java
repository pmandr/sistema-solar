package org.yourorghere;

import com.sun.opengl.util.texture.Texture;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

import com.sun.opengl.util.GLUT;
import com.sun.opengl.util.texture.TextureData;
import com.sun.opengl.util.texture.TextureIO;
import java.io.IOException;
import java.io.InputStream;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

public class Planeta {

    private double xPos;
    private double yPos;
    private double zPos;
    private double pitch;
    private double yaw;
    private double theta = 0;
    private Texture texture;

    public Planeta(double xPos, double yPos, double zPos) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.zPos = zPos;

        this.pitch = 0;
        this.yaw = 0;

        try {
            InputStream stream = getClass().getResourceAsStream("textures/earth.jpg");
            TextureData data = TextureIO.newTextureData(stream, false, "jpg");
            texture = TextureIO.newTexture(data);
        } catch (IOException exc) {
            exc.printStackTrace();
            System.exit(2);
        }

    }

    public void moveForward(double magnitude) {
        double xCurrent = this.xPos;
        double yCurrent = this.yPos;
        double zCurrent = this.zPos;

        // Spherical coordinates maths
        double xMovement = magnitude * Math.cos(pitch) * Math.cos(yaw);
        double yMovement = magnitude * Math.sin(pitch);
        double zMovement = magnitude * Math.cos(pitch) * Math.sin(yaw);

        double xNew = xCurrent + xMovement;
        double yNew = yCurrent + yMovement;
        double zNew = zCurrent + zMovement;

        updatePosition(xNew, yNew, zNew);
    }

    public void updatePosition(double xPos, double yPos, double zPos) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.zPos = zPos;
    }

    public void draw(GLAutoDrawable drawable, GLUT glut) {
        GL gl = drawable.getGL();
        gl.glPushMatrix();

        gl.glTranslated(xPos, yPos, zPos);
        solidSphere(2.0f, 128, 128);
        gl.glPopMatrix();
    }

    private void solidSphere(float radius, int stacks, int columns) {
        GLU glu = new GLU();
        texture.enable();
        texture.bind();
        translate(0.5f);
        GLUquadric quadObj = glu.gluNewQuadric();
        glu.gluQuadricDrawStyle(quadObj, GLU.GLU_FILL);
        glu.gluQuadricNormals(quadObj, GLU.GLU_SMOOTH);
        glu.gluQuadricTexture(quadObj, true);
        glu.gluSphere(quadObj, radius, stacks, columns);
    }

    public void translate(float r) {
        double xCurrent = this.xPos;
        double yCurrent = this.yPos;
        double zCurrent = this.zPos;
        theta += 0.05;
        // Spherical coordinates maths
        double xMovement = r * Math.cos(theta);
        double zMovement = r * Math.sin(theta);

        double xNew = xCurrent + xMovement;
        double yNew = yCurrent;
        double zNew = zCurrent + zMovement;

        updatePosition(xNew, yNew, zNew);
    }
}
