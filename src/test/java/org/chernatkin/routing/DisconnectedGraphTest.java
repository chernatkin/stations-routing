package org.chernatkin.routing;

import java.util.Arrays;
import java.util.List;

import org.chernatkin.routing.model.Route;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class DisconnectedGraphTest extends AbstractGraphTest {
    
    @BeforeClass
    public static void beforeTest(){
        setRouter(RouterBuilder.routerBuilder(10)
                               .addEdge("A", "B", 240)
                               .addEdge("A", "C", 70)
                               .addEdge("A", "D", 120)
                               .addEdge("C", "B", 60)
                               .addEdge("D", "E", 480)
                               .addEdge("C", "E", 240)
                               .addEdge("B", "E", 210)
                               .addEdge("E", "A", 300)
                               
                               .addEdge("F", "G", 100)
                               .addEdge("G", "H", 200)
                               .addEdge("H", "F", 100)
                               .build());
    }

    @Test
    public void routeInFirstGroupTest(){
        Route route = getRouter().loadRoute(station("A"), station("B"));
        
        Assert.assertEquals(130, route.getWeight());
        Assert.assertEquals(Arrays.asList(station("A"), 
                                          station("C"), 
                                          station("B")), 
                            route.getStations());
    }
    
    @Test
    public void routeInSecondGroupTest(){
        Route route = getRouter().loadRoute(station("F"), station("H"));
        
        Assert.assertEquals(300, route.getWeight());
        Assert.assertEquals(Arrays.asList(station("F"), 
                                          station("G"), 
                                          station("H")), 
                            route.getStations());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void routeImmposibleTest(){
        getRouter().loadRoute(station("A"), station("F"));
    }
    
    @Test
    public void nearByInFirstGroupTest(){
        List<Route> routes = getRouter().getNearBy(station("A"), 130);
        
        Assert.assertEquals(Arrays.asList(route(70, "A","C"),
                                          route(120, "A", "D"),
                                          route(130, "A", "C", "B")),
                            routes);
    }

    @Test
    public void nearByInSecondGroupTest(){
        Router router = getRouter();
        List<Route> routes = router.getNearBy(station("F"), 500);
        
        Assert.assertEquals(Arrays.asList(route(100, "F","G"),
                                          route(300, "F", "G", "H")),
                            routes);
    }
}
