import java.net.Socket;
import java.util.ArrayList;

public class findKey implements ClientRequest <String>{

    private final Integer key;

    public findKey(Integer key) {
        this.key = key;
    }

    @Override
    public String handle(Storage storage) {
        return storage.findKey(key);
    }


    @Override
    public String handle(Integer value) {
        return null;
    }


    @Override
    public MessageType getType() {
        return ClientRequest.super.getType();
    }

    @Override
    public String getCommand() {
        return "find-key";
    }
}
