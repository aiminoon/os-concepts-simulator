/**
 * SJF Scheduling Algorithm Implementation
 * This program simulates the Shortest Job First (SJF) CPU scheduling algorithm, which is a non-preemptive
 * scheduling algorithm that selects the process with the smallest burst time to execute next.
 * The program calculates various metrics such as waiting time and turnaround time for each process.
 */
import java.util.*;

/**
 * Class representing a process in the operating system.
 * Contains all necessary information about a process including its ID, arrival time,
 * burst time, and calculated metrics like turnaround time and waiting time.
 */
class Process {
    int processId;     // Unique identifier for the process
    int arrivalTime;   // Time at which the process arrives in the ready queue
    int burstTime;     // Total execution time required by the process
    int turnaroundTime; // Time taken from arrival to completion of the process
    int waitingTime;    // Time spent waiting in the ready queue

    /**
     * Constructor to initialize a process with basic parameters.
     * 
     * @param processId   The unique identifier for the process
     * @param arrivalTime The time at which the process arrives
     * @param burstTime   The execution time required by the process
     */
    public Process(int processId, int arrivalTime, int burstTime) {
        this.processId = processId;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.turnaroundTime = 0;  // Initially set to 0, will be calculated later
        this.waitingTime = 0;     // Initially set to 0, will be calculated later
    }
}

/**
 * Main class that implements the Shortest Job First (SJF) scheduling algorithm.
 * SJF selects the process with the smallest burst time to execute next.
 */
public class SJF_Scheduling {

    /**
     * Implements the Shortest Job First (SJF) non-preemptive scheduling algorithm.
     * This implementation considers arrival times of processes and schedules them based on:
     * 1. Process arrival (must be available to run)
     * 2. Burst time (shortest job among available processes gets priority)
     * 
     * The algorithm simulates a CPU scheduler that keeps track of time and completed processes,
     * calculating metrics for each process as they complete execution.
     * 
     * @param processes List of processes to be scheduled
     */
    public static void sjf(List<Process> processes) {
        int n = processes.size();                 // Total number of processes to be scheduled
        int currentTime = 0;                      // Current simulation time - represents CPU clock
        int completed = 0;                        // Counter to track how many processes have completed execution
        boolean[] isCompleted = new boolean[n];   // Array to track which processes have finished execution
        List<Process> executionOrder = new ArrayList<>();  // Stores processes in order of their execution
    
        // Arrays to track important time metrics for each process
        int[] startTimes = new int[n];            // When each process starts execution
        int[] completionTimes = new int[n];       // When each process completes execution
    
        // Main scheduling loop - continues until all processes are executed
        while (completed < n) {
            // Variables to track the process with shortest burst time at current time
            Process shortest = null;              // Reference to the next process to be executed
            int shortestIndex = -1;               // Index of the shortest process in the processes list
    
            // Iterate through all processes to find the one with minimum burst time
            // that has already arrived and hasn't completed execution
            for (int i = 0; i < n; i++) {
                Process p = processes.get(i);
                
                // Process selection criteria:
                // 1. Process must not be completed already
                // 2. Process must have arrived by the current time
                if (!isCompleted[i] && p.arrivalTime <= currentTime) {
                    // Either this is the first valid process found
                    // or it has a shorter burst time than the current shortest
                    if (shortest == null || p.burstTime < shortest.burstTime) {
                        shortest = p;             // Update our candidate for execution
                        shortestIndex = i;        // Store its index for later reference
                    }
                }
            }
    
            // Case 1: No eligible process found (CPU idle time)
            if (shortest == null) {
                currentTime++;                    // Advance time by 1 unit and check again
            } 
            // Case 2: Process found for execution
            else {
                // Record start time for this process
                startTimes[shortestIndex] = currentTime;
                
                // Simulate process execution by advancing the clock by its burst time
                currentTime += shortest.burstTime;
                
                // Record completion time for this process
                completionTimes[shortestIndex] = currentTime;
    
                // Calculate and store metrics:
                // Waiting time = Time process waited in ready queue = Start time - Arrival time
                shortest.waitingTime = startTimes[shortestIndex] - shortest.arrivalTime;
                
                // Turnaround time = Total time in system = Completion time - Arrival time
                shortest.turnaroundTime = completionTimes[shortestIndex] - shortest.arrivalTime;
    
                // Mark this process as completed
                isCompleted[shortestIndex] = true;
                
                // Increment counter of completed processes
                completed++;
                
                // Add to execution order list for final output display
                executionOrder.add(shortest);
            }
        }
    
        // Display results in a formatted table
        System.out.println("\nSJF Detailed Table:");
        
        // Print table header with column names, using formatting to align columns
        System.out.printf("%-10s %-10s %-10s %-12s %-14s %-10s\n", 
            "Process", "Arrival", "Start", "Completion", "Turnaround", "Waiting");
        
        // Print each process's data in the order they were executed
        for (Process p : executionOrder) {
            // Need to adjust index because processId is 1-based but arrays are 0-based
            int idx = p.processId - 1;
            
            // Format and print each row of data
            // P%-9d = Process number with left alignment
            System.out.printf("P%-9d %-10d %-10d %-12d %-14d %-10d\n", 
                p.processId,             // Process ID
                p.arrivalTime,           // When process arrived
                startTimes[idx],         // When process started execution
                completionTimes[idx],    // When process completed
                p.turnaroundTime,        // Total time in system
                p.waitingTime            // Time spent waiting
            );
        }
    }          

    /**
     * Main method that handles user input, process creation, and displays results.
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);  // Create scanner for user input
        List<Process> processes = new ArrayList<>();  // List to store all processes

        // Get the number of processes from user
        System.out.print("Enter number of processes: ");
        int processNo = scanner.nextInt();

        // Input process details for each process
        for (int i = 1; i <= processNo; i++) {
            // Get arrival time for the process
            System.out.print("\nEnter arrival time for Process " + i + ": ");
            int arrivalTime = scanner.nextInt();

            // Get burst time for the process
            System.out.print("Enter burst time for Process " + i + ": ");
            int burstTime = scanner.nextInt();

            // Create a new Process object and add it to the list
            processes.add(new Process(i, arrivalTime, burstTime));
        }

        // Display the entered process details for verification
        System.out.println("\nProcess \t Arrival Time \t Burst Time");
        for (Process p : processes) {
            System.out.println("Process " + p.processId + "\t\t " + p.arrivalTime + "\t\t " + p.burstTime);
        }

        // Apply the SJF scheduling algorithm to the processes
        sjf(processes);

        // Display the results of SJF scheduling (turnaround time and waiting time)
        System.out.println("\nSJF:");
        for (Process p : processes) {
            System.out.println("Process " + p.processId + ": Turnaround Time = " + p.turnaroundTime + ", Waiting Time = " + p.waitingTime);
        }

        // Close the scanner to prevent resource leak
        scanner.close();
    }
}
