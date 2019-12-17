//BERWIN GAN WQG203
package algomusic_berwin_class;
import java.util.Random;

import java.util.concurrent.ThreadLocalRandom;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.unitgen.UnitVoice;
import com.jsyn.unitgen.SawtoothOscillatorBL;
import com.jsyn.unitgen.VariableRateMonoReader;
import com.jsyn.ports.UnitInputPort;
import com.jsyn.unitgen.Multiply;
import com.softsynth.shared.time.TimeStamp;
import com.jsyn.unitgen.PassThrough;
import com.jsyn.data.SegmentedEnvelope;
import com.jsyn.unitgen.Circuit;

public class MultiRandom extends Circuit implements UnitVoice {
    PassThrough mFrequencyPassThrough;
    public UnitInputPort frequency;
    
    PassThrough mAmplitudePassThrough;
    public UnitInputPort amplitude;
    
    PassThrough mOutputPassThrough;
    public UnitOutputPort output;
    
    SegmentedEnvelope mS;
    VariableRateMonoReader mMonoRdr;
    SawtoothOscillatorBL mSawOscBL;
    SawtoothOscillatorBL mSawOscBL2;
    Multiply mAtimes;
    
    SegmentedEnvelope segEnv;
    VariableRateMonoReader reader;

    public MultiRandom() {

        add(mFrequencyPassThrough = new PassThrough());
        addPort(frequency = mFrequencyPassThrough.input, "frequency");
        add(mAmplitudePassThrough = new PassThrough());
        addPort(amplitude = mAmplitudePassThrough.input, "amplitude");
        add(mOutputPassThrough = new PassThrough());
        addPort( output = mOutputPassThrough.output, "output");
        
        add(reader = new VariableRateMonoReader());

        add(mMonoRdr = new VariableRateMonoReader());
        add(mSawOscBL = new SawtoothOscillatorBL());
        add(mSawOscBL2 = new SawtoothOscillatorBL());
        add(mAtimes = new Multiply());
            
        mFrequencyPassThrough.output.connect(mSawOscBL.frequency);
        mFrequencyPassThrough.output.connect(mAtimes.inputA);
        mAmplitudePassThrough.output.connect(mMonoRdr.amplitude);
        mMonoRdr.output.connect(mSawOscBL.amplitude);
        mMonoRdr.output.connect(mSawOscBL2.amplitude);
        mSawOscBL.output.connect(mOutputPassThrough.input);
        mSawOscBL2.output.connect(mOutputPassThrough.input);
        mAtimes.output.connect(mSawOscBL2.frequency);
        buildEnvelope();
    }
	private void buildEnvelope() {
		double[] envData = {
	            0.1, 0.4,
	            0.0, 0.8,
	            0.1, 0.4,
	            0.0, 0.8,
	            0.5, 0.4,
	            0.01, 0.8,
	            0.0, 0.4,
	            0.06, 0.8,
	            0.2, 0.0,
	        };
		segEnv = new SegmentedEnvelope(envData);
		envData = null;
		segEnv.setSustainBegin(2);
		segEnv.setSustainEnd(6);
	}
	
    public void noteOn(double freq, double amp, TimeStamp ts) {
    	//if you comment out the ones below till mAtimes, it will sound like a human throat
//        double min = 2.0;
//        double max = 11.0;
//        double randomNum = ThreadLocalRandom.current().nextDouble(min, max + 1);
//        mAtimes.inputB.set(randomNum); //insert random multipler for cough here 
        
        this.frequency.set(freq, ts);
		reader.amplitude.set(amp, ts);
		reader.dataQueue.queueOn(segEnv, ts);
    }

    public void noteOff(TimeStamp timeStamp) {
        mMonoRdr.dataQueue.queueOff( mS, false, timeStamp);
    }
    
    public UnitOutputPort getOutput() {
        return output;
    }
}
