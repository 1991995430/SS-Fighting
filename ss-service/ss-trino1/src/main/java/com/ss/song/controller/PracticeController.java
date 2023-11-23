package com.ss.song.controller;
import com.ss.song.datasource.DataSource;
import com.ss.song.datasource.DataSourceFactory;
import com.ss.song.datasource.DataSourceParam;
import com.ss.song.dto.*;
import com.ss.song.params.JobParams;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;


@RestController
@RequestMapping("/v1/trino")
public class PracticeController {

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/con1")
    public List<DataDto> con1()  {
        /*Map<String, String> map = new HashMap<>();
        map.put("value", "value");
        map.put("text", "test");*/
        List<String> list = new ArrayList<>();
        list.add("ssss,111");
        list.add("1111111,222");
        list.add("22222,333");
        List<DataDto> list1 = new ArrayList<>();
        DataDto dataDto = new DataDto();
        dataDto.setText("ssss");
        dataDto.setValue("vcaa");
        DataDto dataDto1 = new DataDto();
        dataDto1.setValue("shang");
        dataDto1.setText("song");
        list1.add(dataDto1);
        list1.add(dataDto);
        return list1;
    }

    @PostMapping("/submit")
    public QueryResultVo submit(@RequestBody JobParams jobParams)  {

        String sql = "insert into mysql.shangsong.user values()";



        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Trino-User", jobParams.getUserName());
        // 提交任务到trino服务
        headers.set("Content-Type", "application/json");
        String nextUri = submitJobToTrino(headers, jobParams.getSql());
        return getMetaData(nextUri);
    }


    @PostMapping("/submit1")
    public void submitToTrino(@RequestBody JobParams jobParams)  {


        try (FileInputStream excelFile = new FileInputStream("D://20230830.xlsx");
             Workbook workbook = new XSSFWorkbook(excelFile)) {

            Sheet sheet1 = workbook.getSheetAt(0);
            Sheet sheet2 = workbook.getSheetAt(1);

            List<String> columnNames = getColumnNames(sheet1);
            List<String> columnTypes = getColumnTypes(sheet2);

            String createTableSql = buildTrinoCreateTableSql("mysql", "shangsong", jobParams.getTableName(), columnNames, columnTypes);

            // String insertSql = insertDataInTrino(jobParams.getTableName(), sheet1, columnNames);
            List<String> insertSqlList = getInsertSqlList("mysql", "shangsong", jobParams.getTableName(), sheet1, columnNames, columnTypes);

            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Trino-User", jobParams.getUserName());
            // 提交任务到trino服务
            getMetaData(submitJobToTrino(headers, createTableSql));

            // getMetaData(submitJobToTrino(headers, insertSql));
            int finishedcount = 0;
            int allCount = sheet1.getLastRowNum();
            System.out.println("总行数：" + allCount + "行");
            System.out.println("已完成：" + finishedcount + "行");
            for (String sql : insertSqlList) {
                QueryResultVo metaData = getMetaData(submitJobToTrino(headers, sql));
                Integer finish = ((List<Integer>) (metaData.getQueryResult().get(0))).get(0);
                finishedcount += finish;
                System.out.println("总行数：" + allCount + "行 - - -" + "已完成：" + finishedcount + "行");
            }

            // getMetaData(submitJobToTrino(headers, "commit"));
            System.out.println("数据导入到Trino成功。");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/submit3")
    public void submitToJdbc(@RequestBody JobParams jobParams)  {


        try (FileInputStream excelFile = new FileInputStream("D://2023090603.xlsx"); Workbook workbook = new XSSFWorkbook(excelFile)) {

            Sheet sheet1 = workbook.getSheetAt(0);
            Sheet sheet2 = workbook.getSheetAt(1);

            List<String> columnNames = getColumnNames(sheet1);
            List<String> columnTypes = getColumnTypes(sheet2);

            /*Class.forName("com.kingbase8.Driver");
            //String url = "jdbc:mysql://localhost:3306/shangsong?useUnicode=true&useSSL=false&characterEncoding=utf8&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai&zeroDateTimeBehavior=CONVERT_TO_NULL";
            String url = "jdbc:kingbase8://192.168.3.208:54321/test";
            String username = "root";
            String password = "123456";
            Connection connection = DriverManager.getConnection(url, username, password);

            createTable(connection, jobParams.getTableName(), columnNames, columnTypes);*/

            DataSourceParam dataSourceParam = new DataSourceParam();
            //dataSourceParam.setIp("192.168.3.208");
            dataSourceParam.setUrl("jdbc:kingbase8://192.168.3.208:54321/test");
            dataSourceParam.setUserName("root");
            dataSourceParam.setPassword("123456");
            dataSourceParam.setType("kingbase");
            DataSource dataSource = DataSourceFactory.create(dataSourceParam);
            if (dataSource == null) {
                throw new RuntimeException("数据源为空");
            }
            boolean ret = dataSource.connect();
            if (!ret) {
                return;
            }

            String createTableSql = buildJdbcCreateTableSql("public", jobParams.getTableName(), columnNames, columnTypes);
            int update = dataSource.update(createTableSql);
            System.out.println(update);

            List<String> insertSqlList = getJdbcInsertSqlList("public", jobParams.getTableName(), sheet1, columnNames, columnTypes);
            dataSource.startTransaction();
            for (String sql : insertSqlList) {
                dataSource.update(sql);
            }
            dataSource.commit();
            dataSource.close();
            //insertData(connection, jobParams.getTableName(), sheet1, columnNames);
            // getMetaData(submitJobToTrino(headers, "commit"));
            System.out.println("数据导入到Trino成功。");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String submitJobToTrino(HttpHeaders headers, String trinoSql) {
        HttpEntity<String> requestEntity = new HttpEntity<>(trinoSql, headers);
        // 获取当前主节点ip
        ResponseEntity<Map> exchange = restTemplate.exchange("http://localhost:8080/v1/statement", HttpMethod.POST, requestEntity, Map.class);
        return (String) exchange.getBody().get("nextUri");
    }


    public QueryResultVo getMetaData(String nextUri) {
        QueryResultVo queryResultVo = new QueryResultVo();
        List<Object> finalData = new ArrayList<>();
        int count = 0;
        while (nextUri != null) {
            QueryResponseInfo responseInfo = restTemplate.getForObject(nextUri, QueryResponseInfo.class);
            nextUri = responseInfo.getNextUri();
            QueryRespStats stats = responseInfo.getStats();

            // 获取字段名称
            List<Column> columnList = new ArrayList<>();
            if (responseInfo.getColumns() != null) {
                columnList = responseInfo.getColumns();
            }

            List<String> columnName = new LinkedList<>();
            columnList.forEach(columns -> columnName.add(columns.getName()));
            queryResultVo.setColumns(columnName);
            if (stats.getState().equals("FAILED")) {
                System.out.println("获取数据源信息任务查询失败！");
                throw new RuntimeException("数据源查询失败:" + ((Map<String, String>)responseInfo.getError()).get("message"));
            }

            if (responseInfo.getData() != null) {
                finalData.addAll((responseInfo.getData()));
            }
            count++;
            System.out.println("nextUri:::" + nextUri);
            System.out.println(finalData);
            /*try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }*/
        }
        System.out.println("---------------------------------");
        System.out.println(count);
        queryResultVo.setQueryResult(finalData);
        System.out.println("任务查询成功！");
        return queryResultVo;
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

    private static List<String> getJdbcInsertSqlList(String schemaName, String tableName, Sheet sheet, List<String> columnNames, List<String> columnTypes) {
        List<String> insertStatements = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO ")
                .append(schemaName)
                .append(".")
                .append(tableName)
                .append(" (")
                .append(String.join(",", columnNames))
                .append(") VALUES");

        int batchCount = 0;
        for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
            Row row = sheet.getRow(rowNum);
            builder.append("(");
            for (int i = 0; i < columnNames.size(); i++) {
                Cell cell = row.getCell(i);
                String cellValue = "";
                if (cell == null) {
                    cellValue = "NULL";
                } else {
                    switch (cell.getCellType()) {
                        case STRING:
                            cellValue = "'" + cell.getStringCellValue() + "'";
                            break;
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(cell)) {
                                Date date = cell.getDateCellValue();
                                if (columnTypes.get(i).toUpperCase(Locale.ROOT).contains("TIMESTAMP")) {
                                    cellValue = "'" + formatTimeStamp(date) + "'";
                                } else if (columnTypes.get(i).toUpperCase(Locale.ROOT).contains("DATE")) {
                                    cellValue = "'" + formatDate(date) + "'";
                                } else if (columnTypes.get(i).toUpperCase(Locale.ROOT).contains("TIME")) {
                                    cellValue = "'" + formatTime(date) + "'";
                                }
                            } else {
                                double numericValue = cell.getNumericCellValue();
                                cellValue = formatNumericValue(numericValue);
                            }
                            break;
                        case BOOLEAN:
                            cellValue = String.valueOf(cell.getBooleanCellValue());
                            break;
                        case FORMULA:
                            cellValue = evaluateFormula(cell);
                            break;
                        case BLANK:
                            cellValue = "NULL";
                            break;
                    }
                }

                builder.append(cellValue);
                if (i != columnNames.size() - 1) {
                    builder.append(",");
                }
            }
            builder.append("),");

            batchCount++;

            if (batchCount == 10000 || rowNum == sheet.getLastRowNum()) {
                insertStatements.add(builder.substring(0, builder.length() - 1)); // 去除最后一个逗号
                builder.setLength(0); // 重置 StringBuilder
                builder.append("INSERT INTO ")
                        .append(schemaName)
                        .append(".")
                        .append(tableName)
                        .append(" (")
                        .append(String.join(",", columnNames))
                        .append(") VALUES");
                batchCount = 0;
            }
        }
        return insertStatements;
    }

    private static List<String> getInsertSqlList(String catalogName, String schemaName, String tableName, Sheet sheet, List<String> columnNames, List<String> columnTypes)
            throws IOException, InterruptedException {
        List<String> insertStatements = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO ")
                .append(catalogName)
                .append(".")
                .append(schemaName)
                .append(".")
                .append(tableName)
                .append(" (")
                .append(String.join(",", columnNames))
                .append(") VALUES");
        int batchCount = 0;
        for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
            Row row = sheet.getRow(rowNum);
            builder.append("(");
            for (int i = 0; i < columnNames.size(); i++) {
                Cell cell = row.getCell(i);
                String columnType = columnTypes.get(i);
                String cellValue = getCellValueAsString(cell, columnType, builder);
                builder.append(cellValue);
                if (i != columnNames.size() - 1) {
                    builder.append(",");
                }
            }
            builder.append("),");

            batchCount++;

            if (batchCount == 10000 || rowNum == sheet.getLastRowNum()) {
                insertStatements.add(removeLastComma(builder)); // 去除最后一个逗号
                resetBuilder(builder, tableName, columnNames);
                batchCount = 0;
            }
        }
        return insertStatements;
    }

    private static String getCellValueAsString(Cell cell, String columnType, StringBuilder builder) {
        if (cell == null) {
            return "NULL";
        }

        switch (cell.getCellType()) {
            case STRING:
                return "'" + cell.getStringCellValue() + "'";
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return handleDateCell(cell, columnType, builder);
                } else {
                    double numericValue = cell.getNumericCellValue();
                    return formatNumericValue(numericValue);
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return evaluateFormula(cell);
            case BLANK:
                return "NULL";
            default:
                return "";
        }
    }

    private static String handleDateCell(Cell cell, String columnType, StringBuilder builder) {
        Date date = cell.getDateCellValue();
        if (columnType.toUpperCase(Locale.ROOT).contains("TIMESTAMP")) {
            builder.append("TimeStamp ");
            return "'" + formatTimeStamp(date) + "'";
        } else if (columnType.toUpperCase(Locale.ROOT).contains("DATE")) {
            builder.append("Date ");
            return "'" + formatDate(date) + "'";
        } else if (columnType.toUpperCase(Locale.ROOT).contains("TIME")) {
            builder.append("Time ");
            return "'" + formatTime(date) + "'";
        }
        return "";
    }

    private static String removeLastComma(StringBuilder builder) {
        int lastCommaIndex = builder.lastIndexOf(",");
        return builder.substring(0, lastCommaIndex);
    }

    private static void resetBuilder(StringBuilder builder, String tableName, List<String> columnNames) {
        builder.setLength(0);
        builder.append("INSERT INTO mysql.shangsong.").append(tableName).append(" (")
                .append(String.join(",", columnNames)).append(") VALUES");
    }

    public static String formatDate(Date date) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.format(date);
        } catch (Exception ignored) {}


        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-dd-MM");
            return dateFormat.format(date);
        } catch (Exception ignored) {}

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
            return dateFormat.format(date);
        } catch (Exception ignored) {}

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
            return dateFormat.format(date);
        } catch (Exception ignored) {}

        throw new IllegalArgumentException("Unrecognized timestamp format: ");
    }

    public static String formatTimeStamp(Date date) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return dateFormat.format(date);
        } catch (Exception ignored) {}

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss");
            return dateFormat.format(date);
        } catch (Exception ignored) {}

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
            return dateFormat.format(date);
        } catch (Exception ignored) {}

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
            return dateFormat.format(date);
        } catch (Exception ignored) {}

        throw new IllegalArgumentException("Unrecognized timestamp format: ");
    }

    public static String formatTime(Date date) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            return dateFormat.format(date);
        } catch (Exception ignored) {}

        throw new IllegalArgumentException("Unrecognized timestamp format: ");
    }

    /*private static String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    private static String formatDateTime(Date date) {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateTimeFormat.format(date);
    }*/

    private static String formatNumericValue(double value) {
        String formattedValue;
        DecimalFormat decimalFormat = new DecimalFormat("#");

        if (value % 1 == 0) {
            // 整数类型
            formattedValue = decimalFormat.format(value);
        } else {
            // 浮点数类型
            DecimalFormat decimalFormatWithFraction = new DecimalFormat("#.##########");
            formattedValue = decimalFormatWithFraction.format(value);
        }

        return formattedValue;
    }

    private static String evaluateFormula(Cell cell) {
        String cellValue = "";

        switch (cell.getCachedFormulaResultType()) {
            case NUMERIC:
                cellValue = formatNumericValue(cell.getNumericCellValue());
                break;
            case STRING:
                cellValue = "'" + cell.getRichStringCellValue().getString() + "'";
                break;
            case BOOLEAN:
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case ERROR:
                FormulaError error = FormulaError.forInt(cell.getErrorCellValue());
                cellValue = error.getString();
                break;
            // 其他类型的计算结果可以按需处理
        }

        return cellValue;
    }

    private static String buildJdbcCreateTableSql(String schemaName, String tableName, List<String> columnNames, List<String> columnTypes) {
        StringBuilder sqlBuilder = new StringBuilder("CREATE TABLE ").append(schemaName).append(".");
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

    private static String buildTrinoCreateTableSql(String catalogName, String schemaName,
                                                   String tableName, List<String> columnNames,
                                                   List<String> columnTypes) {
        StringBuilder sqlBuilder = new StringBuilder("CREATE TABLE ")
                .append(catalogName)
                .append(".")
                .append(schemaName)
                .append(".")
                .append(tableName).append(" (");

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
