package finalproject;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JFrame;

import com.softsynth.jmsl.JMSL;
import com.softsynth.jmsl.MusicJob;

/*
 * Main application is a JFrame with a MusicJob class variable. A button in the JFrame affects the MusicJob when clicked
 * 
 */
public class BerwinFrame extends JFrame {

	MusicJob myMusicJob;
	JButton doSomethingButton;

	void buildMusicJob() {
		myMusicJob = new MusicJob() {
			public double repeat(double playTime) throws InterruptedException {
				System.out.println(getName() + ", repeat(" + playTime + ")");
				return playTime;
			}
		};
		myMusicJob.setRepeatPause(2);
		myMusicJob.setRepeats(1000);
	}

	void buidGUI() {
		doSomethingButton = new JButton("Speed up MusicJob");
		add(doSomethingButton);
		doSomethingButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				myMusicJob.setRepeatPause(myMusicJob.getRepeatPause() / 2);
			}
		});
	}

	void go() {
		myMusicJob.launch(JMSL.now());
	}

	public static void main(String[] args) {
		BerwinFrame frame = new BerwinFrame();
		frame.setTitle("click button to speed up job");
		frame.buidGUI();
		frame.buildMusicJob();
		frame.go();

		frame.pack();
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}
}