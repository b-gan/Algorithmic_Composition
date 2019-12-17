package algomusic_berwin_class;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.data.SegmentedEnvelope;
import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.unitgen.*;
import com.softsynth.shared.time.TimeStamp;

public class PanningRingMod extends Circuit implements UnitVoice {

	// SawtoothOscillatorBL osc1;

	PassThrough frequencyPassThrough;

	SineOscillator osc1;
	SineOscillator osc2;
	Multiply ringMult;
	Multiply freqMult;

	Pan pan;
	RedNoise redNoise;

	SegmentedEnvelope segEnv;
	VariableRateMonoReader reader;

	UnitInputPort panFrequency;
	UnitInputPort frequency;

	public PanningRingMod() {
		add(frequencyPassThrough = new PassThrough()); //used to to math 

		add(osc1 = new SineOscillator());
		add(osc2 = new SineOscillator());
		add(ringMult = new Multiply());
		add(freqMult = new Multiply());

		add(reader = new VariableRateMonoReader());
		add(pan = new Pan());
		add(redNoise = new RedNoise());

		reader.output.connect(osc1.amplitude);
		redNoise.output.connect(pan.pan);
		redNoise.output.connect(freqMult.inputB);
		frequencyPassThrough.output.connect(freqMult.inputA);
		frequencyPassThrough.output.connect(osc1.frequency);

		ringMult.output.connect(pan.input);
		freqMult.output.connect(osc2.frequency);
		osc1.output.connect(ringMult.inputA);
		osc2.output.connect(ringMult.inputB);

		addPort(panFrequency = redNoise.frequency, "PanFreq");
		addPort(frequency = frequencyPassThrough.input, "freqEncy");

		panFrequency.setup(0, .5, 310);

		buildEnvelope();
	}

	private void buildEnvelope() {
		double[] envData = { 0.1, 1.0, 0.3, 0.5, 0.2, 0.6, 0.2, 0.4, 0.4, 0.0 };
		segEnv = new SegmentedEnvelope(envData);
		envData = null;
		segEnv.setSustainBegin(1);
		segEnv.setSustainEnd(4);
	}

	public UnitOutputPort getOutput() {
		return pan.output;
	}

	public void noteOff(TimeStamp ts) {
		reader.dataQueue.queueOff(segEnv, true, ts);
	}

	public void noteOn(double freq, double amp, TimeStamp ts) {
		this.frequency.set(freq, ts);
		reader.amplitude.set(amp, ts);
		reader.dataQueue.queueOn(segEnv, ts);
		// reader.dataQueue.queue(segEnv, 0, 1, ts);
		// reader.dataQueue.queue(segEnv, 3, 1, ts.makeRelative(3));
		// reader.dataQueue.queueLoop(segEnv, 0, 1, ts);
		// reader.dataQueue.clear(ts.makeRelative(2));
	}

	public static void main(String[] args) {
		Synthesizer synth = JSyn.createSynthesizer();
		synth.start();

		LineOut out = new LineOut();
		synth.add(out);
		out.start();

		PanningRingMod myVoice = new PanningRingMod();
		synth.add(myVoice);

		myVoice.getOutput().connect(0, out.input, 0);
		myVoice.getOutput().connect(1, out.input, 1);

		TimeStamp ts = synth.createTimeStamp();
		myVoice.noteOn(300, 0.8, ts.makeRelative(1));
		myVoice.noteOff(ts.makeRelative(3));

	}
}
