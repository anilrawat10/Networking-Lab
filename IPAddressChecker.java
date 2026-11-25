import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;

public class IPAddressChecker {
    public static void main(String[] args) {
        String ip = "192.168.1.10"; // Enter IP address here

        try {
            InetAddress address = InetAddress.getByName(ip);

            // Check whether the input is a valid IPv4 or IPv6 address
            if (address instanceof Inet4Address && isValidIPv4(ip)) {
                System.out.println("IP Address : " + ip);
                System.out.println("Valid IP : Yes");
                System.out.println("Version : IPv4");
            } else if (address instanceof Inet6Address && isValidIPv6(ip)) {
                System.out.println("IP Address : " + ip);
                System.out.println("Valid IP : Yes");
                System.out.println("Version : IPv6");
            } else {
                System.out.println("IP Address : " + ip);
                System.out.println("Valid IP : No");
            }
        } catch (Exception e) {
            System.out.println("IP Address : " + ip);
            System.out.println("Valid IP : No");
        }
    }

    // Check IPv4 format
    public static boolean isValidIPv4(String ip) {
        String[] parts = ip.split("\\.", -1);

        if (parts.length != 4)
            return false;

        for (String part : parts) {
            // Each part must contain only digits
            if (!part.matches("\\d+"))
                return false;
            try {
                int value = Integer.parseInt(part);
                if (value < 0 || value > 255)
                    return false;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }

    // Check IPv6 format
    public static boolean isValidIPv6(String ip) {
        // Basic check: must contain ':' and be a valid IPv6 address
        if (ip == null || ip.isEmpty() || !ip.contains(":"))
            return false;

        // Must contain only hex digits and colons (allowing '::')
        if (!ip.matches("[0-9a-fA-F:.]+"))
            return false;

        // Use InetAddress to confirm it's a genuine IPv6 address
        try {
            InetAddress addr = InetAddress.getByName(ip);
            return addr instanceof Inet6Address;
        } catch (Exception e) {
            return false;
        }
    }
}