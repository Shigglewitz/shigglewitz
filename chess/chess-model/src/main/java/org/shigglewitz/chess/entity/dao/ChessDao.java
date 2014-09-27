package org.shigglewitz.chess.entity.dao;

import java.util.UUID;

import org.shigglewitz.chess.entity.Game;
import org.shigglewitz.chess.entity.Move;
import org.shigglewitz.chess.entity.Player;

public interface ChessDao {
    Player getPlayer(UUID id);

    Game getGame(UUID id);

    void savePlayer(Player player);

    void saveGame(Game game);

    void updatePlayer(Player player);

    void updateGame(Game game);

    void saveMove(Move move);
}
