import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;


// ANSI Color Codes for enhanced terminal output
class Colors {
    public static final String RESET = "\u001B[0m";
    public static final String BOLD = "\u001B[1m";
    public static final String CYAN = "\u001B[36m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String MAGENTA = "\u001B[35m";
    public static final String BLUE = "\u001B[34m";
    public static final String RED = "\u001B[31m";
    public static final String BG_BLUE = "\u001B[44m";
    public static final String BG_GREEN = "\u001B[42m";
    public static final String WHITE = "\u001B[37m";
    public static final String BRIGHT_WHITE = "\u001B[97m";
    public static final String BRIGHT_CYAN = "\u001B[96m";
    public static final String BRIGHT_YELLOW = "\u001B[93m";
    public static final String BRIGHT_GREEN = "\u001B[92m";

}

// Class representing a process that implements Runnable to be run by a thread
class Process implements Runnable {
    private String name; // Name of the process
    private int burstTime; // Total time the process requires to complete (in milliseconds)
    private int timeQuantum; // Time slice (time quantum) allowed per CPU access (in milliseconds)
    private int remainingTime; // Time left for the process to finish its execution
    //Feature (1): Add a priority field to the Process class (integer 1-5, where 5 is highest)
    private int priority; // priority level of the process (1 to 5, 5 is highest priority)

    //Feature (3): Add fields to track the time of creation, total wait time, and entry time for each process
    private long totalWaitTime;
    private long entryTime; 
    private long timeOfCreation; 
    
    // Constructor to initialize the process with name, burst time, and time quantum and **priority -> (Feature (1))
    public Process(String name, int burstTime, int timeQuantum, int priority) {
        this.name = name;
        this.burstTime = burstTime;
        this.timeQuantum = timeQuantum;
        this.remainingTime = burstTime; // Initially, remaining time is equal to the burst time
        this.priority = priority; // Feature (1): Initialize the priority of the process

        this.timeOfCreation = System.currentTimeMillis(); // Feature (3): Set the time of creation to the current time when the process is instantiated 
        this.entryTime = this.timeOfCreation; // Feature (3): Set entry time to the time of creation initially
        this.totalWaitTime = 0; // Feature (3): Initialize total wait time to 0
    }

    // This method will be called when the thread for this process is started
    @Override
    public void run() {
        // Simulate running for either the time quantum or remaining time, whichever is smaller
        int runTime = Math.min(timeQuantum, remainingTime); // Run for the smaller of the two times
        
        // Show quantum execution starting
        String quantumBar = createProgressBar(0, 15);
        System.out.println(Colors.BRIGHT_GREEN + "  ▶ " + Colors.BOLD + Colors.CYAN + name + 
                          Colors.RESET + Colors.GREEN + " executing quantum" + Colors.RESET + 
                          " [" + runTime + "ms] ");
        
        try {
            // Simulate quantum execution with progress updates
            int steps = 5; // Number of progress updates
            int stepTime = runTime / steps;
            
            for (int i = 1; i <= steps; i++) {
                Thread.sleep(stepTime);
                int quantumProgress = (i * 100) / steps;
                quantumBar = createProgressBar(quantumProgress, 15);
                
                // Clear line and show updated progress
                System.out.print("\r  " + Colors.YELLOW + "⚡" + Colors.RESET + 
                                " Quantum progress: " + quantumBar);
            }
            System.out.println(); // New line after quantum completion
            
        } catch (InterruptedException e) {
            System.out.println(Colors.RED + "\n  ✗ " + name + " was interrupted." + Colors.RESET);
        }
        
        remainingTime -= runTime; // Deduct the run time from the remaining time
        int overallProgress = (int) (((double)(burstTime - remainingTime) / burstTime) * 100);
        String overallProgressBar = createProgressBar(overallProgress, 20);
        
        System.out.println(Colors.YELLOW + "  ⏸ " + Colors.CYAN + name + Colors.RESET + 
                          " completed quantum " + Colors.BRIGHT_YELLOW + runTime + "ms" + Colors.RESET + 
                          " │ Overall progress: " + overallProgressBar);
        System.out.println(Colors.MAGENTA + "     Remaining time: " + remainingTime + "ms" + Colors.RESET);
        
        // If the process still has remaining time, it yields CPU for the next process
        if (remainingTime > 0) {
            System.out.println(Colors.BLUE + "  ↻ " + Colors.CYAN + name + Colors.RESET + 
                              " yields CPU for context switch" + Colors.RESET);
        } else {
            // If no time is left, the process has finished its execution
            System.out.println(Colors.BRIGHT_GREEN + "  ✓ " + Colors.BOLD + Colors.CYAN + name + 
                              Colors.RESET + Colors.BRIGHT_GREEN + " finished execution!" + 
                              Colors.RESET);
        }
        System.out.println();
    }
    
    // Helper method to create a visual progress bar
    private String createProgressBar(int progress, int width) {
        int filled = (progress * width) / 100;
        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < width; i++) {
            if (i < filled) {
                bar.append(Colors.GREEN + "█" + Colors.RESET);
            } else {
                bar.append(Colors.WHITE + "░" + Colors.RESET);
            }
        }
        bar.append("] ").append(progress).append("%");
        return bar.toString();
    }

    // Method to run the last process to completion, ignoring the time quantum
    public void runToCompletion() {
        try {
            // Run for the remaining time without splitting into smaller time slices
            System.out.println(Colors.BRIGHT_CYAN + "  ⚡ " + Colors.BOLD + Colors.CYAN + name + 
                              Colors.RESET + Colors.BRIGHT_CYAN + " is the last process, running to completion" + 
                              Colors.RESET + " [" + remainingTime + "ms]");
            Thread.sleep(remainingTime); // Run until completion
            remainingTime = 0; // Mark the process as completed
            System.out.println(Colors.BRIGHT_GREEN + "  ✓ " + Colors.BOLD + Colors.CYAN + name + 
                              Colors.RESET + Colors.BRIGHT_GREEN + " finished execution!" + Colors.RESET);
            System.out.println();
        } catch (InterruptedException e) {
            System.out.println(Colors.RED + "  ✗ " + name + " was interrupted." + Colors.RESET);
        }
    }

    // Getter methods for process name, burst time, and remaining time
    public String getName() {
        return name;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public int getRemainingTime() {
        return remainingTime;
    }
    // Feature (1): Getter method for priority
    public int getPriority() {
        return priority;
    }

    public int getTotalWaitTime() { // Feature (3): Getter method for total wait time
        return (int) totalWaitTime;
    }

    public int getEntryTime() { // Feature (3): Getter method for entry time
        return (int) entryTime;
    }
     public int getTimeOfCreation() { // Feature (3): Getter method for time of creation
        return (int) timeOfCreation;
    }

    // Feature (3): Method to update the total wait time for the process
    public void updateWaitTime(){
        long currentTime = System.currentTimeMillis();
        long waitTime = currentTime - entryTime; // Calculate the wait time since the last entry
       totalWaitTime += waitTime; // Add the wait time to the total wait time
    }
    
    public void setEntryTime(long entryTime) { // Feature (3): Setter method for entry time
        this.entryTime = entryTime;
    }

    // Check if the process has finished (i.e., no remaining time)
    public boolean isFinished() {
        return remainingTime <= 0;
    }
}

public class SchedulerSimulation {
    private static int contextSwitchesCount = 0; // Feature (2): Static counter to track the number of context switches

    private static List<Process> finishedProcess = new ArrayList<>(); // Feature (3): List to keep track of all processes for wait time calculation

    public static void main(String[] args) {
        // ⚠️ IMPORTANT: Put your student ID here to seed the random number generator
        // This makes your output unique to you - DO NOT forget to change this!
        int studentID = 445050235;  // ← CHANGE THIS TO YOUR ACTUAL STUDENT ID
        
        Random random = new Random(studentID);
        
        // Define the time quantum in milliseconds (the maximum time a process gets in one round)
        // Choose a random number between 2000 and 5000 ms with a step of 1000 ms
        int timeQuantum = 2000 + random.nextInt(4) * 1000; // Random: 2000, 3000, 4000, or 5000
        
        // Generate random number of processes between 10 and 20
        int numProcesses = 10 + random.nextInt(11); // Random number between 10 and 20
        
        // Queue to manage processes in a First-In-First-Out (FIFO) order
        Queue<Thread> processQueue = new LinkedList<>();
        
        // Map to associate each thread with its respective process object
        Map<Thread, Process> processMap = new HashMap<>();
        
        // Print simulation header with elegant formatting
        System.out.println("\n" + Colors.BOLD + Colors.BRIGHT_CYAN + 
                          "╔═══════════════════════════════════════════════════════════════════════════════════════╗" + 
                          Colors.RESET);
        System.out.println(Colors.BOLD + Colors.BRIGHT_CYAN + "║" + Colors.RESET + 
                          Colors.BG_BLUE + Colors.BRIGHT_WHITE + Colors.BOLD + 
                          "                          CPU SCHEDULER SIMULATION                                " + 
                          Colors.RESET + Colors.BOLD + Colors.BRIGHT_CYAN + "║" + Colors.RESET);
        System.out.println(Colors.BOLD + Colors.BRIGHT_CYAN + 
                          "╠═══════════════════════════════════════════════════════════════════════════════════════╣" + 
                          Colors.RESET);
        System.out.println(Colors.BOLD + Colors.BRIGHT_CYAN + "║" + Colors.RESET + 
                          Colors.YELLOW + "  ⚙ Processes:     " + Colors.RESET + Colors.BRIGHT_YELLOW + 
                          String.format("%-65s", numProcesses) + 
                          Colors.BOLD + Colors.BRIGHT_CYAN + "║" + Colors.RESET);
        System.out.println(Colors.BOLD + Colors.BRIGHT_CYAN + "║" + Colors.RESET + 
                          Colors.YELLOW + "  ⏱ Time Quantum:  " + Colors.RESET + Colors.BRIGHT_YELLOW + 
                          String.format("%-65s", timeQuantum + "ms") + 
                          Colors.BOLD + Colors.BRIGHT_CYAN + "║" + Colors.RESET);
        System.out.println(Colors.BOLD + Colors.BRIGHT_CYAN + "║" + Colors.RESET + 
                          Colors.YELLOW + "  🔑 Student ID:    " + Colors.RESET + Colors.BRIGHT_YELLOW + 
                          String.format("%-65s", studentID) + 
                          Colors.BOLD + Colors.BRIGHT_CYAN + "║" + Colors.RESET);
        System.out.println(Colors.BOLD + Colors.BRIGHT_CYAN + 
                          "╚═══════════════════════════════════════════════════════════════════════════════════════╝" + 
                          Colors.RESET + "\n");
        
        // Create 'numProcesses' number of processes
        for (int i = 1; i <= numProcesses; i++) {

            // Random priority between 1 and 5 (Feature (1))
            int priority = random.nextInt(5) + 1; 

            // Random burst time for each process between timeQuantum/2 and 3*timeQuantum
            int burstTime = timeQuantum/2 + random.nextInt(2 * timeQuantum + 1);
            
            // Create a new process object with a unique name, burst time, and the defined time quantum
            Process process = new Process("P" + i, burstTime, timeQuantum, priority); // Feature (1): Pass priority to the process constructor
            
            // Add the process to the ready queue and the map
            addProcessToQueue(process, processQueue, processMap);
        }
        
        // Start of the scheduler simulation
        System.out.println(Colors.BOLD + Colors.GREEN + 
                          "╔════════════════════════════════════════════════════════════════════════════════╗" + 
                          Colors.RESET);
        System.out.println(Colors.BOLD + Colors.GREEN + "║" + Colors.RESET + 
                          Colors.BG_GREEN + Colors.WHITE + Colors.BOLD + 
                          "                        ▶  SCHEDULER STARTING  ◀                               " + 
                          Colors.RESET + Colors.BOLD + Colors.GREEN + "║" + Colors.RESET);
        System.out.println(Colors.BOLD + Colors.GREEN + 
                          "╚════════════════════════════════════════════════════════════════════════════════╝" + 
                          Colors.RESET + "\n");
        
        // Loop to manage the scheduling of processes
        while (!processQueue.isEmpty()) {
            // Get the next thread from the queue (FIFO)
            Thread currentThread = processQueue.poll(); // Dequeues the next thread
            
            // Print the current process queue (list of process IDs in the queue)
            System.out.println(Colors.BOLD + Colors.MAGENTA + "┌─ Ready Queue " + "─".repeat(65) + Colors.RESET);
            System.out.print(Colors.MAGENTA + "│ " + Colors.RESET + Colors.BRIGHT_WHITE + "[" + Colors.RESET);
            int queueCount = 0;
            for (Thread thread : processQueue) {
                Process process = processMap.get(thread);
                if (queueCount > 0) System.out.print(Colors.WHITE + " → " + Colors.RESET);
                System.out.print(Colors.BRIGHT_CYAN + process.getName() + Colors.RESET);
                queueCount++;
            }
            if (queueCount == 0) {
                System.out.print(Colors.YELLOW + "empty" + Colors.RESET);
            }
            System.out.println(Colors.BRIGHT_WHITE + "]" + Colors.RESET);
            System.out.println(Colors.BOLD + Colors.MAGENTA + "└" + "─".repeat(79) + Colors.RESET + "\n");
            
            // Start the thread, which will run the process for one time quantum

            // Feature (2): Increment the context switch counter each time we start a new thread (i.e., switching to a new process)
            contextSwitchesCount++;
            currentThread.start();
            
            try {
                // Wait for the thread to finish its time quantum before continuing to the next process
                currentThread.join();
            } catch (InterruptedException e) {
                System.out.println("Main thread interrupted.");
            }
            
            // Retrieve the process associated with the thread from the map
            Process process = processMap.get(currentThread);
            // Feature (3): Update the wait time for the process after it has run for its quantum
            process.updateWaitTime();
            
            // Check if the process is not finished
            if (!process.isFinished()) {
                // If the process still has remaining time, check if there are more processes in queue
                if (!processQueue.isEmpty()) {
                    process.setEntryTime(System.currentTimeMillis()); // Feature (3): Update entry time for the next wait time calculation
                    // Re-enqueue the process to give it another chance to run in the next round
                    addProcessToQueue(process, processQueue, processMap);
                } else {
                    // If this is the last process in the queue, run it to completion
                    System.out.println(Colors.BRIGHT_YELLOW + "  ⚠ " + Colors.CYAN + process.getName() + 
                                      Colors.RESET + Colors.YELLOW + " is the last process → running to completion" + 
                                      Colors.RESET);
                    contextSwitchesCount++; // Feature (2): Increment context switch count before running the last process
                    process.runToCompletion(); // Run until the process completes
                    finishedProcess.add(process); // Feature (3): Add the finished process to the list for wait time calculation
                } 

                

            } else {
                finishedProcess.add(process); 
                System.out.println(Colors.BRIGHT_GREEN + "  ✓ " + Colors.BOLD + Colors.CYAN + process.getName() + Colors.RESET + Colors.BRIGHT_GREEN + " finished execution!" + Colors.RESET);
        }
    }
        // End of the scheduler simulation
        System.out.println(Colors.BOLD + Colors.BRIGHT_GREEN + 
                          "╔════════════════════════════════════════════════════════════════════════════════╗" + 
                          Colors.RESET);
        System.out.println(Colors.BOLD + Colors.BRIGHT_GREEN + "║" + Colors.RESET + 
                          Colors.BG_GREEN + Colors.WHITE + Colors.BOLD + 
                          "                     ✓  ALL PROCESSES COMPLETED  ✓                            " + 
                          Colors.RESET + Colors.BOLD + Colors.BRIGHT_GREEN + "║" + Colors.RESET);
        System.out.println(Colors.BOLD + Colors.BRIGHT_GREEN + 
                          "╚════════════════════════════════════════════════════════════════════════════════╝" + 
                          Colors.RESET + "\n");
                           System.out.println("\n" +  "╔════════════════════════════════════════════════════════════════════════════════╗"+"\n"
                            + Colors.YELLOW + "                           Total Context Switches: " + Colors.BRIGHT_GREEN + contextSwitchesCount + Colors.RESET + "\n");  // Feature (2): Print the total number of context switches at the end of the simulation
            System.out.println(Colors.BOLD + Colors.BRIGHT_GREEN);   
                                    
            printWaitingTimeSummary();
            System.out.println(Colors.RESET + "╚════════════════════════════════════════════════════════════════════════════════╝");
         // Feature (3): Print the waiting time summary at the end of the simulation
    }
    
    // Method to add a process to the queue and map, while printing a "ready" message
    public static void addProcessToQueue(Process process, Queue<Thread> processQueue, Map<Thread, Process> processMap) {
        // Create a new thread to run the process
        Thread thread = new Thread(process);
        
        // Add the thread to the ready queue
        processQueue.add(thread);
        
        // Map the thread to the process, so we can track the process associated with each thread
        processMap.put(thread, process);
        
        // Print a message indicating the process has entered the ready queue

        /*Feature (1): Print the priority of the process when it is added to the ready queue 
          Example output: "P1 (Priority: 4) enters the ready queue..." 
        */  
      
        System.out.println(Colors.BLUE + "  ➕ " + Colors.BOLD + Colors.CYAN + process.getName() + 
                          Colors.RESET + Colors.BRIGHT_WHITE + " (Priority: " + process.getPriority() + ")" + // Feature (1): Print priority in the message
                          Colors.RESET + Colors.BLUE + " added to ready queue" + Colors.RESET + 
                          " │ Burst time: " + Colors.YELLOW + process.getBurstTime() + "ms" + 
                          Colors.RESET);
    }

   // Feature (3): Method to display waiting time summary table
    // Feature (3): Simplified Waiting Time Summary
    public static void printWaitingTimeSummary() {
        System.out.println("\n                           ***Final Statistics***"+Colors.RESET);
        
        long totalWait = 0;

        for (Process p : finishedProcess) {
           System.out.println(p.getName() + " | Burst time: " + p.getBurstTime() + "ms | Wait time: " + Colors.BRIGHT_YELLOW + p.getTotalWaitTime() + "ms" + Colors.RESET);
            totalWait += p.getTotalWaitTime();
        }

        double avg = (double) totalWait / finishedProcess.size();
        
        System.out.println("-------------------");
        System.out.printf(Colors.BRIGHT_GREEN + "                           Average Waiting Time: %.2fms\n" + Colors.RESET, avg);
    }

}
