package berwin_class;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class GUI extends JFrame implements ActionListener {
	JLabel mylabel1;
	JLabel mylabel2;
	JButton mybutton;
	JButton mybutton2;
	JTextField mytextfield;
	String lastgoodvalue = "3.14";

	void build() {
		setLayout(new BorderLayout()); //different type of layout
		
		mylabel1 = new JLabel("This is my label");
		add(BorderLayout.NORTH, mylabel1);
		mylabel1.setForeground(Color.blue);
		mylabel2 = new JLabel("This is also a label");
		add(BorderLayout.SOUTH, mylabel2); // border layout in JFrame, add north to not

		mybutton = new JButton("Click");
		add(BorderLayout.CENTER, mybutton);
		mybutton.addActionListener(this); // connect button to actionevent

		mybutton2 = new JButton("Click ME!");
		add(BorderLayout.WEST, mybutton2);
		mybutton2.addActionListener(this); // connect button to actionevent

		mytextfield = new JTextField(lastgoodvalue,10); // 10 character, overwriting
		add(BorderLayout.EAST, mytextfield); //when entre
	}

	public void actionPerformed(ActionEvent e) {
		System.out.println("Touch");
		Object source = e.getSource();
		if (source == mybutton) { //refactoring 
			mybutton_func();
			
		} else if (source == mybutton2){
			mybutton2_func();
			
		}
		// TODO Auto-generated method stub

	}

	private void mybutton2_func() {
		System.out.println("No!!");
		String valuestr = mytextfield.getText();
		try{
			double actualvalue = Double.parseDouble(valuestr);
			actualvalue *= 10;
			System.out.println(valuestr +"x 10 = "+ actualvalue);
			lastgoodvalue = valuestr;
		}
		catch(java.lang.NumberFormatException e) {
			System.out.println("Youe value is not numeric "+valuestr);
			mytextfield.setText(lastgoodvalue);
		}
	}

	private void mybutton_func() {
		System.out.println("Clicked!!");
		String valuestr = mytextfield.getText();
		System.out.println("Your value is "+valuestr);
	}

	
	
	
	public static void main(String[] args) {
		GUI myexample = new GUI();
		myexample.build();
		myexample.pack();
		myexample.setVisible(true);
		myexample.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.out.println("bye");
				System.exit(0);
			}
		});

	}
}
