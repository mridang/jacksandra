package com.mridang.jacksandra;

import java.util.Date;
import java.util.List;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import com.mridang.jacksandra.annotations.OrderedClusteringColumn;

@SuppressWarnings("unused")
@Entity(defaultKeyspace = "mykeyspace")
public class BeanWithSimpleUDT {

    @SuppressWarnings("DefaultAnnotationParam")
    @PartitionKey(0)
    @CqlName("merchant")
    public final String merchant;

    // Notice the case - lower, no delimiters
    @PartitionKey(1)
    @CqlName("shardkey")
    public final Byte shardKey;

    // Notice the case - lower, no delimiters
    @SuppressWarnings("DefaultAnnotationParam")
    @OrderedClusteringColumn(isAscending = false, value = 0)
    @CqlName("createdat")
    public final Date createdAt;

    // Notice the case - lower, no delimiters
    @OrderedClusteringColumn(isAscending = true, value = 1)
    @CqlName("productid")
    public final String productId;

    // Notice the case - lower, no delimiters
    @CqlName("related")
    public final List<CassandraRelation> productRelations;

    public BeanWithSimpleUDT(
            @CqlName("merchant") String merchant,
            @CqlName("productid") String productid,
            @CqlName("shardkey") Byte shardkey,
            @CqlName("createdat") Date createdat,
            @CqlName("related") List<CassandraRelation> related) {
        this.shardKey = shardkey;
        this.merchant = merchant;
        this.productId = productid;
        this.createdAt = createdat;
        this.productRelations = related;
    }

    @CqlName("shardkey")
    public Byte getShardKey() {
        return shardKey;
    }

    @CqlName("createdat")
    public Date getCreatedAt() {
        return createdAt;
    }

    @CqlName("merchant")
    public String getMerchant() {
        return merchant;
    }

    @CqlName("productid")
    public String getProductId() {
        return productId;
    }

    @CqlName("related")
    public List<CassandraRelation> getProductRelations() {
        return productRelations;
    }

    @CqlName("productrelation")
    public static class CassandraRelation {

        // Notice the case - lower, no delimiters
        @CqlName("merchant")
        public String merchant;

        // Notice the case - lower, no delimiters
        @CqlName("productid")
        public String productId;

        // Notice the case - lower, no delimiters
        @CqlName("score")
        public Float score;

        public CassandraRelation(
                @CqlName("merchant") String merchant,
                @CqlName("productid") String productid,
                @CqlName("score") Float score) {
            this.merchant = merchant;
            this.productId = productid;
            this.score = score;
        }

        @CqlName("merchant")
        public String getMerchant() {
            return merchant;
        }

        @CqlName("productid")
        public String getProductId() {
            return productId;
        }

        @CqlName("score")
        public Float getScore() {
            return score;
        }
    }
}
