#include <stdio.h>

struct Process {
    int processID;
    int AT;  // Arrival Time
    int BT;  // Burst Time
    int CT;  // Completion Time
    int WT;  // Waiting Time
    int ToT; // Turnaround Time
};

// swap function to be use on sortByArrivalTime function
void swap(struct Process *processA, struct Process *processB) {
    struct Process temp = *processA;
    *processA = *processB;
    *processB = temp;
}

// Sorting Function: Sorts the processes based on Arrival Time using Bubble Sort
void sortByArrivalTime(struct Process P[], int numberofProcess) {

    // Outer loop controls how many times we pass through the list
    for (int i = 0; i < numberofProcess - 1; i++) {

        // Inner loop performs pairwise comparisons and swaps if needed
        for (int j = 0; j < numberofProcess - i - 1; j++) {

            // If the arrival time of the current process is greater than the next,
            // swap them to ensure earlier arrivals come first
            if (P[j].AT > P[j + 1].AT) {
                swap(&P[j], &P[j + 1]);  // Swap using helper function
            }
        }
    }
}


// Function to calculate Completion Time (CT), Turnaround Time (ToT), and Waiting Time (WT)
void calculateTimes(struct Process P[], int numberofProcess) {
    int currentTime = 0;  // Keeps track of the current time as the CPU processes jobs

    // Loop through each process in the order they are scheduled (usually sorted by Arrival Time)
    for (int i = 0; i < numberofProcess; i++) {

        // If the next process has not arrived yet, fast-forward time to its Arrival Time
        // This simulates idle CPU time
        if (currentTime < P[i].AT) {
            currentTime = P[i].AT;
        }

        // Completion Time (CT) = current time + the burst time of the process
        // When the process will finish execution
        P[i].CT = currentTime + P[i].BT;

        // Turnaround Time (ToT) = Completion Time - Arrival Time
        // Total time the process spent in the system (from arrival to completion)
        P[i].ToT = P[i].CT - P[i].AT;

        // Waiting Time (WT) = Turnaround Time - Burst Time
        // How long the process was waiting in the ready queue before being executed
        P[i].WT = P[i].ToT - P[i].BT;

        // Move current time forward to the end of this process
        currentTime = P[i].CT;
    }
}


int main() {
    int numberofProcess;
    printf("Please enter number of processes: ");
    scanf("%d", &numberofProcess);

    // Initialize array of Process
    struct Process P[numberofProcess];

    // Get AT and BT for each Process
    for (int i = 0; i < numberofProcess; ++i) {
        P[i].processID = i + 1;

        printf("Arrival Time for Process %d: ", i + 1);
        scanf("%d", &P[i].AT);

        printf("Burst Time for Process %d: ", i + 1);
        scanf("%d", &P[i].BT);
    }
    // call sortByArrivalTime function
    sortByArrivalTime(P, numberofProcess);

    //call calculateTimes function
    calculateTimes(P, numberofProcess);

    // Output Results
    printf("\nPID\tAT\tBT\tCT\tTAT\tWT\n");
    for (int i = 0; i < numberofProcess; i++) {
        printf("%d\t%d\t%d\t%d\t%d\t%d\n",
               P[i].processID,
               P[i].AT,
               P[i].BT,
               P[i].CT,
               P[i].ToT,
               P[i].WT);
    }

    return 0;
}
