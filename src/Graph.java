// jdh CS 3240A / CS 5240A Fall 2024

import java.util.*;

import static java.lang.Integer.MAX_VALUE;

public class Graph {
    List<Node> nodes;

    //-------------------------------------------------

    public Graph() {
        this.nodes = new ArrayList<Node>();
    }

    //-------------------------------------------------

    public void addNode(Node newNode) {
        for (Node n: this.nodes) {
            if (n == newNode) {
                System.out.println("ERROR: graph already has a node " + n.name);
                assert false;
            }
        }
        nodes.add(newNode);
    }

    //-------------------------------------------------

    public void addEdge(Node n1, Node n2, int weight) {
        // make sure edge does not already exist
        int idx1 = this.nodes.indexOf(n1);
        if (idx1 >= 0) {
            for (Link link: this.nodes.get(idx1).adjlist) {
                if (link.n2 == n2) {
                    System.out.println("ERROR: there is already an edge from " + n1.name + " to " + n2.name);
                    return;
                }
            }
            this.nodes.get(idx1).addEdge(n2, weight);
        } else {
            System.out.println("ERROR: node " + n1.name + " not found in graph");
        }

        int idx2 = this.nodes.indexOf(n2);
        if (idx2 >= 0) {
            this.nodes.get(idx2).addEdge(n1, weight);
        } else {
            System.out.println("ERROR: node " + n2.name + " not found in graph");
        }
    } // addEdge()

    //-------------------------------------------------

    public void print() {
        for (Node n1: this.nodes) {
            System.out.print(n1 + ":");
            for (Link link: n1.adjlist) {
                System.out.print(" " + link.n2.name + " (d=" + link.weight + ")");
            }
            System.out.print("|");
        }
        System.out.println();
    } // print()

    //----------------------------------------------------
    // change the key (priority) on a node: by deleting it
    // and reinserting it

    public static void updatePQnode(PriorityQueue<Node> pqueue, Node n) {
        pqueue.remove(n);
        pqueue.add(n);
    }

    //----------------------------------------------------

    public void shortestPath(Node s) {
        // implement this
        /*
        Dijkstra’s Algorithm (G, s)
        Initially, 𝑆 = {𝑠} and d[s] = 0
        Let 𝑆 be the set of explored nodes
        // for each 𝑢 ∈ 𝑆, we will compute and save a distance d[u]
        While 𝑆 ≠ 𝑉
            select the node 𝑣 ∉ 𝑆 having an edge with one end in 𝑆 for which
            the distance from 𝑠 to 𝑣 through nodes of 𝑆 is as small as possible
            add 𝑣 to 𝑆 and set d[v] to that distance
         EndWhile
         */
        Node fake_node = new Node("fake");
        ArrayList<Node> explored_nodes = new ArrayList<Node>(nodes.size());
        ArrayList<Node> unexplored_nodes = new ArrayList<Node>(nodes.size());
        for(Node n : nodes){
            n.distance = Integer.MAX_VALUE;
            unexplored_nodes.add(n);
        }
        explored_nodes.add(s);
        s.distance = 0;
        while(explored_nodes.size() != this.nodes.size()){
            Node u = explored_nodes.get(explored_nodes.size()-1);
            Link shortest_link = new Link(fake_node, MAX_VALUE);
            for(Link l : u.adjlist){
                Node v = l.n2;
                if(!explored_nodes.contains(v)){
                    if(v.distance > u.distance + l.weight){
                        v.distance = u.distance + l.weight;
                    }
                    if(l.weight < shortest_link.weight){
                        shortest_link = l;
                    }
                }
            }
            explored_nodes.add(shortest_link.n2);
        }

    } // shortestPath()

    //----------------------------------------------------

    public void shortestPathPQ(Node s) {
        // implement this
        PriorityQueue<Node> PQ = new PriorityQueue<Node>(nodes.size());
        ArrayList<Node> explored_nodes = new ArrayList<Node>(nodes.size());
        for(Node n : this.nodes) {
            n.distance = Integer.MAX_VALUE;
        }
        s.distance = 0;
        for(Node n : this.nodes) {
            PQ.add(n);
        }
        explored_nodes.add(s);
        while(!(explored_nodes.size() == nodes.size())) {
            Node v = PQ.poll();
            for(Link l : v.adjlist){
                Node w = l.n2;
                int d = v.distance + l.weight;
                if(d < w.distance){
                    w.distance = d;
                    updatePQnode(PQ, w);
                }
            }
            explored_nodes.add(v);
        }
    } // shortestPathPQ()

} // class Graph
