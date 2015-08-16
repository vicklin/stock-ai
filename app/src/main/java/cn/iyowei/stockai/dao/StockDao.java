package cn.iyowei.stockai.dao;

import cn.iyowei.stockai.model.Stock;
import cn.iyowei.stockai.vo.query.StockAIQuery;
import cn.iyowei.stockai.web.config.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;


/**
 * @author vicklin123@gmail.com
 * @version 1.0
 * @since 1.0
 */

@Repository
public class StockDao {

    /**
     * 个股对应的标签
     */
    @Autowired
    private RedisTemplate redisTemplate;

    
    /*
     * 指定RedisTemplate查询某set
     *
     * @param redisTemplate
     * @param query
     * @return
     */
    private Set<ZSetOperations.TypedTuple> findByName(RedisTemplate redisTemplate, StockAIQuery query) {
        Set<ZSetOperations.TypedTuple> set;
        switch (query.getOrder()) {
            case Constant.ORDER_ASC:
                set = redisTemplate.boundZSetOps(query.getKey()).rangeWithScores(query.getStart(), query.getEnd());
                break;
            case Constant.ORDER_DESC:
                set = redisTemplate.boundZSetOps(query.getKey()).reverseRangeWithScores(query.getStart(), query.getEnd());
                break;
            default:
                set = redisTemplate.boundZSetOps(query.getKey()).rangeWithScores(query.getStart(), query.getEnd());
        }
        return set;
    }
}
