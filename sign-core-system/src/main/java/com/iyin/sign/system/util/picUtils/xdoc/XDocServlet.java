package com.iyin.sign.system.util.picUtils.xdoc;///*     */ package com.cyyz.eseal.sign.util.sealpic.com.hg.xdoc;
///*     */
///*     */ import com.cyyz.eseal.sign.util.sealpic.com.hg.doc.DocUtil;
//import com.cyyz.eseal.sign.util.sealpic.com.hg.doc.XDocXml;
//import com.cyyz.eseal.sign.util.sealpic.com.hg.doc.XFont;
//import com.cyyz.eseal.sign.util.sealpic.com.hg.util.StrUtil;
//import com.cyyz.eseal.sign.util.sealpic.com.hg.util.XUrl;
//
//import javax.servlet.ServletException;
//import javax.servlet.ServletOutputStream;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.net.URI;
//import java.net.URISyntaxException;
//
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */ public class XDocServlet extends HttpServlet
///*     */ {
///*     */   public void init()
///*     */     throws ServletException
///*     */   {
///*  44 */     super.init();
///*  45 */     String realPath = null;
///*  46 */     if (getServletContext() != null) {
///*  47 */       realPath = getServletContext().getRealPath("/");
///*     */     }
///*  49 */     if ((realPath == null) || (realPath.length() == 0)) {
///*  50 */       String path = XDocServlet.class.getResource("XDocServlet.class").getPath();
///*  51 */       if (path.startsWith("file:")) {
///*     */         try {
///*  53 */           path = new URI(path).getPath();
///*     */         } catch (URISyntaxException e) {
///*  55 */           e.printStackTrace();
///*     */         }
///*     */       }
///*  58 */       realPath = path.substring(0, path.lastIndexOf("WEB-INF"));
///*     */     }
///*  60 */     realPath = XUrl.fixUrl(realPath);
///*  61 */     XFont.init(realPath + "WEB-INF/font");
///*     */   }
///*     */
///*     */   public void doGet(HttpServletRequest request, HttpServletResponse response)
///*     */     throws IOException
///*     */   {
///*  68 */     doPost(request, response);
///*     */   }
///*     */
///*     */   public void doPost(HttpServletRequest request, HttpServletResponse response)
///*     */     throws IOException
///*     */   {
///*  75 */     request.setCharacterEncoding("UTF-8");
///*  76 */     ServletOutputStream out = response.getOutputStream();
///*  77 */     response.setContentType("application/pdf");
///*  78 */     String xdoc = null;
///*     */     try {
///*  80 */       xdoc = request.getParameter("_xdoc");
///*  81 */       if ("true".equals(request.getParameter("_de")))
///*  82 */         xdoc = StrUtil.URLDecode(xdoc);
///*     */     }
///*     */     catch (Exception e) {
///*  85 */       xdoc = XDocXml.toXml(DocUtil.errDoc(e));
///*     */     }
///*     */     try {
///*  88 */       String format = request.getParameter("_format");
///*  89 */       if (format == null)
///*  90 */         format = "pdf";
///*  91 */       else if (format.equals("jpg")) {
///*  92 */         format = "jpeg";
///*     */       }
///*  94 */       if (format.equals("pdf"))
///*  95 */         response.setContentType("application/pdf");
///*  96 */       else if ((format.equals("png")) || (format.equals("jpeg")))
///*  97 */         response.setContentType("image/" + format);
///*     */       else {
///*  99 */         response.setContentType("text/xml;charset=utf-8");
///*     */       }
///* 101 */       XDoc.write(xdoc, out, format);
///*     */     } catch (Exception e) {
///* 103 */       e.printStackTrace();
///*     */     }
///* 105 */     out.flush();
///* 106 */     out.close();
///*     */   }
///*     */ }
//
///* Location:           C:\Users\admin\Desktop\xdoc.jar
// * Qualified Name:     com.hg.xdoc.XDocServlet
// * JD-Core Version:    0.6.0
// */