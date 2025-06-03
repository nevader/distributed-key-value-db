
public class removeDisconnectedNode implements Broadcast<Void>{

    Topology.Node nodeToRemove;

    public removeDisconnectedNode(Topology.Node nodeToRemove) {
        this.nodeToRemove = nodeToRemove;
    }

    @Override
    public Void handle(Topology topology) {
        topology.removeNode(nodeToRemove);
        System.out.println("- removing host from connected nodes list: [host=localhost, port=" + nodeToRemove.getPort() + "]");
        topology.listCurrentNodes();
        return null;
    }

    @Override
    public MessageType getType() {
        return MessageType.DISCONNECT;
    }

    @Override
    public String getCommand() {
        return "remove-disconnected-node";
    }
}
