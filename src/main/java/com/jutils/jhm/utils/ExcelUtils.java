/* 
 * @(#)ExeclUtils.java    Created on 2014年7月1日
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.jutils.jhm.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * excel导入、导出工具类
 * 
 * @author jianghm
 * @version $Revision: 1.0 $, $Date: 2014年4月22日 下午3:56:09 $
 */
public class ExcelUtils {

    /**
     * 基于结果集的导出
     * 
     * @param title
     * @param cellNames
     * @param companyDtoList
     * @return
     * @throws Exception
     */
    public static <T> byte[] export(String title, String[][] cellNames, List<T> dbList) throws Exception {
        if (dbList.size() == 0) {
            return null;
        }
        // 创建Excel的工作书册 Workbook,对应到一个excel文档
        HSSFWorkbook wb = new HSSFWorkbook();
        // 创建Excel的工作sheet,对应到一个excel文档的tab
        HSSFSheet sheet = wb.createSheet(title);
        // 创建字体样式
        HSSFFont font = wb.createFont();
        font.setFontName("宋体");
        // 创建单元格样式
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 设置边框
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        // 设置字体
        style.setFont(font);
        // 创建Excel的sheet的一行
        HSSFRow row = sheet.createRow(0);
        // 设定行的高度
        row.setHeight((short) 500);
        // 创建一个Excel的单元格
        HSSFCell cell = row.createCell(0);
        // 合并单元格(startRow，endRow，startColumn，endColumn)
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, cellNames.length - 1));
        // 给合并后的单元格加上样式
        for (int j = 0; j <= cellNames.length - 1; j++) {
            HSSFCell cell_temp = row.getCell(j);
            if (cell_temp == null) {
                cell_temp = row.createCell(j);
            }
            cell_temp.setCellStyle(style);
        }
        // 给Excel的单元格设置样式和赋值
        cell.setCellStyle(style);
        cell.setCellValue(title);
        HSSFRow rowTitle = sheet.createRow(1);
        for (int i = 0; i < cellNames.length; i++) {
            HSSFCell cellTitle = rowTitle.createCell(i);
            cellTitle.setCellStyle(style);
            // 设置excel列名
            cellTitle.setCellValue(cellNames[i][1]);
        }
        // 自动换行
        style.setWrapText(true);
        int i = 0;
        for (T bd : dbList) {
            row = sheet.createRow(i + 2);
            for (int j = 0; j < cellNames.length; j++) {
                HSSFCell cellvalue = row.createCell(j);
                String value = null;
                Object object = ObjectUtils.getProperty(bd, cellNames[j][0]);
                if (null == object) {
                    value = "";
                }
                else if (object instanceof Date) {
                    Date date = (Date) object;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    value = sdf.format(date);
                }
                else {
                    value = String.valueOf(object);
                }
                if ("str".equals(cellNames[j][2])) {
                    cellvalue.setCellValue(value);
                    cellvalue.setCellStyle(style);
                }
                else {
                    HSSFDataFormat format = wb.createDataFormat();
                    HSSFCellStyle formatStyle = wb.createCellStyle();
                    // 设置边框
                    formatStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                    formatStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    formatStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                    formatStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
                    // 设置字体
                    formatStyle.setFont(font);
                    if ("amt".equals(cellNames[j][2])) {
                        cellvalue.setCellValue(Double.parseDouble(value));
                        // 设置cell样式为定制的浮点数格式
                        formatStyle.setDataFormat(format.getFormat("#,##0.00"));
                    }
                    else if ("datetime".equals(cellNames[j][2])) {
                        cellvalue.setCellValue(value);
                        // 设置cell样式为定制的日期时间格式
                        formatStyle.setDataFormat(format.getFormat("yyyy-MM-dd hh:mm:ss"));
                    }
                    cellvalue.setCellStyle(formatStyle);
                }
            }
            i++;
        }
        if (i == 0) {
            return null;
        }
        for (int k = 0; k < cellNames.length; k++) {
            // 自动设置列宽
            sheet.autoSizeColumn(k, true);
        }
        // 将生成的Excel放入IO流中
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        // 在内存中把数据写入ByteArrayOutputStream os
        wb.write(os);
        // 在内存中,得到os的字节数组
        byte[] content = os.toByteArray();
        return content;
    }

    /**
     * 读取Excel数据内容
     * 
     * @param file
     * @return Map
     * @throws Exception
     */
    public static Map<Integer, String> readExcelContent(File file) throws Exception {
        Map<Integer, String> content = new HashMap<Integer, String>();
        String str = "";
        HSSFRow row = null;
        InputStream is = new FileInputStream(file);
        POIFSFileSystem fs = new POIFSFileSystem(is);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheetAt(0);
        // 得到总行数
        int rowNum = sheet.getLastRowNum();
        // 得到标题的内容对象
        row = sheet.getRow(0);
        int colNum = row.getPhysicalNumberOfCells();
        // 正文内容应该从第二行开始,第一行为表头的标题
        for (int i = 1; i <= rowNum; i++) {
            row = sheet.getRow(i);
            int j = 0;
            while (j < colNum) {
                str += ExcelUtils.getStringCellValue(row.getCell(j)) + "&";
                j++;
            }
            content.put(i, str);
            str = "";
        }
        return content;
    }

    /**
     * 获取单元格数据内容为字符串类型的数据
     * 
     * @param cell
     * @return
     */
    public static String getStringCellValue(HSSFCell cell) {
        String strCell = "";
        if (null == cell) {
            return " ";
        }
        else {
            switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_STRING:
                strCell = cell.getStringCellValue();
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                DecimalFormat df = new DecimalFormat("0");
                strCell = df.format(cell.getNumericCellValue());
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                strCell = String.valueOf(cell.getBooleanCellValue());
                break;
            case HSSFCell.CELL_TYPE_BLANK:
                strCell = " ";
                break;
            default:
                strCell = " ";
                break;
            }
            if (strCell.equals("") || strCell == null) {
                return " ";
            }
        }
        return strCell;
    }

}
