package game;

/**
 * La classe TicTacToe
 */
class TicTacToe {

    /**
     * Constructeur de la classe TicTacToe
     * @param mark contient la mark du premier joueur.
     */
    TicTacToe(char mark) {
        board = new char[3][3]; // board est une matrice 3x3
        currentPlayerMark = mark; // initialise le premier joueur
        initializeBoard(); // Initialise la table de jeu
    }

    private char[][] board;
    
    char[][] getBoard() {
        return board;
    }

    private char currentPlayerMark;

    char getCurrentPlayerMark() {
        return currentPlayerMark;
    }

    void setCurrentPlayerMark(char currentPlayerMark) {
        this.currentPlayerMark = currentPlayerMark;
    }

    //Initialise la table de jeu avec des cellule vide (avec des '-')
    private void initializeBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = '-';
            }
        }
    }

    // display the board state
    void printBoard() {
        for (char[] chars : board) {
            for (char char2 : chars) {
                System.out.print(char2 + "  ");
            }
            System.out.println();
        }
    }

    /**
     * Permet de parcourir toutes les cellules de la table. Si une cellule est vide (contient '-')
     * alors la fonction retourne false. sinon la table est pleine, donc retourne true.
     */
    boolean isBoardFull() {
        for (char[] chars : board) {
            for (char char2 : chars) {
                if (char2 == '-')
                    return false;
            }
        }
        return true;
    }

    /**
     * retourne true si la partie est gagnée, sinon retourne false cette fonction fait appel à
     * d'autre fonction qui permettent de voir si la partie est gagnée ou pas.
     */
    boolean checkForWin() {
        return (!checkRowsForWin() && !checkColumnsForWin() && !checkDiagonalsForWin());
    }

    /**
     * Parcourt les lignes, pour détecter si une ligne est gagnante (retourne un booléen)
     */
    private boolean checkRowsForWin() {
        for (int i = 0; i < 3; i++) {
            if (checkRowCol(board[i][0], board[i][1], board[i][2]))
                return true;
        }
        return false;
    }

    /**
     * Parcourt les colonnes, pour détecter si une colonne est gagnante (retourne un booléen)
     */
    private boolean checkColumnsForWin() {
        for (int i = 0; i < 3; i++) {
            if (checkRowCol(board[0][i], board[1][i], board[2][i]))
                return true;
        }
        return false;
    }

    /**
     * Parcourt les diagonales, pour détecter si une diagonale est gagnante (retourne un booléen)
     */
    private boolean checkDiagonalsForWin() {
        for (int i = 0; i < 2; i++) {
            //la premiere boucle test la diaganala de i=j et le deuxieme boucle test la seconde diagonale
            if (checkRowCol(board[0][2 * i], board[1][1], board[2][2 - 2 * i]))
                return true;
        }
        return false;
    }

    /**
     * Teste si les 3 valeur sont égaux (et non vide), permet d'indiquer si la partie est gagnée.
     */
    private boolean checkRowCol(char c1, char c2, char c3) {
        return ((c1 != '-') && (c1 == c2) && (c2 == c3));
    }

    /**
     * permet d'indiquer quel joueur va jouer 'X' ou 'O'.
     */
    void changePlayer() {
        currentPlayerMark = (currentPlayerMark == Play.mark) ? Play.computerMark : Play.mark;
    }

    /**
     * permet de choisir une mark a l'ordinateur en fonction de ce que l'adversaire a choisi
     * @return retourne un caractere.
     */
    char setComputerMark() {
        return (Play.mark == 'X') ? 'O' : 'X';
    }

    /**
     * Place la marque du joueur actuel dans la cellule spécifiée (Faire attention à ne pas dépasser
     * la taille maximale du tableau 3x3) retourne un booléen
     */
    boolean placeMark(int row, int col) {
        if (row >= 0 && col >= 0 && row < 3 && col < 3) {
            if (board[row][col] == '-') {
                board[row][col] = currentPlayerMark;
                return true;
            }
        }
        return false;
    }
}
