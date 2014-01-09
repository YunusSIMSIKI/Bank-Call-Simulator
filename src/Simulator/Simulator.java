package Simulator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;

public class Simulator extends JFrame {

	private Operator operator[];
	private int numOfOper;
	private double probAttempts;
	private int timeMin;
	private int timeMax;
	private int runTime;
	private int missedCall;
	private final int initialRunTime;
	private JLabel label;
	private JScrollPane s;

	public Simulator(int val1, double val2, int val3, int val4, int val5) {

		super("Simulator");

		numOfOper = val1;
		probAttempts = val2;
		timeMin = val3;
		timeMax = val4;
		runTime = val5;
		initialRunTime = runTime;

		Operator.setFrequency(probAttempts);
		Client.setTimeMax(timeMax);
		Client.setTimeMin(timeMin);
		operator = new Operator[numOfOper];

		missedCall = 0;

		buildSimulator();

	}

	private void buildSimulator() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
		setLayout(new BorderLayout());

		label = new JLabel("Missed Calls: ");

		JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
		p.add(label);

		s = new JScrollPane(new OperatorPanel());

		s.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		s.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		add(p, BorderLayout.SOUTH);
		add(new TopPanel(), BorderLayout.NORTH);
		add(s, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(null);
	}

	private class OperatorPanel extends JPanel implements ActionListener {

		private Operator busy[];
		private Operator free[];
		private int iteratorB;
		private int iteratorF;
		private Timer timer;

		public OperatorPanel() {
			setLayout(new GridLayout(operator.length / 10 + 1, 10, 5, 5));

			busy = new Operator[operator.length];
			free = new Operator[operator.length];

			iteratorB = 0;
			iteratorF = 0;
			timer = new Timer(1000, this);

			for (int i = 0; i < operator.length; i++) {
				operator[i] = new Operator(i + 1);
				add(operator[i]);
			}

			timer.start();
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			for (int i = 0; i < iteratorB; i++)
				remove(busy[i]);

			for (int i = 0; i < iteratorF; i++)
				remove(free[i]);

			for (int i = 0; i < operator.length; i++)
				busy[i] = null;

			for (int i = 0; i < operator.length; i++)
				free[i] = null;

			iteratorB = 0;
			iteratorF = 0;

			for (int i = 0; i < operator.length; i++)
				if (operator[i].isBusy())
					busy[iteratorB++] = operator[i];
				else
					free[iteratorF++] = operator[i];

			try {
				if ((int) (1 + Math.random() / Operator.getFrequency()) == 1) {

					int minCalled = free[0].totalCallTime();
					int index = 0;

					for (int i = 0; i < iteratorF; i++) {
						if (minCalled > free[i].totalCallTime()) {
							minCalled = free[i].totalCallTime();
							index = i;
						}
					}
					free[index].call();
				}
			} catch (NullPointerException ex) {
				label.setText("Missed Calls: " + (++missedCall));
			}

			for (int i = 0; i < iteratorB; i++)
				add(busy[i]);

			for (int i = 0; i < iteratorF; i++)
				add(free[i]);

			if (runTime == 0) {
				timer.stop();

				new Efficiency();
				try {
					new ReadAndWrite();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}

		}
	}

	private class TopPanel extends JPanel implements ActionListener {
		private JLabel label;
		private Timer timer;

		public TopPanel() {
			setLayout(new FlowLayout(FlowLayout.CENTER));
			label = new JLabel(String.valueOf(runTime));
			timer = new Timer(1000, this);

			add(label);
			timer.start();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (runTime == 0) {
				timer.stop();
				dispose();

			}
			label.setText("Time is remaining: " + (--runTime));
		}
	}

	private class ReadAndWrite {

		private File file;
		private Scanner in;
		private PrintWriter out;
		private String text[][];

		public ReadAndWrite() throws FileNotFoundException {
			file = new File("file.txt");
			text = new String[1000][7];

			read();
			write();
		}

		public void read() throws FileNotFoundException {
			in = new Scanner(file);

			for (int i = 0; in.hasNext(); i++)
				for (int j = 0; j < text[i].length; j++)
					text[i][j] = in.nextLine();
			in.close();
		}

		public void write() throws FileNotFoundException {
			out = new PrintWriter(file);

			for (int i = 0; text[i][0] != null; i++)
				for (int j = 0; j < text[i].length; j++)
					out.println(text[i][j]);

			out.println("The number of operators in the bank : " + numOfOper);
			out.println("The probability distribution that governs dial-in attempts : "
					+ probAttempts);
			out.println("The probability distribution that governs connect time : "
					+ timeMin + " - " + timeMax);
			out.println("The length of time the simulation is to be run : "
					+ initialRunTime);
			out.println("Missed Calls : " + missedCall);
			out.println("Efficiency for missed call : " + effMiss());
			out.println("Efficiency for free time : " + effFree());

			out.close();

			JOptionPane.showMessageDialog(null, "Missed Calls : " + missedCall);
			JOptionPane.showMessageDialog(null, "Efficiency for missed call : "
					+ effMiss());
			if (effMiss() < 50) {
				JOptionPane.showMessageDialog(null,
						"This bank needs more operators");
			}
			JOptionPane.showMessageDialog(null, "Efficiency for free time : "
					+ effFree());
			if (effFree() < 60)
				JOptionPane
						.showMessageDialog(
								null,
								"In this bank, operators have too much free time. This bank needs to decrease the amount of operators ");

		}

		public double effMiss() {

			double rate = (1 - (double) missedCall / (double) initialRunTime) * 100;

			return rate;
		}

		public double effFree() {

			double rate;
			int sum = 0;

			for (int i = 0; i < operator.length; i++)
				for (int j = 0; j < operator[i].callTime.length; j += 2) {
					sum += operator[i].callTime[j];
				}
			
			rate = (1 - ((double) sum / (double) operator.length)
					/ (double) initialRunTime) * 100;

			return rate;
		}

	}

	public class Efficiency extends JFrame {

		Grafics grafics = new Grafics();
		JScrollPane scroll = new JScrollPane(grafics);

		public Efficiency() {
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setVisible(true);

			setLayout(new BorderLayout());

			scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

			add(scroll, BorderLayout.CENTER);

			pack();
			setLocationRelativeTo(null);
		}

		public class Grafics extends JPanel {

			public Grafics() {
				setPreferredSize(new Dimension(initialRunTime * 5 + 150,
						operator.length * 20 + 30));
			}

			protected void paintComponent(Graphics g) {
				super.paintComponent(g);

				g.setFont(new Font("", Font.BOLD, 10));

				int X = 70, Y = 10;
				g.drawLine(X, Y, X, Y - 5);
				for (int i = 0; i < initialRunTime / 10 + 2; i++) {
					for (int j = 0; j < 10; j++) {
						g.drawLine(X + 5, Y, X + 5, Y - 2);
						X += 5;
					}
					g.drawLine(X, Y, X, Y - 5);
				}

				int x = 70, y = 20;

				for (int i = 0; i < operator.length; i++) {

					x = 70;

					g.drawString("Operator" + (i + 1), 5, y + 10);

					for (int j = 0; j < operator[i].iterator; j += 2) {

						g.setColor(Color.GREEN);

						g.fillRect(x, y, operator[i].callTime[j] * 5, 10);
						x += operator[i].callTime[j] * 5;

						g.setColor(Color.red);
						g.fillRect(x, y, operator[i].callTime[j + 1] * 5, 10);
						x += operator[i].callTime[j + 1] * 5;

					}
					y += 20;
					g.setColor(Color.black);
				}

			}
		}
	}
}
