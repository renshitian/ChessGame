package org.shitianren.hw8;

import com.google.gwt.i18n.client.Messages;

public interface ChessMessage extends Messages {

	@DefaultMessage("Welcome to Chess Game")
	String welcome();
	
	@DefaultMessage("Hello")
	String hello();
	
	@DefaultMessage("Opponent")
	String opponent();
	
	@DefaultMessage("Turn")
	String turn();	
	
	@DefaultMessage("Please sign in your Google Account to play Chess Game: ")
	String login();
	
	@DefaultMessage("Sign in")
	String signin();
	
	@DefaultMessage("You can sign out here")
	String logout();
	
	@DefaultMessage("Sign out")
	String signout();
	
	@DefaultMessage("User:")
	String user();
	
	@DefaultMessage("Auto Match")
	String automatch();
	
	@DefaultMessage("Invite Player")
	String inviteplayer();
	
	@DefaultMessage("Choose match to display:")
	String displayNote1();
	
	@DefaultMessage("Select match to display:")
	String displayNote2();
	
	@DefaultMessage("Delete Match")
	String deletematch();
	
	@DefaultMessage("Type in to save")
	String saveNote();
	
	@DefaultMessage("Choose game to load")
	String loadNote();

	@DefaultMessage("Load")
	String load();
	
	@DefaultMessage("Save")
	String save();
	
	@DefaultMessage("Connecting to server, please wait...")
	String connect();
	
	@DefaultMessage("Start new game")
	String startnewgame();
	
	@DefaultMessage("White's Turn")
	String whiteTurn();
	
	@DefaultMessage("Black's Turn")
	String blackTurn();
	
	@DefaultMessage("The game is draw")
	String draw();
	
	@DefaultMessage("Choose which piece kind for promotion: ")
	String promotion();
	
	@DefaultMessage("Connection Success")
	String connectionsuccess();
	
	@DefaultMessage("You are invited by")
	String invited();
	
	@DefaultMessage("Invitation Success")
	String invitationsuccess();
	
	@DefaultMessage("Delete Match Success")
	String deletesuccess();
	
	
	@DefaultMessage("You cannot invite yourself!")
	String inviteself();
	
	@DefaultMessage("Name already exits, please type in another name to save: ")
	String nameoverlap();
	
	@DefaultMessage("There is no name. Type in a name to save game: ")
	String emptyname();
	
	@DefaultMessage("Your color is White")
	String mycolorwhite();
	
	@DefaultMessage("Your color is Black")
	String mycolorblack();
	
	@DefaultMessage("The start date is")
	String startdate();
	
	@DefaultMessage("Play against computer")
	String aibutton();

}
