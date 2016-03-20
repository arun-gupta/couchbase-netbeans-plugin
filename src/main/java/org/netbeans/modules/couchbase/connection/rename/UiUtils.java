package org.netbeans.modules.couchbase.connection.rename;

import com.couchbase.client.java.Cluster;
import java.awt.Font;
import java.awt.FontMetrics;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;

public final class UiUtils {

    private static final Logger LOGGER = Logger.getLogger(UiUtils.class.getName());

    private UiUtils() {
        super();
    }

    public static String getValue(JComboBox<String> combo) {
        if (combo.isEditable()) {
            return getValue((String) combo.getEditor().getItem());
        }
        return getValue((String) combo.getSelectedItem());
    }

    public static String getValue(JTextComponent c) {
        return getValue(c.getText());
    }

    public static String getValue(String str) {
        String value = str;
        if (value != null) {
            value = value.trim();
            if (value.isEmpty()) {
                return null;
            }
        }
        return value;
    }

    public static void configureRowHeight(JTable table) {
        int height = table.getRowHeight();
        Font cellFont = UIManager.getFont("TextField.font");
        if (cellFont != null) {
            FontMetrics metrics = table.getFontMetrics(cellFont);
            if (metrics != null) {
                height = metrics.getHeight() + 2;
            }
        }
        table.setRowHeight(Math.max(table.getRowHeight(), height));
    }

}
