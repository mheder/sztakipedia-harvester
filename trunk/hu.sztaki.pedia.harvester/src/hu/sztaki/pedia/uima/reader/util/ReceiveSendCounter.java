/*******************************************************************************
 * Copyright 2012 Tamas Farkas, MTA SZTAKI, Hungary
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *******************************************************************************/
package hu.sztaki.pedia.uima.reader.util;

/**
 * Singleton counter to keep up the number of articles received, sent, filtered,
 * and the number of currently running threads
 * 
 * @author Tamas Farkas, MTA SZTAKI, Hungary
 * 
 */
public final class ReceiveSendCounter {
	private static ReceiveSendCounter instance = new ReceiveSendCounter();
	private volatile long received = 0;
	private volatile long sent = 0;
	private volatile long filtered = 0;
	private volatile int runningThreads = 0;

	private ReceiveSendCounter() {

	}

	public static ReceiveSendCounter getInstance() {
		return instance;
	}

	public synchronized void countReceived() {
		received++;
	}

	public synchronized void countSent() {
		sent++;
	}

	public synchronized void countFiltered() {
		filtered++;
	}

	public synchronized void threadStarted() {
		runningThreads++;
	}

	public synchronized void threadFinished() {
		runningThreads--;
	}

	public String printState() {
		return "Articles received:" + received + ", sent:" + sent + ", filtered:" + filtered
				+ " Articles in progress:" + (received - sent - filtered)
				+ " Curr. running threads:" + runningThreads;
	}

	public long getReceived() {
		return received;
	}

	public long getSent() {
		return sent;
	}

	public long getFiltered() {
		return filtered;
	}

	public int getRunningThreads() {
		return runningThreads;
	}

}
