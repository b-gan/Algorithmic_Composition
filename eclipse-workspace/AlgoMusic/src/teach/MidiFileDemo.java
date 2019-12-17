package didkovsky.javamusic.jmsl;

import java.io.IOException;

import com.softsynth.jmsl.*;
import com.softsynth.jmsl.jsyn2.JSynMusicDevice;
import com.softsynth.jmsl.jsyn2.JSynUnitVoiceInstrument;
import com.softsynth.jmsl.jsyn2.unitvoices.FilteredSawtoothBL;
import com.softsynth.jmsl.util.MIDIFileToMusicShape;

public class MidiFileDemo {

	MusicShape hbd;

	void readMidi() throws IOException {
		MIDIFileToMusicShape fileToMusicShape = new MIDIFileToMusicShape();
		fileToMusicShape.convertMIDIFile("/Users/nick/Desktop/happy_birthday.mid");
		ParallelCollection par = fileToMusicShape.getParallelCollection();
		System.out.println("Par col has " + par.size() + " children");
		for (int i = 0; i < par.size(); i++) {
			MusicShape s = (MusicShape) par.get(i);
			double totalDur = s.get(s.size() - 1, 0);
			s.differentiate(totalDur, 0);
			s.scale(1.0 / 127.0, 0, s.size() - 1, 2);
			s.print();
			if (i == 1) {
				hbd = s;
			}
		}
	}

	void play() {

		JMSL.clock.setAdvance(0.2);

		JSynMusicDevice dev = JSynMusicDevice.instance();
		dev.open();

		JMSLMixerContainer mixer = new JMSLMixerContainer();
		mixer.start();

		JSynUnitVoiceInstrument ins = new JSynUnitVoiceInstrument(8, FilteredSawtoothBL.class.getName());
		mixer.addInstrument(ins);
//		hbd.setTimeStretch(0.1);
		hbd.setInstrument(ins);
		hbd.setRepeats(4);
		hbd.remove(hbd.size() - 1); // CRASHES ART RUNTIME
		hbd.print();

		hbd.launch(JMSL.now());
	}

	public static void main(String[] args) {
		MidiFileDemo midiFileDemo = new MidiFileDemo();
		try {
			midiFileDemo.readMidi();
			midiFileDemo.play();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
