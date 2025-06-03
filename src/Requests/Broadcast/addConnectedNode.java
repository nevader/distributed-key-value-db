public class addConnectedNode implements Broadcast<Void>{

    Topology.Node nodeToAdd;

    public addConnectedNode(Topology.Node nodeToAdd) {
        this.nodeToAdd = nodeToAdd;
    }

    @Override
    public Void handle(Topology topology) {
        topology.addNode(nodeToAdd);
        topology.listCurrentNodes();
        return null;
    }



    @Override
    public MessageType getType() {
        return Broadcast.super.getType();
    }

    @Override
    public String getCommand() {
        return "add-connected-node";
    }
}
