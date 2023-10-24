package com.example.shoppingmall.util.brand;

import com.example.shoppingmall.domain.brand.entity.ProductBrandCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class ProductBrandCategoryBulkInsertCustomRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    final static private String TABLE_PRODUCT_BRAND_CATEGORY = "product_brand_category";

    private Long productId = 1L;

    private static final RowMapper<ProductBrandCategory> ROW_MAPPER_PRODUCT_BRAND_CATEGORY =
            (ResultSet resultSet, int rowNum) -> ProductBrandCategory.builder()
                    .id(resultSet.getLong("id"))
                    .productId(resultSet.getLong("productId"))
                    .brandId(resultSet.getLong("brandId"))
                    .brandCategoryId(resultSet.getLong("brandCategoryId"))
                    .build();

    public void bulkInsertProductBrandCategory(List<ProductBrandCategory> productBrandCategories){
        var sql = String.format(
                "INSERT INTO %s (product_id, brand_id, brand_category_id) " +
                        "VALUES (:productId, :brandId, :brandCategoryId)",
                TABLE_PRODUCT_BRAND_CATEGORY);

        SqlParameterSource[] params = productBrandCategories
                .stream()
                .map(productBrandCategory -> {
                    MapSqlParameterSource param = new MapSqlParameterSource();
                    param.addValue("productId", productId++); // productId를 증가시키면서 설정
                    param.addValue("brandId", productBrandCategory.getBrandId());
                    param.addValue("brandCategoryId", productBrandCategory.getBrandCategoryId());
                    return param;
                })
                .toArray(SqlParameterSource[]::new);

        namedParameterJdbcTemplate.batchUpdate(sql, params);
    }
}
