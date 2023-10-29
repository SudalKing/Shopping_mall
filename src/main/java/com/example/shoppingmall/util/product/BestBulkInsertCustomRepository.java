package com.example.shoppingmall.util.product;

import com.example.shoppingmall.domain.product_util.entity.Best;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class BestBulkInsertCustomRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    final static private String TABLE_PRODUCT = "Best";

    private Long productId = 1L;

    private static final RowMapper<Best> ROW_MAPPER_BEST = (ResultSet resultSet, int rowNum) -> Best.builder()
            .id(resultSet.getLong("id"))
            .productId(resultSet.getLong("productId"))
            .totalSales(resultSet.getInt("totalSales"))
            .totalLike(resultSet.getInt("totalLike"))
            .score(resultSet.getDouble("score"))
            .build();

    public void bulkInsertBest(List<Best> bests){
        var sql = String.format(
                "INSERT INTO %s (product_id, total_sales, total_like, score) " +
                "VALUES (:productId, :totalSales, :totalLike, :score)" ,
                TABLE_PRODUCT);

        SqlParameterSource[] params = bests
                .stream()
                .map(best -> {
                    MapSqlParameterSource param = new MapSqlParameterSource();
                    param.addValue("productId", productId++);
                    param.addValue("totalSales", best.getTotalSales());
                    param.addValue("totalLike", best.getTotalLike());
                    param.addValue("score", best.getScore());
                    return param;
                })
                .toArray(SqlParameterSource[]::new);
        namedParameterJdbcTemplate.batchUpdate(sql, params);
    }
}
