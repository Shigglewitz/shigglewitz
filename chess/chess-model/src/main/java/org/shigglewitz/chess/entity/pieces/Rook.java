package org.shigglewitz.chess.entity.pieces;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.shigglewitz.chess.entity.Board;
import org.shigglewitz.chess.entity.Game.Color;

@Entity
@DiscriminatorValue("Rook")
public class Rook extends Piece {
    private static final long serialVersionUID = 499825860183232138L;

    public static final char SHORTHAND = 'R';

    /**
     * should only be used by hibernate
     */
    protected Rook() {
    };

    public Rook(Color color, Board board) {
        this.color = color;
        this.moved = false;
        this.name = "Rook";
        this.captured = false;
        this.board = board;
    }
}
