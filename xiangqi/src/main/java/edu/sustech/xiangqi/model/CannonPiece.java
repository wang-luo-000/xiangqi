package edu.sustech.xiangqi.model;

public class CannonPiece extends AbstractPiece {
    public CannonPiece(String name, int row, int col, boolean isRed) {
        super(name, row, col, isRed);
    }

    @Override
    public boolean canMoveTo(int targetRow, int targetCol, ChessBoardModel model) {
        // TODO: 实现炮的移动规则
        return true;
    }
}
