package com.apipas.easyflow.android;

import java.io.Serializable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings("rawtypes")
public class StatefulContext implements Serializable {
	private static final long serialVersionUID = 2324535129909715649L;
	private static long idCounter = 1;
	
	private final String id;
	private State state;
	private final AtomicBoolean terminated = new AtomicBoolean(false);
	private final CountDownLatch completionLatch = new CountDownLatch(1);

	public StatefulContext() {
		id = newId() + ":" + getClass().getSimpleName();
	}

	public StatefulContext(String aId) {
		id = aId + ":" + getClass().getSimpleName();
	}

	public String getId() {
		return id;
	}
	
	public void setState(State state){
		this.state = state;
	}
	
	public State getState() {
		return state;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StatefulContext other = (StatefulContext) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	protected long newId() {
		return idCounter++;
	}

    public boolean isTerminated() {
        return terminated.get();
    }

    public boolean isRunning() {
        return isStarted() && !terminated.get();
    }

    public boolean isStarted() {
        return state != null;
    }

	protected void setTerminated() {
		this.terminated.set(true);
		this.completionLatch.countDown();
	}

	/**
	 * Block current thread until Context terminated
	 */
	protected void awaitTermination() {
	  try {
      this.completionLatch.await();
    } 
	  catch (InterruptedException e) {
	    Thread.interrupted();
	  }
	}
	
    @Override
    public String toString() {
        return id;
    }
}
