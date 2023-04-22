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
        WakeupOnAWTEvent ev;
        WakeupCriterion genericEvt;
        AWTEvent[] events;

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

    public void processAWTEvent(AWTEvent[] events) {
        for( int n = 0; n < events.length; n++)
        {
            if( events[n] instanceof KeyEvent)
            {
                KeyEvent eventKey = (KeyEvent) events[n];

                if( eventKey.getID() == KeyEvent.KEY_PRESSED )
                {
                    int keyCode = eventKey.getKeyCode();

                    Vector3f translate = new Vector3f();

                    Transform3D t3d = new Transform3D();
                    transformers.getTransform(t3d);
                    t3d.get(translate);

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

                    t3d.setTranslation(translate);
                    transformers.setTransform(t3d);
                }
            }
        }
    }
}
