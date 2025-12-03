package edu.sustech.xiangqi.model;

import java.util.ArrayList;
import java.util.List;

public class ChessBoardModel {
    // 储存棋盘上所有的棋子，要实现吃子的话，直接通过pieces.remove(被吃掉的棋子)删除就可以
    private final List<AbstractPiece> pieces;
    private static final int ROWS = 10;
    private static final int COLS = 9;
    private boolean redTurn = true; // 红方先手

    public ChessBoardModel() {
        pieces = new ArrayList<>();
        initializePieces();
    }

    private void initializePieces() {
        // 黑方棋子
        pieces.add(new GeneralPiece("將", 0, 4, false));
        pieces.add(new SoldierPiece("卒", 3, 0, false));
        pieces.add(new SoldierPiece("卒", 3, 2, false));
        pieces.add(new SoldierPiece("卒", 3, 4, false));
        pieces.add(new SoldierPiece("卒", 3, 6, false));
        pieces.add(new SoldierPiece("卒", 3, 8, false));
        pieces.add(new CannonPiece("炮", 2, 1, false));
        pieces.add(new CannonPiece("炮", 2, 7, false));
        pieces.add(new RookPiece("車", 0, 0, false));
        pieces.add(new RookPiece("車", 0, 8, false));
        pieces.add(new HorsePiece("馬", 0, 1, false));
        pieces.add(new HorsePiece("馬", 0, 7, false));
        pieces.add(new ElephantPiece("相", 0, 2, false));
        pieces.add(new ElephantPiece("相", 0, 6, false));
        pieces.add(new AdvisorPiece("仕", 0, 3, false));
        pieces.add(new AdvisorPiece("仕", 0, 5, false));

        // 红方棋子
        pieces.add(new GeneralPiece("帅", 9, 4, true));
        pieces.add(new SoldierPiece("兵", 6, 0, true));
        pieces.add(new SoldierPiece("兵", 6, 2, true));
        pieces.add(new SoldierPiece("兵", 6, 4, true));
        pieces.add(new SoldierPiece("兵", 6, 6, true));
        pieces.add(new SoldierPiece("兵", 6, 8, true));
        pieces.add(new AdvisorPiece("仕", 9, 3, true));
        pieces.add(new AdvisorPiece("仕", 9, 5, true));
        pieces.add(new HorsePiece("馬", 9, 1, true));
        pieces.add(new HorsePiece("馬", 9, 7, true));
        pieces.add(new RookPiece("車", 9, 0, true));
        pieces.add(new RookPiece("車", 9, 8, true));
        pieces.add(new ElephantPiece("相", 9, 2, true));
        pieces.add(new ElephantPiece("相", 9, 6, true));
        pieces.add(new CannonPiece("炮", 7, 1, true));
        pieces.add(new CannonPiece("炮", 7, 7, true));
    }

    public List<AbstractPiece> getPieces() {
        return pieces;
    }

    public AbstractPiece getPieceAt(int row, int col) {
        for (AbstractPiece piece : pieces) {
            if (piece.getRow() == row && piece.getCol() == col) {
                return piece;
            }
        }
        return null;
    }

    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < ROWS && col >= 0 && col < COLS;
    }

    /**
     * 移动棋子并处理吃子
     * @param piece 要移动的棋子
     * @param newRow 目标行
     * @param newCol 目标列
     * @return 移动是否成功
     */
     public boolean movePiece(AbstractPiece piece, int newRow, int newCol) {
        if (!isValidPosition(newRow, newCol)) {
            return false;
        }

        // 检查是否是当前玩家的回合
        if (piece.isRed() != redTurn) {
            return false;
        }

        // 检查移动是否合法
        if (!piece.canMoveTo(newRow, newCol, this)) {
            return false;
        }

        // 检查目标位置是否有棋子
        AbstractPiece targetPiece = getPieceAt(newRow, newCol);

        if (targetPiece != null) {
            // 不能吃己方棋子
            if (targetPiece.isRed() == piece.isRed()) {
                return false;
            }

            // 如果是炮，需要特殊处理吃子逻辑
            if (piece instanceof CannonPiece) {
                // 炮吃子时，中间必须有一个棋子作为炮架
                if (!hasExactlyOnePieceBetween(piece, targetPiece)) {
                    return false;
                }
            }

            // 吃子：移除目标位置的棋子
            pieces.remove(targetPiece);
        }

        // 执行移动
        piece.moveTo(newRow, newCol);

        // 切换回合
        redTurn = !redTurn;

        return true;
    }

    /**
     * 检查两个棋子之间是否恰好有一个棋子（用于炮的吃子规则）
     */
    private boolean hasExactlyOnePieceBetween(AbstractPiece piece1, AbstractPiece piece2) {
        int fromRow = piece1.getRow();
        int fromCol = piece1.getCol();
        int toRow = piece2.getRow();
        int toCol = piece2.getCol();

        int count = 0;

        if (fromRow == toRow) {
            // 水平方向
            int startCol = Math.min(fromCol, toCol);
            int endCol = Math.max(fromCol, toCol);

            for (int col = startCol + 1; col < endCol; col++) {
                if (getPieceAt(fromRow, col) != null) {
                    count++;
                }
            }
        } else if (fromCol == toCol) {
            // 垂直方向
            int startRow = Math.min(fromRow, toRow);
            int endRow = Math.max(fromRow, toRow);

            for (int row = startRow + 1; row < endRow; row++) {
                if (getPieceAt(row, fromCol) != null) {
                    count++;
                }
            }
        }

        // 炮吃子要求中间恰好有一个棋子
        return count == 1;
    }
    /**
     * 移动棋子（通过坐标）
     * @param fromRow 起始行
     * @param fromCol 起始列
     * @param toRow 目标行
     * @param toCol 目标列
     * @return 移动是否成功
     */
    public boolean movePiece(int fromRow, int fromCol, int toRow, int toCol) {
        AbstractPiece piece = getPieceAt(fromRow, fromCol);
        if (piece == null) {
            return false;
        }
        return movePiece(piece, toRow, toCol);
    }

    /**
     * 放置棋子（用于初始化或特殊操作）
     */
    public void placePiece(AbstractPiece piece, int row, int col) {
        // 移除该位置原有的棋子
        AbstractPiece existingPiece = getPieceAt(row, col);
        if (existingPiece != null) {
            pieces.remove(existingPiece);
        }

        // 放置新棋子
        piece.moveTo(row, col);
        if (!pieces.contains(piece)) {
            pieces.add(piece);
        }
    }

    /**
     * 移除指定位置的棋子
     */
    public void removePiece(int row, int col) {
        AbstractPiece piece = getPieceAt(row, col);
        if (piece != null) {
            pieces.remove(piece);
        }
    }

    /**
     * 检查是否被将军
     * @param isRed 检查红方还是黑方
     * @return 是否被将军
     */
    public boolean isInCheck(boolean isRed) {
        // 找到指定颜色的将/帅
        GeneralPiece general = findGeneral(isRed);
        if (general == null) {
            return false;
        }

        int generalRow = general.getRow();
        int generalCol = general.getCol();

        // 检查所有对方棋子是否能攻击到将/帅
        for (AbstractPiece piece : pieces) {
            if (piece.isRed() != isRed) {
                // 将帅直接对面的特殊规则
                if (piece instanceof GeneralPiece) {
                    if (isGeneralsFacingEachOther()) {
                        return true;
                    }
                    continue;
                }

                // 检查该棋子是否能移动到将/帅的位置
                if (piece.canMoveTo(generalRow, generalCol, this)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 检查是否将死
     * @param isRed 检查红方还是黑方
     * @return 是否将死
     */
    public boolean isCheckmate(boolean isRed) {
        // 首先检查是否被将军
        if (!isInCheck(isRed)) {
            return false;
        }

        // 尝试所有可能的移动，看是否能解除将军状态
        for (AbstractPiece piece : pieces) {
            if (piece.isRed() == isRed) {
                // 获取该棋子的所有可能移动
                List<int[]> possibleMoves = getPossibleMovesForPiece(piece);

                for (int[] move : possibleMoves) {
                    int toRow = move[0];
                    int toCol = move[1];

                    // 模拟移动并检查是否安全
                    if (simulateMoveAndCheckSafety(piece, toRow, toCol, isRed)) {
                        return false; // 找到一个可以解除将军的移动，不是将死
                    }
                }
            }
        }

        // 没有找到可以解除将军的移动，是将死
        return true;
    }

    /**
     * 检查将帅是否直接对面
     */
    private boolean isGeneralsFacingEachOther() {
        GeneralPiece redGeneral = findGeneral(true);
        GeneralPiece blackGeneral = findGeneral(false);

        if (redGeneral == null || blackGeneral == null) {
            return false;
        }

        // 检查是否在同一列
        if (redGeneral.getCol() != blackGeneral.getCol()) {
            return false;
        }

        // 检查中间是否有棋子遮挡
        int startRow = Math.min(redGeneral.getRow(), blackGeneral.getRow()) + 1;
        int endRow = Math.max(redGeneral.getRow(), blackGeneral.getRow()) - 1;

        for (int row = startRow; row <= endRow; row++) {
            if (getPieceAt(row, redGeneral.getCol()) != null) {
                return false; // 有棋子遮挡
            }
        }

        return true; // 将帅直接对面
    }

    /**
     * 找到指定颜色的将/帅
     */
    private GeneralPiece findGeneral(boolean isRed) {
        for (AbstractPiece piece : pieces) {
            if (piece instanceof GeneralPiece && piece.isRed() == isRed) {
                return (GeneralPiece) piece;
            }
        }
        return null;
    }

    /**
     * 获取棋子的所有可能移动位置
     */
    private List<int[]> getPossibleMovesForPiece(AbstractPiece piece) {
        List<int[]> moves = new ArrayList<>();

        // 检查所有可能的位置
        for (int toRow = 0; toRow < ROWS; toRow++) {
            for (int toCol = 0; toCol < COLS; toCol++) {
                if (piece.canMoveTo(toRow, toCol, this)) {
                    moves.add(new int[]{toRow, toCol});
                }
            }
        }

        return moves;
    }

    /**
     * 模拟移动并检查移动后是否安全
     */
    private boolean simulateMoveAndCheckSafety(AbstractPiece piece, int toRow, int toCol, boolean isRed) {
        // 保存移动前的状态
        int originalRow = piece.getRow();
        int originalCol = piece.getCol();
        AbstractPiece targetPiece = getPieceAt(toRow, toCol);

        // 执行模拟移动
        piece.moveTo(toRow, toCol);
        if (targetPiece != null) {
            pieces.remove(targetPiece);
        }

        // 检查移动后是否仍然被将军
        boolean stillInCheck = isInCheck(isRed);

        // 恢复棋盘状态
        piece.moveTo(originalRow, originalCol);
        if (targetPiece != null) {
            pieces.add(targetPiece);
        }

        return !stillInCheck;
    }

    /**
     * 获取当前回合的玩家
     * @return true-红方, false-黑方
     */
    public boolean isRedTurn() {
        return redTurn;
    }

    /**
     * 设置回合（用于游戏控制）
     */
    public void setRedTurn(boolean redTurn) {
        this.redTurn = redTurn;
    }

    /**
     * 获取游戏状态描述
     */
    public String getGameStatus() {
        if (isCheckmate(true)) {
            return "黑方胜利！红方被将死";
        } else if (isCheckmate(false)) {
            return "红方胜利！黑方被将死";
        } else if (isInCheck(true)) {
            return "红方被将军！";
        } else if (isInCheck(false)) {
            return "黑方被将军！";
        } else {
            return redTurn ? "红方回合" : "黑方回合";
        }
    }

    /**
     * 检查游戏是否结束
     */
    public boolean isGameOver() {
        return isCheckmate(true) || isCheckmate(false);
    }

    public static int getRows() {
        return ROWS;
    }

    public static int getCols() {
        return COLS;
    }
}

