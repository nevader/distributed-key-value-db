import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Topology {

    private int hostPort;
    private final Map<Integer, HashSet<Node>> topology = new ConcurrentHashMap<>();
    private final HashSet<Node> visitedNodes = new HashSet<>();

    public void join(HashSet<Node> connectedNodes) {
            topology.put(hostPort, connectedNodes);
    }
    public void addVisitedNode(Node nodeToAdd) {
        System.out.println("- adding new entry to list of already visited nodes: [host=localhost, port=" + nodeToAdd.getPort() + "]");
        visitedNodes.add(nodeToAdd);
    }
    public void clearVisitedNodes() {
        System.out.println("- clearing list of already visited nodes");
        visitedNodes.clear();
    }

    public void addNode(Node nodeToAdd) {
        System.out.println("- establishing new connection with: [host=localhost, port=" + nodeToAdd.getPort() + "]");
        Set<Node> nodeList = topology.get(hostPort);
        nodeList.add(nodeToAdd);
    }

    public void removeNode(Node nodeToRemove) {
        Set<Node> nodeList = topology.get(hostPort);
        nodeList.remove(nodeToRemove);
    }

    public void listCurrentNodes() {
        Set<Node> nodeList = topology.get(hostPort);
        System.out.println("- list of currently connected nodes...");
        for (Node current : nodeList) {
            System.out.println("[host=localhost, port=" + current.getPort() + "]");
        }
    }
    public void listVisitedNodes() {
        System.out.println("- list of visited nodes...");
        for (Node current : visitedNodes) {
            System.out.println("[host=localhost, port=" + current.getPort() + "]");
        }
    }

    public HashSet<Node> getVisitedNodes() {
        return visitedNodes;
    }

    public void setHostPort(int hostPort) {
        this.hostPort = hostPort;
    }

    public Map<Integer, HashSet<Node>> getTopology() {
        return topology;
    }


    public static class Node implements Serializable {
        private final int port;
        private final String address;

        public Node(String address, int port) {
            this.port = port;
            this.address = address;
        }

        public int getPort() {
            return port;
        }

        public String getAddress() {
            return address;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Node node = (Node) o;

            if (port != node.port) return false;
            return Objects.equals(address, node.address);
        }

        @Override
        public int hashCode() {
            int result = port;
            result = 31 * result + (address != null ? address.hashCode() : 0);
            return result;
        }
    }
}
