package berwin.jmsl;

import com.softsynth.jmsl.InstrumentAdapter;
import com.softsynth.jmsl.JMSL;
import com.softsynth.jmsl.JMSLRandom;
import com.softsynth.jmsl.MusicShape;

public class InstrumentDemo extends InstrumentAdapter{

	
	public double play(double playTime, double timeStretch, double[] dar) {
		System.out.println("playtime"+playTime);
		double duration = dar[0];
		double pitch = dar[1];
		System.out.println("pitch is "+ pitch);
		return playTime + duration*timeStretch;
	}
	
	public static void main(String[] args) {
		InstrumentDemo ins = new InstrumentDemo();
		MusicShape s = new MusicShape(4);
		s.useStandardDimensionNameSpace();
//		s.add(1.0,60,0.5,0.8);
//		s.add(0.5,61,0.5,0.8);
//		s.add(1.0,62,0.5,1.4);
//		s.add(.25,63,0.5,3.0);
		
		s.prefab(); //auto fills in the java
		for(int i =0;i<=5;i++) {
			double[]dar = new double[4];
			double minValue = s.getLowLimit(0);
			double maxValue = s.getHighLimit(0);
			double randomValue = JMSLRandom.choose(minValue,maxValue);
			dar[0] = randomValue;
			s.add(dar);
		}
		
		s.setRepeats(4);
		s.setRepeatPause(2);
		s.setInstrument(ins);
		
		s.print();
		
		s.launch(JMSL.now());
	}
	

}
