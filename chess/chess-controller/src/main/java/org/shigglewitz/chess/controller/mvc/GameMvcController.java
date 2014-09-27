package org.shigglewitz.chess.controller.mvc;

import java.util.UUID;

import org.shigglewitz.chess.controller.exception.BadRequestException;
import org.shigglewitz.chess.controller.exception.ResourceNotFoundException;
import org.shigglewitz.chess.entity.Game;
import org.shigglewitz.chess.entity.Player;
import org.shigglewitz.chess.entity.dao.ChessDao;
import org.shigglewitz.chess.maven.Properties;
import org.shigglewitz.chess.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(Properties.GAME_MVC_CONTROLLER_PATH)
public class GameMvcController {
	public static final String UUID_REGEX = "[a-f0-9]{8}(-[a-f0-9]{4}){3}-[a-f0-9]{12}";

	@Autowired
	private ChessDao chessDao;

	@Autowired
	private GameService gameService;

	@RequestMapping(value = "/{game_id}", method = RequestMethod.GET)
	public String viewGame(@PathVariable("game_id") UUID gameId, ModelMap model) {
		Game game = this.gameService.getGame(gameId);

		if (game == null) {
			throw new ResourceNotFoundException(gameId.toString());
		}

		model.addAttribute("movie", gameId);
		return "viewChess";
	}

	public void startGame(String playerId, String colorChoice) {

	}

	public void movePiece(String gameId, String playerId, String startSquare,
			String destSquare) throws BadRequestException {
		if (!validUuid(gameId)) {
			throw new BadRequestException(this.invalidGameIdMessage(gameId));
		}
		if (!validUuid(playerId)) {
			throw new BadRequestException(this.invalidPlayerIdMessage(playerId));
		}

		Game game = this.chessDao.getGame(UUID.fromString(gameId));
		if (game == null) {
			throw new BadRequestException(this.invalidGameIdMessage(gameId));
		}

		Player player = this.chessDao.getPlayer(UUID.fromString(playerId));
		if (player == null) {
			throw new BadRequestException(this.invalidPlayerIdMessage(playerId));
		}

		switch (game.getColorToMove()) {
		case LIGHT:
			this.validatePlayersTurn(game.getLightPlayer(), player);
			break;
		case DARK:
			this.validatePlayersTurn(game.getDarkPlayer(), player);
			break;
		default:
			break;
		}

		if (!game.getBoard().validateSquareDescr(startSquare)) {
			throw new BadRequestException("Invalid starting square: "
					+ startSquare);
		}

		if (!game.getBoard().validateSquareDescr(destSquare)) {
			throw new BadRequestException("Invalid destination square: "
					+ destSquare);
		}

		this.gameService.movePiece(game, startSquare, destSquare);
	}

	private String invalidGameIdMessage(String gameId) {
		return "Invalid game id: " + gameId;
	}

	private String invalidPlayerIdMessage(String playerId) {
		return "Invalid player id: " + playerId;
	}

	private void validatePlayersTurn(Player nextMove, Player player)
			throws BadRequestException {
		if (!nextMove.getId().equals(player.getId())) {
			throw new BadRequestException("Not player " + player.getId()
					+ "'s move");
		}
	}

	public static boolean validUuid(String uuid) {
		return uuid.matches(UUID_REGEX);
	}
}
