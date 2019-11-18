//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iyin.sign.system.util.picUtils.util;


import com.iyin.sign.system.util.picUtils.data.IntArray;

import java.text.DecimalFormat;
import java.util.Date;

public class To {
    public To() {
    }

    public static boolean toBool(Object obj, boolean def) {
        if(obj != null) {
            if(obj instanceof Boolean) {
                return ((Boolean)obj).booleanValue();
            } else if(obj instanceof Integer) {
                return ((Integer)obj).intValue() != 0;
            } else if(obj instanceof Long) {
                return ((Long)obj).longValue() != 0L;
            } else if(obj instanceof Double) {
                return ((Double)obj).doubleValue() != 0.0D;
            } else if(obj instanceof Date) {
                return ((Date)obj).getTime() != 0L;
            } else {
                String str = obj.toString().toUpperCase();
                return !str.equals("FALSE") && !str.equals("F") && !str.equals("NO") && !str.equals("OFF") && !str.equals("0") && !str.equals("");
            }
        } else {
            return def;
        }
    }

    public static boolean toBool(Object obj) {
        return toBool(obj, false);
    }

    public static long toLong(Object obj, long def) {
        if(obj != null) {
            if(obj instanceof Long) {
                return ((Long)obj).longValue();
            } else if(obj instanceof Integer) {
                return (long)((Integer)obj).intValue();
            } else if(obj instanceof Double) {
                return ((Double)obj).longValue();
            } else if(obj instanceof Boolean) {
                return (long)(((Boolean)obj).booleanValue()?1:0);
            } else if(obj instanceof Date) {
                return ((Date)obj).getTime();
            } else {
                try {
                    return Long.parseLong(obj.toString());
                } catch (Exception var4) {
                    return def;
                }
            }
        } else {
            return def;
        }
    }

    public static long toLong(Object obj) {
        return toLong(obj, 0L);
    }

    public static int toInt(Object obj, int def) {
        if(obj != null) {
            if(obj instanceof Integer) {
                return ((Integer)obj).intValue();
            } else if(obj instanceof Short) {
                return ((Short)obj).intValue();
            } else if(obj instanceof Long) {
                return ((Long)obj).intValue();
            } else if(obj instanceof Double) {
                return ((Double)obj).intValue();
            } else if(obj instanceof Boolean) {
                return ((Boolean)obj).booleanValue()?1:0;
            } else if(obj instanceof Date) {
                return (int)((Date)obj).getTime();
            } else {
                try {
                    return Integer.parseInt(obj.toString());
                } catch (Exception var5) {
                    try {
                        return (new Double(Double.parseDouble(obj.toString()))).intValue();
                    } catch (Exception var4) {
                        return def;
                    }
                }
            }
        } else {
            return def;
        }
    }

    public static int toInt(Object obj) {
        return toInt(obj, 0);
    }

    public static double toDouble(Object obj, double def) {
        if(obj != null) {
            if(obj instanceof Double) {
                return ((Double)obj).doubleValue();
            } else if(obj instanceof Float) {
                return ((Float)obj).doubleValue();
            } else if(obj instanceof Integer) {
                return (double)((Integer)obj).intValue();
            } else if(obj instanceof Long) {
                return ((Long)obj).doubleValue();
            } else if(obj instanceof Boolean) {
                return (double)(((Boolean)obj).booleanValue()?1:0);
            } else if(obj instanceof Date) {
                return (double)((Date)obj).getTime();
            } else {
                try {
                    return Double.parseDouble(obj.toString());
                } catch (Exception var4) {
                    return def;
                }
            }
        } else {
            return def;
        }
    }

    public static double toDouble(Object obj) {
        return toDouble(obj, 0.0D);
    }

    public static Date toDate(Object obj, Date def) {
        return obj != null?(obj instanceof Date ?(Date)obj:(obj instanceof Integer ?new Date((long)((Integer)obj).intValue()):(obj instanceof Long ?new Date(((Long)obj).longValue()):(obj instanceof Double ?new Date(((Double)obj).longValue()):(obj instanceof String ?DateUtil.toDate((String)obj, def):def))))):def;
    }

    public static String toString(Object obj) {
        return toString(obj, "");
    }

    public static String objToString(Object obj) {
        return objToString(obj, "");
    }

    public static String objToString(Object obj, String def) {
        return toString(obj, def);
    }

    public static Date objToDate(Object obj) {
        return toDate(obj, new Date());
    }

    public static Object strToObj(String str, int type, Object def) {
        Object obj = null;
        if(type == 12) {
            obj = str;
        } else if(type == 16) {
            obj = Boolean.valueOf(!str.equalsIgnoreCase("FALSE") && !str.equalsIgnoreCase("F") && !str.equalsIgnoreCase("NO") && !str.equalsIgnoreCase("0"));
        } else if(type == 4) {
            try {
                obj = Integer.valueOf(str);
            } catch (Exception var9) {
                obj = def == null?new Integer(0):def;
            }
        } else if(type == -5) {
            try {
                obj = Long.valueOf(str);
            } catch (Exception var8) {
                try {
                    obj = new Long(Double.valueOf(str).longValue());
                } catch (Exception var7) {
                    obj = def == null?new Long(0L):def;
                }
            }
        } else if(type == 8) {
            try {
                obj = Double.valueOf(str);
            } catch (Exception var6) {
                obj = def == null?new Double(0.0D):def;
            }
        } else if(type == 91) {
            obj = DateUtil.toDate(str);
        } else {
            obj = str;
        }

        return obj;
    }

    public static Object strToObj(String str, int type) {
        return strToObj(str, type, (Object)null);
    }

    public static Object autoType(String str) {
        if(str != null && str.length() > 0) {
            char c = str.charAt(0);
            boolean bDouble = false;
            if(c == 48) {
                if(str.length() == 1) {
                    return new Long(0L);
                }

                if(str.charAt(1) != 46) {
                    return str;
                }
            }

            if(Character.isDigit(c)) {
                for(int e = 1; e < str.length(); ++e) {
                    c = str.charAt(e);
                    if(!Character.isDigit(c)) {
                        if(c != 46 || bDouble) {
                            return str;
                        }

                        bDouble = true;
                    }
                }

                try {
                    if(bDouble) {
                        return new Double(str);
                    }

                    return new Long(str);
                } catch (Exception var4) {
                    return str;
                }
            }
        }

        return str;
    }

    public static Object toObj(Object obj, int type) {
        Object resobj;
        if(obj != null) {
            if(type == 12) {
                resobj = obj.toString();
            } else if(type == 16) {
                if(obj instanceof Boolean) {
                    resobj = obj;
                } else {
                    resobj = Boolean.valueOf(toBool(obj));
                }
            } else if(type == 4) {
                if(obj instanceof Integer) {
                    resobj = obj;
                } else {
                    resobj = new Integer(toInt(obj));
                }
            } else if(type == -5) {
                if(obj instanceof Long) {
                    resobj = obj;
                } else {
                    resobj = new Long(toLong(obj));
                }
            } else if(type == 8) {
                if(obj instanceof Double) {
                    resobj = obj;
                } else {
                    resobj = new Double(toDouble(obj));
                }
            } else if(type == 6) {
                if(obj instanceof Float) {
                    resobj = obj;
                } else {
                    resobj = new Float(toDouble(obj));
                }
            } else if(type == 91) {
                if(obj instanceof Date) {
                    resobj = obj;
                } else {
                    resobj = objToDate(obj);
                }
            } else if(type == 9002) {
                if(obj instanceof IntArray) {
                    resobj = obj;
                } else {
                    resobj = new IntArray();
                }
            } else if(type == 2000) {
                resobj = obj;
            } else {
                resobj = obj.toString();
            }
        } else {
            resobj = defValue(type);
        }

        return resobj;
    }

    public static Object defValue(int type) {
        return type == 16?new Boolean(false):(type == 5?new Short((short)0):(type == 4?new Integer(0):(type == -5?new Long(0L):(type == 6?new Float(0.0F):(type == 91?new Date():(type == 8?new Double(0.0D):(type == 2000?new Object():(type == 9002?new IntArray():""))))))));
    }

    public static String toString(Object obj, String def) {
        if(obj != null) {
            if(obj instanceof Date) {
                return DateUtil.toDateTimeString((Date)obj);
            } else if(!(obj instanceof Float) && !(obj instanceof Double)) {
                return obj.toString();
            } else {
                String str = obj.toString();
                if(str.indexOf("E") > 0) {
                    DecimalFormat df = new DecimalFormat("0.######");
                    if(obj instanceof Float) {
                        str = df.format((double)((Float)obj).floatValue());
                    } else {
                        str = df.format(((Double)obj).doubleValue());
                    }
                }

                if(str.endsWith(".0")) {
                    str = str.substring(0, str.length() - 2);
                }

                return str;
            }
        } else {
            return def;
        }
    }

    public static String toString(double d) {
        String str = Double.toString(d);
        if(str.indexOf("E") > 0) {
            str = (new DecimalFormat("0.######")).format(d);
        }

        if(str.endsWith(".0")) {
            str = str.substring(0, str.length() - 2);
        }

        return str;
    }
}
