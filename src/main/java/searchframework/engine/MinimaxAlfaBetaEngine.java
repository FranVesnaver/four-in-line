package searchframework.engine;

import searchframework.adversarysearch.EngineAdversary;
import searchframework.adversarysearch.StateAdversary;
import searchframework.adversarysearch.StateProblemAdversary;

public class MinimaxAlfaBetaEngine<S extends StateAdversary, P extends StateProblemAdversary<S>> implements EngineAdversary<P, S> {
    private P p;
    private int maxDepth;

    @Override
    public int getMaxDepth() {
        return maxDepth;
    }

    @Override
    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    @Override
    public P getProblem() {
        return p;
    }

    @Override
    public void setProblem(P p) {
        this.p = p;
    }

    
    @Override
    public int computeValue(S state) {
        return minMaxAB(state, maxDepth, state.isMax(),Integer.MIN_VALUE,Integer.MAX_VALUE);
    }

    @Override
    public S computeSuccessor(S state) {
        S bestSuccessor = null;
        int bestValue = Integer.MIN_VALUE;
        
        for (S successor : p.getSuccessors(state)) {
            int value = minMaxAB(successor, maxDepth - 1, !state.isMax(),Integer.MIN_VALUE,Integer.MAX_VALUE);
            if (bestSuccessor == null || value > bestValue) {
                bestValue = value;
                bestSuccessor = successor;
            }
        }
        return bestSuccessor;

    }

    @Override
    public void report() {
        System.out.println("Algoritmo: Minimax");
        System.out.println("Profundidad máxima: " + maxDepth);
    }
    

    private int minMaxAB(S state, int depth, boolean isMax, int alpha, int beta){
        
        if(depth == 1 || state.end()){
            return state.value();
        }

        if(isMax){
            for(S s : p.getSuccessors(state)){
                if(alpha < beta){
                alpha = Math.max(alpha,minMaxAB(s, depth -1, !isMax,alpha,beta));
                }else break;
            }
            return alpha;
        }else{
            for(S s : p.getSuccessors(state)){
                if(alpha < beta){
                beta = Math.min(beta,minMaxAB(s, depth - 1, !isMax,alpha,beta));
                }else break;
            }
            return beta;
        }

    }
}



