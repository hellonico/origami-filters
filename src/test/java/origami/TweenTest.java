package origami;

import org.junit.Test;
import origami.tween.Orchestrator;
import origami.tween.Range;

public class TweenTest {

    @Test
    public void tweening() throws InterruptedException {
        Orchestrator a = new Orchestrator();
        Filter f = Origami.StringToFilter("{:class origami.filters.brandnew.Mosaical}");
        // TODO: load tween from string
        a.addTween(new Range().target(f));
        // TODO: load tween from string
        a.start();

        // Thread.sleep(2000);
        a.waitForAll();
//        a.stopAnimations();
    }

    @Test
    public void tweening2() throws InterruptedException {
        Orchestrator a = new Orchestrator();
        Filter f = Origami.StringToFilter("{:class origami.filters.RotateWithMatrix}");
        // TODO: load tween from string

        Range tween = new Range();

        tween.start = 10.0;
        tween.end = 11.0;
        tween.step = 0.1;
        tween.sleep = 50;
        tween.reverse = true;
        tween.fieldName = "rotationAngle";

        tween.target(f);

        String s = Origami.TweenToString(tween);
        System.out.println(s);
        Origami.StringToTween(s);

        a.addTween(tween);

        a.start();

        a.waitForAll();
    }
}
