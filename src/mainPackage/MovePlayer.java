package mainPackage;

import javax.media.j3d.*;
import javax.vecmath.Vector3f;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Enumeration;

public class MovePlayer extends Behavior {
    private final float MOVE_UP = 0.1f;
    private final float MOVE_DOWN = -0.1f;

    private WakeupCondition awake = null;
    private WakeupCriterion[] wakeupArray = new WakeupCriterion[1];
    private WakeupOnAWTEvent keyPressed = null;

    private TransformGroup transformers = null;


    /* Constructor that takes in a transform group
    * This transform group is then modified when an
    * AWT event is realized. The behavior is then
    * woken up every time W, S, Up, or Down is
    * pressed
    */
    public MovePlayer(TransformGroup tg) {
        transformers = tg;

        keyPressed = new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED);
        wakeupArray[0] = keyPressed;
        awake = new WakeupOr(wakeupArray);
    }

    @Override
    public void initialize() {
        wakeupOn(awake);
    }

    @Override
    public void processStimulus(Enumeration enumeration) {
        /*
         * This function loops through the enumerable to wake up the behavior
         * It overrides the processStimulus method from the Behavior class
         */
        WakeupOnAWTEvent ev;
        WakeupCriterion genericEvt;
        AWTEvent[] events;

        /*
         * We cast the enumeration to a WakeupCriterion and get teh AWTEvent from it
         * Once the AWTEvent is grabbed we can process it and define the behavior we want
         * that event to perform
         */
        while (enumeration.hasMoreElements()) {
            genericEvt = (WakeupCriterion) enumeration.nextElement();

            if (genericEvt instanceof WakeupOnAWTEvent) {
                ev = (WakeupOnAWTEvent) genericEvt;
                events = ev.getAWTEvent();
                processAWTEvent(events);
            }
        }

        wakeupOn(awake);
    }

    /*
     * This function is going to get the key that was pressed. Then
     * it will determine if that key was W, Up, or S, Down.
     * Depending on the key pressed it will apply a Transform3D to the
     * global transform group.
     */
    public void processAWTEvent(AWTEvent[] events) {
        for( int n = 0; n < events.length; n++)
        {
            //Here we check to see if the AWTEvent is a key event
            if( events[n] instanceof KeyEvent)
            {
                KeyEvent eventKey = (KeyEvent) events[n];

                //Here we check if that key event is a key pressed event
                if( eventKey.getID() == KeyEvent.KEY_PRESSED )
                {
                    int keyCode = eventKey.getKeyCode();

                    //Creating a translate vector that is applied to a transform3d
                    Vector3f translate = new Vector3f();
                    Transform3D t3d = new Transform3D();
                    transformers.getTransform(t3d);
                    t3d.get(translate);

                    //Create a switch case to check if we want to move the object up or down
                    switch (keyCode)
                    {
                        case KeyEvent.VK_W:
                        case KeyEvent.VK_UP:
                            translate.y += MOVE_UP;
                            break;

                        case KeyEvent.VK_S:
                        case KeyEvent.VK_DOWN:
                            translate.y += MOVE_DOWN;
                            break;
                    }

                    //Set the global transform group with the new translation values
                    t3d.setTranslation(translate);
                    transformers.setTransform(t3d);
                }
            }
        }
    }
}
