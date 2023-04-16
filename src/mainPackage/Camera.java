package mainPackage;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Component;

public class Camera implements KeyListener {
    //Camera movement speeds
    public final double MOVE_SPEED = 0.15;
    public final double ROTATION_SPEED = 0.08;

    //Location of player on 2D map as well as x and y components for vector of player
    public double xPos,yPos,xDir, yDir, xPlane, yPlane;
    //Keep track of what keys are being pressed
    public boolean left, right, forward, back;

    //Constructor that tells where camera is
    public Camera (double x, double y, double xd, double yd, double xp, double yp) {
        xPos = x;
        yPos = y;
        xDir = xd;
        yDir = yd;
        xPlane = xp;
        yPlane = yp;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    //While key is pressed set boolean to true
    public void keyPressed (KeyEvent key) {
        if ((key.getKeyCode() == KeyEvent.VK_LEFT))
            left = true;
        if ((key.getKeyCode() == KeyEvent.VK_RIGHT))
            right = true;
        if ((key.getKeyCode() == KeyEvent.VK_UP))
            forward = true;
        if ((key.getKeyCode() == KeyEvent.VK_DOWN))
            back = true;
    }

    //When key is released set boolean to false
    public void keyReleased (KeyEvent key) {
        if ((key.getKeyCode() == KeyEvent.VK_LEFT))
            left = false;
        if ((key.getKeyCode() == KeyEvent.VK_RIGHT))
            right = false;
        if ((key.getKeyCode() == KeyEvent.VK_UP))
            forward = false;
        if ((key.getKeyCode() == KeyEvent.VK_DOWN))
            back = false;
    }

    public void update(int[][] map) {
        //Move character forward while not at a wall
        if (forward) {
            //To move character add xDir to the xPos multiplied by movement speed
            if (map[(int)(xPos + xDir * MOVE_SPEED)][(int)(yPos)] == 0)
                xPos += xDir*MOVE_SPEED;
            if (map[(int)(xPos)][(int)(yPos + yDir * MOVE_SPEED)] == 0)
                yPos += yDir*MOVE_SPEED;
        }

        //Move character backward while not at a wall
        if (back) {
            if (map[(int)(xPos - xDir * MOVE_SPEED)][(int)(yPos)] == 0)
                xPos -= xDir*MOVE_SPEED;
            if (map[(int)(xPos)][(int)(yPos - yDir * MOVE_SPEED)] == 0)
                yPos -= yDir*MOVE_SPEED;
        }

        //Rotate character right
        /*Rotation Matrix:
        [  cos(theta)  -sin(theta)  ]
        [  sin(theta)   cos(theta)  ]
         */
        if (right) {
            //Use rotation matrix to rotate the characters
            double oldXDir = xDir;
            xDir = xDir*Math.cos(-ROTATION_SPEED) - yDir*Math.sin(-ROTATION_SPEED);
            yDir = oldXDir*Math.sin(-ROTATION_SPEED) + yDir*Math.cos(-ROTATION_SPEED);

            double oldXPlane = xPlane;
            xPlane = xPlane*Math.cos(-ROTATION_SPEED) - yPlane*Math.sin(-ROTATION_SPEED);
            yPlane = oldXPlane*Math.sin(-ROTATION_SPEED) + yPlane*Math.cos(-ROTATION_SPEED);
        }

        //Rotate character left
        if (left) {
            double oldXDir = xDir;
            xDir = xDir*Math.cos(ROTATION_SPEED) - yDir*Math.sin(ROTATION_SPEED);
            yDir = oldXDir*Math.sin(ROTATION_SPEED) + yDir*Math.cos(ROTATION_SPEED);

            double oldXPlane = xPlane;
            xPlane = xPlane*Math.cos(ROTATION_SPEED) - yPlane*Math.sin(ROTATION_SPEED);
            yPlane = oldXPlane*Math.sin(ROTATION_SPEED) + yPlane*Math.cos(ROTATION_SPEED);
        }
    }
}
