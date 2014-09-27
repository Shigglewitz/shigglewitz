package org.shigglewitz.chess.service;

import java.util.UUID;

import org.shigglewitz.chess.entity.Board;
import org.shigglewitz.chess.entity.Game;
import org.shigglewitz.chess.entity.Game.Color;
import org.shigglewitz.chess.entity.Player;
import org.shigglewitz.chess.entity.Square;
import org.shigglewitz.chess.entity.dao.ChessDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("gameService")
public class GameService {
	@Autowired
	private ChessDao chessDao;

	@Transactional
	public Player createPlayer() {
		Player player = new Player();

		this.chessDao.savePlayer(player);

		return player;
	}

	public Player getPlayer(UUID id) {
		return this.chessDao.getPlayer(id);
	}

	@Transactional
	public Game createGame(Player lightPlayer, Player darkPlayer) {
		Game game = Game.createDefaultGame();

		game.setLightPlayer(lightPlayer);
		game.setDarkPlayer(darkPlayer);

		this.chessDao.saveGame(game);

		return game;
	}

	@Transactional
	public Game createGame(int size, String lightStart, String darkStart,
			Player lightPlayer, Player darkPlayer) {
		Game game = new Game(size, lightStart, darkStart);

		game.setLightPlayer(lightPlayer);
		game.setDarkPlayer(darkPlayer);

		this.chessDao.saveGame(game);

		return game;
	}

	public Game getGame(UUID id) {
		return this.chessDao.getGame(id);
	}

	@Transactional
	public void movePiece(Game game, String startSquareDescr,
			String destSquareDescr) throws IllegalArgumentException {
		Board board = game.getBoard();
		Square startSquare = board.getSquare(startSquareDescr);

		if (startSquare.getPiece() == null) {
			throw new IllegalArgumentException("No piece at square "
					+ startSquareDescr);
		}

		Square destSquare = board.getSquare(destSquareDescr);

		if (destSquare.getPiece() != null) {
			destSquare.getPiece().setCaptured(true);
		}

		destSquare.setPiece(startSquare.getPiece());
		startSquare.setPiece(null);

		switch (game.getColorToMove()) {
		case LIGHT:
			game.setColorToMove(Color.DARK);
			break;
		case DARK:
			game.setColorToMove(Color.LIGHT);
			break;
		}

		this.chessDao.updateGame(game);
	}
}
