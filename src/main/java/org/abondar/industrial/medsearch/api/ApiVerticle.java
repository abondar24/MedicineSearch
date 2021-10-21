package org.abondar.industrial.medsearch.api;


import io.reactivex.Completable;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import org.abondar.industrial.medsearch.service.JsonUtil;
import org.abondar.industrial.medsearch.service.SearchService;
import org.abondar.industrial.medsearch.service.SearchServiceImpl;

import static org.abondar.industrial.medsearch.api.ApiUtil.API_ROOT;
import static org.abondar.industrial.medsearch.api.ApiUtil.SEARCH_ENDPOINT;
import static org.abondar.industrial.medsearch.api.ApiUtil.SERVER_PORT;
import static org.abondar.industrial.medsearch.api.ApiUtil.TERM_PARAM;
import static org.abondar.industrial.medsearch.service.JsonUtil.EMPTY_RESULT;

public class ApiVerticle extends AbstractVerticle {

    private SearchService service;

    public ApiVerticle(SearchService service) {
        this.service = service;
    }

    public ApiVerticle(){
        this.service = new SearchServiceImpl(JsonUtil.DATASET_FILE);
    }

    @Override
    public Completable rxStart(){

        var router = Router.router(vertx);
        router.get(API_ROOT+SEARCH_ENDPOINT+"/:"+TERM_PARAM)
                .handler(this::searchHandler);

        return vertx.createHttpServer()
                .requestHandler(router)
                .rxListen(SERVER_PORT)
                .ignoreElement();
    }


    private void searchHandler(RoutingContext rc){
        var term = rc.pathParam(TERM_PARAM);


        service.searchByName(term)
                .subscribe(
                nameRes-> sendResult(rc,nameRes),
                err-> sendServerError(rc),
                        ()->service.searchByDisease(term).subscribe(
                                disRes->sendResult(rc,disRes),
                                err->sendServerError(rc),
                                ()->sendNotFound(rc)
                        )
        );

//        if (nameRes.equals(EMPTY_RESULT)){
//            var disRes = service.searchByDisease(term);
//            if (disRes.equals(EMPTY_RESULT)){
//                sendNotFound(rc);
//            } else {
//                sendResult(rc,disRes);
//            }
//        } else {
//
//        }
    }

    private void sendResult(RoutingContext ctx,String result){
        ctx.response()
                .setStatusCode(200)
                .send(result);
    }

    private void sendNotFound(RoutingContext ctx){
        ctx.response()
                .setStatusCode(404)
                .end();
    }

    private void sendServerError(RoutingContext ctx){
        ctx.response()
                .setStatusCode(500)
                .end();
    }

}
