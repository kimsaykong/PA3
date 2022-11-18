import java.util.LinkedList;
import java.util.Queue;

/**
 * Class Monitor 
 * To synchronize dining philosophers. 
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca  
 */
public class Monitor   
{
	/*
	 * ------------    
	 * Data members 
	 * ------------
	 */
	private enum State{EAT,THINK,HUNGRY}
	private enum fork{CLEAN,DIRTY};
	private State[] philosopherState;

	private Queue<Integer> waitingState = new LinkedList<>();

	private boolean isTalking = false;
	/**
	 * Constructor
	 */
	public Monitor(int piNumberOfPhilosophers)
	{
		// TODO: set appropriate number of chopsticks based on the # of philosophers
		philosopherState = new State[piNumberOfPhilosophers];
		for (int i = 0; i < piNumberOfPhilosophers; i++) {
			philosopherState[i] = State.THINK;
		}
	}

	/*
	 * -------------------------------
	 * User-defined monitor procedures
	 * -------------------------------
	 */

	/**
	 * Grants request (returns) to eat when both chopsticks/forks are available.
	 * Else forces the philosopher to wait()
	 */
	public synchronized void pickUp(final int piTID) throws InterruptedException {
		// indicating Hungry state
		philosopherState[piTID-1] = State.HUNGRY;
		System.out.println("Philosopher "+ piTID + " is hungry.");

		//adding to waiting queue
		waitingState.add(piTID);

		// checking left and right neighbor are not eating
		// in unable to eat, then waiting to be signaled
		while (checkNeighborEating(piTID - 1) || waitingState.peek() != piTID) this.wait();
		//left and right neighbor are not eating, and it is the turn of the philosopher.
		// so philosopher can eat.
		philosopherState[piTID-1] = State.EAT;
	}

	/**
	 * When a given philosopher's done eating, they put the chopstiks/forks down
	 * and let others know they are available.
	 */
	public synchronized void putDown(final int piTID)
	{
		// indicating THINKING state
		waitingState.poll();
		philosopherState[piTID-1] =  State.THINK;
		this.notifyAll();
	}

	/**
	 * Only one philosopher at a time is allowed to philosophy
	 * (while she is not eating).
	 */
	public synchronized void requestTalk() throws InterruptedException {
		while (isTalking) this.wait();
		isTalking = true;
	}

	/**
	 * When one philosopher is done talking stuff, others
	 * can feel free to start talking.
	 */
	public synchronized void endTalk()
	{
		isTalking = false;
		this.notifyAll();
	}
	private boolean checkNeighborEating(int tid){
		int numberPhilosophers = philosopherState.length;
		if (philosopherState[(tid-1+1)%numberPhilosophers] == State.EAT) return true;
		if (philosopherState[(tid-1+numberPhilosophers-1)%numberPhilosophers] == State.EAT) return true;
		return false;
	}

}

// EOF
