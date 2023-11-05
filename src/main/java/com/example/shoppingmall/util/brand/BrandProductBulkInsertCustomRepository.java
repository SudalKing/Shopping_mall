package com.example.shoppingmall.util.brand;

import com.example.shoppingmall.domain.brand.entity.BrandProduct;
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
public class BrandProductBulkInsertCustomRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    final static private String TABLE_BRAND_PRODUCT = "Brand_Product";

    private Long productId = 1L;

    private static final RowMapper<BrandProduct> ROW_MAPPER_Brand_Product = (ResultSet resultSet, int rowNum) -> BrandProduct.builder()
            .id(resultSet.getLong("id"))
            .productId(resultSet.getLong("productId"))
            .brandId(resultSet.getLong("brandId"))
            .build();

    public void bulkInsertBrandProduct(List<BrandProduct> brandProducts){
        var sql = String.format(
                "INSERT INTO %s (product_id, brand_id) " +
                "VALUES (:productId, :brandId)" ,
                TABLE_BRAND_PRODUCT);

        SqlParameterSource[] params = brandProducts
                .stream()
                .map(brandProduct -> {
                    MapSqlParameterSource param = new MapSqlParameterSource();
                    param.addValue("productId", productId++);
                    param.addValue("brandId", brandProduct.getBrandId());
                    return param;
                })
                .toArray(SqlParameterSource[]::new);
        namedParameterJdbcTemplate.batchUpdate(sql, params);
    }
}
