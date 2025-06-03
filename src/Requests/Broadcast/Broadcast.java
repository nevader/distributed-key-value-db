public interface Broadcast<R> extends Message {
    R handle(Topology topology);
    @Override
    default MessageType getType() {
        return MessageType.BROADCAST;
    }
}