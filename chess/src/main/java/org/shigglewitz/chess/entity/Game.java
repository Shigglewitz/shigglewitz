package org.shigglewitz.chess.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;
import org.shigglewitz.chess.properties.ErrorMessages;

@Entity
@Table(name = "GAMES")
public class Game implements Serializable {
    private static final long serialVersionUID = -4377523928633766665L;

    public enum Color implements Serializable {
        LIGHT, DARK
    }

    private UUID id;
    private Player lightPlayer;
    private Player darkPlayer;
    private Player winner;
    private Color colorToMove;
    private Board board;
    private List<Move> moves;

    /**
     * this should only be used by hibernate
     */
    protected Game() {
    }

    protected static String emptyStartingString(int size) {
        return String.format(String.format("%%-%ds", size), "");
    }

    public Game(int size) {
        this(size, emptyStartingString(size), emptyStartingString(size));
    }

    public Game(int size, String lightStart, String darkStart) {
        this.setBoard(new Board(size, lightStart, darkStart));
        this.moves = new ArrayList<>();
    }

    public static Game createDefaultGame() {
        Game ret = new Game();
        ret.setBoard(Board.createDefaultBoard());
        ret.setColorToMove(Color.LIGHT);
        ret.moves = new ArrayList<>();

        return ret;
    }

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @Type(type = "pg-uuid")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "lightplayer_id")
    public Player getLightPlayer() {
        return this.lightPlayer;
    }

    public void setLightPlayer(Player lightPlayer) {
        this.lightPlayer = lightPlayer;
    }

    @ManyToOne
    @JoinColumn(name = "darkplayer_id")
    public Player getDarkPlayer() {
        return this.darkPlayer;
    }

    public void setDarkPlayer(Player darkPlayer) {
        this.darkPlayer = darkPlayer;
    }

    @ManyToOne
    @JoinColumn(name = "winner_id")
    public Player getWinner() {
        return this.winner;
    }

    public void setWinner(Player winner) {
        if (this.winner != null) {
            throw new IllegalStateException(ErrorMessages.WINNER_ALREADY_SET);
        }
        this.winner = winner;
    }

    @Column
    public Color getColorToMove() {
        return this.colorToMove;
    }

    public void setColorToMove(Color colorToMove) {
        this.colorToMove = colorToMove;
    }

    @OneToOne
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

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "game")
    protected List<Move> getMoves() {
        return this.moves;
    }

    protected void setMoves(List<Move> moves) {
        this.moves = moves;
    }

    public List<Move> viewMoves() {
        return Collections.unmodifiableList(this.moves);
    }

    public void addMove(Move move) {
        this.moves.add(move);
    }
}
