package org.chernatkin.routing;

import java.util.Arrays;
import java.util.List;

import org.chernatkin.routing.graph.Route;
import org.chernatkin.routing.model.Station;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class LargeGraphTest extends AbstractGraphTest {

    private static final int NODES_NUMBER = 100;
    
    @BeforeClass
    public static void beforeTest(){
        
        RouterBuilder builder = RouterBuilder.routerBuilder(NODES_NUMBER * NODES_NUMBER - 1);
        
        for(int i = 0; i < NODES_NUMBER; i++){
            for(int j = 0; j < NODES_NUMBER; j++){
                if(i == j){
                    continue;
                }
                builder.addEdge("S" + i, "S" + j, i + j);
            }
        }
        
        setRouter(builder.build());
    }
    
    @Test
    public void routeTest(){
        Route<Station> route = getRouter().loadRoute(station("S1"), station("S50"));
        
        Assert.assertEquals(51, route.getWeight());
        Assert.assertEquals(Arrays.asList(station("S1"), 
                                          station("S50")),
                            route.getStations());
    }
    
    @Test
    public void nearByTest(){
        List<Route<Station>> routes = getRouter().getNearBy(station("S0"), 3);
        
        Assert.assertEquals(Arrays.asList(route(1, "S0", "S1"),
                                          route(2, "S0", "S2"),
                                          route(3, "S0", "S3")),
                            routes);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void checkStationTest(){
        for(int i = 0; i < NODES_NUMBER; i++){
            Assert.assertEquals("S" + i, station("S" + i).getName());
        }
        station("S" + NODES_NUMBER);
    }
}
