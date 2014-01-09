package Simulator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.Timer;

public class Operator extends JButton implements ActionListener {

	public int operatorID;
	private boolean busy;
	private static double frequency = 0;
	public int callTime[] = new int[1000];
	private Timer timer;
	public int iterator = 0;
	public int inCallTime = 0;
	public int outCallTime = 0;

	public Operator(int index) {
		super("Operator " + index);
		setPreferredSize(new Dimension(120,40));
		operatorID = index;
		timer = new Timer(1000, this);
		setBusy(false);
		setBackground(Color.GREEN);
		timer.start();
	}

	public void call() {
		inCallTime = (new Client()).getCallTime();
		callTime[iterator++] = outCallTime;
		callTime[iterator++] = inCallTime;
		outCallTime = 0;
		setBusy(true);
	}

	public void end() {
		inCallTime = 0;
		setBusy(false);
	}

	public int totalCallTime() {
		int totalCallTime = 0;
		for (int i = 1; i < iterator; i += 2) {
			totalCallTime += callTime[i];
		}
		return totalCallTime;
	}

	public boolean isBusy() {
		return busy;
	}

	public void setBusy(boolean busy) {
		this.busy = busy;
	}

	public static double getFrequency() {
		return frequency;
	}

	public static void setFrequency(double frequency) {
		Operator.frequency = frequency;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (busy)
			if (inCallTime-- == 0)
				end();

		setText("Operator " + operatorID + " (" + inCallTime + ")");

		if (busy)
			setBackground(Color.RED);
		else {
			setBackground(Color.GREEN);
			outCallTime++;
		}
	}

}
