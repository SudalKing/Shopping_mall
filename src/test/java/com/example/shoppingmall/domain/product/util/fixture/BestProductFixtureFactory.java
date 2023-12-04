package com.example.shoppingmall.domain.product.util.fixture;

import com.example.shoppingmall.domain.product.best.entity.BestProduct;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.randomizers.range.DoubleRangeRandomizer;
import org.jeasy.random.randomizers.range.IntegerRangeRandomizer;

import static org.jeasy.random.FieldPredicates.*;

public class BestProductFixtureFactory {
    public static EasyRandom get(){

        var idPredicate = named("id")
                .and(ofType(Long.class))
                .and(inClass(BestProduct.class));

        var productIdPredicate = named("productId")
                .and(ofType(Long.class))
                .and(inClass(BestProduct.class));

        var totalSalesPredicate = named("totalSales")
                .and(ofType(int.class))
                .and(inClass(BestProduct.class));

        var totalLikePredicate = named("totalLike")
                .and(ofType(int.class))
                .and(inClass(BestProduct.class));

        var scorePredicate = named("score")
                .and(ofType(Double.class))
                .and(inClass(BestProduct.class));

        var param = new EasyRandomParameters()
                .excludeField(idPredicate) // null
                .excludeField(productIdPredicate)
                .randomize(totalSalesPredicate, new IntegerRangeRandomizer(1, 100))
                .randomize(totalLikePredicate, new IntegerRangeRandomizer(0, 200))
                .randomize(scorePredicate, new DoubleRangeRandomizer(0., 5.))
                ;

        return new EasyRandom(param);
    }
}
