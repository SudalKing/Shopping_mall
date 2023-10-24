package com.example.shoppingmall.util.product;

import com.example.shoppingmall.domain.product.entity.Product;
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
public class ProductBulkInsertCustomRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    final static private String TABLE_PRODUCT = "Product";

    private static final RowMapper<Product> ROW_MAPPER_PRODUCT = (ResultSet resultSet, int rowNum) -> Product.builder()
            .id(resultSet.getLong("id"))
            .name(resultSet.getString("name"))
            .price(resultSet.getInt("price"))
            .stock(resultSet.getInt("stock"))
            .description(resultSet.getString("description"))
            .typeId(resultSet.getLong("typeId"))
            .createdAt(resultSet.getObject("createdAt", LocalDateTime.class))
            .deleted(resultSet.getBoolean("deleted"))
            .version(resultSet.getLong("version"))
            .saled(resultSet.getBoolean("saled"))
            .build();

    public void bulkInsertProduct(List<Product> products){
        var sql = String.format(
                "INSERT INTO %s (name, price, stock, description, type_id, created_at, deleted, saled) " +
                "VALUES (:name, :price, :stock, :description, :typeId, :createdAt, :deleted, :saled)" ,
                TABLE_PRODUCT);

        SqlParameterSource[] params = products
                .stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(SqlParameterSource[]::new);
        namedParameterJdbcTemplate.batchUpdate(sql, params);
    }
}
