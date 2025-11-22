package edu.sustech.xiangqi.model;

public class RookPiece extends AbstractPiece {
    public RookPiece(String name, int row, int col, boolean isRed) {
        super(name, row, col, isRed);
    }

    @Override
    public boolean canMoveTo(int targetRow, int targetCol, ChessBoardModel model) {
        // TODO: 实现车的移动规则
        return true;
    }
}
