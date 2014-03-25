package org.shitianren.hw7;


import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.channel.ChannelPresence;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.googlecode.objectify.ObjectifyService;

public class ConnectTrack extends HttpServlet {
	static {
		ObjectifyService.register(Match.class);
		ObjectifyService.register(Player.class);
		ObjectifyService.register(AutoMatch.class);
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ChannelService channelService = ChannelServiceFactory.getChannelService();
		ChannelPresence presence = channelService.parsePresence(request);

		String[] parts = presence.clientId().split(">");
		String email = parts[0];
		
		Player player = ofy().load().type(Player.class).id(email).get();

		if (player == null) {
			throw new IllegalArgumentException("Player not found");
		}

		if(presence.isConnected()) {
			player.getChannels().add(presence.clientId());
			System.out.println(presence.clientId() + " connected");
		} else {
			player.getChannels().remove(presence.clientId());
			System.out.println(presence.clientId() + " disconncted");
		}
		
		ofy().save().entity(player).now();
	}
}

