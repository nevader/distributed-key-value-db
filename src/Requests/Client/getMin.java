import java.util.ArrayList;

public class getMin implements ClientRequest<Integer> {

    Integer currentMax;

    @Override
    public Integer handle(Storage storage) {
        return null;
    }

    @Override
    public Integer handle(Integer value) {
        if (currentMax == null) {
            currentMax = value;
        }

        if (value < currentMax) {
            currentMax = value;
        }

        return currentMax;
    }


    @Override
    public MessageType getType() {
        return MessageType.GET_MIN_MAX;
    }

    @Override
    public String getCommand() {
        return "get-min";
    }
}