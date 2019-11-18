//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iyin.sign.system.util.picUtils.doc;

import com.iyin.sign.system.util.picUtils.util.MapUtil;
import com.iyin.sign.system.util.picUtils.util.StrUtil;
import com.iyin.sign.system.util.picUtils.util.To;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.HashMap;

public class EleTable extends EleRect {
    public boolean input;
    public int header;
    public int footer;
    public String data;
    public String tarType;
    public int rowCount;
    public String rows;
    public String cols;

    public EleTable(XDoc xdoc, HashMap attMap) {
        super(xdoc, attMap);
    }

    public EleTable(XDoc xdoc) {
        super(xdoc);
    }

    protected void init() {
        super.init();
        this.typeName = "table";
        this.rows = "";
        this.cols = "";
        this.color = null;
        this.input = false;
        this.header = 0;
        this.footer = 0;
        this.data = "";
        this.rowCount = 0;
        this.sizeType = "autosize";
        this.tarType = "table";
    }

    public void setRC(int rs, int cs) {
        this.setRC(rs, cs, 24, 96);
    }

    protected void ensureRC() {
        int row = 1;
        int col = 1;

        for(int i = 0; i < this.eleList.size(); ++i) {
            if(this.eleList.get(i) instanceof EleCell) {
                EleCell cell = (EleCell)this.eleList.get(i);
                if(cell.row < 0) {
                    cell.row = 0;
                }

                if(cell.rowSpan <= 0) {
                    cell.rowSpan = 1;
                }

                if(cell.col < 0) {
                    cell.col = 0;
                }

                if(cell.colSpan <= 0) {
                    cell.colSpan = 1;
                }

                if(cell.col + cell.colSpan > col) {
                    col = cell.col + cell.colSpan;
                }

                if(cell.row + cell.rowSpan > row) {
                    row = cell.row + cell.rowSpan;
                }
            }
        }

        this.rows = this.ensure(this.rows, row, 24);
        this.cols = this.ensure(this.cols, col, 96);
    }

    private String ensure(String str, int n, int def) {
        String[] strs = str.split(",");
        if(strs.length >= n) {
            return str;
        } else {
            StringBuffer sb = new StringBuffer();

            for(int i = 0; i < n; ++i) {
                if(i > 0) {
                    sb.append(",");
                }

                if(i < strs.length && strs[i].length() > 0) {
                    sb.append(strs[i]);
                } else {
                    sb.append(def);
                }
            }

            return sb.toString();
        }
    }

    public void setRC(int rs, int cs, int h, int w) {
        StringBuffer sb = new StringBuffer();

        int i;
        for(i = 0; i < rs; ++i) {
            if(i > 0) {
                sb.append(",");
            }

            sb.append(h);
        }

        this.rows = sb.toString();
        sb.setLength(0);

        for(i = 0; i < cs; ++i) {
            if(i > 0) {
                sb.append(",");
            }

            sb.append(w);
        }

        this.cols = sb.toString();
    }

    public void setAttMap(HashMap map) {
        super.setAttMap(map);
        this.rows = MapUtil.getString(map, "rows", this.rows);
        this.cols = MapUtil.getString(map, "cols", this.cols);
        this.header = MapUtil.getInt(map, "header", this.header);
        this.footer = MapUtil.getInt(map, "footer", this.footer);
        this.input = MapUtil.getBool(map, "input", this.input);
        this.data = MapUtil.getString(map, "data", this.data);
        this.tarType = MapUtil.getString(map, "tarType", this.tarType);
        this.rowCount = MapUtil.getInt(map, "rowCount", this.rowCount);
    }

    public HashMap getAttMap() {
        HashMap map = super.getAttMap();
        map.put("rows", this.rows);
        map.put("cols", this.cols);
        map.put("header", String.valueOf(this.header));
        map.put("footer", String.valueOf(this.footer));
        map.put("input", String.valueOf(this.input));
        map.put("data", this.data);
        map.put("tarType", this.tarType);
        map.put("rowCount", String.valueOf(this.rowCount));
        return map;
    }

    public void autoSize() {
        if(!this.sizeType.equals("normal")) {
            this.ensureRC();
            String[] rowStrs = this.rows.split(",");
            String[] colStrs = this.cols.split(",");
            int[] rs = new int[rowStrs.length];
            int[] cs = new int[colStrs.length];

            int cell;
            for(cell = 0; cell < cs.length; ++cell) {
                cs[cell] = To.toInt(colStrs[cell]);
            }

            for(cell = 0; cell < rs.length; ++cell) {
                rs[cell] = To.toInt(rowStrs[cell]);
            }

            int sb;
            int n;
            EleCell var9;
            for(sb = 0; sb < this.eleList.size(); ++sb) {
                var9 = (EleCell)this.eleList.get(sb);
                if(var9.col < cs.length && var9.row < rs.length) {
                    var9.getRect().width = cs[var9.col];
                    EleRect var10000;
                    if(var9.colSpan > 1) {
                        for(n = 1; n < var9.colSpan && n < cs.length; ++n) {
                            var10000 = var9.getRect();
                            var10000.width += cs[n];
                        }
                    }

                    var9.getRect().height = rs[var9.row];
                    if(var9.rowSpan > 1) {
                        for(n = 1; n < var9.rowSpan && n < rs.length; ++n) {
                            var10000 = var9.getRect();
                            var10000.height += rs[n];
                        }
                    }

                    var9.autoSize();
                }
            }

            int i;
            StringBuffer var10;
            if(this.sizeType.equals("autosize") || this.sizeType.equals("autowidth")) {
                for(sb = 0; sb < cs.length; ++sb) {
                    cs[sb] = 8;
                }

                for(sb = 0; sb < this.eleList.size(); ++sb) {
                    var9 = (EleCell)this.eleList.get(sb);
                    if(var9.col < cs.length && var9.row < rs.length && var9.colSpan <= 1) {
                        cs[var9.col] = Math.max(cs[var9.col], var9.width);
                    }
                }

                var10 = new StringBuffer();
                n = 0;

                for(i = 0; i < cs.length; ++i) {
                    if(i > 0) {
                        var10.append(",");
                    }

                    var10.append(cs[i]);
                    n += cs[i];
                }

                this.width = n + (int)this.strokeWidth * 2 + this.margin * 2;
                this.cols = var10.toString();
            }

            if(this.sizeType.equals("autosize") || this.sizeType.equals("autoheight")) {
                for(sb = 0; sb < rs.length; ++sb) {
                    rs[sb] = 24;
                }

                for(sb = 0; sb < this.eleList.size(); ++sb) {
                    var9 = (EleCell)this.eleList.get(sb);
                    if(var9.col < cs.length && var9.row < rs.length && var9.rowSpan <= 1) {
                        rs[var9.row] = Math.max(rs[var9.row], var9.height);
                    }
                }

                var10 = new StringBuffer();
                n = 0;

                for(i = 0; i < rs.length; ++i) {
                    if(i > 0) {
                        var10.append(",");
                    }

                    var10.append(rs[i]);
                    n += rs[i];
                }

                this.height = n + (int)this.strokeWidth * 2 + this.margin * 2;
                this.rows = var10.toString();
            }
        }

    }

    public Object clone() {
        return new EleTable(this.xdoc, this.getAttMap());
    }

    public Rectangle getCellRect(int[] rs, int[] cs, EleCell[][] cells, int row, int col) {
        Rectangle rect = new Rectangle();
        boolean x = false;
        boolean y = false;
        int w = 0;
        int h = 0;
        int var12 = this.margin + (int)this.strokeWidth;
        int var13 = this.margin + (int)this.strokeWidth;

        int i;
        for(i = 0; i < row; ++i) {
            var13 += rs[i];
        }

        for(i = 0; i < col; ++i) {
            var12 += cs[i];
        }

        for(i = 0; i < cells[row][col].rowSpan; ++i) {
            if(row + i < rs.length) {
                h += rs[row + i];
            }
        }

        for(i = 0; i < cells[row][col].colSpan; ++i) {
            if(col + i < cs.length) {
                w += cs[col + i];
            }
        }

        rect.setFrame((double)var12, (double)var13, (double)w, (double)h);
        return rect;
    }

    public void print(Graphics2D g) {
        if(this.isVisible()) {
            this.print(g, 0, this.height);
        }

    }

    public void checkRC() {
        if(this.rows.length() == 0 || this.cols.length() == 0) {
            int rs = 0;
            int cs = 0;

            for(int i = 0; i < this.eleList.size(); ++i) {
                EleCell cell = (EleCell)this.eleList.get(i);
                if(cell.row + cell.rowSpan > rs) {
                    rs = cell.row + cell.rowSpan;
                }

                if(cell.col + cell.colSpan > cs) {
                    cs = cell.col + cell.colSpan;
                }
            }

            this.setRC(rs, cs);
        }

    }

    public void print(Graphics2D g, int pf, int ph) {
        Shape shape = this.fillShape(g);
        this.checkRC();
        String[] rowStrs = this.rows.split(",");
        String[] colStrs = this.cols.split(",");
        int[] rs = new int[rowStrs.length];
        int[] cs = new int[colStrs.length];

        int cw;
        for(cw = 0; cw < rs.length; ++cw) {
            rs[cw] = To.toInt(rowStrs[cw], 24);
        }

        for(cw = 0; cw < cs.length; ++cw) {
            cs[cw] = To.toInt(colStrs[cw], 96);
        }

        cw = 0;
        int ch = 0;

        int ih;
        for(ih = 0; ih < cs.length; ++ih) {
            cw += cs[ih];
        }

        for(ih = 0; ih < rs.length; ++ih) {
            ch += rs[ih];
        }

        ih = this.height - this.margin * 2 - (int)this.strokeWidth * 2;
        int iw = this.width - this.margin * 2 - (int)this.strokeWidth * 2;

        int cells;
        for(cells = 0; cells < rs.length; ++cells) {
            rs[cells] = (int)((double)rs[cells] / (double)ch * (double)ih);
        }

        for(cells = 0; cells < cs.length; ++cells) {
            cs[cells] = (int)((double)cs[cells] / (double)cw * (double)iw);
        }

        cw = 0;
        ch = 0;

        for(cells = 0; cells < cs.length; ++cells) {
            if(cs[cells] < 0) {
                cs[cells] = 0;
            }

            cw += cs[cells];
        }

        for(cells = 0; cells < rs.length; ++cells) {
            if(rs[cells] < 0) {
                rs[cells] = 0;
            }

            ch += rs[cells];
        }

        rs[rs.length - 1] += ih - ch;
        if(rs[rs.length - 1] < 0) {
            rs[rs.length - 1] = 0;
        }

        cs[cs.length - 1] += iw - cw;
        if(cs[cs.length - 1] < 0) {
            cs[cs.length - 1] = 0;
        }

        EleCell[][] var23 = new EleCell[rs.length][cs.length];

        EleCell cell;
        for(int cellRect = 0; cellRect < this.eleList.size(); ++cellRect) {
            if(this.eleList.get(cellRect) instanceof EleCell) {
                cell = (EleCell)this.eleList.get(cellRect);
                if(cell.row < rs.length && cell.col < cs.length) {
                    var23[cell.row][cell.col] = cell;
                    cell.getRect().width = cs[cell.col];
                    cell.getRect().height = rs[cell.row];
                }
            }
        }

        this.setMerge(rs, cs, var23);
        int ncount = 0;
        int inputRow = 1;
        if(this.header > 1) {
            inputRow = this.header;
        }

        for(int i = 0; i < rs.length; ++i) {
            if(!this.input || i != inputRow) {
                if(ncount >= pf && ncount + rs[i] <= pf + ph) {
                    for(int j = 0; j < cs.length; ++j) {
                        if(var23[i][j] != null && var23[i][j].belong == null) {
                            Rectangle var24 = this.getCellRect(rs, cs, var23, i, j);
                            cell = var23[i][j];
                            cell.setBounds(var24.x, var24.y, var24.width, var24.height);
                            Graphics2D cg;
                            if(cell.rotate == 0) {
                                cg = (Graphics2D)g.create(var24.x, var24.y, var24.width + 1, var24.height + 1);
                            } else {
                                int d = (int) Math.ceil(Math.pow(Math.pow((double)cell.width, 2.0D) + Math.pow((double)cell.height, 2.0D), 0.5D));
                                cg = (Graphics2D)g.create(cell.left - (d - cell.width) / 2, cell.top - (d - cell.height) / 2, d, d);
                                AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians((double)cell.rotate), (double)(d / 2), (double)(d / 2));
                                cg.transform(at);
                                cg.translate((d - cell.width) / 2, (d - cell.height) / 2);
                            }

                            cell.print(cg);
                            cg.dispose();
                        }
                    }
                }

                ncount += rs[i];
            }
        }

        this.drawShape(g, shape);
    }

    private void setMerge(int[] rs, int[] cs, EleCell[][] cells) {
        for(int i = 0; i < rs.length && i < cells.length; ++i) {
            for(int j = 0; j < cs.length && j < cells[i].length; ++j) {
                EleCell cell = cells[i][j];
                if(cell != null && cell.belong == null && (cell.rowSpan > 1 || cell.colSpan > 1)) {
                    for(int m = 0; m < cell.rowSpan; ++m) {
                        for(int n = 0; n < cell.colSpan; ++n) {
                            if(i + m < cells.length && j + n < cells[i + m].length && cells[i + m][j + n] != null) {
                                cells[i + m][j + n].belong = cell;
                            }
                        }
                    }

                    cell.belong = null;
                }
            }
        }

    }

    public EleCell getCell(int row, int col) {
        EleCell cell = null;

        for(int i = 0; i < this.eleList.size(); ++i) {
            cell = (EleCell)this.eleList.get(i);
            if(cell.row == row && cell.col == col) {
                break;
            }

            cell = null;
        }

        return cell;
    }

    public void adjustWidth() {
        if(this.width > this.xdoc.getPaper().getContentWidth()) {
            this.width = this.xdoc.getPaper().getContentWidth();
        }

    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        this.checkRC();
        String[] rowStrs = this.rows.split(",");
        String[] colStrs = this.cols.split(",");
        int[] rs = new int[rowStrs.length];
        int[] cs = new int[colStrs.length];

        int cw;
        for(cw = 0; cw < rs.length; ++cw) {
            rs[cw] = To.toInt(rowStrs[cw], 24);
        }

        for(cw = 0; cw < cs.length; ++cw) {
            cs[cw] = To.toInt(colStrs[cw], 96);
        }

        cw = 0;
        int ch = 0;

        int ih;
        for(ih = 0; ih < cs.length; ++ih) {
            cw += cs[ih];
        }

        for(ih = 0; ih < rs.length; ++ih) {
            ch += rs[ih];
        }

        ih = this.height - this.margin * 2 - (int)this.strokeWidth * 2;
        int iw = this.width - this.margin * 2 - (int)this.strokeWidth * 2;

        int cells;
        for(cells = 0; cells < rs.length; ++cells) {
            rs[cells] = (int)((double)rs[cells] / (double)ch * (double)ih);
        }

        for(cells = 0; cells < cs.length; ++cells) {
            cs[cells] = (int)((double)cs[cells] / (double)cw * (double)iw);
        }

        cw = 0;
        ch = 0;

        for(cells = 0; cells < cs.length; ++cells) {
            if(cs[cells] < 0) {
                cs[cells] = 0;
            }

            cw += cs[cells];
        }

        for(cells = 0; cells < rs.length; ++cells) {
            if(rs[cells] < 0) {
                rs[cells] = 0;
            }

            ch += rs[cells];
        }

        rs[rs.length - 1] += ih - ch;
        if(rs[rs.length - 1] < 0) {
            rs[rs.length - 1] = 0;
        }

        cs[cs.length - 1] += iw - cw;
        if(cs[cs.length - 1] < 0) {
            cs[cs.length - 1] = 0;
        }

        EleCell[][] var14 = new EleCell[rs.length][cs.length];

        int i;
        for(i = 0; i < this.eleList.size(); ++i) {
            if(this.eleList.get(i) instanceof EleCell) {
                EleCell cell = (EleCell)this.eleList.get(i);
                if(cell.row < rs.length && cell.col < cs.length) {
                    var14[cell.row][cell.col] = cell;
                    cell.getRect().width = cs[cell.col];
                    cell.getRect().height = rs[cell.row];
                }
            }
        }

        for(i = 0; i < rs.length; ++i) {
            for(int j = 0; j < cs.length; ++j) {
                if(var14[i][j] != null) {
                    sb.append(StrUtil.csvEncode(var14[i][j].toString()));
                }

                if(j < cs.length - 1) {
                    sb.append(",");
                }
            }

            if(i < rs.length - 1) {
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    public void setText(String text) {
        Object base = null;
        if(this.eleList.size() > 0) {
            base = (EleBase)this.eleList.get(0);
        } else {
            EleCell cell = new EleCell(this.xdoc);
            cell.row = 1;
            cell.col = 1;
            base = cell;
        }

        ((EleBase)base).setText(text);
    }
}
