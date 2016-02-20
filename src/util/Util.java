/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author szend
 */
public class Util {

    public static float angleToPositiveToOctave(float angle) {
        float newangle;
        newangle = (angle < 0) ? 360 + angle : angle;
        newangle -= (newangle % 45);
        return newangle;
    }

}
