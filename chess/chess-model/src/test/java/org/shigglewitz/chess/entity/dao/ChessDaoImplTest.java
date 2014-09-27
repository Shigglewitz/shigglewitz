package org.shigglewitz.chess.entity.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.shigglewitz.chess.entity.Board;
import org.shigglewitz.chess.entity.Game;
import org.shigglewitz.chess.entity.Game.Color;
import org.shigglewitz.chess.entity.Move;
import org.shigglewitz.chess.entity.Player;
import org.shigglewitz.chess.entity.pieces.Piece;
import org.shigglewitz.chess.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@ContextConfiguration({ "classpath:applicationContext-hibernate.xml",
		"classpath:applicationContext-daoBeans.xml",
		"classpath:applicationContext-serviceBeans.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class ChessDaoImplTest {
	@Autowired
	private ChessDao chessDao;

	@Autowired
	private GameService gameService;

	@Test
	@Transactional
	public void testSavePlayer() {
		Player player = new Player();

		this.chessDao.savePlayer(player);

		assertNotNull(player.getId());
	}

	@Test
	public void testGetPlayer() {
		Player player = this.gameService.createPlayer();
		UUID id = player.getId();
		player = null;

		player = this.chessDao.getPlayer(id);

		assertNotNull(player);
	}

	@Test
	@Transactional
	public void testUpdatePlayer() {
		String testName = "Temp Name";
		Player player = this.gameService.createPlayer();
		assertEquals("", player.getName());
		player.setName(testName);
		this.chessDao.updatePlayer(player);
		UUID id = player.getId();

		player = null;

		player = this.chessDao.getPlayer(id);
		assertEquals(testName, player.getName());
	}

	@Test
	public void testGetUnknownPlayer() {
		Player player = this.chessDao.getPlayer(UUID.randomUUID());

		assertNull(player);
	}

	@Test
	public void testGetGame() {
		Game game = this.gameService.createGame(null, null);
		UUID id = game.getId();
		assertNotNull(id);
		game = null;

		game = this.chessDao.getGame(id);

		assertNotNull(game);
		assertEquals(Board.DEFAULT_SIZE, game.getBoard().getSize());
	}

	@Test
	public void testGetUnknownGame() {
		Game game = this.chessDao.getGame(UUID.randomUUID());

		assertNull(game);
	}

	@Test
	@Transactional
	public void testSaveGame() {
		Player player1 = new Player();
		Game game = new Game(5, "PBQKR", "NP PB");

		game.setLightPlayer(player1);

		assertNull(player1.getId());
		assertNull(game.getId());
		assertNull(game.getBoard().getId());

		for (Piece p : game.getBoard().getPieces()) {
			assertNull(p.getId());
		}

		this.chessDao.savePlayer(player1);
		this.chessDao.saveGame(game);

		assertNotNull(player1.getId());
		assertNotNull(game.getId());
		assertNotNull(game.getBoard().getId());

		for (Piece p : game.getBoard().getPieces()) {
			assertNotNull(p.getId());
		}
	}

	@Test
	@Transactional
	public void testUpdateGame() {
		Game game = this.gameService.createGame(null, null);
		UUID id = game.getId();
		assertEquals(Color.LIGHT, game.getColorToMove());
		assertEquals(0, game.viewMoves().size());

		assertNotNull(game.getBoard().getSquare("a2").getPiece().getId());

		Move move = new Move(1, Color.LIGHT, game.getBoard().getSquare("a2")
				.getPiece(), game.getBoard().getSquare("a4"), false, game);
		this.chessDao.saveMove(move);
		game.addMove(move);
		game.setColorToMove(Color.DARK);
		this.chessDao.updateGame(game);
		game = null;

		game = this.chessDao.getGame(id);
		assertEquals(Color.DARK, game.getColorToMove());
		assertEquals(1, game.viewMoves().size());
	}
}
