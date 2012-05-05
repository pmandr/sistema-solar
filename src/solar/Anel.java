/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package solar;

import java.util.ArrayList;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

/**
 *
 * @author Lobosque
 */
public class Anel extends Estrela {

    private Planeta father;


    static ArrayList<Anel> create_planets() {
        ArrayList<Anel> planets = new ArrayList<Anel>();

        return planets;
    }

    protected Anel(String name, float radius, float angle, Planeta father) {
        this.name = name;
        this.radius = radius;
        this.angle_sun = angle;
        this.father = father;
        load_texture(name.toLowerCase());
    }

    @Override
    protected void draw(GL gl, GLU glu) {
        gl.glPushMatrix();

        texture.enable();
        texture.bind();

        GLUquadric star = glu.gluNewQuadric();
        glu.gluQuadricTexture(star, true);
        glu.gluQuadricDrawStyle(star, GLU.GLU_FILL);
        glu.gluQuadricNormals(star, GLU.GLU_FLAT);
        glu.gluQuadricOrientation(star, GLU.GLU_OUTSIDE);
        float speed = 1;
        //gl.glRotatef(father.angle_sun, 1.0f, 0.0f, 1.0f);
        float x = (float) (1.5 * Math.sin((Time.year * speed) * 3.1415926535 / 180));
        float z = (float) (1.5 * Math.cos((Time.year * speed) * 3.1415926535 / 180));
        //gl.glTranslatef(x, (float) 0.7f, z);
        float speed_rot = 64/22;
        //gl.glRotatef((float) Time.year*speed_rot, 0.0f, 1.0f, 0.0f);
        //gl.glRotatef(90, 1.0f, 0.0f, 1.0f);

        //glu.gluSphere(star, 0.1, 64, 64);
        glu.gluDisk(star, 6, 8, 100, 2);
        glu.gluDeleteQuadric(star);
        gl.glPopMatrix();

    }


}