import java.util.*;

class FileEntry {
    String name;      // File name
    int startBlock;   // Starting index in disk
    int length;       // Number of blocks allocated

    public FileEntry(String name, int startBlock, int length) {
        this.name = name;
        this.startBlock = startBlock;
        this.length = length;
    }
}

public class SequentialFileAllocationSimulator {
    private String[] disk; // Now holds file name or null (not just true/false)
    private List<FileEntry> files; // File allocation table

    public SequentialFileAllocationSimulator(int diskSize) {
        disk = new String[diskSize]; // null means free, otherwise holds file name
        files = new ArrayList<>();
    }

    // Method to create a new file and allocate contiguous blocks
    public boolean createFile(String name, int size) {
        // Check if file with same name already exists
        for (FileEntry f : files) {
            if (f.name.equals(name)) {
                System.out.println("File with this name already exists.");
                return false;
            }
        }

        int start = findContiguousFreeBlocks(size);
        if (start == -1) {
            System.out.println("Not enough contiguous free blocks to allocate the file.");
            return false;
        }
        // Mark blocks as occupied by the file's name
        for (int i = start; i < start + size; i++) {
            disk[i] = name;
        }
        files.add(new FileEntry(name, start, size));
        System.out.println("File '" + name + "' created, allocated blocks " + start + " to " + (start + size - 1));
        return true;
    }

    // Helper method to find contiguous free space
    private int findContiguousFreeBlocks(int size) {
        int count = 0, start = -1;

        for (int i = 0; i < disk.length; i++) {
            if (disk[i] == null) { // If block is free
                if (count == 0) start = i;
                count++;
                if (count == size) return start;
            } else {
                count = 0; // Reset if a used block is found
            }
        }
        return -1; // Not found
    }

    // Delete a file and free its blocks
    public boolean deleteFile(String name) {
        Iterator<FileEntry> it = files.iterator();
        while (it.hasNext()) {
            FileEntry f = it.next();
            if (f.name.equals(name)) {
                for (int i = f.startBlock; i < f.startBlock + f.length; i++) {
                    disk[i] = null;
                }
                it.remove();
                System.out.println("File '" + name + "' deleted and its blocks freed.");
                return true;
            }
        }
        System.out.println("File not found.");
        return false;
    }

    // Display the status of the disk and file allocation table
    public void displayStatus() {
        System.out.println("\nDisk Block Status:");
        System.out.printf("%-10s%-15s%-10s\n", "Block", "Occupied by", "Status");
        for (int i = 0; i < disk.length; i++) {
            String occupant = (disk[i] == null) ? "-" : disk[i];
            String status = (disk[i] == null) ? "Free" : "Used";
            System.out.printf("%-10d%-15s%-10s\n", i, occupant, status);
        }

        System.out.println("\nFile Allocation Table:");
        System.out.printf("%-15s%-15s%-15s\n", "File Name", "Start Block", "Length");
        System.out.println("-----------------------------------------------");
        for (FileEntry f : files) {
            System.out.printf("%-15s%-15d%-15d\n", f.name, f.startBlock, f.length);
        }
        System.out.println("-----------------------------------------------");

    }

    // Main menu for testing
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        SequentialFileAllocationSimulator sim = new SequentialFileAllocationSimulator(20); // 20 disk blocks

        while (true) {
            System.out.println("\n1. Create File");
            System.out.println("2. Delete File");
            System.out.println("3. Display Disk Status");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int ch = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (ch) {
                case 1:
                    System.out.print("Enter file name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter file size (blocks): ");
                    int size = sc.nextInt();
                    sim.createFile(name, size);
                    break;
                case 2:
                    System.out.print("Enter file name to delete: ");
                    name = sc.nextLine();
                    sim.deleteFile(name);
                    break;
                case 3:
                    sim.displayStatus();
                    break;
                case 4:
                    System.out.println("Exiting...");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
            
        }
        
        
    }
    
}

