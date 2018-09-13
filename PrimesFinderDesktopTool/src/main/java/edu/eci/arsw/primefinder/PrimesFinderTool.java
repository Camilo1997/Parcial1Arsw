package edu.eci.arsw.primefinder;

import edu.eci.arsw.mouseutils.MouseMovementMonitor;
import edu.eci.arsw.primefinder.PrimeFinder;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

public class PrimesFinderTool {

    public static void main(String[] args) throws InterruptedException {

        ArrayList<PrimeFinder> threadArray = new ArrayList<>();
        boolean valueFinish = false;
        int maxPrim = 10000;
        int numThreads = 4;
        PrimesResultSet prs = new PrimesResultSet("john");

        int range = maxPrim / numThreads;
        int minRange, maxRange;

        for (int x = 0; x < maxPrim; x += range){
            minRange = x;
            maxRange = x + range;
            BigInteger a = new BigInteger(String.valueOf(minRange));
            BigInteger b = new BigInteger(String.valueOf(maxRange));
            PrimeFinder pFinder = new PrimeFinder(a, b, prs);
            threadArray.add(pFinder);
        }

        for (PrimeFinder pfThread : threadArray) {
            pfThread.start();
        }
        
        while (!valueFinish) {
            try {
                //checpfThread every 10ms if the idle status (10 seconds without mouse
                //activity) was reached. 
                Thread.sleep(10);
                if (MouseMovementMonitor.getInstance().getTimeSinceLastMouseMovement() > 10000) {
                    int countThreads = 0;
                    for (PrimeFinder pfThread : threadArray) {
                        if (pfThread.isFinalizar()) {
                            countThreads++;
                        }
                        pfThread.resumeThread();
                    }
                    if (countThreads == threadArray.size()) {
                        valueFinish = true;
                    }
                } else {
                    for (PrimeFinder pfThread : threadArray) {
                        pfThread.pauseThread();
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(PrimesFinderTool.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("Prime numbers found:");

        System.out.println(prs.getPrimes());

    }

}
