/*
 * Created on Nov 19, 2012 by Nick
 *
 */
package teach;

import com.jsyn.instruments.SubtractiveSynthVoice;
import com.softsynth.jmsl.*;

import com.softsynth.jmsl.jsyn2.JSynMusicDevice;
import com.softsynth.jmsl.jsyn2.JSynUnitVoiceInstrument;
import com.softsynth.jmsl.view.*;

import didkovsky.javamusic.jmsl.SimpleRingMod;

import java.awt.*;
import java.awt.event.*;

/**
 * Test JSynUnitVoiceInstrument by putting it into a simple 4 dimensional
 * MusicShape
 * 
 * Uses pure Java JSyn API
 * 
 * @author Nick Didkovsky, Nov 19, 2012
 */

public class TestSubtractiveSynthVoiceInstrument {

	public static void main(String args[]) {

		Frame myFrame = new Frame();
		myFrame.setLayout(new BorderLayout());
		myFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				JMSL.closeMusicDevices();
				System.exit(0);
			}
		});

		JMSL.clock.setAdvance(0.1);
		JSynMusicDevice dev = JSynMusicDevice.instance();
		dev.edit(myFrame);
		dev.open();

		JSynUnitVoiceInstrument ins = new JSynUnitVoiceInstrument(8, SubtractiveSynthVoice.class.getName());
		JSynUnitVoiceInstrument sigProcIns = new JSynUnitVoiceInstrument(8, SimpleRingMod.class.getName());

		sigProcIns.addSignalSource(ins.getOutput());

		MusicShape sigShaper = new MusicShape(sigProcIns.getDimensionNameSpace());
		sigShaper.prefab();
		sigShaper.setInstrument(sigProcIns);
		sigShaper.setRepeats(1000);
		sigShaper.setName("Signal processor");

		JMSLMixerContainer mixer = new JMSLMixerContainer();
		mixer.start();
		mixer.addInstrument(ins);
		mixer.addInstrument(sigProcIns);
		mixer.panAmpChange(0, 0, 0.5);
		mixer.panAmpChange(1, 1, 0.5);

		// Build a MusicShape standard dimension name space:
		// duration, pitch, amplitude, hold

		MusicShape s = new MusicShape(4);
		s.useStandardDimensionNameSpace();
		s.add(0.5, 66, 0.35, 1.66);
		s.add(0.75, 77, 0.35, 2.66);
		s.add(0.25, 48, 0.35, 3.66);
		s.add(0.75, 77, 0.35, 1.66);
		s.add(0.25, 48, 0.35, 2.66);
		// put our instrument into the MusicShape
		s.setInstrument(ins);
		// make it repeat a long time, and launch it
		s.setRepeats(1000);

		// create a graphical editor to play with the shape data in realtime
		final MusicShapeEditor se = new MusicShapeEditor();
		se.addMusicShape(s);
		se.addMusicShape(sigShaper);

		s.addRepeatPlayable(new Playable() {

			public double play(double time, Composable parent) throws InterruptedException {
				MusicShape myMusicShape = (MusicShape) parent;
				myMusicShape.scramble(0, myMusicShape.size() - 1, -1);
				myMusicShape.randomize(70, 80, 0, 0, 1);
				se.refresh();
				return time;
			}
		});

		// s.launch(JMSL.now());
		ParallelCollection par = new ParallelCollection(s, sigShaper);
		par.launch(JMSL.now());

		// Build an awt Frame that exits gracefully when closed

		// add the MusicShapeEditor and mixer panel to the Frame
		myFrame.add(BorderLayout.NORTH, se.getComponent());
		myFrame.add(BorderLayout.SOUTH, mixer.getPanAmpControlPanel());
		myFrame.pack();
		myFrame.setVisible(true);
	}

}
