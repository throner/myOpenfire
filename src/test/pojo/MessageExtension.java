package test.pojo;

import org.jivesoftware.smack.packet.PacketExtension;

/**
 * Created by chenjiansheng on 2016-5-28.
 */
public class MessageExtension implements PacketExtension {
    public static final String NAME = "ecc";
    public static final String NAME_SPACE = "ecc:message";

    public String getElementName() {
        return NAME;
    }

    public String getNamespace() {
        return NAME_SPACE;
    }

    public String toXML() {
        StringBuffer sb = new StringBuffer();
        sb.append("<base_info>");
        sb.append("<send_name>张三</send_name>");
        sb.append("<rec_name>李四</rec_name>");
        sb.append("</base_info>");
        sb.append("<show_body>");
        sb.append("<test>xxx</test>");
        sb.append("</show_body>");
        return String.valueOf(sb);
    }
}
