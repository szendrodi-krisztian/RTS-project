/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.texture.Texture2D;

/**
 *
 * @author Krisz
 */
public abstract class Unit {

    public enum Pose {

        STANDING, CROUCHING, LAYING
    }

    public enum Rotation {

        NORTH, SOUTH, EAST, WEST
    }
    
    private static BattleMap map;

    private IVehicle vehicle;
    private IWeapon weapon;

    private Pose pose;

    private Rotation rotation;

    public static final float N_STEP = 30;

    // The position on the grid
    private int posX, posY;
    // positional value [0-1[
    private float fractalX, fractalY;
    private float destX, destY;
    private float nextX, nextY;
    // Health points
    private int health;
    // Angle in rads
    private float accuracy;
    // [0-100]
    private int stamina;
    //
    private float discipline;
    //
    private float morale;
    //
    private float dmg_mult;
    
    public static void init(BattleMap map){
        Unit.map = map;
    }

    public Unit(IVehicle vehicle, IWeapon weapon) {
        this.vehicle = vehicle;
        this.weapon = weapon;
    }

    /**
     * Moves this unit for animation.
     *
     * @param tpf Time per frame.
     */
    public void move(float tpf) {

        fractalX += (tpf * nextX * vehicle.getMovementSpeed()) / N_STEP;
        fractalY += (tpf * nextY * vehicle.getMovementSpeed()) / N_STEP;
        if (FastMath.abs(fractalX) >= 1) {
            posX += FastMath.sign(fractalX);
            fractalX = 0;
            Vector2f d = map.dijkstra(posX, posY, destX, destY); // map.disjk : gives next position on the path
            nextX = d.x;
            nextY = d.y;
        }
        if (FastMath.abs(fractalY) >= 1) {
            posX += FastMath.sign(fractalY);
            fractalY = 0;
            Vector2f d = map.dijkstra(posX, posY, destX, destY);
            nextX = d.x;
            nextY = d.y;
        }

    }

    /**
     * Sets movement destination.
     *
     * @param x
     * @param y
     */
    public void moveTo(int x, int y) {
        destX = x;
        destY = y;
        Vector2f d = map.dijkstra(posX, posY, destX, destY);
        nextX = d.x;
        nextY = d.y;
    }

    public final void attack(int x, int y) {
        weapon.attack(pose, accuracy, dmg_mult, posX, posY, x, y);
    }

    public void setPose(Pose p) {
        pose = p;
    }

    public Pose getPose() {
        return pose;
    }
    
    public abstract Texture2D getTexture();

    @Override
    public final String toString() {
        return "Unit{" + "vehicle=" + vehicle + ", weapon=" + weapon + ", posX=" + posX + ", posY=" + posY + ", fractalX=" + fractalX + ", fractalY=" + fractalY + ", destX=" + destX + ", destY=" + destY + ", nextX=" + nextX + ", nextY=" + nextY + ", health=" + health + ", accuracy=" + accuracy + ", stamina=" + stamina + ", discipline=" + discipline + ", morale=" + morale + ", dmg_mult=" + dmg_mult + '}';
    }

}
