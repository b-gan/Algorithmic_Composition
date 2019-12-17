package algomusic_berwin_class;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.unitgen.LineOut;
import com.softsynth.shared.time.TimeStamp;

public class SineExample {
	public static void main(String[] args) {
		Synthesizer synth = JSyn.createSynthesizer();
		synth.start();
		
		SineVoice voice;
		LineOut out;
		
		synth.add(out = new LineOut());
		synth.add(voice = new SineVoice());
		
		
		
		voice.getOutput().connect(0,out.input,0);
		voice.getOutput().connect(0,out.input,1);
		
		out.start(); //pull based jsyn
		
		TimeStamp currenttime = synth.createTimeStamp();
		//TimeStamp futuretime = currenttime.makeRelative(3.0);
		
		int numstep;
		numstep =  20;
		double stepsize =  1.0/numstep;	
		double partytime = 0.3;
		
		for (int i = 0; i<numstep;i++) { //loop happens instantly 
			double frequency = 440 + (i*20);
			double amplitude = 1 - (i*stepsize);
			
			//TimeStamp futuretimeon = currenttime.makeRelative((partytime*i)-partytime/2);
			//TimeStamp futuretimeoff = currenttime.makeRelative((partytime*i));
			//
			//voice.noteOff(futuretimeoff);
			
			voice.noteOn(frequency, amplitude, currenttime);
			
			//make relative 
			
		}
			
		//voice.noteOn(500, 0.5, currenttime);
		//voice.noteOff(futuretime);
	}
}
