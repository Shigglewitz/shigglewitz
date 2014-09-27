package org.shigglewitz.chess.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.shigglewitz.chess.entity.Game;
import org.shigglewitz.chess.entity.Player;
import org.shigglewitz.chess.entity.pieces.Piece;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration({ "classpath:applicationContext-hibernate.xml",
	"classpath:applicationContext-daoBeans.xml",
	"classpath:applicationContext-serviceBeans.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class GameServiceTest {
    @Autowired
    private GameService gameService;

    @Ignore
    @Test
    public void testMovePiece() {
        Game game = Game.createDefaultGame();
        Player player = new Player();
    }

    @Test
    public void testDefaultGameCreation() {
        Game game = this.gameService.createGame();

        assertNotNull(game.getId());
        assertNotNull(game.getBoard().getId());

        for (Piece p : game.getBoard().getPieces()) {
            assertNotNull(
                    p.getName() + " at square " + p.getFile() + ","
                            + p.getRank() + " has no id", p.getId());
        }
    }
}
