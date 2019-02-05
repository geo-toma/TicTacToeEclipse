package game;

/**
 * @author Georges
 */
public class Main {
    public static void main(String[] args) {

        System.out.println("\t\t+---------------------------------+");
        System.out.println("\t\t| Bienvenue au jeu de Tic-Tac-Toe |");
        System.out.println("\t\t+---------------------------------+");

        System.out.println("Choisisser votre mode de jeu");
        System.out.println(" 1 - Seul contre ordinateur");
        System.out.println(" 2 - Deux joueurs");
        int response = Play.enterGoodValue(2,"mode");
        int level;

        Play play = new Play();
        if (response == 1) {
            System.out.println("Niveau");
            System.out.println(" 1 - Moyen");
            System.out.println(" 2 - Impossible");
            level = Play.enterGoodValue(2,"niveau");
            play.playSolo(level);
        }else
            play.twoPlayers();
    }
}
