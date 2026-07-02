# OS Concepts Simulator

Java and C implementations of core operating system algorithms, built for the Operating Systems (WIA2004) course at Universiti Malaya. Each lab simulates a classic OS mechanism — CPU scheduling, memory allocation, file allocation, deadlock avoidance, page replacement, and process synchronization.

## Labs

| # | Topic | Algorithm | Language |
|---|-------|-----------|----------|
| 1 | CPU Scheduling | First Come First Served (FCFS) | C |
| 2 | CPU Scheduling | Shortest Job First (SJF) | Java |
| 3 | File Allocation | Sequential Allocation | Java |
| 4 | Memory Management | Best-Fit Allocation | Java |
| 5 | Memory Management | First-Fit Allocation | Java |
| 6 | Deadlock Avoidance | Banker's Algorithm | Java |
| 7 | Page Replacement | FIFO | Java |
| 8 | Process Synchronization | Dining Philosophers (multithreaded) | Java |

## Highlights

- **Lab 1 & 2** — Calculates turnaround time and waiting time for FCFS and SJF scheduling.
- **Lab 4 & 5** — Simulates contiguous memory allocation strategies and reports which block each process is assigned to.
- **Lab 6** — Implements the Banker's algorithm to compute resource need and evaluate whether a system state is safe.
- **Lab 7** — Simulates FIFO page replacement with page fault/hit tracking.
- **Lab 8** — Solves the Dining Philosophers problem using Java's `ReentrantLock` and `Condition` for real multithreaded, deadlock-free resource sharing (not just a sequential simulation).

## How to run

**Java labs:**
```bash
cd lab2_sjf_scheduling
javac SJF_Scheduling.java
java SJF_Scheduling
```

**C labs (Lab 1):**
```bash
cd lab1_fcfs_scheduling
gcc lab1.c -o lab1
./lab1
```

## Course context

Built as part of the Operating Systems Laboratory (WIA2004), Faculty of Computer Science & Information Technology, Universiti Malaya. The lab syllabus covers system calls, CPU scheduling, memory management, file systems, and deadlock handling using C/Java in a Linux environment.
