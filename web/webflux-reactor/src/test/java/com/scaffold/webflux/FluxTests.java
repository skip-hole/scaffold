package com.scaffold.webflux;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.junit.Test;
import reactor.core.Disposable;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;


public class FluxTests {

    private final Random random = new Random();


    @Test
    public void disposableExample() throws InterruptedException {
        Disposable disposable = Flux.interval(Duration.ofMillis(50))
                .subscribe(data -> System.out.println("onNext: " + data));
        Thread.sleep(200);
        disposable.dispose();
    }

    @Test
    public void mapExample() {
        Flux.just(1, 2, 3).log()
                .map(i -> i * 2)
                .subscribe(System.out::println);
        System.out.println("finish");
    }

    @Test
    public void indexExample() {
        Flux.range(2018, 5)
                .timestamp()
                .index()
                .subscribe(e -> System.out.println(
                        String.format("index: %d, ts: %s, value: %s",
                                e.getT1(),
                                Instant.ofEpochMilli(e.getT2().getT1()),
                                e.getT2().getT2())
                ));
    }

    @Test
    public void filterExample() {
        Flux.just(1, 2, 3)
                .filter(i -> i % 2 == 0)
                .subscribe(System.out::println);

        System.out.println("---");

        Flux.just(1, 2, 3)
                .take(2)
                .subscribe(System.out::println);

        System.out.println("---");

        Flux.just(1, 2, 3)
                .takeLast(1)
                .subscribe(System.out::println);

        System.out.println("---");

        Flux.just(1, 2, 3)
                .takeUntil(i -> i % 2 == 0)
                .subscribe(System.out::println);

        System.out.println("---");

        Flux.just(1, 2, 3)
                .elementAt(1)
                .subscribe(System.out::println);
    }

    @Test
    public void shouldCreateDefer() {
        Mono<User> userMono = requestUserData(null);
        StepVerifier.create(userMono)
                .expectNextCount(0)
                .expectErrorMessage("Invalid user session")
                .verify();
    }

    private Mono<User> requestUserData(String sessionId) {
        return Mono.defer(() ->
                isValidSession(sessionId)
                        ? Mono.fromCallable(() -> requestUser(sessionId))
                        : Mono.error(new RuntimeException("Invalid user session")));
    }

    private boolean isValidSession(String sessionId) {
        return sessionId != null;
    }

    private User requestUser(String sessionId) {
        return new User();
    }

    @Test
    public void startStopStreamProcessing() throws Exception {
        Mono<?> startCommand = Mono.delay(Duration.ofSeconds(1));
        Mono<?> stopCommand = Mono.delay(Duration.ofSeconds(3));
        Flux<Long> streamOfData = Flux.interval(Duration.ofMillis(100));

        streamOfData
                .skipUntilOther(startCommand)
                .takeUntilOther(stopCommand)
                .subscribe(System.out::println);

        Thread.sleep(4000);
    }

    @Test
    public void collectExample() {
        Flux.just(1, 6, 2, 8, 3, 1, 5, 1)
                .collectSortedList(Comparator.reverseOrder())
                .subscribe(System.out::println);
    }

    @Test
    public void distinctExample() {
        Flux.just(1, 2, 2, 3, 3, 3, 1, 1, 4)
                .distinct()
                .subscribe(System.out::println);

        System.out.println();

        Flux.just(1, 2, 2, 3, 3, 3, 1, 1, 4)
                .distinctUntilChanged()
                .subscribe(System.out::println);
    }

    @Test
    public void anyExample() {
        Flux.just(3, 5, 7, 9, 11, 15, 16, 17)
                .count()
                .subscribe(System.out::println);

        Flux.just(3, 5, 7, 9, 11, 15, 16, 17)
                .any(e -> e % 2 == 0)
                .subscribe(System.out::println);

        Flux.just(3, 5, 7, 9, 11, 15, 16, 17)
                .all(e -> e % 2 == 0)
                .subscribe(System.out::println);

        Flux.just(3, 5, 7, 9, 11, 15, 16, 17)
                .hasElement(7)
                .subscribe(System.out::println);

        Flux.just(3, 5, 7, 9, 11, 15, 16, 17)
                .hasElements()
                .subscribe(System.out::println);
    }

    @Test
    public void sortExample() {
        Flux.just(1, 4, 2, 3)
                .sort()
                .subscribe(System.out::println);

        Flux.just(1, 4, 2, 3)
                .sort(Comparator.reverseOrder())
                .subscribe(System.out::println);
    }

    @Test
    public void reduceExample() {
        Flux.range(1, 5)
                .reduce(0, Integer::sum)
                .subscribe(System.out::println);
    }

    @Test
    public void scanExample() {
        Flux.range(1, 5)
                .scan(0, Integer::sum)
                .subscribe(System.out::println);
    }

    @Test
    public void thenExample() {
        Flux.just(1, 2, 3)
                .thenMany(Flux.just(4, 5))
                .subscribe(System.out::println);
    }

    @Test
    public void concatExample() {
        Flux.concat(
                Flux.range(1, 3),
                Flux.range(4, 2),
                Flux.range(6, 5)
        ).subscribe(System.out::println);
    }

    @Test
    public void mergeExample() {
        Flux.merge(
                Flux.range(1, 3),
                Flux.range(4, 2),
                Flux.range(6, 5)
        ).subscribe(System.out::println);
    }

    @Test
    public void zipExample() {
        Flux.zip(
                Flux.range(1, 3),
                Flux.range(4, 2),
                Flux.range(6, 5)
        ).subscribe(System.out::println);
    }

    @Test
    public void bufferExample() {
        Flux.range(1, 13)
                .buffer(4)
                .subscribe(System.out::println);
    }

    @Test
    public void windowExample() {
        Flux<Flux<Integer>> fluxFlux = Flux.range(101, 20)
                .windowUntil(this::isPrime, true);

        fluxFlux.subscribe(window -> window
                .collectList()
                .subscribe(System.out::println));
    }

    private boolean isPrime(int number) {
        return number > 2
                && IntStream.rangeClosed(2, (int) Math.sqrt(number))
                .noneMatch(n -> (number % n == 0));
    }

    @Test
    public void groupByExample() {
        Flux.range(1, 7)
                .groupBy(e -> e % 2 == 0 ? "Even" : "Odd")
                .subscribe(groupFlux -> groupFlux
                        .scan(
                                new LinkedList<>(),
                                (list, elem) -> {
                                    if (list.size() > 1) {
                                        list.remove(0);
                                    }
                                    list.add(elem);
                                    return list;
                                })
                        .filter(arr -> !arr.isEmpty())
                        .subscribe(data ->
                                System.out.println(groupFlux.key() + ": " + data)));
    }

    @Test
    public void flatMapExample() throws InterruptedException {
        Flux.just("user-1", "user-2", "user-3")
                .flatMap(u -> requestBooks(u)
                        .map(b -> u + "/" + b))
                .subscribe(System.out::println);

        Thread.sleep(1000);
    }

    private Flux<String> requestBooks(String user) {
        return Flux.range(1, random.nextInt(3) + 1)
                .delayElements(Duration.ofMillis(3))
                .map(i -> "book-" + i);
    }

    @Test
    public void sampleExample() throws InterruptedException {
        Flux.range(1, 100)
                .delayElements(Duration.ofMillis(1))
                .sample(Duration.ofMillis(20))
                .subscribe(System.out::println);

        Thread.sleep(1000);
    }

    @Test
    public void doOnExample() {
        Flux.just(1, 2, 3)
                .concatWith(Flux.error(new RuntimeException("Conn error")))
                .doOnEach(System.out::println)
                .subscribe();
    }

    @Test
    public void doOnExample2() {
        Mono.error(new RuntimeException("test exception"))
                .doOnSuccess(signal -> System.out.println("success"))
                .doOnError(cause -> System.out.println("error: " + cause.getMessage()))
                .subscribe();
    }

    @Test
    public void signalProcessing() {
        Flux.range(1, 3)
                .doOnNext(e -> System.out.println("data  : " + e))
                .materialize()
                .doOnNext(e -> System.out.println("signal: " + e))
                .dematerialize()
                .collectList()
                .subscribe(r -> System.out.println("result: " + r));
    }

    @Test
    public void usingPushOperator() throws InterruptedException {
        Flux.push(emitter -> IntStream
                .range(2000, 100000)
                .forEach(emitter::next))
                .delayElements(Duration.ofMillis(1))
                .subscribe(System.out::println);

        Thread.sleep(1000);
    }

    @Test
    public void usingCreateOperator() throws InterruptedException {
        Flux.create(emitter -> {
            emitter.onDispose(() -> System.out.println("Disposed"));
            // push events to emitter
        }).subscribe(System.out::println);

        Thread.sleep(1000);
    }

    @Test
    public void usingGenerate() throws InterruptedException {
        Flux.generate(
                () -> Tuples.of(0L, 1L),
                (state, sink) -> {
                    System.out.println("generated value: " + state.getT2());
                    sink.next(state.getT2());
                    long newValue = state.getT1() + state.getT2();
                    return Tuples.of(state.getT2(), newValue);
                })
                .take(7)
                .subscribe(System.out::println);

        Thread.sleep(100);
    }

    @Test
    public void tryWithResources() {
        try (Connection conn = Connection.newConnection()) {
            conn.getData().forEach(
                    data -> System.out.println("Received data: " + data)
            );
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Test
    public void usingOperator() {
        Flux<String> ioRequestResults = Flux.using(
                Connection::newConnection,
                connection -> Flux.fromIterable(connection.getData()),
                Connection::close
        );

        ioRequestResults
                .subscribe(
                        data -> System.out.println("Received data: " + data),
                        e -> System.out.println("Error: " + e.getMessage()),
                        () -> System.out.println("Stream finished"));
    }


    @Test
    public void coldPublisher() {
        Flux<String> coldPublisher = Flux.defer(() -> {
            System.out.println("Generating new items");
            return Flux.just(UUID.randomUUID().toString());
        });

        System.out.println("No data was generated so far");
        coldPublisher.subscribe(e -> System.out.println("onNext: " + e));
        coldPublisher.subscribe(e -> System.out.println("onNext: " + e));
        System.out.println("Data was generated twice for two subscribers");
    }

    @Test
    public void connectExample() {
        Flux<Integer> source = Flux.range(0, 3)
                .doOnSubscribe(s ->
                        System.out.println("new subscription for the cold publisher"));

        ConnectableFlux<Integer> conn = source.publish();

        conn.subscribe(e -> System.out.println("[Subscriber 1] onNext: " + e));
        conn.subscribe(e -> System.out.println("[Subscriber 2] onNext: " + e));

        System.out.println("all subscribers are ready, connecting");
        conn.connect();
    }

    @Test
    public void cachingExample() throws InterruptedException {
        Flux<Integer> source = Flux.range(0, 2)
                .doOnSubscribe(s ->
                        System.out.println("new subscription for the cold publisher"));

        Flux<Integer> cachedSource = source.cache(Duration.ofSeconds(1));

        cachedSource.subscribe(e -> System.out.println("[S 1] onNext: " + e));
        cachedSource.subscribe(e -> System.out.println("[S 2] onNext: " + e));

        Thread.sleep(1200);

        cachedSource.subscribe(e -> System.out.println("[S 3] onNext: " + e));
    }

    @Test
    public void shareExample() throws InterruptedException {
        Flux<Integer> source = Flux.range(0, 5)
                .delayElements(Duration.ofMillis(100))
                .doOnSubscribe(s ->
                        System.out.println("new subscription for the cold publisher"));

        Flux<Integer> sharedSource = source.share();

        sharedSource.subscribe(e -> System.out.println("[S 1] onNext: " + e));
        Thread.sleep(400);
        sharedSource.subscribe(e -> System.out.println("[S 2] onNext: " + e));

        Thread.sleep(1000);
    }

    @Test
    public void elapsedExample() throws InterruptedException {
        Flux.range(0, 5)
                .delayElements(Duration.ofMillis(100))
                .elapsed()
                .subscribe(e -> System.out.println(
                        String.format("Elapsed %d ms: %d", e.getT1(), e.getT2())));

        Thread.sleep(1000);
    }

    @Test
    public void transformExample() {
        Function<Flux<String>, Flux<String>> logUserInfo =
                stream -> stream
                        .index()
                        .doOnNext(tp ->
                                System.out.println(
                                        String.format("[%d] User: %s", tp.getT1(), tp.getT2())))
                        .map(Tuple2::getT2);

        Flux.range(1000, 3)
                .map(i -> "user-" + i)
                .transform(logUserInfo)
                .subscribe(e -> System.out.println("onNext: " + e));
    }


    static class Connection implements AutoCloseable {
        private final Random rnd = new Random();

        static Connection newConnection() {
            System.out.println("IO Connection created");
            return new Connection();
        }

        public Iterable<String> getData() {
            if (rnd.nextInt(10) < 3) {
                throw new RuntimeException("Communication error");
            }
            return Arrays.asList("Some", "data");
        }

        @Override
        public void close() {
            System.out.println("IO Connection closed");
        }
    }


    @Test
    public void concatMapExample() {
        List<User> users = new ArrayList<>();
        users.add(new User(null, "lily"));
        users.add(new User("2", "lucy"));
        Flux.fromIterable(users)
                .concatMap(user -> {
                    if (user.id == null) {
                        return Mono.empty();
                    } else {
                        return Mono.just(user.id);
                    }
                })
                .next()
                .flatMap(Mono::just)
                .subscribe(System.out::println);
    }

    @AllArgsConstructor
    @NoArgsConstructor
    static class User {
        public String id, name;
    }
}
