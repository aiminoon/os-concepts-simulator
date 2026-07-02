package Lab6;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.Scanner;

public class bankersAlgorithm {
    
    public static int[][] calculateNeed(int[][] maxDemand, int[][] allocated, int P, int R) {
        int[][] need = new int[P][R];
        for (int i = 0; i < P; i++) {
            for (int j = 0; j < R; j++) {
                need[i][j] = maxDemand[i][j] - allocated[i][j];
            }
        }
        return need;
    }

    public static int[] calculateAvailable(int[][] allocated, int[] maxCapacity, int P, int R) {
        int[] available = new int[R];
        for (int j = 0; j < R; j++) {
            int sum = 0;
            for (int i = 0; i < P; i++) {
                sum += allocated[i][j];
            }
            available[j] = maxCapacity[j] - sum;
        }
        return available;
    }

    public static void displayState(int[][] allocated, int[][] maxDemand, int[][] need, int P) {
        System.out.println("\nProcess\t  Allocation\t   Max\t\t   Need");
        System.out.println("\t   A B C\t   A B C\t   A B C");
        for (int i = 0; i < P; i++) {
            StringBuilder allocStr = new StringBuilder();
            StringBuilder maxStr = new StringBuilder();
            StringBuilder needStr = new StringBuilder();
            for (int j = 0; j < allocated[i].length; j++) {
                allocStr.append(allocated[i][j]).append(" ");
                maxStr.append(maxDemand[i][j]).append(" ");
                needStr.append(need[i][j]).append(" ");
            }
            System.out.printf("P%d\t   %s\t   %s\t   %s%n", i, allocStr.toString().trim(), maxStr.toString().trim(), needStr.toString().trim());
        }
    }

    public static boolean isSafe(int[][] allocated, int[][] maxDemand, int[] maxCapacity, int P, int R) {
        int[][] need = calculateNeed(maxDemand, allocated, P, R);
        int[] available = calculateAvailable(allocated, maxCapacity, P, R);
        boolean[] completed = new boolean[P];
        List<Integer> safeSequence = new ArrayList<>();

        System.out.println("\nInitial Available Resources: " + Arrays.toString(available));
        displayState(allocated, maxDemand, need, P);

        for (int loop = 0; loop < P; loop++) {
            boolean found = false;
            for (int i = 0; i < P; i++) {
                if (!completed[i]) {
                    boolean canRun = true;
                    for (int j = 0; j < R; j++) {
                        if (need[i][j] > available[j]) {
                            canRun = false;
                            break;
                        }
                    }
                    if (canRun) {
                        for (int j = 0; j < R; j++) {
                            available[j] += allocated[i][j];
                        }
                        completed[i] = true;
                        safeSequence.add(i);
                        System.out.println("\nP" + i + " can safely run. Available after execution: " + Arrays.toString(available));
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                break;
            }
        }

        boolean allDone = true;
        for (boolean b : completed) {
            if (!b) {
                allDone = false;
                break;
            }
        }

        if (allDone) {
            System.out.println("\nSystem is in a SAFE state.");
            System.out.print("Safe sequence: ");
            for (int i = 0; i < safeSequence.size(); i++) {
                System.out.print("P" + safeSequence.get(i));
                if (i != safeSequence.size() - 1) {
                    System.out.print(" -> ");
                }
            }
            System.out.println();
            return true;
        } else {
            System.out.println("\nSystem is in an UNSAFE state. No safe sequence possible.");
            return false;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int P, R;
        System.out.print("Enter number of processes (e.g. 4): ");
        P = sc.nextInt();
        System.out.print("Enter number of resource types (e.g. 3): ");
        R = sc.nextInt();

        int[][] allocated = new int[P][R];
        int[][] maxDemand = new int[P][R];

        System.out.println("\nEnter the Allocation Matrix (space-separated):");
        for (int i = 0; i < P; i++) {
            System.out.print("Allocation for P" + i + ": ");
            for (int j = 0; j < R; j++) {
                allocated[i][j] = sc.nextInt();
            }
        }

        System.out.println("\nEnter the Max Demand Matrix (space-separated):");
        for (int i = 0; i < P; i++) {
            System.out.print("Max demand for P" + i + ": ");
            for (int j = 0; j < R; j++) {
                maxDemand[i][j] = sc.nextInt();
            }
        }

        System.out.print("\nEnter the total resource capacity (space-separated): ");
        int[] maxCapacity = new int[R];
        for (int j = 0; j < R; j++) {
            maxCapacity[j] = sc.nextInt();
        }

        System.out.println("\nNumber of Processes: " + P);
        System.out.println("Number of Resources: " + R);
        isSafe(allocated, maxDemand, maxCapacity, P, R);

        sc.close();
    }
}
