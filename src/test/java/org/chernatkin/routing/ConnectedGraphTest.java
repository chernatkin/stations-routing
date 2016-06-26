package org.chernatkin.routing;

import java.util.Arrays;
import java.util.List;

import org.chernatkin.routing.graph.Route;
import org.chernatkin.routing.model.Station;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConnectedGraphTest extends AbstractGraphTest {
    
    @BeforeClass
    public static void beforeTest(){
        setRouter(RouterBuilder.routerBuilder(8)
                               .addEdge("A", "B", 240)
                               .addEdge("A", "C", 70)
                               .addEdge("A", "D", 120)
                               .addEdge("C", "B", 60)
                               .addEdge("D", "E", 480)
                               .addEdge("C", "E", 240)
                               .addEdge("B", "E", 210)
                               .addEdge("E", "A", 300)
                               .build());
    }

    @Test
    public void routeTest(){
        Route<Station> route = getRouter().loadRoute(station("A"), station("B"));
        
        Assert.assertEquals(130, route.getWeight());
        Assert.assertEquals(Arrays.asList(station("A"), 
                                          station("C"), 
                                          station("B")), 
                            route.getStations());
    }
    
    @Test
    public void nearByTest(){
        List<Route<Station>> routes = getRouter().getNearBy(station("A"), 130);
        
        Assert.assertEquals(Arrays.asList(route(70, "A","C"),
                                          route(120, "A", "D"),
                                          route(130, "A", "C", "B")),
                            routes);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void checkStationTest(){
        station("F");
    }
}
