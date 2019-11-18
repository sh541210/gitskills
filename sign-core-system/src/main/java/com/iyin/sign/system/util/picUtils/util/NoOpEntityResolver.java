/*     */ package com.iyin.sign.system.util.picUtils.util;
/*     */ 
/*     */

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import java.io.StringReader;

/*     */
/*     */

/*     */
/*     */ class NoOpEntityResolver
/*     */   implements EntityResolver
/*     */ {
/* 319 */   public static NoOpEntityResolver er = new NoOpEntityResolver();
/*     */ 
/* 321 */   public InputSource resolveEntity(String publicId, String systemId) { return new InputSource(new StringReader(""));
/*     */   }
/*     */ }

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.util.NoOpEntityResolver
 * JD-Core Version:    0.6.0
 */