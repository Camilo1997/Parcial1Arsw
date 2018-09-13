package edu.eci.arsw.primefinder;

import edu.eci.arsw.math.MathUtilities;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PrimeFinder extends Thread {

    public boolean finishThread = false;
    public boolean pauseThread = false;
    public Object objectLock = new Object();
    BigInteger a, b;
    PrimesResultSet prs;

    public PrimeFinder(BigInteger a, BigInteger b, PrimesResultSet prs) {
        this.a = a;
        this.b = b;
        this.prs = prs;
    }

    @Override
    public void run() {
        findPrimes(a, b, prs);
    }

    public void findPrimes(BigInteger _a, BigInteger _b, PrimesResultSet prs) {
        BigInteger a = _a;
        BigInteger b = _b;
        MathUtilities mt = new MathUtilities();
        BigInteger i = a;
        int itCount = 0;

        obtainPrime(i, b, itCount, mt);

        finishThread = true;
    }

    private void obtainPrime(BigInteger i, BigInteger b, int itCount, MathUtilities mt) {
        while (i.compareTo(b) <= 0) {
            itCount++;
            if (mt.isPrime(i)) {
                prs.addPrime(i);
            }
            i = i.add(BigInteger.ONE);
            verifyPause();
        }
    }

    private void verifyPause() {
        if (pauseThread) {
            synchronized (objectLock) {
                try {
                    objectLock.wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(PrimeFinder.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void pauseThread() {
        pauseThread = true;
        System.out.println("User working again!");
    }

    public void resumeThread() {
        pauseThread = false;
        synchronized (objectLock) {
            objectLock.notifyAll();
        }
        System.out.println("Idle CPU ");
    }

    public boolean isFinalizar() {
        return finishThread;
    }
}
