package org.shigglewitz.chess.entity.pieces;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.shigglewitz.chess.entity.Board;
import org.shigglewitz.chess.entity.Game.Color;

@Entity
@DiscriminatorValue("King")
public class King extends Piece {
    private static final long serialVersionUID = 1976153611675389156L;

    public static final char SHORTHAND = 'K';

    /**
     * should only be used by hibernate
     */
    protected King() {
    };

    public King(Color color, Board board) {
        this.color = color;
        this.moved = false;
        this.name = "King";
        this.captured = false;
        this.board = board;
    }

}
