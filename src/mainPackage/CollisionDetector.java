package mainPackage;

import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnCollisionEntry;
import javax.media.j3d.WakeupOnCollisionExit;

public class CollisionDetector extends Behavior {

    private Shape3D shape1;
    private Shape3D shape2;

    public CollisionDetector(Shape3D shape1, Shape3D shape2) {
        this.shape1 = shape1;
        this.shape2 = shape2;
    }

    public void initialize() {
        this.wakeupOn(new WakeupOnCollisionEntry(shape1));
    }

    public void processStimulus(Enumeration criteria) {
        while (criteria.hasMoreElements()) {
            WakeupCriterion criterion = (WakeupCriterion) criteria.nextElement();
            if (criterion instanceof WakeupOnCollisionEntry) {
                WakeupOnCollisionEntry collisionEntry = (WakeupOnCollisionEntry) criterion;
                if (collisionEntry.getTriggeringPath().getObject() == shape2) {
                    System.out.println("Collision detected!");
                    System.exit(1);
                }
            }
        }
        this.wakeupOn(new WakeupOnCollisionEntry(shape1));
    }
}

