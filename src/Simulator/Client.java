package Simulator;

public class Client {

	private static int timeMin;
	private static int timeMax;
	private int callTime;

	public Client() {
		callTime = (int) (timeMin + Math.random() * (timeMax - timeMin + 1));
	}

	public static void setTimeMin(int timeMin) {
		Client.timeMin = timeMin;
	}

	public static void setTimeMax(int timeMax) {
		Client.timeMax = timeMax;
	}

	public int getCallTime() {
		return callTime;
	}

}
