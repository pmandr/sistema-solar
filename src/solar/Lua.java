/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package solar;

import java.util.ArrayList;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import java.util.Random;

/**
 *
 * @author Lobosque
 */
public class Lua extends Estrela {

    private Planeta father;
    private Random r = new Random();
    private float speed = (float)r.nextInt(10); 
    private float speed_rot = r.nextInt(360)/22;
    private float distanciapai;
    float offsetx = -180.0f + (float)r.nextInt(180);
    float varx = r.nextInt(3)+r.nextFloat();
    float varz = r.nextInt(5)+r.nextFloat();
    
    protected Lua(String name, float radius, float angle, Planeta father) {
        this.name = name;
        this.radius = radius;
        this.angle_sun = angle;
        this.father = father;
        load_texture(name.toLowerCase());
        
        this.distanciapai= 0.2f+father.getRadius()+r.nextInt(3)+r.nextFloat();
    }
    
    static ArrayList<Lua> create_planets() {
        ArrayList<Lua> planets = new ArrayList<Lua>();

        return planets;
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
         
        //gl.glRotatef((float) Time.year*this.speed_rot, 0.0f, 1.0f, 0.0f);
        
        float x = (float) (varx* Math.sin((Time.year * this.speed) * 3.1415926535 / 180));
        float z = (float) (varz * Math.cos((Time.year * this.speed) * 3.1415926535 / 180));
        System.out.println(x);
        gl.glTranslatef(x, distanciapai, z);
        
        gl.glRotatef(offsetx, 0.0f, 1.0f, 1.0f);

        glu.gluSphere(star, this.radius, 32, 32);
        glu.gluDeleteQuadric(star);
        gl.glPopMatrix();

    }


}
