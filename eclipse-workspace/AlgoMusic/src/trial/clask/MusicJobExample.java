package trial.clask;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

 

import com.softsynth.jmsl.*;

import com.softsynth.jmsl.util.EventDistributions;

 

public class MusicJobExample extends MusicJob {

 

public double repeat(double playTime) {

System.out.println(getName() + " is repeating now, playTime=" + playTime + ", JMSL.realTime()=" + JMSL.realTime());

// double duration = EventDistributions.genEntryDelayLog(4);

double duration = EventDistributions.genEntryDelayMyhill(4, 100.80001);

System.out.println("duration=" + duration);

return playTime + duration;

}

 

public static void main(String[] args) {

JMSL.clock.setAdvance(.2);

MusicJobExample mj1 = new MusicJobExample();

mj1.setRepeats(10);

// mj1.setRepeatPause(.25);

// mj1.launch(JMSL.now());

MusicJobExample mj2 = new MusicJobExample();

mj2.setRepeats(10);

 

SequentialCollection par = new SequentialCollection();

par.add(mj1);

par.add(mj2);

par.setRepeats(3);

par.setRepeatPause(4);

par.launch(JMSL.now());

 

System.out.println("MAIN done");

JFrame jf = new JFrame("Close to exit");

jf.setSize(new Dimension(320, 200));

jf.setVisible(true);

jf.addWindowListener(new WindowAdapter() {

public void windowClosing(WindowEvent e) {

System.exit(0);

}

});

}

}