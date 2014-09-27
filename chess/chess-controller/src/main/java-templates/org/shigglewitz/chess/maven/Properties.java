package org.shigglewitz.chess.maven;

public interface Properties {
	// String WAR_NAME = "${war.name}";
	// String VIEW_FILE_PATH = "${path.to.view.files}";
	// String VIEW_EXTENSION = "${view.extension}";
	// String GAME_CONTROLLER_PATH = "${controller.game.path}";
	// String PLAYER_CONTROLLER_PATH = "${controller.player.path}";

	String WAR_NAME = "Chess";
	String VIEW_FILE_PATH = "WEB-INF/jsp/";
	String VIEW_EXTENSION = ".jsp";
	String GAME_MVC_CONTROLLER_PATH = "/game";
	String PLAYER_MVC_CONTROLLER_PATH = "/player";
	String GAME_REST_CONTROLLER_PATH = "/rest/game";
	String PLAYER_REST_CONTROLLER_PATH = "/rest/player";
}
