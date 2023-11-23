package com.ss.song.datasource;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

@Data
@Slf4j
public class RecordSet {
    PreparedStatement preparedStatement;
    ResultSet resultSet;

    public void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        } catch (SQLException e) {
            //log.error("[close]Close resultSet or preparedStatement failed.");
        }
    }

    public Record next() {
        try {
            boolean ret = resultSet.next();
            if (!ret) {
                return null;
            }
            Record record = new Record();
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 0; i < metaData.getColumnCount(); i++) {
                int metaIndex = i + 1;
                Field field = new Field();
                field.setId(metaIndex);
                field.setName(metaData.getColumnName(metaIndex));
                field.setTypeId(metaData.getColumnType(metaIndex));
                field.setTypeName(metaData.getColumnTypeName(metaIndex));
                field.setValue(resultSet.getObject(metaIndex));
                if(metaData.getColumnLabel(metaIndex)!= null){
                    field.setColumnLabel(metaData.getColumnLabel(metaIndex));
                }
                record.add(field);
            }
            return record;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }

    public void setPreparedStatement(PreparedStatement preparedStatement) {
        this.preparedStatement = preparedStatement;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }
}
