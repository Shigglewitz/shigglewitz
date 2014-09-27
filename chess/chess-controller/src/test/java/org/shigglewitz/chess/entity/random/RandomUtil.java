package org.shigglewitz.chess.entity.random;

import org.shigglewitz.chess.entity.Game;
import org.shigglewitz.chess.entity.dao.ChessDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RandomUtil {
    @Autowired
    private ChessDao chessDao;

    @Transactional
    public Game createRandomDefaultGame() {
        Game ret = Game.createDefaultGame();

        this.chessDao.saveGame(ret);

        return ret;
    }
}
