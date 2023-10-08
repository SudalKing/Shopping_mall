package com.example.shoppingmall.util;

import com.example.shoppingmall.domain.product.entity.Product;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.randomizers.range.IntegerRangeRandomizer;
import org.jeasy.random.randomizers.range.LongRangeRandomizer;

import java.time.LocalDate;

import static org.jeasy.random.FieldPredicates.*;
import static org.jeasy.random.FieldPredicates.inClass;

public class ProductFixtureFactory {

//    public static EasyRandom get(Long memberId, LocalDate firstDate, LocalDate lastDate)
    public static EasyRandom get(LocalDate firstDate, LocalDate lastDate){

        var idPredicate = named("id")
                .and(ofType(Long.class))
                .and(inClass(Product.class));

        var pricePredicate = named("price")
                .and(ofType(Integer.class))
                .and(inClass(Product.class));

        var stockPredicate = named("stock")
                .and(ofType(Integer.class))
                .and(inClass(Product.class));

        var categoryIdPredicate = named("categoryId")
                .and(ofType(Long.class))
                .and(inClass(Product.class));

        var brandIdPredicate = named("brandId")
                .and(ofType(Long.class))
                .and(inClass(Product.class));
//        var memberIdPredicate = named("memberId")
//                .and(ofType(Long.class))
//                .and(inClass(Product.class));

        var param = new EasyRandomParameters()
                .excludeField(idPredicate) // null
                .dateRange(firstDate, lastDate) // 날짜 범위
                .randomize(pricePredicate, new IntegerRangeRandomizer(100000, 10000000))
                .randomize(stockPredicate, new IntegerRangeRandomizer(1, 30))
                .randomize(categoryIdPredicate, new LongRangeRandomizer(1L, 4L))
                .randomize(brandIdPredicate, new LongRangeRandomizer(1L, 4L));

//                .randomize(memberIdPredicate, () -> memberId); // 고정

        return new EasyRandom(param);
    }
}
