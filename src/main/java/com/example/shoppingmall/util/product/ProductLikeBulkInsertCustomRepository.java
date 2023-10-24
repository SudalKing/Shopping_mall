package com.example.shoppingmall.util.product;

import com.example.shoppingmall.domain.product.entity.ProductLike;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class ProductLikeBulkInsertCustomRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    final static private String TABLE_PRODUCT_LIKE = "Product_Like";

    private static final RowMapper<ProductLike> ROW_MAPPER_PRODUCT = (ResultSet resultSet, int rowNum) -> ProductLike.builder()
            .id(resultSet.getLong("id"))
            .productId(resultSet.getLong("productId"))
            .userId(resultSet.getLong("userId"))
            .createdAt(resultSet.getObject("createdAt", LocalDateTime.class))
            .build();

    public void bulkInsertProductLike(List<ProductLike> productLikes){
        var sql = String.format(
                "INSERT INTO %s (product_id, user_id, created_at) " +
                "VALUES (:productId, :userId, :createdAt)" ,
                TABLE_PRODUCT_LIKE);

        SqlParameterSource[] params = productLikes
                .stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(SqlParameterSource[]::new);
        namedParameterJdbcTemplate.batchUpdate(sql, params);
    }
}
