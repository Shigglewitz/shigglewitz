package org.shigglewitz.chess.entity.pieces;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.shigglewitz.chess.entity.Board;
import org.shigglewitz.chess.entity.Game.Color;

@Entity
@DiscriminatorValue("Pawn")
public class Pawn extends Piece {
    private static final long serialVersionUID = 6424886482636109306L;

    public static final char SHORTHAND = 'P';

    /**
     * should only be used by hibernate
     */
    protected Pawn() {
    };

    public Pawn(Color color, Board board) {
        this.color = color;
        this.moved = false;
        this.name = "Pawn";
        this.captured = false;
        this.board = board;
    }
}
