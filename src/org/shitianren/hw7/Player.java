package org.shitianren.hw7;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Load;

@Entity
public class Player {
	@Id
	private String email;
	private String name;
	private Set<String> channels = new HashSet<String>();
	@Load private Set<Key<Match>> matches = new TreeSet<Key<Match>>();
	private int rank;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Key<Match>> getMatches() {
		return matches;
	}

	public void setMatches(Set<Key<Match>> matches) {
		this.matches = matches;
	}

	public Set<String> getChannels() {
		return channels;
	}

	public void setChannels(Set<String> channels) {
		this.channels = channels;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

}
