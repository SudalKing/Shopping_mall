package com.example.shoppingmall.domain.brand.util.fixture;


import com.example.shoppingmall.domain.brand.entity.BrandProduct;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.randomizers.range.LongRangeRandomizer;

import static org.jeasy.random.FieldPredicates.*;

public class BrandProductFixtureFactory {
    public static EasyRandom get(){

        var idPredicate = named("id")
                .and(ofType(Long.class))
                .and(inClass(BrandProduct.class));

        var productIdPredicate = named("productId")
                .and(ofType(Long.class))
                .and(inClass(BrandProduct.class));

        var brandIdPredicate = named("brandId")
                .and(ofType(Long.class))
                .and(inClass(BrandProduct.class));

        var param = new EasyRandomParameters()
                .excludeField(idPredicate) // null
                .excludeField(productIdPredicate)
                .randomize(brandIdPredicate, new LongRangeRandomizer(1L, 10L))
                ;

        return new EasyRandom(param);
    }
}
