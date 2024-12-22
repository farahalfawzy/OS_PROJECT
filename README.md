# OS_PROJECT
This project is a simulation of an operating system implemented in Java, developed as part of the **CSEN 602 Operating Systems** course. The project aims to provide a hands-on understanding of operating system concepts such as process management, memory management, mutual exclusion, and scheduling.

## Table of Contents
- [Introduction](#introduction)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Contributors](#contributors)


## Introduction
The objective of this project is to build a simulation of an operating system, complete with:
- A basic interpreter to execute text-based programs.
- Simulated memory management for processes.
- Mutual exclusion using mutexes for critical resources.
- A Round Robin scheduling algorithm for process management.

The simulated OS reads and executes predefined programs, handling processes, scheduling, memory allocation, and resource management.

## Features
- **Interpreter:**
  - Parses and executes commands from text-based program files.
  - Supports custom syntax for operations like printing, assigning values, reading/writing files, and more.

- **Memory Management:**
  - Fixed-size memory (40 words) with space for code, variables, and process control blocks (PCBs).
  - Swapping processes to/from disk when memory is full.
  - Human-readable memory state displayed at every clock cycle.

- **Process Management:**
  - Processes are represented by PCBs, containing information like Process ID, state, program counter, and memory boundaries.

- **Scheduling:**
  - Round Robin scheduling algorithm with configurable time slices.
  - Manages ready and blocked queues, ensuring fair process execution.

- **Mutexes:**
  - Implemented for mutual exclusion over critical resources:
    - File access (read/write)
    - User input
    - Screen output
  - Uses `semWait` and `semSignal` commands for resource locking and releasing.

- **System Calls:**
  - Supports the following operations:
    - Read data from a file.
    - Write text to a file.
    - Print data to the screen.
    - Take user input.
    - Read and write data in memory.


## Technologies Used
- **Programming Language:** Java
- **Core Concepts:** Process scheduling, memory management, mutual exclusion, system calls
- **Data Structures:** Queues, linked lists, and custom objects for PCBs and memory

## Contributors
+ [Farah Maher](https://github.com/farahalfawzy)
+ [Omar Ahmed](https://github.com/OMAR-AHMED-SAAD)
+ [Malak El Wassif](https://github.com/malakElWassif)
+ [Abdullah El Nahas](https://github.com/AbdullahElNahas)
+ [Youssef Samer Samir](https://github.com/YoussefSamerSamir)
