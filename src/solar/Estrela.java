/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package solar;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureData;
import com.sun.opengl.util.texture.TextureIO;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

/**
 *
 * @author Lobosque
 */
public class Estrela {

    protected String name;

    protected float radius;
    protected float distance;
    protected Texture texture;
    protected float angle_sun;
    protected float tilt;
    private float x, y, z;

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }



    protected int SLICES = 32;
    protected int STACKS = 32;
    public Estrela() {
    }

    public Estrela(String name, float radius, float distance, float angle, float tilt) {
        this.name = name;
        this.radius = radius;
        this.distance = distance;
        this.angle_sun = angle;
        this.tilt = tilt;
        load_texture(name.toLowerCase());
    }
    
    public float getRadius(){
        return this.radius;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }


    void drawOrbit(float inc, GL gl) {
        gl.glPushMatrix();
        float oy = 0.0f;
        gl.glRotatef(angle_sun, 1.0f, 0.0f, 1.0f);
        gl.glBegin(GL.GL_POINTS);
        for (float angle = 0; angle <= 360; angle += inc) {
            float ox = (float) (distance * Math.sin((angle) * 3.1415926535 / 180));
            float oz = (float) (distance * Math.cos((angle) * 3.1415926535 / 180));
            gl.glVertex3f(ox, oy, oz);
        }
        gl.glEnd();

        gl.glPopMatrix();
    }


    protected void draw(GL gl, GLU glu) {
        gl.glColor3f(0.5f, 0.5f, 0.5f);
        if(Solar2.show_orbit) drawOrbit(2f, gl);
        texture.enable();
        texture.bind();

        GLUquadric star = glu.gluNewQuadric();
        glu.gluQuadricTexture(star, true);
        glu.gluQuadricDrawStyle(star, GLU.GLU_FILL);
        glu.gluQuadricNormals(star, GLU.GLU_FLAT);
        glu.gluQuadricOrientation(star, GLU.GLU_OUTSIDE);

        float speed = 64/distance;
        
        //controle de translacao
        this.x = (float) (distance * Math.sin((Time.year * speed) * 3.1415926535 / 180));
        this.z = (float) (distance * Math.cos((Time.year * speed) * 3.1415926535 / 180));
        gl.glTranslatef(x, 0.0f, z);
        gl.glRotatef(angle_sun, 1.0f, 0.0f, 1.0f);
        float speed_rot = 128/radius;
        gl.glRotatef((float) Time.year*speed_rot, 0.0f, 1.0f, 0.0f);
        
        //deixa os planetas "em pe": 90 eh perfeitamente alinhado
        gl.glRotatef(85, 1.0f, 0.0f, 0.0f);
        //gl.glRotatef(tilt, 0.0f, 0.0f,0.0f);

        
        
        glu.gluSphere(star, radius, SLICES, STACKS);
        glu.gluDeleteQuadric(star);
    }

    protected void load_texture(String filename) {
        try {
            System.out.println("Carregando textura de: " + filename);

            InputStream stream = getClass().getResourceAsStream("/texturas/" + filename + ".jpg");
            TextureData data = TextureIO.newTextureData(stream, false, "jpg");
            texture = TextureIO.newTexture(data);
        } catch (IOException exc) {
            System.out.println("Falha ao carregar a textura de " + filename);
            //System.exit(1);
        }
    }
}
