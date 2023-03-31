import java.net.*;  // for Socket, ServerSocket, and InetAddress
import java.io.*;   // for IOException and Input/OutputStream
import java.nio.charset.StandardCharsets;
import java.sql.*;

public class Server {
    private static final int BUFSIZE = 1;   // Size of receive buffer

    public static void main(String[] args) throws IOException, SQLException {

        // Create a server socket to accept client connection requests
        try {
            ServerSocket servSock = new ServerSocket(10044);
            for (;;) { // Run forever, accepting and servicing connections

                Socket clientSock = servSock.accept();
                InputStream in = clientSock.getInputStream();
                OutputStream out = clientSock.getOutputStream();

                String request = awaitMessage(in);
                String result = doRequest(request);
                reply(result, out);
            }
        } catch (IOException err) {
            System.err.println(err);
        }

    }

    /**
     * Await messages from connected client.
     * @param in Client InputStream
     * @return finalString
     * @throws IOException
     */
    public static String awaitMessage(InputStream in) throws IOException {
        String finalString = "";
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            byte[] data = new byte[BUFSIZE];
            while (in.read(data) != -1) {
                buffer.write(data);
                if ((char) data[0] == '\n') break;
                data = new byte[BUFSIZE];
            }

            byte[] receivedData = buffer.toByteArray();
            finalString = new String(receivedData, "UTF-16");
            finalString = finalString.substring(0, finalString.length() - 1);
        } catch (SocketException err) {
            System.err.println(err);
        }
        return finalString;
    }

    /**
     * Fulfills query or update request by calling post or get depending on the type of request.
     * @param request
     * @return
     * @throws SQLException
     */
    public static String doRequest(String request) throws SQLException {
        String result = "";
        String type;
        if (request.contains("SELECT")) {
            type = "query";
        } else {
            type = "update";
        }

        switch(type) {
            case "update":
                post(request);
                break;
            case "query":
                result = get(request);
                break;
            default:
                //do nothing
        }
        return result;
    }

    /**
     * Post changes to a table in the database.
     * @param request
     * @throws SQLException
     */
    public static void post(String request) throws SQLException {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:C:/Users/jlewi/IdeaProjects/Project1/shop.db");
            Statement statement = connection.createStatement();
            statement.executeUpdate(request);
        } catch (SQLException err) {
            System.err.println(err);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * Get data from a table.
     * @param request
     * @return
     * @throws SQLException
     */
    public static String get(String request) throws SQLException{
        Connection connection = null;
        String result = "";
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:C:/Users/jlewi/IdeaProjects/Project1/shop.db");
            Statement statement = connection.createStatement();
            String[] requestArray = request.split(" ");
            ResultSet columnNames = statement.executeQuery("PRAGMA table_info(" + requestArray[3] + ")");

            ResultSet query = statement.executeQuery(request);
            ResultSetMetaData columnsMetaData = columnNames.getMetaData();

            while (query.next()) {
                for (int i = 1; i < columnsMetaData.getColumnCount() + 1; i++) {
                    result += query.getString(i) + ";next;";
                }
                result += ";nextLine;";
            }
            result += "\n";
        } catch (SQLException error) {
            System.err.println(error.getMessage());
        } finally {
            if (connection != null) connection.close();
        }
        return result;
    }

    /**
     * Return back some data to the client.
     * @param result
     * @param out
     * @throws IOException
     */
    public static void reply(String result, OutputStream out) throws IOException {
        if (result == "") result = "\n";
        out.write(result.getBytes(StandardCharsets.UTF_16), 0, result.getBytes(StandardCharsets.UTF_16).length);
        out.flush();
    }
}
