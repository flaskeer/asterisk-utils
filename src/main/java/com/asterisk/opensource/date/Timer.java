package com.asterisk.opensource.date;


import java.util.concurrent.TimeUnit;

/**
 * Title: Timer
 * Description: Timer for record method execution times, easy in log print,
 * we only need create a Timer object before invoke a method, and log it as a param.
 * <p>
 * Timer timer = new Timer(); timer.start(); timer.stop(); timer.getTotalTime();
 * Bad Usage, Needed To Be ThreadLocal.
 *
 */
public class Timer {

    private long startTime;
    private long endTime; // If this class used as a optimistic lock, this field need to be declared as AtomicLong

    protected Clock clock = Clock.DEFAULT; // Easy to mock and do some interesting things.

    public Timer() {
        start();
    }

    /**
     * Start record, invoke by construct method
     */
    public void start() {
        startTime = clock.getCurrentTimeInMillis();
    }

    /**
     * Stop record
     */
    public void stop() {
        endTime = clock.getCurrentTimeInMillis();
    }

    /**
     * Get the beginning time miles.
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * Get the ending time miles.
     */
    public long getEndTime() {
        return endTime;
    }

    /**
     * Get execute time from record
     */
    public long getTotalTime() {
        return getEndTime() - getStartTime();
    }

    /**
     * Is record time longer than expected time.
     */
    public boolean isExpired(long expiredTime) {
        stop();
        return getTotalTime() > TimeUnit.MILLISECONDS.toMillis(expiredTime);
    }

    public boolean isNotExpired(long expiredTime) {
        return !isExpired(expiredTime);
    }

    /**
     * helper for better logger print
     */
    public String formatStartTime() {
        return formatTimestamp(getStartTime());
    }

    /**
     * helper for better logger print
     */
    public String formatEndTime() {
        return formatTimestamp(getEndTime());
    }

    private String formatTimestamp(long timestamp) {
        return String.format("%1$tY-%1$tb-%1$td %1$tT %tZ", timestamp);
    }

    /**
     * helper for better logger print
     */
    @Override
    public String toString() {
        stop();
        return Long.toString(getTotalTime());
    }
}
