package edu.sustech.xiangqi.model;

public class ElephantPiece extends AbstractPiece {
    public ElephantPiece(String name, int row, int col, boolean isRed) {
        super(name, row, col, isRed);
    }

    @Override
    public boolean canMoveTo(int targetRow, int targetCol, ChessBoardModel model) {
        // TODO: 实现象/相的移动规则
        return true;
    }
}
