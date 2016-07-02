package org.chernatkin.routing;

import java.util.ArrayList;
import java.util.List;

import org.chernatkin.routing.graph.Route;
import org.chernatkin.routing.model.Station;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class RoundLargeGraphTest extends AbstractGraphTest {

    private static final int NODES_NUMBER = 100;
    
    @BeforeClass
    public static void beforeTest(){
        
        RouterBuilder builder = RouterBuilder.routerBuilder(NODES_NUMBER - 1);
        
        for(int i = 1; i < NODES_NUMBER; i++){
            builder.addEdge("S" + i, "S" + (i + 1), i);
        }
        builder.addEdge("S" + NODES_NUMBER, "S1", NODES_NUMBER);
        
        setRouter(builder.build());
    }
    
    @Test
    public void routeTest(){
        
        Route<Station> route = getRouter().loadRoute(station("S1"), station("S" + NODES_NUMBER));
        
        Assert.assertEquals(NODES_NUMBER * (NODES_NUMBER - 1) / 2, route.getWeight());
        
        List<Station> expectedRoute = new ArrayList<>(NODES_NUMBER);
        for(int i = 1; i <= NODES_NUMBER; i++){
            expectedRoute.add(station("S" + i));
        }
        Assert.assertEquals(expectedRoute, route.getStations());
    }
    
}
