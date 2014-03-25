package org.shitianren.hw3;

import java.util.Set;

import org.shared.chess.Color;
import org.shared.chess.GameResult;
import org.shared.chess.GameResultReason;
import org.shared.chess.Move;
import org.shared.chess.Piece;
import org.shared.chess.PieceKind;
import org.shared.chess.Position;
import org.shared.chess.State;
import org.shitianren.hw2_5.StateExplorerImpl;
import org.shitianren.hw3.Presenter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class PresenterTest {
        Presenter presenter;
        Presenter.View view;

        @Before
        public void setup() {
                presenter = new Presenter();
                view = Mockito.mock(Presenter.View.class);
                presenter.setView(view);
        }

        @Test
        public void testSetState() {
                State state = new State();
                presenter.setState(state);
                Mockito.verify(view).setWhoseTurn(Color.WHITE);
                Mockito.verify(view).setPiece(0, 7,
                                new Piece(Color.WHITE, PieceKind.ROOK));
                Mockito.verify(view).setPiece(4, 4, null);
        }

        @Test
        public void testShowPossibleStartPosition() {
                State state = new State();
                presenter.setStateVar(state);
                presenter.showPossibleStartPosition();
                Mockito.verify(view).clearHighlighted();

                StateExplorerImpl stateExplorer = new StateExplorerImpl();

                Set<Position> possibleStartPosition = stateExplorer
                                .getPossibleStartPositions(state);
                if (!possibleStartPosition.isEmpty()) {
                        for (Position start : possibleStartPosition) {
                                Mockito.verify(view).setHighlighted(start.getRow(),
                                                start.getCol(), true);
                        }
                }
        }

        @Test
        public void testClickOnInvalidPiece() {
                State state = new State();
                presenter.setStateVar(state);
                presenter.clickOn(0, 0);
                Mockito.verify(view).clearHighlighted();
                Mockito.verify(view).removePromotion();
                StateExplorerImpl stateExplorer = new StateExplorerImpl();

                Set<Position> possibleStartPosition = stateExplorer
                                .getPossibleStartPositions(state);
                if (!possibleStartPosition.isEmpty()) {
                        for (Position start : possibleStartPosition) {
                                Mockito.verify(view).setHighlighted(start.getRow(),
                                                start.getCol(), true);
                        }
                }
        }

        @Test
        public void testClickOnValidPiece() {
                State state = new State();
                presenter.setStateVar(state);
                presenter.clickOn(0, 1);
                StateExplorerImpl stateExplorer = new StateExplorerImpl();
                Set<Move> possibleMoveFromPosition = stateExplorer
                                .getPossibleMovesFromPosition(state, new Position(0, 1));
                Mockito.verify(view).clearHighlighted();
                for (Move p : possibleMoveFromPosition) {
                        Mockito.verify(view).setHighlighted(p.getTo().getRow(),
                                        p.getTo().getCol(), true);
                }
        }
        @Test
        public void testGameResultWithCheckmate(){
                State state = new State();
                state.setGameResult(new GameResult(Color.WHITE,GameResultReason.CHECKMATE));
                presenter.setState(state);
                
                Mockito.verify(view).setGameResult(state.getGameResult());
                Mockito.verify(view).setPiece(0, 7,
                                new Piece(Color.WHITE, PieceKind.ROOK));
                Mockito.verify(view).setPiece(4, 4, null);
                Mockito.verify(view).setRestartButton();
        }
        
        @Test
        public void testGameResultWithStalemate(){
                State state = new State();
                state.setGameResult(new GameResult(null,GameResultReason.STALEMATE));
                presenter.setState(state);
                
                Mockito.verify(view).setGameResult(state.getGameResult());
                Mockito.verify(view).setPiece(0, 7,
                                new Piece(Color.WHITE, PieceKind.ROOK));
                Mockito.verify(view).setPiece(4, 4, null);
                Mockito.verify(view).setRestartButton();        
        }
        
        @Test
        public void testGameResultWithFiftyMoveRule(){
                State state = new State();
                state.setGameResult(new GameResult(null,GameResultReason.FIFTY_MOVE_RULE));
                presenter.setState(state);
                
                Mockito.verify(view).setGameResult(state.getGameResult());
                Mockito.verify(view).setPiece(0, 7,
                                new Piece(Color.WHITE, PieceKind.ROOK));
                Mockito.verify(view).setPiece(4, 4, null);
                Mockito.verify(view).setRestartButton();        
        }
        
        @Test
        public void testShowPossibleStartAfterGameEnd(){
                State state = new State();
                state.setGameResult(new GameResult(null,GameResultReason.STALEMATE));
                presenter.setStateVar(state);
                presenter.showPossibleStartPosition();
                Mockito.verify(view).clearHighlighted();
        }
        
        @Test
        public void testClickOnKnightShowMoveToPosition(){
                State state = new State();
                presenter.setStateVar(state);
                presenter.clickOn(0, 1);
                
                Mockito.verify(view).clearHighlighted();
                Mockito.verify(view).setHighlighted(2,0,true);          
                Mockito.verify(view).setHighlighted(2,2,true);          
        }
        
        @Test
        public void testClickOnPawnShowMoveToPosition(){
                State state = new State();
                presenter.setStateVar(state);
                presenter.clickOn(1, 0);
                
                Mockito.verify(view).clearHighlighted();
                Mockito.verify(view).setHighlighted(2,0,true);          
                Mockito.verify(view).setHighlighted(3,0,true);                  
        }
        
        
        
        
        

}