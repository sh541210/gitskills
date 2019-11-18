package com.iyin.sign.system.util.sign;


import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.security.ExternalSignatureContainer;

import java.io.InputStream;
import java.security.GeneralSecurityException;


public class IyinExternalSignatureContainer implements ExternalSignatureContainer {
    private PdfDictionary sigDic;

    
    public IyinExternalSignatureContainer(PdfDictionary sigDic) {
        this.sigDic = sigDic;
    }
    
    public IyinExternalSignatureContainer(PdfName filter, PdfName subFilter) {
        sigDic = new PdfDictionary();
        sigDic.put(PdfName.FILTER, filter);
        sigDic.put(PdfName.SUBFILTER, subFilter);
    }
    
    @Override
    public byte[] sign(InputStream data) throws GeneralSecurityException {
        return new byte[0];
    }

    @Override
    public void modifySigningDictionary(PdfDictionary signDic) {
        signDic.putAll(sigDic);
    }
    
}