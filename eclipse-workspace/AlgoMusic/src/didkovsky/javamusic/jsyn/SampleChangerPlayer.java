package didkovsky.javamusic.jsyn;

import java.awt.Dimension;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.data.FloatSample;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.VariableRateMonoReader;
import com.jsyn.util.SampleLoader;
import com.softsynth.jmsl.JMSLRandom;

public class SampleChangerPlayer {

	Synthesizer synth;
	LineOut out;
	FloatSample mySample;
	VariableRateMonoReader samplePlayer;

	void initJSyn() {
		synth = JSyn.createSynthesizer();
		synth.start();
		synth.add(out = new LineOut());
		out.start();
	}

	void buildSamplePlayer() {
		synth.add(samplePlayer = new VariableRateMonoReader());
		samplePlayer.output.connect(out.input);
	}

	void loadSample() throws IOException {
		File sampleFile = new File("/Users/berwingan/Downloads/pirate.mid");
		mySample = SampleLoader.loadFloatSample(sampleFile);
	}

	void changeSample() {
		float[] far = new float[mySample.getNumFrames()];
		mySample.read(far);

		// grab 0.1 seconds of it from somewhere and overwrite some other part
		// of it
		int numFramesToGrab = (int) (0.1 * mySample.getFrameRate());
		System.out.println("Grabbing " + numFramesToGrab + " frames");
		int startIndex = JMSLRandom.choose(0, far.length - numFramesToGrab);
		int destIndex = JMSLRandom.choose(0, far.length - numFramesToGrab);
		System.arraycopy(far, startIndex, far, destIndex, numFramesToGrab);
		mySample.write(far);
	}

	void playSample() {
		samplePlayer.amplitude.set(1);
		samplePlayer.rate.set(mySample.getFrameRate());
		samplePlayer.dataQueue.queue(mySample);
	}

	public static void main(String[] args) {
		final SampleChangerPlayer sampleChangerPlayer = new SampleChangerPlayer();
		sampleChangerPlayer.initJSyn();
		sampleChangerPlayer.buildSamplePlayer();

		JButton changeAndPlayButton = new JButton("Change and play");
		changeAndPlayButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sampleChangerPlayer.changeSample();
				sampleChangerPlayer.playSample();
			}
		});

		JFrame jf = new JFrame("Close to quit");
		jf.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		jf.add(changeAndPlayButton);
		jf.pack();
		jf.setVisible(true);

		try {
			sampleChangerPlayer.loadSample();
			sampleChangerPlayer.playSample();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
