package org.shigglewitz.chess.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.shigglewitz.chess.entity.Game.Color;
import org.shigglewitz.chess.entity.pieces.Bishop;
import org.shigglewitz.chess.entity.pieces.King;
import org.shigglewitz.chess.entity.pieces.Knight;
import org.shigglewitz.chess.entity.pieces.Pawn;
import org.shigglewitz.chess.entity.pieces.Piece;
import org.shigglewitz.chess.entity.pieces.Queen;
import org.shigglewitz.chess.entity.pieces.Rook;

public class BoardTest {
    @Test
    public void testGetSquare() {
        Board board = new Board(5, "     ", "     ");

        assertEquals("a1", board.getSquare("a1").getDescr());
        assertEquals("a1", board.getSquare(1, 1).getDescr());
        assertEquals("b2", board.getSquare("b2").getDescr());
        assertEquals("b2", board.getSquare(2, 2).getDescr());
        assertEquals("c1", board.getSquare("c1").getDescr());
        assertEquals("c1", board.getSquare(3, 1).getDescr());
        assertEquals("a3", board.getSquare("a3").getDescr());
        assertEquals("a3", board.getSquare(1, 3).getDescr());
    }

    @Test
    public void testDefaultSetup() {
        Board board = Board.createDefaultBoard();

        assertEquals(Board.DEFAULT_SIZE, board.getSize());

        // check pawns
        for (int i = 0; i < Board.DEFAULT_SIZE; i++) {
            this.assertSquarePieceProperties(board.getSquare(i + 1, 2),
                    Pawn.class, Color.LIGHT, board);
        }

        for (int i = 0; i < Board.DEFAULT_SIZE; i++) {
            this.assertSquarePieceProperties(board.getSquare(i + 1, 7),
                    Pawn.class, Color.DARK, board);
        }

        // check light back rank
        this.assertSquarePieceProperties(board.getSquare("a1"), Rook.class,
                Color.LIGHT, board);
        this.assertSquarePieceProperties(board.getSquare("b1"), Knight.class,
                Color.LIGHT, board);
        this.assertSquarePieceProperties(board.getSquare("c1"), Bishop.class,
                Color.LIGHT, board);
        this.assertSquarePieceProperties(board.getSquare("d1"), Queen.class,
                Color.LIGHT, board);
        this.assertSquarePieceProperties(board.getSquare("e1"), King.class,
                Color.LIGHT, board);
        this.assertSquarePieceProperties(board.getSquare("f1"), Bishop.class,
                Color.LIGHT, board);
        this.assertSquarePieceProperties(board.getSquare("g1"), Knight.class,
                Color.LIGHT, board);
        this.assertSquarePieceProperties(board.getSquare("h1"), Rook.class,
                Color.LIGHT, board);

        // check dark back rank
        this.assertSquarePieceProperties(board.getSquare("a8"), Rook.class,
                Color.DARK, board);
        this.assertSquarePieceProperties(board.getSquare("b8"), Knight.class,
                Color.DARK, board);
        this.assertSquarePieceProperties(board.getSquare("c8"), Bishop.class,
                Color.DARK, board);
        this.assertSquarePieceProperties(board.getSquare("d8"), Queen.class,
                Color.DARK, board);
        this.assertSquarePieceProperties(board.getSquare("e8"), King.class,
                Color.DARK, board);
        this.assertSquarePieceProperties(board.getSquare("f8"), Bishop.class,
                Color.DARK, board);
        this.assertSquarePieceProperties(board.getSquare("g8"), Knight.class,
                Color.DARK, board);
        this.assertSquarePieceProperties(board.getSquare("h8"), Rook.class,
                Color.DARK, board);

        // rest of the board is null
        for (int i = 3; i < 7; i++) {
            for (int j = 1; j <= 8; j++) {
                assertNull(board.getSquare(j, i).getPiece());
            }
        }

        for (Square s : board.getSquares()) {
            if (s.getPiece() != null) {
                assertEquals(s.getRank(), s.getPiece().getRank());
                assertEquals(s.getFile(), s.getPiece().getFile());
            }
        }
    }

    private void assertSquarePieceProperties(Square square,
            Class<? extends Piece> clazz, Color color, Board board) {
        Piece piece = square.getPiece();
        assertNotNull("Square " + square.getDescr() + " has no piece", piece);
        assertEquals(clazz, piece.getClass());
        assertEquals(color, piece.getColor());
        assertEquals(board, piece.getBoard());
    }
}
