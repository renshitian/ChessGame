package org.shitianren.hw6.server;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.shitianren.hw8.RankCal;
import org.shitianren.hw8.RankCal.Outcome;
import org.shitianren.hw3.HistoryParser;
import org.shitianren.hw7.Match;
import org.shitianren.hw7.MatchInfo;
import org.shitianren.hw7.Player;
import org.shitianren.hw7.AutoMatch;
import org.shared.chess.Color;
import org.shared.chess.State;
import org.shitianren.hw6.client.ChessGameService;
import org.shitianren.hw6.client.GameInfo;
import org.shitianren.hw6.client.PlayerInfo;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.VoidWork;
import com.googlecode.objectify.Work;

public class ChessGameServiceImpl extends RemoteServiceServlet implements
		ChessGameService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Queue<String> waitList = new LinkedList<String>();

	private ChannelService channelService = ChannelServiceFactory
			.getChannelService();

	private Logger logger = Logger.getLogger(ChessGameServiceImpl.class
			.toString());

	private long matchCount = 0;
	static {
		ObjectifyService.register(Match.class);
		ObjectifyService.register(Player.class);
		ObjectifyService.register(AutoMatch.class);
		
	}

	@Override
	public synchronized PlayerInfo connect() {

		PlayerInfo info = new PlayerInfo();

		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();

		String longId = user.getEmail() + ">" + UUID.randomUUID().toString();

		String clientId = longId
				.substring(0, Math.min(63, longId.length() - 1));

		String token = channelService.createChannel(clientId);

		Player player = getOrCreatePlayer(user.getEmail());

		player.setName(user.getNickname());
		
		Set<String> channels = new HashSet<String>();
		channels.add(clientId);
		player.setChannels(channels);

		ofy().save().entity(player).now();

		info.setChannelId(token);
		info.setClientId(clientId);
		info.setUsername(user.getNickname());
		info.setEmail(user.getEmail());
		info.setRank(player.getRank());
		
		System.out.println("server connect user id: "+info.getUsername());
		System.out.println("server connect client id: "+clientId);
		System.out.println("server connect channel id: "+token);
		return info;
	}

	@Override
	public void sendState(final String matchId, final String stateString, final int turn) {
		logger.log(Level.INFO, "state received " + stateString);

		ofy().transact(new VoidWork() {

			@Override
			public void vrun() {
				// System.out.println("server send state matchID: "+matchId);
				Match match = ofy().load().type(Match.class)
						.id(Integer.valueOf(matchId)).get();

				if (match == null) {
					throw new IllegalArgumentException("Invalid match");
				}

				State newState = HistoryParser.history2State(stateString);
				State oldState = HistoryParser.history2State(match.getState());

				if (newState.equals(oldState)) {
					System.out.println("same state");
					//throw new IllegalArgumentException("Invalid state");
				}

				match.setState(stateString);
				match.setTurnNumber(turn);
				
				ofy().save().entity(match).now();
				//------------------------------------------------------------------
				if (newState.getGameResult() != null) {
					Outcome whiteOutcome = Outcome.DRAW;
					Outcome blackOutcome = Outcome.DRAW;

					if (newState.getGameResult().getWinner() != null) {
						if (newState.getGameResult().getWinner().equals(Color.WHITE)) {
							whiteOutcome = Outcome.WIN;
							blackOutcome = Outcome.LOSS;
						} else {
							whiteOutcome = Outcome.LOSS;
							blackOutcome = Outcome.WIN;
						}
					}

					int whiteScore = RankCal.getNewScore(match.getWhitePlayer().getRank(), match
							.getBlackPlayer().getRank(), whiteOutcome);
					int blackScore = RankCal.getNewScore(match.getBlackPlayer().getRank(), match
							.getWhitePlayer().getRank(), blackOutcome);

					match.getWhitePlayer().setRank(whiteScore);
					match.getBlackPlayer().setRank(blackScore);

					ofy().save().entity(match.getWhitePlayer()).now();
					ofy().save().entity(match.getBlackPlayer()).now();
				}
				//---------------------------------------------------------------------------
				Player player = getCurrentPlayer();
				
				if (player.getEmail().equals(match.getWhitePlayer().getEmail())) {
					for (String channel : match.getBlackPlayer().getChannels()) {
						System.out.println("send state to user "+match.getBlackPlayer().getName()+" channel :"+ channel);
						channelService.sendMessage(new ChannelMessage(channel, matchId + "#"
								+ stateString));
					}
				} else {
					for (String channel : match.getWhitePlayer().getChannels()) {
						System.out.println("send state to user "+match.getWhitePlayer().getName()+" channel :"+ channel);
						channelService.sendMessage(new ChannelMessage(channel, matchId + "#"
								+ stateString));
					}
				}

			}
		});

	}

	@Override
	public MatchInfo createMatch(final String email) {

		final Player currentPlayer = getCurrentPlayer();

		final Match match = new Match();

		match.setState(HistoryParser.state2History(new State()));
		match.setName(UUID.randomUUID().toString());
		Match lastMatch = ofy().load().type(Match.class).order("-id").first().get();
		final Long id = matchCount+1;
		ofy().transact(new VoidWork() {
			@Override
			public void vrun() {
				match.setId(id);

				Player white = currentPlayer;
				Player black = getOrCreatePlayer(email);

				match.setWhitePlayer(white);

				Date startDate = new Date();

				match.setStartDate(startDate);

				Key<Match> key = Key.create(match);

				white.getMatches().add(key);

				match.setBlackPlayer(black);
				black.getMatches().add(key);
				ofy().save().entity(black).now();

				ofy().save().entity(match).now();
				ofy().save().entity(white).now();
			}
		});

		// TODO if the black player has any open channels then we should be
		// sending him a message at this point

		return getMatchInfoFromMatch(match, currentPlayer);
	}

	private MatchInfo getMatchInfoFromMatch(Match match, Player currentPlayer) {
		MatchInfo info = new MatchInfo();
		info.setMatchId(String.valueOf(match.getId()));

		if (match.getWhitePlayer().getEmail().equals(currentPlayer.getEmail())) {
			info.setMyColor(Color.WHITE);
			info.setOpponentEmail(match.getBlackPlayer().getEmail());
			info.setOpponentRank(match.getBlackPlayer().getRank());
		} else {
			info.setMyColor(Color.BLACK);
			info.setOpponentEmail(match.getWhitePlayer().getEmail());
			info.setOpponentRank(match.getWhitePlayer().getRank());
		}

		info.setStartDate(match.getStartDate());

		State state = HistoryParser.history2State(match.getState());
		info.setTurn(state.getTurn());
		if (state.getGameResult() != null) {
			info.setReason(state.getGameResult().getGameResultReason());
			info.setWinner(state.getGameResult().getWinner());
		}

		info.setStateString(match.getState());
		info.setTurnNumber(match.getTurnNumber());

		return info;
	}

	private Player getOrCreatePlayer(String email) {

		Player player = ofy().load().type(Player.class).id(email).get();

		if (player == null) {
			player = new Player();
			player.setEmail(email);
			player.setRank(1500);
			player.setMatches(new TreeSet<Key<Match>>());
			player.setChannels(new HashSet<String>());
		}

		return player;
	}

	@Override
	public List<MatchInfo> getMatches() {
		final List<MatchInfo> data = new ArrayList<MatchInfo>();
		final Player currentPlayer = getCurrentPlayer();

		for (Key<Match> matchKey : currentPlayer.getMatches()) {
			Match match = ofy().cache(false).load().key(matchKey).getValue();

			if (match == null) {
				continue;
			}

			data.add(getMatchInfoFromMatch(match, currentPlayer));
		}

		return data;
	}

	@Override
	public MatchInfo getMatchState(final String matchId) {
		final Player currentPlayer = getCurrentPlayer();

		return ofy().transact(new Work<MatchInfo>() {

			@Override
			public MatchInfo run() {
				Match match = ofy().load().type(Match.class).id(Integer.valueOf(matchId)).get();

				if (match == null) {
					throw new IllegalArgumentException("Match not found");
				}

				return getMatchInfoFromMatch(match, currentPlayer);
			}
		});
	}

	@Override
	public void deleteMatch(String matchId) {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		
		Player player = ofy().load().type(Player.class).id(user.getEmail()).get();

		if (player == null) {
			throw new IllegalStateException("Invalid player");
		}

		player.getMatches().remove(Key.create(Match.class, Integer.valueOf(matchId)));

		ofy().save().entity(player).now();
		
		Match match = ofy().load().type(Match.class)
				.id(Integer.valueOf(matchId)).get();
		if(match.getBlackPlayer().equals(player)){
			match.setBD(true);
		}
		else{
			match.setWD(true);
		}
		if(match.isWD()&&match.isBD()){
			ofy().delete().entity(match);
		}
		
	}

	public void invitedMatch(String opEmail) {
		Player player = getCurrentPlayer();
		MatchInfo info = createMatch(opEmail);

		for (String channel : player.getChannels()) {
			System.out.println("send invited to user "+player.getName()+" channel: "+channel);
			channelService.sendMessage(new ChannelMessage(channel, "invited!"+info.getMatchId()));
		}
		
		Player otherPlayer = ofy().load().type(Player.class).id(opEmail).get();
		for (String channel : otherPlayer.getChannels()) {
			System.out.println("send invited to user "+otherPlayer.getName()+" channel: "+channel);
			channelService.sendMessage(new ChannelMessage(channel, "invited!"+info.getMatchId()));
		}
	}

	@Override
	public void autoMatch() {

		Player player = getCurrentPlayer();

		if (waitList.isEmpty()) {
			waitList.add(player.getEmail());
			System.out.println("server auto match first player: "+player.getEmail());
		} else {
			String opEmail = waitList.peek().trim();
			String currentEmail = player.getEmail().trim();
			System.out.println("server auto match else: opEmail: "+opEmail+" currentEmail: "+currentEmail);
			if (opEmail.equals(currentEmail)) {
				System.out.println("same user want to auto match");
				return;
			}
			waitList.poll();
			MatchInfo info = createMatch(opEmail);

			for (String channel : player.getChannels()) {
				System.out.println("send new game to user "+player.getName()+" channel: "+channel);
				channelService.sendMessage(new ChannelMessage(channel,
						"NewMatch=" + info.getMatchId()));
			}
			Player otherPlayer = ofy().load().type(Player.class).id(opEmail)
					.get();
			for (String channel : otherPlayer.getChannels()) {
				System.out.println("send new game to user "+otherPlayer.getName()+" channel: "+channel);
				channelService.sendMessage(new ChannelMessage(channel,
						"NewMatch=" + info.getMatchId()));
			}
		}
	}

	private Player getCurrentPlayer() {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		Player player = ofy().load().type(Player.class).id(user.getEmail())
				.get();

		if (player == null) {
			player = getOrCreatePlayer(user.getEmail());
			player.setName(user.getNickname());
		}
		return player;
	}

	@Override
	public PlayerInfo getPlayerInfo() {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();

		Player player = getCurrentPlayer();

		player.setName(user.getNickname());

		PlayerInfo info = new PlayerInfo();
		info.setUsername(user.getNickname());
		info.setEmail(user.getEmail());
		info.setRank(player.getRank());

		return info;
	}

}
