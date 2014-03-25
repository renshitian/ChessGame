package org.shitianren.hw7;

import java.io.Serializable;
import java.util.Date;

import org.shitianren.hw3.HistoryParser;
import org.shared.chess.Color;
import org.shared.chess.GameResultReason;
import org.shared.chess.State;

public class MatchInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String matchId;
	private Color turn;
	private GameResultReason reason;
	private Color winner;
	private Date startDate;
	private int turnNumber;
	private String stateString;
	private Color myColor;
	private Color opponentColor;
	private String opponentEmail;
	private int opponentRank;

	private transient State state;

	public String getMatchId() {
		return matchId;
	}

	public void setMatchId(String matchId) {
		this.matchId = matchId;
	}

	public Color getTurn() {
		return turn;
	}

	public void setTurn(Color turn) {
		this.turn = turn;
	}

	public GameResultReason getReason() {
		return reason;
	}

	public void setReason(GameResultReason reason) {
		this.reason = reason;
	}

	public Color getWinner() {
		return winner;
	}

	public void setWinner(Color winner) {
		this.winner = winner;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public int getTurnNumber() {
		return turnNumber;
	}

	public void setTurnNumber(int turnNumber) {
		this.turnNumber = turnNumber;
	}

	public String getStateString() {
		return stateString;
	}

	public void setStateString(String state) {
		this.stateString = state;
		this.state = HistoryParser.history2State(stateString);
	}

	public State getState() {
		if (!stateString.isEmpty() && state == null) {
			state = HistoryParser.history2State(stateString);
		}

		return state.copy();
	}

	public void setState(State state) {
		this.state = state;
		stateString = HistoryParser.state2History(state);
	}

	public Color getMyColor() {
		return myColor;
	}

	public void setMyColor(Color myColor) {
		this.myColor = myColor;
	}

	public Color getOpponentColor() {
		return opponentColor;
	}

	public void setOpponentColor(Color opponentColor) {
		this.opponentColor = opponentColor;
	}

	public String getOpponentEmail() {
		return opponentEmail;
	}

	public void setOpponentEmail(String opponentEmail) {
		this.opponentEmail = opponentEmail;
	}

	public int getOpponentRank() {
		return opponentRank;
	}

	public void setOpponentRank(int opponentRank) {
		this.opponentRank = opponentRank;
	}

	public String serialize() {
		String s = "";

		s += matchId + "###";
		s += turn + "###";
		s += reason + "###";
		s += winner + "###";
		s += startDate.getTime() + "###";
		s += turnNumber + "###";
		s += myColor + "###";
		s += opponentColor + "###";
		s += opponentEmail + "###";
		s += opponentRank + "###";
		s += stateString + "###";

		return s;
	}

	public static MatchInfo deserialize(String s) {
		MatchInfo m = new MatchInfo();

		String[] data = s.split("###");

		m.matchId = data[0];
		m.turn = data[1].equals("W") ? Color.WHITE : Color.BLACK;
		
		if (data[2].equals("null")) {
			m.reason = null;
		} else if (data[2].equals("CHECKMATE")) {
			m.reason = GameResultReason.CHECKMATE;
		} else if (data[2].equals("FIFTY_MOVE_RULE")) {
			m.reason = GameResultReason.FIFTY_MOVE_RULE;
		} else if (data[2].equals("THREEFOLD_REPETITION_RULE")) {
			m.reason = GameResultReason.THREEFOLD_REPETITION_RULE;
		} else {
			m.reason = GameResultReason.STALEMATE;
		}
		
		m.winner = data[3].equals("W") ? Color.WHITE : Color.BLACK;
		
		m.startDate = new Date(Long.parseLong(data[4]));
		
		m.turnNumber = Integer.parseInt(data[5]);
		
		m.myColor = data[6].equals("W") ? Color.WHITE : Color.BLACK;
		
		m.opponentColor = data[7].equals("W") ? Color.WHITE : Color.BLACK;
		
		m.opponentEmail = data[8];
		
		m.opponentRank = Integer.parseInt(data[9]);
		
		m.stateString = data[10];
		
		return m;
	}

	@Override
	public String toString() {
		return "MatchInfo [matchId=" + matchId + ", turn=" + turn + ", reason=" + reason
				+ ", winner=" + winner + ", startDate=" + startDate + ", turnNumber=" + turnNumber
				+ ", stateString=" + stateString + ", myColor=" + myColor + ", opponentColor="
				+ opponentColor + ", opponentEmail=" + opponentEmail + ", opponentRank="
				+ opponentRank + "]";
	}
}
