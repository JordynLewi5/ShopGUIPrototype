import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class Client {
    private static final int BUFSIZE = 1;


    /**
     * Sends message containing SQL script to server and awaits reply and then returns the reply.
     * @param message
     * @return {String} reply
     */
    public static String sendRequest(String message) {

        message += "\n";
        String reply = "";

        try {
            Socket socket = new Socket("localhost", 10044);
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            out.write(message.getBytes(StandardCharsets.UTF_16), 0, message.getBytes(StandardCharsets.UTF_16).length);
            out.flush();

            reply = awaitReply(in);

        } catch (IOException err) {
            System.err.println(err);
        }

        return reply;
    }

    /**
     * Awaits a reply after a request is sent and then returns the reply.
     * @return {String} finalString
     * @throws IOException
     */
    public static String awaitReply(InputStream in) throws IOException {

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
            finalString = new String(receivedData, StandardCharsets.UTF_16);
            finalString = finalString.substring(0, finalString.length() - 1);
            System.out.println(finalString);
        } catch (SocketException err) {
            System.err.println(err);
        }
        return finalString;
    }

    /**
     * Inserts a new record into a table.
     * @param tableName
     * @param columnNames
     * @param columnValues
     */
    public static void insertOnServer(String tableName, String columnNames, String columnValues) {

        sendRequest("INSERT INTO " + tableName + " (" + columnNames + ") VALUES (" + columnValues +")");
    }

    /**
     * Inserts a new record into a table.
     * @param tableName
     * @param columnValues
     */
    public static void insertOnServer(String tableName, String columnValues) {

        sendRequest("INSERT INTO " + tableName + " VALUES(" + columnValues + ")");
    }

    /**
     * Inserts a new record into a table.
     * @param message
     */
    public static void insertOnServer(String message) {
        sendRequest(message);
    }

    /**
     * Update existing record in a table.
     * @param tableName
     * @param columnName
     * @param setColumn
     * @param identifier
     * @param key
     */
    public static void updateOnServer(String tableName, String columnName, String setColumn, String identifier, String key) {

        sendRequest("UPDATE " + tableName + " SET " + columnName + " = " + setColumn + " WHERE " + identifier + " = " + key);
    }

    /**
     * Delete an existing record in a table.
     * @param tableName
     * @param identifier
     * @param key
     */
    public static void deleteOnServer(String tableName, String identifier, String key) {

        sendRequest("DELETE FROM " + tableName + " WHERE " + identifier + " = " + key);
    }

    /**
     * Select and retrieve table from server.
     * @param tableName
     * @return {ArrayList<String[]>} table
     */
    public static ArrayList<String[]> selectOnServer(String tableName) {

        return parseStringToArrayList(sendRequest("SELECT * FROM " + tableName));
    }

    /**
     * Parse the received string from server into an ArrayList<String[]> table.
     * @param string
     * @return {ArrayList<String[]>} table
     */
    public static ArrayList<String[]> parseStringToArrayList(String string) {

        ArrayList<String[]> table = new ArrayList<String[]>();
        ArrayList<String> lines = new ArrayList<>(Arrays.asList(string.split(";nextLine;")));

        for (String line : lines) {
            table.add(line.split(";next;"));
        }

        return table;
    }
}
