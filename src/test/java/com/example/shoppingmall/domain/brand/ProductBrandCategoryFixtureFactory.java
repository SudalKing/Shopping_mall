package com.example.shoppingmall.domain.brand;

import com.example.shoppingmall.domain.brand.entity.ProductBrandCategory;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.randomizers.range.LongRangeRandomizer;

import static org.jeasy.random.FieldPredicates.*;

public class ProductBrandCategoryFixtureFactory {

    public static EasyRandom get(){

        var idPredicate = named("id")
                .and(ofType(Long.class))
                .and(inClass(ProductBrandCategory.class));

        var productIdPredicate = named("productId")
                .and(ofType(Long.class))
                .and(inClass(ProductBrandCategory.class));

        var brandIdPredicate = named("brandId")
                .and(ofType(Long.class))
                .and(inClass(ProductBrandCategory.class));

        var brandCategoryIdPredicate = named("brandCategoryId")
                .and(ofType(Long.class))
                .and(inClass(ProductBrandCategory.class));


        var param = new EasyRandomParameters()
                .excludeField(idPredicate)
                .excludeField(productIdPredicate)
                .randomize(brandIdPredicate, new LongRangeRandomizer(1L, 5L))
                .randomize(brandCategoryIdPredicate, new LongRangeRandomizer(1L, 10L))
                ;

        return new EasyRandom(param);
    }
}
