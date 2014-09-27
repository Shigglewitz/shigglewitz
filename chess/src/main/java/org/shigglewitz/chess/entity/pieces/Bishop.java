package org.shigglewitz.chess.entity.pieces;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.shigglewitz.chess.entity.Board;
import org.shigglewitz.chess.entity.Game.Color;

@Entity
@DiscriminatorValue("Bishop")
public class Bishop extends Piece {
    private static final long serialVersionUID = -3945875984807877282L;

    public static final char SHORTHAND = 'B';

    /**
     * should only be used by hibernate
     */
    protected Bishop() {
    }

    public Bishop(Color color, Board board) {
        this.color = color;
        this.moved = false;
        this.name = "Bishop";
        this.captured = false;
        this.board = board;
    }
}
