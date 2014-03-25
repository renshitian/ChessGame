package org.shitianren.hw8;

public class RankCal {

	public enum Outcome {
		WIN(1), DRAW(0.5), LOSS(0);
		private double value;

		Outcome(double value) {
			this.value = value;
		}

		public double getValue() {
			return value;
		}
	};

	private static final int K = 15;

	public static int getNewScore(double score, double opponentScore, Outcome outcome) {

		double e = 1 / (1 + Math.pow(10, (opponentScore - score) / 400));

		return (int) Math.floor(score + (K * (outcome.getValue() - e)));
	}
}
