import java.net.InetAddress;

public class ClientID {
    String nickname;
    String address;
    int port;


    public ClientID(String nickname, String address, int port) {
        this.nickname = nickname;
        this.address = address;
        this.port = port;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return "nickname: "+ getNickname() +
                "; address: " + getAddress() +
                "; port: " + getPort();
    }
}
