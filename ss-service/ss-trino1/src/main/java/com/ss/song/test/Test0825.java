package com.ss.song.test;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpRequest;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * author shangsong 2023/8/25
 */
public class Test0825 {

    public static void main(String[] args) {
        String excelFilePath = "D://20230825.xlsx";
        String jdbcUrl = "jdbc:mysql://localhost:3306/shangsong?useUnicode=true&useSSL=false&characterEncoding=utf8&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai&zeroDateTimeBehavior=CONVERT_TO_NULL";
        String username = "root";
        String password = "199551";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             FileInputStream excelFile = new FileInputStream(excelFilePath);
             Workbook workbook = new XSSFWorkbook(excelFile)) {

            Sheet sheet1 = workbook.getSheetAt(0);
            Sheet sheet2 = workbook.getSheetAt(1);

            List<String> columnNames = getColumnNames(sheet1);

            // 第一个sheet的字段类型
            List<String> columnTypes = getColumnTypes(sheet2);

            createTable(connection, "user8888", columnNames, columnTypes);

            insertData(connection, "user8888", sheet1, columnNames);

            System.out.println("导入数据成功。");

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static List<String> getColumnNames(Sheet sheet) {
        Row headerRow = sheet.getRow(0);
        List<String> columnNames = new ArrayList<>();

        for (Cell cell : headerRow) {
            columnNames.add(cell.getStringCellValue());
        }

        return columnNames;
    }

    private static List<String> getColumnTypes(Sheet sheet) {
        Row headerRow = sheet.getRow(0);
        List<String> columnTypes = new ArrayList<>();

        for (Cell cell : headerRow) {
            columnTypes.add(cell.getStringCellValue());
        }

        return columnTypes;
    }

    private static void createTable(Connection connection, String tableName, List<String> columnNames,
                                    List<String> columnTypes) throws SQLException {
        StringBuilder sqlBuilder = new StringBuilder("CREATE TABLE ");
        sqlBuilder.append(tableName).append(" (");

        for (int i = 0; i < columnNames.size(); i++) {
            String columnName = columnNames.get(i);
            String columnType = columnTypes.get(i);

            sqlBuilder.append(columnName).append(" ").append(columnType);

            if (i != columnNames.size() - 1) {
                sqlBuilder.append(", ");
            }
        }

        sqlBuilder.append(")");

        PreparedStatement statement = connection.prepareStatement(sqlBuilder.toString());
        statement.execute();
    }

    private static void insertData(Connection connection, String tableName, Sheet sheet, List<String> columnNames)
            throws SQLException {

        String sql = "INSERT INTO " + tableName + " (" + String.join(", ", columnNames) + ") VALUES ("
                + String.join(", ", Collections.nCopies(columnNames.size(), "?")) + ")";
        PreparedStatement statement = connection.prepareStatement(sql);

        for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
            Row row = sheet.getRow(rowNum);

            for (int i = 0; i < columnNames.size(); i++) {
                Cell cell = row.getCell(i);

                switch (cell.getCellType()) {
                    case STRING:
                        statement.setString(i + 1, cell.getStringCellValue());
                        break;
                    case NUMERIC:
                        statement.setDouble(i + 1, cell.getNumericCellValue());
                        break;
                    // 处理其他数据类型
                }
            }

            statement.executeUpdate();
        }
    }


    private static void createTableInTrino(String trinoUrl, String tableName, List<String> columnNames,
                                           List<String> columnTypes) throws IOException, InterruptedException {
        String createTableSql = buildCreateTableSql(tableName, columnNames, columnTypes);
        //executeTrinoStatement(trinoUrl, createTableSql);
    }

    private static void insertDataInTrino(String trinoUrl, String tableName, Sheet sheet, List<String> columnNames)
            throws IOException, InterruptedException {
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO ").append(tableName).append(" (")
                .append(String.join(",", columnNames)).append(") VALUES");

        for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
            Row row = sheet.getRow(rowNum);
            builder.append("(");

            for (int i = 0; i < columnNames.size(); i++) {
                Cell cell = row.getCell(i);
                String cellValue = "";

                switch (cell.getCellType()) {
                    case STRING:
                        cellValue = "'" + cell.getStringCellValue() + "'";
                        break;
                    case NUMERIC:
                        cellValue = String.valueOf(cell.getNumericCellValue());
                        break;
                    // 处理其他数据类型
                }

                builder.append(cellValue);
                if (i != columnNames.size() - 1) {
                    builder.append(",");
                }
            }
            builder.append("),");
        }

        String insertDataSql = builder.substring(0, builder.length() - 1);  // 去除最后一个逗号
        //executeTrinoStatement(trinoUrl, insertDataSql);
    }

    private static String buildCreateTableSql(String tableName, List<String> columnNames, List<String> columnTypes) {
        StringBuilder sqlBuilder = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        sqlBuilder.append(tableName).append(" (");

        for (int i = 0; i < columnNames.size(); i++) {
            String columnName = columnNames.get(i);
            String columnType = columnTypes.get(i);

            sqlBuilder.append(columnName).append(" ").append(columnType);

            if (i != columnNames.size() - 1) {
                sqlBuilder.append(", ");
            }
        }

        sqlBuilder.append(")");

        return sqlBuilder.toString();
    }


}
