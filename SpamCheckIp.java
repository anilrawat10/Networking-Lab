import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

public class SpamCheckIp {

    // Spamhaus returns 127.0.0.2, 127.0.0.3, etc. depending on the list.
    // All 127.0.0.0/24 responses indicate a listing. We exclude 127.0.0.1 (not listed).
    private static final String BLACKLIST_DOMAIN = "sbl.spamhaus.org";
    private static final Pattern IPV4_PATTERN = Pattern.compile(
            "^((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}" +
            "(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)$");

    /**
     * Checks whether the given IPv4 address is listed on the Spamhaus SBL.
     *
     * @param ipAddress the IPv4 address to check
     * @return true if the IP is blacklisted, false otherwise
     * @throws IllegalArgumentException if the IP is null or not a valid IPv4 address
     */
    public static boolean isSpam(String ipAddress) {
        if (ipAddress == null || !IPV4_PATTERN.matcher(ipAddress).matches()) {
            throw new IllegalArgumentException("Invalid IPv4 address: " + ipAddress);
        }

        // Reverse the IP address (e.g. 192.0.2.1 -> 1.2.0.192)
        String[] parts = ipAddress.split("\\.");
        String reversedIpAddress = parts[3] + "." + parts[2] + "." + parts[1] + "." + parts[0];
        String query = reversedIpAddress + "." + BLACKLIST_DOMAIN;

        try {
            InetAddress address = InetAddress.getByName(query);
            String result = address.getHostAddress();

            // Any 127.0.0.x response (except 127.0.0.1) means the IP is listed.
            return result.startsWith("127.0.0.") && !result.equals("127.0.0.1");
        } catch (UnknownHostException e) {
            // NXDOMAIN means the IP is not on the blacklist.
            return false;
        }
    }

    public static void main(String[] args) {
        String ipAddress = "192.0.2.1";
        try {
            boolean isSpam = isSpam(ipAddress);
            System.out.println("Is spam: " + isSpam);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}