package com.hjc.community.dao.elastic;

import com.hjc.community.entity.DiscussPost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @Classname DiscussPostEsRepo
 * @Description TODO
 * @Date 2020-03-23 1:46 p.m.
 * @Created by Justin
 */
@Repository
public interface DiscussPostEsRepo extends ElasticsearchRepository<DiscussPost, Integer> {

}
