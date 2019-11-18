/*     */ package com.iyin.sign.system.util.picUtils.data;
/*     */


import com.iyin.sign.system.util.picUtils.util.To;

import java.util.*;

/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ 
/*     */ public class Row
/*     */ {
/*     */   private RowSet rowSet;
/*     */   private ArrayList fields;
/*     */   public int rowno;
/*     */   public Object[] data;
/*     */ 
/*     */   public Field fieldAt(int index)
/*     */   {
/*  50 */     return (Field)this.fields.get(index);
/*     */   }
/*     */ 
/*     */   public String fullFieldNameAt(int index)
/*     */   {
/*  58 */     String name = fieldAt(index).name;
/*  59 */     if ((this.rowSet != null) && (this.rowSet.alise.length() > 0)) {
/*  60 */       name = this.rowSet.alise + "." + name;
/*     */     }
/*  62 */     return name;
/*     */   }
/*     */ 
/*     */   public int fieldSize()
/*     */   {
/*  69 */     return this.fields.size();
/*     */   }
/*     */ 
/*     */   public int fieldIndex(String fieldName)
/*     */   {
/*  77 */     if (this.rowSet != null) {
/*  78 */       return this.rowSet.fieldIndex(fieldName);
/*     */     }
/*     */ 
/*  81 */     for (int i = 0; i < fieldSize(); i++) {
/*  82 */       String name = fieldAt(i).name;
/*  83 */       if ((name.equalsIgnoreCase(fieldName)) || (
/*  84 */         (name.indexOf(".") >= 0) && ((name.endsWith('.' + fieldName)) || (name.toLowerCase().endsWith('.' + fieldName.toLowerCase()))))) {
/*  85 */         return i;
/*     */       }
/*     */     }
/*  88 */     return -1;
/*     */   }
/*     */ 
/*     */   public Field getField(String fieldName)
/*     */   {
/*  97 */     int n = fieldIndex(fieldName);
/*  98 */     return n >= 0 ? fieldAt(n) : null;
/*     */   }
/*     */ 
/*     */   public Row(Map map)
/*     */   {
/* 104 */     this.fields = new ArrayList();
/* 105 */     this.data = new Object[map.size()];
/*     */ 
/* 107 */     int n = 0;
/* 108 */     for (Iterator i = map.keySet().iterator(); i.hasNext(); ) {
/* 109 */       String key = (String)i.next();
/* 110 */       this.data[n] = map.get(key);
/* 111 */       this.fields.add(new Field(key, DbTypes.getObjType(this.data[n])));
/* 112 */       n++;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Row(ArrayList fields)
/*     */   {
/* 121 */     this.fields = fields;
/* 122 */     this.data = defRowData();
/*     */   }
/*     */ 
/*     */   public Row(RowSet rowSet, ArrayList fields, Object[] data)
/*     */   {
/* 129 */     this.rowSet = rowSet;
/* 130 */     this.fields = fields;
/* 131 */     this.data = data;
/*     */   }
/*     */ 
/*     */   public HashMap toMap()
/*     */   {
/* 138 */     HashMap map = new HashMap();
/* 139 */     for (int i = 0; i < this.fields.size(); i++) {
/* 140 */       map.put(((Field)this.fields.get(i)).name, this.data[i]);
/*     */     }
/* 142 */     return map;
/*     */   }
/*     */ 
/*     */   private Object[] defRowData()
/*     */   {
/* 149 */     Object[] data = new Object[fieldSize()];
/*     */ 
/* 151 */     for (int i = 0; i < fieldSize(); i++) {
/* 152 */       Field field = fieldAt(i);
/* 153 */       if (field.type == 16)
/* 154 */         data[i] = new Boolean(false);
/* 155 */       else if (field.type == 5)
/* 156 */         data[i] = new Short((short) 0);
/* 157 */       else if (field.type == 4)
/* 158 */         data[i] = new Integer(0);
/* 159 */       else if (field.type == -5)
/* 160 */         data[i] = new Long(0L);
/* 161 */       else if (field.type == 6)
/* 162 */         data[i] = new Float(0.0F);
/* 163 */       else if (field.type == 91)
/* 164 */         data[i] = new Date();
/* 165 */       else if (field.type == 8)
/* 166 */         data[i] = new Double(0.0D);
/* 167 */       else if (field.type == 2000)
/* 168 */         data[i] = new Object();
/*     */       else {
/* 170 */         data[i] = "";
/*     */       }
/*     */     }
/* 173 */     return data;
/*     */   }
/*     */ 
/*     */   public Object get(String key)
/*     */   {
/* 185 */     return get(key, null);
/*     */   }
/*     */ 
/*     */   public Object get(String key, Object def)
/*     */   {
/* 193 */     Object val = null;
/* 194 */     int index = fieldIndex(key);
/* 195 */     if (index >= 0) {
/* 196 */       val = this.data[index];
/*     */     }
/* 198 */     return val == null ? def : val;
/*     */   }
/*     */ 
/*     */   public void set(String key, Object val)
/*     */   {
/* 206 */     int index = fieldIndex(key);
/* 207 */     if (index >= 0)
/* 208 */       set(index, val);
/*     */   }
/*     */ 
/*     */   public Object get(int index)
/*     */   {
/* 218 */     return this.data[index];
/*     */   }
/*     */ 
/*     */   public Object get(int index, Object def)
/*     */   {
/* 227 */     return this.data[index] == null ? def : this.data[index];
/*     */   }
/*     */ 
/*     */   public void set(int index, Object val)
/*     */   {
/* 235 */     this.data[index] = To.toObj(val, ((Field)this.fields.get(index)).type);
/* 236 */     if (this.rowSet != null)
/* 237 */       this.rowSet.setDataChange();
/*     */   }
/*     */ 
/*     */   public String getString(String key) {
/* 241 */     return getString(key, null);
/*     */   }
/*     */   public String getString(String key, String def) {
/* 244 */     Object obj = get(key);
/* 245 */     if (obj != null) {
/* 246 */       return To.objToString(obj);
/*     */     }
/* 248 */     return def;
/*     */   }
/*     */ 
/*     */   public long getLong(String key) {
/* 252 */     return getLong(key, 0L);
/*     */   }
/*     */   public long getLong(String key, long def) {
/* 255 */     Object obj = get(key);
/* 256 */     if (obj != null) {
/* 257 */       if ((obj instanceof Long))
/* 258 */         return ((Long)obj).longValue();
/* 259 */       if ((obj instanceof String)) {
/*     */         try {
/* 261 */           return Long.parseLong((String)obj);
/*     */         } catch (Exception e) {
/* 263 */           return def;
/*     */         }
/*     */       }
/* 266 */       return def;
/*     */     }
/*     */ 
/* 269 */     return def;
/*     */   }
/*     */ 
/*     */   public int getInt(String key) {
/* 273 */     return getInt(key, 0);
/*     */   }
/*     */   public int getInt(String key, int def) {
/* 276 */     return (int)getLong(key, def);
/*     */   }
/*     */ 
/*     */   public void setData(Map map) {
/* 280 */     for (Iterator it = map.keySet().iterator(); it.hasNext(); ) {
/* 281 */       String key = (String)it.next();
/* 282 */       set(key, map.get(key));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void clear() {
/* 286 */     this.data = defRowData();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 292 */     StringBuffer sb = new StringBuffer();
/*     */ 
/* 294 */     for (int i = 0; i < this.fields.size(); i++) {
/* 295 */       Field field = (Field)this.fields.get(i);
/* 296 */       if (i > 0) {
/* 297 */         sb.append(",");
/*     */       }
/* 299 */       sb.append(field.getShortName()).append("=").append(getString(field.name, ""));
/*     */     }
/* 301 */     return sb.toString();
/*     */   }
/*     */ }

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.data.Row
 * JD-Core Version:    0.6.0
 */