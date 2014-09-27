package org.shigglewitz.chess.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.shigglewitz.chess.entity.Game.Color;
import org.shigglewitz.chess.properties.ErrorMessages;
import org.springframework.util.StringUtils;

public class GameTest {
    @Test
    public void testEmptyStartingString() {
        for (int i = 1; i < 20; i++) {
            String test = Game.emptyStartingString(i);

            assertEquals(i, test.length());
            assertFalse(StringUtils.hasText(test));
        }
    }

    @Test
    public void testDefaultSetup() {
        Game game = Game.createDefaultGame();
        assertEquals(Color.LIGHT, game.getColorToMove());
    }

    @Test(expected = IllegalStateException.class)
    public void testWinnerCannotBeChanged() {
        Game game = Game.createDefaultGame();
        Player player = new Player();

        assertNull(game.getWinner());

        game.setWinner(player);

        try {
            game.setWinner(null);
        } catch (IllegalStateException e) {
            assertNotNull(game.getWinner());
            assertNull(e.getCause());
            assertEquals(ErrorMessages.WINNER_ALREADY_SET, e.getMessage());

            throw e;
        }
    }
}
