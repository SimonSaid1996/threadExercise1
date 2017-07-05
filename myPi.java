import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class myPi {
	static long sysTime =0;
    public static long innerValue(long n){
        long inside=0;
        double x, y;
        for (long i=0; i<n; i++){
            x=ThreadLocalRandom.current().nextDouble(0.000, 1.000);														 // get random double
            y=ThreadLocalRandom.current().nextDouble(0.000, 1.000);
            
           
            if ((Math.pow(x, 2) + Math.pow(y, 2)) <1)																	 // check if the pint lies "inside"
                inside++;																								        // x^2 + y^2 < 1
        }
        return inside;
    }
    
    public static void main(String[] args) {
		long started =System.currentTimeMillis();
		long numThreads=0;			// number of threads
		long numIteration=0;			// total number of iterations
		AtomicLong inside=new AtomicLong(0);	// total number of values "inside" the circle
	    double pi=0;				// calculate value of pi

	    try{																							// get number of threads			
	        numThreads = Long.parseLong(args[0]);
	        if (numThreads<1)
	                throw new Exception();
	    } catch (Exception ex){
	        System.out.println("Expected positive int as argument!");
	        System.exit(1);
	    }

	    
	    try{																							// get number of iterations
	        numIteration = Long.parseLong(args[1]);
	        if (numIteration<1)
	                throw new Exception();
	    } catch (Exception ex){
	        System.out.println("Expected positive int as argument!");
	        System.exit(1);
	    }

	   
	    final long iterationPerThread = numIteration / numThreads;									 // calculate iterationPerThread

	    
	    Thread[] ts = new Thread[(int)numThreads];													// create threads				

	    for (int j = 0; j < (int)numThreads; j++){
	        final int fj = j;
	        long finside = 0;
	        ts[fj] = new Thread(() ->
	            {
	                inside.addAndGet(innerValue(iterationPerThread));
					sysTime= System.currentTimeMillis();
	            });
	    }    
 
		try {
		    for (Thread t : ts)	
		    	t.start();
		    for (Thread t : ts)
		    	t.join();
		} catch (InterruptedException iex) {}

		System.out.println("Total \t = " + numIteration);														// Print out all the values
		System.out.println("Inside \t = " + inside.get());
		double ratio = (double)inside.get() / numIteration;
		System.out.println("Ratio \t = " + ratio);
		System.out.println("Pi \t = " + ratio * 4.00);
		
		long realT = System.currentTimeMillis() - started;
		System.out.println("real " + realT + " milliseconds" );
		System.out.println("sys " + (sysTime - started) +" milliseconds");
		System.out.println("user " + (realT - (sysTime - started) ) +" milliseconds");
    }	
}
