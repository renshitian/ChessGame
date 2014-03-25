package org.shitianren.hw3;

import java.util.Map;
import java.util.Map.Entry;


import org.shitianren.hw5.MoveAnimation;
import org.shared.chess.Color;
import org.shared.chess.GameResult;
import org.shared.chess.Move;
import org.shared.chess.Piece;
import org.shared.chess.Position;
import org.shared.chess.State;

import org.shitianren.hw3.Presenter.View;
import org.shitianren.hw8.ChessMessage;
import org.shitianren.hw10.PieceDragController;
import org.shitianren.hw10.Callback;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.SimpleDropController;
import com.google.gwt.appengine.channel.client.Channel;
import com.google.gwt.appengine.channel.client.ChannelFactory;
import com.google.gwt.appengine.channel.client.ChannelFactoryImpl;
import com.google.gwt.appengine.channel.client.SocketListener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasDragOverHandlers;
import com.google.gwt.event.dom.client.HasDragStartHandlers;
import com.google.gwt.event.dom.client.HasDropHandlers;
import com.google.gwt.media.client.Audio;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Graphics extends Composite implements View {
	private static GameImages gameImages = GWT.create(GameImages.class);
	private static GraphicsUiBinder uiBinder = GWT
			.create(GraphicsUiBinder.class);

	interface GraphicsUiBinder extends UiBinder<Widget, Graphics> {
	}

	@UiField
	VerticalPanel connectPanel;
	@UiField
	Label waitStatus;
	@UiField
	SimplePanel simplepanelGameEnd;
	@UiField
	SimplePanel simplepanelCapture;
	@UiField
	SimplePanel simplepanelClick;
	@UiField
	GameCss css;
	@UiField
	Label gameStatus;
	@UiField
	Grid gameGrid;
	@UiField
	Label promotionStatus;
	@UiField
	Grid promotionGrid;
	@UiField
	Button restartButton;
	@UiField
	Button saveButton;
	@UiField
	Button loadButton;
	@UiField
	ListBox listBox;
	@UiField
	TextBox nameText;
	@UiField
	Label saveStatus;
	@UiField
	Label loadStatus;
	@UiField
	VerticalPanel autoMatchPanel;
	@UiField
	VerticalPanel invitePanel;
	@UiField
	VerticalPanel gameDisplayPanel;
	@UiField
	Button autoMatchButton;
	@UiField
	TextBox invitePlayerText;
	@UiField
	Button inviteButton;
	@UiField
	ListBox matchList;
	@UiField
	Label gameDisStatus;
	@UiField
	Button deleteGameButton;
	@UiField
	Button AIButton;
	@UiField
	AbsolutePanel gameBoard;
	
	private Image[][] board = new Image[8][8];
	private Image[][] promotionBoard = new Image[2][2];
	private FlowPanel[][] containers = new FlowPanel[8][8];
	//private String disp = "Choose match to display";
	//private ChessMessage chessMessage = GWT.create(ChessMessage.class);
	private PickupDragController[][] dragControllers = new PickupDragController[8][8];

	public Graphics() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void initialGamePanel(){
		waitStatus.setText("Connecting to server. Please wait...");
		autoMatchButton.setText("Auto Match");
		AIButton.setText("Play against AI");
		inviteButton.setText("Invite");
		gameDisStatus.setText("Choose to display");
		saveStatus.setText("Type in to save");
		loadStatus.setText("Choose to load");
		deleteGameButton.setText("Delete");
		saveButton.setText("Save");
		loadButton.setText("Load");
		restartButton.setText("Start New Game");
		showWaitStatus();
		//hideWaitStatus();
		showAutoMatchPanel();
		showInvitePlayer();
		showGameDisplayPanel();
		gameGrid.resize(8, 8);
		gameGrid.setCellPadding(0);
		gameGrid.setCellSpacing(0);
		gameGrid.setBorderWidth(0);

		promotionGrid.resize(2, 2);
		promotionGrid.setCellPadding(0);
		promotionGrid.setCellSpacing(0);
		promotionGrid.setBorderWidth(0);

		
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				final Image image = new Image();
				FlowPanel imageContainer = new FlowPanel();
				board[row][col] = image;
				image.setWidth("100%");				
				if (row % 2 == 0 && col % 2 == 1 || row % 2 == 1
						&& col % 2 == 0) {
					imageContainer.setStylePrimaryName("black_tile");
				} else {
					imageContainer.setStylePrimaryName("white_tile");
				}
				imageContainer.add(image);
				containers[row][col] = imageContainer;
				gameGrid.setWidget(row, col, imageContainer);
				//board[row][col].getElement().setDraggable(Element.DRAGGABLE_TRUE);				
				PickupDragController dragController = new PieceDragController(RootPanel.get(),
						false);

				dragController.setBehaviorDragStartSensitivity(5);
				dragController.setBehaviorDragProxy(true);
				dragControllers[row][col] = dragController;
			}
		}
		
		
		
	}
	
	
	
	public HasClickHandlers getDeleteGameButton(){
		return deleteGameButton;
	}
	
	/*
	public void setDraggable(){
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				board[row][col].getElement().setDraggable(Element.DRAGGABLE_TRUE);				
			}
		}
	}
	*/
	
	public void resetImageObject(){
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				final Image image = new Image();
				board[row][col] = image;
				image.setWidth("100%");
				if (row % 2 == 0 && col % 2 == 1 || row % 2 == 1
						&& col % 2 == 0) {
					image.setResource(gameImages.blackTile());
				} else {
					image.setResource(gameImages.whiteTile());
				}
				gameGrid.setWidget(row, col, image);				
			}
		}
	}
	
	@Override
	public void setPiece(int row, int col, Piece piece) {
		FlowPanel imageContainer = containers[row][col];
		imageContainer.clear();
		Image image = new Image();
		image.setWidth("100%");
		image.setHeight("100%");
		
		if (piece == null) {
			board[row][col] = image;
			imageContainer.add(image);
			return;
		}

		switch (piece.getKind()) {
		case QUEEN:
			if (piece.getColor().equals(Color.WHITE)) {
				image.setResource(gameImages.whiteQueen());
			} else {
				image.setResource(gameImages.blackQueen());
			}
			break;
		case KING:
			if (piece.getColor().equals(Color.WHITE)) {
				image.setResource(gameImages.whiteKing());
			} else {
				image.setResource(gameImages.blackKing());
			}
			break;

		case PAWN:
			if (piece.getColor().equals(Color.WHITE)) {
				image.setResource(gameImages.whitePawn());
			} else {
				image.setResource(gameImages.blackPawn());
			}
			break;

		case ROOK:
			if (piece.getColor().equals(Color.WHITE)) {
				image.setResource(gameImages.whiteRook());
			} else {
				image.setResource(gameImages.blackRook());
			}
			break;

		case BISHOP:
			if (piece.getColor().equals(Color.WHITE)) {
				image.setResource(gameImages.whiteBishop());
			} else {
				image.setResource(gameImages.blackBishop());
			}
			break;

		case KNIGHT:
			if (piece.getColor().equals(Color.WHITE)) {
				image.setResource(gameImages.whiteKnight());
			} else {
				image.setResource(gameImages.blackKnight());
			}
			break;
		}		
		board[row][col] = image;
		imageContainer.add(image);
	}
	
	@Override
	public void setHighlighted(int row, int col, boolean highlighted) {
		Element element = board[row][col].getElement();
		if (highlighted) {
			element.setClassName(css.highlighted());
		} else {
			element.removeClassName(css.highlighted());
		}
	}

	@Override
	public void setWhoseTurn(Color color) {
		if (color.equals(Color.WHITE)) {
			gameStatus.setText("White's turn");
		} else if (color.equals(Color.BLACK)) {
			gameStatus.setText("Black's turn");
		} else {
			gameStatus.setText("Unknown Color!!!");
		}
	}

	public void setGameStatus(String status) {
		gameStatus.setText(status);
	}

	@Override
	public void setGameResult(GameResult gameResult) {
		if (gameResult.getWinner() == null) {
			gameStatus.setText("Draw");
		}
		gameStatus.setText(gameResult.toString());
		
	}

	public HasClickHandlers getImageClickHandler(int row, int col) {
		return board[row][col];
	}

	public void clearHighlighted() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				setHighlighted(i, j, false);
			}
		}
	}

	@Override
	public HasClickHandlers getPromotionImage(int row, int col) {

		return promotionBoard[row][col];
	}

	
	public void initialPromotion(){
		setPromotion(Color.WHITE);
		promotionGrid.setVisible(false);
	}
	
	@Override
	public void setPromotion(Color color) {
		
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				final Image image1 = new Image();
				promotionBoard[i][j] = image1;
				image1.setWidth("100%");
				promotionGrid.setWidget(i, j, image1);
			}
		}
		//set promotion status
		promotionStatus.setText("Choose for Promotion");
		
		
		//add image for promotion
		if (color.equals(Color.BLACK)) {
			promotionBoard[0][0].setResource(gameImages.blackBishop());
			promotionBoard[0][1].setResource(gameImages.blackKnight());
			promotionBoard[1][0].setResource(gameImages.blackQueen());
			promotionBoard[1][1].setResource(gameImages.blackRook());
		}
		else if (color.equals(Color.WHITE)) {
			promotionBoard[0][0].setResource(gameImages.whiteBishop());
			promotionBoard[0][1].setResource(gameImages.whiteKnight());
			promotionBoard[1][0].setResource(gameImages.whiteQueen());
			promotionBoard[1][1].setResource(gameImages.whiteRook());
		}
		
		Element element0 = promotionBoard[0][0].getElement();
		Element element1 = promotionBoard[0][1].getElement();
		Element element2 = promotionBoard[1][0].getElement();
		Element element3 = promotionBoard[1][1].getElement();

		element0.setClassName(css.highlighted());
		element1.setClassName(css.highlighted());
		element2.setClassName(css.highlighted());
		element3.setClassName(css.highlighted());
		
		
		promotionGrid.setVisible(true);
	}

	@Override
	public void removePromotion() {
		promotionGrid.setVisible(false);
		/*
		promotionBoard[0][0].setResource(gameImages.whiteTile());
		promotionBoard[0][1].setResource(gameImages.whiteTile());
		promotionBoard[1][0].setResource(gameImages.whiteTile());
		promotionBoard[1][1].setResource(gameImages.whiteTile());
		promotionStatus.setText("");
		
		Element element0 = promotionBoard[0][0].getElement();
		Element element1 = promotionBoard[0][1].getElement();
		Element element2 = promotionBoard[1][0].getElement();
		Element element3 = promotionBoard[1][1].getElement();
		
		element0.removeClassName(css.highlighted());
		element1.removeClassName(css.highlighted());
		element2.removeClassName(css.highlighted());
		element3.removeClassName(css.highlighted());
		*/
	}

	@Override
	public void setRestartButton() {
		restartButton.setVisible(true);
	}
	public void removeRestartButton(){
		restartButton.setVisible(false);		
	}

	@Override
	public HasClickHandlers getRestartButton() {
		return restartButton;
	}
	
	public HasClickHandlers getSaveButton(){
		return saveButton;
	}
	
	public HasClickHandlers getLoadButton(){
		return loadButton;
	}
	
	public HasDragStartHandlers getImageDragStartHandlers(int row, int col){
		return board[row][col];
	}
	
	@Override
	public void removePromotionHighlishted() {
		Element element0 = promotionBoard[0][0].getElement();
		Element element1 = promotionBoard[0][1].getElement();
		Element element2 = promotionBoard[1][0].getElement();
		Element element3 = promotionBoard[1][1].getElement();
		
		element0.removeClassName(css.highlighted());
		element1.removeClassName(css.highlighted());
		element2.removeClassName(css.highlighted());
		element3.removeClassName(css.highlighted());
	}
	
	public void setCaptureAudio(Audio audio){
		simplepanelCapture.setWidget(audio);
	}
	public void setGameEndAudio(Audio audio){
		simplepanelGameEnd.setWidget(audio);
	}
	public void setClickAudio(Audio audio) {
		simplepanelClick.setWidget(audio);
	}
	/*
	@Override
	public HasDragOverHandlers getImageDragOverHandlers(int row, int col) {
		return board[row][col];
	}

	@Override
	public HasDropHandlers getImageDropHandlers(int row, int col) {
		return board[row][col];
	}
	*/

	@Override
	public Image getImage(int row, int col) {
		return board[row][col];
	}


	@Override
	public ListBox getStorageList() {
		return listBox;
	}
	
	public TextBox getNameText(){
		return nameText;
	}


	@Override
	public void setSaveStatus(String str) {
		saveStatus.setText(str);
	}


	@Override
	public void setLoadStatus(String str) {
		loadStatus.setText(str);
	}
	
	public ListBox getMatchList(){
		return matchList;
	}
	
	public HasClickHandlers getAutoMatchButton(){
		return autoMatchButton;
	}
	public HasClickHandlers getInviteButton(){
		return inviteButton;
	}
	public TextBox getInvitePlayerText(){
		return invitePlayerText;
	}
	
	public void showAutoMatchPanel(){
		autoMatchPanel.setVisible(true);
	}
	public void hideAutoMatchPanel(){
		autoMatchPanel.setVisible(false);
	}
	public void showInvitePlayer(){
		autoMatchPanel.setVisible(true);
	}
	public void hideInvitePlayer(){
		autoMatchPanel.setVisible(false);
	}
	public void showGameDisplayPanel(){
		autoMatchPanel.setVisible(true);
	}
	public void hideGameDisplayPanel(){
		autoMatchPanel.setVisible(false);
	}
	public void showWaitStatus(){
		waitStatus.setVisible(true);
	}
	public void hideWaitStatus(){
		waitStatus.setVisible(false);
	}
	
	public void showConnectPanel(){
		connectPanel.setVisible(true);
	}
	public void hideConnectPanel(){
		connectPanel.setVisible(false);
	}

	@Override
	public String getSelectedMatch() {
		return matchList.getValue(matchList.getSelectedIndex());
	}
	
	@Override
	public void setMatchList(Map<String, String> items, String selectedMatchId) {
		matchList.clear();
		
		matchList.addItem("choose match to display", "");
		
		int index = 1;
		for(Entry<String, String> entry : items.entrySet()) {
			//System.out.println("4 add into list "+entry.getValue());
			matchList.addItem(entry.getValue(), entry.getKey());
			
			if(entry.getKey().equals(selectedMatchId)) {
				matchList.setSelectedIndex(index);
			}
			
			index++;
		}
	}
	
	public void setWaitStatus(String str){
		waitStatus.setText(str);
	}

	@Override
	public void openChannel(String token, SocketListener listener) {
		ChannelFactory fact = new ChannelFactoryImpl();
		Channel channel = fact.createChannel(token);
		channel.open(listener);
	}
	
	public void setGameDisStatus(String str){
		gameDisStatus.setText(""+":  "+str);
	}

	public HasClickHandlers getAIButton(){
		return AIButton;
	}
	


	@Override
	public SourcesTableEvents getGrid() {

		return gameGrid;
	}

	@Override
	public void makeDraggableFromTo(int fromRow, int fromCol, int toRow,
			int toCol, final Callback callback) {
		// TODO Auto-generated method stub
		PickupDragController drag = dragControllers[fromRow][fromCol];
		drag.makeDraggable(board[fromRow][fromCol]);

		drag.registerDropController(new SimpleDropController(board[toRow][toCol]) {
			@Override
			public void onDrop(DragContext ctx) {
				//System.out.println("on drop");
				callback.execute();
			}
		});
	}

	@Override
	public void clearDragDrop() {
		// TODO Auto-generated method stub
		//System.out.println("clear dragndrop");
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				PickupDragController drag = dragControllers[i][j];
				drag.unregisterDropControllers();
			}
		}
	}

	@Override
	public void addDragHandler(int row, int col, DragHandler dh) {
		dragControllers[row][col].addDragHandler(dh);
		
	}

	@Override
	public Image[][] getBoard() {
		// TODO Auto-generated method stub
		return board;
	}

	@Override
	public void animateMove(Move move, Callback callback) {
		// TODO Auto-generated method stub
		MoveAnimation anim = new MoveAnimation(board, move, callback);
		anim.run(2000);
	}
	

}
