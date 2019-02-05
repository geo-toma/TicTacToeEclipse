package game;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

class Play {

	static Scanner scan = new Scanner(System.in);
	static char mark;
	static char computerMark;
	private TicTacToe game;

	/**
	 * le constructeur par defaut.
	 */
	Play() {
		// ceci donne la possibilie au joueur 1 de choisir le caractere qu'il veut comme mark;
		// le joueur 2 n'a pas de choix.
		System.out.println("Joueur 1 : Entrez la lettre correspondand a votre pion (en majuscule si possible)");
		mark = scan.next().charAt(0);
		game = new TicTacToe(mark);
		computerMark = game.setComputerMark();
	}

	/**
	 * permet de jouer contre l'IA.
	 * @param level indique le niveau du jeu
	 */
	void playSolo(int level) {
		int[] tab;
		Game game1 = new Game();
		AtomicBoolean test = new AtomicBoolean(false);
		do {
			display(1);
			game.setCurrentPlayerMark(mark);
			playerTurn();
			test.set(game.checkForWin() && !game.isBoardFull());
			if (test.get()) {
				display(0);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					System.out.println("peut pas attendre");
				}
				game.changePlayer();
				do {
					tab = (level == 1) ? averageLevel() : game1.caseToPlay(game.getBoard());
				} while (!game.placeMark(tab[0], tab[1]));
				test.set(game.checkForWin() && !game.isBoardFull());
			}
		} while (test.get());
		conclusion(false);
	}

	/**
	 * permet de jouer a deux.
	 */
	void twoPlayers() {
		System.out.println("Joueur 2 : votre mark est " + computerMark + "\n");
		do {
			display(1);
			playerTurn();
			game.changePlayer();
		} while (game.checkForWin() && !game.isBoardFull());
		conclusion(true);
	}

	/**
	 * @param maxValue valeur maximal de la plage.
	 * @param label mot a afficher au cas ou l'utilisateur entre une mauvaise valeur.
	 * @return retourne une la valeur que l'utilisateur entre si ca correspond a la
	 * plage donnee au cas contraire redemande la vaeur a l'utilisateur.
	 */
	static int enterGoodValue(int maxValue, String label) {
		AtomicInteger value = new AtomicInteger(maxValue + 1); // initialise value at maxValue + 1.
		do {
			try {
				value.set(scan.nextInt());
			} catch (InputMismatchException e) {
				scan.next(); // destroy what the user enter if it is different to integer and let the value
							// at his initial value.
			}
			if (value.get() < 1 || value.get() > maxValue)
				System.out.println("Choisissez votre " + label + " entre " + 1 + " et " + maxValue);
		} while (value.get() < 1 || value.get() > maxValue);
		return value.get();
	}

	/**
	 * affiche la table de jeu.
	 * @param whichPlayer indique le joueur qui joue.
	 */
	private void display(int whichPlayer) {
		System.out.println("La table de jeu actuelle:");
		game.printBoard();
		if (whichPlayer == 1)
			System.out.println("Le joueur " + game.getCurrentPlayerMark());
		else
			System.out.println("L'ordinateur (" + computerMark + ") va jouer");
	}

	/**
	 * demande au joueur suivant de jouer.
	 */
	private void playerTurn() {
		int row, col;
		do {
			System.out.println("Saisisseez la ligne de votre case");
			row = enterGoodValue(3, "ligne") - 1;
			System.out.println("Saisisseez la colonne de votre case pour placer la marque");
			col = enterGoodValue(3, "colonne") - 1;
		} while (!game.placeMark(row, col));
	}

	/**
	 * message a afficher en fin de jeu.
	 * @param choice indique si le jeu a ete jouer a deux ou contre l'IA.
	 */
	private void conclusion(boolean choice) {
		if (game.isBoardFull() && game.checkForWin()) {
			game.printBoard();
			System.out.println("La partie est serrée. Egalité!!!");
		} else {
			System.out.println("La table de jeu actuelle:");
			game.printBoard();
			if (choice)
				game.changePlayer();
			System.out.println(Character.toUpperCase(game.getCurrentPlayerMark()) + " gagne la partie!");
		}
	}

	/**
	 * @return retourne une table qui contient aleatoirement une ligne et une colonne pour permettre a l'IA niveau moyen 
	 * de jouer aleatoirement.
	 */
	private int[] averageLevel() {
		int[] tab = new int[2];
		Random random = new Random();
		tab[0] = random.nextInt(3);
		tab[1] = random.nextInt(3);
		return tab;
	}
}
