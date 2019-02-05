package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

class Game {

	private int wght // contient a chaque moment le plus grand poid d'une ligne ou col ou diag du
						// cote adversaire
			, pds // contient le poids des lignes calculer du cote de l'IA
			, pds1 // contient le poids des colonnes calculer du cote de l'IA
			, pds2 // contient le poids des diagonales calculer du cote de l'IA
			, poids; // va permettre d'enticiper le jeu de l'adversaire.
	private int[] board = new int[2]; // contient les indices de la case a jouer si on trouve le poids egal a 4
	private int a,b; // les indices de la case dont le poids est egale a 4

	/**
	 * @param tab la table du jeu
	 * @return retourne la liste des cases potentielles que l'uilisateur peut
	 *         utiliser pour gagner
	 */
	private List<String> fullWeightList(char[][] tab) {
		wght = 0;
		List<String> list = new ArrayList<>(); // les indices des cases potentielles seront mise dans cette liste
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (tab[i][j] == '-' && wght < caseWeight(i, j, tab, "user")) {
					list.clear(); // la liste va etre effacer si on trouve un plus grand poids
					wght = caseWeight(i, j, tab, "user");
					list.add(i + "," + j); // on ajoute les nouvelle case a la liste
				} else if (tab[i][j] == '-' && wght == caseWeight(i, j, tab, "user"))
					list.add(i + "," + j); // ajoute des case a la liste sans la supprimer
			}
		}
		Collections.sort(list);
		return list;
	}

	/**
	 * @param tab1  table du jeu
	 * @param a     le rang de la ligne ou col ou diag
	 * @param label permet d'indiquer si c'est une ligne ou une colonne ou une
	 *              diagonale.
	 * @return parcours une ligne ou une colonne ou une diagonale de rang 'a' et
	 *         retourne les indices de la case libre sur celle-ci sous forme de
	 *         tableau
	 */
	private int[] fillBoard(char[][] tab1, int a, int label) { // pour les lignes
		int[] tab = new int[2];
		switch (label) {
		case 0:
			for (int i = 0; i < 3; i++) {
				if (tab1[a][i] == '-') {
					tab = new int[] {a,i};
					break;
				} // donne a tab les indices de la case libre sur la ligne
			}
			break;
		case 1:
			for (int i = 0; i < 3; i++) {
				if (tab1[i][a] == '-') {
					tab = new int[] {i,a};
					break;
				} // donne a tab les indices de la case libre sur la colonne
			}
			break;
		case 2:
			for (int i = 0; i < 3; i++) {
				switch (a) {
				case 0:
					if (tab1[a + i][i] == '-') {
						tab = new int[] {a+i,i};
						break;
					}
					break;
				case 1:
					if (tab1[i][2 - i] == '-') {
						tab = new int[] {i,2-i};
						break;
					}
				}// donne a tab les indices de la case libre sur la diagonale
			}
			break;
		}

		return tab;
	}

	/**
	 * @param tab1 table du jeu
	 * @return retourne les indices d'une case si elle est gagnante pour l'IA.
	 */
	private int[] firstCase(char[][] tab1) {
		int[] tab = new int[2];
		for (int i = 0; i < 3; i++) {
			pds1 = playerWeight(tab1[i][0], tab1[i][1], tab1[i][2], "IA");
			pds2 = playerWeight(tab1[0][i], tab1[1][i], tab1[2][i], "IA");
			if (i < 2)
				pds = playerWeight(tab1[0][2 * i], tab1[1][1], tab1[2][2 - 2 * i], "IA");
			if (pds1 == 3) {
				tab = fillBoard(tab1, i, 0);
				break;
			} else if (pds2 == 3) {
				tab = fillBoard(tab1, i, 1);
				break;
			} else if (pds == 3) {
				tab = fillBoard(tab1, i, 2);
				break;
			}
		}
		return tab;
	}

	/**
	 * @param tab1 la table de jeu
	 * @return retourne un tableau contenant la ligne et la colonne pour empecher
	 *         l'adversaire de realiser son jeu.
	 */
	private int[] prevision(char[][] tab1) {
		int[] tab = new int[2];
		for (int i = 0; i < 3; i++) {
			a = i;
			for (int j = 0; j < 3; j++) {
				b = j;
				if (i+j != 1 && i+j != 3 && tab1[i][j]=='-') {
					poids = caseWeight(i, j, tab1, "user");
					if (poids == 4) { // le poids de 4 specifie precisement quand l'adversaire veut piege l'IA
						if (tab1[i][1] == '-')
							tab = new int[]{i, 1};
						else if (tab1[1][j] == '-')
							tab = new int[]{1, j};
						break;
					}
				}
			}
			if (poids == 4)
				break;
		}		
		return tab;
	}

	private int validPrediction(char[][] tab1) {
		int[] tab = prevision(tab1);
		boolean test = (tab[0] == 1 && (tab1[1][0] != '-' || tab1[1][2] != '-')),
				test1 = (tab[1] == 1 && (tab1[0][1] != '-' || tab1[2][1] != '-')),
				test3 = (poids == 4 && rowColDiagWeight(tab1) == 3) || (poids == 4 && tab1[1][1] == '-'),
				test4 = (playerWeight(tab1[a][0], tab1[a][1], tab1[a][2], "user") != 2),
				test5 = (playerWeight(tab1[0][b], tab1[1][b], tab1[2][b], "user") != 2);
		if (test3)
			poids = 0;
		if (test1 || test)
			poids = 0;
		if ( test4)
			poids = 0;
		if ( test5)
			poids = 0;
		return poids;
	}

	/**
	 * @param tab la table du jeu.
	 * @return retourne le poids le plus grand de la ligne, colonne ou diagonale du
	 *         cote de l'adversaire.
	 */
	private int rowColDiagWeight(char[][] tab) {
		int a, b, c = 0, d = 0;
		for (int i = 0; i < 3; i++) {
			a = playerWeight(tab[i][0], tab[i][1], tab[i][2], "user");
			b = playerWeight(tab[0][i], tab[1][i], tab[2][i], "user");
			if (i < 2)
				c = playerWeight(tab[0][2 * i], tab[1][1], tab[2][2 - 2 * i], "user");
			d = (a > d) ? a : d;
			d = (b > d) ? b : d;
			d = (c > d) ? c : d;
			if (a == 3) {
				board = fillBoard(tab, i, 0);
				d =a;
				break;
			}
			if (b == 3) {
				board = fillBoard(tab, i, 1);
				d=b;
				break;
			}
			if (c == 3 && i < 2) {
				board = fillBoard(tab, i, 2);
				d=c;
				break;
			}
		}
		return d;
	}

	/**
	 * @param tab1 table du jeu
	 * @return retourne la meilleur case a jouer si il n'y a pas de case gagnante
	 *         pour l'IA
	 */
	int[] caseToPlay(char[][] tab1) {
		int[] tab;
		List<String> list;
		tab = firstCase(tab1);
		if (pds != 3 && pds1 != 3 && pds2 != 3) {
			tab = prevision(tab1);
			list = fullWeightList(tab1);
			if (validPrediction(tab1) != 4) {
				if (rowColDiagWeight(tab1) == 3) {
					tab = board;
				} else {
					AtomicInteger wght = new AtomicInteger();
					for (String s : list) {
						int li = Integer.valueOf(s.split(",")[0]);
						int col = Integer.valueOf(s.split(",")[1]);
						if (caseWeight(li, col, tab1, "IA") > wght.get()) {
							wght.set(caseWeight(li, col, tab1, "IA"));
							tab = new int[] { li, col };
						} else if (caseWeight(li, col, tab1, "IA") == wght.get())
							tab = new int[] { li, col };
					}
				}
			}
		}
		return tab;
	}

	/**
	 * @param c1,c2,c3 represente les cases des ligne ou colonnes ou diagonales
	 * @return calcule et retourne le poid d'une ligne ou col ou diag du cote
	 *         adversaire ou du cote de l'IA
	 */
	private int playerWeight(char c1, char c2, char c3, String label) {
		char caract = (label.equals("IA")) ? Play.mark : Play.computerMark;
		if ((c1 == caract || c2 == caract || c3 == caract))
			return 0;
		else if ((c1 == c2 && c1 != '-') || (c1 == c3 && c1 != '-') || (c3 == c2 && c2 != '-'))
			return 3;
		else if (c1 == '-' && c2 == '-' && c3 == '-')
			return 1;
		else
			return 2;
	}

	/**
	 * @param li  indice de la ligne
	 * @param col indice de la colonne
	 * @param tab la table de jeu
	 * @return calcul et retourne le poid d'une case en ne faisant que la somme des
	 *         poids de la ligne la col ou la diag sur laquelle se trouve cette
	 *         case.
	 */
	private int caseWeight(int li, int col, char[][] tab, String label) {
		int weight, weight1, weight2 = 0;
		int weight3 = playerWeight(tab[li][0], tab[li][1], tab[li][2], label);
		int weight4 = playerWeight(tab[0][col], tab[1][col], tab[2][col], label);
		if ((li + col) % 2 == 0) {
			weight = playerWeight(tab[0][0], tab[1][1], tab[2][2], label);
			weight1 = playerWeight(tab[0][2], tab[1][1], tab[2][0], label);
			weight2 = (col == 1 && li == 1) ? (weight + weight1) : (li == col) ? weight : weight1;
		}
		return (weight2 + weight3 + weight4);
	}
}
