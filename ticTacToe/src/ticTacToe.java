import java.util.*;

public class ticTacToe {
    static ArrayList<Integer> playerPositions = new ArrayList<>();
    static ArrayList<Integer> cpuPositions = new ArrayList<>();

    static char[][] gameBoard = {{' ', '|', ' ', '|', ' '},
            {'-', '+', '-', '+', '-'},
            {' ', '|', ' ', '|', ' '},
            {'-', '+', '-', '+', '-'},
            {' ', '|', ' ', '|', ' '}};


    public static void main(String[] args) {

        printBoard(gameBoard);

        boolean[] takenPositions = new boolean[10]; // To keep track of taken positions; Ignore index 0 for simplicity
        Scanner scan = new Scanner(System.in);
        int position;

        while (true) {
            while (true) {
                try {
                    System.out.println("Enter your placement (1-9): ");
                    position = scan.nextInt();

                    if (position < 1 || position > 9) {
                        System.out.println("Must be between 1 and 9.");
                    } else if (takenPositions[position]) {
                        System.out.println("Position already taken. Choose another.");
                    } else {
                        takenPositions[position] = true;
                        System.out.println("You entered: " + position);
                        break; // Breaks the loop once a valid input is given.
                    }
                } catch (InputMismatchException e) {
                    System.out.println("That's not a valid number. Please enter a number between 1 and 9.");
                    scan.nextLine(); // Clear the scanner's buffer
                }
            }

            placeMark(gameBoard, position, "player");
            printBoard(gameBoard);
            String result = checkWinner();
            if(result.length() > 0) {
                System.out.println(result);
                break;
            }

            int bestMove = -1;
            int bestScore = Integer.MIN_VALUE;
            for (int i = 1; i <= 9; i++) {
                if (!playerPositions.contains(i) && !cpuPositions.contains(i)) {
                    placeMark(gameBoard, i, "cpu"); // want to simulate a move
                    int score = miniMax(gameBoard, false);
                    cpuPositions.remove((Integer) i); // Undo the move
                    placeMark(gameBoard, i, "empty"); // clear the move from the board
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = i;
                    }
                }
            }

            if (bestMove != -1) { // check whether a valid best move has been found
                takenPositions[bestMove] = true;
                placeMark(gameBoard, bestMove, "cpu");
            }
            System.out.println("Computer chose: " + bestMove);
            printBoard(gameBoard);


            result = checkWinner();
            if(result.length() > 0) {
                System.out.println(result);
                break;
            }
        }
    }

    // has to be static because it belongs to the class rather than an object
    // without static you'd have to create an object to use it
    public static void printBoard(char[][] gameBoard) {
        for (char[] row : gameBoard) {
            for (char c : row) {
                System.out.print(c);
            }
            System.out.println();
        }
    }

    public static void placeMark(char[][] gameBoard, int position, String user) {

        char symbol = ' ';

        switch (user) {
            case "player":
                symbol = 'X';
                playerPositions.add(position);
                break;
            case "cpu":
                symbol = 'O';
                cpuPositions.add(position);
                break;
            case "empty":
                break; // Just keep it ' '
        }
        switch (position) {
            case 1:
                gameBoard[0][0] = symbol;
                break;
            case 2:
                gameBoard[0][2] = symbol;
                break;
            case 3:
                gameBoard[0][4] = symbol;
                break;
            case 4:
                gameBoard[2][0] = symbol;
                break;
            case 5:
                gameBoard[2][2] = symbol;
                break;
            case 6:
                gameBoard[2][4] = symbol;
                break;
            case 7:
                gameBoard[4][0] = symbol;
                break;
            case 8:
                gameBoard[4][2] = symbol;
                break;
            case 9:
                gameBoard[4][4] = symbol;
                break;
            default:
                break;
        }
    }

    public static String checkWinner() {
        List<Integer> topRow = Arrays.asList(1, 2, 3);
        List<Integer> midRow = Arrays.asList(4, 5, 6);
        List<Integer> btmRow = Arrays.asList(7, 8, 9);
        List<Integer> leftCol = Arrays.asList(1, 4, 7);
        List<Integer> midCol = Arrays.asList(2, 5, 8);
        List<Integer> rightCol = Arrays.asList(3, 6, 9);
        List<Integer> cross1 = Arrays.asList(1, 5, 9);
        List<Integer> cross2 = Arrays.asList(3, 5, 7);

        List<List> winningConditions = new ArrayList<>();
        winningConditions.add(topRow);
        winningConditions.add(midRow);
        winningConditions.add(btmRow);
        winningConditions.add(leftCol);
        winningConditions.add(midCol);
        winningConditions.add(rightCol);
        winningConditions.add(cross1);
        winningConditions.add(cross2);

        for (List l : winningConditions) {
            if (playerPositions.containsAll(l)) {
                return "You won!";
            } else if (cpuPositions.containsAll(l)) {
                return "You lost";
            } else if (playerPositions.size() + cpuPositions.size() == 9)
                return "No winner";
        }
        return "";
    }
    public static int miniMax(char[][] board, boolean isCPU){
        String result = checkWinner();

        // base cases
        if ("You won!".equals(result)) return -10;
        if ("You lost".equals(result)) return 10;
        if ("No winner".equals(result)) return 0;

        int bestScore;

        if (isCPU) {
            bestScore = Integer.MIN_VALUE;
            for (int i = 1; i <= 9; i++) {
                if (!playerPositions.contains(i) && !cpuPositions.contains(i)) {
                    placeMark(board, i, "cpu");
                    int score = miniMax(board, false);
                    cpuPositions.remove((Integer) i); // Undo the move
                    placeMark(board, i, "empty");
                    bestScore = Math.max(bestScore, score);
                }
            }
        } else {  // simulate optimal player moves
            bestScore = Integer.MAX_VALUE;
            for (int i = 1; i <= 9; i++) {
                if (!playerPositions.contains(i) && !cpuPositions.contains(i)) {
                    placeMark(board, i, "player");
                    int score = miniMax(board, true);
                    playerPositions.remove((Integer) i); // Undo the move
                    placeMark(board, i, "empty");
                    bestScore = Math.min(bestScore, score);
                }
            }
        }
        return bestScore;
    }
}
