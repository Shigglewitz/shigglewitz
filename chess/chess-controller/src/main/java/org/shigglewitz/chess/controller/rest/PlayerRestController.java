package org.shigglewitz.chess.controller.rest;

import org.shigglewitz.chess.entity.Player;
import org.shigglewitz.chess.maven.Properties;
import org.shigglewitz.chess.representation.PlayerRepresentation;
import org.shigglewitz.chess.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(Properties.PLAYER_REST_CONTROLLER_PATH)
public class PlayerRestController {
	@Autowired
	private GameService gameService;

	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody PlayerRepresentation createPlayer() {
		Player player = this.gameService.createPlayer();

		return new PlayerRepresentation(player);
	}
}
