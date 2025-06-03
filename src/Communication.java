import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Communication implements Serializable {

    private ServerSocket serverSocket;

    public void start(int port, Storage storage) {
        while (true) {
            try {
                serverSocket = new ServerSocket(port);
                storage.setPort(port);
                System.out.println("* SERVER STARTED AT: [host=localhost, port=" + serverSocket.getLocalPort() + "] *");
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void listen(Storage storage, Topology topology) throws Exception {

        while (true) {

            System.out.println("* LISTENING FOR CONNECTIONS... *");
            Socket socket = serverSocket.accept();


            new Thread(() -> {
                while (true) {
                    try {

                        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

                        Message message = (Message) in.readObject();

                        if (message.getType() == Message.MessageType.REQUEST) {

                            System.out.println("* ACCEPTED CLIENT REQUEST [command=" + message.getCommand() +"] *");

                            ClientRequest<?> request = (ClientRequest<?>) message;
                            Object result = request.handle(storage);

                            if (result == null) {
                                broadcastTopology(new NodeVisitedAdd(new Topology.Node("localhost", serverSocket.getLocalPort())),
                                        topology);
                                var value = askForValue(topology, request);

                                System.out.println("- returning request to the client: [value=" + result + "]");
                                out.writeObject(Objects.requireNonNullElse(value, "ERROR"));
                                topology.clearVisitedNodes();

                            } else {
                                out.writeObject(result);
                            }

                        } else if (message.getType() == Message.MessageType.BROADCAST) {

                            System.out.println("* ACCEPTED BROADCAST REQUEST: [command=" + message.getCommand() +"] *");
                            Broadcast<?> broadcast = (Broadcast<?>) message;
                            broadcast.handle(topology);

                        } else if (message.getType() == Message.MessageType.GET_MIN_MAX) {

                            System.out.println("* ACCEPTED CLIENT REQUEST: [command=" + message.getCommand() +"] *");
                            Integer values = storage.getStoredValue();

                            ClientRequest<?> request = (ClientRequest<?>) message;
                            Object result = request.handle(values);

                            broadcastTopology(new NodeVisitedAdd(new Topology.Node("localhost", serverSocket.getLocalPort())),
                                    topology);
                            var value = askForValue(topology, request);
                            if (value == null) {
                                topology.clearVisitedNodes();
                                System.out.println("- returning request to the client: [value=" + result + "]");
                                out.writeObject(result);
                            }

                            System.out.println("- returning request to the client: [value=" + result + "]");
                            topology.clearVisitedNodes();
                            out.writeObject(Objects.requireNonNullElse(value, "ERROR"));

                        } else if (message.getType() == Message.MessageType.DISCONNECT) {
                            System.out.println("* ACCEPTED BROADCAST REQUEST: [command=" + message.getCommand() +"] *");
                            Broadcast<?> broadcast = (Broadcast<?>) message;
                            broadcast.handle(topology);

                        } else if (message.getType() == Message.MessageType.TERMINATE) {

                            System.out.println("* ACCEPTED CLIENT REQUEST: [command=" + message.getCommand() +"] *");
                            ClientRequest<?> request = (ClientRequest<?>) message;
                            var result = request.handle(storage);

                            broadcastTopology(new removeDisconnectedNode(new Topology.Node("localhost", serverSocket.getLocalPort())), topology);
                            out.writeObject(result);
                            out.close();
                            in.close();
                            serverSocket.close();
                        }

                    } catch (Exception e1) {
                        break;
                    }

                }
            }).start();
        }
    }

    public <R> R askForValue(Topology topology, ClientRequest<?> request) throws Exception {

        var listOfNodes = topology.getTopology().get(serverSocket.getLocalPort());

        for (Topology.Node current : listOfNodes) {
            if (!topology.getVisitedNodes().contains(current)) {
                return (R) execute(request, "localhost", current.getPort());
            }
        }
        return null;
    }

    public <R> R execute(ClientRequest<R> request, String gateway, int port) throws Exception {
        Socket socket = new Socket(gateway, port);
        System.out.println("* CONNECTED WITH: [host=localhost, port=" + port + "] *");

        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

        out.writeObject(request);

        var x = (R)in.readObject();

        socket.close();
        out.close();
        in.close();

        return x;
    }

    public <R> void broadcastTopology(Broadcast<R> request, Topology topology) throws IOException {

        var listOfNodes = topology.getTopology().get(serverSocket.getLocalPort());

        for (Topology.Node current : listOfNodes) {
            var dstAd = current.getAddress();
            var dstPort = current.getPort();
            Socket socket;
            try {
                socket = new Socket(dstAd, dstPort);
                System.out.println("- sending broadcast to: [host=localhost, port=" + dstPort + "], [command=" +request.getCommand() + "]");
            } catch (IOException e) {
                throw new RuntimeException("\nCannot connect to:" + dstPort);
            }

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(request);

            socket.close();
            out.close();
            in.close();
        }
    }
}
