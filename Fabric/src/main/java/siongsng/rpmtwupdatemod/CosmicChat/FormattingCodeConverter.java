/*
感謝 露米諾斯 Luminous#0631 製作此 Discord MarkDown 到 Minecraft 格式化代碼轉換器
 */
package siongsng.rpmtwupdatemod.CosmicChat;

public class FormattingCodeConverter {
    public String FormattingCodeToMD(String source) throws IndexOutOfBoundsException {
//        Pattern regex = Pattern.compile("/(\\*[^\\*]+\\*|\\*{2}[^\\*{2}]+\\*{2}|\\*{3}[^\\*{3}]+\\*{3}|~{2}[^~{2}]+~{2}|_{2}[^_{2}]+_{2})/g");
//        String Msg = "";
        StringBuilder result = new StringBuilder();
        boolean bold = false;
        boolean italic = false;
        boolean removing = false;
        boolean underscore = false;

        for (int i = 0; i < source.length(); i++) {
            switch (source.charAt(i)) {
                case '*':
                    if (source.charAt(i + 1) == '*') {
                        if (bold) {
                            result.append("§r");
                            result.append(italic ? "§o" : "");
                            result.append(removing ? "§m" : "");
                            result.append(underscore ? "§n" : "");
                        } else {
                            result.append("§l");
                        }
                        bold = !bold;
                        i++;
                        break;
                    }
                    if (italic) {
                        result.append("§r");
                        result.append(bold ? "§l" : "");
                        result.append(removing ? "§m" : "");
                        result.append(underscore ? "§n" : "");
                    } else {
                        result.append("§o");
                    }
                    italic = !italic;
                    break;

                case '~':
                    if (source.charAt(i + 1) == '~') {
                        if (removing) {
                            result.append("§r");
                            result.append(bold ? "§l" : "");
                            result.append(italic ? "§o" : "");
                            result.append(underscore ? "§n" : "");
                        } else {
                            result.append("§m");
                        }
                        removing = !removing;
                        i++;
                        break;
                    }
                    result.append("~");
                    break;
                case '_':
                    if (source.charAt(i + 1) == '_') {
                        if (underscore) {
                            result.append("§r");
                            result.append(bold ? "§l" : "");
                            result.append(italic ? "§o" : "");
                            result.append(removing ? "§m" : "");
                        } else {
                            result.append("§n");
                        }
                        underscore = !underscore;
                        i++;
                        break;
                    }
                    result.append("_");
                    break;
                case '\\':
                    switch (source.charAt(++i)) {
                        case '*' -> result.append("*");
                        case '~' -> result.append("~");
                        case '_' -> result.append("_");
                        case '\\' -> result.append("\\");
                        default -> result.append("\\").append(source.charAt(i));
                    }
                    break;

                default:
                    result.append(source.charAt(i));
                    break;
            }
        }

        if (bold) {
            int index = result.lastIndexOf("§l");
            result.delete(index, index + 2);
            result.insert(index,"**");
        }
        if (italic) {
            int index = result.lastIndexOf("§o");
            result.delete(index, index + 2);
            result.insert(index,"*");
        }
        if (removing) {
            int index = result.lastIndexOf("§m");
            result.delete(index, index + 2);
            result.insert(index,"~~");
        }
        if (underscore) {
            int index = result.lastIndexOf("§n");
            result.delete(index, index + 2);
            result.insert(index,"_");
        }

        return result.toString();
    }
}
