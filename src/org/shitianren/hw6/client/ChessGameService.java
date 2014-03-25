package org.shitianren.hw6.client;

import java.util.List;

import org.shitianren.hw6.client.PlayerInfo;
import org.shitianren.hw7.MatchInfo;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("chessgameservice")
public interface ChessGameService extends RemoteService {

	PlayerInfo connect();

	void sendState(String matchId, String state, int turn);

	MatchInfo createMatch(String email);

	List<MatchInfo> getMatches();

	MatchInfo getMatchState(String matchId);
	
	void deleteMatch(String matchId);
	
	void autoMatch();
	
	PlayerInfo getPlayerInfo();
	
	void invitedMatch(String opEmail);
}