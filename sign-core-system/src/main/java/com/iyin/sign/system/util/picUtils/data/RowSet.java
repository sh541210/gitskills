/*     */ package com.iyin.sign.system.util.picUtils.data;
/*     */ 
/*     */



import com.iyin.sign.system.util.picUtils.util.To;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*     */
/*     */
/*     */

/*     */
/*     */ public class RowSet
/*     */ {
/*  31 */   public String alise = "";
/*     */   public int rowno;
/*     */   private ArrayList fields;
/* 104 */   private List data = new ArrayList();
/*     */ 
/*     */   public Field fieldAt(int index)
/*     */   {
/*  46 */     return (Field)this.fields.get(index);
/*     */   }
/*     */ 
/*     */   public int fieldSize()
/*     */   {
/*  53 */     return this.fields.size();
/*     */   }
/*     */ 
/*     */   public int fieldIndex(String fieldName)
/*     */   {
/*  63 */     for (int i = 0; i < fieldSize(); i++) {
/*  64 */       String name = (this.alise.length() > 0 ? this.alise + "." : "") + fieldAt(i).name;
/*  65 */       if ((name.equalsIgnoreCase(fieldName)) || (
/*  66 */         (name.indexOf(".") >= 0) && ((name.endsWith('.' + fieldName)) || (name.toLowerCase().endsWith('.' + fieldName.toLowerCase()))))) {
/*  67 */         return i;
/*     */       }
/*     */     }
/*  70 */     return -1;
/*     */   }
/*     */ 
/*     */   public Field getField(String fieldName)
/*     */   {
/*  78 */     int n = fieldIndex(fieldName);
/*  79 */     return n >= 0 ? fieldAt(n) : null;
/*     */   }
/*     */ 
/*     */   public RowSet()
/*     */   {
/*  86 */     this(new ArrayList());
/*     */   }
/*     */ 
/*     */   public RowSet(ArrayList fields)
/*     */   {
/*  93 */     this.fields = fields;
/*     */   }
/*     */ 
/*     */   public RowSet(ArrayList fields, String fileName)
/*     */   {
/* 102 */     this(fields);
/*     */   }
/*     */ 
/*     */   private Object[] defRowData()
/*     */   {
/* 110 */     Object[] data = new Object[fieldSize()];
/*     */ 
/* 112 */     for (int i = 0; i < fieldSize(); i++) {
/* 113 */       Field field = fieldAt(i);
/* 114 */       if (field.type == 16)
/* 115 */         data[i] = new Boolean(false);
/* 116 */       else if (field.type == 5)
/* 117 */         data[i] = new Short((short) 0);
/* 118 */       else if (field.type == 4)
/* 119 */         data[i] = new Integer(0);
/* 120 */       else if (field.type == -5)
/* 121 */         data[i] = new Long(0L);
/* 122 */       else if (field.type == 6)
/* 123 */         data[i] = new Float(0.0F);
/* 124 */       else if (field.type == 91)
/* 125 */         data[i] = new Date();
/* 126 */       else if (field.type == 8)
/* 127 */         data[i] = new Double(0.0D);
/* 128 */       else if (field.type == 9002)
/* 129 */         data[i] = new IntArray();
/*     */       else {
/* 131 */         data[i] = "";
/*     */       }
/*     */     }
/* 134 */     return data;
/*     */   }
/*     */ 
/*     */   private Row dataToRow(Object[] data)
/*     */   {
/* 142 */     return new Row(this, this.fields, data);
/*     */   }
/*     */ 
/*     */   public Row add()
/*     */   {
/* 150 */     return add(size(), null);
/*     */   }
/*     */ 
/*     */   public Row add(int index)
/*     */   {
/* 158 */     return add(index, null);
/*     */   }
/*     */ 
/*     */   public Row add(Row row)
/*     */   {
/* 166 */     return add(size(), row);
/*     */   }
/*     */ 
/*     */   public Row add(int index, Row row)
/*     */   {
/* 175 */     return dataToRow((Object[])addObj(index, row != null ? row.data : null));
/*     */   }
/*     */ 
/*     */   public Row set(int index, Row row)
/*     */   {
/* 184 */     return dataToRow((Object[])setObj(index, row != null ? row.data : null));
/*     */   }
/*     */   public void addObj(Object obj) {
/* 187 */     addObj(size(), obj);
/*     */   }
/*     */   public Object addObj(int index, Object obj) {
/* 190 */     obj = validate(obj);
/* 191 */     this.data.add(index, obj);
/* 192 */     return obj;
/*     */   }
/*     */   private Object setObj(int index, Object obj) {
/* 195 */     obj = validate(obj);
/* 196 */     this.data.set(index, obj);
/* 197 */     return obj;
/*     */   }
/*     */ 
/*     */   public void addAll(RowSet tmpSet)
/*     */   {
/* 205 */     for (int i = 0; i < tmpSet.size(); i++)
/* 206 */       addObj(tmpSet.getObj(i));
/*     */   }
/*     */ 
/*     */   public Object getObj(int index) {
/* 210 */     return this.data.get(index);
/*     */   }
/*     */ 
/*     */   public Row get(int rowIndex)
/*     */   {
/* 219 */     return dataToRow((Object[])getObj(rowIndex));
/*     */   }
/*     */ 
/*     */   public Object[] getData(int rowIndex)
/*     */   {
/* 228 */     return (Object[])getObj(rowIndex);
/*     */   }
/*     */ 
/*     */   public Object getCellValue(int rowIndex, int colIndex)
/*     */   {
/* 238 */     return ((Object[])getObj(rowIndex))[colIndex];
/*     */   }
/*     */ 
/*     */   public void setCellValue(int rowIndex, int colIndex, Object obj)
/*     */   {
/* 248 */     ((Object[])getObj(rowIndex))[colIndex] = To.toObj(obj, fieldAt(colIndex).type);
/*     */   }
/*     */ 
/*     */   private Object validate(Object obj)
/*     */   {
/* 256 */     Object[] data = defRowData();
/* 257 */     if ((obj != null) && ((obj instanceof Object[]))) {
/* 258 */       Object[] data2 = (Object[])obj;
/* 259 */       for (int i = 0; (i < data.length) && (i < data2.length); i++) {
/* 260 */         data[i] = To.toObj(data2[i], fieldAt(i).type);
/*     */       }
/*     */     }
/* 263 */     return data;
/*     */   }
/*     */   public int size() {
/* 266 */     return this.data.size();
/*     */   }
/*     */ 
/*     */   public Row createRow()
/*     */   {
/* 273 */     return dataToRow(defRowData());
/*     */   }
/*     */ 
/*     */   public RowSet createRowSet()
/*     */   {
/* 280 */     RowSet rowSet = new RowSet(this.fields);
/* 281 */     rowSet.alise = this.alise;
/* 282 */     return rowSet;
/*     */   }
/*     */   public ArrayList toList() {
/* 285 */     ArrayList list = new ArrayList();
/* 286 */     for (int i = 0; i < size(); i++) {
/* 287 */       list.add(get(i).toMap());
/*     */     }
/* 289 */     return list;
/*     */   }
/*     */   public String toString() {
/* 292 */     return "ROWSET";
/*     */   }
/*     */   public void remove(int index) {
/* 295 */     this.data.remove(index);
/*     */   }
/*     */   public void clear() {
/* 298 */     this.data.clear();
/*     */   }
/*     */ 
/*     */   protected void setDataChange()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void writeData()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void dropFully()
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.data.RowSet
 * JD-Core Version:    0.6.0
 */