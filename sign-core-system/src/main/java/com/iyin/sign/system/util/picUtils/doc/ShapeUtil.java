//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iyin.sign.system.util.picUtils.doc;



import com.iyin.sign.system.util.picUtils.util.To;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D.Double;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.util.ArrayList;

public class ShapeUtil {
    public ShapeUtil() {
    }

    public static GeneralPath shapeToPath(Shape s) {
        GeneralPath path = new GeneralPath();

        for(PathIterator pi = s.getPathIterator((AffineTransform)null); !pi.isDone(); pi.next()) {
            float[] coordinates = new float[6];
            int type = pi.currentSegment(coordinates);
            switch(type) {
                case 0:
                    path.moveTo(coordinates[0], coordinates[1]);
                    break;
                case 1:
                    path.lineTo(coordinates[0], coordinates[1]);
                    break;
                case 2:
                    path.quadTo(coordinates[0], coordinates[1], coordinates[2], coordinates[3]);
                    break;
                case 3:
                    path.curveTo(coordinates[0], coordinates[1], coordinates[2], coordinates[3], coordinates[4], coordinates[5]);
                    break;
                case 4:
                    path.closePath();
            }
        }

        return path;
    }

    public static Shape getRectShape(int width, int height, String arc) {
        if(!arc.equals("0") && arc.length() != 0) {
            boolean a = false;
            int at = 15;
            int a1;
            if(arc.indexOf(",") > 0) {
                a1 = To.toInt(arc.substring(0, arc.indexOf(",")));
                at = getArcConner(arc);
            } else {
                a1 = To.toInt(arc);
            }

            if(a1 < 0) {
                return new Double(0.0D, 0.0D, (double)width, (double)height);
            } else {
                if(a1 > Math.min(width / 2, height / 2)) {
                    a1 = Math.min(width / 2, height / 2);
                }

                GeneralPath path = new GeneralPath();
                if((at & 1) == 1) {
                    path.moveTo(0.0F, (float)a1);
                    path.quadTo(0.0F, 0.0F, (float)a1, 0.0F);
                } else {
                    path.moveTo(0.0F, 0.0F);
                }

                if((at & 2) == 2) {
                    path.lineTo((float)(width - a1), 0.0F);
                    path.quadTo((float)width, 0.0F, (float)width, (float)a1);
                } else {
                    path.lineTo((float)width, 0.0F);
                }

                if((at & 4) == 4) {
                    path.lineTo((float)width, (float)(height - a1));
                    path.quadTo((float)width, (float)height, (float)(width - a1), (float)height);
                } else {
                    path.lineTo((float)width, (float)height);
                }

                if((at & 8) == 8) {
                    path.lineTo((float)a1, (float)height);
                    path.quadTo(0.0F, (float)height, 0.0F, (float)(height - a1));
                } else {
                    path.lineTo(0.0F, (float)height);
                }

                path.closePath();
                return path;
            }
        } else {
            return new Rectangle(0, 0, width, height);
        }
    }

    public static int getArcConner(String arc) {
        int at = 15;
        if(arc.indexOf(44) > 0) {
            String atStr = arc.substring(arc.indexOf(44) + 1);
            if(atStr.length() == 4) {
                at = (atStr.charAt(0) == 49?1:0) + (atStr.charAt(1) == 49?2:0) + (atStr.charAt(2) == 49?8:0) + (atStr.charAt(3) == 49?4:0);
            } else {
                at = To.toInt(atStr);
            }
        }

        return at;
    }

    public static String shapeToStr(Shape s) {
        StringBuffer sb = new StringBuffer();

        for(PathIterator pi = s.getPathIterator((AffineTransform)null); !pi.isDone(); pi.next()) {
            if(sb.length() > 0) {
                sb.append(" ");
            }

            float[] coordinates = new float[6];
            int type = pi.currentSegment(coordinates);
            switch(type) {
                case 0:
                    sb.append("M ");
                    sb.append(To.toString((double)coordinates[0])).append(" ");
                    sb.append(To.toString((double)coordinates[1]));
                    break;
                case 1:
                    sb.append("L ");
                    sb.append(To.toString((double)coordinates[0])).append(" ");
                    sb.append(To.toString((double)coordinates[1]));
                    break;
                case 2:
                    sb.append("Q ");
                    sb.append(To.toString((double)coordinates[0])).append(" ");
                    sb.append(To.toString((double)coordinates[1])).append(" ");
                    sb.append(To.toString((double)coordinates[2])).append(" ");
                    sb.append(To.toString((double)coordinates[3]));
                    break;
                case 3:
                    sb.append("C ");
                    sb.append(To.toString((double)coordinates[0])).append(" ");
                    sb.append(To.toString((double)coordinates[1])).append(" ");
                    sb.append(To.toString((double)coordinates[2])).append(" ");
                    sb.append(To.toString((double)coordinates[3])).append(" ");
                    sb.append(To.toString((double)coordinates[4])).append(" ");
                    sb.append(To.toString((double)coordinates[5]));
                    break;
                case 4:
                    sb.append("Z");
            }
        }

        return sb.toString();
    }

    public static String shapeToIntStr(Shape s) {
        StringBuffer sb = new StringBuffer();

        for(PathIterator pi = s.getPathIterator((AffineTransform)null); !pi.isDone(); pi.next()) {
            if(sb.length() > 0) {
                sb.append(" ");
            }

            float[] coordinates = new float[6];
            int type = pi.currentSegment(coordinates);

            for(int i = 0; i < coordinates.length; ++i) {
                coordinates[i] = (float) Math.round(coordinates[i]);
            }

            switch(type) {
                case 0:
                    sb.append("M ");
                    sb.append((int)coordinates[0]).append(" ");
                    sb.append((int)coordinates[1]);
                    break;
                case 1:
                    sb.append("L ");
                    sb.append((int)coordinates[0]).append(" ");
                    sb.append((int)coordinates[1]);
                    break;
                case 2:
                    sb.append("Q ");
                    sb.append((int)coordinates[0]).append(" ");
                    sb.append((int)coordinates[1]).append(" ");
                    sb.append((int)coordinates[2]).append(" ");
                    sb.append((int)coordinates[3]);
                    break;
                case 3:
                    sb.append("C ");
                    sb.append((int)coordinates[0]).append(" ");
                    sb.append((int)coordinates[1]).append(" ");
                    sb.append((int)coordinates[2]).append(" ");
                    sb.append((int)coordinates[3]).append(" ");
                    sb.append((int)coordinates[4]).append(" ");
                    sb.append((int)coordinates[5]);
                    break;
                case 4:
                    sb.append("Z");
            }
        }

        return sb.toString();
    }

    public static Shape strToShape(String str) {
        str = str.trim().toUpperCase();
        GeneralPath path = new GeneralPath();
        ArrayList data = new ArrayList();
        StringBuffer sb = new StringBuffer();
        char type = 32;

        for(int i = 0; i < str.length(); ++i) {
            char c = str.charAt(i);
            if(c != 77 && c != 76 && c != 81 && c != 67 && c != 90) {
                if(!Character.isSpaceChar(c) && c != 10 && c != 44) {
                    sb.append(c);
                } else {
                    if(sb.length() > 0) {
                        data.add(new java.lang.Double(To.toDouble(sb.toString())));
                    }

                    sb.setLength(0);
                }
            } else {
                if(sb.length() > 0) {
                    data.add(new java.lang.Double(To.toDouble(sb.toString())));
                }

                sb.setLength(0);
                processData(path, type, data);
                type = c;
            }
        }

        if(sb.length() > 0) {
            data.add(new java.lang.Double(To.toDouble(sb.toString())));
        }

        processData(path, type, data);
        return path;
    }

    private static void processData(GeneralPath path, char type, ArrayList data) {
        int i;
        if(type == 77) {
            for(i = 0; i + 1 < data.size(); i += 2) {
                path.moveTo(((java.lang.Double)data.get(i)).floatValue(), ((java.lang.Double)data.get(i + 1)).floatValue());
            }
        } else if(type == 76) {
            for(i = 0; i + 1 < data.size(); i += 2) {
                path.lineTo(((java.lang.Double)data.get(i)).floatValue(), ((java.lang.Double)data.get(i + 1)).floatValue());
            }
        } else if(type == 81) {
            for(i = 0; i + 3 < data.size(); i += 4) {
                path.quadTo(((java.lang.Double)data.get(i)).floatValue(), ((java.lang.Double)data.get(i + 1)).floatValue(), ((java.lang.Double)data.get(i + 2)).floatValue(), ((java.lang.Double)data.get(i + 3)).floatValue());
            }
        } else if(type == 67) {
            for(i = 0; i + 5 < data.size(); i += 6) {
                path.curveTo(((java.lang.Double)data.get(i)).floatValue(), ((java.lang.Double)data.get(i + 1)).floatValue(), ((java.lang.Double)data.get(i + 2)).floatValue(), ((java.lang.Double)data.get(i + 3)).floatValue(), ((java.lang.Double)data.get(i + 4)).floatValue(), ((java.lang.Double)data.get(i + 5)).floatValue());
            }
        } else if(type == 90) {
            path.closePath();
        }

        data.clear();
    }
}
