package games.connectfour;

import java.util.Scanner;

import searchframework.engine.MinimaxAlfaBetaEngine;
 
public class ConnectFourApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Mensajes por consola
        System.out.println ("\n---WELCOME TO A FOUR IN LINE---\n");
        System.out.println ("  ¿Who should start the game ? \n");
        System.out.println ("-Press 1 if you want the AI to start");
        System.out.println ("-Press 2 if you want start\n");
        System.out.println ("ACLARATION: You play as o. You must enter columns (1,2,3,4,5,6 or 7)\n");
        System.out.println ("OPTION: ");
        
        int opcion = scanner.nextInt();
        while (opcion != 1 && opcion !=2){
            System.out.println("Choose again");
            opcion = scanner.nextInt();
        }
        

        boolean humanoEsMax;
        // comienza jugando la AI
        if (opcion == 1){
            humanoEsMax = false; // Maquina juega como X;
            ConnectFourProblem problema = new ConnectFourProblem(humanoEsMax);
            ConnectFourState state = problema.initialState();
        
            //configurando el engine Minimax
            MinimaxAlfaBetaEngine<ConnectFourState, ConnectFourProblem> engine = new MinimaxAlfaBetaEngine<>();
            engine.setProblem(problema);
            engine.setMaxDepth(6); // Profundidad maxima para el 4 en linea
            
            
            while (!state.end()){
                // Se mostrara el estado inicial
                System.out.println(state);
                if ((!state.isMax() == humanoEsMax)){
                    //turno maquina con el minmax
                    System.out.println("Turn of the AI");
                    ConnectFourState best = (ConnectFourState) engine.computeSuccessor(state);
                    if (best != null){
                        state = best;
                    }else{
                        // no hay sucesores
                        break;
                    }
                }else{
                    int col;
                    while (true){
                        System.out.println ("Your turn. Your move (Column): ");
                        col = scanner.nextInt();
                        // Compruebo el valor que ingreso el usuario
                        if (col <= 7 && col >= 1 && state.getCurrentBoard()[0][col-1] == '-' ){
                            break;
                        }else{
                            System.out.println("Invalid move. Try again");
                        }

                    }
                    state.makeMove(col-1);
                }
            }
            
            finalMessage(state);
        //comienza jugando el humano
        }else{
            humanoEsMax = true; // Humano juega como O;
            ConnectFourProblem problema = new ConnectFourProblem(humanoEsMax);
            ConnectFourState state = problema.initialState();
        
            //configurando el engine Minimax
            MinimaxAlfaBetaEngine<ConnectFourState, ConnectFourProblem> engine = new MinimaxAlfaBetaEngine<>();
            engine.setProblem(problema);
            engine.setMaxDepth(6); // Profundidad maxima para el 4 en linea
            
            while (!state.end()){
                // Se mostrara el estado inicial
                System.out.println(state);
                if (!state.isMax() == humanoEsMax){
                    int col;
                    while (true){
                        System.out.println ("Your turn. Your move (Column): ");
                        col = scanner.nextInt();
                        // Compruebo el valor que ingreso el usuario
                        if (col <= 7 && col >= 1 && state.getCurrentBoard()[0][col-1] == '-' ){
                            break;
                        }else{
                            System.out.println("Invalid move. Try again");
                        }
                    }
                    
                    state.makeMove(col-1);

                }else{
                    //turno maquina con el minmax
                    System.out.println("Turn of the AI");
                    ConnectFourState best = (ConnectFourState) engine.computeSuccessor(state);
                    if (best != null){
                        state = best;
                    }else{
                        //no hay sucesores
                        break;
                    }
                }    
            }
            finalMessage(state);
        }
        scanner.close();
    }

    public static void finalMessage(ConnectFourState state){
        System.out.println(state);
        if (state.value() == -1000){
            System.out.println("YOU WIN, CONGRATULATIONS!!!");
        }else if (state.value() == 1000){
            System.out.println("YOU LOST :(. TRY AGAIN, DONT GIVE UP");
        }else{
            System.out.println("DRAW!!");
        }
    }
}
