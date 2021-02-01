import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.*;

public class Main {
    public static final int CARS_COUNT = 4;
    public static void main(String[] args) {
        CyclicBarrier cb = new CyclicBarrier(CARS_COUNT);
        Semaphore smp = new Semaphore(CARS_COUNT / 2);
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        Race race = new Race(new Road(60), new Tunnel(smp), new Road(40));
        Car[] cars = new Car[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10), cb);
        }
        CountDownLatch latch = new CountDownLatch(CARS_COUNT);
        ExecutorService executorService = Executors.newFixedThreadPool(CARS_COUNT);
        for (int i = 0; i < cars.length; i++) {
            //new Thread(cars[i]).start();
            int finalI = i;
            executorService.execute(() -> {
                cars[finalI].run();
                latch.countDown();
            });
        }
        while (true){
            if (Car.getReadyCount() == CARS_COUNT){
                System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
                break;
            }
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
    }
}

