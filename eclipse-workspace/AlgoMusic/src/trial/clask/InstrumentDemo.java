package trial.clask;

import com.softsynth.jmsl.*;

public class InstrumentDemo extends InstrumentAdapter {

	public double play(double playTime, double timeStretch, double[] dar) {
		System.out.println("playTime=" + playTime);
		double duration = dar[0];
		double pitch = dar[1];
		System.out.println("pitch=" + pitch);

//		double amINan = dar[4];
//		Double ddd = new Double(amINan);
//		System.out.println((amINan == amINan) + " <---??? " + (ddd.equals(ddd)));
		return playTime + duration * timeStretch;
	}

	public static void main(String[] args) {
		InstrumentDemo ins = new InstrumentDemo();

		MusicShape s = new MusicShape(5);

		s.setDimName(4, "pokeyness");
		s.setDefault(4, 2.0);
		s.setLimits(4, 0, 5);

		s.useStandardDimensionNameSpace();
		s.prefab();
		
		// for (int i = 0; i < 5; i++) {
		//
		// double[] dar = new double[s.dimension()];
		//
		// for (int d = 0; d < s.dimension(); d++) {
		// double minLimit = s.getLowLimit(d);
		// double maxLimit = s.getHighLimit(d);
		// double randomValue = JMSLRandom.choose(minLimit, maxLimit);
		// dar[d] = randomValue;
		// }
		//
		// s.add(dar);
		// }

		// s.add(1.0, 60, .5, 0.8);
		// s.add(0.5, 61, .5, 0.8);
		// s.add(1.0, 62, .5, 1.4);
		// s.add(.25, 63, .5, 3.0);
		s.setRepeats(4);
		s.setRepeatPause(3);
		s.setInstrument(ins);

		s.print();

		s.launch(JMSL.now());

	}

}
