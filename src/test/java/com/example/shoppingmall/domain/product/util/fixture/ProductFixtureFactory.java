package com.example.shoppingmall.domain.product.util.fixture;

import com.example.shoppingmall.domain.product.product.entity.Product;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.FieldPredicates;
import org.jeasy.random.randomizers.misc.BooleanRandomizer;
import org.jeasy.random.randomizers.range.IntegerRangeRandomizer;
import org.jeasy.random.randomizers.range.LongRangeRandomizer;

import java.time.LocalDate;

import static org.jeasy.random.FieldPredicates.inClass;
import static org.jeasy.random.FieldPredicates.*;

public class ProductFixtureFactory {

    public static EasyRandom get(LocalDate firstDate, LocalDate lastDate){

        var idPredicate = named("id")
                .and(ofType(Long.class))
                .and(inClass(Product.class));

        var categoryIdPredicate = named("categoryId")
                .and(ofType(Long.class))
                .and(inClass(Product.class));

        var subCategoryIdPredicate = named("subCategoryId")
                .and(ofType(Long.class))
                .and(inClass(Product.class));

        var pricePredicate = named("price")
                .and(ofType(Integer.class))
                .and(inClass(Product.class));

        var stockPredicate = named("stock")
                .and(ofType(Integer.class))
                .and(inClass(Product.class));

        var saledPredicate = named("saled")
                .and(ofType(boolean.class))
                .and(inClass(Product.class));

//        var memberIdPredicate = named("memberId")
//                .and(ofType(Long.class))
//                .and(inClass(Product.class));

        var param = new EasyRandomParameters()
                .excludeField(idPredicate) // null
                .dateRange(firstDate, lastDate) // 날짜 범위
                .randomize(categoryIdPredicate, new LongRangeRandomizer(1L, 6L))
                .randomize(subCategoryIdPredicate, new LongRangeRandomizer(1L, 6L))
                .randomize(pricePredicate, new IntegerRangeRandomizer(100000, 10000000))
                .randomize(stockPredicate, new IntegerRangeRandomizer(1, 10))
                .randomize(saledPredicate, new BooleanRandomizer());

//                .randomize(memberIdPredicate, () -> memberId); // 고정

        return new EasyRandom(param);
    }
}
