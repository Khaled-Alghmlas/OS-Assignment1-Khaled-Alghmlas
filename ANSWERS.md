# Assignment Questions

## Instructions
Answer all 4 questions with detailed explanations. Each answer should be **3-5 sentences minimum** and demonstrate your understanding of the concepts.

---

## Question 1: Thread vs Process

**Question**: Explain the difference between a **thread** and a **process**. Why did we use threads in this assignment instead of creating separate processes?

**Your Answer:**
A process is a heavy, independent program with its own dedicated memory space and system resources.
A thread is a lightweight program that exists within a process and shares its memory with it's parent.

We used threads because creating 16 separate processes would be highly resource-intensive and very slow compared to threads.

Threads allow our Java simulation to quickly switch contexts ( switch between threads rapidly, creating the illusion of parallelism), run concurrently in shared memory, and easily track global variables like the context switch counter.

---

## Question 2: Ready Queue Behavior

**Question**: In Round-Robin scheduling, what happens when a process doesn't finish within its time quantum? Explain using an example from your program output.

**Your Answer:**

In Round-Robin scheduling, a process that finishes its time quantum is paused and forced to yield the CPU so the the next process can enter the CPU.

The scheduler then moves this process to the back of the ready queue so that it waits for its next turn. This mechanism prevents CPU starvation by ensuring no single heavy task can use all the system resources.

Example from my output:
```
┌─ Ready Queue ─────────────────────────────────────────────────────────────────
│ [P2 → P3 → P4 → P5 → P6 → P7 → P8 → P9 → P10 → P11 → P12 → P13 → P14 → P15 → P16]
└───────────────────────────────────────────────────────────────────────────────

  ▶ P1 executing quantum [3000ms] 
  ⚡ Quantum progress: [███████████████] 100%
  ⏸ P1 completed quantum 3000ms │ Overall progress: [██████████████████░░] 94%
    Remaining time: 174ms
  ↻ P1 yields CPU for context switch

  ➕ P1 (Priority: 1) added to ready queue │ Burst time: 3174ms
┌─ Ready Queue ─────────────────────────────────────────────────────────────────
│ [P3 → P4 → P5 → P6 → P7 → P8 → P9 → P10 → P11 → P12 → P13 → P14 → P15 → P16 → P1]
└───────────────────────────────────────────────────────────────────────────────

```

**Explanation of example:**
In my output, P1 ran for its time quantam which is 3000ms, but it still had 174ms.
Because it hasn't finished, it yields the CPU and is removed from the active state, then moves to the back of the ready queue to await it's turn.

---

## Question 3: Thread States

**Question**: A thread can be in different states: **New**, **Runnable**, **Running**, **Waiting**, **Terminated**. Walk through these states for one process (P1) from your simulation.

**Your Answer:**

1. **New**: P1 is initialized as an object [Thread P1 = new Thread(process)], it is created but still hasn't started.

2. **Runnable**: When it is triggered by [P!.start()], P1 then enters the ready queue and awaist the scheduler to allocate the time quantum.

3. **Running**: Then, The scheduler selects P! when [P1.run()] is called, subsequently, the method executes the process on the CPU

4. **Waiting**: When the process waits for an I/O, Processing delays, or when it is forced to yield the CPU so that other processes can start running, we can simulate waiting by [Thread.sleep()].

5. **Terminated**: P1 finishes its [run()] method. The main thread may also call [P1.join()] to confirmed completion.

---

## Question 4: Real-World Applications

**Question**: Give **TWO** real-world examples where Round-Robin scheduling with threads would be useful. Explain why this scheduling algorithm works well for those scenarios.

**Your Answer:**

### Example 1: [Name of application/scenario]

**Description**: 
[Describe the real-world scenario or application]

**Why Round-Robin works well here**: 
[Explain why Round-Robin scheduling is suitable. Consider fairness, responsiveness, predictability, etc.]

### Example 2: [Name of application/scenario]

**Description**: 
[Describe the real-world scenario or application]

**Why Round-Robin works well here**: 
[Explain why Round-Robin scheduling is suitable. Consider fairness, responsiveness, predictability, etc.]

---

## Summary

**Key concepts I understood through these questions:**
1. 
2. 
3. 

**Concepts I need to study more:**
1. 
2. 
