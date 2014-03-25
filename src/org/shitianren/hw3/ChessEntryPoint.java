package org.shitianren.hw3;


import org.shitianren.hw6.client.LoginService;
import org.shitianren.hw6.client.LoginServiceAsync;
import org.shitianren.hw6.client.LoginInfo;
import org.shitianren.hw8.ChessMessage;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ChessEntryPoint implements EntryPoint {
	//private ChessMessage chessMessage = GWT.create(ChessMessage.class);
	private LoginInfo userInfo = null;
	private VerticalPanel loginPanel = new VerticalPanel();
	private Anchor signInLink = new Anchor("Sign in");
	private Anchor signOutLink = new Anchor("Sign out");	

	@Override
	public void onModuleLoad() {
		LoginServiceAsync loginService = GWT
				.create(LoginService.class);
		loginService.login(Window.Location.getHref(),
				new AsyncCallback<LoginInfo>() {
					public void onFailure(Throwable error) {}
					public void onSuccess(LoginInfo result) {
						userInfo = result;
						if (userInfo.isLoggedIn()) {
							loadChessGame();
						} else {
							loadLoginPanel();
						}
					}
				});
	}

	public void loadChessGame() {
		final Graphics graphics = new Graphics();
		graphics.initialGamePanel();
		Presenter presenter = new Presenter();
		presenter.setView(graphics);
		presenter.initGamePanel();
		signOutLink.setHref(userInfo.getLogoutUrl());
		RootPanel.get().add(new Label("Hello"+", "+userInfo.getNickname()+", "));
		RootPanel.get().add(new Label("Logout here"));
		RootPanel.get().add(signOutLink);
		RootPanel.get().add(graphics);
	}
	
	public void loadLoginPanel(){
		
		signInLink.setHref(userInfo.getLoginUrl());
		loginPanel.add(new Label("Login here"));
		loginPanel.add(signInLink);
		RootPanel.get().add(loginPanel);
	}
}
