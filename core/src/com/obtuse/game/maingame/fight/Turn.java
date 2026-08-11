package com.obtuse.game.maingame.fight;

public abstract class Turn implements Runnable {
    private Thread t;
    private String threadName = "turn";

    public Turn() {
        start();
    }

    public static void sleep(float seconds) {
        try {
            Thread.sleep((long) (seconds * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleep() {
        sleep(1f / 60);
    }

    public static void longSleep() {
        sleep(1f / 5);
    }

    public void start() {
        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }

    public boolean isAlive() {
        return t.isAlive();
    }

    public void stop() {
        t.stop();
    }
}
