import common.BaseThread; 

/**
 * Class Philosopher.
 * Outlines main subrutines of our virtual philosopher.  
 * 
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca  
 */
public class Philosopher extends BaseThread
{
	/**
	 * Max time an action can take (in milliseconds)   
	 */
	public static final long TIME_TO_WASTE = 1000; 

	/**
	 * The act of eating.
	 * - Print the fact that a given phil (their TID) has started eating. 
	 * - Then sleep() for a random interval.
	 * - The print that they are done eating.
	 */
	public void eat()
	{
		try
		{
			System.out.println("Philosopher " + getTID() + " is eating");
			sleep((long)(Math.random() * TIME_TO_WASTE));
			System.out.println("Philosopher " + getTID() + " is done eating");
		}
		catch(InterruptedException e)
		{
			System.err.println("Philosopher.eat():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}
	}

	/**
	 *  The act of thinking.
	 * - Print the fact that a given phil (their TID) has started thinking.
	 * - Then sleep() for a random interval.
	 * - The print that they are done thinking.
	 */
	public void think() throws InterruptedException {
		System.out.println("Philosopher " + getTID() + " is thinking.");
		sleep((long)(Math.random() * TIME_TO_WASTE));
		System.out.println("Philosopher " + getTID() + " is done thinking.");
	}

	/**
	 * The act of talking.
	 * - Print the fact that a given phil (their TID) has started talking.
	 * - Say something brilliant at random
	 * - The print that they are done talking.
	 */
	public void talk()
	{
		// ...
		System.out.println("Philosopher " + getTID() + " is start talking.");
		saySomething();
		System.out.println("Philosopher " + getTID() + " is done talking.");
		// ...
	}

	/**
	 * No, this is not the act of running, just the overridden Thread.run()
	 */
	public void run()
	{
		for(int i = 0; i < DiningPhilosophers.DINING_STEPS; i++)
		{
			try {
				DiningPhilosophers.soMonitor.pickUp(getTID());
				eat();
				DiningPhilosophers.soMonitor.putDown(getTID());
				think();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			/*
			 * TODO:
			 * A decision is made at random whether this particular
			 * philosopher is about to say something terribly useful.
			 */
			if((int) (Math.random() * (100-0))+0 > 90) // A random decision
			{
				// Some monitor ops down here...
				try {
					DiningPhilosophers.soMonitor.requestTalk();
					talk();
					DiningPhilosophers.soMonitor.endTalk();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
				// ...
			}


		}
	} // run()

	/**
	 * Prints out a phrase from the array of phrases at random.
	 * Feel free to add your own phrases.
	 */
	public void saySomething()
	{
		String[] astrPhrases =
		{
			"Eh, it's not easy to be a philosopher: eat, think, talk, eat...",
			"You know, true is false and false is true if you think of it",
			"2 + 2 = 5 for extremely large values of 2...",
			"If thee cannot speak, thee must be silent",
			"My number is " + getTID() + ""
		};

		System.out.println
		(
			"Philosopher " + getTID() + " says: " +
			astrPhrases[(int)(Math.random() * astrPhrases.length)]
		);
	}
}

// EOF
