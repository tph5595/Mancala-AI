import java.util.ArrayList;
import java.util.Arrays;

public class MancalaAI {
	int ply = 4;
	int sply = ply;
	ArrayList<int[]> inboards = new ArrayList<int[]>();

	Tuple bad = new Tuple(Integer.MIN_VALUE, "i");
	Tuple max = new Tuple(Integer.MAX_VALUE, "a");
	int[] weights = { 1, 1, 1, 2, 2, -1000, -3, -2, -2, -1, -1, -1, -1, 1000 };
	int[] masterBoards = new int[ply * 4];
 
	public MancalaAI(int[] gaValues) {
		weights = gaValues;

	}

	public MancalaAI() {

	}

	public String nextmove(int[] board) {
		ply = (sply)+((board[13]+board[6])/25)+((board[13]+board[6])/35)+((board[13]+board[6])/40);
		System.out.println(ply);
		return search(ply, board, false).move;
	}

	public String nextmove(int[] board, boolean flip) {
		return searchFlipped(ply, board).move;
	}

	public Tuple search(int ply, int[] mboard, boolean flipped) {
		String ac = "a";
		String bc = "b";
		if (flipped) {
			ac = "b";
			bc = "a";
		}
		int alphaPrune = Integer.MIN_VALUE;
		// if on last layer then search for last ply and then evaluate
		if (ply - 1 == 0) {
			// set current best move to -inf
			Tuple bb = bad;
			// create instance of board for local search instance
			ArrayList<int[]> board = new ArrayList<int[]>();
			if (this.isGameOver(mboard)) {
				return new Tuple(this.hueristic(mboard), "n");
			}
			// search for max's move
			for (int j = 0; j <= 5; j++) {
				// find possible board states from starting move (accounts for
				// "free" moves)
				// find possible board outcomes of mins move aj
				// check if there are pieces in the position selected
				if (!this.invalid(bc + j, mboard)) {
					board = this.untilQuite(Arrays.copyOf(mboard, mboard.length), bc + j, false);
					Tuple b = null;
					// System.out.println(board.size());
					// run through all of mins move aj
					for (int[] curBoard : board) {
						// System.out.println("hi");
						// calculate max's move b0 by finding all boards
						// possible
						// from b0 and taking the min and then creating a board
						// state from that
						// set min to +inf
						b = max;
						// run through all possible starting moves
						for (int i = 0; i <= 5; i++) {
							// if we have found a value less then the current
							// prune
							// then stop
							// if (b.value < alphaPrune) {
							// return b;
							// }
							// check if there are pieces in the position
							// selected
							if (!this.invalid(ac + i, curBoard)) {
								// else calculate next branch same as we did for
								// b0
								b = Tuple.min(b,
										new Tuple(
												this.hueristic(this.min(this
														.untilQuite(Arrays.copyOf(curBoard, curBoard.length), ac + i, false))),
										ac + i));
							}
						}
						// find the max of all the current min moves
						if(b.value > bb.value){
							bb = b;
							bb.move = bc + j;
						}
						//bb = Tuple.max(bb, b);
						
						// if this max is less than the current prune then set
						// prune
						// to this new max
						// if (bb.value < alphaPrune) {
						// alphaPrune = bb.value;
						// }
					}
				}
			}
			// return best state move
			return bb;
		} else {
			Tuple bb = bad;
			ArrayList<int[]> aboards = new ArrayList<int[]>();
			ArrayList<int[]> bboards = null;
			if (this.isGameOver(mboard)) {
				return new Tuple(this.hueristic(mboard), "n");
			}
			for (int j = 0; j <= 5; j++) {
				if (!this.invalid(bc + j, mboard)) {
					// System.out.println("b"+j);
					aboards = this.untilQuite(Arrays.copyOf(mboard, mboard.length), bc + j, false);
					Tuple b = max;
					boolean found = false;
					// System.out.println(aboards.size());
					for (int[] curBoard : aboards) {
						if (!this.isGameOver(curBoard)) {
							if (!this.invalid(ac + "0", curBoard)) {
								found = true;
								bboards = this.untilQuite(curBoard, ac + "0", false);
								for (int[] bboard : bboards) {
									b = Tuple.min(b, search(ply - 1, Arrays.copyOf(bboard, bboard.length), flipped));
								}
							}
							for (int i = 1; i <= 5; i++) {
								// if (b.value < alphaPrune) {
								// return b;
								// }
								if (!this.invalid(ac + i, curBoard)) {
									found = true;
									bboards = this.untilQuite(curBoard, ac + i, false);
									for (int[] bboard : bboards) {
										b = Tuple.min(b,
												search(ply - 1, Arrays.copyOf(bboard, bboard.length), flipped));
									}
								}
							}
						} else {
							b = new Tuple(this.hueristic(curBoard), "m");
							found = true;
							// System.out.println(b.value);
						}
						if (found && b.value > bb.value) {
							bb = b;
							bb.move = bc + j;
						}
						// bb = Tuple.max(bb, b);
						// if (bb.value < alphaPrune) {
						// alphaPrune = bb.value;
						// }
					}
				}
			}
			return bb;
		}
	}

	public Tuple searchFlipped(int ply, int[] mboard) {
		int alphaPrune = Integer.MIN_VALUE;
		// if on last layer then search for last ply and then evaluate
		if (ply - 1 == 0) {
			// set current best move to -inf
			Tuple bb = bad;
			// create instance of board for local search instance
			ArrayList<int[]> board = new ArrayList<int[]>();
			if (this.isGameOver(mboard)) {
				return new Tuple(this.hueristic(mboard), "n");
			}
			// search for max's move
			for (int j = 0; j <= 5; j++) {
				// find possible board states from starting move (accounts for
				// "free" moves)
				// find possible board outcomes of mins move aj
				// check if there are pieces in the position selected
				if (!this.invalid("a" + j, mboard)) {
					board = this.untilQuite(Arrays.copyOf(mboard, mboard.length), "a" + j, true);
					Tuple b = null;
					// System.out.println(board.size());
					// run through all of mins move aj
					for (int[] curBoard : board) {
						// System.out.println("hi");
						// calculate max's move b0 by finding all boards
						// possible
						// from b0 and taking the min and then creating a board
						// state from that
						// set min to +inf
						b = max;
						// run through all possible starting moves
						for (int i = 0; i <= 5; i++) {
							// if we have found a value less then the current
							// prune
							// then stop
							// if (b.value < alphaPrune) {
							// return b;
							// }
							// check if there are pieces in the position
							// selected
							if (!this.invalid("b" + i, curBoard)) {
								// else calculate next branch same as we did for
								// b0
								b = Tuple.min(b,
										new Tuple(this.hueristic(this.min(
												this.untilQuite(Arrays.copyOf(curBoard, curBoard.length), "b" + i, true))),
										"b" + i));
							}
						}
						// find the max of all the current min moves
						bb = Tuple.max(bb, b);
						bb.move = "a" + j;
						// if this max is less than the current prune then set
						// prune
						// to this new max
						// if (bb.value < alphaPrune) {
						// alphaPrune = bb.value;
						// }
					}
				}
			}
			// return best state move
			return bb;
		} else {
			Tuple bb = bad;
			ArrayList<int[]> aboards = new ArrayList<int[]>();
			ArrayList<int[]> bboards = null;
			if (this.isGameOver(mboard)) {
				return new Tuple(this.hueristic(mboard), "n");
			}
			for (int j = 0; j <= 5; j++) {
				if (!this.invalid("a" + j, mboard)) {
					// System.out.println("b"+j);
					aboards = this.untilQuite(Arrays.copyOf(mboard, mboard.length), "a" + j, true);
					Tuple b = max;
					boolean found = false;
					// System.out.println(aboards.size());
					for (int[] curBoard : aboards) {
						if (!this.isGameOver(curBoard)) {
							if (!this.invalid("b0", curBoard)) {
								found = true;
								bboards = this.untilQuite(curBoard, "b0", true);
								for (int[] bboard : bboards) {
									b = Tuple.min(b, searchFlipped(ply - 1, Arrays.copyOf(bboard, bboard.length)));
								}
							}
							for (int i = 1; i <= 5; i++) {
								// if (b.value < alphaPrune) {
								// return b;
								// }
								if (!this.invalid("b" + i, curBoard)) {
									found = true;
									bboards = this.untilQuite(curBoard, "b" + i, true);
									for (int[] bboard : bboards) {
										b = Tuple.min(b, searchFlipped(ply - 1, Arrays.copyOf(bboard, bboard.length)));
									}
								}
							}
						} else {
							b = new Tuple(this.hueristic(curBoard), "m");
							found = true;
							// System.out.println(b.value);
						}
						if (found && b.value > bb.value) {
							bb = b;
							bb.move = "a" + j;
						}
						// bb = Tuple.max(bb, b);
						// if (bb.value < alphaPrune) {
						// alphaPrune = bb.value;
						// }
					}
				}
			}
			return bb;
		}
	}

	public int hueristic(int[] board) {
		// Mancala m = new Mancala(null);
		// m.displayBoard(board);
		if (board == null) {
			return Integer.MAX_VALUE;
		}
		if (isGameOver(board) && board[13] - board[6] > 0)
			return Integer.MIN_VALUE + 2;
		else if (this.isGameOver(board))
			return Integer.MAX_VALUE - 1;
		int sum = 0;
		for (int i = 0; i < board.length; i++) {
			sum += (weights[i] * board[i]);
		}
		// System.out.println("board value: " + sum);

		// System.out.println("\n\n\n");
		return sum;
	}

	public int hueristic2(int[] board) {
		// Mancala m = new Mancala(null);
		// System.out.println("board value: " + (board[13] - board[6]));
		// m.displayBoard(board);
		// System.out.println("\n\n\n");
		if (board == null) {
			return Integer.MAX_VALUE;
		}
		if (isGameOver(board) && board[6] - board[13] > 0)
			return Integer.MIN_VALUE + 1;
		else if (this.isGameOver(board))
			return Integer.MAX_VALUE - 1;
		int sum = 0;
		for (int i = 0; i < board.length; i++) {
			sum += (weights[i] * board[i]);
		}
		return sum;
	}

	private int[] min(ArrayList<int[]> boards) {
		if (boards.size() == 0) {
			return null;
		}
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

	private ArrayList<int[]> untilQuite(int[] board, String startMove, boolean flipped) {
		// System.out.println(startMove);
		boolean playerTurn = false;
		Mancala m = new Mancala(null);
		String ab = startMove.substring(0, 1);
		inboards.clear();
		ArrayList<int[]> outboards = new ArrayList<int[]>();
		playerTurn = makeMove(startMove, playerTurn, board);
		// m.displayBoard(board);
		if (playerTurn || this.isGameOver(board)) {
			outboards.add(board);
		} else {
			inboards.add(board);
		}

		int[] cur = null;
		int[] aBoard = null;
		while (inboards.size() > 0) {
			// System.out.println((inboards.size()));
			aBoard = inboards.get(0);
			inboards.remove(0);
			// m.displayBoard(inboards.get(0));
			for (int i = 0; i <= 5; i++) {
				if (!this.invalid(ab + i, aBoard)) {
					playerTurn = false;
					cur = Arrays.copyOf(aBoard, aBoard.length);
					playerTurn = makeMove(ab + i, playerTurn, cur);
					// testing
					
					// end test
					if (playerTurn || this.isGameOver(cur)) {
						outboards.add(cur);
						/*if (cur[12] == 0 && cur[11] == 0 && cur[10] == 0) {
							// Mancala m = new Mancala(null);
							m.displayBoard(cur);
							int sum = 0;
							for (int k = 0; k < cur.length; k++) {
								sum += (weights[k] * cur[k]);
							}
							System.out.println("board value n : " + sum);

							System.out.println("\n\n\n");
						}*/
					} else {
						inboards.add(cur);
					}
				}
			}
		}
		int[] max = outboards.get(0);
		int mv = 0;
		if (!flipped)
			mv = this.hueristic(max);
		if (flipped)
			mv = this.hueristic2(max);
		for (int[] curb : outboards) {
			if (!flipped && this.hueristic(curb) > mv) {
				max = curb;
				mv = this.hueristic(curb);
			}
			if (flipped && this.hueristic2(curb) > mv) {
				max = curb;
				mv = this.hueristic2(curb);
			}
		}
		if(mv > 1000){
			//m.displayBoard(max);
			//System.out.println(mv+"\n"+startMove+"\n\n");
		}
		if (max[12] == 0 && max[11] == 0 && max[10] == 0) {
			//m.displayBoard(max);
			int sum = 0;
			for (int k = 0; k < max.length; k++) {
				sum += (weights[k] * max[k]);
			}
			//System.out.println("board value: " + sum);

			//System.out.println("\n\n\n");
		}
		outboards.clear();
		outboards.add(max);
		return outboards;
	}

	private boolean makeMove(String move, boolean playerTurn, int[] board) {
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

	private boolean isGameOver(int[] board) {
		return (sumOfSubArray(board, 0, 6) == 0 || sumOfSubArray(board, 7, 13) == 0);
	}

	private int sumOfSubArray(int[] board, int start, int end) {
		int count = 0;
		for (int i = start; i < end; i++)
			count += board[i];
		return count;
	}
}
