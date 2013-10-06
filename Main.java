import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class Main {
	
	static double G = 0.5;
	static final double LR = 0.5;
	static final double DISC = 0.5;
	static final int NUMBER_OF_LOOPS = 10;
	static final double E = 0.5;
	static int[][] rewardMatrix = {	{-1,0,-1,-1,-1},
									{0,-1,0,-1,-1},
									{-1,0,-1,0,-1},
									{-1,-1,0,-1,100},
									{-1,-1,-1,0,100}};

	
	public static void main(String[] args)
	{
		//initialize the q matrix to all 0's
		int rows = rewardMatrix.length;
		int cols = rewardMatrix[0].length;
		double[][]qMatrix = new double[rows][cols];
		
		
		//Execute the algorithm n times
		for(int i = 0;i<NUMBER_OF_LOOPS;i++){
			
			//run the Q-Learning or SARSA algorithm
			runQ(qMatrix);
			//runSARSA(qMatrix);
			
		}
		
		//display results
		System.out.println("\n\nResults:");
		printMatrix(qMatrix);
		
		
		
		
	}
	
	public static void runSARSA(double[][] qMatrix)
	{
		
		//select a random state
		Random r = new Random();
		int numberOfStates = qMatrix[0].length;
		System.out.println("Number of states available: " + numberOfStates);
		int state = r.nextInt(numberOfStates);
		System.out.println("state selected: " + state);
		
		//choose an action from state using a policy derived from Q
		int action = selectAction(state,qMatrix);
		System.out.println("Action selected: "+ action);
		
		//repeat for each state of episode
		boolean foundGoal = (state == 4);
		while(!foundGoal){
			//take a screenshot of the q-matrix before updating it
			double[][] screenshot = new double[qMatrix.length][qMatrix[0].length];
			copyArray(qMatrix,screenshot);
			//execute action and observe reward and state'
			int reward = rewardMatrix[state][action];
			int statep = action;
			System.out.println("s': " + statep);
			//chose action' from state' using policy derived from Q
			int actionp = selectAction(statep,qMatrix);
			System.out.println("a' : "+ actionp);
			qMatrix[state][action] = qMatrix[state][action] + LR*(reward + DISC*qMatrix[statep][actionp] - qMatrix[state][action]);
			state = statep;
			action = actionp;
			foundGoal = (state == 4);
			
			
		}
		
		
	}
	
	public static void copyArray(double[][] original, double[][]copy)
	{
		
		for(int i = 0;i<original.length;i++){
			for(int j = 0;j<original[i].length;j++){
				copy[i][j] = original[i][j];
			}
		}
		
		
	}
	
	public static int selectAction(int state,double[][]qMatrix)
	{
		//going to use greedy policy
	
		Random r = new Random();
		
		//see if you will use the best policy or a random one
		boolean useBestPolicy = (r.nextDouble() < E);
		
		
		if(useBestPolicy){
			
			double max = -1;
			int bestAction = -1;
			ArrayList<Integer> availableActions = getActions(state);
			for(int i = 0;i<availableActions.size();i++){
				int action = availableActions.get(i);
				if (qMatrix[state][action]>max){
					max = qMatrix[state][action];
					bestAction = action;
				}
			}
			
			return bestAction;
			
		}
		else{
			//choose a random state
			ArrayList<Integer> availableActions = getActions(state);
			int randomOption = availableActions.get(r.nextInt(availableActions.size()));
			return randomOption;
		}
		
		
	
	}
	
	
	public static void runQ(double[][] qMatrix)
	{
		
		//select a random state
		Random r = new Random();
		int numberOfStates = qMatrix[0].length;
		System.out.println("Number of states available: " + numberOfStates);
		int state = r.nextInt(numberOfStates);
		System.out.println("state selected: " + state);
		
		//go with it until you hit the destination
		boolean foundGoal = (state == 4);
		while(!foundGoal){
			
			//take a screenshot of the q-matrix before updating it
			double[][] screenshot = new double[qMatrix.length][qMatrix[0].length];
			copyArray(qMatrix,screenshot);
			
			
			System.out.println("\nCurrent state: " + state);
			//pick one of the actions available
			ArrayList<Integer> availableActions  = getActions(state);
			System.out.println("Available actions:" + availableActions);
				//chose an action based on a greed policy
			int action = selectAction(state,qMatrix);
			System.out.println("selected action: " + action);
				//check if you are at the goal
			foundGoal = (action == 4);
			if(foundGoal)
				System.out.println("We are at goal!!");
				
			//update the Q matrix
				//calculate the optimal future for the action taken
			double optimalFuture = getOptimalFuture(action,qMatrix);
			System.out.println("The optimal future for state" + action + " is: " + optimalFuture);
			qMatrix[state][action] = qMatrix[state][action] + LR*(rewardMatrix[state][action] + G*optimalFuture - qMatrix[state][action]);
			System.out.println("qMatrix["+ state + "]["+ action + "] = " + qMatrix[state][action]);
			if(!Arrays.deepEquals(screenshot, qMatrix)){
				System.out.println();
				printMatrix(screenshot);
				System.out.println("changed to:");
				printMatrix(qMatrix);
			}
			
			//change the current state to the chosen one
			state = action;
			
		}
		
		
	}
	

	
	
	public static void printMatrix(double[][] qMatrix)
	{
		for(int i = 0;i<qMatrix.length;i++){
			for(int j = 0;j<qMatrix[i].length;j++){
				System.out.print(qMatrix[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	public static ArrayList<Integer> getActions(int state)
	{
		ArrayList<Integer> result = new ArrayList<Integer>();
		for(int i = 0;i<rewardMatrix[state].length;i++){
			if(rewardMatrix[state][i]!=-1)
				result.add(i);
		}
		
		return result;
	}
	
	public static double getOptimalFuture(int state, double[][]qMatrix)
	{
		
		double max = 0;
		for(int i = 0;i<qMatrix[state].length;i++){
			if(qMatrix[state][i]>max)
				max = qMatrix[state][i];
		}
		
		return max;
	}

}
