package com.iyin.sign.system.util;

import com.iyin.sign.system.model.code.SignSysResponseCode;
import com.iyin.sign.system.model.exception.BusinessException;
import com.iyin.sign.system.vo.req.ExportEnterpriseReqVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: ExcelReaderUtil
 * @Description: execl解析工具
 * @Author: luwenxiong
 * @CreateDate: 2019/7/17 15:27
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/7/17 15:27
 * @Version: 0.0.1
 */
@Slf4j
public class ExcelReaderUtil {

    /**
     * 读取Excel数据内容
     * @param is
     * @return List<Map<String, String>>  Map的key是列Id(0代表第一列)，值是具体内容
     */
    public static  List<ExportEnterpriseReqVO> readEnterpriseExcel(InputStream is) throws Exception{

        List<Map<Integer, String>> list = new ArrayList<Map<Integer,String>>();
        Workbook wb =null;
        try {
            //支持 xlsx
             wb = new XSSFWorkbook(is);
        } catch (Exception e) {
            //支持 xls
            wb = new HSSFWorkbook(is);
        }
        Sheet sheet =  wb.getSheetAt(0);

        // 得到总行数
        int rowNum = sheet.getLastRowNum();
        Row row = sheet.getRow(0);
        int colNum = row.getPhysicalNumberOfCells();

        // 验证表格数据是否有为空的
        for (int i = 0; i <= rowNum; i++) {
            row = sheet.getRow(i);
            int j = 0;
            while (j < colNum) {
                if(row.getCell(j)==null){
                   log.error("第{}行的第{}列为空",i+1,j+1);
                   return null;
                }
                j++;
            }
        }

        //封装数据 正文内容应该从第二行开始,第一行为表头的标题
        List<ExportEnterpriseReqVO> exportEnterpriseReqVOS = new ArrayList<ExportEnterpriseReqVO>();
        for(int i=1;i<=rowNum;i++){
            row =sheet.getRow(i);
            int k=0;
            ExportEnterpriseReqVO exportEnterpriseReqVO =new ExportEnterpriseReqVO();
            while (k<colNum){
                row.getCell(k).setCellType(Cell.CELL_TYPE_STRING);
                String value = row.getCell(k).getStringCellValue();
                exportEnterpriseReqVO.setLineNum(i+1);
                if(k==0){
                    exportEnterpriseReqVO.setEnterpriseName(value);
                }else if(k==1){
                    exportEnterpriseReqVO.setCreditCode(value);
                }else if(k==2){
                    exportEnterpriseReqVO.setLegalName(value);
                } else if(k==3){
                    exportEnterpriseReqVO.setUserName(value);
                } else if(k==4){
                    exportEnterpriseReqVO.setPassword(value);
                    exportEnterpriseReqVOS.add(exportEnterpriseReqVO);
                }
                k++;
            }
        }

        return exportEnterpriseReqVOS;
    }


    public static void main(String[] args) throws Exception{
        String path = "T:\\单位表.xlsx";
        File execl = new File(path);
        FileInputStream inputStream = new FileInputStream(execl);
        List<ExportEnterpriseReqVO> list = readEnterpriseExcel(inputStream);
        log.info("list:{}",list);

    }


}
