package de.bischinger.cache;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Random;

import static java.lang.String.valueOf;

@State(Scope.Thread)
public class VorwahlJmh {

    private VorwahlCache vorwahlCache;
    private Random random;
    private WebTarget target;
    private Client client;

    @Setup(Level.Trial)
    public void init() throws IOException {

        vorwahlCache = new VorwahlCacheImpl("vorwahl.dat");

         client = ClientBuilder.newClient();
         target = client.target("http://127.0.0.1:8080/api/vorwahl");

        random = new Random();
    }

    @TearDown(Level.Trial)
    public void close() throws Exception {
        vorwahlCache.close();
        client.close();
    }

    @Benchmark
    public void readSpeed() throws InterruptedException {
        vorwahlCache.get(valueOf(random.nextInt()));
    }

    @Benchmark
    public void readRestJaxRs() throws InterruptedException, IOException {
        target.queryParam("phonenumber", random.nextInt()).request().get().readEntity(String.class);
    }

    @Benchmark
    public void readRest() throws InterruptedException, IOException {
        URL url = new URL("http://127.0.0.1:8080/api/vorwahl?phonenumber="+random.nextInt());

        try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()))) {
            String line;
            while ((line= br.readLine())!=null);
        }
    }

    public static void main(String[] args) throws RunnerException, IOException {
        Options options = new OptionsBuilder()
                .include(VorwahlJmh.class.getSimpleName()).forks(1).build();

        new Runner(options).run();
    }
}
