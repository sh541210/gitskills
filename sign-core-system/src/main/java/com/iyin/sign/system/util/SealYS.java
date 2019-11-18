package com.iyin.sign.system.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 压缩
 * @author Administrator
 *
 */
public class SealYS {

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        if(input != null){
            input.close();
        }
        return output.toByteArray();
    }

    public static int[] getEYsByte(InputStream msxx, String sid) throws IOException {
        byte[] tempByte = toByteArray(msxx);
        int[] imgByte = byteToInt(tempByte);

        ByteArrayInputStream buffin = new ByteArrayInputStream(tempByte,0,imgByte.length);

        BufferedImage bitmap = ImageIO.read(buffin);
        //BufferedImage bitmap = ImageIO.read(msxx);
        //Bitmap bitmap = new Bitmap(msxx);
        int width = bitmap.getWidth(null);
        int height = bitmap.getHeight(null);
        int width3 = width * 3;
        if (width3 % 4 != 0) {
            width3 += 4 - width3 % 4;
        }

        Color forecolor = new Color(255, 0, 0);//印章前景
        Color blockcolor = new Color(255, 255, 255);//印章背景
//        Color idcolor = new Color(0, 0, 255);//印章编码
//        Color foreflagcolor = new Color(192, 88, 207);//背景中的辅助识别特征
//        Color blockflagcolor = new Color(0, 255, 0);//前景中的辅助识别特征
        int[] imgMem = new int[22 + width * height];

        final byte mForeground = 0x00;//印章前景
        final byte mBlockground = 0x01;//印章背景
//        final byte m_stampIDColor = 0x02;//印章编码
//        final byte m_foregroundflag = 0x03;//前景中的辅助识别特征
//        final byte m_backgroundflag = 0x04;//背景中的辅助识别特征

        int[] mWidth = byteToInt(String.valueOf(width).getBytes());
        int[] mHeight = byteToInt(String.valueOf(height).getBytes());
        int[] mSid = byteToInt(String.valueOf(sid).getBytes());
        for (int i1 = 0; i1 < 13; i1++)
        {
            imgMem[i1] = (byte)mSid[i1];

        }
        for (int i1 = 13; i1 < 13 + mWidth.length; i1++)
        {
            imgMem[i1] = (byte)mWidth[i1 - 13];

        }
        for (int i1 = 17; i1 < 17 + mHeight.length; i1++)
        {
            imgMem[i1] = (byte)mHeight[i1 - 17];
        }

        String t = "2";
        imgMem[21] = (byte)t.charAt(0);
        for (int i = height - 1; i >= 0; i--)
        {
            for (int j = 0; j < width; j++)
            {

                int bmpHeadLen = 54;
                int mBlue = imgByte[width3 * i + j * 3 + bmpHeadLen];//width像素宽
                int mGreed = imgByte[width3 * i + j * 3 + 1 + bmpHeadLen];//width像素宽
                int mRed = imgByte[width3 * i + j * 3 + 2 + bmpHeadLen];//width像素宽
                Color rgb = new Color(mRed, mGreed, mBlue);

                if (rgb.equals(blockcolor))//印章背景
                {
                    imgMem[22 + width * (height - i - 1) + j] = (byte) mBlockground;
                } else//印章前景
                {
                    imgMem[22 + width * (height - i - 1) + j] = (byte) mForeground;
                }



//                if (rgb.equals(blockcolor))//印章背景
//                    imgMem[22 + width * (height - i - 1) + j] = (byte)mBlockground;
//
//                else if (rgb.equals(foreflagcolor))////背景中的辅助识别特征
//                    imgMem[22 + width * (height - i - 1) + j] = (byte)m_backgroundflag;
//
//                else if (rgb.equals(blockflagcolor))//前景中的辅助识别特征
//                    imgMem[22 + width * (height - i - 1) + j] = (byte)m_foregroundflag;
//
//                else if (rgb.equals(forecolor) || (mBlue < 189 && mGreed < 189 && mRed < 189))//印章前景
//                    imgMem[22 + width * (height - i - 1) + j] = (byte)m_foreground;
//
//                else if (rgb.equals(idcolor) || (mGreed == 255 && mBlue < 255 && mRed < 255))//印章编码
//                    imgMem[22 + width * (height - i - 1) + j] = (byte)m_stampIDColor;
//
//                else
//                    imgMem[22 + width * (height - i - 1) + j] = (byte)mBlockground;//印章背景
            }
        }

        int[] ysBitmap = imgys(width, height, imgMem);
        return ysBitmap;
    }

    public static int[] byteToInt(byte[] byt){
        int[] it = new int[byt.length];
        for(int i=0;i<byt.length;i++){
            it[i]=byt[i] & 0xff;
        }
        return it;
    }

    public static byte[] intToByte(int[] it){
        byte[] byt = new byte[it.length];
        for(int i=0;i<it.length;i++){
            byt[i]=(byte)it[i];
        }
        return byt;
    }

    private static int[] imgys(int nWidth, int nHeight, int[] imgByte){
        int fristDataLen = 22;
        //int fristDataLen = 1;
        int[] binByte = new int[nWidth * nHeight];

        for (int i = 0; i < fristDataLen - 1; i++) {
            binByte[i] = imgByte[i];
        }
        String t = "3";

        //BinByte[fristDataLen-1] = (byte)t[1];
        binByte[fristDataLen - 1] = t.charAt(0);
        int imgQJ = 0;
        int imgBJ = 0x20;
        //byte ImgBM = 0x40;
        //byte ImgQJTZ = 0x60;
        //byte ImgBJTZ = 0x80;
        int nByte, tZ;
        int j = 0;
        int k, temp;
        int templen = 0;
        k = 0;


        for (int i = 0; i < nHeight; i++)
        {
            while (j < nWidth)
            {
                int tempValue = fristDataLen + i * nWidth + j;
                temp = imgByte[tempValue];

                tZ = imgQJ;
                switch (temp)
                {
                    case 0://前景颜色
                    {
                        while ((fristDataLen + i * nWidth + j < imgByte.length) && (imgByte[fristDataLen + i * nWidth + j] == 0) && (j < nWidth))
                        {
                            templen++;
                            j++;
                        }

                        if (templen < 32)
                        {

                            //BinByte[fristDataLen + k] = TZ + templen;
                            binByte[fristDataLen + k] = (tZ + templen);

                            templen = 0;
                            nByte = 0;
                            k++;
                        }
                        else
                        {
                            tZ = 0xc0;
                            //nByte = templen / 256; //取得倍数；
                            nByte = (templen / 256);

                            //BinByte[fristDataLen + k] = TZ + nByte;
                            binByte[fristDataLen + k] = (tZ + nByte);

                            k++;
                            //nByte = templen % 256;
                            nByte = (templen % 256);

                            binByte[fristDataLen + k] = nByte;
                            k++;
                            templen = 0;
                            nByte = 0;
                        };
                        if (j == nWidth) //行结束0
                        {
                            binByte[fristDataLen + k] = 0;
                            templen = 0;
                            nByte = 0;
                            k++;

                        }
                    }
                    break;
                    case 1:      //背景
                    {
                        tZ = imgBJ;

                        while ((fristDataLen + i * nWidth + j < imgByte.length) && (imgByte[fristDataLen + i * nWidth + j] == 1) && (j < nWidth))
                        {
                            templen++;
                            j++;
                        }

                        if (j >= nWidth - 1)
                        {
                            binByte[fristDataLen + k] = 0;
                            templen = 0;
                            nByte = 0;
                            k++;
                        }
                        else
                        {
                            if (templen < 32)
                            {
                                //BinByte[fristDataLen + k] = TZ + templen;
                                binByte[fristDataLen + k] = (tZ + templen);

                                templen = 0;
                                k++;
                            }
                            else
                            {
                                tZ = 0xa0;
                                //nByte = templen / 256; //取得倍数；
                                nByte = (templen / 256);

                                //BinByte[fristDataLen + k] = TZ + nByte;
                                binByte[fristDataLen + k] = (tZ + nByte);

                                k++;
                                //nByte = templen % 256;
                                nByte = (templen % 256);

                                binByte[fristDataLen + k] = nByte;
                                k++;
                                templen = 0;
                                nByte = 0;
                                //k := k+1
                            }
                        }
                    }
                    break;
                    case 2:
                    {
                        tZ = 0x40;
                        while ((fristDataLen + i * nWidth + j < imgByte.length) && (imgByte[fristDataLen + i * nWidth + j] == 2) && (j < nWidth) && (templen < 31))
                        {
                            templen++;
                            j++;

                        }
                        //BinByte[fristDataLen + k] = TZ + templen;
                        binByte[fristDataLen + k] = (tZ + templen);

                        k++;
                        templen = 0;
                        if (j == nWidth)
                        {
                            binByte[fristDataLen + k] = 0;
                            templen = 0;
                            nByte = 0;
                            k++;
                        }
                    }
                    break;
                    case 3:
                    {
                        tZ = 0x60;
                        while ((fristDataLen + i * nWidth + j < imgByte.length) && (imgByte[fristDataLen + i * nWidth + j] == 3) && (j < nWidth) & (templen < 31))
                        {
                            templen++;
                            j++;

                        }
                        //BinByte[fristDataLen + k] = TZ + templen;
                        binByte[fristDataLen + k] = (tZ + templen);

                        k++;
                        templen = 0;
                        if (j == nWidth)
                        {
                            binByte[fristDataLen + k] = 0;
                            templen = 0;
                            nByte = 0;
                            k++;
                        }
                    }
                    break;
                    case 4:
                    {
                        tZ = 0x80;
                        while ((fristDataLen + i * nWidth + j < imgByte.length) && (imgByte[fristDataLen + i * nWidth + j] == 4) && (j < nWidth) && (templen < 31))
                        {
                            templen++;
                            j++;

                        }
                        //BinByte[fristDataLen + k] = TZ + templen;
                        binByte[fristDataLen + k] = (tZ + templen);

                        templen = 0;
                        k++;
                        if (j == nWidth)
                        {
                            binByte[fristDataLen + k] = 0;
                            templen = 0;
                            nByte = 0;
                            k++;
                        }
                    }
                    break;
                    default:
                    {
                        j++;
                    }
                    break;
                }
            }
            j = 0;

        }
        k += fristDataLen;
        //int nBackL = k;

        //byte* pBuf=new byte[k] ;
        //memcpy(pBuf,BinByte,k);
        //delete BinByte;
        //return pBuf;

        int[] rbt = new int[k];
        System.arraycopy(binByte, 0, rbt, 0, k);

        return rbt;
    }
}
