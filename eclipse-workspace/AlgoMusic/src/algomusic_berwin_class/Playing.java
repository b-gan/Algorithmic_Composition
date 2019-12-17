package algomusic_berwin_class;

import com.jsyn.instruments.SubtractiveSynthVoice;
import com.softsynth.jmsl.*;
import com.softsynth.jmsl.jsyn2.JSynMusicDevice;
import com.softsynth.jmsl.jsyn2.JSynUnitVoiceInstrument;
import com.softsynth.jmsl.view.*;
import java.awt.*;
import java.awt.event.*;

public class Playing {

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
		JSynUnitVoiceInstrument ins = new JSynUnitVoiceInstrument(8, BerwinBellDelay.class.getName());
		JMSLMixerContainer mixer = new JMSLMixerContainer();
		mixer.start();
		mixer.addInstrument(ins);
		MusicShape s;
		s = new MusicShape(ins.getDimensionNameSpace());
		s.prefab(200); // number of rows
		s.setInstrument(ins);
		s.setRepeats(1000);
		s.launch(JMSL.now());
		MusicShapeEditor se = new MusicShapeEditor();
		se.addMusicShape(s);
		myFrame.add(BorderLayout.NORTH, se.getComponent());
		myFrame.add(BorderLayout.SOUTH, mixer.getPanAmpControlPanel());
		myFrame.pack();
		myFrame.setVisible(true);
	}

}
