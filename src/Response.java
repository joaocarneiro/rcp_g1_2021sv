import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Response {

    private final Socket connection;
    private List<String> response;
    private Map<String, String> headers;

    public Response( Socket connection ) {
        this.connection = connection;
    }

    public Response response() {
        try {
            BufferedReader reader = new BufferedReader( new InputStreamReader( connection.getInputStream() ) );
            response = new ArrayList<>();
            String line;
            while ( ( line = reader.readLine() ) != null ) {
                response.add( line );
                System.out.println( line );
            }
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        return this;
    }

    public int getStatusCode() {
        for ( String line : response ) {
            Pattern pattern = Pattern.compile( "HTTP\\/1.1 (\\d{3}) \\w.*" );
            Matcher matcher = pattern.matcher( line );
            if ( matcher.find() )
                return Integer.parseInt( matcher.group( 1 ) );
        }
        return -1;
    }

    public void getHeaders() {
        headers = new HashMap<>();
        for ( String s : response ) {
            Pattern pattern = Pattern.compile( "^([\\w\\-?\\w]*):\\s(.*)" );
            Matcher matcher = pattern.matcher( s );
            if ( matcher.find() ) {
                headers.put( matcher.group( 1 ), matcher.group( 2 ) );
            }
        }
    }

    public String getHeaderValue( String header ) {
        getHeaders();
        for ( String key : headers.keySet() ) {
            if ( key.equals( header ) )
                return headers.get( key );
        }
        return null;
    }

    public URL redirect() {
        URL url = null;
        String location = getHeaderValue( "Location" );
        try {
            url = new URL( location );
        } catch ( MalformedURLException e ) {
            e.printStackTrace();
        }
        System.out.println( "\nRedirecting to " + location + "\n" );
        try {
            Thread.sleep( 2000 );
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        }
        return url;
    }

    public boolean isStatusCode1XX() {
        int status = getStatusCode();
        return status >= 100 && status < 200;
    }

    public boolean isStatusCode2XX() {
        int status = getStatusCode();
        return status >= 200 && status < 300;
    }

    public boolean isStatusCode3XX() {
        int status = getStatusCode();
        return status >= 300 && status < 400;
    }

    public boolean isStatusCode4XX() {
        int status = getStatusCode();
        return status >= 400 && status < 500;
    }

    public boolean isStatusCode5XX() {
        int status = getStatusCode();
        return status >= 500 && status < 600;
    }
}
