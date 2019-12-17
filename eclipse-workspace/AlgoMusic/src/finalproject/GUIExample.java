package algomusic.didkovsky;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.*;

import javax.swing.*;

public class GUIExample extends JFrame implements ActionListener {

	JLabel myLabel1;
	JLabel myLabel2;
	JButton myButton;
	JButton myButton2;
	JTextField myTextField;

	String lastGoodValue = "3.14159";

	void build() {
		setLayout(new BorderLayout());
		myLabel1 = new JLabel("This is my label");
		myLabel1.setForeground(Color.green);
		add(BorderLayout.NORTH, myLabel1);
		myLabel2 = new JLabel("This is also a label");
		add(BorderLayout.SOUTH, myLabel2);

		myButton = new JButton("Click Me");
		add(BorderLayout.CENTER, myButton);
		myButton.addActionListener(this);

		myButton2 = new JButton("No Click Me");
		add(BorderLayout.WEST, myButton2);
		myButton2.addActionListener(this);

		myTextField = new JTextField(lastGoodValue, 10);
		add(BorderLayout.EAST, myTextField);
	}

	public void actionPerformed(ActionEvent e) {
		System.out.println("CLICK!!!");
		Object source = e.getSource();
		if (source == myButton) {
			handleButton1();
		} else if (source == myButton2) {
			handleButton2();
		}
	}

	private void handleButton2() {
		System.out.println("Do something else");
		String valueStr = myTextField.getText();
		try {
			double value = Double.parseDouble(valueStr);
			System.out.println("Your numerical value is " + value);
			double calculatedValue = value * 10;
			System.out.println(value + " x 10 = " + calculatedValue);
			lastGoodValue = valueStr;
		} catch (java.lang.NumberFormatException e) {
			System.out.println("Your value is NOT numeric " + valueStr);
			myTextField.setText(lastGoodValue);
		}
	}

	private void handleButton1() {
		System.out.println("Do one thing");
		String valueStr = myTextField.getText();
		System.out.println("Your value is " + valueStr);
	}

	public static void main(String[] args) {
		GUIExample myExample = new GUIExample();
		myExample.build();
		myExample.pack();
		myExample.setVisible(true);
		myExample.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}

		});
	}

}
