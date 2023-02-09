package com.company;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.random;
import static java.lang.Thread.sleep;


public class Main {

    public static void main(String[] args) throws InterruptedException {
        final int timeManufaktured = 300;
        final int timeWaiting = 1500;
        final int maxBuyers = 10;
        final boolean[] threadStop = new boolean[1];
        List<Buyer> buyers = new ArrayList<>();

        List marka = Arrays.asList(
                "TOYOTA",
                "LEXUS",
                "GAZ",
                "NISSAN",
                "MAZDA",
                "BMW",
                "MERCEDES",
                "FERRARI",
                "VAZ"
        );
        List<String> names = new ArrayList<>();
        Thread salon = new Thread(() -> {
            while (!threadStop[0]) {
                synchronized (names) {
                    int i = (int) ((int) (marka.size() - 1) * random());
                    String s = (String) marka.get(i);
                    names.add(s);
                    System.out.println("Производитель доставил в салон автомобиль марки " + s);
                    names.notify();
                }
                try {
                    sleep(timeManufaktured);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
            System.out.println("\n" + maxBuyers + " покупателей обслужено");
            return;
        });
        salon.start();

        Thread autoBuyer = new Thread(() -> {
            int counterBuyers = maxBuyers;
            List<Instant> timeCounter = new ArrayList<>();
            List<String> buyersName = new ArrayList<>();
            int numberClient = 1;
            int index = 0;
            while (counterBuyers != 0) {
                synchronized (names) {
                    int j = (int) ((marka.size() - 1) * random());
                    buyers.add(new Buyer((String) marka.get(j), (int) (10 * timeWaiting * random()), numberClient));
                        System.out.println("Клиент по номером " + buyers.get(index++).getNumberClient() + " зашёл в автосалон за автомобилем " + marka.get(j));
                        numberClient++;

                    for (int i = 0; i < buyers.size(); i++) {
                        ;
                        timeCounter.add(i, Instant.now());
                        try {
                            sleep(timeWaiting);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (!names.isEmpty()) {
                            try {
                                Instant t = Instant.now();
                                if (Duration.between(timeCounter.get(i), t).toMillis() > buyers.get(i).getTimeWaiting()) {
                                    System.out.println("Клиент под номером " + buyers.get(i).getNumberClient() + ", не дождавшись нужного ему авто, ушёл несолоно хлебавши ");
                                    buyers.remove(i);
                                    index--;
                                } else if (names.contains(buyers.get(i).getMarkaAuto())) {
                                    System.out.println("\n Клиент под номером " + buyers.get(i).getNumberClient() + " уехал на авто марки " + buyers.get(i).getMarkaAuto());
                                    names.remove(buyers.get(i).getMarkaAuto());
                                    counterBuyers--;
                                    buyers.remove(i);
                                    index--;


                                }
                                names.wait();

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }

            }
            threadStop[0] = true;
            return;
        });
        autoBuyer.start();
    }
}
