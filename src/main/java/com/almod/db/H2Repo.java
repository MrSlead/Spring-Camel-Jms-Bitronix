package com.almod.db;

import org.apache.camel.Handler;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class H2Repo {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public H2Repo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    @Handler
    public void writeInDB(InputStream inputStream) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        String sqlMessageDB = "INSERT INTO message(text) VALUES(?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlMessageDB, Statement.RETURN_GENERATED_KEYS);
            try {
                String text = IOUtils.toString(inputStream, "UTF-8");
                ps.setString(1, text);
            } catch (Exception e) {
                //LOG.error(e.getMessage());
            }
            return ps;
        }, keyHolder);

        String sqlHeadersDB = "INSERT INTO headers(id, head) VALUES(?, ?)";
        try {
            jdbcTemplate.update(sqlHeadersDB, keyHolder.getKey().longValue(), "Type");
        } catch (Exception e) {
            //LOG.error(e.getMessage());
        }
    }

    public void showDataDB() {
        //LOG.info("Show result in database");
        String sql = "SELECT mg.text, hs.head" +
                " FROM message mg" +
                " INNER JOIN headers hs" +
                " ON mg.id = hs.id;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
        while(rowSet.next()) {
            System.out.println();
            System.out.println(rowSet.getString(1) + "  " + rowSet.getString(2));
            System.out.println();
            //.info(rowSet.getString(1) + "  " + rowSet.getString(2));
        }
    }
}
