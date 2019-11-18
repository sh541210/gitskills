//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iyin.sign.system.util.picUtils.data;


import com.iyin.sign.system.util.picUtils.util.HgException;
import com.iyin.sign.system.util.picUtils.util.To;

import java.util.ArrayList;

public class MathExpression extends Expression {
    private static final int PARSE_RES_TYPE_BOOLEAN = 0;
    private static final int PARSE_RES_TYPE_LONG = 1;
    private static final int PARSE_RES_TYPE_DOUBLE = 2;
    private static final int PARSE_RES_TYPE_STRING = 3;
    public static final int PARSE_RES_TYPE_VARIABLE = 4;
    private static final int PARSE_RES_TYPE_FUNC_EXPRESSION = 5;
    private static final int PARSE_RES_TYPE_MATH_EXPRESSION = 6;
    public static final int PARSE_RES_TYPE_QUERY = 7;
    public int parseResType;
    public Expression expA;
    public Expression expB;
    private char operator;

    public MathExpression(BlkExpression blkExp, String str) {
        super(blkExp, str);
    }

    public void setExpStr(String str) throws HgException {
        this.expStr = str;
        this.parse();
    }

    public void parse() throws HgException {
        if(this.expStr.length() > 0) {
            boolean strBegin = false;
            boolean singleQuotBegin = false;
            int pars = 0;
            String expStrA = "";
            String expStrB = "";
            String tmpExpStr = this.expStr.trim();
            boolean split = false;

            do {
                if(tmpExpStr.length() == 0) {
                    tmpExpStr = "\'\'";
                }

                char c;
                int i;
                for(i = tmpExpStr.length() - 1; i > -1; --i) {
                    c = tmpExpStr.charAt(i);
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
                    } else if(!strBegin && pars == 0 && (c == 43 || c == 45 || c == 124)) {
                        this.operator = c;
                        expStrB = tmpExpStr.substring(i + 1);
                        if(c == 124 && i - 1 >= 0 && tmpExpStr.charAt(i - 1) == 124) {
                            --i;
                        }

                        expStrA = tmpExpStr.substring(0, i);
                        split = true;
                        break;
                    }
                }

                if(!split) {
                    for(i = tmpExpStr.length() - 1; i > -1; --i) {
                        c = tmpExpStr.charAt(i);
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
                        } else if(!strBegin && pars == 0 && (c == 42 || c == 47 || c == 37)) {
                            this.operator = c;
                            expStrA = tmpExpStr.substring(0, i);
                            expStrB = tmpExpStr.substring(i + 1);
                            split = true;
                            break;
                        }
                    }
                }

                if(split || tmpExpStr.charAt(0) != 40 || tmpExpStr.charAt(tmpExpStr.length() - 1) != 41) {
                    break;
                }

                tmpExpStr = tmpExpStr.substring(1, tmpExpStr.length() - 1).trim();
            } while(!tmpExpStr.startsWith("SELECT ") && !tmpExpStr.startsWith("SELECTX ") && !tmpExpStr.startsWith("#"));

            if(!split) {
                if(tmpExpStr.charAt(0) == 39 && tmpExpStr.charAt(tmpExpStr.length() - 1) == 39) {
                    this.resultDataType = 12;
                    this.parseResType = 3;
                    this.parseRes = Parser.decode(tmpExpStr);
                } else {
                    if(tmpExpStr.indexOf(46) >= 0) {
                        try {
                            this.parseRes = Double.valueOf(tmpExpStr);
                            this.resultDataType = 8;
                            this.parseResType = 2;
                        } catch (Exception var11) {
                            ;
                        }
                    } else {
                        try {
                            this.parseRes = Long.valueOf(tmpExpStr);
                            this.resultDataType = -5;
                            this.parseResType = 1;
                        } catch (Exception var10) {
                            ;
                        }
                    }

                    if(this.parseRes == null) {
                        if(tmpExpStr.equals("NULL")) {
                            this.parseResType = 3;
                            this.resultDataType = 12;
                            this.parseRes = "";
                        } else if(tmpExpStr.equals("TRUE")) {
                            this.parseResType = 0;
                            this.resultDataType = 16;
                            this.parseRes = Boolean.TRUE;
                        } else if(tmpExpStr.equals("FALSE")) {
                            this.parseResType = 0;
                            this.resultDataType = 16;
                            this.parseRes = Boolean.FALSE;
                        } else {
                            this.parseResType = 4;
                            this.parseRes = tmpExpStr;
                        }
                    }
                }
            } else {
                this.expA = new MathExpression(this.blkExp, expStrA);
                this.expB = new MathExpression(this.blkExp, expStrB);
                this.expA.parse();
                this.expB.parse();
                this.parseResType = 6;
            }
        } else {
            this.resultDataType = -5;
            this.parseResType = 1;
            this.parseRes = new Long(0L);
        }

        this.parseEval();
        this.parsed = true;
    }

    private void parseEval() {
        if(this.parseResType == 6 && ((MathExpression)this.expA).isConst() && ((MathExpression)this.expB).isConst()) {
            Object resA = this.expA.parseRes;
            Object resB = this.expB.parseRes;
            if(this.operator == 45) {
                if(this.expA.resultDataType == -5 && this.expB.resultDataType == -5) {
                    this.resultDataType = -5;
                    this.result = new Long(To.toLong(resA) - To.toLong(resB));
                } else {
                    this.resultDataType = 8;
                    this.result = new Double(To.toDouble(resA) - To.toDouble(resB));
                }
            } else if(this.operator == 42) {
                if(this.expA.resultDataType == -5 && this.expB.resultDataType == -5) {
                    this.resultDataType = -5;
                    this.result = new Long(To.toLong(resA) * To.toLong(resB));
                } else {
                    this.resultDataType = 8;
                    this.result = new Double(To.toDouble(resA) * To.toDouble(resB));
                }
            } else if(this.operator == 47) {
                this.resultDataType = 8;
                if(this.resultDataStyle == 3 && To.toLong(resB) == 0L) {
                    this.result = new Double(1.7976931348623157E308D);
                } else {
                    this.result = new Double(To.toDouble(resA) / To.toDouble(resB));
                }
            } else if(this.operator == 37) {
                this.resultDataType = -5;
                if(this.resultDataStyle == 3 && To.toLong(resB) == 0L) {
                    this.result = new Long(9223372036854775807L);
                } else {
                    this.result = new Long(To.toLong(resA) % To.toLong(resB));
                }
            } else if(this.operator == 43 || this.operator == 124) {
                if(this.operator == 43 && this.expA.resultDataType == -5 && this.expB.resultDataType == -5) {
                    this.resultDataType = -5;
                    this.result = new Long(To.toLong(resA) + To.toLong(resB));
                } else if(this.operator != 124 && this.expA.resultDataType != 12 && this.expB.resultDataType != 12) {
                    this.resultDataType = 8;
                    this.result = new Double(To.toDouble(resA) + To.toDouble(resB));
                } else {
                    this.resultDataType = 12;
                    this.result = To.objToString(resA) + To.objToString(resB);
                }
            }

            this.expA = null;
            this.expB = null;
            if(this.resultDataType == 16) {
                this.parseResType = 0;
            } else if(this.resultDataType == -5) {
                this.parseResType = 1;
            } else if(this.resultDataType == 8) {
                this.parseResType = 2;
            } else if(this.resultDataType == 12) {
                this.parseResType = 3;
            }

            this.parseRes = this.result;
        }

    }

    public void eval(Conn conn) throws HgException {
        super.eval(conn);
        if(this.expA != null) {
            this.expA.data = this.data;
            this.expA.dataStyle = this.dataStyle;
        }

        if(this.expB != null) {
            this.expB.data = this.data;
            this.expB.dataStyle = this.dataStyle;
        }

        int i;
        if(this.parseResType != 0 && this.parseResType != 1 && this.parseResType != 2 && this.parseResType != 3) {
            RowSet var9;
            Array var14;
            if(this.parseResType == 4) {
                if(this.dataStyle == 1) {
                    this.resultDataStyle = 3;
                    Field var8 = null;
                    if(this.data != null && ((String)this.parseRes).charAt(0) != 58) {
                        Row var10 = (Row)this.data;
                        var8 = var10.getField((String)this.parseRes);
                        if(var8 != null) {
                            this.resultDataType = var8.type;
                            this.result = var10.get((String)this.parseRes);
                        }
                    }

                    if(var8 == null) {
                        if(((String)this.parseRes).charAt(0) != 58) {
                            this.result = this.getByVar((String)this.parseRes);
                        } else {
                            this.result = this.getByVar(((String)this.parseRes).substring(1));
                        }

                        if(this.result != null) {
                            if(this.result instanceof Array && ((Array)this.result).size() > 0) {
                                this.result = ((Array)this.result).getObj(0);
                            }

                            this.resultDataType = getDataType(this.result);
                        } else {
                            this.resultDataType = 12;
                            this.result = "";
                        }
                    }
                } else {
                    this.resultDataStyle = 4;
                    this.result = null;
                    var9 = (RowSet)this.data;
                    Field var12 = null;
                    if(this.data != null) {
                        var12 = var9.getField((String)this.parseRes);
                        if(var12 != null) {
                            this.resultDataType = var12.type;
                            var14 = new Array(this.resultDataType);

                            for(int resObjs = 0; resObjs < var9.size(); ++resObjs) {
                                Row objsB = var9.get(resObjs);
                                var14.addObj(objsB.get((String)this.parseRes));
                            }

                            this.result = var14;
                        }
                    }

                    if(var12 == null) {
                        this.result = this.getByVar((String)this.parseRes);
                        int var15;
                        if(this.result != null) {
                            this.resultDataType = getDataType(this.result);
                            var14 = new Array(this.resultDataType);

                            for(var15 = 0; var15 < var9.size(); ++var15) {
                                var14.addObj(this.result);
                            }
                        } else {
                            this.resultDataType = 0;
                            var14 = new Array();

                            for(var15 = 0; var15 < var9.size(); ++var15) {
                                var14.addObj("");
                            }
                        }

                        this.result = var14;
                    }
                }
            } else if(this.parseResType == 6) {
                this.expA.eval(conn);
                this.expB.eval(conn);
                Object var11 = null;
                Object var13 = null;
                if(this.expA.resultDataStyle != 3 && this.expB.resultDataStyle != 3) {
                    this.resultDataStyle = 4;
                } else {
                    this.resultDataStyle = 3;
                    if(this.expA.resultDataStyle == 4) {
                        ((MathExpression)this.expA).compress();
                    }

                    if(this.expB.resultDataStyle == 4) {
                        ((MathExpression)this.expB).compress();
                    }

                    var11 = this.expA.result;
                    var13 = this.expB.result;
                }

                int i1;
                Array var16;
                Array var17;
                if(this.operator == 45) {
                    if(this.expA.resultDataType == -5 && this.expB.resultDataType == -5) {
                        this.resultDataType = -5;
                        if(this.resultDataStyle == 3) {
                            this.result = new Long(To.toLong(var11) - To.toLong(var13));
                        } else {
                            var14 = (Array)this.expA.result;
                            var16 = (Array)this.expB.result;
                            var17 = new Array(-5);

                            for(i1 = 0; i1 < var14.size(); ++i1) {
                                var17.addObj(new Long(To.toLong(var14.getObj(i1)) - To.toLong(var16.getObj(i1))));
                            }

                            this.result = var17;
                        }
                    } else {
                        this.resultDataType = 8;
                        if(this.resultDataStyle == 3) {
                            this.result = new Double(To.toDouble(var11) - To.toDouble(var13));
                        } else {
                            var14 = (Array)this.expA.result;
                            var16 = (Array)this.expB.result;
                            var17 = new Array(8);

                            for(i1 = 0; i1 < var14.size(); ++i1) {
                                var17.addObj(new Double(To.toDouble(var14.getObj(i1)) - To.toDouble(var16.getObj(i1))));
                            }

                            this.result = var17;
                        }
                    }
                } else if(this.operator == 42) {
                    if(this.expA.resultDataType == -5 && this.expB.resultDataType == -5) {
                        this.resultDataType = -5;
                        if(this.resultDataStyle == 3) {
                            this.result = new Long(To.toLong(var11) * To.toLong(var13));
                        } else {
                            var14 = (Array)this.expA.result;
                            var16 = (Array)this.expB.result;
                            var17 = new Array(-5);

                            for(i1 = 0; i1 < var14.size(); ++i1) {
                                var17.addObj(new Long(To.toLong(var14.getObj(i1)) * To.toLong(var16.getObj(i1))));
                            }

                            this.result = var17;
                        }
                    } else {
                        this.resultDataType = 8;
                        if(this.resultDataStyle == 3) {
                            this.result = new Double(To.toDouble(var11) * To.toDouble(var13));
                        } else {
                            var14 = (Array)this.expA.result;
                            var16 = (Array)this.expB.result;
                            var17 = new Array(8);

                            for(i1 = 0; i1 < var14.size(); ++i1) {
                                var17.addObj(new Double(To.toDouble(var14.getObj(i1)) * To.toDouble(var16.getObj(i1))));
                            }

                            this.result = var17;
                        }
                    }
                } else if(this.operator == 47) {
                    this.resultDataType = 8;
                    if(this.resultDataStyle == 3 && To.toDouble(var13) == 0.0D) {
                        this.result = new Double(1.7976931348623157E308D);
                    } else if(this.resultDataStyle == 3) {
                        this.result = new Double(To.toDouble(var11) / To.toDouble(var13));
                    } else {
                        var14 = (Array)this.expA.result;
                        var16 = (Array)this.expB.result;
                        var17 = new Array(8);

                        for(i1 = 0; i1 < var14.size(); ++i1) {
                            if(To.toDouble(var16.getObj(i1)) == 0.0D) {
                                var17.addObj(new Double(0.0D));
                            } else {
                                var17.addObj(new Double(To.toDouble(var14.getObj(i1)) / To.toDouble(var16.getObj(i1))));
                            }
                        }

                        this.result = var17;
                    }
                } else if(this.operator == 37) {
                    this.resultDataType = -5;
                    if(this.resultDataStyle == 3 && To.toDouble(var13) == 0.0D) {
                        this.result = new Long(9223372036854775807L);
                    } else if(this.resultDataStyle == 3) {
                        this.result = new Long(To.toLong(var11) % To.toLong(var13));
                    } else {
                        var14 = (Array)this.expA.result;
                        var16 = (Array)this.expB.result;
                        var17 = new Array(-5);

                        for(i1 = 0; i1 < var14.size(); ++i1) {
                            if(To.toDouble(var16.getObj(i1)) == 0.0D) {
                                var17.addObj(new Long(0L));
                            } else {
                                var17.addObj(new Long(To.toLong(var14.getObj(i1)) % To.toLong(var16.getObj(i1))));
                            }
                        }

                        this.result = var17;
                    }
                } else if(this.operator == 43 || this.operator == 124) {
                    if(this.operator == 43 && this.expA.resultDataType == -5 && this.expB.resultDataType == -5) {
                        this.resultDataType = -5;
                        if(this.resultDataStyle == 3) {
                            this.result = new Long(To.toLong(var11) + To.toLong(var13));
                        } else {
                            var14 = (Array)this.expA.result;
                            var16 = (Array)this.expB.result;
                            var17 = new Array(-5);

                            for(i1 = 0; i1 < var14.size(); ++i1) {
                                var17.addObj(new Long(To.toLong(var14.getObj(i1)) + To.toLong(var16.getObj(i1))));
                            }

                            this.result = var17;
                        }
                    } else if(this.operator != 124 && this.expA.resultDataType != 12 && this.expB.resultDataType != 12) {
                        this.resultDataType = 8;
                        if(this.resultDataStyle == 3) {
                            this.result = new Double(To.toDouble(var11) + To.toDouble(var13));
                        } else {
                            var14 = (Array)this.expA.result;
                            var16 = (Array)this.expB.result;
                            var17 = new Array(8);

                            for(i1 = 0; i1 < var14.size(); ++i1) {
                                var17.addObj(new Double(To.toDouble(var14.getObj(i1)) + To.toDouble(var16.getObj(i1))));
                            }

                            this.result = var17;
                        }
                    } else {
                        this.resultDataType = 12;
                        if(this.resultDataStyle == 3) {
                            this.result = To.objToString(var11) + To.objToString(var13);
                        } else {
                            var14 = (Array)this.expA.result;
                            var16 = (Array)this.expB.result;
                            var17 = new Array();

                            for(i1 = 0; i1 < var14.size(); ++i1) {
                                var17.addObj(To.objToString(var14.getObj(i1)) + To.objToString(var16.getObj(i1)));
                            }

                            this.result = var17;
                        }
                    }
                }
            } else {
                Row var18;
                if(this.parseResType == 5) {
                    this.expA.eval(conn);
                    if(this.expA.resultDataType == 9001) {
                        var9 = (RowSet)this.expA.result;
                        if(this.resultDataType != 9001) {
                            if(var9.fieldSize() > 0) {
                                this.resultDataType = var9.fieldAt(0).type;
                                if(var9.size() > 0) {
                                    this.result = var9.getCellValue(0, 0);
                                } else {
                                    this.result = "";
                                }
                            } else {
                                this.resultDataType = 12;
                            }
                        } else {
                            this.result = var9;
                        }
                    } else if(this.expA.resultDataType == 9000) {
                        var18 = (Row)this.expA.result;
                        if(var18.fieldSize() > 0) {
                            this.resultDataType = var18.fieldAt(0).type;
                            this.result = var18.get(var18.fieldAt(0).name);
                        } else {
                            this.resultDataType = 12;
                        }
                    } else {
                        this.resultDataStyle = this.expA.resultDataStyle;
                        this.resultDataType = this.expA.resultDataType;
                        this.result = this.expA.result;
                    }
                } else if(this.parseResType == 7) {
                    if(this.data != null) {
                        var18 = (Row)this.data;

                        for(i = 0; i < var18.fieldSize(); ++i) {
                            String var19 = var18.fullFieldNameAt(i);
                            this.expA.blkExp.varMap.put(var19, var18.get(var19));
                        }
                    }

                    this.expA.eval(conn);
                    var9 = (RowSet)this.expA.result;
                    if(this.resultDataType != 9001) {
                        if(var9.fieldSize() > 0) {
                            this.resultDataType = var9.fieldAt(0).type;
                            if(var9.size() > 0) {
                                this.result = var9.getCellValue(0, 0);
                            } else {
                                this.result = "";
                            }
                        } else {
                            this.resultDataType = 12;
                        }
                    } else {
                        this.result = var9;
                    }
                }
            }
        } else {
            if(this.parseResType == 0) {
                this.resultDataType = 16;
            } else if(this.parseResType == 1) {
                this.resultDataType = -5;
            } else if(this.parseResType == 2) {
                this.resultDataType = 8;
            } else if(this.parseResType == 3) {
                this.resultDataType = 12;
            }

            if(this.dataStyle == 1) {
                this.resultDataStyle = 3;
                this.result = this.parseRes;
            } else {
                this.resultDataStyle = 4;
                int rowSet = ((RowSet)this.data).size();
                Array fieldName = new Array(this.resultDataType);

                for(i = 0; i < rowSet; ++i) {
                    fieldName.addObj(this.parseRes);
                }

                this.result = fieldName;
            }
        }

    }

    private static int getDataType(Object result) {
        short type = 12;
        if(result instanceof Double) {
            type = 8;
        } else if(result instanceof Long) {
            type = -5;
        } else if(result instanceof Boolean) {
            type = 16;
        } else if(result instanceof Row) {
            type = 9000;
        } else if(result instanceof RowSet) {
            type = 9001;
        }

        return type;
    }

    private Object getByVar(String str) {
        return this.blkExp != null?this.blkExp.getValue(str):null;
    }

    public void compress() throws HgException {
        Object obj = null;
        Array objs = (Array)this.result;
        if(objs.size() > 0) {
            obj = objs.getObj(0);
        }

        for(int i = 1; i < objs.size(); ++i) {
            if(!objs.getObj(i).equals(obj)) {
                obj = "";
                break;
            }
        }

        this.result = obj;
        this.resultDataStyle = 3;
    }

    public ArrayList getChildExpList(Conn conn) throws HgException {
        ArrayList list = new ArrayList();
        if(!this.parsed) {
            this.parse();
        }

        if(this.expA != null) {
            list.add(this.expA);
        }

        if(this.expB != null) {
            list.add(this.expB);
        }

        return list;
    }

    public boolean isConst() {
        return this.parseResType == 0 || this.parseResType == 1 || this.parseResType == 2 || this.parseResType == 3;
    }

    public void fillVarList(ArrayList list) throws HgException {
        if(!this.parsed) {
            this.parse();
        }

        if(this.parseResType == 4) {
            if(!list.contains(this.parseRes)) {
                list.add(this.parseRes);
            }
        } else if(this.parseResType == 6) {
            ((MathExpression)this.expA).fillVarList(list);
            ((MathExpression)this.expB).fillVarList(list);
        }

    }

    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        } else {
            if(obj instanceof MathExpression) {
                MathExpression mathExp = (MathExpression)obj;
                if(!mathExp.parsed) {
                    try {
                        mathExp.parse();
                    } catch (HgException var6) {
                        ;
                    }
                }

                if(!this.parsed) {
                    try {
                        this.parse();
                    } catch (HgException var5) {
                        ;
                    }
                }

                if(mathExp.parseResType == this.parseResType) {
                    if(this.parseResType == 0 || this.parseResType == 1 || this.parseResType == 2 || this.parseResType == 3) {
                        return mathExp.parseRes.equals(this.parseRes);
                    }

                    if(this.parseResType == 4) {
                        String str1 = (String)this.parseRes;
                        String str2 = (String)mathExp.parseRes;
                        if(!str1.equals(str2) && !str1.endsWith("." + str2) && !str2.endsWith("." + str1)) {
                            return false;
                        }

                        return true;
                    }

                    if(this.parseResType == 6) {
                        if(this.expA.equals(mathExp.expA) && this.expB.equals(mathExp.expB)) {
                            return true;
                        }

                        return false;
                    }

                    if(this.parseResType == 5) {
                        return this.expA.equals(mathExp.expA);
                    }
                }
            }

            return false;
        }
    }

    public void ensureDataType(Row row) throws HgException {
        if(!this.parsed) {
            this.parse();
        }

        this.resultDataType = 12;
        if(this.parseResType != 0 && this.parseResType != 1 && this.parseResType != 2 && this.parseResType != 3) {
            if(this.parseResType == 4) {
                Field field = null;
                field = row.getField((String)this.parseRes);
                if(field != null) {
                    this.resultDataType = field.type;
                }
            } else if(this.parseResType == 6) {
                if(this.operator == 45) {
                    if(this.expA.resultDataType == -5 && this.expB.resultDataType == -5) {
                        this.resultDataType = -5;
                    } else {
                        this.resultDataType = 8;
                    }
                } else if(this.operator == 42) {
                    if(this.expA.resultDataType == -5 && this.expB.resultDataType == -5) {
                        this.resultDataType = -5;
                    } else {
                        this.resultDataType = 8;
                    }
                } else if(this.operator == 47) {
                    this.resultDataType = 8;
                } else if(this.operator == 43 || this.operator == 124) {
                    if(this.operator == 43 && this.expA.resultDataType == -5 && this.expB.resultDataType == -5) {
                        this.resultDataType = -5;
                    } else if(this.operator != 124 && this.expA.resultDataType != 12 && this.expB.resultDataType != 12) {
                        this.resultDataType = 8;
                    } else {
                        this.resultDataType = 12;
                    }
                }
            }
        } else if(this.parseResType == 0) {
            this.resultDataType = 16;
        } else if(this.parseResType == 1) {
            this.resultDataType = -5;
        } else if(this.parseResType == 2) {
            this.resultDataType = 8;
        } else if(this.parseResType == 3) {
            this.resultDataType = 12;
        }

    }
}
