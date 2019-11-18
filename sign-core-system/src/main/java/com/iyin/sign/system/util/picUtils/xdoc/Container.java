/*    */ package com.iyin.sign.system.util.picUtils.xdoc;
/*    */ 
/*    */

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

/*    */
/*    */
/*    */

/*    */
/*    */ public class Container extends Component
/*    */ {
/* 38 */   private List comps = new ArrayList();
/*    */ 
/*    */   protected Container(String tagName)
/*    */   {
/* 33 */     super(tagName);
/*    */   }
/*    */ 
/*    */   public void add(Component comp)
/*    */   {
/* 44 */     this.comps.add(comp);
/*    */   }
/*    */ 
/*    */   public List getComps()
/*    */   {
/* 51 */     return this.comps;
/*    */   }
/*    */ 
/*    */   public Element toElement(Document doc)
/*    */   {
/* 57 */     Element ele = super.toElement(doc);
/*    */ 
/* 59 */     for (int i = 0; i < this.comps.size(); i++) {
/* 60 */       Component comp = (Component)this.comps.get(i);
/* 61 */       ele.appendChild(comp.toElement(doc));
/*    */     }
/* 63 */     return ele;
/*    */   }
/*    */ }

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.xdoc.Container
 * JD-Core Version:    0.6.0
 */