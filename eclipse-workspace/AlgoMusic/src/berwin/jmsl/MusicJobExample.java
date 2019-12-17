package berwin.jmsl;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import com.softsynth.jmsl.JMSL;
import com.softsynth.jmsl.MusicJob;
import com.softsynth.jmsl.ParallelCollection;
import com.softsynth.jmsl.util.EventDistributions;

public class MusicJobExample extends MusicJob{
	public double repeat(double playTime) {
		//System.out.println("I am printing now, Playtime = "+ playTime + "JMSL.realtime() =" + JMSL.realTime());
		
		double duration  = EventDistributions.genEntryDelayLog(4); //4 events per unit 
		double duration2 = EventDistributions.genEntryDelayMyhill(4, 256); //knob here --> drunk drummer ratio 
		//Myhill Distribution
		//density and ratio
		System.out.println("Duration is "+ duration2);
		return playTime + duration2;
	}
	public static void main(String[] args) {
		MusicJobExample mj1 = new MusicJobExample();
		mj1.setRepeats(100);
		
		MusicJobExample mj2 = new MusicJobExample();
		mj2.setRepeats(100);
		
		ParallelCollection par = new ParallelCollection();
		par.add(mj2);
		par.add(mj1);
		//mj1.setRepeatPause(0.25);
		par.launch(JMSL.now());
		//mj1.launch(JMSL.now()); // spawns their own threads
		
		System.out.println("Main Done");
		JFrame jf = new JFrame("Close to exit");
		jf.setSize(new Dimension(320,200));
		jf.setVisible(true);
		jf.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {			
				System.exit(0);				
			}			
			
		});
		
	}
}
