import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Lesson4 {
    private volatile static int threadVar = 1;
    static Object mon = new Object();

    public static void main(String[] args) {
        // попробовал 2 варианта реализации
        // с FixedExecutor оставил.

        Thread thrA = new Thread(() -> {
//            for (int i = 0; i < 5; i++) {
                synchronized (mon) {
                    while (threadVar != 1) {
                        try {
                            mon.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    threadVar = 2;
                    System.out.print("A");
                    mon.notifyAll();
                }
//            }
        });

        Thread thrB = new Thread(() -> {
//            for (int i = 0; i < 5; i++) {
                synchronized (mon) {
                    while (threadVar != 2) {
                        try {
                            mon.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    threadVar = 3;
                    System.out.print("B");
                    mon.notifyAll();
                }
//            }
        });

        Thread thrC = new Thread(() -> {
//            for (int i = 0; i < 5; i++) {
                synchronized (mon) {
                    while (threadVar != 3) {
                        try {
                            mon.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    threadVar = 1;
                    System.out.print("C");
                    mon.notifyAll();
                }
//            }
        });

//        thrA.start();
//        thrB.start();
//        thrC.start();

        ExecutorService thrPool = Executors.newFixedThreadPool(3);

        for (int i = 0; i < 5; i++) {
            thrPool.execute(thrA);
            thrPool.execute(thrB);
            thrPool.execute(thrC);
        }
        thrPool.shutdown();
    }
}
