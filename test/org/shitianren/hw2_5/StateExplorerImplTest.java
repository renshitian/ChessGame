package org.shitianren.hw2_5;

import static org.junit.Assert.assertEquals;
import static org.shared.chess.Color.BLACK;
import static org.shared.chess.Color.WHITE;
import static org.shared.chess.PieceKind.KING;
import static org.shared.chess.PieceKind.KNIGHT;
import static org.shared.chess.PieceKind.PAWN;
import static org.shared.chess.PieceKind.ROOK;
import static org.shared.chess.PieceKind.QUEEN;
import static org.shared.chess.PieceKind.BISHOP;

import java.util.Set;
import org.shitianren.hw2_5.StateExplorerImpl;
import org.junit.Test;
import org.shared.chess.AbstractStateExplorerAllTest;
import org.shared.chess.AbstractStateExplorerTest;
import org.shared.chess.Color;
import org.shared.chess.Move;
import org.shared.chess.Piece;
import org.shared.chess.PieceKind;
import org.shared.chess.Position;
import org.shared.chess.State;
import org.shared.chess.StateExplorer;

import com.google.common.collect.Sets;

public class StateExplorerImplTest extends AbstractStateExplorerAllTest {
        @Override
        public StateExplorer getStateExplorer() {
                return new StateExplorerImpl();
        }

        /*
         * Begin Tests by Yoav Zibin <yoav.zibin@gmail.com>
         */
        @Test
        public void testGetPossibleStartPositions_InitState() {
                Set<Position> expectedPositions = Sets.newHashSet();
                // pawn positions
                for (int c = 0; c < 8; c++)
                        expectedPositions.add(new Position(1, c));
                // knight positions
                expectedPositions.add(new Position(0, 1));
                expectedPositions.add(new Position(0, 6));
                assertEquals(expectedPositions,
                                stateExplorer.getPossibleStartPositions(start));
        }

        @Test
        public void testGetPossibleMoves_InitState() {
                Set<Move> expectedMoves = Sets.newHashSet();
                // pawn moves
                for (int c = 0; c < 8; c++) {
                        expectedMoves.add(new Move(new Position(1, c), new Position(2, c),
                                        null));
                        expectedMoves.add(new Move(new Position(1, c), new Position(3, c),
                                        null));
                }
                // knight moves
                expectedMoves
                                .add(new Move(new Position(0, 1), new Position(2, 0), null));
                expectedMoves
                                .add(new Move(new Position(0, 1), new Position(2, 2), null));
                expectedMoves
                                .add(new Move(new Position(0, 6), new Position(2, 5), null));
                expectedMoves
                                .add(new Move(new Position(0, 6), new Position(2, 7), null));
                assertEquals(expectedMoves, stateExplorer.getPossibleMoves(start));
        }

        @Test
        public void testGetPossibleMovesFromPosition_InitStateForLeftKnight() {
                Set<Move> expectedMoves = Sets.newHashSet();
                // knight moves
                expectedMoves
                                .add(new Move(new Position(0, 1), new Position(2, 0), null));
                expectedMoves
                                .add(new Move(new Position(0, 1), new Position(2, 2), null));
                assertEquals(expectedMoves, stateExplorer.getPossibleMovesFromPosition(
                                start, new Position(0, 1)));
        }

        @Test
        public void testGetPossibleMovesFromPosition_Promotion() {
                start.setPiece(new Position(1, 0), null);
                start.setPiece(new Position(6, 0), new Piece(Color.WHITE,
                                PieceKind.PAWN));

                Set<Move> expectedMoves = Sets.newHashSet();
                // promotion moves
                expectedMoves.add(new Move(new Position(6, 0), new Position(7, 1),
                                PieceKind.BISHOP));
                expectedMoves.add(new Move(new Position(6, 0), new Position(7, 1),
                                PieceKind.KNIGHT));
                expectedMoves.add(new Move(new Position(6, 0), new Position(7, 1),
                                PieceKind.ROOK));
                expectedMoves.add(new Move(new Position(6, 0), new Position(7, 1),
                                PieceKind.QUEEN));
                assertEquals(expectedMoves, stateExplorer.getPossibleMovesFromPosition(
                                start, new Position(6, 0)));
        }

        /*
         * End Tests by Yoav Zibin <yoav.zibin@gmail.com>
         */

        /*
         * Begin Tests by Shitian Ren <renshitian@gmail.com>
         */
        @Test
        public void testGetAllPossibleMoves() {
                State before = new State(BLACK, new Piece[8][8], new boolean[] { false,
                                false }, new boolean[] { false, false }, null, 0, null);

                before.setPiece(0, 4, new Piece(WHITE, KING));
                before.setPiece(7, 4, new Piece(BLACK, KING));
                before.setPiece(4, 3, new Piece(BLACK, KNIGHT));
                before.setPiece(3, 3, new Piece(WHITE, PAWN));
                before.setPiece(3, 4, new Piece(BLACK, PAWN));
                before.setPiece(4, 4, new Piece(WHITE, PAWN));

                Set<Move> expectedMoves = Sets.newHashSet();
                expectedMoves
                                .add(new Move(new Position(7, 4), new Position(7, 3), null));
                expectedMoves
                                .add(new Move(new Position(7, 4), new Position(7, 5), null));
                expectedMoves
                                .add(new Move(new Position(7, 4), new Position(6, 3), null));
                expectedMoves
                                .add(new Move(new Position(7, 4), new Position(6, 4), null));
                expectedMoves
                                .add(new Move(new Position(7, 4), new Position(6, 5), null));
                expectedMoves
                                .add(new Move(new Position(4, 3), new Position(6, 2), null));
                expectedMoves
                                .add(new Move(new Position(4, 3), new Position(5, 1), null));
                expectedMoves
                                .add(new Move(new Position(4, 3), new Position(3, 1), null));
                expectedMoves
                                .add(new Move(new Position(4, 3), new Position(2, 2), null));
                expectedMoves
                                .add(new Move(new Position(4, 3), new Position(2, 4), null));
                expectedMoves
                                .add(new Move(new Position(4, 3), new Position(3, 5), null));
                expectedMoves
                                .add(new Move(new Position(4, 3), new Position(5, 5), null));
                expectedMoves
                                .add(new Move(new Position(4, 3), new Position(6, 4), null));
                expectedMoves
                                .add(new Move(new Position(3, 4), new Position(2, 4), null));

                assertEquals(expectedMoves, stateExplorer.getPossibleMoves(before));

        }

        @Test
        public void testGetAllPossibleMoves_MovementLeads2Check() {
                State before = new State(WHITE, new Piece[8][8], new boolean[] { false,
                                false }, new boolean[] { false, false }, null, 0, null);

                before.setPiece(0, 3, new Piece(WHITE, KING));
                before.setPiece(0, 4, new Piece(WHITE, ROOK));
                before.setPiece(1, 5, new Piece(BLACK, ROOK));
                before.setPiece(2, 4, new Piece(BLACK, PAWN));
                before.setPiece(7, 4, new Piece(BLACK, KING));

                Set<Move> expectedMoves = Sets.newHashSet();
                expectedMoves
                                .add(new Move(new Position(0, 3), new Position(0, 2), null));
                expectedMoves
                                .add(new Move(new Position(0, 4), new Position(1, 4), null));
                expectedMoves
                                .add(new Move(new Position(0, 4), new Position(2, 4), null));
                expectedMoves
                                .add(new Move(new Position(0, 4), new Position(0, 5), null));
                expectedMoves
                                .add(new Move(new Position(0, 4), new Position(0, 6), null));
                expectedMoves
                                .add(new Move(new Position(0, 4), new Position(0, 7), null));

                assertEquals(expectedMoves, stateExplorer.getPossibleMoves(before));
        }

        @Test
        public void testGetAllPossibleMoves_UnderCheck_Move2Defend() {
                State before = new State(WHITE, new Piece[8][8], new boolean[] { false,
                                false }, new boolean[] { false, false }, null, 0, null);

                before.setPiece(0, 0, new Piece(WHITE, KING));
                before.setPiece(1, 1, new Piece(BLACK, QUEEN));
                before.setPiece(1, 5, new Piece(WHITE, QUEEN));
                before.setPiece(7, 4, new Piece(BLACK, KING));

                Set<Move> expectedMoves = Sets.newHashSet();
                expectedMoves
                                .add(new Move(new Position(1, 5), new Position(1, 1), null));
                expectedMoves
                                .add(new Move(new Position(0, 0), new Position(1, 1), null));

                assertEquals(expectedMoves, stateExplorer.getPossibleMoves(before));

        }

        @Test
        public void testGetAllPossibleMoves_CastleKingSide() {
                State before = new State(WHITE, new Piece[8][8], new boolean[] { false,
                                false }, new boolean[] { false, false }, null, 0, null);

                before.setPiece(1, 0, new Piece(BLACK, ROOK));
                before.setPiece(0, 4, new Piece(WHITE, KING));
                before.setPiece(0, 7, new Piece(WHITE, ROOK));
                before.setPiece(2, 7, new Piece(BLACK, PAWN));
                before.setPiece(7, 3, new Piece(BLACK, KING));

                before.setCanCastleKingSide(WHITE, true);
                Set<Move> expectedMoves = Sets.newHashSet();

                expectedMoves
                                .add(new Move(new Position(0, 4), new Position(0, 3), null));
                expectedMoves
                                .add(new Move(new Position(0, 4), new Position(0, 5), null));
                expectedMoves
                                .add(new Move(new Position(0, 4), new Position(0, 6), null));
                expectedMoves
                                .add(new Move(new Position(0, 7), new Position(0, 6), null));
                expectedMoves
                                .add(new Move(new Position(0, 7), new Position(0, 5), null));
                expectedMoves
                                .add(new Move(new Position(0, 7), new Position(1, 7), null));
                expectedMoves
                                .add(new Move(new Position(0, 7), new Position(2, 7), null));

                assertEquals(expectedMoves, stateExplorer.getPossibleMoves(before));
                Set<Move> expectedMoves1 = Sets.newHashSet();

                before.setPiece(5, 6, new Piece(BLACK, ROOK));
                expectedMoves1.add(new Move(new Position(0, 4), new Position(0, 3),
                                null));
                expectedMoves1.add(new Move(new Position(0, 4), new Position(0, 5),
                                null));
                expectedMoves1.add(new Move(new Position(0, 7), new Position(0, 6),
                                null));
                expectedMoves1.add(new Move(new Position(0, 7), new Position(0, 5),
                                null));
                expectedMoves1.add(new Move(new Position(0, 7), new Position(1, 7),
                                null));
                expectedMoves1.add(new Move(new Position(0, 7), new Position(2, 7),
                                null));
                assertEquals(expectedMoves1, stateExplorer.getPossibleMoves(before));

        }

        @Test
        public void testGetAllPossibleMoves_CastleQueenSide() {
                State before = new State(WHITE, new Piece[8][8], new boolean[] { false,
                                false }, new boolean[] { false, false }, null, 0, null);

                before.setPiece(1, 0, new Piece(BLACK, ROOK));
                before.setPiece(0, 4, new Piece(WHITE, KING));
                before.setPiece(0, 0, new Piece(WHITE, ROOK));
                before.setPiece(2, 5, new Piece(BLACK, ROOK));
                before.setPiece(7, 3, new Piece(BLACK, KING));

                before.setCanCastleQueenSide(WHITE, true);
                Set<Move> expectedMoves = Sets.newHashSet();

                expectedMoves
                                .add(new Move(new Position(0, 4), new Position(0, 3), null));
                expectedMoves
                                .add(new Move(new Position(0, 4), new Position(0, 2), null));
                expectedMoves
                                .add(new Move(new Position(0, 0), new Position(0, 1), null));
                expectedMoves
                                .add(new Move(new Position(0, 0), new Position(0, 2), null));
                expectedMoves
                                .add(new Move(new Position(0, 0), new Position(0, 3), null));
                expectedMoves
                                .add(new Move(new Position(0, 0), new Position(1, 0), null));

                assertEquals(expectedMoves, stateExplorer.getPossibleMoves(before));

                before.setPiece(3, 2, new Piece(BLACK, QUEEN));

                Set<Move> expectedMoves1 = Sets.newHashSet();

                expectedMoves1.add(new Move(new Position(0, 4), new Position(0, 3),
                                null));
                expectedMoves1.add(new Move(new Position(0, 0), new Position(0, 1),
                                null));
                expectedMoves1.add(new Move(new Position(0, 0), new Position(0, 2),
                                null));
                expectedMoves1.add(new Move(new Position(0, 0), new Position(0, 3),
                                null));
                expectedMoves1.add(new Move(new Position(0, 0), new Position(1, 0),
                                null));
                assertEquals(expectedMoves1, stateExplorer.getPossibleMoves(before));

        }

        @Test
        public void testGetPossibleStartPositions_NoMovement() {
                State before = new State(WHITE, new Piece[8][8], new boolean[] { false,
                                false }, new boolean[] { false, false }, null, 0, null);

                before.setPiece(0, 4, new Piece(WHITE, KING));
                before.setPiece(7, 4, new Piece(BLACK, KING));
                before.setPiece(4, 3, new Piece(BLACK, KNIGHT));
                before.setPiece(3, 3, new Piece(WHITE, PAWN));
                before.setPiece(3, 4, new Piece(BLACK, PAWN));
                before.setPiece(4, 4, new Piece(WHITE, PAWN));
                Set<Position> expectedPositions = Sets.newHashSet();

                expectedPositions.add(new Position(0, 4));
                expectedPositions.add(new Position(4, 4));

                assertEquals(expectedPositions,
                                stateExplorer.getPossibleStartPositions(before));

        }

        @Test
        public void testGetPossibleStartPositions_MoveLeads2Check() {
                State before = new State(WHITE, new Piece[8][8], new boolean[] { false,
                                false }, new boolean[] { false, false }, null, 0, null);

                before.setPiece(0, 4, new Piece(WHITE, KING));
                before.setPiece(7, 4, new Piece(BLACK, KING));
                before.setPiece(2, 4, new Piece(WHITE, BISHOP));
                before.setPiece(4, 4, new Piece(BLACK, QUEEN));

                Set<Position> expectedPositions = Sets.newHashSet();

                expectedPositions.add(new Position(0, 4));
                assertEquals(expectedPositions,
                                stateExplorer.getPossibleStartPositions(before));
        }

        @Test
        public void testGetPossibleMovesFromPosition_MovesLeads2Check() {
                State before = new State(WHITE, new Piece[8][8], new boolean[] { false,
                                false }, new boolean[] { false, false }, null, 0, null);

                before.setPiece(0, 4, new Piece(WHITE, KING));
                before.setPiece(7, 4, new Piece(BLACK, KING));
                before.setPiece(2, 4, new Piece(WHITE, BISHOP));
                before.setPiece(4, 4, new Piece(BLACK, QUEEN));
                Set<Position> expectedPositions = Sets.newHashSet();

                assertEquals(expectedPositions,
                                stateExplorer.getPossibleMovesFromPosition(before,
                                                new Position(2, 4)));

        }

        @Test
        public void testGetPosssibleMovesFromPosition_QueenSideCastle() {
                State before = new State(WHITE, new Piece[8][8], new boolean[] { false,
                                false }, new boolean[] { false, false }, null, 0, null);

                before.setPiece(1, 0, new Piece(BLACK, ROOK));
                before.setPiece(0, 4, new Piece(WHITE, KING));
                before.setPiece(0, 0, new Piece(WHITE, ROOK));
                before.setPiece(2, 5, new Piece(BLACK, ROOK));
                before.setPiece(7, 3, new Piece(BLACK, KING));

                before.setCanCastleQueenSide(WHITE, true);
                Set<Move> expectedMoves = Sets.newHashSet();

                expectedMoves
                                .add(new Move(new Position(0, 4), new Position(0, 3), null));
                expectedMoves
                                .add(new Move(new Position(0, 4), new Position(0, 2), null));

                assertEquals(expectedMoves, stateExplorer.getPossibleMovesFromPosition(
                                before, new Position(0, 4)));

                before.setPiece(3, 2, new Piece(BLACK, QUEEN));

                Set<Move> expectedMoves1 = Sets.newHashSet();

                expectedMoves1.add(new Move(new Position(0, 4), new Position(0, 3),
                                null));
        }

        @Test
        public void testGetPosssibleMovesFromPosition_KingSideCastle() {
                State before = new State(WHITE, new Piece[8][8], new boolean[] { false,
                                false }, new boolean[] { false, false }, null, 0, null);

                before.setPiece(1, 0, new Piece(BLACK, ROOK));
                before.setPiece(0, 4, new Piece(WHITE, KING));
                before.setPiece(0, 7, new Piece(WHITE, ROOK));
                before.setPiece(2, 7, new Piece(BLACK, PAWN));
                before.setPiece(7, 3, new Piece(BLACK, KING));

                before.setCanCastleKingSide(WHITE, true);
                Set<Move> expectedMoves = Sets.newHashSet();

                expectedMoves
                                .add(new Move(new Position(0, 4), new Position(0, 3), null));
                expectedMoves
                                .add(new Move(new Position(0, 4), new Position(0, 5), null));
                expectedMoves
                                .add(new Move(new Position(0, 4), new Position(0, 6), null));

                assertEquals(expectedMoves, stateExplorer.getPossibleMovesFromPosition(
                                before, new Position(0, 4)));

                Set<Move> expectedMoves1 = Sets.newHashSet();
                before.setPiece(5, 6, new Piece(BLACK, ROOK));
                expectedMoves1.add(new Move(new Position(0, 4), new Position(0, 3),
                                null));
                expectedMoves1.add(new Move(new Position(0, 4), new Position(0, 5),
                                null));

                assertEquals(expectedMoves1,
                                stateExplorer.getPossibleMovesFromPosition(before,
                                                new Position(0, 4)));

        }

        /*
         * End Tests by Shitian Ren <renshitian@gmail.com>
         */

}