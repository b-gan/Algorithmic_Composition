package teach;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import com.softsynth.jmsl.*;
import com.softsynth.jmsl.jsyn2.JSynMusicDevice;
import com.softsynth.jmsl.jsyn2.JSynUnitVoiceInstrument;
import com.softsynth.jmsl.jsyn2.unitvoices.FilteredSawtoothBL;
import com.softsynth.jmsl.util.EventDistributions;
import com.softsynth.jmsl.view.MusicShapeEditor;

public class JMSLJSyn extends JFrame {

	JMSLMixerContainer mixer;
	JSynUnitVoiceInstrument ins;
	MusicShape musicShape;

	void initJSynMusicDevice() {
		JSynMusicDevice dev = JSynMusicDevice.instance();
		dev.open();
	}

	void initMixer() {
		mixer = new JMSLMixerContainer();
		mixer.start();
	}

	void initIns() {
		// ins = new JSynUnitVoiceInstrument(8,
		// FancyVoiceModdedND.class.getName());
		// ins = new JSynUnitVoiceInstrument(8, SawVoice2.class.getName());
		ins = new JSynUnitVoiceInstrument(8, FilteredSawtoothBL.class.getName());
//		ins = new JSynUnitVoiceInstrument(8, ChooseYrFlavor.class.getName());
		mixer.addInstrument(ins);
	}

	void initMusicShape() {
		musicShape = new MusicShape(ins.getDimensionNameSpace());
		musicShape.prefab();

		tweakMusicShape();

		musicShape.print();
		musicShape.setInstrument(ins);
		musicShape.setRepeats(1000);
	}

	private void tweakMusicShape() {
		for (int i = 0; i < musicShape.size(); i++) {
			double ratio = 1.0001 + i * 0.2;
			double duration = EventDistributions.genEntryDelayMyhill(4, ratio);
			musicShape.set(duration, i, 0);
		}
	}

	void play() {
		musicShape.launch(JMSL.now());
	}

	void initMusicShapeEditor() {
		MusicShapeEditor se = new MusicShapeEditor();
		se.addMusicShape(musicShape);
		add(se.getComponent());
	}

	public static void main(String[] args) {
		JMSL.clock.setAdvance(0.5);
		JMSLJSyn demo = new JMSLJSyn();
		demo.initJSynMusicDevice();
		demo.initMixer();
		demo.initIns();
		demo.initMusicShape();

		demo.initMusicShapeEditor();
		demo.pack();
		demo.setVisible(true);

		demo.play();

		demo.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				JMSL.closeMusicDevices();
				System.exit(0);
			}
		});

	}
}
