import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public interface ClientRequest<R> extends Message {


    R handle(Storage storage);
    R handle(Integer value);

    @Override
    default MessageType getType() {
        return MessageType.REQUEST;
    }
}