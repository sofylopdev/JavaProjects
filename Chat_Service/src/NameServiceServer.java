

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import static helperClasses.ConstantsCodes.*;

public class NameServiceServer extends Thread {

    private DatagramSocket socket;

    private byte[] buffer;
    private DatagramPacket packet;
    private InetAddress address;
    private int port;

    private List<ClientID> listOfClients = new ArrayList<ClientID>();
    private List<String> onlyNicknames = new ArrayList<>();

    private boolean receivedResponse = false;
    private InetAddress addressOfSender;
    private int portOfSender;

    private NameServiceServer(String name) {
        super(name);
    }

    public static void main(String[] args) {
        NameServiceServer server = new NameServiceServer("NameServiceServer");
        server.run();
    }

    public void run() {
        try {

            socket = new DatagramSocket(5000);

        } catch (SocketException e) {
            System.out.println("SocketException: " + e.getMessage());
        }

        while (true) {
            try {

                buffer = new byte[1024];
                packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                address = packet.getAddress();
                port = packet.getPort();

                String receivedString = (new String(buffer, 0, packet.getLength())).trim();
                String receivedCode = receivedString.split(":")[0];
                System.out.println("RECEIVED STRING: " + receivedString);

                switch(receivedCode) {
                    case GET_CONNECTIONS:
                        checkForAvailableConnections();
                        break;
                    case BIND:
                        bindNickname();
                        break;
                    case SENDTO:
                        sendMessage(receivedString);
                        break;
                    case ALIVE_STRING:
                        clientIsAlive();
                        break;
                    case LEAVING_CHAT:
                        //getting the nickname:
                        String nicknameOfThePersonLeavingTheChat = receivedString.split(":")[1];
                        //removing name from the list:
                        unbindClient(nicknameOfThePersonLeavingTheChat);
                        //update list of available clients:
                        checkForAvailableConnections();
                        break;
                }
            } catch (Exception except) {
                System.out.println(except.getMessage());
                for (StackTraceElement each : except.getStackTrace())
                    System.out.println(each);
            }
        }
    }

    private void checkForAvailableConnections() throws IOException {
        StringBuilder sb = new StringBuilder();

        for (ClientID eachClient : listOfClients) {
            int currentClientPort = eachClient.getPort();

            if (onlyNicknames.size() > 1) {
                for (ClientID each : listOfClients) {
                    if (each.getPort() != currentClientPort) {
                        sb.append(each.getNickname());
                        sb.append(";");
                    }
                }

                String builtString = CONNECTIONS_STRING + ":" + sb.toString();
                byte[] bufferConnectionsAvailable = builtString.getBytes();
                packet = new DatagramPacket(bufferConnectionsAvailable,
                        bufferConnectionsAvailable.length,
                        InetAddress.getByName(eachClient.getAddress()),
                        eachClient.getPort());
                socket.send(packet);
                sb = new StringBuilder();

            } else {
                String noConnections = CONNECTIONS_STRING + ":" + NO_CONNECTIONS;
                byte[] bufferNoConnect = noConnections.getBytes();
                packet = new DatagramPacket(bufferNoConnect,
                        bufferNoConnect.length,
                        InetAddress.getByName(eachClient.getAddress()),
                        eachClient.getPort());
                socket.send(packet);
                sb = new StringBuilder();
            }
        }
    }

    private void bindNickname() throws IOException {

        String nicknameString = new String(buffer, 0, buffer.length);
        String nickname = nicknameString.split(BIND + ":")[1].trim();


        System.out.println("Nickname: "+ nickname+", "+nickname.length());
        //if list doesn't have nickname
        if (!onlyNicknames.contains(nickname) && !nickname.toLowerCase().equals("me") && !nickname.toLowerCase().equals("me: ")) {
            //add to the lists
            listOfClients.add(
                    new ClientID(nickname,
                            address.toString().split("/")[1],
                            port));
            onlyNicknames.add(nickname);

            //and send message saying that the nickname is available
            String nicknameOk = NICKNAME_AVAILABLE_STRING + ":";
            byte[] bufferNicknameOk = nicknameOk.getBytes();
            packet = new DatagramPacket(bufferNicknameOk,
                    bufferNicknameOk.length,
                    address,
                    port);
            socket.send(packet);
            //After adding the name, sending available connections
            checkForAvailableConnections();

        //if list contains the name
        } else {
            //send message that the nickname is not available
            String nicknameTaken = NICKNAME_NOT_AVAILABLE_STRING + ":";
            byte[] bufferNicknameTaken = nicknameTaken.getBytes();
            packet = new DatagramPacket(bufferNicknameTaken,
                    bufferNicknameTaken.length,
                    address,
                    port);
            socket.send(packet);
        }
    }

    private void sendMessage(String receivedString) throws IOException {
        //parse the received string to get the message receiver
        String sendTo = receivedString.split(SENDTO + ":")[1].trim();

        //if that name is available in the list
        if (onlyNicknames.contains(sendTo)) {
        //in here we set these values because the value of 'address' and 'port' will change if the 'alive' client responds
            addressOfSender = address;
            portOfSender = port;
            for (ClientID each : listOfClients) {
                if (each.getNickname().equals(sendTo)) {
                    //getting the address and port of the message receiver
                    InetAddress addressOfReceiver = InetAddress.getByName(each.getAddress());
                    int portOfReceiver = each.getPort();
                    //checking if the receiver is alive or if he disconnected
                    String areYouAlive = ALIVE_STRING + ":";
                    byte[] bufferAreUALIVE = areYouAlive.getBytes();
                    packet = new DatagramPacket(bufferAreUALIVE,
                            bufferAreUALIVE.length,
                            addressOfReceiver,
                            portOfReceiver);
                    socket.send(packet);
                    System.out.println("Checking if receiver is alive");
                    //wait a bit for the response, if we don't get any answer, remove from the list and update data
                    WaitingResponseFromReceiver threadToWaitResponse = new WaitingResponseFromReceiver(sendTo);
                    threadToWaitResponse.start();
                    break;
                }
            }
         //this is called when the user selects the "No connections available"
        } else {
            String unavailableNickname = MESSAGE_STRING + ":" + "That nickname is unavailable to send messages.";
            byte[] bufferNoConnect = unavailableNickname.getBytes();
            packet = new DatagramPacket(bufferNoConnect,
                    bufferNoConnect.length,
                    address,
                    port);
            socket.send(packet);
            //updating available connections for the user:
            checkForAvailableConnections();
        }
    }

    private void clientIsAlive() throws IOException {
        receivedResponse = true;
     //the positive response came from the msg receiver, hence we can use the address and port received in this packet
        String infoOfReceiver = ADDRESS_STRING + ": " + address + PORT_STRING + port;
        byte[] bufferSendInfo = infoOfReceiver.getBytes();
        //sending the data to the message sender
        packet = new DatagramPacket(bufferSendInfo,
                bufferSendInfo.length,
                addressOfSender,
                portOfSender);
        socket.send(packet);
    }

    private void unbindClient(String nicknameToRemove) {
        //removing nickname from both lists
        onlyNicknames.remove(nicknameToRemove);

        for (ClientID each : listOfClients) {
            if (each.getNickname().equals(nicknameToRemove)) {
                listOfClients.remove(each);
                break;
            }
        }
    }


    public class WaitingResponseFromReceiver extends Thread{

        String receiverNickname;

        private WaitingResponseFromReceiver(String nickname) {
            receiverNickname = nickname;
        }

        public void run() {
            try {
                //waiting 5 secs for a response from receiver'
                Thread.sleep(3000);
                //if he didn't respond after that time:
                if (!receivedResponse) {
                    System.out.println("Removing user:" + receiverNickname);
                    //remove user
                    unbindClient(receiverNickname);

                   // System.out.println("received response is: " + receivedResponse + "; receiver nickname to be deleted: " + receiverNickname);

                    String receiverDead = MESSAGE_STRING + ":" + "ClientClass '" + receiverNickname + "' is disconnected."
                            + "\n" + "Check again for available connections.";
                    byte[] bufferReceiverDead = receiverDead.getBytes();

                    //send message to all the other users, warning that this user is no longer available
                    for (ClientID eachClient : listOfClients) {
                        DatagramPacket packet = new DatagramPacket(bufferReceiverDead,
                                bufferReceiverDead.length,
                                InetAddress.getByName(eachClient.getAddress()),
                                eachClient.getPort());
                        socket.send(packet);
                        //updating all the available connections automatically for each user
                        checkForAvailableConnections();
                    }
                } else {
                    //if after the 5 secd the response has arrived, then we need to change 'receivedResponse' to false
                    receivedResponse = false;
                }
            } catch (InterruptedException ie) {
                System.out.println("Sleep was interrupted: " + ie.getMessage());
            } catch (IOException ioe) {
                System.out.println("ClientClass disconnected message: Could't send packet to sender: " + ioe.getMessage());
            } finally {
                Thread.currentThread().interrupt();
                System.out.println("Thread interrupted");
            }
        }
    }
}
