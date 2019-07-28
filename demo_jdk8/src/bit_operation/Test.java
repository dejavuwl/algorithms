package bit_operation;

import java.util.concurrent.CountDownLatch;

/**
 * @author farwind dejavuwl@gmail.com
 * @version 2019/7/28
 * Description
 */
public class Test {
    public static void main(String[] args) throws InterruptedException {
        Test test = new Test();
        long timeTasks = test.timeTasks(10, new Runnable() {
            @Override
            public void run() {

//                    Thread.sleep(1000);
                    Hello hello = new Hello();
                int a = Math.round((int)(Math.random()*100));
                hello.test(a);

            }
        });
        System.out.println(timeTasks);
    }

    public long timeTasks(int nTreads,final Runnable task) throws InterruptedException {
        for (int i = 0; i < 100000; i++) {
            task.run();
        }

        final CountDownLatch stateGate = new CountDownLatch(1);
        final CountDownLatch endGate = new CountDownLatch(nTreads);
        for (int i = 0; i < nTreads; i++) {
            Thread t = new Thread(){
                @Override
                public void run() {
                    try {
                        stateGate.await();
                        try {
                            task.run();
                        }finally {
                            endGate.countDown();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            t.start();
        }
        long start = System.currentTimeMillis();
        stateGate.countDown();
        endGate.await();
        long end = System.currentTimeMillis();
        return end-start;
    }
}
