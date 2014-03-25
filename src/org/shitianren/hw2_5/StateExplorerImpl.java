package org.shitianren.hw2_5;

import static org.shared.chess.Color.BLACK;
import static org.shared.chess.Color.WHITE;
import static org.shared.chess.PieceKind.BISHOP;
import static org.shared.chess.PieceKind.KING;
import static org.shared.chess.PieceKind.KNIGHT;
import static org.shared.chess.PieceKind.PAWN;
import static org.shared.chess.PieceKind.QUEEN;
import static org.shared.chess.PieceKind.ROOK;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.shared.chess.Color;
import org.shared.chess.Move;
import org.shared.chess.Piece;
import org.shared.chess.PieceKind;
import org.shared.chess.Position;
import org.shared.chess.State;
import org.shared.chess.StateExplorer;

public class StateExplorerImpl implements StateExplorer {

	@Override
	public Set<Move> getPossibleMoves(State state) {
		ArrayList<Move> temp = new ArrayList<Move>();
		Set<Move> result = new HashSet<Move>();
		temp = possibleMovePosition(state.getTurn(), state);
		if(state.getGameResult()!=null)
			return result;
		
		for (int i = 0; i < temp.size(); i++) {
			result.add(temp.get(i));
		}

		return result;
	}

	@Override
	public Set<Move> getPossibleMovesFromPosition(State state, Position start) {
		ArrayList<Move> temp = new ArrayList<Move>();
		Set<Move> result = new HashSet<Move>();
		if(state.getGameResult()!=null)
			return result;
		Piece piece = state.getPiece(start);
		if (piece != null) {
			PieceKind piecekind = piece.getKind();
			Color color = piece.getColor();


			switch (piecekind) {
			case PAWN:
				addPossiPawnMove(color, start, state, temp);
				break;
			case KNIGHT:
				addPossiKnightMove(color, start, state, temp);
				break;
			case QUEEN:
				addPossiQueenMove(color, start, state, temp);
				break;
			case ROOK:
				addPossiRookMove(color, start, state, temp);
				break;
			case BISHOP:
				addPossiBishopMove(color, start, state, temp);
				break;
			case KING:
				addPossiKingMove(color, start, state, temp);
				break;

			}
			for (int i = 0; i < temp.size(); i++) {
				result.add(temp.get(i));
			}

			return result;

		} else
			return result;
	}

	@Override
	public Set<Position> getPossibleStartPositions(State state) {
		Color color = state.getTurn();
		Set<Position> result = new HashSet<Position>();
		if(state.getGameResult()!=null)
			return result;
		for (int i = 0; i <= 7; i++) {
			for (int j = 0; j <= 7; j++) {
				Piece piece = state.getPiece(i, j);
				if ((piece != null) && (piece.getColor().equals(color))) {
					PieceKind pk = piece.getKind();
					if (pk.equals(PAWN)) {
						ArrayList<Move> temp = new ArrayList<Move>();
						addPossiPawnMove(color, new Position(i, j), state, temp);
						if (temp.size() > 0)
							result.add(new Position(i, j));
					}
					if (pk.equals(KNIGHT)) {
						ArrayList<Move> temp = new ArrayList<Move>();
						addPossiKnightMove(color, new Position(i, j), state,
								temp);
						if (temp.size() > 0)
							result.add(new Position(i, j));
					}
					if (pk.equals(QUEEN)) {
						ArrayList<Move> temp = new ArrayList<Move>();
						addPossiQueenMove(color, new Position(i, j), state,
								temp);
						if (temp.size() > 0)
							result.add(new Position(i, j));
					}
					if (pk.equals(ROOK)) {
						ArrayList<Move> temp = new ArrayList<Move>();
						addPossiRookMove(color, new Position(i, j), state, temp);
						if (temp.size() > 0)
							result.add(new Position(i, j));
					}
					if (pk.equals(BISHOP)) {
						ArrayList<Move> temp = new ArrayList<Move>();
						addPossiBishopMove(color, new Position(i, j), state,
								temp);
						if (temp.size() > 0)
							result.add(new Position(i, j));

					}
					if (pk.equals(KING)) {
						ArrayList<Move> temp = new ArrayList<Move>();
						addPossiKingMove(color, new Position(i, j), state, temp);
						if (temp.size() > 0)
							result.add(new Position(i, j));

					}
				}
			}
		}
		return result;
	}

	private ArrayList<Move> possibleMovePosition(Color opposite,
			State temp_result) {

		ArrayList<Move> temp_position = new ArrayList<Move>();
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
			State temp_result, ArrayList<Move> temp_position) {

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
						temp_position.add(new Move(position,
								possiKingPosition[l], null));
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
				Position[] positionNeedCheck1 = new Position[] {
						new Position(0, 4), new Position(0, 5),
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
				temp_position.add(new Move(position, new Position(0, 6), null));
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
			if (!isOccupied) {
				Position[] positionNeedCheck1 = new Position[] {
						new Position(0, 4), new Position(0, 3),
						new Position(0, 2) };
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
				temp_position.add(new Move(position, new Position(0, 2), null));
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
				Position[] positionNeedCheck1 = new Position[] {
						new Position(7, 4), new Position(7, 5),
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
				temp_position.add(new Move(position, new Position(7, 6), null));
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
				Position[] positionNeedCheck1 = new Position[] {
						new Position(7, 4), new Position(7, 3),
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
				temp_position.add(new Move(position, new Position(7, 2), null));
			}
		}

	}

	private void addPossiBishopMove(Color bishopcolor, Position position,
			State temp_result, ArrayList<Move> temp_position) {
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
				temp_position
						.add(new Move(position, new Position(i3, j3), null));
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
					temp_position.add(new Move(position, new Position(i3, j3),
							null));
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
				temp_position
						.add(new Move(position, new Position(i4, j4), null));
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
					temp_position.add(new Move(position, new Position(i4, j4),
							null));
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
				temp_position
						.add(new Move(position, new Position(i7, j7), null));
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
					temp_position.add(new Move(position, new Position(i7, j7),
							null));
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
				temp_position
						.add(new Move(position, new Position(i8, j8), null));
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
					temp_position.add(new Move(position, new Position(i8, j8),
							null));
				}
			}
		}
	}

	private void addPossiRookMove(Color rookcolor, Position position,
			State temp_result, ArrayList<Move> temp_position) {
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
				temp_position.add(new Move(position, new Position(i1, col),
						null));
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
					temp_position.add(new Move(position, new Position(i1, col),
							null));
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
				temp_position.add(new Move(position, new Position(i2, col),
						null));
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
					temp_position.add(new Move(position, new Position(i2, col),
							null));
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
				temp_position.add(new Move(position, new Position(row, j5),
						null));
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
					temp_position.add(new Move(position, new Position(row, j5),
							null));
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
				temp_position.add(new Move(position, new Position(row, j6),
						null));
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
					temp_position.add(new Move(position, new Position(row, j6),
							null));
				}
			}
		}

	}

	private void addPossiQueenMove(Color queencolor, Position position,
			State temp_result, ArrayList<Move> temp_position) {
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
				temp_position.add(new Move(position, new Position(i1, col),
						null));
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
					temp_position.add(new Move(position, new Position(i1, col),
							null));
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
				temp_position.add(new Move(position, new Position(i2, col),
						null));
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
					temp_position.add(new Move(position, new Position(i2, col),
							null));
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
				temp_position
						.add(new Move(position, new Position(i3, j3), null));
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
					temp_position.add(new Move(position, new Position(i3, j3),
							null));
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
				temp_position
						.add(new Move(position, new Position(i4, j4), null));
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
					temp_position.add(new Move(position, new Position(i4, j4),
							null));
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
				temp_position.add(new Move(position, new Position(row, j5),
						null));
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
					temp_position.add(new Move(position, new Position(row, j5),
							null));
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
				temp_position.add(new Move(position, new Position(row, j6),
						null));
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
					temp_position.add(new Move(position, new Position(row, j6),
							null));
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
				temp_position
						.add(new Move(position, new Position(i7, j7), null));
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
					temp_position.add(new Move(position, new Position(i7, j7),
							null));
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
				temp_position
						.add(new Move(position, new Position(i8, j8), null));
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
					temp_position.add(new Move(position, new Position(i8, j8),
							null));
				}
			}
		}
	}

	private void addPossiKnightMove(Color knightcolor, Position position,
			State temp_result, ArrayList<Move> temp_position) {
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
						temp_position.add(new Move(position,
								possiKnightPosition[l], null));
					}
				}
			}
		}
	}

	private void addPossiPawnMove(Color pawncolor, Position position,
			State temp_result, ArrayList<Move> temp_position) {
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
							temp_position.add(new Move(position, new Position(
									row + 2, col), null));
						}
					}
				}
				if (temp_result.getPiece(row + 1, col) == null) {
					State temp = temp_result.copy();
					temp.setPiece(row, col, null);
					temp.setPiece(row + 1, col, temp_result.getPiece(position));
					if (!isUnderCheck(pawncolor, temp, kingPosition)) {
						if ((row + 1) == 7) {
							temp_position.add(new Move(position, new Position(
									row + 1, col), QUEEN));
							temp_position.add(new Move(position, new Position(
									row + 1, col), BISHOP));
							temp_position.add(new Move(position, new Position(
									row + 1, col), KNIGHT));
							temp_position.add(new Move(position, new Position(
									row + 1, col), ROOK));
						} else
							temp_position.add(new Move(position, new Position(
									row + 1, col), null));

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
							if ((row + 1) == 7) {

								temp_position.add(new Move(position,
										new Position(row + 1, col + 1), QUEEN));
								temp_position
										.add(new Move(position, new Position(
												row + 1, col + 1), BISHOP));
								temp_position
										.add(new Move(position, new Position(
												row + 1, col + 1), KNIGHT));
								temp_position.add(new Move(position,
										new Position(row + 1, col + 1), ROOK));
							} else
								temp_position.add(new Move(position,
										new Position(row + 1, col + 1), null));
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
							temp_position.add(new Move(position, new Position(
									row + 1, col + 1), null));
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
							if ((row + 1) == 7) {
								temp_position.add(new Move(position,
										new Position(row + 1, col - 1), QUEEN));
								temp_position
										.add(new Move(position, new Position(
												row + 1, col - 1), BISHOP));
								temp_position
										.add(new Move(position, new Position(
												row + 1, col - 1), KNIGHT));
								temp_position.add(new Move(position,
										new Position(row + 1, col - 1), ROOK));
							} else
								temp_position.add(new Move(position,
										new Position(row + 1, col - 1), null));
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
							temp_position.add(new Move(position, new Position(
									row + 1, col - 1), null));
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
							temp_position.add(new Move(position, new Position(
									row - 2, col), null));
						}
					}
				}
				if (temp_result.getPiece(row - 1, col) == null) {
					State temp = temp_result.copy();
					temp.setPiece(row, col, null);
					temp.setPiece(row - 1, col, temp_result.getPiece(position));
					if (!isUnderCheck(pawncolor, temp, kingPosition)) {
						if ((row - 1) == 0) {
							temp_position.add(new Move(position, new Position(
									row - 1, col), QUEEN));
							temp_position.add(new Move(position, new Position(
									row - 1, col), BISHOP));
							temp_position.add(new Move(position, new Position(
									row - 1, col), KNIGHT));
							temp_position.add(new Move(position, new Position(
									row - 1, col), ROOK));
						} else
							temp_position.add(new Move(position, new Position(
									row - 1, col), null));
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
							if ((row - 1) == 0) {
								temp_position.add(new Move(position,
										new Position(row - 1, col + 1), QUEEN));
								temp_position
										.add(new Move(position, new Position(
												row - 1, col + 1), BISHOP));
								temp_position
										.add(new Move(position, new Position(
												row - 1, col + 1), KNIGHT));
								temp_position.add(new Move(position,
										new Position(row - 1, col + 1), ROOK));
							} else
								temp_position.add(new Move(position,
										new Position(row - 1, col + 1), null));
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
							temp_position.add(new Move(position, new Position(
									row - 1, col + 1), null));
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

							if ((row - 1) == 0) {
								temp_position.add(new Move(position,
										new Position(row - 1, col - 1), QUEEN));
								temp_position
										.add(new Move(position, new Position(
												row - 1, col - 1), BISHOP));
								temp_position
										.add(new Move(position, new Position(
												row - 1, col - 1), KNIGHT));
								temp_position.add(new Move(position,
										new Position(row - 1, col - 1), ROOK));
							} else
								temp_position.add(new Move(position,
										new Position(row - 1, col - 1), null));
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
							temp_position.add(new Move(position, new Position(
									row - 1, col - 1), null));
						}

					}
				}

			}

		}

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

}