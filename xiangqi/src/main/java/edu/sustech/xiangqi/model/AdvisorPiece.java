package edu.sustech.xiangqi.model;

public class AdvisorPiece extends AbstractPiece {
    public AdvisorPiece(String name, int row, int col, boolean isRed) {
        super(name, row, col, isRed);
    }

    @Override
    public boolean canMoveTo(int targetRow, int targetCol, ChessBoardModel model) {
        int currentRow = getRow();
        int currentCol = getCol();

        if (currentRow == targetRow && currentCol == targetCol) {
            return false;
        }

        int rowDiff = Math.abs(targetRow - currentRow);
        int colDiff = Math.abs(targetCol - currentCol);

        // 士只能走斜线一步
        if (rowDiff != 1 || colDiff != 1) {
            return false;
        }

        // 检查是否在九宫格内
        if (!isInPalace(targetRow, targetCol)) {
            return false;
        }

        // 检查目标位置是否有己方棋子（在movePiece中处理）
        return true;
    }

    /**
     * 检查目标位置是否在九宫格内
     */
    private boolean isInPalace(int row, int col) {
        if (isRed()) {
            // 红方九宫格：行7-9，列3-5
            return row >= 7 && row <= 9 && col >= 3 && col <= 5;
        } else {
            // 黑方九宫格：行0-2，列3-5
            return row >= 0 && row <= 2 && col >= 3 && col <= 5;
        }
    }
}
