package edu.sustech.xiangqi.model;

public class AdvisorPiece extends AbstractPiece {
    public AdvisorPiece(String name, int row, int col, boolean isRed) {
        super(name, row, col, isRed);
    }

    @Override
    public boolean canMoveTo(int targetRow, int targetCol, ChessBoardModel model) {
        // TODO: 实现士/Advisor的移动规则
        return true;
    }
}
