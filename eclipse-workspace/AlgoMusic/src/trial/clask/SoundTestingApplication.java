//BERWIN GAN WQG203
package trial.clask;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.swing.SoundTweaker;
import com.jsyn.unitgen.LineOut;

public class SoundTestingApplication {
	public static void main(String[] args) {

		final Synthesizer synth = JSyn.createSynthesizer();
		synth.start();

		BerwinBellDelay voice = new BerwinBellDelay();
		synth.add(voice);

		LineOut out = new LineOut();
		synth.add(out);
		out.start();

		voice.getOutput().connect(0, out.input, 0);
		voice.getOutput().connect(0, out.input, 1);

		JFrame jf = new JFrame();
		SoundTweaker tweaker = new SoundTweaker(synth, "Test this sound", voice);
		jf.add(tweaker);
		jf.pack();
		jf.setVisible(true);
		jf.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				synth.stop();

				System.exit(0);
			}

		});
	}

}