package org.shigglewitz.chess.entity.dao;

import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.shigglewitz.chess.entity.Game;
import org.shigglewitz.chess.entity.Move;
import org.shigglewitz.chess.entity.Player;
import org.shigglewitz.chess.entity.pieces.Piece;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("chessDao")
public class ChessDaoImpl implements ChessDao {
    protected EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    @Override
    public Player getPlayer(UUID id) {
        return this.entityManager.find(Player.class, id);
    }

    @Override
    public Game getGame(UUID id) {
        return this.entityManager.find(Game.class, id);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void saveGame(Game game) {
        for (Piece p : game.getBoard().getPieces()) {
            this.entityManager.persist(p);
        }
        this.entityManager.persist(game.getBoard());
        this.entityManager.persist(game);
        this.entityManager.flush();
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void savePlayer(Player player) {
        this.entityManager.persist(player);
        this.entityManager.flush();
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void updatePlayer(Player player) {
        this.entityManager.merge(player);
        this.entityManager.flush();
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void updateGame(Game game) {
        this.entityManager.merge(game);
        this.entityManager.flush();
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void saveMove(Move move) {
        this.entityManager.persist(move);
        this.entityManager.flush();
    }
}
