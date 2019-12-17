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

public class Throat extends Circuit implements UnitVoice {
    // Declare units and ports.
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



    public Throat() {

        add(mFrequencyPassThrough = new PassThrough());
        addPort(frequency = mFrequencyPassThrough.input, "frequency");
        add(mAmplitudePassThrough = new PassThrough());
        addPort(amplitude = mAmplitudePassThrough.input, "amplitude");
        add(mOutputPassThrough = new PassThrough());
        addPort( output = mOutputPassThrough.output, "output");
        double[] mSData = {
            0.10243055555555555, 0.3684210526315789,
            0.0, 0.7719298245614035,
            0.09995930481585791, 0.3991228070175439,
            0.0, 0.7587719298245614,
            0.046104788113615736, 0.39035087719298245,
            0.0568381250952226, 0.7543859649122807,
            0.0, 0.3684210526315789,
            0.05817913520645002, 0.7192982456140351,
            0.1444114804776342, 0.0,
        };
        mS = new SegmentedEnvelope( mSData ); //Envelope from syntona 
        add(mMonoRdr = new VariableRateMonoReader());
        add(mSawOscBL = new SawtoothOscillatorBL());
        add(mSawOscBL2 = new SawtoothOscillatorBL());
        add(mAtimes = new Multiply());
        // Connect units and ports.
        
        mFrequencyPassThrough.output.connect(mSawOscBL.frequency);
        mFrequencyPassThrough.output.connect(mAtimes.inputA);
        mAmplitudePassThrough.output.connect(mMonoRdr.amplitude);
        mMonoRdr.output.connect(mSawOscBL.amplitude);
        mMonoRdr.output.connect(mSawOscBL2.amplitude);
        mSawOscBL.output.connect(mOutputPassThrough.input);
        mSawOscBL2.output.connect(mOutputPassThrough.input);
        mAtimes.output.connect(mSawOscBL2.frequency);
    }

    public void noteOn(double frequency, double amplitude, TimeStamp timeStamp) {
        this.frequency.set(frequency, timeStamp);
        this.amplitude.set(amplitude, timeStamp);
        mMonoRdr.dataQueue.queueOn( mS, timeStamp);
    }

    public void noteOff(TimeStamp timeStamp) {
        mMonoRdr.dataQueue.queueOff( mS, false, timeStamp);
    }
    
    public UnitOutputPort getOutput() {
        return output;
    }
}
