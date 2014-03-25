package org.shitianren.hw6.client;

import java.util.List;

import org.shitianren.hw6.client.PlayerInfo;
import org.shitianren.hw7.MatchInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ChessGameServiceAsync {
	void connect(AsyncCallback<PlayerInfo> callback);

	void sendState(String matchId, String state, int turn, AsyncCallback<Void> callback);

	void createMatch(String email, AsyncCallback<MatchInfo> callback);

	void getMatches(AsyncCallback<List<MatchInfo>> callback);
	
	void getMatchState(String matchId, AsyncCallback<MatchInfo> callback);
	
	void deleteMatch(String matchId, AsyncCallback<Void> callback);
	
	void autoMatch(AsyncCallback<Void> callback);
	
	void getPlayerInfo(AsyncCallback<PlayerInfo> playerInfo);
	
	void invitedMatch(String opEmail,AsyncCallback<Void> callback);
}
