package org.chernatkin.routing;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.chernatkin.routing.graph.Route;
import org.chernatkin.routing.model.Station;

public class AbstractGraphTest {

    private static Router testRouter;
    
    protected static Route<Station> route(int weight, String... names){
        return new Route<Station>(weight, Stream.of(names)
                                                .map(n -> station(n))
                                                .collect(Collectors.toList()));
    }
    
    protected static Station station(String name){
        return testRouter.checkStation(name);
    }

    protected static Router getRouter() {
        return testRouter;
    }

    protected static void setRouter(Router router) {
        testRouter = router;
    }
}
