package com.routeoptimizer.demo;

import com.routeoptimizer.algorithm.BreadthFirstSearch;
import com.routeoptimizer.algorithm.DijkstraAlgorithm;
import com.routeoptimizer.graph.Graph;
import com.routeoptimizer.model.PathResult;

import java.util.List;

public class Demo {
    public static void main(String[] args) {
        Graph graph = new Graph();

        // Classic weighted example to validate Dijkstra's numbers by hand:
        // A -C(1)-> , A -B(4)->, C -B(2)->, B -D(5)->, C -D(8)->, D -E(3)->, C -E(10)->
        graph.addRoute("A", "B", 4);
        graph.addRoute("A", "C", 1);
        graph.addRoute("C", "B", 2);
        graph.addRoute("B", "D", 5);
        graph.addRoute("C", "D", 8);
        graph.addRoute("D", "E", 3);
        graph.addRoute("C", "E", 10);

        // A cycle to prove BFS doesn't loop forever: B -> F -> B
        graph.addRoute("B", "F", 1);
        graph.addRoute("F", "B", 1);

        // An unreachable island, disconnected from A's component
        graph.addCity("Z");

        System.out.println("=== Dijkstra: A -> D ===");
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm();
        PathResult result = dijkstra.findShortestPath(graph, "A", "D");
        System.out.println(result);
        System.out.println("Expected: A -> C -> B -> D, distance 8.0");

        System.out.println();
        System.out.println("=== Dijkstra: A -> Z (unreachable) ===");
        PathResult unreachable = dijkstra.findShortestPath(graph, "A", "Z");
        System.out.println(unreachable);

        System.out.println();
        System.out.println("=== BFS: all cities reachable from A ===");
        BreadthFirstSearch bfs = new BreadthFirstSearch();
        List<String> reachable = bfs.traverse(graph, "A");
        System.out.println(reachable);
        System.out.println("Expect: A,B,C,D,F,E in some BFS order, Z excluded");

        System.out.println();
        System.out.println("=== BFS: all cities reachable from Z (isolated) ===");
        List<String> fromZ = bfs.traverse(graph, "Z");
        System.out.println(fromZ);
        System.out.println("Expect: just [Z]");
    }
}