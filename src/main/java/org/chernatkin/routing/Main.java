package org.chernatkin.routing;

import java.io.Console;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.chernatkin.routing.model.Route;
import org.chernatkin.routing.model.Station;

public class Main {

    private static final Pattern EDGE_PATTERN = Pattern.compile("^([a-zA-Z0-9]+)\\s+\\->\\s+([a-zA-Z0-9]+):\\s+([0-9]+).*$");
    
    private static final String ROUTE_CMD = "route";
    
    private static final String NEARBY_CMD = "nearby";
    
    private static final String EXIT_CMD = "exit";
    
    
    public static void main(String[] args) {
        
        Console console = System.console();
        
        Router router = readEdges(console);
        while(true){
            processRouteRequests(console, router);
        }
    }
    
    private static Router readEdges(Console console) {
        
        final int edgesNumber = Integer.parseInt(console.readLine());
        
        final RouterBuilder builder = RouterBuilder.routerBuilder(edgesNumber);
        
        for(int readEdges = 0; readEdges < edgesNumber;){
            String edge = console.readLine();
            Matcher m = EDGE_PATTERN.matcher(edge);
            if(!m.matches()){
                console.printf("Invalid input edge:%s\n", edge);
                continue;
            }
            
            String stationFrom = m.group(1);
            String stationTo = m.group(2);
            int weight = Integer.parseInt(m.group(3));
            
            builder.addEdge(stationFrom, stationTo, weight);
            readEdges++;
        }
        
        return builder.build();
    }
    
    private static void processRouteRequests(Console console, Router router){
        String line = console.readLine().trim().replace(',', ' ');
        if(line == null || line.isEmpty()){
            return;
        }
        if(EXIT_CMD.equalsIgnoreCase(line)) {
            System.exit(0);
        }
        
        String[] cmds = line.split("\\s+");
        
        if(cmds.length < 3 || cmds.length > 4){
            console.printf("Invalid query:%s\n", line);
        }
        
        try{
            if(ROUTE_CMD.equalsIgnoreCase(cmds[0])) {
                executeRoute(console, router, cmds[1], cmds[3]);
            }
            else if(NEARBY_CMD.equalsIgnoreCase(cmds[0])) {
                executeNearBy(console, router, cmds[1], Integer.parseInt(cmds[2]));
            }
            else {
                throw new IllegalArgumentException("command is unknown");
            }
        } catch(IllegalArgumentException iae){
            console.printf(iae.getMessage() + "\n");
        }
    }
    
    private static void executeRoute(Console console, Router router, String from, String to){
        Station fromStation = router.checkStation(from);
        Station toStation = router.checkStation(to);
        
        Route route = router.loadRoute(fromStation, toStation);
        
        console.printf("%s: %s\n", route.getStations().stream()
                                                    .map(s -> s.getName())
                                                    .collect(Collectors.joining(" -> ")), 
                                 Integer.toString(route.getWeight()));
    }
    
    private static void executeNearBy(Console console, Router router, String from, int maxTime){
        Station fromStation = router.checkStation(from);
        List<Route> list = router.getNearBy(fromStation, maxTime);
        
        console.printf(list.stream()
                           .map(r -> {
                               List<Station> stations = r.getStations();
                               return stations.get(stations.size() - 1) + ": " + r.getWeight();
                           })
                           .collect(Collectors.joining(", ")) + "\n");
    }
}
