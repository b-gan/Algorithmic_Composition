package finalproject;

import java.awt.BorderLayout;
import java.security.SecureRandom; 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.*; 
import java.util.function.Supplier;

import javax.swing.JButton;
import javax.swing.JFrame;

import com.softsynth.jmsl.*;
import com.softsynth.jmsl.jsyn2.JSynMusicDevice;
import com.softsynth.jmsl.jsyn2.JSynUnitVoiceInstrument;
import com.softsynth.jmsl.jsyn2.unitvoices.FilteredSawtoothBL;
import com.softsynth.jmsl.util.MIDIFileToMusicShape;
import com.softsynth.jmsl.view.MusicShapeEditor;

public class MultiTrackSort extends JFrame{
	List<MusicShape> musicshapelist = new ArrayList<>();
	List<Double> fitnessScore = new ArrayList<>();
	MusicShape hbd;
	MusicShape hbd_copy;
	MusicShape s; //or any modifying stuff here 
	PlayLurker indexListener;
	int internal_index;
	SecureRandom randomDouble = new SecureRandom();
	public static int left = 0;
	public static int right;
	public static int current = 0;

	void tempo(double tempo) { //need to check again in future
		double given_tempo = 120.0; 
		double ratio = given_tempo/tempo; //flip, nice, done.
		for(int i =0;i<hbd.size();i++) { //duration pitch, amplitude hold
			double new_duration = (hbd.get(i, 0))*ratio;
			double new_hold =(hbd.get(i, 3))*ratio;
			hbd.set(new_duration, i,0);
			hbd.set(new_hold, i,3);
		}
	}
	void readMidi() throws IOException { //done.
		MIDIFileToMusicShape fileToMusicShape = new MIDIFileToMusicShape();
		//fileToMusicShape.convertMIDIFile("/Users/berwingan/Downloads/pirate.mid"); //540 
		fileToMusicShape.convertMIDIFile("/Users/berwingan/Downloads/bjsbmm01.mid");	//140
		//fileToMusicShape.convertMIDIFile("/Users/berwingan/Downloads/bumble_bee.mid"); //140
		ParallelCollection par = fileToMusicShape.getParallelCollection();
		System.out.println("Par col has " + par.size() + " children");
		for (int i = 0; i < par.size(); i++) { //duration pitch, amplitude hold
			MusicShape s = (MusicShape) par.get(i);
			double totalDur = s.get(s.size() - 1, 0);
			s.differentiate(totalDur, 0);
			s.scale(1.0 / 127.0, 0, s.size() - 1, 2); //pitches 
			s.print();
			if (i == 1) {
				hbd = s;
			}
		}
		hbd.useStandardDimensionNameSpace();
		hbd.remove(hbd.size()-1); //remove last
	}
	
    public void buildIndexListener() {
        indexListener = new PlayLurker() {
            public void notifyPlayLurker(double playTime, MusicJob musicShape, int index) {
                MusicShape ms = (MusicShape) musicShape;
                System.out.println("Index " + index + " being played in musicShape " + musicShape.getName() + " at playTime="
                        + playTime + ", pitch=" + ms.get(index, 1));
                internal_index =index;  
            }
        };
        hbd.addPlayLurker(indexListener);
    }
    public MusicShape RandomShuffle(MusicShape hbd) { //one of many 
    	MusicShape s = new MusicShape(hbd.dimension()); //future proofing yo
    	s.useStandardDimensionNameSpace();
		List<Integer> ret = new ArrayList<>();
    	for(int i = 0;i<=hbd.size()-1;i++) {
    		ret.add(i);
    	}
    	Collections.shuffle(ret);
    	double []data = new double[hbd.dimension()];
    	for(int i =0;i<=ret.size()-1;i++) {
    		data = hbd.get(ret.get(i));
    		s.add(data);
    	}
    	s.print();
    	//System.exit(0);
    	return s;
    }
    
	void initMusicShapeEditor() {
		MusicShapeEditor se = new MusicShapeEditor();
		se.addMusicShape(hbd);
		add(se.getComponent());
	}
	
	MusicShape deepcopy(MusicShape a) {
		MusicShape b =new MusicShape(a.dimension());
		b.useStandardDimensionNameSpace();
		double []data = new double[a.dimension()];
		for(int i =0; i<a.size();i++){
			data  = a.get(i);
			b.add(data); //problem here unable to SET IS NOT ADD OMG 
			
		}
		return b;
	}
	double fitness(MusicShape check, MusicShape finish, double minimumPercent,double[]max ,double []min) { //still assuming same length
		double score = 0.0;
		double total = finish.dimension()*finish.size();
		double []data2 = new double[finish.dimension()];
		double []data1 = new double[check.dimension()]; 
		for (int i=0;i<finish.size();i++) {
			data1 = check.get(i);
			data2 = finish.get(i);
			for(int j=0;j<finish.dimension();j++) {
				double range1 = max[j]-min[j];
				double toAdd = range1* new SecureRandom().nextDouble();
				double range = range1* minimumPercent;		
				if((Math.abs(data1[j]-data2[j]))<range) { //within the range
					score +=1.0;
				}
			}
		}
		System.out.println("Score is "+ score/total);
		return score/total;
	}
	
	MusicShape improve(MusicShape check,MusicShape finish, double minimumPercent, double[]max ,double []min) { //change check inside
		MusicShape lip = new MusicShape(finish.dimension());
		for (int i=0;i<finish.size();i++) {
			double []data2 = new double[finish.dimension()];
			double []data1 = new double[check.dimension()]; 
			data1 = check.get(i);
			data2 = finish.get(i);

			for(int j=0;j<finish.dimension();j++) {
				//double max = finish.getMax(j); broken <-- here 
				//double min = finish.getMin(j); //duration , pitch, hold , amp		
				//get max and min of each dim and JMSLRandom within them if the difference the values is greater than 10%? <--arbituary
				double range1 = max[j]-min[j];
				double toAdd = range1* randomDouble.nextDouble();
				double range = range1* minimumPercent;		
				if((Math.abs(data1[j]-data2[j]))>range) {
					//data1[j] = JMSLRandom.choose(min, max); //interesting thing, the random is always the same, same first number 
					System.out.println("orgi is " + data1[j]);
					data1[j] = (min[j] + toAdd); //need true random to progress
					System.out.println("new is " + data1[j]);

				}
			}
			lip.add(data1);
		}
//		double a = fitness(lip,finish,minimumPercent,max,min);
//		double b = fitness(check,finish,minimumPercent,max,min);
//		System.exit(0);
		return deepcopy(lip);
	}

	MusicShape improve_1(MusicShape check,MusicShape finish, double minimumPercent, double[]max ,double []min) { //change check inside
		MusicShape lip = new MusicShape(finish.dimension());
		List<Integer> shuffleHere = new ArrayList<>();
		for (int i=0;i<finish.size();i++) {
			double []data2 = new double[finish.dimension()];
			double []data1 = new double[check.dimension()]; 
			data1 = check.get(i);
			data2 = finish.get(i);
			int chicken = 0;
			for(int j=0;j<finish.dimension();j++) {
				if(data1[j]!=data2[j]) {
					chicken+=1;
				}
			}
			if(chicken>2) { //add to list of index that is not correct
				shuffleHere.add(i);
			}
		}
		Collections.shuffle(shuffleHere); //real random
		for(int i=0;i<finish.size();i++) {
			double []data3 = new double[check.dimension()];
			if(shuffleHere.contains(i)==true) { //list.size, i
				int indexInner = shuffleHere.indexOf(i);
				int toGet = (shuffleHere.size()-1)-indexInner; //gets flipped
				data3 =check.get(toGet);
			}
			else {
				data3 = check.get(i);
			}
			lip.add(data3);
		}
		return deepcopy(lip);
	}

	void set_up(double lastPercentage, double minimumPercent) {
		double []max = new double[hbd.dimension()];
		double []min = new double[hbd.dimension()];
		for(int i =0;i<max.length;i++) {
			max[i] = 0.0;
			min[i] = 500.0;
		}
		
		double []data = new double[hbd.dimension()];
		for(int i =0;i<hbd.size();i++) {
			data = hbd.get(i);
			for(int j=0;j<hbd.dimension();j++) {
				if(data[j]>max[j]) {
					max[j] = data[j];
				}
				if(data[j]<min[j]) {
					min[j] = data[j];
				}
			}
		}

		//---------------------------- modification to musical shapes here	
		hbd_copy = deepcopy(hbd); //copy of original //JAVA WORKs on reference, deep copy is needed
 		s = RandomShuffle(hbd); //s -> ... -> ... ->hbd_copy
		hbd=deepcopy(s); //one you play with //copy of shuffle //LEARNING ANY MODIFICATION WITH THE MUSIC SHAPE MUST HAPPEN BEFORE putting ins in	
		//put first in list musicshapelist,fitnessScore
		musicshapelist.add(s); fitnessScore.add(fitness(s,hbd_copy,minimumPercent,max,min));
		//while loop
		MusicShape inner = new MusicShape(s.dimension());
		inner = deepcopy(s);
		//---------- magic here 
		//elementalRandom(inner,minimumPercent,lastPercentage,max,min);
		double a = fitness(inner,hbd_copy,minimumPercent,max,min);
		double b;
		while(a<lastPercentage) {
			b =fitness(inner,hbd_copy,minimumPercent,max,min);
			inner = improve_1(inner,hbd_copy,minimumPercent,max,min);
			a = fitness(inner,hbd_copy,minimumPercent,max,min);
			if(b==a) {
				break;
			}
			musicshapelist.add(deepcopy(inner));
			fitnessScore.add(a);
		}
		
		//put last in 
		musicshapelist.add(hbd_copy); fitnessScore.add(fitness(hbd_copy,hbd_copy,minimumPercent,max,min));
	}
		
	void play() {
	
		JMSL.clock.setAdvance(.2); //how to auto set the time required

		JSynMusicDevice dev = JSynMusicDevice.instance();
		dev.open();

		JMSLMixerContainer mixer = new JMSLMixerContainer();
		mixer.start();

		JSynUnitVoiceInstrument ins = new JSynUnitVoiceInstrument(8, FilteredSawtoothBL.class.getName());
		mixer.addInstrument(ins);
		//hbd.setTimeStretch(0.1); //percentage to finish while loop, percentage for fitness to say ok
		set_up(0.95,0.2); //needs to be 95 almost to clear the loop //interesting 0.75 it can't go better than 0.75 for a statics one
		hbd.setInstrument(ins);
		hbd.setRepeats(100);
		//hbd.remove(hbd.size() - 1); // CRASHES ART RUNTIME ---> placed in read MIDI
		buildIndexListener(); //use internal index to keep track
		hbd.print();
		right = fitnessScore.size();
		hbd.launch(JMSL.now());
	}
	
	JButton towardsCorrect;
	JButton towardsWrong;
	JButton Original;
	JButton Mess;
	void buidGUI() { //also print how many there are and what number it is on currently

		
		setLayout(new BorderLayout());
		
		towardsCorrect = new JButton("Towards Correct");
		add(BorderLayout.NORTH, towardsCorrect);
		towardsCorrect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { //do things here when clicked 			
	              //here to move
				if(current!=right) {
					current+= 1;
                	double []data = new double[hbd.dimension()];
                	for(int i=internal_index+10;i<s.size()-1;i++) { //need to give a little space, +2 - + 5, otherwise, will here an audible pause
                		data = musicshapelist.get(current).get(i);
                		hbd.set(data, i);
                	}
				}
                 //here pause here 	
			}
		});
		
		towardsWrong = new JButton("Towards Wrong");
		add(BorderLayout.SOUTH, towardsWrong);
		towardsWrong.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent f) { //do things here when clicked 			
	              //here to move
				if(current!=left) {
					current-=1;
                	double []data1 = new double[hbd.dimension()]; //currently all size is same
                	for(int i=internal_index+10;i<s.size()-1;i++) { //need to give a little space, +2 - + 5, otherwise, will here an audible pause
                		data1 = musicshapelist.get(current).get(i);
                		hbd.set(data1, i);
                	}
				}
                 //here pause here 	
			}
		});
		
		Original = new JButton("Original");
		add(BorderLayout.EAST, Original);
		Original.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent f) { //do things here when clicked 			
	              //here to move
	
					current=musicshapelist.size()-1;
                	double []data1 = new double[hbd.dimension()]; //currently all size is same
                	for(int i=internal_index+10;i<s.size()-1;i++) { //need to give a little space, +2 - + 5, otherwise, will here an audible pause
                		data1 = musicshapelist.get(current).get(i);
                		hbd.set(data1, i);
                	}
				
                 //here pause here 	
			}
		});
		Mess = new JButton("Mess");
		add(BorderLayout.WEST, Mess);
		Mess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent f) { //do things here when clicked 			
	              //here to move
			
					current=0;
                	double []data1 = new double[hbd.dimension()]; //currently all size is same
                	for(int i=internal_index+10;i<s.size()-1;i++) { //need to give a little space, +2 - + 5, otherwise, will here an audible pause
                		data1 = musicshapelist.get(current).get(i);
                		hbd.set(data1, i);
                	}
				
                 //here pause here 	
			}
		});
	}
	


	public static void main(String[] args) {
		MultiTrackSort midiFileDemo = new MultiTrackSort();
		midiFileDemo.setTitle("Fun Times");
		
		try {
			midiFileDemo.readMidi();

		} catch (IOException e) {
			e.printStackTrace();
		}
		midiFileDemo.tempo(140.0);
		//---
//		midiFileDemo.initMusicShapeEditor();
//		midiFileDemo.pack();
//		midiFileDemo.setVisible(true);
		//--- here musicshapeeditor
		midiFileDemo.play();
		midiFileDemo.buidGUI();
		midiFileDemo.pack();
		midiFileDemo.setVisible(true);

	}

}


