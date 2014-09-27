package org.shigglewitz.chess.entity.pieces;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.shigglewitz.chess.entity.Board;
import org.shigglewitz.chess.entity.Game.Color;

@Entity
@Table(name = "pieces")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class Piece implements Serializable {
    private static final long serialVersionUID = -2115083671763364158L;

    protected Color color;
    protected boolean moved;
    protected String name;
    protected boolean captured;
    protected Board board;
    protected int rank;
    protected int file;
    protected UUID id;

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

    public String getName() {
        return this.name;
    }

    /**
     * should only be used by hibernate
     * 
     * @param name
     */
    protected void setName(String name) {
        this.name = name;
    }

    public boolean isMoved() {
        return this.moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public boolean isCaptured() {
        return this.captured;
    }

    public void setCaptured(boolean captured) {
        this.captured = captured;
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

    public int getRank() {
        return this.rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getFile() {
        return this.file;
    }

    public void setFile(int file) {
        this.file = file;
    }
}
