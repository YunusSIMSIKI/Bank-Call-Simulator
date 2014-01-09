package Runner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.crypto.IllegalBlockSizeException;
import javax.naming.SizeLimitExceededException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Simulator.Simulator;

public class MainFrame extends JFrame {

	private LabelPanel labelPanel;
	private TextPanel textPanel;
	private JButton run;

	public MainFrame() {
		super("ACBS BANK");

		labelPanel = new LabelPanel();
		textPanel = new TextPanel();
		run = new JButton("Run");
		buildFrame();

		run.setBackground(Color.GREEN);
		run.setFont(new Font("", Font.BOLD, 20));

		setResizable(false);

		run.addActionListener(new RunListener());

	}

	private void buildFrame() {

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		setLayout(new BorderLayout());

		add(labelPanel, BorderLayout.CENTER);
		add(textPanel, BorderLayout.EAST);
		add(run, BorderLayout.SOUTH);
		pack();
		setLocationRelativeTo(null);
	}

	private class LabelPanel extends JPanel {

		private JLabel label1;
		private JLabel label2;
		private JLabel label3;
		private JLabel label4;

		public LabelPanel() {

			setLayout(new GridLayout(4, 1, 10, 30));
			setBackground(Color.LIGHT_GRAY);

			label1 = new JLabel("The number of operators in the bank:");
			label2 = new JLabel(
					"The probability distribution that governs dial-in attempts:");
			label3 = new JLabel(
					"The probability distribution that governs connect time:");
			label4 = new JLabel(
					"The length of time the simulation is to be run:");

			label1.setFont(new Font("", Font.BOLD, 15));
			label2.setFont(new Font("", Font.BOLD, 15));
			label3.setFont(new Font("", Font.BOLD, 15));
			label4.setFont(new Font("", Font.BOLD, 15));

			add(label1);
			add(label2);
			add(label3);
			add(label4);
		}

	}

	private class TextPanel extends JPanel {
		private JTextField text1;
		private JTextField text2;
		private JTextField text3;
		private JTextField text4;
		private JTextField text5;

		public TextPanel() {

			setLayout(new GridLayout(4, 1, 5, 30));
			setBackground(Color.LIGHT_GRAY);

			text1 = new JTextField(2);
			text2 = new JTextField(2);
			text3 = new JTextField(2);
			text4 = new JTextField(2);
			text5 = new JTextField(2);

			text1.setFont(new Font("", Font.BOLD, 20));
			text2.setFont(new Font("", Font.BOLD, 20));
			text3.setFont(new Font("", Font.BOLD, 20));
			text4.setFont(new Font("", Font.BOLD, 20));
			text5.setFont(new Font("", Font.BOLD, 20));

			add(text1);
			add(text2);
			JPanel p = new JPanel(new GridLayout(1, 2));
			p.add(text3);
			p.add(text4);
			add(p);
			add(text5);
		}
	}

	private class RunListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			int value1 = 0;
			double value2 = 0;
			int value3 = 0;
			int value4 = 0;
			int value5 = 0;

			try {

				value1 = Integer.parseInt(textPanel.text1.getText());
				value2 = Double.parseDouble(textPanel.text2.getText());
				value3 = Integer.parseInt(textPanel.text3.getText());
				value4 = Integer.parseInt(textPanel.text4.getText());
				value5 = Integer.parseInt(textPanel.text5.getText());

				checkExceptions(value1, value2, value3, value4, value5);

				new Simulator(value1, value2, value3, value4, value5);

			} catch (NumberFormatException ex) {

				JOptionPane
						.showMessageDialog(
								run,
								"Please check your inputs the second input must be double and the others intergers!",
								"Invalid Inputs!", JOptionPane.WARNING_MESSAGE);
			} catch (SizeLimitExceededException ex) {
				JOptionPane.showMessageDialog(textPanel.text2,
						"The probability distribution must be between (0 - 1)",
						"", JOptionPane.WARNING_MESSAGE);
			} catch (ArithmeticException ex) {
				JOptionPane
						.showMessageDialog(
								run,
								"First input for minimum time and the second for maximum",
								"Logic Error!", JOptionPane.WARNING_MESSAGE);
			} catch (IllegalArgumentException ex) {
				JOptionPane.showMessageDialog(run,
						"Inputs must be above \"0\"", "Invalid Inputs!",
						JOptionPane.WARNING_MESSAGE);
			}

		}

		public void checkExceptions(int v1, double v2, int v3, int v4, int v5)
				throws SizeLimitExceededException {

			if (v2 > 1)
				throw new SizeLimitExceededException();
			else if (v3 >= v4)
				throw new ArithmeticException();
			else if (v1 <= 0 || v2 <= 0 || v3 <= 0 || v4 <= 0 || v5 <= 0)
				throw new IllegalArgumentException();
		}
	}

}
