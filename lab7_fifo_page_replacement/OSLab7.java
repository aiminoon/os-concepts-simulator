import java.util.*;

public class OSLab7 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int framesCount = 0;
        int pagesCount = 0;

        System.out.print("Enter the number of frames: ");
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a positive integer.");
            scanner.next(); 
        }
        framesCount = scanner.nextInt();

        System.out.print("Enter the number of pages in the reference string: ");
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a positive integer.");
            scanner.next(); 
        }
        pagesCount = scanner.nextInt();

        scanner.nextLine(); 

        int[] pages = new int[pagesCount];

        while (true) {
            System.out.println("Please enter " + pagesCount + " page numbers (space-separated):");
            String line = scanner.nextLine().trim();

            if (line.isEmpty()) {
                System.out.println("Error: Input cannot be empty.");
                continue;
            }

            String[] tokens = line.split("\\s+");

            if (tokens.length != pagesCount) {
                System.out.println("Error: You entered " + tokens.length + " page numbers. Please enter exactly " + pagesCount + ".");
                continue;
            }

            boolean valid = true;
            for (int i = 0; i < pagesCount; i++) {
                try {
                    pages[i] = Integer.parseInt(tokens[i]);
                } catch (NumberFormatException e) {
                    System.out.println("Error: All inputs must be integers.");
                    valid = false;
                    break;
                }
            }

            if (valid) break; // only break if all values were valid
        }

        fifoPageReplacement(pages, framesCount);
    }

    public static void fifoPageReplacement(int[] pages, int framesCount) {
        Queue<Integer> memory = new LinkedList<>();
        Set<Integer> memorySet = new HashSet<>();
        int pageFaults = 0;
        int pageHits = 0;

        System.out.println("\nSimulating FIFO Page Replacement:");
        for (int page : pages) {
            if (!memorySet.contains(page)) {
                if (memory.size() == framesCount) {
                    int removed = memory.poll();
                    memorySet.remove(removed);
                }
                memory.add(page);
                memorySet.add(page);
                pageFaults++;
                System.out.println("Page " + page + " -> Page Fault | Frames: " + memory);
            } else {
                pageHits++;
                System.out.println("Page " + page + " -> No Fault  | Frames: " + memory);
            }
        }

        System.out.println("\nTotal Page Faults: " + pageFaults);
        System.out.println("Total Page Hits: " + pageHits);
    }
}
