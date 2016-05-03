package de.bischinger.cache;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

import java.io.IOException;

import static java.util.Optional.ofNullable;

public class MainVerticle extends AbstractVerticle {

    private VorwahlCacheImpl vorwahlCache;

    @Override
    public void start() throws IOException {

        vorwahlCache = new VorwahlCacheImpl("vorwahl.dat");
        System.out.println(vorwahlCache.size());

        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        router.get("/api/vorwahl").handler(routingContext -> {
            HttpServerRequest request = routingContext.request();
            String phonenumber1 = request.getParam("phonenumber");
            routingContext.response().end(
                    ofNullable(vorwahlCache.get(phonenumber1)).map(CharSequence::toString).orElse(""));
        });

        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
    }

    @Override
    public void stop() throws Exception {
        vorwahlCache.close();
    }

    public static void main(String[] args) throws IOException {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(MainVerticle.class.getName());
    }
}