package org.shitianren.hw7;
import java.io.Serializable;
import java.util.Date;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Load;

@Entity
public class Match implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
	@Load
	private Ref<Player> white;
	@Load
	private Ref<Player> black;
	private String state;
	private String name;
	private Date startDate;
	private int turnNumber;
	private boolean WD = false;
	private boolean BD = false;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Player getWhitePlayer() {
		return white.getValue();
	}

	public void setWhitePlayer(Player white) {
		this.white = Ref.create(white);
	}

	public Player getBlackPlayer() {
		return black.getValue();
	}

	public void setBlackPlayer(Player black) {
		this.black = Ref.create(black);
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Match other = (Match) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public int getTurnNumber() {
		return turnNumber;
	}

	public void setTurnNumber(int turnNumber) {
		this.turnNumber = turnNumber;
	}

	public boolean isWD() {
		return WD;
	}

	public void setWD(boolean wD) {
		WD = wD;
	}

	public boolean isBD() {
		return BD;
	}

	public void setBD(boolean bD) {
		BD = bD;
	}

}
