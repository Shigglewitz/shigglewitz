package org.shigglewitz.chess.service;

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

    @Transactional
    public Game createGame() {
        Game game = Game.createDefaultGame();

        this.chessDao.saveGame(game);

        return game;
    }

    @Transactional
    public Game createGame(int size, String lightStart, String darkStart) {
        Game game = new Game(size, lightStart, darkStart);

        this.chessDao.saveGame(game);

        return game;
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
