package games.connectfour;

import java.util.ArrayList;
import java.util.List;

import searchframework.adversarysearch.StateProblemAdversary;

public class ConnectFourProblem implements StateProblemAdversary<ConnectFourState> {
    private ConnectFourState initial;

    public ConnectFourProblem(boolean player){
        initial = new ConnectFourState(player);
    }

    /**
     * Returns the initial state corresponding to the problem.
     * Concrete implementations of AdversarySearchProblem must
     * implement this routine, to indicate the starting point for
     * the (adversary) search.
     * 
     * @return the initial state for the problem.
     *         @pre. true.
     *         @post. the initial state for the problem is returned.
     */
    @Override
     public ConnectFourState initialState(){
        return initial;
    }

    /**
     * Returns the list of successor states for a given state, in the
     * context of the current problem. Concrete implementations of
     * StateProblemAdversary must implement this routine, to indicate
     * the 'advance' rules (or game rules) for the search.
     * 
     * @param s is the state for which its successors are being
     *          computed.
     * @return the list of successor states of state.
     *         @pre. state!=null.
     *         @post. the list of successor states of state is returned.
     */

     /*
     * Algorithm:
    * 1. Filter valid columns (non-full columns from the state matrix)
    * 2. For each valid column, iterate from bottom to top
    * 3. When finding the first empty position:
        *    - Create a temporary matrix copy
        *    - Place the appropriate piece ('x' for max, 'o' for min)
        *    - Generate new state and add to successors list
    */
    @Override
    public List<ConnectFourState> getSuccessors(ConnectFourState s){
        List<Integer> columnValidas = new ArrayList<>(); 
        List<ConnectFourState> succs = new ArrayList<>();
        char[][] mtz = s.getCurrentBoard();

        for(int i = 0; i < 7; i++){
            if(mtz[0][i] == '-')
                columnValidas.add(i);
        }
        
        for(int j : columnValidas){
            for(int i = 5; i >= 0; i--){
                if(mtz[i][j] == '-'){
                    char[][] temporalMtz = new char[6][7];
                    
                    //Matrix Manually Copy 
                    
                    for(int k = 0; k < 7; k++ ){
                        for(int l = 0; l < 6; l++){
                            temporalMtz[l][k] = mtz[l][k];        
                        }
                    }
                    
                    if(s.isMax()){
                        temporalMtz[i][j] = 'x';
                    }else{
                        temporalMtz[i][j] = 'o';
                    }

                    succs.add(new ConnectFourState(s.isMax(),temporalMtz,s));

                    break;
                }
            }
        }
        return succs;

    }

    /**
     * Indicates the least possible value for a state in the problem.
     * Together with maxValue(), it determines an interval in which
     * values for states must range. This value must be
     * strictly smaller than maxValue().
     * 
     * @return an integer value, representing the least possible value
     *         for the problem.
     *         @pre. true.
     *         @post. an integer value, representing the least possible value
     *         for states, is returned.
     */
    @Override
     public int minValue(){
        return -1000;
    }

    /**
     * Indicates the greatest possible value for a state in the problem.
     * Together with minValue(), it determines an interval in which
     * values for states must range.
     * 
     * @return an integer value, representing the greatest possible value
     *         for the problem.
     *         This value must be strictly greater than minValue().
     *         @pre. true.
     *         @post. an integer value, representing the greatest possible value
     *         for states, is returned.
     */
    @Override
    public int maxValue(){
        return 1000;
    }
}
