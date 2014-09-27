package org.shigglewitz.chess.entity;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
@Table(name = "SQUARES")
public class Square implements Serializable {
    private static final long serialVersionUID = -4666913760303484707L;

    public static final String DESCR_REGEX = "([a-z]+)([1-9][0-9]*)";
    public static final Pattern DESCR_PATTERN = Pattern.compile(DESCR_REGEX);
    public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";

    private Piece piece;
    private SquareId squareId;

    /**
     * should only used by hibernate
     * 
     */
    protected Square() {
    }

    public Square(int rank, int file, Board board) {
        this(rank, file, Board.DEFAULT_SIZE, board);
    }

    public Square(int file, int rank, int boardSize, Board board) {
        Board.validateFileAndRank(file, rank, boardSize);

        SquareId squareId = new SquareId();
        squareId.setFile(file);
        squareId.setRank(rank);
        squareId.setBoard(board);
        this.squareId = squareId;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    @Transient
    public Piece getPiece() {
        return this.piece;
    }

    @EmbeddedId
    protected SquareId getSquareId() {
        return this.squareId;
    }

    protected void setSquareId(SquareId squareId) {
        this.squareId = squareId;
    }

    @Transient
    public Board getBoard() {
        return this.squareId.getBoard();
    }

    @Transient
    public int getRank() {
        return this.squareId.getRank();
    }

    @Transient
    public int getFile() {
        return this.squareId.getFile();
    }

    @Transient
    public Game.Color getColor() {
        if (this.getFile() + this.getRank() % 2 == 0) {
            return Color.DARK;
        } else {
            return Color.LIGHT;
        }
    }

    @Transient
    public String getDescr() {
        return Character.toString((char) (this.getFile() - 1 + 'a'))
                + Integer.toString(this.getRank());
    }

    /**
     * 
     * @return [0] = file, [1] = rank
     */
    public static int[] getFileAndRankFromDescr(String descr) {
        int[] ret = new int[2];

        if (!descr.matches(DESCR_REGEX)) {
            throw new IllegalArgumentException("Descr " + descr
                    + " does not match regex!");
        }

        Matcher matcher = DESCR_PATTERN.matcher(descr);
        matcher.find();

        String letters = matcher.group(1).toLowerCase();
        int fileValue = 0;

        for (int i = 0; i < letters.length(); i++) {
            fileValue *= 26;
            fileValue += (letters.charAt(i) - 'a' + 1);
        }

        ret[0] = fileValue;
        ret[1] = Integer.parseInt(matcher.group(2));

        return ret;
    }

    @Override
    public String toString() {
        return this.getDescr() + this.piece.toString();
    }

    @Embeddable
    public static class SquareId implements Serializable {
        private static final long serialVersionUID = -1030408769666521645L;
        private int rank;
        private int file;
        private Board board;

        @Column(name = "rank")
        public int getRank() {
            return this.rank;
        }

        /**
         * should only be used by hibernate
         * 
         * @param rank
         */
        protected void setRank(int rank) {
            this.rank = rank;
        }

        @Column(name = "file")
        public int getFile() {
            return this.file;
        }

        /**
         * should only be used by hibernate
         * 
         * @param file
         */
        protected void setFile(int file) {
            this.file = file;
        }

        @ManyToOne
        @JoinColumn(name = "board_id")
        public Board getBoard() {
            return this.board;
        }

        /**
         * should only be used by hibernate
         * 
         * @param board
         */
        protected void setBoard(Board board) {
            this.board = board;
        }
    }

}
