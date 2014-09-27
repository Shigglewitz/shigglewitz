package org.shigglewitz.chess.representation;

import java.util.UUID;

import org.shigglewitz.chess.entity.Player;

public class PlayerRepresentation {
	private UUID id;

	protected PlayerRepresentation() {
	}

	public PlayerRepresentation(Player player) {
		this.setId(player.getId());
	}

	public UUID getId() {
		return this.id;
	}

	public void setId(UUID gameId) {
		this.id = gameId;
	}
}
