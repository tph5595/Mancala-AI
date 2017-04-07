import java.util.Scanner;

public class Mancala {
	int bvalues = 3;
	int[] board = { bvalues, bvalues, bvalues, bvalues, bvalues, bvalues, 0, bvalues, bvalues, bvalues, bvalues, bvalues, bvalues, 0 };
	MancalaAI AI;
	MancalaAI AI2;
	boolean playerTurn = true;
	private boolean aiGame = false;
	boolean quite = false;
	Scanner console = new Scanner(System.in);

	public Mancala(MancalaAI mancalaAI) {
		this.AI = mancalaAI;
	}

	public Mancala(MancalaAI ai1, MancalaAI ai2) {
		this.AI = ai1;
		this.AI2 = ai2;
		this.aiGame = true;
		quite = true;
	}

	public void displayBoard(int[] board) {
		System.out.print("          0    1    2    3    4    5");
		for (int i = 0; i < 2; i++) {
			System.out.print("\n   ");
			if (i != 0) {
				System.out.print("|");
				if (board[6] / 10 == 0) {
					System.out.print(" ");
				}
				System.out.print(board[6] + " ");
			}
			for (int k = 0; k < 39 + (i * (-8)); k++)
				System.out.print("-");
			if (i != 0) {
				if (board[13] / 10 == 0) {
					System.out.print(" ");
				}
				System.out.print("" + board[13] + " ");

				System.out.print("|");
			}
			System.out.print("\n");
			if (i == 0)
				System.out.print("a");
			else
				System.out.print("b");
			if (i == 0) {

				System.out.print("  |   | ");
				if (board[5] / 10 == 0) {
					System.out.print(" ");
				}
				System.out.print(board[5] + " |");
				for (int j = 4; j >= 0; j--) {
					if (board[j] / 10 == 0) {
						System.out.print(" ");
					}
					System.out.print(" " + board[j] + " |");
				}
			} else {
				System.out.print("  |   | ");
				if (board[7] / 10 == 0) {
					System.out.print(" ");
				}
				System.out.print(board[7] + " |");
				for (int j = 1; j < 6; j++) {
					if (board[i * 7 + j] / 10 == 0) {
						System.out.print(" ");
					}
					System.out.print(" " + board[i * 7 + j] + " |");
				}
			}
			System.out.print("   |");
		}
		System.out.print("\n   ");
		for (int k = 0; k < 39; k++)
			System.out.print("-");
		System.out.println();
	}

	public boolean run() {
		String move = null;
		while (!this.isGameOver()) {
			if(!quite)
				this.displayBoard(board);
			if (playerTurn && !this.aiGame)
				move = promptPlayer();
			else if (playerTurn && this.aiGame) {
				move = promptAI2();
			} else
				move = promptAI();
			playerTurn = makeMove(move, playerTurn);
		}
		System.out.println("\n\n\nGAME OVER\nFinal Board: ");
		this.displayBoard(board);
		if (board[13] - board[6] > 0) {
			System.out.println("AI won!");
			return true;
		} else {
			System.out.println("You won!");
			return false;
		}

	}

	private boolean makeMove(String move, boolean playerTurn) {
		if(!quite)
			this.printmove(move, playerTurn);
		int piecesInHand;
		int pos;
		if (move.charAt(0) == 'a') {
			piecesInHand = board[Math.abs((move.charAt(1) - '0') - 5)];
			pos = Math.abs((move.charAt(1) - '0') - 5);
			board[Math.abs((move.charAt(1) - '0') - 5)] = 0;
		} else {
			piecesInHand = board[(move.charAt(1) - '0') + 7];
			pos = (move.charAt(1) - '0') + 7;
			board[(move.charAt(1) - '0') + 7] = 0;
		}

		while (piecesInHand > 0) {
			pos = (pos + 1) % board.length;
			board[pos]++;
			piecesInHand--;
		}
		if ((pos == 6 && move.charAt(0) == 'a') || (pos == 13 && move.charAt(0) == 'b'))
			return playerTurn;
		return !playerTurn;
	}

	private void printmove(String move, boolean playerTurn) {
		if (playerTurn) {
			System.out.println("move :" + move);
			System.out.println("You moved pieces from " + move.substring(0, 2));
		} else {
			System.out.println("move :" + move);
			System.out.println("The AI moved pieces from " + move.substring(0, 2));
		}

	}

	private String promptAI() {
		return AI.nextmove(board);

	}

	private String promptAI2() {
		return AI2.nextmove(board, true);

	}

	private String promptPlayer() {
		String input;
		boolean invalid;
		do {
			System.out.print("What move would you like to make? ");
			input = console.next();
			invalid = invalid(input);
			if (invalid) {
				System.out.println("INVALID INPUT\n");
			}
		} while (invalid);
		return input.substring(0, 2);

	}

	private boolean invalid(String input) {
		boolean inrange = input.matches("[a-b][0-5]");
		if (!inrange)
			return true;
		if (input.charAt(0) == 'b')
			return true;
		if (board[Math.abs((input.charAt(1) - '0') - 5)] == 0)
			return true;
		return false;
	}

	private boolean isGameOver() {
		return (sumOfSubArray(0, 6) == 0 || sumOfSubArray(7, 13) == 0);
	}

	private int sumOfSubArray(int start, int end) {
		int count = 0;
		for (int i = start; i < end; i++)
			count += board[i];
		return count;
	}
}
