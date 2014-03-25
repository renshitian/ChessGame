package org.shitianren.hw3;

import org.shared.chess.Color;
import org.shared.chess.GameResult;
import org.shared.chess.GameResultReason;
import org.shared.chess.Piece;
import org.shared.chess.PieceKind;
import org.shared.chess.Position;
import org.shared.chess.State;

public class HistoryParser {
	
	public static String state2History(State state) {
		String result = "";
		String turn = "";
		turn += parseStateTurn(state);
		turn += "&";
		result += turn;
		String boardString = "";
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				if (row == 7 && col == 7)
					boardString += parseStatePiece(state.getPiece(row, col));
				else
					boardString += (parseStatePiece(state.getPiece(row, col)) + "!");
			}
		}

		result += boardString;
		result += "&";

		String canCastleBlackKing = "";
		if (state.isCanCastleKingSide(Color.BLACK))
			canCastleBlackKing += "T!";
		else
			canCastleBlackKing += "F!";

		String canCastleBlackQueen = "";
		if (state.isCanCastleQueenSide(Color.BLACK))
			canCastleBlackQueen += "T!";
		else
			canCastleBlackQueen += "F!";

		String canCastleWhiteKing = "";
		if (state.isCanCastleKingSide(Color.WHITE))
			canCastleWhiteKing += "T!";
		else
			canCastleWhiteKing += "F!";

		String canCastleWhiteQueen = "";
		if (state.isCanCastleQueenSide(Color.WHITE))
			canCastleWhiteQueen += "T";
		else
			canCastleWhiteQueen += "F";
		result += canCastleBlackKing;
		result += canCastleBlackQueen;
		result += canCastleWhiteKing;
		result += canCastleWhiteQueen;
		result += "&";

		String enpassantPosition = parseStateEnpassantPosition(state);

		result += enpassantPosition;
		result += "&";

		String gameResult = parseStateGameResult(state);

		result += gameResult;
		result += "&";

		String numberOfMovesWithoutCaptureNorPawnMoved = "";
		numberOfMovesWithoutCaptureNorPawnMoved += state
				.getNumberOfMovesWithoutCaptureNorPawnMoved();

		result += numberOfMovesWithoutCaptureNorPawnMoved;

		return result;
	}

	private static String parseStateGameResult(State state) {
		GameResult gameResult = state.getGameResult();
		String result = "";
		if (gameResult == null)
			return "NO";
		else {
			Color winColor = gameResult.getWinner();
			if (winColor == null) {
				result += "draw!";
			} else {
				result += (winColor + "!");
			}

			GameResultReason grr = gameResult.getGameResultReason();
			result += grr;

			return result;
		}
	}

	private static String parseStateEnpassantPosition(State state) {
		Position enpassantPosition = state.getEnpassantPosition();
		if (enpassantPosition == null)
			return "NN";
		else {
			String result = "";
			result += enpassantPosition.getRow();
			result += enpassantPosition.getCol();
			return result;
		}

	}

	private static String parseStateTurn(State state) {
		Color turn = state.getTurn();
		if (turn.equals(Color.BLACK))
			return "B";
		else
			return "W";
	}

	private static String parseStatePiece(Piece piece) {
		if (piece == null) {
			return "NN";
		} else {
			PieceKind pieceKind = piece.getKind();
			Color pieceColor = piece.getColor();
			String result = "";

			if (pieceColor.equals(Color.BLACK))
				result += "B";
			else
				result += "W";

			switch (pieceKind) {
			case QUEEN:
				result += "Q";
				break;
			case ROOK:
				result += "R";
				break;
			case BISHOP:
				result += "B";
				break;
			case KNIGHT:
				result += "N";
				break;
			case KING:
				result += "K";
				break;
			case PAWN:
				result += "P";
				break;
			}
			return result;
		}
	}

	public static State history2State(String history) {

		State state = new State();

		String[] tokens = history.split("&");
		String turn = tokens[0];
		String boardString = tokens[1];
		String canCastle = tokens[2];
		String enPosition = tokens[3];
		String gameResult = tokens[4];
		String numberWithout = tokens[5];


		if (turn.equals("W"))
			state.setTurn(Color.WHITE);
		else if (turn.equals("B"))
			state.setTurn(Color.BLACK);

		Piece[][] board = parseHistoryBoard(boardString);
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				state.setPiece(row, col, board[row][col]);
			}
		}
		//System.out.println("board: " + board[0][0]);

		boolean[] canCastling = new boolean[4];
		canCastling = parseCanCastle(canCastle);
	//	System.out.println("canCastling " + canCastling[0]+canCastling[1]);

		Position stateEnPosition = parseEnPosition(enPosition);
	//	System.out.println("stateEnPosition: " + stateEnPosition);

		GameResult stateGameResult = parseGameResult(gameResult);
	//	System.out.println("stateGameResult: " + stateGameResult);

	//	System.out.println("before parser number str:"+numberWithout);
		int number = parseNumberWithout(numberWithout);
	//	System.out.println("number: " + number);

		state.setCanCastleKingSide(Color.BLACK, canCastling[0]);
		state.setCanCastleQueenSide(Color.BLACK, canCastling[1]);
		state.setCanCastleKingSide(Color.WHITE, canCastling[2]);
		state.setCanCastleQueenSide(Color.WHITE, canCastling[3]);
	//	System.out.println("setcastle");

		state.setEnpassantPosition(stateEnPosition);
	//	System.out.println("set enposition");

		state.setGameResult(stateGameResult);
	//	System.out.println("set gameresult");

		state.setNumberOfMovesWithoutCaptureNorPawnMoved(number);
	//	System.out.println("set number ");

	//	System.out.println("history2state return state:"+state.toString());

		return state;

	}

	private static int parseNumberWithout(String numberWithout) {
	//	System.out.println("in parser number str:"+numberWithout);
		int result = Integer.parseInt(numberWithout);
	//	System.out.println("in parser number result int:"+result);
		return result;
	}

	private static GameResult parseGameResult(String gameResult) {
		if (gameResult.equals("NO"))
			return null;
		else {
			String[] temp = gameResult.split("!");
			Color winColor = null;
			if (temp[0].equals("draw"))
				winColor = null;
			else if (temp[0].equals("W"))
				winColor = Color.WHITE;
			else if (temp[0].equals("B"))
				winColor = Color.BLACK;

			GameResultReason reason = null;
			if (temp[1].equals("CHECKMATE"))
				reason = GameResultReason.CHECKMATE;
			else if (temp[1].equals("FIFTY_MOVE_RULE"))
				reason = GameResultReason.FIFTY_MOVE_RULE;
			else if (temp[1].equals("STALEMATE"))
				reason = GameResultReason.STALEMATE;
			else if (temp[1].equals("THREEFOLD_REPETITION_RULE"))
				reason = GameResultReason.THREEFOLD_REPETITION_RULE;

			return new GameResult(winColor, reason);
		}

	}

	private static Position parseEnPosition(String enPosition) {
		int row;
		int col;
		if (enPosition.equals("NN"))
			return null;
		int temp = Integer.parseInt(enPosition);
		col = temp % 10;
		row = (temp - col) / 10;
		return new Position(row, col);
	}

	private static boolean[] parseCanCastle(String canCastle) {
		boolean[] result = new boolean[4];
		String[] temp = canCastle.split("!");
		if (temp[0].equals("T"))
			result[0] = true;
		else
			result[0] = false;

		if (temp[1].equals("T"))
			result[1] = true;
		else
			result[1] = false;

		if (temp[2].equals("T"))
			result[2] = true;
		else
			result[2] = false;

		if (temp[3].equals("T"))
			result[3] = true;
		else
			result[3] = false;
		return result;
	}

	private static Piece[][] parseHistoryBoard(String boardString) {
		Piece[][] board = new Piece[8][8];
		String a = boardString.trim();
		String[] temp = a.split("!");
		int index = 0;
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				//System.out.println("index: " + index + "value: " + temp[index]);
				Piece tempPiece = parsePiece(temp[index]);
				//System.out.println(tempPiece);
				board[row][col] = tempPiece;
				index++;
			}
		}
		return board;
	}

	private static Piece parsePiece(String string) {

		Piece returnPiece = null;

		if (string.equals("WR"))
			returnPiece = new Piece(Color.WHITE, PieceKind.ROOK);
		else if (string.equals("WK"))
			returnPiece = new Piece(Color.WHITE, PieceKind.KING);
		else if (string.equals("WN"))
			returnPiece = new Piece(Color.WHITE, PieceKind.KNIGHT);
		else if (string.equals("WQ"))
			returnPiece = new Piece(Color.WHITE, PieceKind.QUEEN);
		else if (string.equals("WP"))
			returnPiece = new Piece(Color.WHITE, PieceKind.PAWN);
		else if (string.equals("WB"))
			returnPiece = new Piece(Color.WHITE, PieceKind.BISHOP);
		else if (string.equals("BR"))
			returnPiece = new Piece(Color.BLACK, PieceKind.ROOK);
		else if (string.equals("BK"))
			returnPiece = new Piece(Color.BLACK, PieceKind.KING);
		else if (string.equals("BN"))
			returnPiece = new Piece(Color.BLACK, PieceKind.KNIGHT);
		else if (string.equals("BQ"))
			returnPiece = new Piece(Color.BLACK, PieceKind.QUEEN);
		else if (string.equals("BP"))
			returnPiece = new Piece(Color.BLACK, PieceKind.PAWN);
		else if (string.equals("BB"))
			returnPiece = new Piece(Color.BLACK, PieceKind.BISHOP);
		else if (string.equals("NN"))
			returnPiece = null;

		return returnPiece;
	}

}
