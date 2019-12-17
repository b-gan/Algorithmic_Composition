/** Store timestamps of incoming Midi in an array and print periodically to a file called stamps.txt
@author Nick Didkovsky 8/14/99 12:16PM
*/
package jmsltestsuite;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

import com.softsynth.jmsl.JMSL;
import com.softsynth.jmsl.MusicDevice;
import com.softsynth.jmsl.midi.*;

public class MidiInputTest implements MidiListener {

	double[] timestamps;
	int index;
	int size = 100;

	public MidiInputTest() {
		timestamps = new double[size];
		index = 0;
	}

	void dump() {
		try {
			PrintWriter out = new PrintWriter(new BufferedOutputStream(new FileOutputStream("stamps.txt")));
			for (int i = 1; i < size; i++) {
				double delta = timestamps[i] - timestamps[i - 1];
				out.println(delta + "");
			}
			out.close();
			System.out.println("Dumped");
		} catch (IOException e) {
			System.out.println("Error dumping timestamps: " + e);
		}
	}

	public void handleNoteOn(double timeStamp, int channel, int pitch, int velocity) {
		timestamps[index++] = timeStamp;
		if (index == size) {
			dump();
			index = 0;
		}
		System.out.println(timeStamp + ", " + pitch + ", " + velocity);
	}

	public void handleNoteOff(double timeStamp, int channel, int pitch, int velocity) {
	}

	public void handlePolyphonicAftertouch(double timeStamp, int channel, int pitch, int pressure) {
	}

	public void handleControlChange(double timeStamp, int channel, int id, int value) {
	}

	public void handleProgramChange(double timeStamp, int channel, int program) {
	}

	public void handleChannelAftertouch(double timeStamp, int channel, int pressure) {
	}

	public void handlePitchBend(double timeStamp, int channel, int lsb, int msb) {
	}

	public void handleSysEx(double timeStamp, byte[] data) {
	}

	public static void main(String args[]) {
		Frame f = new Frame("Listen to MIDI note on's and report timestamps");
		WindowAdapter windowAdapter = new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				JMSL.closeMusicDevices();
				System.exit(0);
			}
		};
		f.addWindowListener(windowAdapter);
		f.setSize(320, 200);
		f.setVisible(true);

		// MusicDevice dev = MidiIO_MidiShare.instance();
		MidiIO_JavaSound dev = MidiIO_JavaSound.instance();
//		dev.setInDevice(0);
//		dev.setOutDevice(1);
		dev.edit(f);
		dev.open();

		MidiParser parser = new MidiParser();
		JMSL.midi.addMidiParser(parser);
		parser.addMidiListener(new MidiInputTest());

	}

}
