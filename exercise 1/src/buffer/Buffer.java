package buffer;

import se.lth.cs.realtime.semaphore.*;

/**
 * The buffer.
 */
class Buffer {
	Semaphore mutex; // For mutual exclusion blocking.
	Semaphore free; // For buffer full blocking.
	Semaphore avail; // For blocking when no data is available.
	String buffData; // The actual buffer.

	Buffer() {
		mutex = new MutexSem();
		free = new CountingSem(1);
		avail = new CountingSem();
	}

	void putLine(String input) {
		free.take(); // Wait for buffer empty.
		mutex.take(); // Wait for exclusive access.
		buffData = new String(input); // Store copy of object.
		mutex.give(); // Allow others to access.
		avail.give(); // Allow others to get line.
	}

	String getLine() {
		// Exercise 2 ...
		// Here you should add code so that if the buffer is empty, the
		// calling process is delayed until a line becomes available.
		// A caller of putLine hanging on buffer full should be released.
		// ...
		return null;
	}
}
