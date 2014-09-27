package org.shigglewitz.chess.representation;

import java.util.UUID;

import org.shigglewitz.chess.entity.Game;

public class GameRepresentation {
	private UUID id;
	private UUID lightPlayer;
	private UUID darkPlayer;

	protected GameRepresentation() {
	}

	public GameRepresentation(Game game) {
		this.setId(game.getId());
		if (game.getLightPlayer() != null) {
			this.setLightPlayer(game.getLightPlayer().getId());
		}
		if (game.getDarkPlayer() != null) {
			this.setDarkPlayer(game.getDarkPlayer().getId());
		}
	}

	public UUID getId() {
		return this.id;
	}

	public void setId(UUID gameId) {
		this.id = gameId;
	}

	public UUID getLightPlayer() {
		return this.lightPlayer;
	}

	public void setLightPlayer(UUID lightPlayer) {
		this.lightPlayer = lightPlayer;
	}

	public UUID getDarkPlayer() {
		return this.darkPlayer;
	}

	public void setDarkPlayer(UUID darkPlayer) {
		this.darkPlayer = darkPlayer;
	}
}
