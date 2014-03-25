package org.shitianren.hw6.client;
import java.io.Serializable;
import java.util.Date;

import org.shared.chess.Color;
import org.shared.chess.GameResult;
import org.shared.chess.GameResultReason;

public class GameInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	private String whiteEmail;
	private String blackEmail;
	private String state;
	private String matchId;
	private int whiteRank;
	private int blackRank;
	private Date startDate;

	public String getWhiteEmail() {
		return whiteEmail;
	}

	public void setWhiteEmail(String whiteEmail) {
		this.whiteEmail = whiteEmail;
	}

	public String getBlackEmail() {
		return blackEmail;
	}

	public void setBlackEmail(String blackEmail) {
		this.blackEmail = blackEmail;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "GameInfo [whiteEmail=" + whiteEmail + ", blackEmail=" + blackEmail + ", state="
				+ state + "]";
	}

	public String getMatchId() {
		return matchId;
	}

	public void setMatchId(String matchId) {
		this.matchId = matchId;
	}

	public int getWhiteRank() {
		return whiteRank;
	}

	public void setWhiteRank(int whiteRank) {
		this.whiteRank = whiteRank;
	}

	public int getBlackRank() {
		return blackRank;
	}

	public void setBlackRank(int blackRank) {
		this.blackRank = blackRank;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

}