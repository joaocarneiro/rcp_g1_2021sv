import java.net.*;
import java.util.*;

public class WebClient {
    private static Scanner scanner;
    private static String httpMethod;
    private static URL url;
    private static List<String> headers;

    public static void main( String[] args ) {
        scanner = new Scanner( System.in );
        int port = 80;

        insertHTTPMethod();
        insertURL();
        insertHeaders();

        try {
            Socket connector = new Connector( url, port ).create();
            while ( true ) {
                new Request( connector ).httpMethod( httpMethod )
                        .url( url )
                        .headers( headers )
                        .build();
                Response response = new Response( connector ).response();
                if ( response.isStatusCode1XX() ||
                     response.isStatusCode2XX() ||
                     response.isStatusCode4XX() ||
                     response.isStatusCode5XX() ) {
                    break;
                }
                if ( response.isStatusCode3XX() ) {
                    url = response.redirect();
                    connector = new Connector( url, port ).create();
                }
            }
        } catch ( UnknownHostException ex ) {
            System.out.println( "Server not found: " + ex.getMessage() );
        } catch ( Exception ex ) {
            System.out.println( "I/O error: " + ex.getMessage() );
        }
    }

    private static void insertHTTPMethod() {
        System.out.println( "Choose HTTP Method: " );
        for ( HTTPMethods value : HTTPMethods.values() ) {
            System.out.println( value );
        }
        System.out.print( "Choose one from the options presented above: " );
        httpMethod = scanner.nextLine().toUpperCase( Locale.ROOT );
    }

    private static void insertHeaders() {
        System.out.println( "Insert headers: " );
        headers = new ArrayList<>();
        String header;
        do {
            header = scanner.nextLine();
            headers.add( header );
        }
        while ( !header.equals( "" ) );
        System.out.println();
    }

    public static void insertURL() {
        System.out.print( "Insert URL: " );
        try {
            url = new URL( scanner.nextLine().toLowerCase( Locale.ROOT ) );
        } catch ( MalformedURLException ex ) {
            ex.printStackTrace();
        }
    }
}
