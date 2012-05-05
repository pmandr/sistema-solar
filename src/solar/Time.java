/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package solar;

/**
 *
 * @author Lobosque
 */
public class Time {
    static float acceleration = 0.1f;
    static float year = 0;

      static void rotate() {

        year += acceleration;
        
        if (year > (Float.MAX_VALUE-10.0f)) {
            year = 0;
        }
        //glutPostRedisplay();
    }

     static void increaseAcc(){
         acceleration += 0.025;
     }

     static void decreaseAcc() {
         if(acceleration > 0.025) acceleration -= 0.025;
     }
}
