import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.List;

public class Request {

    private final Socket connection;
    private String httpMethod;
    private URL url;
    private List<String> headers;

    Request( Socket connection ) {
        this.connection = connection;
    }

    public Request httpMethod( String httpMethod ) {
        this.httpMethod = httpMethod;
        return this;
    }

    public Request url( URL url ) {
        this.url = url;
        return this;
    }

    public Request headers( List<String> headers ) {
        this.headers = headers;
        return this;
    }

    public void build() {
        try {
            StringBuilder request = new StringBuilder();
            request.append( httpMethod )
                   .append( " " )
                   .append( url.getPath() )
                   .append( " " )
                   .append( "HTTP/1.1" );

            PrintWriter printWriter = new PrintWriter( connection.getOutputStream(), true );
            printWriter.println( request );
            printWriter.println( "Host: " + url.getHost() );
            for ( String header : headers ) {
                printWriter.println( header );
            }
            printWriter.println();

        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }
}
