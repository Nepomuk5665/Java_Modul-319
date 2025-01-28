public class Main {
    public static void main(String[] args) {
        char[][] board = {
                {'t', 's', 'l', 'q', 'k', 'l', 's', 't'},
                {'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b'},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B'},
                {'T', 'S', 'L', 'Q', 'K', 'L', 'S', 'T'},
        };

        printBoard(board);
        move(board, 1, 0, 3, 0);
        move(board, 6, 1, 4, 1);
        printBoard(board);
    }

    static void printBoard(char[][] board) {
        for (char[] row : board) {
            for (char piece : row) {
                System.out.print(piece + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    static void move(char[][] board, int fromX, int fromY, int toX, int toY) {
        board[toX][toY] = board[fromX][fromY];
        board[fromX][fromY] = ' ';
    }
}