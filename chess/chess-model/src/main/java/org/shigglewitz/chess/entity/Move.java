package org.shigglewitz.chess.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.shigglewitz.chess.entity.Game.Color;
import org.shigglewitz.chess.entity.pieces.Piece;

@Entity
@Table(name = "MOVES")
public class Move {
    private MoveId moveId;
    private Piece piece;
    private Square destination;
    private boolean capture;
    private Game game;

    /**
     * should only be used by hibernate
     */
    protected Move() {
    }

    public Move(int number, Color color, Piece piece, Square destination,
            boolean capture, Game game) {
        this.moveId = new MoveId();
        this.moveId.setNumber(number);
        this.moveId.setColor(color);

        this.piece = piece;
        this.destination = destination;
        this.capture = capture;
        this.game = game;
    }

    @EmbeddedId
    protected MoveId getMoveId() {
        return this.moveId;
    }

    protected void setMoveId(MoveId moveId) {
        this.moveId = moveId;
    }

    @Transient
    public int getNumber() {
        return this.moveId.getNumber();
    }

    /**
     * should only be set by hibernate
     * 
     * @param number
     */
    protected void setNumber(int number) {
        this.moveId.setNumber(number);
    }

    @Transient
    public Color getColor() {
        return this.moveId.getColor();
    }

    /**
     * should only be set by hibernate
     * 
     * @param color
     */
    protected void setColor(Color color) {
        this.moveId.setColor(color);
    }

    @ManyToOne
    @JoinColumn(name = "piece_id")
    public Piece getPiece() {
        return this.piece;
    }

    /**
     * should only be set by hibernate
     * 
     * @param piece
     */
    protected void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Square getDestination() {
        return this.destination;
    }

    /**
     * should only be set by hibernate
     * 
     * @param destination
     */
    protected void setDestination(Square destination) {
        this.destination = destination;
    }

    public boolean isCapture() {
        return this.capture;
    }

    /**
     * should only be set by hibernate
     * 
     * @param capture
     */
    protected void setCapture(boolean capture) {
        this.capture = capture;
    }

    @ManyToOne
    @JoinColumn(name = "game_id")
    public Game getGame() {
        return this.game;
    }

    /**
     * should only be set by hibernate
     * 
     * @param game
     */
    protected void setGame(Game game) {
        this.game = game;
    }

    @Embeddable
    public static class MoveId implements Serializable {
        private static final long serialVersionUID = 8188707881996576749L;

        private int number;
        private Color color;

        @Column
        public int getNumber() {
            return this.number;
        }

        /**
         * should only be used by hibernate
         * 
         * @param number
         */
        protected void setNumber(int number) {
            this.number = number;
        }

        @Column
        public Color getColor() {
            return this.color;
        }

        /**
         * should only be used by hibernate
         * 
         * @param color
         */
        protected void setColor(Color color) {
            this.color = color;
        }
    }
}
