package org.shitianren.hw6.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginServiceAsync {
	  public void login(String requestUri, AsyncCallback<LoginInfo> async);
}
