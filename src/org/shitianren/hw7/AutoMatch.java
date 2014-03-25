package org.shitianren.hw7;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Load;

@Entity
public class AutoMatch {
	private @Id Long id;
	@Load private Ref<Player> player;

	public Player getPlayer() {
		return player.getValue();
	}

	public void setPlayer(Player player) {
		this.player = Ref.create(player);
	}
}
