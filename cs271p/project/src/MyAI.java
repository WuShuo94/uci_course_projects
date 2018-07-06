//import Agent.Action;
import java.util.*;
// ======================================================================
// FILE:        MyAI.java
//
// AUTHOR:      Abdullah Younis
//
// DESCRIPTION: This file contains your agent class, which you will
//              implement. You are responsible for implementing the
//              'getAction' function and any helper methods you feel you
//              need.
//
// NOTES:       - If you are having trouble understanding how the shell
//                works, look at the other parts of the code, as well as
//                the documentation.
//
//              - You are only allowed to make changes to this portion of
//                the code. Any changes to other portions of the code will
//                be lost when the tournament runs your code.
// ======================================================================

public class MyAI extends Agent
{
	class MemTile
	{
		boolean pit;
		boolean wumpus;
//		boolean breeze = true;
//		boolean stench = true;
		MemTile() {
			this.pit = true;
			this.wumpus = true;
		}
	}

	class Node {
		int f;
		int g;
		int dir;
		Integer curr;
		Node parent;
		Node(int f, int g, int dir, Integer curr, Node parent) {
			this.f = f;
			this.g = g;
			this.dir = dir;
			this.curr = curr;
			this.parent = parent;
		}
	}

	private boolean	hasArrow;	// True if the agent can shoot
	private boolean	getGold;

	private int	agentDir;		// The direction the agent is facing: 0 - right, 1 - down, 2 - left, 3 - up
	private int	agentX;			// The column where the agent is located ( x-coord = col-coord )
	private int	agentY;			// The row where the agent is located ( y-coord = row-coord )

	private int	colDimension;	// The number of columns the game board has
	private int	rowDimension;	// The number of rows the game board has
	private MemTile[][] memBoard;

	private boolean startUp;
	private boolean exit;
	private Set<Integer> path;
	private Set<Integer> children;
	private Stack<Action> seq;
	private int[][] directions = {{1,0},{0,-1},{-1,0},{0,1}};


	public MyAI ( )
	{
		// ======================================================================
		// YOUR CODE BEGINS
		// ======================================================================
		this.agentDir = 0;
		this.agentX = 0;
		this.agentY = 0;
		this.rowDimension = 7;
		this.colDimension = 7;
		this.hasArrow = true;
		this.getGold = false;
		this.memBoard = new MemTile[7][7];
//		Arrays.fill(memBoard, new MemTile());
		for(int i = 0; i < 7; i++) {
			for(int j = 0; j < 7; j++) {
				memBoard[i][j] = new MemTile();
			}
		}
		this.startUp = false;
		this.exit = false;
		this.path = new HashSet<>();
		this.path.add(this.agentX*10 + this.agentY);
		this.children = new HashSet<>();
		this.seq = new Stack<>();
		// ======================================================================
		// YOUR CODE ENDS
		// ======================================================================
	}

	public Action getAction
			(
					boolean stench,
					boolean breeze,
					boolean glitter,
					boolean bump,
					boolean scream
			)
	{
		// ======================================================================
		// YOUR CODE BEGINS
		// ======================================================================

		if(!getGold && glitter) {
			getGold = true;
			exit = true;
			children.remove(agentX*10+agentY);
			path.add(agentX*10+agentY);
			seq.clear();
			return Action.GRAB;
		}

		if(bump) {
			children.remove(agentX*10+agentY);
			if(agentDir == 0)
				colDimension = agentX--;
			if(agentDir == 3)
				rowDimension = agentY--;
			if(agentDir == 1)
				agentY++;
			if(agentDir == 2)
				agentX++;
			seq.clear();
		}

		reasoning(breeze, stench, scream);
		addChildren();
		path.add(agentX*10+agentY);
		if(children.contains(agentX*10+agentY)) {
			children.remove(agentX*10+agentY);
			seq.clear();
		}
		
		if(seq.isEmpty()) {
			int targetXY = searchNext();
//			int targetY = 0;
//			targetY = targetX % 10;
//			targetX = targetX / 10;
//			generateSeq(targetX, targetY, seq);
		}

		Action nextAct = seq.pop();
		changeStatus(nextAct);
		return nextAct;
		// ======================================================================
		// YOUR CODE ENDS
		// ======================================================================
	}

	// ======================================================================
	// YOUR CODE BEGINS
	// ======================================================================

	int generateSeq(int targetX, int targetY, Stack<Action> path2Target) {
		// prepare PriorityQueue
		Comparator<Node> cmp = new Comparator<Node>() {
			public int compare(Node n1, Node n2) {
				return n1.f - n2.f;
			}
		};
		PriorityQueue<Node> pq = new PriorityQueue<>(64,cmp);

		// prepare first node
		Integer startXY = agentX * 10 + agentY;
		Integer targetXY = targetX * 10 + targetY;
		int f = Math.abs(targetX - agentX) + Math.abs(targetY - agentY);
		int g = 0;
		Node currNode = new Node(f, g, agentDir, startXY, null);

		// start A* search. if it is the target, break.
		while(!targetXY.equals(currNode.curr)) {
			// expand
			int x = currNode.curr / 10;
			int y = currNode.curr % 10;
			for(int i = 0; i < 4; i++) {
				int newX = x + directions[i][0];
				int newY = y + directions[i][1];
				Integer newXY = newX * 10 + newY;

				// not safe to go
				if(!path.contains(newXY) && !children.contains(newXY)) continue;

				// agent can go to this position
				g = currNode.g + 1;
				if(i == 0) g += 2 - Math.abs(currNode.dir - 2);
				else if(i == 1) g += Math.abs(currNode.dir - 1);
				else if(i == 2) g += Math.abs(currNode.dir - 2);
				else g += 2 - Math.abs(currNode.dir - 1);

				f = Math.abs(targetX - newX) + Math.abs(targetY - newY) + g;
				Node newNode = new Node(f, g, i, newXY, currNode);
				pq.add(newNode);
			}
			currNode = pq.poll();
		}

		// target found, make the queue of actions
		if(targetXY == 0) path2Target.push(Action.CLIMB);
		int cost = currNode.g;
		Integer lastXY = targetXY;
		if(currNode.parent != null) {
			currNode = currNode.parent;
			while(currNode.parent != null) {
				// get direction of the next as to the current
				int nextDir = whatDirection(lastXY / 10 - currNode.curr / 10, lastXY % 10 - currNode.curr % 10);

				// get direction of the parent as to the current
				int currDir = whatDirection(currNode.curr / 10 - currNode.parent.curr / 10, currNode.curr % 10 - currNode.parent.curr % 10);

				// push actions
				actionToPush(path2Target, nextDir, currDir);

				// prepare
				lastXY = currNode.curr;
				currNode = currNode.parent;
			}
			// do with current agent
			int nextDir = whatDirection(lastXY / 10 - currNode.curr / 10, lastXY % 10 - currNode.curr % 10);
			actionToPush(path2Target, nextDir, agentDir);
		}
		return cost;
	}

	private int whatDirection(int x, int y) {
		// what direction is the target to the current
		int dir = 0;
		if (x == 1 && y == 0) dir = 0;
		else if(x == 0 && y == -1) dir = 1;
		else if(x == -1 && y == 0) dir = 2;
		else dir = 3;
		return dir;
	}

	private void actionToPush(Stack<Action> stack, int dir1, int dir2) {
		// Actions to take to turn from dir2 to dir1
		stack.push(Action.FORWARD);
		if(Math.abs(dir1 - dir2) == 2) {
			stack.push(Action.TURN_LEFT);
			stack.push(Action.TURN_LEFT);
		} else if(dir1 == dir2 + 1 || dir1 == dir2 - 3) {
			stack.push(Action.TURN_RIGHT);
		} else if(dir1 != dir2){
			stack.push(Action.TURN_LEFT);
		}
	}

	private void reasoning(boolean breeze, boolean stench, boolean scream) {
		if(scream) {
			for(int i = 0; i < 7; i++) {
				for(int j = 0; j < 7; j++) {
					memBoard[i][j].wumpus = false;
				}
			}
		}

		if(!breeze) {
			if(valid(agentX-1,agentY))  memBoard[agentX-1][agentY].pit = false;
			if(valid(agentX,agentY-1))  memBoard[agentX][agentY-1].pit = false;
			if(valid(agentX+1,agentY))  memBoard[agentX+1][agentY].pit = false;
			if(valid(agentX,agentY+1))  memBoard[agentX][agentY+1].pit = false;
		}

		if(!stench) {
			if(valid(agentX-1,agentY))  memBoard[agentX-1][agentY].wumpus = false;
			if(valid(agentX,agentY-1))  memBoard[agentX][agentY-1].wumpus = false;
			if(valid(agentX+1,agentY))  memBoard[agentX+1][agentY].wumpus = false;
			if(valid(agentX,agentY+1))  memBoard[agentX][agentY+1].wumpus = false;
		}
		if(stench) {
			for(int i = 0; i < 7; i++) {
				for(int j = 0; j < 7; j++) {
					if(!((i == agentX-1 && j == agentY) || (i == agentX+1 && j == agentY) || (i == agentX && j == agentY-1) || (i == agentX && j == agentY+1)))
						memBoard[i][j].wumpus = false;
				}
			}
		}
		return;
	}


	private void addChildren() {
		if(valid(agentX-1,agentY) && !path.contains((agentX-1)*10+agentY) && !memBoard[agentX-1][agentY].pit && !memBoard[agentX-1][agentY].wumpus)
			children.add((agentX-1)*10+agentY);
		if(valid(agentX,agentY-1) && !path.contains((agentX)*10+agentY-1) && !memBoard[agentX][agentY-1].pit && !memBoard[agentX][agentY-1].wumpus)
			children.add((agentX)*10+agentY-1);
		if(valid(agentX+1,agentY) && !path.contains((agentX+1)*10+agentY) && !memBoard[agentX+1][agentY].pit && !memBoard[agentX+1][agentY].wumpus)
			children.add((agentX+1)*10+agentY);
		if(valid(agentX,agentY+1) && !path.contains((agentX)*10+agentY+1) && !memBoard[agentX][agentY+1].pit && !memBoard[agentX][agentY+1].wumpus)
			children.add((agentX)*10+agentY+1);
	}

	private int searchNext() {	
		if(getGold || children.isEmpty()) {	// Got gold or explored all of the safe tiles, exit!
			children.clear();
			children.add(0);
		}
		Integer[] array = children.toArray(new Integer[0]);
		int targetXY = 0;
		int cost = Integer.MAX_VALUE;
		for(int i = 0; i < array.length; i++) {
			Stack<Action> tmpStack = new Stack<>();
			int tmpTargetXY = array[i];
			int tmpCost = generateSeq(tmpTargetXY/10, tmpTargetXY%10, tmpStack);
			if(cost > tmpCost) {
				targetXY = tmpTargetXY;
				cost = tmpCost;
				seq = tmpStack;
			}
		}
		return targetXY;
	}

	private int isTarget(int i, int j) {
		if(valid(i,j) && !path.contains(i*10+j) && children.contains(i*10+j)) {
//			children.remove(i*10+j);
			return i*10+j;
		}
		return -1;
	}

	private boolean valid(int x, int y) {
		return (x> -1 && y > -1 && x < colDimension && y < rowDimension);
	}

	private void changeStatus(Action nextAction) {
		if(nextAction == Action.FORWARD) {
			switch (agentDir) {
				case 0:
					agentX++;
					break;
				case 1:
					agentY--;
					break;
				case 2:
					agentX--;
					break;
				case 3:
					agentY++;
					break;
			}
		} else if(nextAction == Action.TURN_LEFT) {
			agentDir = (agentDir == 0)? 3 : agentDir-1;
		} else if(nextAction == Action.TURN_RIGHT) {
			agentDir = (agentDir == 3)? 0 : agentDir+1;
		}
	}

	// ======================================================================
	// YOUR CODE ENDS
	// ======================================================================
}