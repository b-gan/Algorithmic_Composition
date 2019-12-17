package algomusic_berwin_class;

import com.jsyn.ports.UnitOutputPort;
import com.jsyn.unitgen.Circuit;
import com.jsyn.unitgen.SineOscillator;
import com.jsyn.unitgen.UnitVoice;
import com.softsynth.shared.time.TimeStamp;

public class SineVoice extends Circuit implements UnitVoice {
	
	SineOscillator osc;
	public SineVoice() { //constructor 
		add(osc = new SineOscillator());
		
	}
	public UnitOutputPort getOutput() {
		return osc.output;
	}
	
	public void noteOff(TimeStamp ts) {
		osc.amplitude.set(0,ts);
	}
	
	public void noteOn(double freq, double amp, TimeStamp ts) {
		osc.frequency.set(freq,ts);
		osc.amplitude.set(amp,ts);
	}

}
