public class terminate implements ClientRequest<String> {

    @Override
    public String handle(Storage storage) {
        return "OK";
    }

    @Override
    public String handle(Integer value) {
        return null;
    }

    @Override
    public MessageType getType() {
        return MessageType.TERMINATE;
    }

    @Override
    public String getCommand() {
        return "terminate";
    }
}
