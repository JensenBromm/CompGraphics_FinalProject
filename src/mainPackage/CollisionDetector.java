package mainPackage;

import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.Shape3D;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnCollisionEntry;

public class CollisionDetector extends Behavior {

    private Shape3D shape1;
    private Shape3D shape2;

    public CollisionDetector(Shape3D shape1, Shape3D shape2) {
        this.shape1 = shape1;
        this.shape2 = shape2;
    }

    /* Called when a behavior is added to a WakeupCondition object
     * WakeupOnCollisionEntry.USE_GEOMETRY sets the collision bounds of the objects to their shape
     * Without this line the bounds are too large and a collision is detected in between the pipes
     */
    public void initialize() {
        this.wakeupOn(new WakeupOnCollisionEntry(shape1, WakeupOnCollisionEntry.USE_GEOMETRY));
    }

    /*
     * Looops through everything that triggered a wakeup
     * Checks if it is a wakeup on a collision entry
     * Checks if the collision is with shape 2
     * 			Shape 2 is always the top or bottom pipe
     */
    public void processStimulus(Enumeration criteria) {
        while (criteria.hasMoreElements()) {
            WakeupCriterion criterion = (WakeupCriterion) criteria.nextElement();
            if (criterion instanceof WakeupOnCollisionEntry) {
                WakeupOnCollisionEntry collisionEntry = (WakeupOnCollisionEntry) criterion;
                if (collisionEntry.getTriggeringPath().getObject() == shape2) {
                    System.out.println("Collision detected!");
                    Driver.gameOver = true;
                    //System.exit(1);
                }
            }
        }
        this.wakeupOn(new WakeupOnCollisionEntry(shape1));
    }
}

