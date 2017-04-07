import java.util.Arrays;

public class Main {

	public static void main(String[] args) {
		boolean learn = true;
		if (learn) {
			learn(10, 10, 5, -100, 100);
		} else {
			Mancala game = new Mancala(new MancalaAI());
			game.run();
		}
	}

	public static void learn(int pop, int gens, int games, int min, int max) {
		Mancala game;
		int[][] members = new int[pop][14];
		double[] wins = null;
		boolean win;
		int [] played = new int[pop];
		int rand;

		randomStart(members, min, max);
		for (int i = 0; i < gens; i++) {
			wins = new double[pop];
			for (int j = 0; j < pop; j++) {
				for (int k = 0; k < games; k++) {
					rand = (int) (Math.random() * pop);
					while(rand == k){
						rand = (int)(Math.random() * pop);
					}
					System.out.println("Generation: " + i + "\tChild: " + j + "\tGame: " + k);
					game = new Mancala(new MancalaAI(members[k]), new MancalaAI(members[rand]));
					win = game.run();
					played[k]++;
					played[rand]++;
					if (win) {
						wins[k]++;
					} else {
						wins[rand]++;
					}
				}
			}
			int maxpos = 0;
			for(int m = 0; m < wins.length; m++){
				if(wins[m]/played[m] > wins[maxpos]/played[maxpos]){
					maxpos = m;
				}
			}
			System.out.println(Arrays.toString(members[maxpos]));
			if (i < gens - 1)
				evolve(members, wins, games * pop, min, max, played);
		}
		int maxpos = 0;
		for(int i = 0; i < wins.length; i++){
			if(wins[i] > wins[maxpos]){
				maxpos = i;
			}
		}
		System.out.println("Best values are: " + Arrays.toString(members[maxpos]));
	}

	private static void evolve(int[][] members, double[] wins, int totalGames, int min, int max, int [] played) {
		// find probability of each value in array
		for (int i = 0; i < wins.length; i++) {
			wins[i] = wins[i] / played[i];
		}
		// find probability ranges
		float runningTotal = 0;
		for (int i = 0; i < wins.length; i++) {
			runningTotal += wins[i];
			wins[i] = runningTotal;
		}
		// crossover
		double rand1;
		double rand2;
		int a;
		int b;
		int p;
		int cur = 0;
		int[][] m2 = new int[members.length][14];
		for (int i = 0; i < wins.length; i++) {
			p = 0;
			rand1 = Math.random();
			rand2 = Math.random();
			while (wins[p] < rand1) {
				p++;
			}
			a = p;
			p = 0;
			while (wins[p] < rand2) {
				p++;
			}
			b = p;
			crossover(m2, cur, members[a], members[b]);
			if (Math.random() < 0.3) {
				mutate(m2, cur, min, max);
			}
			cur++;
		}
		members = m2;
	}

	private static void mutate(int[][] m2, int cur, int min, int max) {
		int value = min + (int) (Math.random() * max);
		int pos = (int) (Math.random() * 14);
		m2[cur][pos] = value;
	}

	private static void crossover(int[][] m2, int cur, int[] is, int[] is2) {
		for (int i = 0; i <= 6; i++) {
			m2[cur][i] = is[i];
		}
		// 6
		for (int i = 7; i <= 13; i++) {
			m2[cur][i] = is2[i];
		}
		// 13
	}

	private static void randomStart(int[][] members, int min, int max) {
		for (int i = 0; i < members.length; i++) {
			for (int j = 0; j < 14; j++) {
				//System.out.println(i+"\t"+j);
				members[i][j] = min + (int) (Math.random() * max);
			}
		}

	}
}
