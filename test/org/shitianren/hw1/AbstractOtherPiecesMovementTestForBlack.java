package org.shitianren.hw1;

import static org.junit.Assert.*;

import static org.shared.chess.Color.BLACK;
import static org.shared.chess.Color.WHITE;
import static org.shared.chess.PieceKind.KNIGHT;
import static org.shared.chess.PieceKind.KING;
import static org.shared.chess.PieceKind.PAWN;
import static org.shared.chess.PieceKind.BISHOP;
import static org.shared.chess.PieceKind.ROOK;
import static org.shared.chess.PieceKind.QUEEN;


import org.junit.Before;
import org.junit.Test;
import org.shared.chess.AbstractStateChangerTest;
import org.shared.chess.Color;
import org.shared.chess.GameResult;
import org.shared.chess.IllegalMove;
import org.shared.chess.Move;
import org.shared.chess.Piece;
import org.shared.chess.Position;
import org.shared.chess.State;

public abstract class AbstractOtherPiecesMovementTestForBlack extends AbstractStateChangerTest{
        
        @Test
        public void testBlackKnightLegalMovement()  {
                //create an initial board with several Pieces
                State before = new State(BLACK, new Piece[8][8], new boolean[]{false, false}, new boolean[]{false, false}, null,0, null);

                before.setPiece(0, 4, new Piece(WHITE,KING));
                before.setPiece(7,4, new Piece(BLACK,KING));
                before.setPiece(4,3, new Piece(BLACK,KNIGHT));
                before.setPiece(3, 3, new Piece(WHITE,PAWN));
                before.setPiece(3, 4, new Piece(BLACK,PAWN));
                before.setPiece(4,4,new Piece(WHITE,PAWN));

                Move move = new Move(new Position(4,3),new Position(2,4),null);

                //change the position of several Pieces to make it as expected
                State before1 = before.copy();
                State expected1 = before1.copy();
                
                expected1.setTurn(WHITE);
                expected1.setPiece(4, 3, null);
                expected1.setPiece(2,4,new Piece(BLACK,KNIGHT));
            expected1.setNumberOfMovesWithoutCaptureNorPawnMoved(1);
            stateChanger.makeMove(before1, move);
            assertEquals(before1,expected1);
            
            //test for movement of black knight to position occupied by one white pawn
            State before2 = before.copy();          
            before2.setPiece(3, 3, null);
            before2.setPiece(2,4,new Piece(WHITE,PAWN));
            State expected2 = before2.copy();
            
            expected2.setTurn(WHITE);
                expected2.setPiece(4, 3, null);
                expected2.setPiece(2,4,new Piece(BLACK,KNIGHT));
            expected2.setNumberOfMovesWithoutCaptureNorPawnMoved(0);
            stateChanger.makeMove(before2, move);
            assertEquals(before2,expected2);
                
            //test for movement of black knight to unoccupied position
                State before3 = before.copy();
                State expected3 = before3.copy();
                Move move2 = new Move(new Position(4,3),new Position(3,1),null);
                expected3.setTurn(WHITE);
                expected3.setPiece(4, 3, null);
                expected3.setPiece(3,1,new Piece(BLACK,KNIGHT));
            expected3.setNumberOfMovesWithoutCaptureNorPawnMoved(1);
            stateChanger.makeMove(before3, move2);
            assertEquals(before3,expected3);
            
        }
        
        
        //test for illegal movement of black knight to position occupied with same color piece 
        @Test(expected = IllegalMove.class)
        public void testBlackKnightMoveToBlackPiece() {
                Move move = new Move(new Position(4,3),new Position(2,4),null);
                State before = new State(BLACK, new Piece[8][8], new boolean[]{false, false}, new boolean[]{false, false}, null,0, null);

                before.setPiece(0, 4, new Piece(WHITE,KING));
                before.setPiece(7,4, new Piece(BLACK,KING));
                before.setPiece(4,3, new Piece(BLACK,KNIGHT));
                before.setPiece(3, 3, new Piece(WHITE,PAWN));
                before.setPiece(2, 4, new Piece(BLACK,PAWN));
                before.setPiece(4,4,new Piece(WHITE,PAWN));

                
                stateChanger.makeMove(before, move);
        }
        
        //test for illegal movement of black knight to a square unoccupied position
        @Test(expected = IllegalMove.class)
        public void testBlackKnightMoveSquare() {
                State before = new State(BLACK, new Piece[8][8], new boolean[]{false, false}, new boolean[]{false, false}, null,0, null);

                before.setPiece(0, 4, new Piece(WHITE,KING));
                before.setPiece(7,4, new Piece(BLACK,KING));
                before.setPiece(4,3, new Piece(BLACK,KNIGHT));
                before.setPiece(3, 3, new Piece(WHITE,PAWN));
                before.setPiece(3, 4, new Piece(BLACK,PAWN));
                before.setPiece(4,4,new Piece(WHITE,PAWN));
                
                Move move = new Move(new Position(4,3),new Position(5,2),null);
                
                stateChanger.makeMove(before, move);
        }
        
        
        //test for movement of black knight to a diagonal position
        @Test(expected = IllegalMove.class)
        public void testBlackKnightMoveDiagonal() {
                State before = new State(BLACK, new Piece[8][8], new boolean[]{false, false}, new boolean[]{false, false}, null,0, null);

                before.setPiece(0, 4, new Piece(WHITE,KING));
                before.setPiece(7,4, new Piece(BLACK,KING));
                before.setPiece(4,3, new Piece(BLACK,KNIGHT));
                before.setPiece(3, 3, new Piece(WHITE,PAWN));
                before.setPiece(3, 4, new Piece(BLACK,PAWN));
                before.setPiece(4,4,new Piece(WHITE,PAWN));
                Move move = new Move(new Position(4,3),new Position(2,1),null);
                
                stateChanger.makeMove(before, move);
        }
        
        
        //test for movement of black knight to a vertical position
        @Test(expected = IllegalMove.class)
        public void testBlackKnightMoveVertical() {
                State before = new State(BLACK, new Piece[8][8], new boolean[]{false, false}, new boolean[]{false, false}, null,0, null);

                before.setPiece(0, 4, new Piece(WHITE,KING));
                before.setPiece(7,4, new Piece(BLACK,KING));
                before.setPiece(4,3, new Piece(BLACK,KNIGHT));
                before.setPiece(3, 3, new Piece(WHITE,PAWN));
                before.setPiece(3, 4, new Piece(BLACK,PAWN));
                before.setPiece(4,4,new Piece(WHITE,PAWN));
                Move move = new Move(new Position(4,3),new Position(6,3),null);
                
                stateChanger.makeMove(before, move);
        }
        
        //test for movement of black knight to a horizontal position
        @Test(expected = IllegalMove.class)
        public void testBlackKnightMoveHorizontal() {
                State before = new State(BLACK, new Piece[8][8], new boolean[]{false, false}, new boolean[]{false, false}, null,0, null);

                before.setPiece(0, 4, new Piece(WHITE,KING));
                before.setPiece(7,4, new Piece(BLACK,KING));
                before.setPiece(4,3, new Piece(BLACK,KNIGHT));
                before.setPiece(3, 3, new Piece(WHITE,PAWN));
                before.setPiece(3, 4, new Piece(BLACK,PAWN));
                before.setPiece(4,4,new Piece(WHITE,PAWN));
                Move move = new Move(new Position(4,3),new Position(4,0),null);

                stateChanger.makeMove(before, move);
        }
        
        
        //test for movement of black bishop
        @Test
        public void testBlackBishopLegalMove() {
                //setup the state board
                State before = new State(BLACK, new Piece[8][8], new boolean[]{false, false}, new boolean[]{false, false}, null,0, null);

                before.setPiece(0,4, new Piece(WHITE,KING));
                before.setPiece(7,4, new Piece(BLACK,KING));
                before.setPiece(4,3, new Piece(BLACK,BISHOP));
                before.setPiece(2,1, new Piece(WHITE,PAWN));
                before.setPiece(7,6, new Piece(BLACK,KNIGHT));
                
                //test for movement of black bishop to unoccupied position
                State before1 = before.copy();
                State expected1 = before1.copy();
                Move move1 = new Move(new Position(4,3),new Position(6,1),null);
                
                
                expected1.setTurn(WHITE);
                expected1.setPiece(4,3, null);
                expected1.setPiece(6,1,new Piece(BLACK,BISHOP));
            expected1.setNumberOfMovesWithoutCaptureNorPawnMoved(1);
            stateChanger.makeMove(before1, move1);
            assertEquals(before1,expected1);
            
            //test for movement of black bishop to occupied position by white piece
                State before2 = before.copy();
                State expected2 = before2.copy();
                Move move2 = new Move(new Position(4,3),new Position(2,1),null);
                
                expected2.setTurn(WHITE);
                expected2.setPiece(4,3, null);
                expected2.setPiece(2,1,new Piece(BLACK,BISHOP));
            expected2.setNumberOfMovesWithoutCaptureNorPawnMoved(0);
            stateChanger.makeMove(before2, move2);
            assertEquals(before2,expected2);
            
            //test for movement of black biship to unoccupied position
                State before3 = before.copy();
                State expected3 = before3.copy();
                Move move3 = new Move(new Position(4,3),new Position(6,5),null);
                
                expected3.setTurn(WHITE);
                expected3.setPiece(4,3, null);
                expected3.setPiece(6,5,new Piece(BLACK,BISHOP));
            expected3.setNumberOfMovesWithoutCaptureNorPawnMoved(1);
            stateChanger.makeMove(before3, move3);
            assertEquals(before3,expected3);
        }
        
        //test for movement of black bishop to vertical position
        @Test(expected = IllegalMove.class)
        public void testBlackBishopMoveVertical(){
                Move move = new Move(new Position(4,3),new Position(2,3),null);
                State before = new State(BLACK, new Piece[8][8], new boolean[]{false, false}, new boolean[]{false, false}, null,0, null);

                before.setPiece(0,4, new Piece(WHITE,KING));
                before.setPiece(7,4, new Piece(BLACK,KING));
                before.setPiece(4,3, new Piece(BLACK,BISHOP));
                before.setPiece(2,1, new Piece(WHITE,PAWN));
                before.setPiece(7,6, new Piece(BLACK,KNIGHT));
        
                stateChanger.makeMove(before, move);            
        }
        
        //test for movement of black bishop to horizontal position
        @Test(expected = IllegalMove.class)
        public void testBlackBishopMoveHorizontal(){
                Move move = new Move(new Position(4,3),new Position(4,6),null);
                State before = new State(BLACK, new Piece[8][8], new boolean[]{false, false}, new boolean[]{false, false}, null,0, null);

                before.setPiece(0,4, new Piece(WHITE,KING));
                before.setPiece(7,4, new Piece(BLACK,KING));
                before.setPiece(4,3, new Piece(BLACK,BISHOP));
                before.setPiece(2,1, new Piece(WHITE,PAWN));
                before.setPiece(7,6, new Piece(BLACK,KNIGHT));
        
                stateChanger.makeMove(before, move);            
        }
        
        //test for movement of black bishop to non-diagonal position
        @Test(expected = IllegalMove.class)
        public void testBlackBishopMoveNotDiagonal(){
                Move move = new Move(new Position(4,3),new Position(6,6),null);
                State before = new State(BLACK, new Piece[8][8], new boolean[]{false, false}, new boolean[]{false, false}, null,0, null);

                before.setPiece(0,4, new Piece(WHITE,KING));
                before.setPiece(7,4, new Piece(BLACK,KING));
                before.setPiece(4,3, new Piece(BLACK,BISHOP));
                before.setPiece(2,1, new Piece(WHITE,PAWN));
                before.setPiece(7,6, new Piece(BLACK,KNIGHT));

                stateChanger.makeMove(before, move);            
        }
        
        //test for movement of black bishop over piece
        @Test(expected = IllegalMove.class)
        public void testBlackBishopMoveOverPiece(){
                Move move = new Move(new Position(4,3),new Position(1,0),null);
                State before = new State(BLACK, new Piece[8][8], new boolean[]{false, false}, new boolean[]{false, false}, null,0, null);

                before.setPiece(0,4, new Piece(WHITE,KING));
                before.setPiece(7,4, new Piece(BLACK,KING));
                before.setPiece(4,3, new Piece(BLACK,BISHOP));
                before.setPiece(2,1, new Piece(WHITE,PAWN));
                before.setPiece(7,6, new Piece(BLACK,KNIGHT));

                stateChanger.makeMove(before, move);            
        }
        
        
        //test for movement of black bishop to position occupied by piece with same color
        @Test(expected = IllegalMove.class)
        public void testBlackBishopMoveToPositionOfSameColor(){
                State before = new State(BLACK, new Piece[8][8], new boolean[]{false, false}, new boolean[]{false, false}, null,0, null);

                before.setPiece(0,4, new Piece(WHITE,KING));
                before.setPiece(7,4, new Piece(BLACK,KING));
                before.setPiece(4,3, new Piece(BLACK,BISHOP));
                before.setPiece(2,1, new Piece(WHITE,PAWN));
                before.setPiece(7,6, new Piece(BLACK,KNIGHT));
                Move move = new Move(new Position(4,3),new Position(7,6),null);

                stateChanger.makeMove(before, move);            
        }
        
        
        //test for legal movement of black rook
        @Test
        public void testBlackRookLegalMove(){
                State before = new State(BLACK, new Piece[8][8], new boolean[]{false, false}, new boolean[]{false, false}, null,0, null);
                before.setPiece(0,4,new Piece(WHITE,KING));
                before.setPiece(7, 4, new Piece(BLACK,KING));
                before.setPiece(4,3, new Piece(BLACK,ROOK));
                before.setPiece(3, 3, new Piece(BLACK,KNIGHT));
                before.setPiece(4,6,new Piece(WHITE,PAWN));
                
                
                //test for movement of black rook to unoccupied position
                State before1 = before.copy();
                State except1 = before1.copy();
                Move move1 = new Move(new Position(4,3),new Position(4,0),null);
                
                stateChanger.makeMove(before1, move1);
                except1.setPiece(4, 3, null);
                except1.setPiece(4, 0, new Piece(BLACK,ROOK));
                except1.setNumberOfMovesWithoutCaptureNorPawnMoved(1);
                assertEquals(except1,before1);
                
                //test for movement of black rook to position occupied by op piece
                State before2 = before.copy();
                State except2 = before2.copy();
                Move move2 = new Move(new Position(4,3),new Position(4,6),null);
                
                stateChanger.makeMove(before2, move2);
                except2.setPiece(4, 3, null);
                except2.setPiece(4, 6, new Piece(BLACK,ROOK));
                except2.setNumberOfMovesWithoutCaptureNorPawnMoved(0);
                assertEquals(except2,before2);
                
                                
        }
        
        //test for movement of black rook to position occupied by piece with same color
        @Test(expected = IllegalMove.class)
        public void testBlackRookMoveToPositionOfSameColor(){
                State before = new State(BLACK, new Piece[8][8], new boolean[]{false, false}, new boolean[]{false, false}, null,0, null);
                before.setPiece(0,4,new Piece(WHITE,KING));
                before.setPiece(7, 4, new Piece(BLACK,KING));
                before.setPiece(4,3, new Piece(BLACK,ROOK));
                before.setPiece(3, 3, new Piece(BLACK,KNIGHT));
                before.setPiece(4,6,new Piece(WHITE,PAWN));
                Move move = new Move(new Position(4,3),new Position(3,3),null);

                stateChanger.makeMove(before, move);            
        }
        
        //test for movement of black rook to non-vertical and non-horizontal position
        @Test(expected = IllegalMove.class)
        public void testBlackRookNotMoveVerticalNorHorizontal(){
                State before = new State(BLACK, new Piece[8][8], new boolean[]{false, false}, new boolean[]{false, false}, null,0, null);
                before.setPiece(0,4,new Piece(WHITE,KING));
                before.setPiece(7, 4, new Piece(BLACK,KING));
                before.setPiece(4,3, new Piece(BLACK,ROOK));
                before.setPiece(3, 3, new Piece(BLACK,KNIGHT));
                before.setPiece(4,6,new Piece(WHITE,PAWN));
                Move move = new Move(new Position(4,3),new Position(2,2),null);

                
                stateChanger.makeMove(before, move);            
        }
        
        //test for movement of black rook over op piece
        @Test(expected = IllegalMove.class)
        public void testBlackRookMoveOverOpPiece(){
                State before = new State(BLACK, new Piece[8][8], new boolean[]{false, false}, new boolean[]{false, false}, null,0, null);
                before.setPiece(0,4,new Piece(WHITE,KING));
                before.setPiece(7, 4, new Piece(BLACK,KING));
                before.setPiece(4,3, new Piece(BLACK,ROOK));
                before.setPiece(3, 3, new Piece(BLACK,KNIGHT));
                before.setPiece(4,6,new Piece(WHITE,PAWN));
                Move move = new Move(new Position(4,3),new Position(4,7),null);
                
                stateChanger.makeMove(before, move);            
        }
        
        //test for movement of black rook over piece with same color
        @Test(expected = IllegalMove.class)
        public void testBlackRookMoveOverSamePiece(){
                State before = new State(BLACK, new Piece[8][8], new boolean[]{false, false}, new boolean[]{false, false}, null,0, null);
                before.setPiece(0,4,new Piece(WHITE,KING));
                before.setPiece(7, 4, new Piece(BLACK,KING));
                before.setPiece(4,3, new Piece(BLACK,ROOK));
                before.setPiece(3, 3, new Piece(BLACK,KNIGHT));
                before.setPiece(4,6,new Piece(WHITE,PAWN));
                Move move = new Move(new Position(4,3),new Position(1,3),null);

                
                stateChanger.makeMove(before, move);            
        }
        
        
        
        //test for legal movement of black queen 
        @Test
        public void testBlackQueenLegalMove(){
                State before = new State(BLACK, new Piece[8][8], new boolean[]{false, false}, new boolean[]{false, false}, null,0, null);
                
                before.setPiece(0, 4, new Piece(WHITE,KING));
                before.setPiece(7, 4,new Piece(BLACK,KING));
                before.setPiece(3, 3,new Piece(BLACK,QUEEN));
                before.setPiece(5, 1,new Piece(BLACK,PAWN));
                before.setPiece(3, 7,new Piece(WHITE,PAWN));            
                //test for movement of black queen to unoccupied vertical position
                State before1 = before.copy();
                State expected1 = before1.copy();
                Move move1 = new Move(new Position(3,3),new Position(6,3),null);
                
                expected1.setPiece(3, 3,null);
                expected1.setPiece(6, 3, new Piece(BLACK,QUEEN));
                expected1.setNumberOfMovesWithoutCaptureNorPawnMoved(1);
                stateChanger.makeMove(before1, move1);
                assertEquals(before1,expected1);
                
                
                //test for movement of black queen to unoccupied horizontal position
                State before2 = before.copy();
                State expected2 = before2.copy();
                Move move2 = new Move(new Position(3,3),new Position(3,0),null);
                
                expected2.setPiece(3, 3,null);
                expected2.setPiece(3, 0, new Piece(BLACK,QUEEN));
                expected2.setNumberOfMovesWithoutCaptureNorPawnMoved(1);
                stateChanger.makeMove(before2, move2);
                assertEquals(before2,expected2);
                
                
                //test for movement of black queen to unoccupied diagonal position
                State before3 = before.copy();
                State expected3 = before3.copy();
                Move move3 = new Move(new Position(3,3),new Position(4,2),null);
                
                expected3.setPiece(3, 3,null);
                expected3.setPiece(4, 2, new Piece(BLACK,QUEEN));
                expected3.setNumberOfMovesWithoutCaptureNorPawnMoved(1);
                stateChanger.makeMove(before3, move3);
                assertEquals(before3,expected3);
                
                
                //test for movement of black queen to unoccupied diagonal position
                State before4 = before.copy();
                State expected4 = before4.copy();
                Move move4 = new Move(new Position(3,3),new Position(1,1),null);
                
                expected4.setPiece(3, 3,null);
                expected4.setPiece(1, 1, new Piece(BLACK,QUEEN));
                expected4.setNumberOfMovesWithoutCaptureNorPawnMoved(1);
                stateChanger.makeMove(before4, move4);
                assertEquals(before4,expected4);

                
                //test for movement of black queen to occupied position with op piece
                State before5 = before.copy();
                State expected5 = before5.copy();
                Move move5 = new Move(new Position(3,3),new Position(3,7),null);
                
                expected5.setPiece(3, 3,null);
                expected5.setPiece(3, 7, new Piece(BLACK,QUEEN));
                expected5.setNumberOfMovesWithoutCaptureNorPawnMoved(0);
                stateChanger.makeMove(before5, move5);
                assertEquals(before5,expected5);
        }
        
        
        //test for movement of black queen over piece
        @Test(expected = IllegalMove.class)
        public void testBlackQueenMoveOverPiece(){
                State before = new State(BLACK, new Piece[8][8], new boolean[]{false, false}, new boolean[]{false, false}, null,0, null);
                
                before.setPiece(0, 4, new Piece(WHITE,KING));
                before.setPiece(7, 4,new Piece(BLACK,KING));
                before.setPiece(3, 3,new Piece(BLACK,QUEEN));
                before.setPiece(5, 1,new Piece(BLACK,PAWN));
                before.setPiece(3, 7,new Piece(WHITE,PAWN));
                Move move = new Move(new Position(3,3),new Position(6,0),null);

                stateChanger.makeMove(before, move);            
        }       
        
        //test for movement of black queen to illegal direction
        @Test(expected = IllegalMove.class)
        public void testBlackQueenMoveWrongDirection(){
                State before = new State(BLACK, new Piece[8][8], new boolean[]{false, false}, new boolean[]{false, false}, null,0, null);
                
                before.setPiece(0, 4, new Piece(WHITE,KING));
                before.setPiece(7, 4,new Piece(BLACK,KING));
                before.setPiece(3, 3,new Piece(BLACK,QUEEN));
                before.setPiece(5, 1,new Piece(BLACK,PAWN));
                before.setPiece(3, 7,new Piece(WHITE,PAWN));
                Move move = new Move(new Position(3,3),new Position(4,7),null);
        
                stateChanger.makeMove(before, move);            
        }
        
}