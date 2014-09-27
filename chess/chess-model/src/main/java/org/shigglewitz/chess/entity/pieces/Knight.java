package org.shigglewitz.chess.entity.pieces;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.shigglewitz.chess.entity.Board;
import org.shigglewitz.chess.entity.Game.Color;

@Entity
@DiscriminatorValue("Knight")
public class Knight extends Piece {
    private static final long serialVersionUID = -1053742019637114436L;

    public static final char SHORTHAND = 'N';

    /**
     * should only be used by hibernate
     */
    protected Knight() {
    };

    public Knight(Color color, Board board) {
        this.color = color;
        this.moved = false;
        this.name = "Knight";
        this.captured = false;
        this.board = board;
    }
}
