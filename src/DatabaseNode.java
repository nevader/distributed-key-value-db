import java.util.HashSet;

public class DatabaseNode {

    private final Communication comm = new Communication();
    private final Storage storage = new Storage();
    private final Topology topology = new Topology();

    public void start(int serverPort) throws Exception {
        comm.start(serverPort, storage);
        comm.broadcastTopology(new addConnectedNode(new Topology.Node("localhost", serverPort)), topology);
        comm.listen(storage, topology);
    }

    public static void main(String[] args) throws Exception {

        int serverPort = 0;
        String record = "";
        HashSet<Topology.Node> nodesToConnect = new HashSet<>();

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-tcpport":
                    serverPort = Integer.parseInt(args[i + 1]);
                    break;
                case "-connect":
                    String connect = args[i + 1];
                    String[] parts = connect.split(":");
                    Topology.Node newNode = new Topology.Node(parts[0], Integer.parseInt(parts[1]));
                    nodesToConnect.add(newNode);
                    break;
                case "-record":
                    record = args[i + 1];
                    break;
            }
        }

        DatabaseNode databaseNode = new DatabaseNode();

        String[] parts = record.split(":");
        databaseNode.storage.put(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        databaseNode.topology.setHostPort(serverPort);
        databaseNode.topology.join(nodesToConnect);
        databaseNode.start(serverPort);
    }
}
