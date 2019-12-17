package algomusic_berwin_class;

import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.unitgen.Circuit;
import com.jsyn.unitgen.Multiply;
import com.jsyn.unitgen.SineOscillator;
import com.jsyn.unitgen.UnitVoice;
import com.softsynth.shared.time.TimeStamp;

public class RingVoice extends Circuit implements UnitVoice {
	
	SineOscillator osc;
	SineOscillator osc2;
	Multiply mult;
	
	UnitInputPort freq2;
	
	public RingVoice() { //constructor 
		add(osc = new SineOscillator());
		add(osc2 = new SineOscillator());
		add(mult = new Multiply());
		
		addPort(freq2 =  osc2.frequency);
		
		
		osc.output.connect(mult.inputA);
		osc2.output.connect(mult.inputB);
		
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
