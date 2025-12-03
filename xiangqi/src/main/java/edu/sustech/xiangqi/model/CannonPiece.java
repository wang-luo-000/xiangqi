package edu.sustech.xiangqi.model;

public class CannonPiece extends AbstractPiece {
    public CannonPiece(String name, int row, int col, boolean isRed) {
        super(name, row, col, isRed);
    }

    @Override
    public boolean canMoveTo(int targetRow, int targetCol, ChessBoardModel model) {
        int currentRow = getRow();
        int currentCol = getCol();

        // 不能原地不动
        if (currentRow == targetRow && currentCol == targetCol) {
            return false;
        }

        // 只能走直线
        if (currentRow != targetRow && currentCol != targetCol) {
            return false;
        }

        // 检查目标位置是否有棋子
        AbstractPiece targetPiece = model.getPieceAt(targetRow, targetCol);

        // 计算路径上的棋子数量
        int piecesCount = countPiecesBetween(currentRow, currentCol, targetRow, targetCol, model);

        if (targetPiece == null) {
            // 非吃子移动：路径上不能有任何棋子
            return piecesCount == 0;
        } else {
            // 吃子移动：路径上必须恰好有一个棋子（炮架）
            return piecesCount == 1;
        }
    }

    /**
     * 计算两个位置之间的棋子数量（不包括起点和终点）
     */
    private int countPiecesBetween(int fromRow, int fromCol, int toRow, int toCol, ChessBoardModel model) {
        int count = 0;

        if (fromRow == toRow) {
            // 水平移动
            int startCol = Math.min(fromCol, toCol);
            int endCol = Math.max(fromCol, toCol);

            for (int col = startCol + 1; col < endCol; col++) {
                if (model.getPieceAt(fromRow, col) != null) {
                    count++;
                }
            }
        } else {
            // 垂直移动
            int startRow = Math.min(fromRow, toRow);
            int endRow = Math.max(fromRow, toRow);

            for (int row = startRow + 1; row < endRow; row++) {
                if (model.getPieceAt(row, fromCol) != null) {
                    count++;
                }
            }
        }

        return count;
    }
}

