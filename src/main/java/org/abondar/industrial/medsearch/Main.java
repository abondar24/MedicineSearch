package org.abondar.industrial.medsearch;


import io.vertx.reactivex.core.Vertx;
import org.abondar.industrial.medsearch.api.ApiVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.abondar.industrial.medsearch.api.ApiUtil.SERVER_PORT;

public class Main {
  private static final Logger logger = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {
    System.setProperty("vertx.logger-delegate-factory-class-name", "io.vertx.core.logging.SLF4JLogDelegateFactory");
    var vertx = Vertx.vertx();

    vertx.rxDeployVerticle(new ApiVerticle())
            .subscribe(
                    ok -> logger.info("Server up on port {}",SERVER_PORT),
                    err -> logger.info("Error: ",err)
            );
  }
}
