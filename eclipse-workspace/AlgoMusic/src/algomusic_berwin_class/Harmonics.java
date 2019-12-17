package algomusic_berwin_class;
//BERWIN GAN (wqg203)

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.data.SegmentedEnvelope;
import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.unitgen.*;
import com.softsynth.shared.time.TimeStamp;

public class Harmonics extends Circuit implements UnitVoice {



	PassThrough frequencyPassThrough;

	SineOscillator osc1; //base note 4
	SineOscillator osc2; //3rd up 5
 	SineOscillator osc3; //5th up 6
 
	MultiplyAdd final_out;
	MultiplyAdd fifth;
	MultiplyAdd seventh;

	SegmentedEnvelope segEnv;
	VariableRateMonoReader reader;

	
	UnitInputPort frequency;
	UnitInputPort first_harmonics; // ratio for major is 4:5 (0.8)
	UnitInputPort second_harmonics; // 4:6 (0.66)
	UnitInputPort third_harmonics; // 0.55 for a 7th

	public Harmonics() { //chords are additive not multiplicative
		add(frequencyPassThrough = new PassThrough());

		add(osc1 = new SineOscillator());
		//add(osc2 = new SineOscillator());
		//add(osc3 = new SineOscillator());
		
		add(final_out = new MultiplyAdd());
		add(fifth = new MultiplyAdd());
		add(seventh = new MultiplyAdd());
		add(reader = new VariableRateMonoReader());


		reader.output.connect(osc1.amplitude);
		frequencyPassThrough.output.connect(osc1.frequency);
		
		osc1.output.connect(final_out.inputA); //Initially required 
		osc1.output.connect(final_out.inputC); //output for now
		
		osc1.output.connect(fifth.inputA); //orignal
		final_out.output.connect(fifth.inputC); //first and third
		
		osc1.output.connect(seventh.inputA);
		fifth.output.connect(seventh.inputC);
		
		addPort(first_harmonics = final_out.inputB, "First Harmonics(1.25)"); 
		addPort(second_harmonics = fifth.inputB, "Second Harmonics(1.5)");
		addPort(third_harmonics = seventh.inputB, "Third Harmonics(1.8 for 7th)");
		addPort(frequency = frequencyPassThrough.input, "Original Freq");

		first_harmonics.setup(0,0.1,3);
		second_harmonics.setup(0,0.1,3);
		third_harmonics.setup(0,0.1,3);

		buildEnvelope();
	}

	private void buildEnvelope() {
		double[] envData = { 0.5, 1.0,
	            0.5, 0.2,
	            0.5, 0.8,
	            0.5, 0.0, };
		segEnv = new SegmentedEnvelope(envData);
		envData = null;
		segEnv.setSustainBegin(1);
		segEnv.setSustainEnd(4);
	}

	public UnitOutputPort getOutput() {
		return seventh.output;
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

		Harmonics myVoice = new Harmonics();
		synth.add(myVoice);

		myVoice.getOutput().connect(0, out.input, 0);
		myVoice.getOutput().connect(1, out.input, 1);

		TimeStamp ts = synth.createTimeStamp();
		myVoice.noteOn(300, 0.8, ts.makeRelative(1));
		myVoice.noteOff(ts.makeRelative(3));

	}
}
