package de.bischinger.cache;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.util.Random;

import static java.lang.String.valueOf;

@State(Scope.Thread)
public class VorwahlJmh {

    private VorwahlCache vorwahlCache;
    private Random random;

    @Setup(Level.Trial)
    public void init() throws IOException {

        vorwahlCache = new VorwahlCacheImpl("vorwahl.dat");

        random = new Random();
    }

    @TearDown(Level.Trial)
    public void close() throws Exception {
        vorwahlCache.close();
    }

    @Benchmark
    public void readSpeed() throws InterruptedException {
        vorwahlCache.get(valueOf(random.nextInt()));
    }

    public static void main(String[] args) throws RunnerException, IOException {
        Options options = new OptionsBuilder()
                .include(VorwahlJmh.class.getSimpleName()).forks(1).build();

        new Runner(options).run();
    }
}
