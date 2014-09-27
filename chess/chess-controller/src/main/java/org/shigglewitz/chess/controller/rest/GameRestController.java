package org.shigglewitz.chess.controller.rest;

import java.util.UUID;

import org.shigglewitz.chess.controller.exception.ResourceNotFoundException;
import org.shigglewitz.chess.entity.Game;
import org.shigglewitz.chess.entity.Player;
import org.shigglewitz.chess.maven.Properties;
import org.shigglewitz.chess.representation.GameRepresentation;
import org.shigglewitz.chess.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = Properties.GAME_REST_CONTROLLER_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class GameRestController {
	@Autowired
	private GameService gameService;

	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody GameRepresentation createGame(
			@RequestParam("lightPlayer") UUID lightPlayerId,
			@RequestParam("darkPlayer") UUID darkPlayerId) {
		Player lightPlayer = this.gameService.getPlayer(lightPlayerId);
		Player darkPlayer = this.gameService.getPlayer(darkPlayerId);

		Game game = this.gameService.createGame(lightPlayer, darkPlayer);

		return new GameRepresentation(game);
	}

	@RequestMapping(value = "/{game_id}", method = RequestMethod.GET)
	public @ResponseBody GameRepresentation viewGame(
			@PathVariable("game_id") UUID gameId) {
		Game game = this.gameService.getGame(gameId);

		if (game == null) {
			throw new ResourceNotFoundException(gameId.toString());
		}

		return new GameRepresentation(game);
	}
}
