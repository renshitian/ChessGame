package org.shitianren.hw3;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.shitianren.hw10.NullMatchInfo;
import org.shitianren.hw10.StorageHelper;
import org.shitianren.hw10.Callback;
import org.shitianren.hw9.AlphaBetaPruning;
import org.shitianren.hw9.ChessHeuristic;
import org.shitianren.hw9.Timer;
import org.shared.chess.Color;
import org.shared.chess.GameResult;
import org.shared.chess.Move;
import org.shared.chess.Piece;
import org.shared.chess.PieceKind;
import org.shared.chess.Position;
import org.shared.chess.State;
import org.shitianren.hw2.StateChangerImpl;
import org.shitianren.hw2_5.StateExplorerImpl;
import org.shitianren.hw6.client.ChessGameService;
import org.shitianren.hw6.client.ChessGameServiceAsync;
import org.shitianren.hw6.client.PlayerInfo;
import org.shitianren.hw7.MatchInfo;

import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragHandlerAdapter;
import com.google.gwt.appengine.channel.client.ChannelError;
import com.google.gwt.appengine.channel.client.Socket;
import com.google.gwt.appengine.channel.client.SocketListener;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.AudioElement;
import com.google.gwt.dom.client.MediaElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.media.client.Audio;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.TextBox;

public class Presenter {

	public interface View {
		/**
		 * Renders the piece at this position. If piece is null then the
		 * position is empty.
		 */
		void setPiece(int row, int col, Piece piece);

		/**
		 * Turns the highlighting on or off at this cell. Cells that can be
		 * clicked should be highlighted.
		 */
		void setHighlighted(int row, int col, boolean highlighted);

		/**
		 * Indicate whose turn it is.
		 */
		void setWhoseTurn(Color color);

		/**
		 * Indicate whether the game is in progress or over.
		 */
		void setGameResult(GameResult gameResult);

		/**
		 * get the image object for [row][col] to set event handler
		 */
		HasClickHandlers getImageClickHandler(int row, int col);

		/**
		 * clear all the highlighted in the board
		 */
		void clearHighlighted();

		/**
		 * set the gamestatus
		 */
		void setGameStatus(String status);

		/**
		 * get image1 from the promotionGrid
		 */
		HasClickHandlers getPromotionImage(int row, int col);

		/**
		 * set the promotion image
		 * 
		 * @param color
		 */
		void setPromotion(Color color);

		/**
		 * remove the promotion image
		 */
		void removePromotion();

		/**
		 * create restart button when the game is over
		 */
		void setRestartButton();

		/**
		 * get the restartButton
		 * 
		 * @return
		 */
		HasClickHandlers getRestartButton();

		/**
		 * remove the hightligted of promotion image when the user click on one
		 * piece to promote
		 */
		void removePromotionHighlishted();

		void setCaptureAudio(Audio audio);

		void setGameEndAudio(Audio audio);

		void setClickAudio(Audio audio);


		Image getImage(int row, int col);

		void removeRestartButton();

		HasClickHandlers getLoadButton();

		HasClickHandlers getSaveButton();

		ListBox getStorageList();

		TextBox getNameText();

		void setSaveStatus(String str);

		void setLoadStatus(String str);


		void showConnectPanel();

		void hideConnectPanel();

		void showWaitStatus();

		void hideWaitStatus();

		void showGameDisplayPanel();

		void hideGameDisplayPanel();

		void hideInvitePlayer();

		void showInvitePlayer();

		void hideAutoMatchPanel();

		void showAutoMatchPanel();

		TextBox getInvitePlayerText();

		HasClickHandlers getInviteButton();

		HasClickHandlers getAutoMatchButton();

		ListBox getMatchList();

		void setMatchList(Map<String, String> matches, String matchId);

		String getSelectedMatch();

		void setWaitStatus(String str);

		void openChannel(String token, SocketListener listener);

		void setGameDisStatus(String str);

		HasClickHandlers getDeleteGameButton();

		HasClickHandlers getAIButton();

		void makeDraggableFromTo(int fromRow, int fromCol, int toRow,
				int toCol, Callback callback);

		void clearDragDrop();

		void addDragHandler(int row, int col, DragHandler dh);

		SourcesTableEvents getGrid();

		Image[][] getBoard();

		void animateMove(Move move, Callback callback);
	}

	private View view;
	private State state;
	private StateChangerImpl stateChanger = new StateChangerImpl();
	private StateExplorerImpl stateExplorer = new StateExplorerImpl();
	private Position fromPosition = null;
	private Set<Move> currentPossible2Position = null;
	private boolean choosePromotion = false;
	private Audio captureAudio;
	private Audio gameEndAudio;
	private Audio clickAudio
	private boolean doAnimation = true;
	public Color myColor;
	private String myEmail;
	private String opponentEmail;
	public ChessGameServiceAsync service = GWT.create(ChessGameService.class);
	private Socket socket;
	private Logger logger = Logger.getLogger(Presenter.class.toString());
	private boolean doInvite = false;
	
	private List<MatchInfo> matches = new ArrayList<MatchInfo>();
	private StorageHelper storage = new StorageHelper();
	private Map<String, String> pendingMoveMap = new LinkedHashMap<String, String>();
	private MatchInfo matchInfo = NullMatchInfo.INSTANCE;
	
	public Presenter() {
		myEmail = storage.getFromStorage("email");
	}
	void setMatchInfo(MatchInfo info) {
		this.matchInfo = info;
		setState(state);
	}
	public void setEmail(String email) {
		myEmail = email;
	}
	public void initGamePanel() {

		loadMatchList();
		initChessGame();
		view.getInviteButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				initializeInviteGame(view.getInvitePlayerText().getText());
			}
		});
		view.getAutoMatchButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				initializeAutoMatch();
			}

		});
		view.getMatchList().addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				logger.info(view.getSelectedMatch());

				if (view.getSelectedMatch().isEmpty()) {
					setState(null);
				} else {
					loadMatch(view.getSelectedMatch());
				}
			}
		});

		view.getDeleteGameButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				deleteMatch(view.getSelectedMatch());
			}

		});

		view.getAIButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				AIMode();
			}

		});

		service.connect(new AsyncCallback<PlayerInfo>() {

			@Override
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, "error", caught);

			}

			@Override
			public void onSuccess(PlayerInfo result) {
				Window.alert("Connection Success");
				view.setWaitStatus("Welcome" + " " + result.getUsername() + "["
						+ result.getRank() + "]" + " <" + result.getEmail()
						+ ">");
				myEmail = result.getEmail();
				
				storage.saveInStorage("email", myEmail);
				
				view.openChannel(result.getChannelId(), new SocketListener() {

					@Override
					public void onOpen() {
						logger.log(Level.INFO, "open");
					}

					@Override
					public void onMessage(String message) {
						logger.log(Level.INFO, message);

						if (message.contains("NewMatch")) {
							logger.info("new game");

							String matchId = message.substring(9, message.length() - 1);

							loadMatchList();

							loadMatch(matchId);
						} else if (message.contains("invited")) {

							if (doInvite == false) {
								String matchId = message.substring(8,
										message.length() - 1);

								
								loadMatchList();
								//loadMatch(matchId);
							} else {
								doInvite = false;

								String matchId = message.substring(8,
										message.length() - 1);
								loadMatchList();
								loadMatch(matchId);
							}
						} else {

							String[] parts = message.trim().split("#");

							String id = parts[0];
							String stateString = parts[1];
							if (id.equals(matchInfo.getMatchId())) {
								state = HistoryParser
										.history2State(stateString);
								setState(state);
								showPossibleStartPosition();
								if (state.getGameResult() != null) {
									updateRank();
								}
							}
						}
					}

					@Override
					public void onError(ChannelError error) {
						logger.log(Level.INFO, error.toString());
					}

					@Override
					public void onClose() {
						logger.log(Level.INFO, "close");

					}
				});
				logger.log(Level.INFO, result.toString());
			}
		});

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				final int x = i;
				final int y = j;

				view.addDragHandler(i, j, new DragHandlerAdapter() {
					@Override
					public void onDragEnd(DragEndEvent event) {
						//view.clearDragDrop();
						view.clearHighlighted();
						fromPosition = null;
					}

					@Override
					public void onDragStart(DragStartEvent event) {
						dragStart(x, y);
					}
				});
			}
		}

		com.google.gwt.user.client.Timer t = new com.google.gwt.user.client.Timer() {
			public void run() {
				if (!pendingMoveMap.isEmpty()) {
					Map.Entry<String, String> entry = pendingMoveMap.entrySet().iterator().next();

					updateServerState(entry.getKey(), entry.getValue(), false);
				}
			}


		};

		t.scheduleRepeating(10000);

		pendingMoveMap = loadPendingMovesFromStorage();
	}
	private void updateServerState(final String moveId, String token, final boolean showAlert) {
		logger.log(Level.INFO, "sending sent " + moveId + " " + token);
		String[] data = moveId.split("_");
		String matchId = data[0];
		int turn = Integer.parseInt(data[1]);
		service.sendState(matchId, token, turn, new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				if (showAlert) {
					Window.alert("You are not online. Move will be submitted to server when you reconnect");
				}
			}

			@Override
			public void onSuccess(Void result) {
				loadMatchList();
				pendingMoveMap.remove(moveId);
				savePendingMovesToStorage();
			}
		});
	}
	private void updateRank() {
		service.getPlayerInfo(new AsyncCallback<PlayerInfo>() {

			@Override
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, "error", caught);

			}

			@Override
			public void onSuccess(PlayerInfo result) {
				view.setWaitStatus("Welcome" + " " + result.getUsername() + "["
						+ result.getRank() + "]" + " <" + result.getEmail()
						+ ">");
			}
		});
	}

	private void deleteMatch(String selectedMatch) {
		service.deleteMatch(selectedMatch, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(Void result) {
				loadMatchList();
				setState(null);
			}

		});
	}

	public void initializeInviteGame(String playerEmail) {
		Window.alert("invite " + playerEmail);
		final String email = view.getInvitePlayerText().getText();
		if (myEmail.equals(email.trim())) {
			return;
		}
		doInvite = true;
		invitedMatch(email);

	}

	public void initializeAutoMatch() {

		service.autoMatch(new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, "error", caught);
				Window.alert("auto match failure");
			}

			@Override
			public void onSuccess(Void result) {
				logger.info("auto matching");
			}
		});
	}

	public void initChessGame() {
		/*
		 * initialize chess game
		 */

		captureAudio = createCaptureAudio();
		view.setCaptureAudio(captureAudio);
		gameEndAudio = createGameEndAudio();
		view.setGameEndAudio(gameEndAudio);
		clickAudio = createClickAudio();
		view.setClickAudio(clickAudio);
		doAnimation = true;

		addRestartButtonListener();
		view.setRestartButton();
	}

	private void updateStateInServer() {

		service.sendState(matchInfo.getMatchId(), HistoryParser.state2History(state),
				matchInfo.getTurnNumber(), new AsyncCallback<Void>() {
					@Override
					public void onFailure(Throwable caught) {
						logger.log(Level.SEVERE, "error", caught);
					}

					@Override
					public void onSuccess(Void result) {
						logger.log(Level.INFO, "state sent");
						loadMatchList();

					}
				});
	}

	private void sendState() {
		
		
		if (!state.getTurn().equals(myColor) && opponentEmail.equals("AI")) {

			AlphaBetaPruning abp = new AlphaBetaPruning(new ChessHeuristic(),
					stateChanger);
			Move move = abp.findBestMove(state, 10, new Timer(5000));

			logger.info("AI move " + move.toString());
			doAnimation = true;
			doMove(move);
			return;
		}
		
		final State state = matchInfo.getState();
		String token = matchInfo.getStateString();
		logger.info("sending state " + token);

		final String moveId = matchInfo.getMatchId() + "_" + matchInfo.getTurnNumber();

		pendingMoveMap.put(moveId, token);
		savePendingMovesToStorage();

		logger.info(pendingMoveMap.toString());

		updateServerState(moveId, token, true);

	}

	private void doMove(Move move) {
		
		stateChanger.makeMove(state, move);


		matchInfo.setState(state);
		matchInfo.setTurnNumber(matchInfo.getTurnNumber() + 1);
		matchInfo.setTurn(state.getTurn());
		// also update winner/reason

		for (int i = 0; i < matches.size(); i++) {
			MatchInfo info = matches.get(i);
			if (info.getMatchId().equals(matchInfo.getMatchId())) {
				matches.set(i, matchInfo);
			}
		}

		saveMatchesToStorage();

		if (doAnimation) {
			view.animateMove(move, new Callback() {
				@Override
				public void execute() {
					setState(state);
					afterMove();
				}
			});
			
		} else {
			 setState(state);
			afterMove();
		
		}

		if (state.getGameResult() != null)
			gameEndAudio.play();
		fromPosition = null;
		currentPossible2Position = null;
		tempCurrentPosition = null;
		showPossibleStartPosition();
		view.removePromotion();
		doAnimation = true;
	}

	public void afterMove() {
		sendState();
	}

	public void setView(View view) {
		this.view = view;
	}

	public void setStateVar(State state) {
		this.state = state;
	}

	public void setState(State state) {
		this.state = state;
		if (state == null) {
			for (int r = 0; r < 8; r++) {
				for (int c = 0; c < 8; c++) {
					view.setPiece(r, c, null);
				}
			}
			return;
		}

		if (state.getGameResult() == null)
			view.setWhoseTurn(state.getTurn());
		else
			view.setGameResult(state.getGameResult());

		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				view.setPiece(r, c, state.getPiece(r, c));
			}
		}
		if (state.getGameResult() != null) {
			view.setRestartButton();
		}

		


		view.clearDragDrop();
		if (state.getTurn().equals(myColor)) {
			Set<Move> moves = stateExplorer.getPossibleMoves(state);
			for (Move move : moves) {
				final Position start = move.getFrom();
				final Position target = move.getTo();
				view.makeDraggableFromTo(start.getRow(), start.getCol(),
						target.getRow(), target.getCol(), new Callback() {

							@Override
							public void execute() {
								dropOn(target.getRow(), target.getCol());
							}
						});
			}
		}
		addImageHandlers();
		showPossibleStartPosition();

	}


	private void dropOn(int row, int col) {
		dealDropMove(row, col);
	}

	private void dealDropMove(int row, int col) {

		if (fromPosition == null)
			return;
		if (state == null)
			return;

		tempCurrentPosition = new Position(row, col);
		currentPossible2Position = stateExplorer.getPossibleMovesFromPosition(
				state, fromPosition);

		if (fromPosition.getRow() == 6
				&& state.getPiece(fromPosition).equals(
						new Piece(Color.WHITE, PieceKind.PAWN))) {
			if (currentPossible2Position.contains(new Move(fromPosition,
					tempCurrentPosition, PieceKind.QUEEN))) {
				view.setPromotion(Color.WHITE);
				addPromotionListener();
				return;
			}
		} else if (fromPosition.getRow() == 1
				&& state.getPiece(fromPosition).equals(
						new Piece(Color.BLACK, PieceKind.PAWN))) {
			if (currentPossible2Position.contains(new Move(fromPosition,
					tempCurrentPosition, PieceKind.QUEEN))) {
				view.setPromotion(Color.BLACK);
				addPromotionListener();
				return;
			}
		} else {
			doAnimation = false;
			movePieceInBoard(tempCurrentPosition, null);
		}
	}

	private void dragStart(int row, int col) {
		
		fromPosition = new Position(row, col);
		//System.out.println("drag start: "+fromPosition.toString());
		Set<Move> possibleDragMove = stateExplorer
				.getPossibleMovesFromPosition(state, fromPosition);
		view.clearHighlighted();

		for (Move p : possibleDragMove) {
			view.setHighlighted(p.getTo().getRow(), p.getTo().getCol(), true);
		}
	}

	private void addRestartButtonListener() {
		view.getRestartButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				view.setGameStatus("restart game");
				restartTheGame();
			}

		});
	}

	private void restartTheGame() {

		currentPossible2Position = null;
		fromPosition = null;
		promotionTo = null;
		tempCurrentPosition = null;
		choosePromotion = false;
		doAnimation = true;
		state = new State();
		setState(state);
		showPossibleStartPosition();
		sendState();

	}

	
	public void addImageHandlers() {
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				final int i = row;
				final int j = col;
				
				view.getImageClickHandler(row, col).addClickHandler(
						new ClickHandler() {
							@Override
							public void onClick(ClickEvent event) {
								clickOn(i, j);
							}
						});
			}
		}
		
	}

	PieceKind promotionTo = null;
	Set<Move> possibleMoveFromPosition = null;

	Position tempCurrentPosition = null;

	public void clickOn(int row, int col) {
		if(state==null)
			return;
		/*
		 * deal with not user turn
		 */
		if (!state.getTurn().equals(myColor)) {
			Window.alert("It is not your turn " + myEmail + " Turn: "
					+ state.getTurn());
			tempCurrentPosition = null;
			fromPosition = null;
			currentPossible2Position = null;
			return;
		}
		clickAudio.play();
		tempCurrentPosition = new Position(row, col);
		Set<Position> possibleStartPosition = stateExplorer
				.getPossibleStartPositions(state);
		if (possibleStartPosition.contains(tempCurrentPosition)) {
			possibleMoveFromPosition = stateExplorer
					.getPossibleMovesFromPosition(state, tempCurrentPosition);
			view.clearHighlighted();
			for (Move p : possibleMoveFromPosition) {
				view.setHighlighted(p.getTo().getRow(), p.getTo().getCol(),
						true);
			}
			currentPossible2Position = possibleMoveFromPosition;
			fromPosition = tempCurrentPosition;
			promotionTo = null;
			tempCurrentPosition = null;
			return;
		} else if (!possibleStartPosition.contains(tempCurrentPosition)
				&& (currentPossible2Position == null)) {
			fromPosition = null;
			promotionTo = null;
			tempCurrentPosition = null;
			showPossibleStartPosition();
			choosePromotion = false;
			view.removePromotion();
			return;

		} else if (!possibleStartPosition.contains(tempCurrentPosition)
				&& currentPossible2Position != null) {

			if (fromPosition.getRow() == 6
					&& state.getPiece(fromPosition).equals(
							new Piece(Color.WHITE, PieceKind.PAWN))) {
				if (currentPossible2Position.contains(new Move(fromPosition,
						tempCurrentPosition, PieceKind.QUEEN))) {
					view.setPromotion(Color.WHITE);
					addPromotionListener();
				}
			} else if (fromPosition.getRow() == 1
					&& state.getPiece(fromPosition).equals(
							new Piece(Color.BLACK, PieceKind.PAWN))) {
				if (currentPossible2Position.contains(new Move(fromPosition,
						tempCurrentPosition, PieceKind.QUEEN))) {
					view.setPromotion(Color.BLACK);
					addPromotionListener();
				}
			}

			else {
				movePieceInBoard(tempCurrentPosition, null);
			}
		}
	}

	public void promotionClickOn(int row, int col) {

		if (row == 0 && col == 0)
			promotionTo = PieceKind.BISHOP;
		else if (row == 0 && col == 1)
			promotionTo = PieceKind.KNIGHT;
		else if (row == 1 && col == 0)
			promotionTo = PieceKind.QUEEN;
		else if (row == 1 && col == 1)
			promotionTo = PieceKind.ROOK;

		view.removePromotionHighlishted();
		movePieceInBoard(tempCurrentPosition, promotionTo);
	}

	public void movePieceInBoard(Position tempCurrentPosition,
			PieceKind promotionKind) {

		if (currentPossible2Position.contains(new Move(fromPosition,
				tempCurrentPosition, promotionKind))) {
			if (state.getPiece(tempCurrentPosition) != null
					|| (state.getEnpassantPosition() != null && tempCurrentPosition
							.equals(state.getEnpassantPosition()))) {
				captureAudio.play();
			}

			Move move = new Move(fromPosition, tempCurrentPosition,
					promotionKind);
			doMove(move);
			return;

		} else {
			fromPosition = null;
			currentPossible2Position = null;
			tempCurrentPosition = null;
			showPossibleStartPosition();
			view.removePromotion();
			return;
		}
	}

	public void showPossibleStartPosition() {
		view.clearHighlighted();
		stateExplorer = new StateExplorerImpl();

		Set<Position> possibleStartPosition = stateExplorer
				.getPossibleStartPositions(state);
		if (!possibleStartPosition.isEmpty()) {
			for (Position start : possibleStartPosition) {
				view.setHighlighted(start.getRow(), start.getCol(), true);
			}
		}

	}


	private void addPromotionListener() {
		for (int row = 0; row < 2; row++) {
			for (int col = 0; col < 2; col++) {
				final int i = row;
				final int j = col;
				view.getPromotionImage(row, col).addClickHandler(
						new ClickHandler() {
							@Override
							public void onClick(ClickEvent event) {
								promotionClickOn(i, j);
							}
						});
			}
		}
	}

	private Audio createCaptureAudio() {
		Audio audio = Audio.createIfSupported();
		if (audio == null) {
			return null;
		}
		if (MediaElement.CAN_PLAY_PROBABLY.equals(audio
				.canPlayType(AudioElement.TYPE_MP3))) {
			audio.setSrc("shitianrenAudio/captureAudio.mp3");
		} else if (MediaElement.CAN_PLAY_PROBABLY.equals(audio
				.canPlayType(AudioElement.TYPE_WAV))) {
			audio.setSrc("shitianrenAudio/captureAudio.wav");
		} else if (MediaElement.CAN_PLAY_MAYBE.equals(audio
				.canPlayType(AudioElement.TYPE_MP3))) {
			audio.setSrc("shitianrenAudio/captureAudio.mp3");
		} else if (MediaElement.CAN_PLAY_MAYBE.equals(audio
				.canPlayType(AudioElement.TYPE_WAV))) {
			audio.setSrc("shitianrenAudio/captureAudio.wav");
		}
		audio.load();
		return audio;

	}

	private Audio createClickAudio() {
		Audio audio = Audio.createIfSupported();
		if (audio == null) {
			return null;
		}
		if (MediaElement.CAN_PLAY_PROBABLY.equals(audio
				.canPlayType(AudioElement.TYPE_MP3))) {
			audio.setSrc("shitianrenAudio/button_click.mp3");
		} else if (MediaElement.CAN_PLAY_PROBABLY.equals(audio
				.canPlayType(AudioElement.TYPE_WAV))) {
			audio.setSrc("shitianrenAudio/button_click.wav");
		} else if (MediaElement.CAN_PLAY_MAYBE.equals(audio
				.canPlayType(AudioElement.TYPE_MP3))) {
			audio.setSrc("shitianrenAudio/button_click.mp3");
		} else if (MediaElement.CAN_PLAY_MAYBE.equals(audio
				.canPlayType(AudioElement.TYPE_WAV))) {
			audio.setSrc("shitianrenAudio/button_click.wav");
		}
		audio.load();
		return audio;
	}

	private Audio createGameEndAudio() {
		Audio audio = Audio.createIfSupported();
		if (audio == null)
			return null;
		else {
			if (MediaElement.CAN_PLAY_PROBABLY.equals(audio
					.canPlayType(AudioElement.TYPE_MP3))) {
				audio.setSrc("shitianrenAudio/gameend.mp3");
			} else if (MediaElement.CAN_PLAY_PROBABLY.equals(audio
					.canPlayType(AudioElement.TYPE_WAV))) {
				audio.setSrc("shitianrenAudio/gameend.wav");
			} else if (MediaElement.CAN_PLAY_MAYBE.equals(audio
					.canPlayType(AudioElement.TYPE_MP3))) {
				audio.setSrc("shitianrenAudio/gameend.mp3");
			} else if (MediaElement.CAN_PLAY_MAYBE.equals(audio
					.canPlayType(AudioElement.TYPE_WAV))) {
				audio.setSrc("shitianrenAudio/gameend.wav");
			}
			audio.load();
			return audio;
		}
	}

	private void invitedMatch(final String email) {
		service.invitedMatch(email, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, "error", caught);

			}

			@Override
			public void onSuccess(Void info) {
				loadMatchList();
			}
		});
	}

	private void loadMatch(String matchId) {
		
		for (MatchInfo match : matches) {
			if (match.getMatchId().equals(matchId)) {
				matchInfo = match;
				myColor = matchInfo.getMyColor();
				opponentEmail = matchInfo.getOpponentEmail();
				setState(matchInfo.getState());

				break;
			}
		}

		service.getMatchState( matchId, new AsyncCallback<MatchInfo>() {
			@Override
			public void onSuccess(MatchInfo result) {
				logger.info(result.toString());

				matchInfo = result;
				myColor = matchInfo.getMyColor();
				opponentEmail = matchInfo.getOpponentEmail();
				setState(matchInfo.getState());
			}

			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
		String disStatus = "Your Color: "+myColor+"; "+"Opponent: "+opponentEmail+"; "+"Start Date: "+DateTimeFormat
				.getFormat(
						DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT)
				.format(matchInfo.getStartDate());
		
		view.setGameDisStatus(disStatus);

	}

	private void loadMatchList() {
		String savedMatchesString = storage.getFromStorage("matchList");

		if (savedMatchesString != null) {
			List<MatchInfo> savedMatches = new ArrayList<MatchInfo>();
			String[] data = savedMatchesString.split(" ");
			for (String savedMatch : data) {
				savedMatches.add(MatchInfo.deserialize(savedMatch));
			}
			processMatchList(savedMatches);
		}

		service.getMatches( new AsyncCallback<List<MatchInfo>>() {

			@Override
			public void onSuccess(List<MatchInfo> result) {
				logger.info(result.toString());

				processMatchList(result);
				saveMatchesToStorage();

			}

			@Override
			public void onFailure(Throwable caught) {
				
			}


		});
	}
	

	private void processMatchList(List<MatchInfo> matches) {
		this.matches = matches;

		// a map of matchIds to match descriptions to give to the view
		Map<String, String> matchMap = new LinkedHashMap<String, String>();

		for (MatchInfo info : matches) {
			matchMap.put(info.getMatchId(), getMatchListItem(info));
		}

		view.setMatchList(matchMap, matchInfo.getMatchId());
	}
	
	private void saveMatchesToStorage() {
		if (matches.isEmpty()) {
			storage.remove("matchList");
			return;
		}
		String matchString = "";
		for (MatchInfo info : matches) {
			matchString += info.serialize() + " ";
		}

		matchString = matchString.substring(0, matchString.length() - 1);

		storage.saveInStorage("matchList", matchString);
	}
	
	
	private void savePendingMovesToStorage() {
		if (pendingMoveMap.isEmpty()) {
			storage.remove("pendingMoves");
			return;
		}
		String s = "";
		for (Map.Entry<String, String> entry : pendingMoveMap.entrySet()) {
			s += entry.getKey() + "#" + entry.getValue() + " ";
		}
		s = s.substring(0, s.length() - 1);
		storage.saveInStorage("pendingMoves", s);
	}
	
	private Map<String, String> loadPendingMovesFromStorage() {
		String s = storage.getFromStorage("pendingMoves");
		Map<String, String> moveMap = new LinkedHashMap<String, String>();
		if (s == null || s.isEmpty()) {
			return moveMap;
		}

		try {
			String[] moves = s.split(" ");
			for (String move : moves) {
				String[] data = move.split("#");
				moveMap.put(data[0], data[1]);
			}
		} catch (Exception e) {
		}

		return moveMap;
	}
	
	
	
	public String getMatchListItem(MatchInfo info) {
		String result = "Opponent" + ": ";
		result += info.getOpponentEmail();
		result += (" " + "Turn" + ": ");
		if (info.getTurn().equals(info.getMyColor())) {
			result += (myEmail);
		} else{
			String opEmail = info.getOpponentEmail();
			result += (opEmail);

		}

		result += (getDateDescription(info));
		return result;
	}

	private String getDateDescription(MatchInfo info) {
		String string = " "
				+ "Start Date"
				+ ": "
				+ DateTimeFormat.getFormat(
						DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT)
						.format(info.getStartDate());
		return string;
	}

	private void AIMode() {
		createMatch("AI");
	}

	private void createMatch(final String email) {
		service.createMatch(email, new AsyncCallback<MatchInfo>() {

			@Override
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, "error", caught);

			}

			@Override
			public void onSuccess(MatchInfo info) {
				logger.info("match created with " + email);
				loadMatch(info.getMatchId());
				loadMatchList();

			}
		});

	}

}
