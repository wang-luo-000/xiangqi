package edu.sustech.xiangqi.model;

import java.util.ArrayList;
import java.util.List;

public class GeneralPiece extends AbstractPiece {

    public GeneralPiece(String name, int row, int col, boolean isRed) {
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

        int rowDiff = Math.abs(targetRow - currentRow);
        int colDiff = Math.abs(targetCol - currentCol);

        // 将（帅）的移动规则：
        // 1. 只能在九宫格内移动
        // 2. 每次只能横向或纵向移动一步
        // 3. 不能与对方将帅直接对面（无棋子遮挡）

        // 检查是否在九宫格内
        if (!isInPalace(targetRow, targetCol)) {
            return false;
        }

        // 检查移动步长（只能移动一步）
        if ((rowDiff == 1 && colDiff == 0) || (rowDiff == 0 && colDiff == 1)) {
            // 检查是否会导致将帅直接对面
            if (willFaceOpponentGeneral(targetRow, targetCol, model)) {
                return false;
            }
            return true;
        }

        return false;
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

    /**
     * 检查移动后是否会导致将帅直接对面
     */
    private boolean willFaceOpponentGeneral(int targetRow, int targetCol, ChessBoardModel model) {
        // 找到对方将帅的位置
        int opponentGeneralRow = -1;
        int opponentGeneralCol = -1;

        // 遍历棋盘找到对方将帅
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 9; col++) {
                AbstractPiece piece = model.getPieceAt(row, col);
                if (piece != null &&
                        piece instanceof GeneralPiece &&
                        piece.isRed() != this.isRed()) {
                    opponentGeneralRow = row;
                    opponentGeneralCol = col;
                    break;
                }
            }
            if (opponentGeneralRow != -1) break;
        }

        // 如果没找到对方将帅，返回false
        if (opponentGeneralRow == -1) {
            return false;
        }

        // 检查是否在同一列
        if (targetCol != opponentGeneralCol) {
            return false;
        }

        // 检查中间是否有棋子遮挡
        int startRow = Math.min(targetRow, opponentGeneralRow) + 1;
        int endRow = Math.max(targetRow, opponentGeneralRow) - 1;

        for (int row = startRow; row <= endRow; row++) {
            if (model.getPieceAt(row, targetCol) != null) {
                // 中间有棋子遮挡，不会直接对面
                return false;
            }
        }

        // 中间没有棋子遮挡，将帅直接对面
        return true;
    }

    /**
     * 获取所有可能的移动位置（用于高亮显示）
     */
    public List<int[]> getPossibleMoves(ChessBoardModel model) {
        List<int[]> possibleMoves = new ArrayList<>();

        int currentRow = getRow();
        int currentCol = getCol();

        // 将（帅）有4个可能的移动方向：上、下、左、右
        int[][] directions = {
                {-1, 0}, // 上
                {1, 0},  // 下
                {0, -1}, // 左
                {0, 1}   // 右
        };

        for (int[] dir : directions) {
            int newRow = currentRow + dir[0];
            int newCol = currentCol + dir[1];

            if (canMoveTo(newRow, newCol, model)) {
                possibleMoves.add(new int[]{newRow, newCol});
            }
        }

        return possibleMoves;
    }

    /**
     * 检查是否被将军（可选方法，用于游戏逻辑）
     */
    public boolean isInCheck(ChessBoardModel model) {
        // 遍历所有对方棋子，检查是否能攻击到本方将帅
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 9; col++) {
                AbstractPiece piece = model.getPieceAt(row, col);
                if (piece != null && piece.isRed() != this.isRed()) {
                    // 跳过将帅直接对面的检查（已经在移动规则中处理）
                    if (piece instanceof GeneralPiece) {
                        continue;
                    }

                    if (piece.canMoveTo(getRow(), getCol(), model)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获取移动方向描述（用于调试和显示）
     */

}
