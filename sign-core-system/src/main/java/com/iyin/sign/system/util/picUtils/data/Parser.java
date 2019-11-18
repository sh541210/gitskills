//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iyin.sign.system.util.picUtils.data;



import com.iyin.sign.system.util.picUtils.util.HgException;

import java.util.ArrayList;

public class Parser {
    public static final String[] keyStr = new String[]{"CONNECT", "CREATE", "DROP", "ALTER", "SELECT", "SELECTX", "INSERT", "DELETE", "UPDATE", "TO", "DATABASE", "USER", "SCHEMA", "TABLE", "VIEW", "PROCEDURE", "TRIGGER", "SEQUENCE", "SYNONYM", "INDEX", "PUBLIC", "SYS", "FIELD", "TYPE", "CHAR", "VARCHAR", "VARCHAR2", "STRING", "INT", "DOUBLE", "NUMBER", "NUMERIC", "DATE", "ROW", "ROWSET", "ALL", "DISTINCT", "FROM", "WHERE", "GROUP", "BY", "ORDER", "LIMIT", "DESC", "ASC", "HAVING", "AS", "LIKE", "IN", "CROSS", "FULL", "INNER", "LEFT", "RIGHT", "OUTER", "JOIN", "ON", "SOME", "IN", "SOME", "ANY", "EXISTS", "BETWEEN", "AND", "NOT", "OR", "UNION", "MINUS", "INTERSECT", "NULL", "REPLACE", "CASE", "WHEN", "THEN", "ELSE", "ELSIF", "BEGIN", "EXCEPTION", "END", "RETURN", "IF", "FOR", "WHILE", "LOOP", "RESULT", "RAISE", "$OUTPUT", "RESULT", "ROWCOUNT", "DATA"};
    public static final char[] keyChar = new char[]{'+', '-', '*', '/', '|', ' ', ',', '.', '(', ')', '=', '!', '>', '<', '%', '^', '@', '#', '\n', '\t', '\'', '\"', '\\', '/', ':', ';', '?', '~'};

    public Parser() {
    }

    public static String encode(String str) {
        StringBuffer sb = new StringBuffer("\'");

        for(int i = 0; i < str.length(); ++i) {
            char c = str.charAt(i);
            if(c == 92) {
                sb.append("\\\\");
            } else if(c == 39) {
                sb.append("\'\'");
            } else if(c == 10) {
                sb.append("\\n");
            } else if(c == 13) {
                sb.append("\\r");
            } else if(c == 9) {
                sb.append("\\t");
            } else {
                sb.append(c);
            }
        }

        sb.append("\'");
        return sb.toString();
    }

    public static String decode(String str) {
        str = str.trim();
        if(str.startsWith("\'") && str.endsWith("\'")) {
            StringBuffer sb = new StringBuffer();

            for(int i = 1; i < str.length() - 1; ++i) {
                char c = str.charAt(i);
                if(c == 92 && i + 1 < str.length() - 1) {
                    c = str.charAt(i + 1);
                    if(c == 110) {
                        sb.append('\n');
                    } else if(c == 114) {
                        sb.append('\r');
                    } else if(c == 116) {
                        sb.append('\t');
                    } else if(c == 117) {
                        if(i + 6 < str.length()) {
                            try {
                                sb.append((char) Integer.decode("0x" + str.substring(i + 2, i + 6)).intValue());
                            } catch (Exception var5) {
                                sb.append(str.substring(i + 2, i + 6));
                            }

                            i += 4;
                        }
                    } else {
                        sb.append(c);
                    }

                    ++i;
                } else if(c == 39 && i + 1 < str.length() - 1 && str.charAt(i + 1) == 39) {
                    sb.append('\'');
                    ++i;
                } else {
                    sb.append(c);
                }
            }

            str = sb.toString();
        }

        return str;
    }

    public static String[] split(String str, char sepChar) {
        ArrayList strList = new ArrayList();
        int i;
        if(str.length() > 0) {
            i = 0;
            boolean strBegin = false;
            boolean expBegin = false;
            boolean singleQuotBegin = false;
            int pars = 0;
            String tmpExpStr = str.trim();
            boolean bSpace = false;

            for(int i1 = 0; i1 < tmpExpStr.length(); ++i1) {
                char strs = tmpExpStr.charAt(i1);
                if(sepChar == 32) {
                    if(bSpace) {
                        if(strs == 32) {
                            continue;
                        }

                        bSpace = false;
                    }

                    if(strs == 32) {
                        bSpace = true;
                    }
                }

                if(strs != 39 && singleQuotBegin) {
                    strBegin = false;
                    singleQuotBegin = false;
                }

                if(strs == 39) {
                    if(!strBegin) {
                        strBegin = true;
                    } else if(singleQuotBegin) {
                        singleQuotBegin = false;
                    } else {
                        singleQuotBegin = true;
                    }
                } else if(!strBegin && strs == 40) {
                    ++pars;
                } else if(!strBegin && strs == 41) {
                    --pars;
                } else if(!strBegin && pars == 0) {
                    if(strs == 44) {
                        expBegin = false;
                    }

                    if(strs != 43 && strs != 45 && strs != 42 && strs != 47) {
                        if(strs == sepChar) {
                            if(!expBegin) {
                                strList.add(tmpExpStr.substring(i, i1).trim());
                                i = i1 + 1;
                            }

                            if(expBegin) {
                                expBegin = false;
                            }
                        }
                    } else {
                        expBegin = true;
                    }
                }
            }

            if(i < tmpExpStr.length()) {
                strList.add(tmpExpStr.substring(i).trim());
            }
        }

        String[] var12 = new String[strList.size()];

        for(i = 0; i < strList.size(); ++i) {
            var12[i] = (String)strList.get(i);
        }

        return var12;
    }

    public static String[] split(String str) {
        return split(str, ' ');
    }

    public static String[] pretreat(String sql) throws HgException {
        if(sql.length() == 0) {
            return new String[]{""};
        } else {
            ArrayList sqlList = new ArrayList();
            StringBuffer sbSql = new StringBuffer();
            sql = sql.trim();
            int pars = 0;
            int lastIndex = 0;
            boolean strBegin = false;
            boolean singleQuotBegin = false;
            boolean pressed = true;

            int i;
            for(i = 0; i < sql.length(); ++i) {
                char c = sql.charAt(i);
                if(c != 39 && singleQuotBegin) {
                    if(strBegin) {
                        pressed = false;
                    }

                    strBegin = false;
                    singleQuotBegin = false;
                }

                if(c == 39) {
                    if(!strBegin) {
                        if(!strBegin) {
                            pressed = false;
                        }

                        strBegin = true;
                    } else if(singleQuotBegin) {
                        singleQuotBegin = false;
                    } else {
                        singleQuotBegin = true;
                    }
                } else if(!strBegin && c == 40) {
                    ++pars;
                } else if(!strBegin && c == 41) {
                    --pars;
                }

                if(strBegin && c == 92 && i + 1 < sql.length()) {
                    if(sql.charAt(i + 1) == 39) {
                        sql = sql.substring(0, i) + '\'' + sql.substring(i + 1);
                        ++i;
                        continue;
                    }

                    if(sql.charAt(i + 1) == 92) {
                        ++i;
                        continue;
                    }
                }

                if(strBegin && !pressed) {
                    sbSql.append(upperIgnoreDQ(sql.substring(lastIndex, i)));
                    lastIndex = i;
                    pressed = true;
                }

                if(!strBegin && !pressed) {
                    sbSql.append(sql.substring(lastIndex, i));
                    lastIndex = i;
                    pressed = true;
                }

                if(!strBegin && c == 45 && sql.charAt(i + 1) == 45) {
                    sbSql.append(upperIgnoreDQ(sql.substring(lastIndex, i)));
                    if(sql.indexOf(10, i + 1) >= 0) {
                        i = sql.indexOf(10, i + 1);
                        lastIndex = i + 1;
                    } else {
                        i = sql.length();
                        lastIndex = i;
                    }
                }

                if(!strBegin && c == 47 && i + 1 < sql.length() && sql.charAt(i + 1) == 42) {
                    sbSql.append(upperIgnoreDQ(sql.substring(lastIndex, i)));
                    if(sql.indexOf("*/", i + 1) >= 0) {
                        i = sql.indexOf("*/", i + 1) + 1;
                        lastIndex = i + 1;
                    } else {
                        i = sql.length();
                        lastIndex = i;
                    }
                }

                if(!strBegin && c == 59) {
                    if(pars > 0) {
                        throw new HgException("括号没有结束");
                    }

                    sbSql.append(upperIgnoreDQ(sql.substring(lastIndex, i)));
                    lastIndex = i + 1;
                    sqlList.add(sbSql.toString());
                    sbSql.setLength(0);
                }

                if(!strBegin && c == 36 && i + 1 < sql.length() && sql.charAt(i + 1) == 123) {
                    sbSql.append(upperIgnoreDQ(sql.substring(lastIndex, i)));
                    sbSql.append("XV(");
                    i += 2;

                    for(StringBuffer sb = new StringBuffer(); i < sql.length(); ++i) {
                        c = sql.charAt(i);
                        if(c == 125) {
                            sbSql.append(encode(sb.toString())).append(")");
                            break;
                        }

                        sb.append(c);
                    }

                    lastIndex = i + 1;
                }
            }

            if(strBegin && !singleQuotBegin) {
                throw new HgException("字符串没有结束");
            } else if(pars > 0) {
                throw new HgException("括号没有结束");
            } else {
                if(lastIndex < sql.length()) {
                    if(strBegin && singleQuotBegin) {
                        sbSql.append(sql.substring(lastIndex));
                    } else {
                        sbSql.append(upperIgnoreDQ(sql.substring(lastIndex)));
                    }

                    sqlList.add(sbSql.toString());
                    sbSql.setLength(0);
                }

                if(sbSql.length() > 0) {
                    sqlList.add(sbSql.toString());
                }

                String[] strs = new String[sqlList.size()];

                for(i = 0; i < strs.length; ++i) {
                    strs[i] = transCaseInStr((String)sqlList.get(i));
                }

                return strs;
            }
        }
    }

    private static String upperIgnoreDQ(String str) {
        if(str.indexOf("\"") >= 0) {
            StringBuffer sb = new StringBuffer();
            boolean b = false;

            for(int i = 0; i < str.length(); ++i) {
                if(str.charAt(i) == 34) {
                    b = !b;
                } else {
                    sb.append(!b? Character.toUpperCase(str.charAt(i)):str.charAt(i));
                }
            }

            str = sb.toString();
        } else {
            str = str.toUpperCase();
        }

        return str.replace('\n', ' ').replace('\r', ' ');
    }

    private static String transCaseInStr(String str) throws HgException {
        String resStr = str;
        if(str.indexOf("CASE ") >= 0) {
            StringBuffer sb = new StringBuffer();
            sb.append(' ').append(str).append(' ');
            str = sb.toString();
            sb.setLength(0);
            boolean strBegin = false;
            boolean caseBegin = false;
            boolean singleQuotBegin = false;
            int lastCommaIndex = 0;

            for(int i = 0; i < str.length(); ++i) {
                char c = str.charAt(i);
                if(c != 39 && singleQuotBegin) {
                    strBegin = false;
                    singleQuotBegin = false;
                }

                if(c == 39) {
                    if(!strBegin) {
                        strBegin = true;
                    } else if(singleQuotBegin) {
                        singleQuotBegin = false;
                    } else {
                        singleQuotBegin = true;
                    }
                }

                if(!strBegin) {
                    if(!Character.isLetterOrDigit(c) && i + 6 < str.length() && str.substring(i + 1, i + 6).equals("CASE ")) {
                        if(caseBegin) {
                            throw new HgException("case语句不允许嵌套");
                        }

                        caseBegin = true;
                        sb.append(str.substring(lastCommaIndex, i + 1));
                        lastCommaIndex = i + 1;
                        i += 6;
                    } else if(caseBegin && (i + 4 == str.length() && str.substring(i, i + 4).equals(" END") || i + 4 < str.length() && str.substring(i, i + 4).equals(" END") && !Character.isLetterOrDigit(str.charAt(i + 4)))) {
                        caseBegin = false;
                        sb.append(parseCase(str.substring(lastCommaIndex, i + 4)));
                        lastCommaIndex = i + 4;
                    }
                }
            }

            if(lastCommaIndex < str.length()) {
                sb.append(str.substring(lastCommaIndex));
            }

            resStr = sb.toString();
        }

        return resStr;
    }

    private static String parseCase(String str) {
        str = str.trim().substring(4).trim();
        StringBuffer sb = new StringBuffer();
        boolean strBegin;
        boolean singleQuotBegin;
        int pars;
        int lastCommaIndex;
        char c;
        int i;
        if(str.startsWith("WHEN ")) {
            sb.append("CASE(");
            str = str.substring(5);
            strBegin = false;
            singleQuotBegin = false;
            pars = 0;
            lastCommaIndex = 0;

            for(i = 0; i < str.length(); ++i) {
                c = str.charAt(i);
                if(c != 39 && singleQuotBegin) {
                    strBegin = false;
                    singleQuotBegin = false;
                }

                if(c == 39) {
                    if(!strBegin) {
                        strBegin = true;
                    } else if(singleQuotBegin) {
                        singleQuotBegin = false;
                    } else {
                        singleQuotBegin = true;
                    }
                } else if(!strBegin && c == 40) {
                    ++pars;
                } else if(!strBegin && c == 41) {
                    --pars;
                }

                if(!strBegin && pars == 0) {
                    if(i + 6 >= str.length() || !str.substring(i, i + 6).equals(" WHEN ") && !str.substring(i, i + 6).equals(" ELSE ")) {
                        if(i + 6 < str.length() && str.substring(i, i + 6).equals(" THEN ")) {
                            if(sb.length() > 5) {
                                sb.append(",");
                            }

                            sb.append(encode(str.substring(lastCommaIndex, i)));
                            lastCommaIndex = i + 6;
                            i += 5;
                        } else if(i + 4 == str.length() && str.substring(i, i + 4).equals(" END")) {
                            if(sb.length() > 5) {
                                sb.append(",");
                            }

                            sb.append(str.substring(lastCommaIndex, i));
                            break;
                        }
                    } else {
                        if(sb.length() > 5) {
                            sb.append(",");
                        }

                        sb.append(str.substring(lastCommaIndex, i));
                        lastCommaIndex = i + 6;
                        i += 5;
                    }
                }
            }
        } else {
            sb.append("DECODE(");
            strBegin = false;
            singleQuotBegin = false;
            pars = 0;
            lastCommaIndex = 0;

            for(i = 0; i < str.length(); ++i) {
                c = str.charAt(i);
                if(c != 39 && singleQuotBegin) {
                    strBegin = false;
                    singleQuotBegin = false;
                }

                if(c == 39) {
                    if(!strBegin) {
                        strBegin = true;
                    } else if(singleQuotBegin) {
                        singleQuotBegin = false;
                    } else {
                        singleQuotBegin = true;
                    }
                } else if(!strBegin && c == 40) {
                    ++pars;
                } else if(!strBegin && c == 41) {
                    --pars;
                }

                if(!strBegin && pars == 0) {
                    if(i + 6 < str.length() && (str.substring(i, i + 6).equals(" WHEN ") || str.substring(i, i + 6).equals(" THEN ") || str.substring(i, i + 6).equals(" ELSE "))) {
                        if(sb.length() > 7) {
                            sb.append(",");
                        }

                        sb.append(str.substring(lastCommaIndex, i));
                        lastCommaIndex = i + 6;
                        i += 5;
                    } else if(i + 4 == str.length() && str.substring(i, i + 4).equals(" END")) {
                        if(sb.length() > 7) {
                            sb.append(",");
                        }

                        sb.append(str.substring(lastCommaIndex, i));
                        break;
                    }
                }
            }
        }

        sb.append(")");
        return sb.toString();
    }

    public static int keyAt(String sql, String key) {
        return keyAt(sql, key, true);
    }

    public static int keyAt(String sql, String key, boolean inPar) {
        int pos = -1;
        sql = " " + sql + " ";
        boolean strBegin = false;
        boolean singleQuotBegin = false;
        int pars = 0;

        for(int i = 0; i < sql.length(); ++i) {
            char c = sql.charAt(i);
            if(c != 39 && singleQuotBegin) {
                strBegin = false;
                singleQuotBegin = false;
            }

            if(c == 39) {
                if(!strBegin) {
                    strBegin = true;
                } else if(singleQuotBegin) {
                    singleQuotBegin = false;
                } else {
                    singleQuotBegin = true;
                }
            } else if(!strBegin && c == 40) {
                ++pars;
            } else if(!strBegin && c == 41) {
                --pars;
            } else if(!strBegin && (inPar || pars == 0) && sql.length() > i + key.length() && sql.substring(i, i + key.length()).equals(key)) {
                pos = i - 1;
                break;
            }
        }

        return pos;
    }

    public static String[] parseParam(String str) {
        String[] strs = split(str);

        for(int i = 0; i < strs.length; ++i) {
            if(strs[i].startsWith("\'") && strs[i].endsWith("\'")) {
                strs[i] = decode(strs[i]);
            }
        }

        return strs;
    }

    public static String parseSchemaName(String name) {
        String schema = "";
        if(!name.startsWith("//") && !name.startsWith("$")) {
            if(name.indexOf(64) > 0) {
                schema = name.substring(name.indexOf(64) + 1);
                if(schema.length() == 0 && name.indexOf(".") > 0) {
                    schema = name.substring(0, name.indexOf("."));
                }
            } else if(name.indexOf(".") > 0) {
                schema = name.substring(0, name.indexOf("."));
            }
        }

        return schema;
    }

    public static String parseObjName(String name) {
        String objName = name;
        if(!name.startsWith("//") && !name.startsWith("$")) {
            if(name.indexOf(64) > 0) {
                objName = name.substring(0, name.indexOf(64) + 1);
                if(name.endsWith("@") && name.indexOf(".") > 0) {
                    objName = name.substring(name.indexOf(".") + 1);
                }
            } else if(name.indexOf(".") > 0) {
                objName = name.substring(name.indexOf(".") + 1);
            }
        }

        return objName;
    }

    public static String[] paraSplit(String sql) {
        ArrayList strList = new ArrayList();
        int i;
        if(sql.length() > 0) {
            i = 0;
            boolean strBegin = false;
            boolean singleQuotBegin = false;
            String tmpExpStr = sql;

            for(int i1 = 0; i1 < tmpExpStr.length(); ++i1) {
                char strs = tmpExpStr.charAt(i1);
                if(strs != 39 && singleQuotBegin) {
                    strBegin = false;
                    singleQuotBegin = false;
                }

                if(strs == 39) {
                    if(!strBegin) {
                        strBegin = true;
                    } else if(singleQuotBegin) {
                        singleQuotBegin = false;
                    } else {
                        singleQuotBegin = true;
                    }
                } else if(!strBegin && strs == 10) {
                    if(i1 == tmpExpStr.length() && tmpExpStr.charAt(i1 + 1) == 47) {
                        strList.add(tmpExpStr.substring(i, i1).trim());
                        i = i1 + 1;
                        ++i1;
                    } else if(i1 < tmpExpStr.length() - 2 && tmpExpStr.charAt(i1 + 1) == 47 && (tmpExpStr.charAt(i1 + 2) == 13 || tmpExpStr.charAt(i1 + 2) == 10)) {
                        strList.add(tmpExpStr.substring(i, i1).trim());
                        i = i1 + 2;
                        i1 += 2;
                    }
                }
            }

            if(i < tmpExpStr.length()) {
                strList.add(tmpExpStr.substring(i).trim());
            }
        }

        String[] var8 = new String[strList.size()];

        for(i = 0; i < strList.size(); ++i) {
            var8[i] = (String)strList.get(i);
        }

        return var8;
    }
}
