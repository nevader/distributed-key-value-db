import java.net.ServerSocket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Storage {

    private final Map<Integer, Integer> data = new ConcurrentHashMap<>();
    private  Integer port;
    private int currentKey;

    public void setPort (Integer port) {
        this.port = port;
    }

    public void put(Integer key, Integer value) {
        data.clear();
        System.out.println("PUT [key=" + key +", value=" + value + "]");
        data.put(key, value);
        currentKey = key;
    }
    public String replaceValue(Integer key, Integer value) {
        if (data.containsKey(key)) {
            System.out.println("UPDATED VALUE: [from=" + data.get(key) + ", to=" + value + "]");
            data.put(key, value);
            return "OK";
        } else  {
            return null;
        }
    }
    public Integer getStoredValue() {
        return data.get(currentKey);
    }
    public String findKey(Integer key) {
        if (data.containsKey(key)) {
            return "localhost:" + port;
        } else {
            return null;

        }
    }

    public String getValueAsString(Integer key) {
        System.out.println("GET [key=" + key +", return=" + data.get(key) + "]");
        if (data.get(key) == null) {
            return null;
        } else {
            return key + ":" + data.get(key).toString();
        }
    }

}
