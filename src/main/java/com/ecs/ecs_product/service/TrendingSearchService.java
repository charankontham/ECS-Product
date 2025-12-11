package com.ecs.ecs_product.service;

import com.ecs.ecs_product.entity.TrendingSearch;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TrendingSearchService {
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<TrendingSearch> getTrendingSearches(int limit) {

        List<Document> pipeline = Arrays.asList(

                // 1. GROUP
                Document.parse("{ " +
                        "$group: { " +
                        "  _id: '$search_query', " +
                        "  frequency: { $sum: '$metaData.search_frequency' }, " +
                        "  unique_users: { $addToSet: '$customer_id' }, " +
                        "  lastSearchedAt: { $max: '$timestamp' } " +
                        "} " +
                        "}"),

                // 2. ADD uniqueUserCount + hoursSinceLastSearch
                Document.parse("{ " +
                        "$addFields: { " +
                        "  uniqueUserCount: { $size: '$unique_users' }, " +
                        "  hoursSinceLastSearch: { " +
                        "     $divide: [ { $subtract: [ '$$NOW', '$lastSearchedAt' ] }, 3600000 ] " +
                        "  } " +
                        "} " +
                        "}"),

                // 3. RECENCY BOOST
                Document.parse("{ " +
                        "$addFields: { " +
                        "  recencyBoost: { " +
                        "     $cond: [ { $lte: [ '$hoursSinceLastSearch', 24 ] }, 5, 1 ] " +
                        "  } " +
                        "} " +
                        "}"),

                // 4. FINAL SCORE
                Document.parse("{ " +
                        "$addFields: { " +
                        "  finalScore: { " +
                        "     $add: [ " +
                        "        { $multiply: [ '$frequency', 0.7 ] }, " +
                        "        { $multiply: [ '$recencyBoost', 0.3 ] }, " +
                        "        { $multiply: [ '$uniqueUserCount', 0.2 ] } " +
                        "     ] " +
                        "  } " +
                        "} " +
                        "}"),

                Document.parse("{" +
                        "$addFields: { " +
                        "   searchQuery: '$_id'" +
                        "   }" +
                        "}"),

                Document.parse("{" +
                        "  $project: {" +
                        "    _id: 0" +
                        "  }" +
                        "}"),

                // 5. SORT
                Document.parse("{ $sort: { finalScore: -1 } }"),

                // 6. LIMIT
                Document.parse("{ $limit: " + limit + " }")
        );

        return mongoTemplate.getDb()
                .getCollection("user_search_docs")
                .aggregate(pipeline)
                .map(doc -> mongoTemplate.getConverter().read(TrendingSearch.class, doc))
                .into(new ArrayList<>());
    }
}
