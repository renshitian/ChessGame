package org.shitianren.hw2;

import java.util.ArrayList;

import org.shared.chess.Color;
import org.shared.chess.GameResult;
import org.shared.chess.GameResultReason;
import org.shared.chess.IllegalMove;
import org.shared.chess.Move;
import org.shared.chess.Piece;
import org.shared.chess.PieceKind;
import org.shared.chess.Position;
import org.shared.chess.State;
import org.shared.chess.StateChanger;
import static org.shared.chess.Color.BLACK;
import static org.shared.chess.Color.WHITE;
import static org.shared.chess.PieceKind.KNIGHT;
import static org.shared.chess.PieceKind.KING;
import static org.shared.chess.PieceKind.PAWN;
import static org.shared.chess.PieceKind.BISHOP;
import static org.shared.chess.PieceKind.ROOK;
import static org.shared.chess.PieceKind.QUEEN;

public class StateChangerImpl implements StateChanger {
	// get the piecekind to determine where this piece can go
	private PieceKind piecekind;
	private int toRow;
	private int toCol;
	private int fromRow;
	private int fromCol;
	private Position from;
	private Position to;
	private Piece piece;
	private Color color;
	private State state1;
	private PieceKind promotion;
	private boolean canCastlingKingSide;
	private boolean canCastlingQueenSide;


	private Position EnpassantPosition; // record the position for
										// EnpassantPosition
	private boolean isPawnMove; // flag for whether the pawn moves
	private boolean isLegalMove; // flag for legal move
	private boolean isCapture;// flag for capture happens
	private boolean isPromotion; // flag for whether there is legal promotion
	private boolean doCastlingKingSide; // flag for castlingkingside happens
	private boolean doCastlingQueenSide;// flag for castlingqueenside happens
	private boolean doEnpassantCapture;// flag for enpassantcapture happens

	public void makeMove(State state, Move move) throws IllegalMove {
		// initiate these instance variable so that other private method can use
		promotion = null;
		canCastlingKingSide = false;
		canCastlingQueenSide = false;
		boolean canCastlingKingSideOp = false;
		boolean canCastlingQueenSideOp = false;
		isPawnMove = false;
		isLegalMove = false;
		isCapture = false;
		isPromotion = false;
		doCastlingKingSide = false;
		doCastlingQueenSide = false;
		doEnpassantCapture = false;
		this.state1 = state.copy();
		from = move.getFrom();
		to = move.getTo();
		piece = state1.getPiece(from);
		EnpassantPosition = state.getEnpassantPosition();

		toRow = to.getRow();
		toCol = to.getCol();
		fromRow = from.getRow();
		fromCol = from.getCol();
		promotion = move.getPromoteToPiece();

		if (state1.getGameResult() != null) {
			// Game already ended!
			throw new IllegalMove();
		}
		if (piece == null) {
			// Nothing to move!
			throw new IllegalMove();
		}
		color = piece.getColor();
		piecekind = piece.getKind();


		canCastlingKingSide = state1.isCanCastleKingSide(color);
		canCastlingQueenSide = state1.isCanCastleQueenSide(color);
		canCastlingQueenSideOp = state1.isCanCastleQueenSide(color.getOpposite());
		canCastlingKingSideOp = state1.isCanCastleKingSide(color.getOpposite());
		// other piecekind want to promote
		if ((!(piecekind.equals(PAWN))) && (promotion != null))
			throw new IllegalMove();
		// want to promote into king or pawn
		if ((promotion == KING) || (promotion == PAWN))
			throw new IllegalMove();

		if (color != state1.getTurn()) {
			// Wrong player moves!
			throw new IllegalMove();
		}

		if ((toRow == fromRow) && (toCol == fromCol)) {
			// Piece does not move
			throw new IllegalMove();

		}

		if ((toRow < 0) || (toRow > 7) || (toCol < 0) || (toCol > 7)) {
			// To a place out of board
			throw new IllegalMove();
		}

		if ((fromRow < 0) || (fromRow > 7) || (fromCol < 0) || (fromCol > 7)) {
			// from a place out of board
			throw new IllegalMove();
		}

		// check whether this piece moves legally
		switch (piecekind) {
		case PAWN:
			ArrayList<Position> a = new ArrayList<Position>();
			addPossiPawnMove(color, from, state1, a);
			// check for legal to position
			for (int i = 0; i < a.size(); i++) {
				if (a.get(i).equals(to)) {
					isLegalMove = true;
					isPawnMove = true;
					EnpassantPosition = null;
					// move two square
					if ((from.getCol() == to.getCol())
							&& (Math.abs(from.getRow() - to.getRow()) == 2))
						EnpassantPosition = to;
					// white piece promotion
					if ((color.equals(WHITE)) && (to.getRow() == 7)
							&& (promotion != null)) {
						isPromotion = true;
					}
					// white piece promotion illegally
					else if ((color.equals(WHITE)) && (to.getRow() != 7)
							&& (promotion != null)) {
						throw new IllegalMove();
					}
					// black piece promotion
					if ((color.equals(BLACK)) && (to.getRow() == 0)
							&& (promotion != null)) {
						isPromotion = true;
					}
					// black piece promotion illegally
					else if ((color.equals(BLACK)) && (to.getRow() != 0)
							&& (promotion != null)) {
						throw new IllegalMove();
					}
					// move to capture
					if (state1.getPiece(to) != null)
						isCapture = true;

					// move to capture enpassant
					if ((state1.getPiece(to) == null)
							&& (state1.getEnpassantPosition() != null)
							&& (state1.getEnpassantPosition().getRow() == from
									.getRow())
							&& (Math.abs(state1.getEnpassantPosition().getCol()
									- from.getCol()) == 1)) {
						if ((color == BLACK)
								&& (to.getRow() == (state1
										.getEnpassantPosition().getRow() - 1))
								&& (state1.getEnpassantPosition().getCol() == to
										.getCol())) {
							doEnpassantCapture = true;
						}
						if ((color == WHITE)
								&& (to.getRow() == (state1
										.getEnpassantPosition().getRow() + 1))
								&& (state1.getEnpassantPosition().getCol() == to
										.getCol())) {
							doEnpassantCapture = true;
						}

					}
					break;
				}
			}
			break;
		case BISHOP:
			isLegalMove = isLegalBishopMove();
			break;
		case KING:
			isLegalMove = isLegalKingMove();
			break;
		case KNIGHT:
			isLegalMove = isLegalKnightMove();
			break;
		case QUEEN:
			isLegalMove = isLegalQueenMove();
			break;
		case ROOK:
			isLegalMove = isLegalRookMove();
			break;
		}
		// if illegal throw exception
		if (!isLegalMove)
			throw new IllegalMove();

		// check whether the resulted state is legal:such as whether the king is
		// undercheck after movement
		State temp_result = state1.copy();

		// do some changes to temp_result and check whether the result is legal
		if (doEnpassantCapture) {
			temp_result.setPiece(from, null);
			temp_result.setPiece(to, piece);
			temp_result.setPiece(temp_result.getEnpassantPosition(), null);
			temp_result.setNumberOfMovesWithoutCaptureNorPawnMoved(0);
			EnpassantPosition = null;
		}

		else if (doCastlingKingSide) {

			for (int i = from.getCol(); i <= to.getCol(); i++) {
				if (isUnderCheck(color, temp_result, new Position(
						from.getRow(), i)))
					throw new IllegalMove();
			}

			temp_result.setPiece(from, null);
			temp_result.setPiece(to, piece);
			if (color == WHITE) {
				temp_result.setPiece(new Position(0, 7), null);
				temp_result
						.setPiece(new Position(0, 5), new Piece(WHITE, ROOK));
			} else if (color == BLACK) {
				temp_result.setPiece(new Position(7, 7), null);
				temp_result
						.setPiece(new Position(7, 5), new Piece(BLACK, ROOK));
			}
			canCastlingKingSide = false;
			temp_result.setNumberOfMovesWithoutCaptureNorPawnMoved(state1
					.getNumberOfMovesWithoutCaptureNorPawnMoved() + 1);
		}

		else if (doCastlingQueenSide) {
			for (int i = from.getCol(); i >= to.getCol(); i--) {
				if (isUnderCheck(color, temp_result, new Position(
						from.getRow(), i)))
					throw new IllegalMove();
			}

			temp_result.setPiece(from, null);
			temp_result.setPiece(to, piece);
			if (color == WHITE) {
				temp_result.setPiece(new Position(0, 0), null);
				temp_result
						.setPiece(new Position(0, 3), new Piece(WHITE, ROOK));
			} else if (color == BLACK) {
				temp_result.setPiece(new Position(7, 0), null);
				temp_result
						.setPiece(new Position(7, 3), new Piece(BLACK, ROOK));
			}
			temp_result.setNumberOfMovesWithoutCaptureNorPawnMoved(state1
					.getNumberOfMovesWithoutCaptureNorPawnMoved() + 1);
			canCastlingQueenSide = false;
		}

		else {
			if (isCapture) {
				// check whether the op rook is captured
				if (color == WHITE) {
					if (to.equals(new Position(7, 0))
							&& temp_result.getPiece(to).equals(
									new Piece(BLACK, ROOK))
							&& canCastlingQueenSideOp) {
						canCastlingQueenSideOp = false;
					}
					if (to.equals(new Position(7, 7))
							&& temp_result.getPiece(to).equals(
									new Piece(BLACK, ROOK))
							&& canCastlingKingSideOp) {
						canCastlingKingSideOp = false;
					}
				} else if (color == BLACK) {
					if (to.equals(new Position(0, 0))
							&& temp_result.getPiece(to).equals(
									new Piece(WHITE, ROOK))
							&& canCastlingQueenSideOp) {
						canCastlingQueenSideOp = false;
					}
					if (to.equals(new Position(0, 7))
							&& temp_result.getPiece(to).equals(
									new Piece(WHITE, ROOK))
							&& canCastlingKingSideOp) {
						canCastlingKingSideOp = false;
					}
				}
			}

			temp_result.setPiece(from, null);
			if (isPromotion) {
				temp_result.setPiece(to, new Piece(color, promotion));
			} else {
				temp_result.setPiece(to, piece);
			}

			if (isPawnMove || isCapture) {
				temp_result.setNumberOfMovesWithoutCaptureNorPawnMoved(0);
			} else {
				temp_result.setNumberOfMovesWithoutCaptureNorPawnMoved(state1
						.getNumberOfMovesWithoutCaptureNorPawnMoved() + 1);
			}

		}
		temp_result.setTurn(color.getOpposite());
		temp_result.setCanCastleKingSide(color,
				canCastlingKingSide);
		temp_result.setCanCastleQueenSide(color,
				canCastlingQueenSide);
		temp_result.setCanCastleKingSide(color.getOpposite(),canCastlingKingSideOp);
		temp_result.setCanCastleQueenSide(color.getOpposite(), canCastlingQueenSideOp);
		temp_result.setEnpassantPosition(EnpassantPosition);

		// find the position of king
		Position kingPosition = null;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (temp_result.getPiece(i, j) != null) {
					if (temp_result.getPiece(i, j).equals(
							new Piece(color, KING))) {
						kingPosition = new Position(i, j);
						break;
					}
				}
			}
		}

		// check whether ths king is under check after movement
		if (isUnderCheck(color, temp_result, kingPosition)) {
			throw new IllegalMove();
		}

		// check for several gameresult reasons for op color and set the related
		// reasons

		// checkmate or stalemate
		isCheckOrStaleMate(temp_result);
		// check for a draw for fifty move rule
		checkFiftyMoveRule(temp_result);

		// if reach here, then it is legal, do changes to state
		if (doEnpassantCapture) {
			state.setPiece(from, null);
			state.setPiece(to, piece);
			state.setPiece(state.getEnpassantPosition(), null);
		}

		else if (doCastlingKingSide) {
			state.setPiece(from, null);
			state.setPiece(to, piece);
			if (color == WHITE) {
				state.setPiece(new Position(0, 7), null);
				state.setPiece(new Position(0, 5), new Piece(WHITE, ROOK));
			} else if (color == BLACK) {
				state.setPiece(new Position(7, 7), null);
				state.setPiece(new Position(7, 5), new Piece(BLACK, ROOK));
			}
		}

		else if (doCastlingQueenSide) {
			state.setPiece(from, null);
			state.setPiece(to, piece);
			if (color == WHITE) {
				state.setPiece(new Position(0, 0), null);
				state.setPiece(new Position(0, 3), new Piece(WHITE, ROOK));
			} else if (color == BLACK) {
				state.setPiece(new Position(7, 0), null);
				state.setPiece(new Position(7, 3), new Piece(BLACK, ROOK));
			}
		}

		else {

			state.setPiece(from, null);
			if (isPromotion) {
				state.setPiece(to, new Piece(color, promotion));
			} else {
				state.setPiece(to, piece);
			}
		}
		state.setCanCastleKingSide(color,
				temp_result.isCanCastleKingSide(color));
		state.setCanCastleKingSide(color.getOpposite(),
				temp_result.isCanCastleKingSide(color.getOpposite()));

		state.setCanCastleQueenSide(color,
				temp_result.isCanCastleQueenSide(color));
		state.setCanCastleQueenSide(color.getOpposite(),
				temp_result.isCanCastleQueenSide(color.getOpposite()));
		state.setEnpassantPosition(temp_result.getEnpassantPosition());
		state.setGameResult(temp_result.getGameResult());
		state.setTurn(temp_result.getTurn());
		state.setNumberOfMovesWithoutCaptureNorPawnMoved(temp_result
				.getNumberOfMovesWithoutCaptureNorPawnMoved());

	}

	// end of makeMove

	private void isCheckOrStaleMate(State temp_result) {
		// find king position
		Position kingPosition = null;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (temp_result.getPiece(i, j) != null) {
					if (temp_result.getPiece(i, j).equals(
							new Piece(color.getOpposite(), KING))) {
						kingPosition = new Position(i, j);
						break;
					}
				}
			}
		}
		// check whether there is possible position to move
		boolean flag = (possibleMovePosition(color.getOpposite(), temp_result)
				.size() > 0);

		if (isUnderCheck(color.getOpposite(), temp_result, kingPosition)) {
			if (!flag) {
				// if under check and no possible moves - checkmate
				temp_result.setGameResult(new GameResult(color,
						GameResultReason.CHECKMATE));
			}
		} else {
			if (!flag) {
				// if not under check and no moves - stalemate
				temp_result.setGameResult(new GameResult(null,
						GameResultReason.STALEMATE));
			}
		}
	}

	private ArrayList<Position> possibleMovePosition(Color opposite,
			State temp_result) {

		ArrayList<Position> temp_position = new ArrayList<Position>();
		// for each square find piece
		for (int i = 0; i <= 7; i++) {
			for (int j = 0; j <= 7; j++) {
				Piece piece_check = temp_result.getPiece(new Position(i, j));
				if (piece_check != null) {
					if (piece_check.getColor().equals(opposite)) {
						PieceKind pk = piece_check.getKind();
						if (pk.equals(PAWN)) {
							addPossiPawnMove(opposite, new Position(i, j),
									temp_result, temp_position);
						}
						if (pk.equals(KNIGHT)) {
							addPossiKnightMove(opposite, new Position(i, j),
									temp_result, temp_position);
						}
						if (pk.equals(QUEEN)) {
							addPossiQueenMove(opposite, new Position(i, j),
									temp_result, temp_position);
						}
						if (pk.equals(ROOK)) {
							addPossiRookMove(opposite, new Position(i, j),
									temp_result, temp_position);
						}
						if (pk.equals(BISHOP)) {
							addPossiBishopMove(opposite, new Position(i, j),
									temp_result, temp_position);
						}
						if (pk.equals(KING)) {
							addPossiKingMove(opposite, new Position(i, j),
									temp_result, temp_position);
						}
					}
				}
			}
		}
		return temp_position;
	}

	private void addPossiKingMove(Color kingcolor, Position position,
			State temp_result, ArrayList<Position> temp_position) {
		int row = position.getRow();
		int col = position.getCol();
		Position[] possiKingPosition = new Position[] {
				new Position(row + 1, col), new Position(row - 1, col),
				new Position(row, col + 1), new Position(row, col - 1),
				new Position(row + 1, col + 1), new Position(row - 1, col - 1),
				new Position(row - 1, col + 1), new Position(row + 1, col - 1) };

		for (int l = 0; l < possiKingPosition.length; l++) {
			int i = possiKingPosition[l].getRow();
			int j = possiKingPosition[l].getCol();

			if ((i < 0) || (i > 7) || (j < 0) || (j > 7)) {
				continue;
			} else {
				if ((temp_result.getPiece(possiKingPosition[l]) == null)
						|| ((temp_result.getPiece(possiKingPosition[l]) != null) && (temp_result
								.getPiece(possiKingPosition[l]).getColor()
								.equals(kingcolor.getOpposite())))) {
					State temp = temp_result.copy();
					temp.setPiece(position, null);
					temp.setPiece(possiKingPosition[l],
							temp_result.getPiece(position));
					if (!isUnderCheck(kingcolor, temp, possiKingPosition[l])) {
						temp_position.add(possiKingPosition[l]);
					}

				}
			}
		}

		if ((kingcolor.equals(WHITE))
				&& (temp_result.isCanCastleKingSide(kingcolor))) {
			boolean isOccupied = false;
			Position[] positionNeedCheck = new Position[] { new Position(0, 5),
					new Position(0, 6) };
			for (int i = 0; i < positionNeedCheck.length; i++) {
				if (temp_result.getPiece(positionNeedCheck[i]) != null) {
					isOccupied = true;
					break;
				}
			}
			boolean isChecked = false;
			if (!isOccupied) {
				Position[] positionNeedCheck1 = new Position[] {new Position(0,4), new Position(0, 5),
						new Position(0, 6) };
				for (int i = 0; i < positionNeedCheck1.length; i++) {
					State temp = temp_result.copy();
					temp.setPiece(position, null);
					temp.setPiece(positionNeedCheck1[i], new Piece(kingcolor,
							KING));
					isChecked = isUnderCheck(kingcolor, temp,
							positionNeedCheck1[i]);
					if (isChecked) {
						break;
					}
				}
			}

			if ((!isOccupied) && (!isChecked)) {
				temp_position.add(new Position(0, 6));
			}
		}

		if ((kingcolor.equals(WHITE))
				&& (temp_result.isCanCastleQueenSide(kingcolor))) {
			boolean isOccupied = false;
			Position[] positionNeedCheck = new Position[] { new Position(0, 3),
					new Position(0, 2), new Position(0, 1) };
			for (int i = 0; i < positionNeedCheck.length; i++) {
				if (temp_result.getPiece(positionNeedCheck[i]) != null) {
					isOccupied = true;
					break;
				}
			}
			boolean isChecked = false;
			Position[] positionNeedCheck1 = new Position[] {new Position(0,4) ,new Position(0, 3),
					new Position(0, 2) };
			if (!isOccupied) {
				for (int i = 0; i < positionNeedCheck1.length; i++) {
					State temp = temp_result.copy();
					temp.setPiece(position, null);
					temp.setPiece(positionNeedCheck1[i], new Piece(kingcolor,
							KING));
					isChecked = isUnderCheck(kingcolor, temp,
							positionNeedCheck1[i]);
					if (isChecked) {
						break;
					}
				}
			}

			if ((!isOccupied) && (!isChecked)) {
				temp_position.add(new Position(0, 2));
			}
		}

		if ((kingcolor.equals(BLACK))
				&& (temp_result.isCanCastleKingSide(kingcolor))) {
			boolean isOccupied = false;
			Position[] positionNeedCheck = new Position[] { new Position(7, 5),
					new Position(7, 6) };
			for (int i = 0; i < positionNeedCheck.length; i++) {
				if (temp_result.getPiece(positionNeedCheck[i]) != null) {
					isOccupied = true;
					break;
				}
			}
			boolean isChecked = false;
			if (!isOccupied) {
				Position[] positionNeedCheck1 = new Position[] {new Position(7,4), new Position(7, 5),
						new Position(7, 6) };
				for (int i = 0; i < positionNeedCheck1.length; i++) {
					State temp = temp_result.copy();
					temp.setPiece(position, null);
					temp.setPiece(positionNeedCheck1[i], new Piece(kingcolor,
							KING));
					isChecked = isUnderCheck(kingcolor, temp,
							positionNeedCheck1[i]);
					if (isChecked) {
						break;
					}
				}
			}

			if ((!isOccupied) && (!isChecked)) {
				temp_position.add(new Position(7, 6));
			}
		}
		if ((kingcolor.equals(BLACK))
				&& (temp_result.isCanCastleQueenSide(kingcolor))) {
			boolean isOccupied = false;
			Position[] positionNeedCheck = new Position[] { new Position(7, 3),
					new Position(7, 2), new Position(7, 1) };
			for (int i = 0; i < positionNeedCheck.length; i++) {
				if (temp_result.getPiece(positionNeedCheck[i]) != null) {
					isOccupied = true;
					break;
				}
			}
			boolean isChecked = false;
			if (!isOccupied) {
				Position[] positionNeedCheck1 = new Position[] {new Position(7,4) ,new Position(7, 3),
						new Position(7, 2) };
				for (int i = 0; i < positionNeedCheck1.length; i++) {
					State temp = temp_result.copy();
					temp.setPiece(position, null);
					temp.setPiece(positionNeedCheck1[i], new Piece(kingcolor,
							KING));
					isChecked = isUnderCheck(kingcolor, temp,
							positionNeedCheck1[i]);
					if (isChecked) {
						break;
					}
				}
			}

			if ((!isOccupied) && (!isChecked)) {
				temp_position.add(new Position(7, 2));
			}
		}
	}

	private void addPossiBishopMove(Color bishopcolor, Position position,
			State temp_result, ArrayList<Position> temp_position) {
		int row = position.getRow();
		int col = position.getCol();
		Position kingPosition = null;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (temp_result.getPiece(i, j) != null) {
					if (temp_result.getPiece(i, j).equals(
							new Piece(bishopcolor, KING))) {
						kingPosition = new Position(i, j);
						break;
					}
				}
			}
		}
		// check up-left
		int i3 = row + 1;
		int j3 = col - 1;
		while ((i3 <= 7) && (j3 >= 0) && (temp_result.getPiece(i3, j3) == null)) {
			State temp = temp_result.copy();
			temp.setPiece(position, null);
			temp.setPiece(new Position(i3, j3), temp_result.getPiece(position));
			if (!isUnderCheck(bishopcolor, temp, kingPosition)) {
				temp_position.add(new Position(i3, j3));
			}
			i3++;
			j3--;
		}
		if ((i3 <= 7) && (j3 >= 0)) {
			if (temp_result.getPiece(i3, j3).getColor()
					.equals(bishopcolor.getOpposite())) {
				State temp = temp_result.copy();
				temp.setPiece(position, null);
				temp.setPiece(new Position(i3, j3),
						temp_result.getPiece(position));
				if (!isUnderCheck(bishopcolor, temp, kingPosition)) {
					temp_position.add(new Position(i3, j3));
				}
			}
		}
		// check down-left
		int i4 = row - 1;
		int j4 = col - 1;
		while ((i4 >= 0) && (j4 >= 0) && (temp_result.getPiece(i4, j4) == null)) {
			State temp = temp_result.copy();
			temp.setPiece(position, null);
			temp.setPiece(new Position(i4, j4), temp_result.getPiece(position));
			if (!isUnderCheck(bishopcolor, temp, kingPosition)) {
				temp_position.add(new Position(i4, j4));
			}
			i4--;
			j4--;
		}
		if ((i4 >= 0) && (j4 >= 0)) {
			if (temp_result.getPiece(i4, j4).getColor()
					.equals(bishopcolor.getOpposite())) {
				State temp = temp_result.copy();
				temp.setPiece(position, null);
				temp.setPiece(new Position(i4, j4),
						temp_result.getPiece(position));
				if (!isUnderCheck(bishopcolor, temp, kingPosition)) {
					temp_position.add(new Position(i4, j4));
				}
			}
		}

		// check up-right
		int i7 = row + 1;
		int j7 = col + 1;
		while ((i7 <= 7) && (j7 <= 7) && (temp_result.getPiece(i7, j7) == null)) {
			State temp = temp_result.copy();
			temp.setPiece(position, null);
			temp.setPiece(new Position(i7, j7), temp_result.getPiece(position));
			if (!isUnderCheck(bishopcolor, temp, kingPosition)) {
				temp_position.add(new Position(i7, j7));
			}
			i7++;
			j7++;
		}
		if ((i7 <= 7) && (j7 <= 7)) {
			if (temp_result.getPiece(i7, j7).getColor()
					.equals(bishopcolor.getOpposite())) {
				State temp = temp_result.copy();
				temp.setPiece(position, null);
				temp.setPiece(new Position(i7, j7),
						temp_result.getPiece(position));
				if (!isUnderCheck(bishopcolor, temp, kingPosition)) {
					temp_position.add(new Position(i7, j7));
				}
			}
		}
		// check down-right
		int i8 = row - 1;
		int j8 = col + 1;
		while ((i8 >= 0) && (j8 <= 7) && (temp_result.getPiece(i8, j8) == null)) {
			State temp = temp_result.copy();
			temp.setPiece(position, null);
			temp.setPiece(new Position(i8, j8), temp_result.getPiece(position));
			if (!isUnderCheck(bishopcolor, temp, kingPosition)) {
				temp_position.add(new Position(i8, j8));
			}
			i8--;
			j8++;
		}
		if ((i8 >= 0) && (j8 <= 7)) {
			if (temp_result.getPiece(i8, j8).getColor()
					.equals(bishopcolor.getOpposite())) {
				State temp = temp_result.copy();
				temp.setPiece(position, null);
				temp.setPiece(new Position(i8, j8),
						temp_result.getPiece(position));
				if (!isUnderCheck(bishopcolor, temp, kingPosition)) {
					temp_position.add(new Position(i8, j8));
				}
			}
		}
	}

	private void addPossiRookMove(Color rookcolor, Position position,
			State temp_result, ArrayList<Position> temp_position) {
		int row = position.getRow();
		int col = position.getCol();
		Position kingPosition = null;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (temp_result.getPiece(i, j) != null) {
					if (temp_result.getPiece(i, j).equals(
							new Piece(rookcolor, KING))) {
						kingPosition = new Position(i, j);
						break;
					}
				}
			}
		}
		// check up
		int i1 = row + 1;
		while ((i1 <= 7) && (temp_result.getPiece(i1, col) == null)) {
			State temp = temp_result.copy();
			temp.setPiece(position, null);
			temp.setPiece(new Position(i1, col), temp_result.getPiece(position));
			if (!isUnderCheck(rookcolor, temp, kingPosition)) {
				temp_position.add(new Position(i1, col));
			}
			i1++;
		}
		if (i1 <= 7) {
			if (temp_result.getPiece(i1, col).getColor()
					.equals(rookcolor.getOpposite())) {
				State temp = temp_result.copy();
				temp.setPiece(position, null);
				temp.setPiece(new Position(i1, col),
						temp_result.getPiece(position));
				if (!isUnderCheck(rookcolor, temp, kingPosition)) {
					temp_position.add(new Position(i1, col));
				}
			}
		}
		// check down
		int i2 = row - 1;
		while ((i2 >= 0) && (temp_result.getPiece(i2, col) == null)) {
			State temp = temp_result.copy();
			temp.setPiece(position, null);
			temp.setPiece(new Position(i2, col), temp_result.getPiece(position));
			if (!isUnderCheck(rookcolor, temp, kingPosition)) {
				temp_position.add(new Position(i2, col));
			}
			i2--;
		}
		if (i2 >= 0) {
			if (temp_result.getPiece(i2, col).getColor()
					.equals(rookcolor.getOpposite())) {
				State temp = temp_result.copy();
				temp.setPiece(position, null);
				temp.setPiece(new Position(i2, col),
						temp_result.getPiece(position));
				if (!isUnderCheck(rookcolor, temp, kingPosition)) {
					temp_position.add(new Position(i2, col));
				}
			}
		}

		// check left
		int j5 = col - 1;
		while ((j5 >= 0) && (temp_result.getPiece(row, j5) == null)) {
			State temp = temp_result.copy();
			temp.setPiece(position, null);
			temp.setPiece(new Position(row, j5), temp_result.getPiece(position));
			if (!isUnderCheck(rookcolor, temp, kingPosition)) {
				temp_position.add(new Position(row, j5));
			}
			j5--;
		}
		if (j5 >= 0) {
			if (temp_result.getPiece(row, j5).getColor()
					.equals(rookcolor.getOpposite())) {
				State temp = temp_result.copy();
				temp.setPiece(position, null);
				temp.setPiece(new Position(row, j5),
						temp_result.getPiece(position));
				if (!isUnderCheck(rookcolor, temp, kingPosition)) {
					temp_position.add(new Position(row, j5));
				}
			}
		}
		// check right
		int j6 = col + 1;
		while ((j6 <= 7) && (temp_result.getPiece(row, j6) == null)) {
			State temp = temp_result.copy();
			temp.setPiece(position, null);
			temp.setPiece(new Position(row, j6), temp_result.getPiece(position));
			if (!isUnderCheck(rookcolor, temp, kingPosition)) {
				temp_position.add(new Position(row, j6));
			}
			j6++;
		}
		if (j6 <= 7) {
			if (temp_result.getPiece(row, j6).getColor()
					.equals(rookcolor.getOpposite())) {
				State temp = temp_result.copy();
				temp.setPiece(position, null);
				temp.setPiece(new Position(row, j6),
						temp_result.getPiece(position));
				if (!isUnderCheck(rookcolor, temp, kingPosition)) {
					temp_position.add(new Position(row, j6));
				}
			}
		}

	}

	private void addPossiQueenMove(Color queencolor, Position position,
			State temp_result, ArrayList<Position> temp_position) {
		int row = position.getRow();
		int col = position.getCol();
		Position kingPosition = null;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (temp_result.getPiece(i, j) != null) {
					if (temp_result.getPiece(i, j).equals(
							new Piece(queencolor, KING))) {
						kingPosition = new Position(i, j);
						break;
					}
				}
			}
		}
		// check up
		int i1 = row + 1;
		while ((i1 <= 7) && (temp_result.getPiece(i1, col) == null)) {
			State temp = temp_result.copy();
			temp.setPiece(position, null);
			temp.setPiece(new Position(i1, col), temp_result.getPiece(position));
			if (!isUnderCheck(queencolor, temp, kingPosition)) {
				temp_position.add(new Position(i1, col));
			}
			i1++;
		}
		if (i1 <= 7) {
			if (temp_result.getPiece(i1, col).getColor()
					.equals(queencolor.getOpposite())) {
				State temp = temp_result.copy();
				temp.setPiece(position, null);
				temp.setPiece(new Position(i1, col),
						temp_result.getPiece(position));
				if (!isUnderCheck(queencolor, temp, kingPosition)) {
					temp_position.add(new Position(i1, col));
				}
			}
		}
		// check down
		int i2 = row - 1;
		while ((i2 >= 0) && (temp_result.getPiece(i2, col) == null)) {
			State temp = temp_result.copy();
			temp.setPiece(position, null);
			temp.setPiece(new Position(i2, col), temp_result.getPiece(position));
			if (!isUnderCheck(queencolor, temp, kingPosition)) {
				temp_position.add(new Position(i2, col));
			}
			i2--;
		}
		if (i2 >= 0) {
			if (temp_result.getPiece(i2, col).getColor()
					.equals(queencolor.getOpposite())) {
				State temp = temp_result.copy();
				temp.setPiece(position, null);
				temp.setPiece(new Position(i2, col),
						temp_result.getPiece(position));
				if (!isUnderCheck(queencolor, temp, kingPosition)) {
					temp_position.add(new Position(i2, col));
				}
			}
		}

		// check up-left
		int i3 = row + 1;
		int j3 = col - 1;
		while ((i3 <= 7) && (j3 >= 0) && (temp_result.getPiece(i3, j3) == null)) {
			State temp = temp_result.copy();
			temp.setPiece(position, null);
			temp.setPiece(new Position(i3, j3), temp_result.getPiece(position));
			if (!isUnderCheck(queencolor, temp, kingPosition)) {
				temp_position.add(new Position(i3, j3));
			}
			i3++;
			j3--;
		}
		if ((i3 <= 7) && (j3 >= 0)) {
			if (temp_result.getPiece(i3, j3).getColor()
					.equals(queencolor.getOpposite())) {
				State temp = temp_result.copy();
				temp.setPiece(position, null);
				temp.setPiece(new Position(i3, j3),
						temp_result.getPiece(position));
				if (!isUnderCheck(queencolor, temp, kingPosition)) {
					temp_position.add(new Position(i3, j3));
				}
			}
		}
		// check down-left
		int i4 = row - 1;
		int j4 = col - 1;
		while ((i4 >= 0) && (j4 >= 0) && (temp_result.getPiece(i4, j4) == null)) {
			State temp = temp_result.copy();
			temp.setPiece(position, null);
			temp.setPiece(new Position(i4, j4), temp_result.getPiece(position));
			if (!isUnderCheck(queencolor, temp, kingPosition)) {
				temp_position.add(new Position(i4, j4));
			}
			i4--;
			j4--;
		}
		if ((i4 >= 0) && (j4 >= 0)) {
			if (temp_result.getPiece(i4, j4).getColor()
					.equals(queencolor.getOpposite())) {
				State temp = temp_result.copy();
				temp.setPiece(position, null);
				temp.setPiece(new Position(i4, j4),
						temp_result.getPiece(position));
				if (!isUnderCheck(queencolor, temp, kingPosition)) {
					temp_position.add(new Position(i4, j4));
				}
			}
		}
		// check left
		int j5 = col - 1;
		while ((j5 >= 0) && (temp_result.getPiece(row, j5) == null)) {
			State temp = temp_result.copy();
			temp.setPiece(position, null);
			temp.setPiece(new Position(row, j5), temp_result.getPiece(position));
			if (!isUnderCheck(queencolor, temp, kingPosition)) {
				temp_position.add(new Position(row, j5));
			}
			j5--;
		}
		if (j5 >= 0) {
			if (temp_result.getPiece(row, j5).getColor()
					.equals(queencolor.getOpposite())) {
				State temp = temp_result.copy();
				temp.setPiece(position, null);
				temp.setPiece(new Position(row, j5),
						temp_result.getPiece(position));
				if (!isUnderCheck(queencolor, temp, kingPosition)) {
					temp_position.add(new Position(row, j5));
				}
			}
		}
		// check right
		int j6 = col + 1;
		while ((j6 <= 7) && (temp_result.getPiece(row, j6) == null)) {
			State temp = temp_result.copy();
			temp.setPiece(position, null);
			temp.setPiece(new Position(row, j6), temp_result.getPiece(position));
			if (!isUnderCheck(queencolor, temp, kingPosition)) {
				temp_position.add(new Position(row, j6));
			}
			j6++;
		}
		if (j6 <= 7) {
			if (temp_result.getPiece(row, j6).getColor()
					.equals(queencolor.getOpposite())) {
				State temp = temp_result.copy();
				temp.setPiece(position, null);
				temp.setPiece(new Position(row, j6),
						temp_result.getPiece(position));
				if (!isUnderCheck(queencolor, temp, kingPosition)) {
					temp_position.add(new Position(row, j6));
				}
			}
		}
		// check up-right
		int i7 = row + 1;
		int j7 = col + 1;
		while ((i7 <= 7) && (j7 <= 7) && (temp_result.getPiece(i7, j7) == null)) {
			State temp = temp_result.copy();
			temp.setPiece(position, null);
			temp.setPiece(new Position(i7, j7), temp_result.getPiece(position));
			if (!isUnderCheck(queencolor, temp, kingPosition)) {
				temp_position.add(new Position(i7, j7));
			}
			i7++;
			j7++;
		}
		if ((i7 <= 7) && (j7 <= 7)) {
			if (temp_result.getPiece(i7, j7).getColor()
					.equals(queencolor.getOpposite())) {
				State temp = temp_result.copy();
				temp.setPiece(position, null);
				temp.setPiece(new Position(i7, j7),
						temp_result.getPiece(position));
				if (!isUnderCheck(queencolor, temp, kingPosition)) {
					temp_position.add(new Position(i7, j7));
				}
			}
		}
		// check down-right
		int i8 = row - 1;
		int j8 = col + 1;
		while ((i8 >= 0) && (j8 <= 7) && (temp_result.getPiece(i8, j8) == null)) {
			State temp = temp_result.copy();
			temp.setPiece(position, null);
			temp.setPiece(new Position(i8, j8), temp_result.getPiece(position));
			if (!isUnderCheck(queencolor, temp, kingPosition)) {
				temp_position.add(new Position(i8, j8));
			}
			i8--;
			j8++;
		}
		if ((i8 >= 0) && (j8 <= 7)) {
			if (temp_result.getPiece(i8, j8).getColor()
					.equals(queencolor.getOpposite())) {
				State temp = temp_result.copy();
				temp.setPiece(position, null);
				temp.setPiece(new Position(i8, j8),
						temp_result.getPiece(position));
				if (!isUnderCheck(queencolor, temp, kingPosition)) {
					temp_position.add(new Position(i8, j8));
				}
			}
		}
	}

	private void addPossiKnightMove(Color knightcolor, Position position,
			State temp_result, ArrayList<Position> temp_position) {
		int row = position.getRow();
		int col = position.getCol();

		Position kingPosition = null;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (temp_result.getPiece(i, j) != null) {
					if (temp_result.getPiece(i, j).equals(
							new Piece(knightcolor, KING))) {
						kingPosition = new Position(i, j);
						break;
					}
				}
			}
		}
		Position[] possiKnightPosition = new Position[] {
				new Position(row + 2, col + 1), new Position(row + 1, col + 2),
				new Position(row - 2, col - 1), new Position(row - 1, col - 2),
				new Position(row + 2, col - 1), new Position(row - 2, col + 1),
				new Position(row - 1, col + 2), new Position(row + 1, col - 2) };

		for (int l = 0; l < possiKnightPosition.length; l++) {

			int i = possiKnightPosition[l].getRow();
			int j = possiKnightPosition[l].getCol();

			if ((i < 0) || (i > 7) || (j < 0) || (j > 7)) {
				continue;
			} else {
				if ((temp_result.getPiece(possiKnightPosition[l]) == null)
						|| ((temp_result.getPiece(possiKnightPosition[l]) != null) && (temp_result
								.getPiece(possiKnightPosition[l]).getColor()
								.equals(knightcolor.getOpposite())))) {
					State temp = temp_result.copy();
					temp.setPiece(position, null);
					temp.setPiece(possiKnightPosition[l],
							temp_result.getPiece(position));
					if (!isUnderCheck(knightcolor, temp, kingPosition)) {
						temp_position.add(possiKnightPosition[l]);
					}
				}
			}
		}
	}

	private void addPossiPawnMove(Color pawncolor, Position position,
			State temp_result, ArrayList<Position> temp_position) {
		int row = position.getRow();
		int col = position.getCol();
		Position kingPosition = null;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (temp_result.getPiece(i, j) != null) {
					if (temp_result.getPiece(i, j).equals(
							new Piece(pawncolor, KING))) {
						kingPosition = new Position(i, j);
						break;
					}
				}
			}
		}
		if (pawncolor == WHITE) {
			if (row < 7) {
				if (row == 1) {
					if ((temp_result.getPiece(row + 2, col) == null)
							&& (temp_result.getPiece(row + 1, col) == null)) {
						State temp = temp_result.copy();
						temp.setPiece(row, col, null);
						temp.setPiece(row + 2, col,
								temp_result.getPiece(position));
						if (!isUnderCheck(pawncolor, temp, kingPosition)) {
							temp_position.add(new Position(row + 2, col));
						}
					}
				}
				if (temp_result.getPiece(row + 1, col) == null) {
					State temp = temp_result.copy();
					temp.setPiece(row, col, null);
					temp.setPiece(row + 1, col, temp_result.getPiece(position));
					if (!isUnderCheck(pawncolor, temp, kingPosition)) {
						temp_position.add(new Position(row + 1, col));
					}
				}

				if ((col < 7)
						&& (temp_result.getPiece(row + 1, col + 1) != null)) {
					if (temp_result.getPiece(row + 1, col + 1).getColor()
							.equals(pawncolor.getOpposite())) {
						State temp = temp_result.copy();
						temp.setPiece(row, col, null);
						temp.setPiece(row + 1, col + 1,
								temp_result.getPiece(position));
						if (!isUnderCheck(pawncolor, temp, kingPosition)) {
							temp_position.add(new Position(row + 1, col + 1));
						}
					}
				} else if ((col < 7)
						&& (temp_result.getPiece(row + 1, col + 1) == null)) {
					if ((temp_result.getEnpassantPosition() != null)
							&& (temp_result.getEnpassantPosition()
									.equals(new Position(row, col + 1)))) {
						State temp = temp_result.copy();
						temp.setPiece(row, col, null);
						temp.setPiece(temp_result.getEnpassantPosition(), null);
						temp.setPiece(row + 1, col + 1,
								temp_result.getPiece(position));
						if (!isUnderCheck(pawncolor, temp, kingPosition)) {
							temp_position.add(new Position(row + 1, col + 1));
						}
					}
				}

				if ((col > 0)
						&& (temp_result.getPiece(row + 1, col - 1) != null)) {
					if (temp_result.getPiece(row + 1, col - 1).getColor()
							.equals(pawncolor.getOpposite())) {
						State temp = temp_result.copy();
						temp.setPiece(row, col, null);
						temp.setPiece(row + 1, col - 1,
								temp_result.getPiece(position));
						if (!isUnderCheck(pawncolor, temp, kingPosition)) {
							temp_position.add(new Position(row + 1, col - 1));
						}
					}
				}

				else if ((col > 0)
						&& (temp_result.getPiece(row + 1, col - 1) == null)) {
					if ((temp_result.getEnpassantPosition() != null)
							&& (temp_result.getEnpassantPosition()
									.equals(new Position(row, col - 1)))) {
						State temp = temp_result.copy();
						temp.setPiece(row, col, null);
						temp.setPiece(temp_result.getEnpassantPosition(), null);
						temp.setPiece(row + 1, col - 1,
								temp_result.getPiece(position));
						if (!isUnderCheck(pawncolor, temp, kingPosition)) {
							temp_position.add(new Position(row + 1, col - 1));
						}
					}
				}

			}

		}
		// black
		else {
			if (row > 0) {
				if (row == 6) {
					if ((temp_result.getPiece(row - 2, col) == null)
							&& (temp_result.getPiece(row - 1, col) == null)) {

						State temp = temp_result.copy();
						temp.setPiece(row, col, null);
						temp.setPiece(row - 2, col,
								temp_result.getPiece(position));
						if (!isUnderCheck(pawncolor, temp, kingPosition)) {
							temp_position.add(new Position(row - 2, col));
						}
					}
				}
				if (temp_result.getPiece(row - 1, col) == null) {
					State temp = temp_result.copy();
					temp.setPiece(row, col, null);
					temp.setPiece(row - 1, col, temp_result.getPiece(position));
					if (!isUnderCheck(pawncolor, temp, kingPosition)) {
						temp_position.add(new Position(row - 1, col));
					}
				}

				if ((col < 7)
						&& (temp_result.getPiece(row - 1, col + 1) != null)) {
					if (temp_result.getPiece(row - 1, col + 1).getColor()
							.equals(pawncolor.getOpposite())) {
						State temp = temp_result.copy();
						temp.setPiece(row, col, null);
						temp.setPiece(row - 1, col + 1,
								temp_result.getPiece(position));
						if (!isUnderCheck(pawncolor, temp, kingPosition)) {
							temp_position.add(new Position(row - 1, col + 1));
						}
					}
				} else if ((col < 7)
						&& (temp_result.getPiece(row - 1, col + 1) == null)) {
					if ((temp_result.getEnpassantPosition() != null)
							&& (temp_result.getEnpassantPosition()
									.equals(new Position(row, col + 1)))) {
						State temp = temp_result.copy();
						temp.setPiece(row, col, null);
						temp.setPiece(temp_result.getEnpassantPosition(), null);
						temp.setPiece(row - 1, col + 1,
								temp_result.getPiece(position));
						if (!isUnderCheck(pawncolor, temp, kingPosition)) {
							temp_position.add(new Position(row - 1, col + 1));
						}
					}
				}

				if ((col > 0)
						&& (temp_result.getPiece(row - 1, col - 1) != null)) {
					if (temp_result.getPiece(row - 1, col - 1).getColor()
							.equals(pawncolor.getOpposite())) {
						State temp = temp_result.copy();
						temp.setPiece(row, col, null);
						temp.setPiece(row - 1, col - 1,
								temp_result.getPiece(position));
						if (!isUnderCheck(pawncolor, temp, kingPosition)) {
							temp_position.add(new Position(row - 1, col - 1));
						}
					}
				} else if ((col > 0)
						&& (temp_result.getPiece(row - 1, col - 1) == null)) {
					if ((temp_result.getEnpassantPosition() != null)
							&& (temp_result.getEnpassantPosition()
									.equals(new Position(row, col - 1)))) {
						State temp = temp_result.copy();
						temp.setPiece(row, col, null);
						temp.setPiece(temp_result.getEnpassantPosition(), null);
						temp.setPiece(row - 1, col - 1,
								temp_result.getPiece(position));
						if (!isUnderCheck(pawncolor, temp, kingPosition)) {
							temp_position.add(new Position(row - 1, col - 1));
						}

					}
				}

			}

		}

	}

	private void checkFiftyMoveRule(State temp_result) {
		if (temp_result.getNumberOfMovesWithoutCaptureNorPawnMoved() == 100)
			temp_result.setGameResult(new GameResult(null,
					GameResultReason.FIFTY_MOVE_RULE));

	}

	private boolean isUnderCheck(Color temp_color, State temp_result,
			Position kingPosition) {

		int kingRow = kingPosition.getRow();
		int kingCol = kingPosition.getCol();
		// check up
		if (kingRow < 7) {
			for (int i = kingRow + 1; i <= 7; i++) {
				Piece temp_piece = temp_result.getPiece(i, kingCol);
				if (temp_piece != null) {
					if (temp_piece.getColor() == temp_color.getOpposite()) {
						if ((temp_piece.getKind() == QUEEN)
								|| (temp_piece.getKind() == ROOK)) {
							return true;
						} else {
							break;
						}
					} else if (temp_piece.getColor() == temp_color) {
						break;
					}
				}
			}
		}
		// check down
		if (kingRow > 0) {
			for (int i = kingRow - 1; i >= 0; i--) {
				Piece temp_piece = temp_result.getPiece(i, kingCol);
				if (temp_piece != null) {
					if (temp_piece.getColor() == temp_color.getOpposite()) {
						if ((temp_piece.getKind() == QUEEN)
								|| (temp_piece.getKind() == ROOK)) {
							return true;
						} else {
							break;
						}
					} else if (temp_piece.getColor() == temp_color) {
						break;
					}
				}
			}
		}
		// check left
		if (kingCol > 0) {
			for (int j = kingCol - 1; j >= 0; j--) {
				Piece temp_piece = temp_result.getPiece(kingRow, j);
				if (temp_piece != null) {
					if (temp_piece.getColor() == temp_color.getOpposite()) {
						if ((temp_piece.getKind() == QUEEN)
								|| (temp_piece.getKind() == ROOK)) {
							return true;
						} else {
							break;
						}
					} else if (temp_piece.getColor() == temp_color) {
						break;
					}
				}
			}
		}
		// check right
		if (kingCol < 7) {
			for (int j = kingCol + 1; j <= 7; j++) {
				Piece temp_piece = temp_result.getPiece(kingRow, j);
				if (temp_piece != null) {
					if (temp_piece.getColor() == temp_color.getOpposite()) {
						if ((temp_piece.getKind() == QUEEN)
								|| (temp_piece.getKind() == ROOK)) {
							return true;
						} else {
							break;
						}
					} else if (temp_piece.getColor() == temp_color) {
						break;
					}
				}
			}
		}
		// check up-left
		if ((kingRow < 7) && (kingCol > 0)) {
			int i = kingRow + 1;
			int j = kingCol - 1;
			while ((i <= 7) && (j >= 0)) {
				Piece temp_piece = temp_result.getPiece(i, j);
				if (temp_piece != null) {
					if (temp_piece.getColor() == temp_color.getOpposite()) {
						if ((temp_piece.getKind() == QUEEN)
								|| (temp_piece.getKind() == BISHOP)) {
							return true;
						} else {
							break;
						}
					} else if (temp_piece.getColor() == temp_color) {
						break;
					}
				}

				i++;
				j--;

			}
		}
		// check up-right
		if ((kingRow < 7) && (kingCol < 7)) {
			int i = kingRow + 1;
			int j = kingCol + 1;
			while ((i <= 7) && (j <= 7)) {
				Piece temp_piece = temp_result.getPiece(i, j);
				if (temp_piece != null) {
					if (temp_piece.getColor() == temp_color.getOpposite()) {
						if ((temp_piece.getKind() == QUEEN)
								|| (temp_piece.getKind() == BISHOP)) {
							return true;
						} else {
							break;
						}
					} else if (temp_piece.getColor() == temp_color) {
						break;
					}
				}
				i++;
				j++;
			}
		}
		// check down-left
		if ((kingRow > 0) && (kingCol > 0)) {
			int i = kingRow - 1;
			int j = kingCol - 1;
			while ((i >= 0) && (j >= 0)) {
				Piece temp_piece = temp_result.getPiece(i, j);
				if (temp_piece != null) {
					if (temp_piece.getColor() == temp_color.getOpposite()) {
						if ((temp_piece.getKind() == QUEEN)
								|| (temp_piece.getKind() == BISHOP)) {
							return true;
						} else {
							break;
						}
					} else if (temp_piece.getColor() == temp_color) {
						break;
					}
				}
				i--;
				j--;
			}
		}
		// check down-right
		if ((kingRow > 0) && (kingCol < 7)) {
			int i = kingRow - 1;
			int j = kingCol + 1;
			while ((i >= 0) && (j <= 7)) {
				Piece temp_piece = temp_result.getPiece(i, j);
				if (temp_piece != null) {
					if (temp_piece.getColor() == temp_color.getOpposite()) {
						if ((temp_piece.getKind() == QUEEN)
								|| (temp_piece.getKind() == BISHOP)) {
							return true;
						} else {
							break;
						}
					} else if (temp_piece.getColor() == temp_color) {
						break;
					}
				}

				i--;
				j++;
			}
		}

		// check knight position
		Position[] possiKnightPosition = new Position[] {
				new Position(kingRow + 2, kingCol + 1),
				new Position(kingRow + 1, kingCol + 2),
				new Position(kingRow - 2, kingCol - 1),
				new Position(kingRow - 1, kingCol - 2),
				new Position(kingRow + 2, kingCol - 1),
				new Position(kingRow - 2, kingCol + 1),
				new Position(kingRow - 1, kingCol + 2),
				new Position(kingRow + 1, kingCol - 2) };
		for (int l = 0; l < possiKnightPosition.length; l++) {
			int i = possiKnightPosition[l].getRow();
			int j = possiKnightPosition[l].getCol();
			if ((i < 0) || (i > 7) || (j < 0) || (j > 7)) {
				continue;
			} else {
				Piece temp_piece = temp_result.getPiece(i, j);
				if (temp_piece != null) {
					if (temp_piece.getColor() == temp_color.getOpposite()) {
						if (temp_piece.getKind() == KNIGHT) {
							return true;
						} else {
							continue;
						}
					} else if (temp_piece.getColor() == temp_color) {
						continue;
					}
				}
			}
		}

		// check pawn position
		if (temp_color == WHITE) {
			if (kingRow < 7) {
				if (kingCol > 0) {
					Piece temp_piece1 = temp_result.getPiece(kingRow + 1,
							kingCol - 1);
					if (temp_piece1 != null) {
						if (temp_piece1.getColor() == temp_color.getOpposite()) {
							if (temp_piece1.getKind() == PAWN) {
								return true;
							}
						}
					}
				}
				if (kingCol < 7) {
					Piece temp_piece2 = temp_result.getPiece(kingRow + 1,
							kingCol + 1);
					if (temp_piece2 != null) {
						if (temp_piece2.getColor() == temp_color.getOpposite()) {
							if (temp_piece2.getKind() == PAWN) {
								return true;
							}
						}
					}
				}

			}
		} else if (temp_color == BLACK) {
			if (kingRow > 0) {
				if (kingCol > 0) {
					Piece temp_piece1 = temp_result.getPiece(kingRow - 1,
							kingCol - 1);
					if (temp_piece1 != null) {
						if (temp_piece1.getColor() == temp_color.getOpposite()) {
							if (temp_piece1.getKind() == PAWN) {
								return true;
							}
						}
					}
				}

				if (kingCol < 7) {
					Piece temp_piece2 = temp_result.getPiece(kingRow - 1,
							kingCol + 1);
					if (temp_piece2 != null) {
						if (temp_piece2.getColor() == temp_color.getOpposite()) {
							if (temp_piece2.getKind() == PAWN) {
								return true;
							}
						}
					}
				}
			}
		}
		// check king position
		Position[] possiKingPosition = new Position[] {
				new Position(kingRow + 1, kingCol),
				new Position(kingRow - 1, kingCol),
				new Position(kingRow, kingCol + 1),
				new Position(kingRow, kingCol - 1),
				new Position(kingRow + 1, kingCol + 1),
				new Position(kingRow - 1, kingCol - 1),
				new Position(kingRow - 1, kingCol + 1),
				new Position(kingRow + 1, kingCol - 1) };
		for (int l = 0; l < possiKingPosition.length; l++) {
			int i = possiKingPosition[l].getRow();
			int j = possiKingPosition[l].getCol();
			if ((i < 0) || (i > 7) || (j < 0) || (j > 7)) {
				continue;
			} else {
				Piece temp_piece = temp_result.getPiece(i, j);
				if (temp_piece != null) {
					if (temp_piece.getColor() == temp_color.getOpposite()) {
						if (temp_piece.getKind() == KING) {
							return true;
						} else {
							continue;
						}
					} else if (temp_piece.getColor() == temp_color) {
						continue;
					}
				}
			}
		}

		return false;

	}

	// check whether queen moves legally
	public boolean isLegalQueenMove() {
		// check whether this queen piece moves vertical, horizontal, diagonal
		if (!((fromRow == toRow) || (fromCol == toCol) || (Math.abs(toRow
				- fromRow) == Math.abs(toCol - fromCol)))) {
			return false;
		}

		// check whether this queen moves over piece
		if ((toRow == fromRow) && (toCol > fromCol)) {
			int l = toCol - fromCol;
			for (int i = 1; i < l; i++) {
				if (state1.getPiece(fromRow, fromCol + i) != null)
					return false;
			}
		}

		else if ((toRow == fromRow) && (toCol < fromCol)) {
			int l = toCol - fromCol;
			for (int i = -1; i > l; i--) {
				if (state1.getPiece(fromRow, fromCol + i) != null)
					return false;
			}
		}

		else if ((toCol == fromCol) && (toRow > fromRow)) {
			int l = toRow - fromRow;
			for (int i = 1; i < l; i++) {
				if (state1.getPiece(fromRow + i, fromCol) != null)
					return false;
			}
		}

		else if ((toCol == fromCol) && (toRow < fromRow)) {
			int l = toRow - fromRow;
			for (int i = -1; i > l; i--) {
				if (state1.getPiece(fromRow + i, fromCol) != null)
					return false;
			}
		}

		else if ((toRow > fromRow) && (toCol > fromCol)) {
			int l = toRow - fromRow;
			for (int i = 1; i < l; i++) {
				if (state1.getPiece(fromRow + i, fromCol + i) != null)
					return false;
			}
		} else if ((toRow > fromRow) && (toCol < fromCol)) {
			int l = toRow - fromRow;
			for (int i = 1; i < l; i++) {
				if (state1.getPiece(fromRow + i, fromCol - i) != null)
					return false;
			}
		} else if ((toRow < fromRow) && (toCol > fromCol)) {
			int l = toCol - fromCol;
			for (int i = 1; i < l; i++) {
				if (state1.getPiece(fromRow - i, fromCol + i) != null)
					return false;
			}
		} else if ((toRow < fromRow) && (toCol < fromCol)) {
			int l = toRow - fromRow;
			for (int i = -1; i > l; i--) {
				if (state1.getPiece(fromRow + i, fromCol + i) != null)
					return false;
			}
		}

		// check whether the to position is occupied
		Piece topiece = state1.getPiece(to);
		if (topiece != null) {
			if (color == topiece.getColor())
				return false;
			if (color != topiece.getColor())
				isCapture = true;
		}

		EnpassantPosition = null;
		return true;
	}

	// check whether rook moves legally
	private boolean isLegalRookMove() {
		// check whether this rook moves vertical or horizontal
		if (!((toRow == fromRow) || (toCol == fromCol))) {
			return false;
		}
		// check whether this rook moves over other pieces
		if ((toRow == fromRow) && (toCol > fromCol)) {
			int l = toCol - fromCol;
			for (int i = 1; i < l; i++) {
				if (state1.getPiece(fromRow, fromCol + i) != null)
					return false;
			}
		}

		else if ((toRow == fromRow) && (toCol < fromCol)) {
			int l = toCol - fromCol;
			for (int i = -1; i > l; i--) {
				if (state1.getPiece(fromRow, fromCol + i) != null)
					return false;
			}
		}

		else if ((toCol == fromCol) && (toRow > fromRow)) {
			int l = toRow - fromRow;
			for (int i = 1; i < l; i++) {
				if (state1.getPiece(fromRow + i, fromCol) != null)
					return false;
			}
		} else if ((toCol == fromCol) && (toRow < fromRow)) {
			int l = toRow - fromRow;
			for (int i = -1; i > l; i--) {
				if (state1.getPiece(fromRow + i, fromCol) != null)
					return false;
			}
		}
		// check whether the to position is occupied
		Piece topiece = state1.getPiece(to);
		if (topiece != null) {
			if (color == topiece.getColor())
				return false;
			if (color != topiece.getColor())
				isCapture = true;
		}

		if ((color == WHITE) && (fromRow == 0) && (fromCol == 0)
				&& (canCastlingQueenSide))
			canCastlingQueenSide = false;
		else if ((color == WHITE) && (fromRow == 0) && (fromCol == 7)
				&& (canCastlingKingSide))
			canCastlingKingSide = false;
		else if ((color == BLACK) && (fromRow == 7) && (fromCol == 0)
				&& (canCastlingQueenSide))
			canCastlingQueenSide = false;
		else if ((color == BLACK) && (fromRow == 7) && (fromCol == 7)
				&& (canCastlingKingSide))
			canCastlingKingSide = false;

		EnpassantPosition = null;
		return true;
	}

	// check whether this king moves legally without concening checkmate
	private boolean isLegalKingMove() {
		// check whether the king wants to castling king side
		if (color == WHITE) {
			if ((canCastlingKingSide) && (from.equals(new Position(0, 4)))
					&& (to.equals(new Position(0, 6)))) {
				for (int i = 5; i <= 6; i++) {
					Piece temp_piece = state1.getPiece(new Position(0, i));
					if (temp_piece != null)
						return false;
				}
				doCastlingKingSide = true;
			} else if ((canCastlingQueenSide)
					&& (from.equals(new Position(0, 4)))
					&& (to.equals(new Position(0, 2)))) {
				for (int i = 3; i > 0; i--) {
					Piece temp_piece = state1.getPiece(new Position(0, i));
					if (temp_piece != null)
						return false;
				}
				doCastlingQueenSide = true;
			}
		} else if (color == BLACK) {
			if ((canCastlingKingSide) && (from.equals(new Position(7, 4)))
					&& (to.equals(new Position(7, 6)))) {
				for (int i = 5; i <= 6; i++) {
					Piece temp_piece = state1.getPiece(new Position(7, i));
					if (temp_piece != null)
						return false;
				}
				doCastlingKingSide = true;
			} else if ((canCastlingQueenSide)
					&& (from.equals(new Position(7, 4)))
					&& (to.equals(new Position(7, 2)))) {
				for (int i = 3; i > 0; i--) {
					Piece temp_piece = state1.getPiece(new Position(7, i));
					if (temp_piece != null)
						return false;
				}
				doCastlingQueenSide = true;
			}
		}

		if ((!doCastlingKingSide) && (!doCastlingQueenSide)) {
			// if the king does not castle then check whether the king moves one
			// square
			if (!(((Math.abs(toRow - fromRow) == 1) && (Math.abs(toCol
					- fromCol) == 1))
					|| ((toRow == fromRow) && ((Math.abs(toCol - fromCol) == 1))) || ((toCol == fromCol) && (Math
					.abs(toRow - fromRow) == 1)))) {
				return false;
			}
			// check whether the to position is occupied
			Piece topiece = state1.getPiece(to);
			if (topiece != null) {
				if (color == topiece.getColor())
					return false;
				if (color != topiece.getColor())
					isCapture = true;
			}
		}
		canCastlingKingSide = false;
		canCastlingQueenSide = false;
		EnpassantPosition = null;
		return true;
	}

	// check whether this knight moves legally
	private boolean isLegalKnightMove() {
		// check whether the knight moves legal
		if (!(((Math.abs(toRow - fromRow) == 2) && (Math.abs(toCol - fromCol) == 1)) || ((Math
				.abs(toRow - fromRow) == 1) && (Math.abs(toCol - fromCol) == 2)))) {
			return false;
		}
		// check whether the to position is occupied
		Piece toPiece = state1.getPiece(toRow, toCol);
		if (toPiece != null) {
			if (color == toPiece.getColor()) {
				return false;
			}
			if (color != toPiece.getColor()) {
				isCapture = true;
			}
		}
		EnpassantPosition = null;
		return true;
	}

	// check whether this bishop moves legally
	private boolean isLegalBishopMove() {

		if (!(Math.abs(toRow - fromRow) == Math.abs(toCol - fromCol))) {
			return false;
		}

		// check whether this piece moves over pieces

		if ((toRow > fromRow) && (toCol > fromCol)) {
			int l = toRow - fromRow;
			for (int i = 1; i < l; i++) {
				if (state1.getPiece(fromRow + i, fromCol + i) != null)
					return false;
			}
		} else if ((toRow > fromRow) && (toCol < fromCol)) {
			int l = toRow - fromRow;
			for (int i = 1; i < l; i++) {
				if (state1.getPiece(fromRow + i, fromCol - i) != null)
					return false;
			}
		} else if ((toRow < fromRow) && (toCol > fromCol)) {
			int l = toCol - fromCol;
			for (int i = 1; i < l; i++) {
				if (state1.getPiece(fromRow - i, fromCol + i) != null)
					return false;
			}
		} else if ((toRow < fromRow) && (toCol < fromCol)) {
			int l = toRow - fromRow;
			for (int i = -1; i > l; i--) {
				if (state1.getPiece(fromRow + i, fromCol + i) != null)
					return false;
			}
		}

		// check whether the to position is occupied
		Piece toPiece = state1.getPiece(to);
		if (toPiece != null) {
			if (color == toPiece.getColor())
				return false;
			if (color != toPiece.getColor())
				isCapture = true;
		}
		EnpassantPosition = null;
		return true;
	}
	

}
