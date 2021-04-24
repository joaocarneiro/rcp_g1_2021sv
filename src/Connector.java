import java.io.*;
import java.net.Socket;
import java.net.URL;

public class Connector {
    private final URL url;
    private final int port;

    Connector( URL url, int port ) {
        this.url = url;
        this.port = port;
    }

    public Socket create() throws IOException {
        return new Socket( url.getHost(), port );
    }
}
