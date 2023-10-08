package com.example.shoppingmall.util;

import com.example.shoppingmall.domain.product.entity.ProductLike;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.randomizers.range.LongRangeRandomizer;

import java.time.LocalDate;

import static org.jeasy.random.FieldPredicates.*;

public class ProductLikeFixtureFactory {

//    public static EasyRandom get(Long memberId, LocalDate firstDate, LocalDate lastDate)
    public static EasyRandom get(LocalDate firstDate, LocalDate lastDate){

        var idPredicate = named("id")
                .and(ofType(Long.class))
                .and(inClass(ProductLike.class));

        var productIdPredicate = named("productId")
                .and(ofType(Long.class))
                .and(inClass(ProductLike.class));

        var userIdPredicate = named("userId")
                .and(ofType(Long.class))
                .and(inClass(ProductLike.class));

        var param = new EasyRandomParameters()
                .excludeField(idPredicate) // null
                .dateRange(firstDate, lastDate) // 날짜 범위
                .randomize(productIdPredicate, new LongRangeRandomizer(200L, 1500L))
                .randomize(userIdPredicate, new LongRangeRandomizer(1L, 20L));

//                .randomize(memberIdPredicate, () -> memberId); // 고정

        return new EasyRandom(param);
    }
}
