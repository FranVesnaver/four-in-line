package games.connectfour;

import searchframework.adversarysearch.StateAdversary;

public class ConnectFourState implements StateAdversary {

    // playersTurn: true if it's the player's turn, false if it's the machine's turn
    private boolean playersTurn; 

    // actualBoard: current state of the board, 'x's are the machine tokens, 'o's are the player's
    private char[][] currentBoard;

    // parent: state from which the game came to this state
    private ConnectFourState parent;

    // column in which the last move was made
    private Integer lastColumn;


    public ConnectFourState(boolean playersTurn, char[][] currentBoard, ConnectFourState parent){
        this.playersTurn = playersTurn;
        this.parent = parent;

        this.currentBoard = new char[currentBoard.length][currentBoard[0].length];
        for (int i = 0; i < currentBoard.length; i++) {
            for (int j = 0; j < currentBoard[0].length; j++) {
                this.currentBoard[i][j] = currentBoard[i][j];
            
                if (parent != null) {
                    char[][] previousBoard = parent.getCurrentBoard();
                    if(currentBoard[i][j] != previousBoard[i][j]) {
                        this.lastColumn = j;
                    }            
                }
            }
        }
    }

    public ConnectFourState (boolean playersTurn){
        this.playersTurn = playersTurn;
        this.lastColumn = null;
        currentBoard = new char[6][7];
        parent = null;
        for (int i = 0; i < currentBoard.length; i++){
            for (int j = 0; j < currentBoard[0].length; j++){
                currentBoard[i][j] = '-';
            }
        }
    }

    public char[][] getCurrentBoard(){
        return currentBoard;
    }

    /**
    * Returns the parent of the current state. This method
    * must be implemented by all concrete classes implementing StateAdversary.
    * @return the parent of the current state or null if this does not have a parent.
    */
    public StateAdversary getParent(){
        return parent;
    }

    public void makeMove (int column) {
        int i = 0;
        while (i < currentBoard.length && currentBoard[i][column] == '-'){
            i++;
        }

        if (playersTurn)
            currentBoard[--i][column] = 'o';
        else
            currentBoard[--i][column] = 'x';
            
        playersTurn = !playersTurn;
    }

    @Override
    public boolean isSuccess() {
        return value() == 1000;
    }

    /** 
    * Indicates whether the current state is a max state or not.
    * If the current state is not a 'max' state, then it is assumed
    * to be a min state. 
    * @return true iff 'this' is a max state.
    * @pre. true.
    * @post. true is returned iff 'this' is a max state.
    */
    public boolean isMax(){
        return !playersTurn;
    }


    /** 
    * Indicates whether this state is an end state, i.e., a 
    * state with no successors. 
    * @return true iff state is an end state.
    * @pre. this!=null.
    * @post. true is returned iff this is an end state.
    */
    public boolean end(){
        if (value() == 1000 || value() == -1000) return true;

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (currentBoard[i][j] == '-') {
                    return false;
                }
            }
        }
        return true;
    }

    /** 
    * Computes the value of this  state. If the state is a leaf
    * (end state), then this value is an exact value, and indicates
    * the outcome of the game. If the state is not an end state, then
    * this value is an approximate value. Its estimation plays a
    * crucial role in the performance of search. This value must 
    * be greater than minValue(), and smaller than maxValue().
    * @return an integer value, representing the value of the state.
    * @pre. this!=null.
    * @post. an integer value, representing the value of the state.   
    */
    public int value(){
        int score = 0;

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7 ; j++) {
                int scoreForThisPoint = 0;
                // if no one won, compute the 'score' of the state given the current board
                if (currentBoard[i][j] == 'x') {
                    int count;
                    if (j <= 3) {
                        // to the right
                        count = 1;
                        for (int k = 1; k < 4; k++) {
                            if (currentBoard[i][j+k] == 'x') count++;
                            if (currentBoard[i][j+k] == 'o') {
                                count = 0;
                                break;
                            }
                        }
                        switch (count){
                            case 1: scoreForThisPoint += 1; break;
                            case 2: scoreForThisPoint += 10; break;
                            case 3: scoreForThisPoint += 25; break;
                            case 4: score = 1000; break;
                        }
                    }


                    if (i <= 2) {
                        // down
                        count = 1;
                        for (int k = 1; k < 4; k++) {
                            if (currentBoard[i+k][j] == 'x') count++;
                            if (currentBoard[i+k][j] == 'o') {
                                count = 0;
                                break;
                            }
                        }
                        switch (count){
                            case 1: scoreForThisPoint += 1; break;
                            case 2: scoreForThisPoint += 10; break;
                            case 3: scoreForThisPoint += 25; break;
                            case 4: score = 1000; break;
                        }
                    }

                    if (i <= 2 && j <= 3) {
                        // down to the right
                        count = 1;
                        for (int k = 1; k < 4; k++) {
                            if (currentBoard[i+k][j+k] == 'x') count++;
                            if (currentBoard[i+k][j+k] == 'o') {
                                count = 0;
                                break;
                            }
                        }
                        switch (count){
                            case 1: scoreForThisPoint += 1; break;
                            case 2: scoreForThisPoint += 10; break;
                            case 3: scoreForThisPoint += 25; break;
                            case 4: score = 1000; break;
                        }
                    }

                    if (i <= 2 && j >= 3) {
                        // down to the left
                        count = 1;
                        for (int k = 1; k < 4; k++) {
                            if (currentBoard[i+k][j-k] == 'x') count++;
                            if (currentBoard[i+k][j-k] == 'o') {
                                count = 0;
                                break;
                            }
                        }
                        switch (count){
                            case 1: scoreForThisPoint += 1; break;
                            case 2: scoreForThisPoint += 10; break;
                            case 3: scoreForThisPoint += 25; break;
                            case 4: score = 1000; break;
                        }
                    }
                    score += scoreForThisPoint;
                    if (score >= 1000) return 1000;


                } else if (currentBoard[i][j] == 'o') {
                    int count;
                    if (j <= 3) {
                        // to the right
                        count = 1;
                        for (int k = 1; k < 4; k++) {
                            if (currentBoard[i][j+k] == 'o') count++;
                            if (currentBoard[i][j+k] == 'x') {
                                count = 0;
                                break;
                            }
                        }
                        switch (count){
                            case 1: scoreForThisPoint += 1; break;
                            case 2: scoreForThisPoint += 10; break;
                            case 3: scoreForThisPoint += 25; break;
                            case 4: score = -1000; break;
                        }
                    }


                    if (i <= 2) {
                        // down
                        count = 1;
                        for (int k = 1; k < 4; k++) {
                            if (currentBoard[i+k][j] == 'o') count++;
                            if (currentBoard[i+k][j] == 'x') {
                                count = 0;
                                break;
                            }
                        }
                        switch (count){
                            case 1: scoreForThisPoint += 1; break;
                            case 2: scoreForThisPoint += 10; break;
                            case 3: scoreForThisPoint += 25; break;
                            case 4: score = -1000; break;
                        }
                    }

                    if (i <= 2 && j <= 3) {
                        // down to the right
                        count = 1;
                        for (int k = 1; k < 4; k++) {
                            if (currentBoard[i+k][j+k] == 'o') count++;
                            if (currentBoard[i+k][j+k] == 'x') {
                                count = 0;
                                break;
                            }
                        }
                        switch (count){
                            case 1: scoreForThisPoint += 1; break;
                            case 2: scoreForThisPoint += 10; break;
                            case 3: scoreForThisPoint += 25; break;
                            case 4: score = -1000; break;
                        }
                    }

                    if (i <= 2 && j >= 3) {
                        // down to the left
                        count = 1;
                        for (int k = 1; k < 4; k++) {
                            if (currentBoard[i+k][j-k] == 'o') count++;
                            if (currentBoard[i+k][j-k] == 'x') {
                                count = 0;
                                break;
                            }
                        }
                        switch (count){
                            case 1: scoreForThisPoint += 1; break;
                            case 2: scoreForThisPoint += 10; break;
                            case 3: scoreForThisPoint += 25; break;
                            case 4: score = -1000; break;
                        }
                    }
                    score -= scoreForThisPoint;
                    if (score <= -1000) return -1000;

                }
            }
        }
        return score;
    }



    /** 
    * Checks whether 'this' is equal to another state. 
    * @param other is the state to compare 'this' to.
    * @return true iff 'this' is equal, as a state, to 'other'.
    * @pre. other!=null.
    * @post. true is returned iff 'this' is equal, as a state,
         to 'other'.
    */
    @Override
    public boolean equals(Object other){
        // referential equality
        if (this == other) return true;

        if (!(other instanceof ConnectFourState)) return false;

        ConnectFourState otherState = (ConnectFourState) other;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (currentBoard[i][j] != otherState.getCurrentBoard()[i][j])
                    return false;
            }
        }

        return true;
    }

    /** 
    * Returns a representation as a string of the current state. 
    * @return a string representing the current state.
    * @pre. true.
    * @post. A text representation of the current state is returned.
    */
    @Override 
    public String toString(){
        StringBuilder board = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            board.append("|");
            for (int j = 0; j < 7; j++) {
                board.append(" ").append(currentBoard[i][j]);
            }
            board.append(" |");
            board.append("\n");
        }

        return board.toString();
    }

     public void setLastColumn(int column) {
        this.lastColumn = column;
    }

    /** 
    * Returns an object representing the rule applied, leading to the
    * current state. 
    * @return an object representing the rule applied, leading to the
         current state. If the state is the initial state, then null is 
         returned.
    * @pre. true.
    * @post. An object representing the rule applied, leading to the
         current state, is returned. If the state is the initial state, 
         then null is returned.
         TODO Replace Object by a more specific class or interface.
    */
    public Object ruleApplied(){
        return lastColumn;
    }
}
