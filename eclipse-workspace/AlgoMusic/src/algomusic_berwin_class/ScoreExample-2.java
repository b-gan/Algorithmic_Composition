package algomusic_berwin_class;

import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

import com.softsynth.jmsl.*;
import com.softsynth.jmsl.jsyn2.JSynMusicDevice;
import com.softsynth.jmsl.jsyn2.JSynUnitVoiceInstrument;
import com.softsynth.jmsl.score.*;
import com.softsynth.jmsl.score.transcribe.*;
import com.softsynth.jmsl.util.Patch;
import com.softsynth.jmsl.util.TimeSignature;


public class ScoreExample {

	Score score;
	ScoreFrame scoreFrame;
	JSynUnitVoiceInstrument ins;
	JSynUnitVoiceInstrument sigProcIns;

	void initJSyn() {
		JSynMusicDevice jsynMusicDevice = JSynMusicDevice.instance();
		jsynMusicDevice.open();
	}

	void initInstrument() {
		ins = new JSynUnitVoiceInstrument(8, BerwinBellDelay.class.getName());
		sigProcIns = new JSynUnitVoiceInstrument(8, SimpleRingMod.class.getName());
		Orchestra orch = new Orchestra();
		orch.addInstrument(ins);
		orch.addInstrument(sigProcIns);
		// sigProcIns.addSignalSource(ins);
		orch.addOrchPatch(new Patch(0, 1));
		orch.patchInstruments();
		score.setOrchestra(orch);

	}

	void initScore() {
		score = new Score(2, 800, 400, "My Score Example");
		score.addMeasure();
	}

	public void initScoreFrame() {
		scoreFrame = new ScoreFrame();
		scoreFrame.addUnaryCopyBufferTransform(new MyUnaryCopyBufferTransform());
		scoreFrame.addScore(score);
		scoreFrame.pack();
		scoreFrame.setVisible(true);
	}

	void populateScore() {
//		// Note note = score.addNote(0, 61, 0.5, 0.8);
//		// note.setAccPref(Note.ACC_PREFER_FLAT);
//		// MusicShape temp = new MusicShape(ins.getDimensionNameSpace());
//		// double[] data = temp.getDefaultArray();
//		// JMSL.printDoubleArray(data);
//		// data[1] = 72;
//		// Note note = score.addNote(data);
		double arr[];
		arr = new double[1000];
		int point = 0;
		double scale[] = {60.0,63.0,65.0,66.0,67.0,70.0,72.0};
		for(int i=0;i<7;i++) {
			int j = JMSLRandom.choose(3,12);
			for(int k =0;k<j;k++) {
				arr[point] = scale[i];
				point++;
			}
		}
		for(int i=0;i<7;i++) {
			int j = JMSLRandom.choose(3,7);
			for(int k =0;k<j;k++) {
				arr[point] = scale[i]+12;
				point++;
			}
		}
		for(int i=0;i<7;i++) {
			int j = JMSLRandom.choose(3,6);
			for(int k =0;k<j;k++) {
				arr[point] = scale[i]-12;
				point++;
			}
		}
		double arr1[];
		arr1 = new double[point];
        for (int i = 0; i < point; i++) {
            arr1[i] = arr[i];
        }
		
		MusicShape temp = new MusicShape(ins.getDimensionNameSpace());
		temp.prefab(1200); //100 per key
		temp.print();
		int count = 0;
		Random generator = new Random();
		 for (int i = 0; i < temp.size(); i++) {
			 int randomIndex = generator.nextInt(arr1.length);
			 double pitch = arr1[randomIndex];
			 temp.set(pitch, i,1);
			 temp.set(1.002,i,4); //microtonal
			 temp.set(1.001,i,5);
			 temp.set(1.003,i,6);
		 double[] data = temp.get(i);
		 if (data[0] > 0) {
		 score.addNote(data);
		 }
		 count++;
		 if(count ==100) {
			 for(int k =0;k<arr1.length;k++) {
				 arr1[k] = arr1[k]+1;
			 }
			 count=0;
		 }
		 }

		 
		temp.integrate(0);
		temp.print();
		BeatDivisionSchemeList.defaultSetup();
		Transcriber transcriber = new Transcriber();
		transcriber.setScore(score);
		transcriber.setSourceMusicShape(temp);
//
//		Vector v = new Vector();
//		// v.add(new TimeSignature(2, 4));
//		// v.add(new TimeSignature(2, 4));
//		// v.add(new TimeSignature(3, 4));
//		// transcriber.setTimeSignatures(v);
//
//		v.add(new TempoTimeSignatureHolder(new Tempo(120), new TimeSignature(4, 4)));
//		v.add(new TempoTimeSignatureHolder(new Tempo(72), new TimeSignature(3, 4)));
//		transcriber.setTempoTimeSignatures(v);
//
//		try {
//			transcriber.transcribe();
//		} catch (SearchPathListExpansionException e) {
//			e.printStackTrace();
//		} catch (ElementMissedException e) {
//			e.printStackTrace();
//		}
//
		score.rewind();
		score.setCurrentStaffNumber(1);
		// score.addNote(1.0, 70, 0.5, 0.8);
		// score.addNote(1.0, 76, 0.5, 0.8);
		// score.addNote(1.0, 73, 0.5, 0.8);
		// score.addNote(1.0, 72, 0.5, 0.8);
		MusicShape sigShape = new MusicShape(sigProcIns.getDimensionNameSpace()); //there is only 4 for the simple ring mod 
		sigShape.print();
		double decay = 1.1;
		double decayholder = 1.1;
		double decayminus = 1.0;
		for (int i = 0; i < 1200; i++) { //i must be about the same as the top// ossilate with increasing  decay 
			double dur = JMSLRandom.choose(1.0, decay);
			double pitch = 60 + decay * 2.2;
			double amp = 0.5;
			double hold = dur * 0.8;
			sigShape.add(dur, pitch, amp, hold);
			decay -= decayminus;
			decayminus += 0.23;
			if(decay<0) {
				decay = decayholder+0.12;
				decayholder = decay;
				decayminus /=4;
			}
		}
		sigShape.integrate(0);

		transcriber.setSourceMusicShape(sigShape);
		//--------------- here to transcribe the modulation down 
		try {
			transcriber.transcribe();
		} catch (SearchPathListExpansionException e) {
			e.printStackTrace();
		} catch (ElementMissedException e) {
			e.printStackTrace();
		}
		//--------------- here it ends 
	}

	public static void main(String[] args) {
		JMSL.clock.setAdvance(0.2);
		ScoreExample scoreExample = new ScoreExample();
		scoreExample.initJSyn();
		scoreExample.initScore();
		scoreExample.initInstrument();
		
		scoreExample.populateScore();
		
		scoreExample.initScoreFrame();
	}
}

class MyUnaryCopyBufferTransform extends UnaryCopyBufferTransform {

	MyUnaryCopyBufferTransform() {
		setName("My transform");
	}

	public void operate(CopyBuffer copyBuffer) {
		for (int i = 0; i < copyBuffer.size(); i++) {
			Note n = (Note) copyBuffer.elementAt(i);
			double currentPitch = n.getPitchData();
			double newPitch = currentPitch + 12.5;
			n.setPitchData(newPitch);
			NoteFactory.updateFromPitch(n);
		}
	}

}
