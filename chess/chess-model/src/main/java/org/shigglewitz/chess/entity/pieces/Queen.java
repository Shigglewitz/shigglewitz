package org.shigglewitz.chess.entity.pieces;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.shigglewitz.chess.entity.Board;
import org.shigglewitz.chess.entity.Game.Color;

@Entity
@DiscriminatorValue("Queen")
public class Queen extends Piece {
    private static final long serialVersionUID = -6016315054667013925L;

    public static final char SHORTHAND = 'Q';

    /**
     * should only be used by hibernate
     */
    protected Queen() {
    };

    public Queen(Color color, Board board) {
        this.color = color;
        this.moved = false;
        this.name = "Queen";
        this.captured = false;
        this.board = board;
    }
}
