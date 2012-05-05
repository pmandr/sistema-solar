/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package solar;

import java.util.ArrayList;
import java.util.Iterator;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

/**
 *
 * @author Lobosque
 */
public class Planeta extends Estrela {

    static float d = 32;
    private ArrayList<Lua> moons = new ArrayList<Lua>();
    private ArrayList<Anel> rings = new ArrayList<Anel>();
    
    static ArrayList<Planeta> create_planets() {
        ArrayList<Planeta> planets = new ArrayList<Planeta>();
        Planeta earth;
        Planeta saturn;
        Planeta mars;
        Planeta jupiter;
        Planeta saturno;
        Planeta urano;
        Planeta neptuno;
        Planeta plutao;
        planets.add(new Planeta("Mercury", 1.0f, 0.4f*d, 3.38f, 0.03f));
        planets.add(new Planeta("Venus", 1.0f, 0.7f*d, 3.86f, 177.4f));
        earth = new Planeta("Earth", 1.0f, 1.0f*d, 7.155f, 23.44f);
        earth.add_moon("Moon", 0.3f, 10, earth);
        planets.add(earth);
        mars = new Planeta("Mars", 1.0f, 1.6f*d, 5.65f, 25.15f);
        mars.add_moon("Moon", 0.1f, 10, mars); //fobos
        mars.add_moon("Moon", 0.3f, 10, mars); //delmos
        planets.add(mars);
        jupiter = new Planeta("Jupiter", 4.0f, 3.0f*d, 6.09f, 3.13f);
        jupiter.add_moon("Moon", 0.3f, 10, jupiter);
        jupiter.add_moon("Moon", 0.2f, 10, jupiter);
        jupiter.add_moon("Moon", 0.5f, 10, jupiter);
        jupiter.add_moon("Moon", 0.4f, 10, jupiter);
        planets.add(jupiter);
        saturno  = new Planeta("Saturn",  4.0f, 4.5f*d, 5.51f, 0);
        saturno.add_moon("Moon", 0.1f, 0, saturno);
        saturno.add_moon("Moon", 0.1f, 0, saturno);
        saturno.add_moon("Moon", 0.1f, 0, saturno);
        saturno.add_moon("Moon", 0.1f, 0, saturno);
        saturno.add_moon("Moon", 0.15f, 0, saturno);
        saturno.add_moon("Moon", 0.2f, 0, saturno);
        saturno.add_moon("Moon", 0.2f, 0, saturno);
        saturno.add_moon("Moon", 0.5f, 0, saturno);
        saturno.addRings("Ring", 0.0f, 0, saturno);
        planets.add(saturno);
        urano  = new Planeta("Uranus", 2.0f, 6.6f*d, 6.48f, 97.77f);
        urano.add_moon("Moon", 0.1f, 0, urano);
        urano.add_moon("Moon", 0.1f, 0, urano);
        urano.add_moon("Moon", 0.1f, 0, urano);
        urano.add_moon("Moon", 0.1f, 0, urano);
        urano.add_moon("Moon", 0.1f, 0, urano);
        planets.add(urano);
        neptuno  = new Planeta("Neptune", 2.0f, 7.8f*d, 6.43f, 28.32f);
        neptuno.add_moon("Moon", 0.1f, 0, neptuno);
        neptuno.add_moon("Moon", 0.3f, 0, neptuno);
        neptuno.add_moon("Moon", 0.1f, 0, neptuno);
        planets.add(neptuno);
        plutao = new Planeta("Pluto", 0.4f, 10.2f*d, 3.0f, 119.61f);
        plutao.add_moon("Moon", 0.1f, 0, plutao);
        planets.add(plutao);

        return planets;
    }

    void add_moon(String name,float radius,float angle, Planeta father) {
        this.moons.add(new Lua(name, radius, angle, father));
    }

    void addRings(String name, float radius, int angle, Planeta father) {
        this.rings.add(new Anel(name, radius, angle, father));
    }

    protected void draw_moons(GL gl, GLU glu) {
        Iterator<Lua> itr = moons.iterator();
        while (itr.hasNext()) {
            itr.next().draw(gl , glu);
        }
    }
    
    protected void draw_rings(GL gl, GLU glu) {
        Iterator<Anel> itr = rings.iterator();
        while (itr.hasNext()) {
            itr.next().draw(gl , glu);
        }
    }

    protected void draw(GL gl, GLU glu) {
        super.draw(gl, glu);
        draw_moons(gl, glu);
        draw_rings(gl, glu);
    }

    private Planeta(String string, float radius, float distance, float angle, float tilt) {
        super(string, radius, distance, angle, tilt);
    }

   
}
