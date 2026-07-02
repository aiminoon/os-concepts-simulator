import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Scanner;

class DPArbitrator {        //arbitrator controls access to chopsticks

    private final int numPhilosophers;  // number of philosophers
    private final boolean[] chopsticks; // true = taken, false = free
    private final Lock lock;            // lock ensures only one thread can access chopsticks at a time
    private final Condition condition;  // condition ensures other philosphers wait if chopsticks are not available

    private volatile boolean running = true;    // check if main thread signal philosophers thread to keep running(volatile ensures visibility across threads)

    public DPArbitrator(int numPhilosophers) {      // constructor
        this.numPhilosophers = numPhilosophers;
        this.chopsticks = new boolean[numPhilosophers];
        this.lock = new ReentrantLock();
        this.condition = lock.newCondition();
    }

    public void requestChopsticks(int philosopherId) throws InterruptedException { // method requests chopsticks from arbitrator
        int left = philosopherId;                              // left chopstick (lower number upto philosopher 4, then higher number for philosopher 5)
        int right = (philosopherId + 1) % numPhilosophers;     // right chopstick(higher number upto philosopher 4, then lower number for philosopher 5)

        lock.lock();                                    // acquire lock to try to take chopsticks
        try {
            while (chopsticks[left] || chopsticks[right]) {     // Wait until both chopsticks are free, releases lock during wait and reacquires before rechecking
                condition.await();
            }
            chopsticks[left] = true;                            // mark left chopstick as taken
            chopsticks[right] = true;                           // mark right chopstick as taken
        } finally {
            lock.unlock();                                      // release lock for other thread to try acquiring 
        }
    }

    public void releaseChopsticks(int philosopherId) {                     // method releases chopsticks after use
        int left = philosopherId;
        int right = (philosopherId + 1) % numPhilosophers;

        lock.lock();                                            //acquire lock to release chopsticks
        try {
            chopsticks[left] = false;                           // mark left chopstick as free
            chopsticks[right] = false;                          // mark right chopstick as free
            condition.signalAll();                              // notify all waiting threads to recheck chopstick availability
        } finally {
            lock.unlock();                                      // release lock for other thread to try acquiring
        }
    }

    public void philosopherActions(int philosopherId) {                // method simulate philosophers actions 
        int displayId = philosopherId + 1;
        try {
            while (running) {
                System.out.println("Philosopher " + displayId + " is thinking..."); // print philosopher is thinking
                Thread.sleep((long) (1000 + Math.random() * 1000));                 // simulate thinking time

                if (!running) break;                                                // stop running after duration ended

                System.out.println("Philosopher " + displayId + " is hungry and requests chopsticks");  // print the philosopher is requesting chopsticks

                requestChopsticks(philosopherId);                                                  // call requestChopsticks method

                System.out.println("Philosopher " + displayId + " got both chopsticks and is eating...");   // print philospher eating
                Thread.sleep((long) (1000 + Math.random() * 1000));                 // simulate eating time

                System.out.println("Philosopher " + displayId + " finished eating and releases chopsticks"); // print philosopher releases chopstick
                releaseChopsticks(philosopherId);                                                  // call releaseChopsticks method

                System.out.println("Philosopher " + displayId + " returned to thinking");   // print philosopher return to thinking
                System.out.println("----------------------------------------------");       // to make processes easier to see
            }
        } catch (InterruptedException e) {                                          // user interrupt
            Thread.currentThread().interrupt();
        }
    }

    public void startSimulation(int durationSeconds) throws InterruptedException {      // method start the simulation
        Thread[] philosophers = new Thread[numPhilosophers];                            // create thread array to hold philosopher threads

        for (int i = 0; i < numPhilosophers; i++) {
            final int id = i;                                                           // philosoper id
            philosophers[i] = new Thread(() -> philosopherActions(id));                 // lambda to run philosopher actions
            philosophers[i].start();                                                    // start thread
            System.out.println("Philosopher " + (id + 1) + " seated");                  // print philosopher is seated
        }

        Thread.sleep(durationSeconds * 1000);                                           // Main thread sleep for selected duration to let philosophers thread run
        running = false;                                                                // Set running to false to stop philosopher threads in their next loop

        for (Thread t : philosophers) {                                                 // allow the philosopher threads time to finish
            t.join(2000);
        }

        System.out.println("Simulation ended successfully!");                           // print simulation ended
    }
}

public class Main{
    public static void main(String[] args) throws InterruptedException {
        System.out.println("DINING PHILOSOPHERS PROBLEM - ARBITRATOR SOLUTION");
        System.out.println("================================================");
        System.out.println("Number of Philosophers: 5");

        int duration = 15;                                                              // default duration = 15 seconds
        try {
            System.out.print("Enter simulation duration in seconds (default 15): ");    // prompt user for duration
            Scanner sc = new Scanner(System.in);
            String inputStr = sc.nextLine();
            if (!inputStr.isEmpty()) {                                                  // if user input duration, parse it into Integer
                duration = Integer.parseInt(inputStr);
            }
        } catch (Exception e) {
            System.out.println("Invalid input, using default duration of 15 seconds");  // if invalid input, use default duration
        }

        System.out.println("\nStarting simulation for " + duration + " seconds...\n");  // print starting simulation
        System.out.println("Press CTRL+C to interrupt the simulation\n");

        DPArbitrator simulation = new DPArbitrator(5);                                  // instantiate object DPArbitrator with number of philosophers 5
        simulation.startSimulation(duration);                                           // startsimulation for duration given
    }
}
