public class NodeVisitedAdd implements Broadcast<Void> {


    Topology.Node nodeToAdd;

    public NodeVisitedAdd(Topology.Node nodeToAdd) {
        this.nodeToAdd = nodeToAdd;
    }

    @Override
    public Void handle(Topology topology) {
        topology.addVisitedNode(nodeToAdd);
        topology.listVisitedNodes();
        return null;
    }



    @Override
    public MessageType getType() {
        return Broadcast.super.getType();
    }

    @Override
    public String getCommand() {
        return "add-visited-node";
    }

}
