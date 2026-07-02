

import java.util.Scanner;
// Imports the Scanner class from java.util so we can read user input

public class BestFitAllocation {
    // Defines a public class named BestFitAllocation; the filename must match this class name

    public static void main(String[] args) {
        // The main method—Java’s entry point. JVM starts execution here.

        Scanner sc = new Scanner(System.in);
        // Creates a Scanner object `sc` to read from standard input (the keyboard)

        // 1. Read configuration
        System.out.print("Enter the number of blocks: ");
        // Prompts user without a newline
        int nBlocks = sc.nextInt();
        // Reads an integer from input and stores it in nBlocks

        System.out.print("Enter the number of processes: ");
        // Prompts user for the number of processes
        int nProcesses = sc.nextInt();
        // Reads that integer into nProcesses

        // 2. Initialize arrays
        int[] blockSize = new int[nBlocks];
        // Creates an array to hold the size of each memory block
        boolean[] blockAllocated = new boolean[nBlocks];
        // Boolean flag array: true if that block has been taken
        int[] processSize = new int[nProcesses];
        // Array to hold each process’s requested size
        int[] allocatedBlock = new int[nProcesses];
        // For each process, stores the index of the block it gets (or -1)
        int[] remainingSize = new int[nProcesses];
        // After allocation, how much space is left unused in the chosen block

        // 3. Input block sizes
        System.out.println("\nEnter the size of the blocks:");
        // Prints a newline and a header
        for (int i = 0; i < nBlocks; i++) {
            // Loop over each block index
            System.out.print("  Block no." + (i + 1) + ": ");
            // Prompt for block i (displayed as 1-based)
            blockSize[i] = sc.nextInt();
            // Read and store the block’s size
        }

        // 4. Input process sizes
        System.out.println("\nEnter the size of the processes:");
        // Header for process-size input
        for (int i = 0; i < nProcesses; i++) {
            // Loop over each process
            System.out.print("  Process no." + (i + 1) + ": ");
            // Prompt for process i
            processSize[i] = sc.nextInt();
            // Read and store the process’s required size
            allocatedBlock[i] = -1;
            // Initialize to –1 meaning “not allocated yet”
        }

        // 5. Best-Fit allocation
        for (int i = 0; i < nProcesses; i++) {
            // For each process…
            int bestIndex = -1;
            // Will track the index of the “best” block found (–1 if none)
            int minDiff = Integer.MAX_VALUE;
            // Will track the smallest leftover (initially very large)

            // find the smallest hole that fits
            for (int j = 0; j < nBlocks; j++) {
                // Scan every block
                if (!blockAllocated[j] && blockSize[j] >= processSize[i]) {
                    // If block j is free AND big enough…
                    int diff = blockSize[j] - processSize[i];
                    // Calculate leftover space after allocating this process
                    if (diff < minDiff) {
                        // If this leftover is smaller than any we’ve seen…
                        minDiff = diff;
                        // Update the smallest leftover
                        bestIndex = j;
                        // Remember this block as the current best fit
                    }
                }
            }

            // allocate if found
            if (bestIndex != -1) {
                // If we found a suitable block…
                allocatedBlock[i] = bestIndex;
                // Record its index for this process
                remainingSize[i] = minDiff;
                // Record how much space remains
                blockAllocated[bestIndex] = true;
                // Mark that block as no longer available
            }
        }

        // 6. Display results
        System.out.println("\nProcess No   Process Size   Block No   Block Size   Remaining Block Size");
        // Table header
        for (int i = 0; i < nProcesses; i++) {
            // For each process, print one row
            if (allocatedBlock[i] != -1) {
                // If it got allocated…
                int b = allocatedBlock[i];
                // b is the block index
                System.out.printf(
                        "%-13d%-15d%-10d%-13d%-20d\n",
                        (i + 1),                  // Process number (1-based)
                        processSize[i],         // Its requested size
                        (b + 1),                  // Block number (1-based)
                        blockSize[b],           // Original size of that block
                        remainingSize[i]        // Wasted (leftover) space
                );
            } else {
                // If no block was large enough…
                System.out.printf(
                        "%-13d%-15d%-10s%-13s%-20s\n",
                        (i + 1),
                        processSize[i],
                        "Not allocated",
                        "-",
                        "-"
                );
            }
        }

        sc.close();
        // Close the Scanner to free up the input stream
    }
}
