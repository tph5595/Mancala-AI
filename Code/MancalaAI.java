import java.util.ArrayList;
import java.util.Arrays;

public class MancalaAI {
	int ply = 1;
	Tuple bad = new Tuple(Integer.MIN_VALUE, "");
	Tuple max = new Tuple(Integer.MAX_VALUE, "");

	public String nextmove(int[] board) {
		return search(ply, board).move;
	}

	public Tuple search(int ply, int[] mboard) {
		int alphaPrune = Integer.MIN_VALUE;
		// if on last layer then search for last ply and then evaluate
		if (ply - 1 == 0) {
			// set current best move to -inf
			Tuple bb = bad;
			// create instance of board for local search instance
			ArrayList<int[]> board = new ArrayList<int[]>();
			// search for max's move
			for (int j = 0; j <= 5; j++) {
				// find possible board states from starting move (accounts for
				// "free" moves)
				// find possible board outcomes of mins move aj
				//check if there are pieces in the position selected
				if (!this.invalid("a" + j, mboard)) {
					board = this.untilQuite(mboard, "a" + j);
					Tuple b = null;
					// run through all of mins move aj
					for (int[] curBoard : board) {
						// calculate max's move b0 by finding all boards
						// possible
						// from b0 and taking the min and then creating a board
						// state from that
						//set min to +inf
						b = max;
						// run through all possible starting moves
						for (int i = 0; i <= 5; i++) {
							// if we have found a value less then the current
							// prune
							// then stop
							if (b.value < alphaPrune) {
								return b;
							}
							//check if there are pieces in the position selected
							if (!this.invalid("b" + i, curBoard)) {
								// else calculate next branch same as we did for
								// b0
								b = Tuple.min(b, new Tuple(this.hueristic(this.min(this.untilQuite(curBoard, "b" + i))),
										"b" + i));
							}
						}
						// find the max of all the current min moves
						bb = Tuple.max(bb, b);
						// if this max is less than the current prune then set
						// prune
						// to this new max
						if (bb.value < alphaPrune) {
							alphaPrune = bb.value;
						}
					}
				}
			}
			// return best state move
			return bb;
		} else {
			Tuple bb = bad;
			ArrayList<int[]> aboards = new ArrayList<int[]>();
			ArrayList<int[]> bboards = null;
			for (int j = 0; j <= 5; j++) {
				aboards = this.untilQuite(mboard, "a" + j);
				Tuple b = bad;
				for (int[] curBoard : aboards) {
					bboards = this.untilQuite(curBoard, "b0");
					for (int[] bboard : bboards) {
						b = Tuple.min(b, search(ply - 1, bboard));
					}
					for (int i = 1; i <= 5; i++) {
						if (b.value < alphaPrune) {
							return b;
						}
						bboards = this.untilQuite(curBoard, "b" + i);
						for (int[] bboard : bboards) {
							b = Tuple.min(b, search(ply - 1, bboard));
						}
					}
					bb = Tuple.max(bb, b);
					if (bb.value < alphaPrune) {
						alphaPrune = bb.value;
					}
				}
			}
			return bb;
		}
	}

	public int hueristic(int[] board) {
		return board[13]-board[6];
	}

	private int[] min(ArrayList<int[]> boards) {
		int b = this.hueristic(boards.get(0));
		int[] ba = boards.get(0);
		int nb;
		for (int[] bc : boards) {
			nb = this.hueristic(bc);
			if (nb < b) {
				b = nb;
				ba = bc;
			}
		}
		return ba;
	}

	private ArrayList<int[]> untilQuite(int[] board, String startMove) {
		boolean playerTurn = false;
		String ab = startMove.substring(0, 1);
		ArrayList<int[]> inboards = new ArrayList<int[]>();
		ArrayList<int[]> outboards = new ArrayList<int[]>();
		playerTurn = makeMove(startMove, playerTurn, board);
		if (playerTurn) {
			outboards.add(board);
		} else {
			inboards.add(board);
		}
		int[] cur = null;
		while (inboards.size() > 0) {
			for (int[] aBoard : inboards) {
				for (int i = 0; i <= 5; i++) {
					if (!this.invalid(ab + i, aBoard)) {
						cur = Arrays.copyOf(aBoard, aBoard.length);
						playerTurn = makeMove(ab + i, playerTurn, cur);
						if (playerTurn) {
							outboards.add(cur);
						} else {
							inboards.add(cur);
						}
					}
				}
			}
		}
		return outboards;
	}

	private boolean makeMove(String move, boolean playerTurn, int[] board) {
		int piecesInHand;
		if (move.charAt(0) == 'a') {
			piecesInHand = board[Math.abs((move.charAt(1) - '0') - 5)];
		} else {
			piecesInHand = board[(move.charAt(1) - '0') + 7];
		}
		board[Math.abs((move.charAt(1) - '0') - 5)] = 0;
		int pos = Math.abs((move.charAt(1) - '0') - 5);
		while (piecesInHand > 0) {
			pos = (pos + 1) % board.length;
			board[pos]++;
			piecesInHand--;
		}
		if ((pos == 6 && move.charAt(0) == 'a') || (pos == 13 && move.charAt(0) == 'b'))
			return playerTurn;
		return !playerTurn;
	}

	private boolean invalid(String input, int[] board) {
		boolean inrange = input.matches("[a-b][0-5]");
		if (!inrange)
			return true;
		if (input.charAt(0) == 'b' && board[(input.charAt(1) - '0') + 7] == 0)
			return true;
		if (input.charAt(0) == 'a' && board[Math.abs((input.charAt(1) - '0') - 5)] == 0)
			return true;
		return false;
	}
}
