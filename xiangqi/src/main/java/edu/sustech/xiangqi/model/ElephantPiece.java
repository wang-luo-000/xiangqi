package edu.sustech.xiangqi.model;

import java.util.ArrayList;
import java.util.List;

public class ElephantPiece extends AbstractPiece {

    public ElephantPiece(String name, int row, int col, boolean isRed) {
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

        int rowDiff = targetRow - currentRow;
        int colDiff = Math.abs(targetCol - currentCol);

        // 象的移动规则：
        // 1. 必须走"田"字（对角线方向移动两格）
        // 2. 不能过河（红象在己方河界内，黑象在己方河界内）
        // 3. 象眼不能被堵（田字中心不能有棋子）

        // 检查是否走"田"字
        if (Math.abs(rowDiff) != 2 || colDiff != 2) {
            return false; // 不是田字移动
        }

        // 检查是否过河
        if (isCrossingRiver(targetRow)) {
            return false;
        }

        // 检查象眼是否被堵
        if (isBlocked(currentRow, currentCol, targetRow, targetCol, model)) {
            return false;
        }

        // 检查目标位置是否有己方棋子（由父类或调用者处理）
        // 这里只返回true，实际的吃子逻辑在外部处理
        return true;
    }

    /**
     * 检查是否过河
     */
    private boolean isCrossingRiver(int targetRow) {
        if (isRed()) {
            // 红象不能过河（红方在棋盘下方，河界在row=4-5之间）
            // 红象只能在自己的半场（row >= 5）
            return targetRow < 5;
        } else {
            // 黑象不能过河（黑方在棋盘上方，河界在row=4-5之间）
            // 黑象只能在自己的半场（row <= 4）
            return targetRow > 4;
        }
    }

    /**
     * 检查象眼是否被堵
     */
    private boolean isBlocked(int currentRow, int currentCol, int targetRow, int targetCol, ChessBoardModel model) {
        // 计算象眼位置（田字中心）
        int eyeRow = (currentRow + targetRow) / 2;
        int eyeCol = (currentCol + targetCol) / 2;

        // 象眼位置有棋子则被堵
        return model.getPieceAt(eyeRow, eyeCol) != null;
    }

    /**
     * 获取所有可能的移动位置（可选实现，用于高亮显示）
     */
    public List<int[]> getPossibleMoves(ChessBoardModel model) {
        List<int[]> possibleMoves = new ArrayList<>();

        int currentRow = getRow();
        int currentCol = getCol();

        // 象有4个可能的移动方向
        int[][] directions = {
                {-2, -2}, // 左上
                {-2,  2}, // 右上
                { 2, -2}, // 左下
                { 2,  2}  // 右下
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
     * 获取移动方向描述（用于调试和显示）
     */


    /**
     * 获取象眼位置（用于高亮显示）
     */
    public int[] getEyePosition(int targetRow, int targetCol) {
        int currentRow = getRow();
        int currentCol = getCol();

        int eyeRow = (currentRow + targetRow) / 2;
        int eyeCol = (currentCol + targetCol) / 2;

        return new int[]{eyeRow, eyeCol};
    }
}
