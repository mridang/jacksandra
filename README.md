# Jacksandra

Jacksandra (aptly named) is a Jackson-based module for working with Cassandra rows. At the time of writing, Jacksandra can only be used for schema generation using Jackson, but a future version will add support for ser-deser to types to the corresponding "row" objects. 

## Installation

Unfortunately, Jacksandra is not available in any public Maven repositories except the GitHub Package Registry. For more information on how to install packages from the GitHub Package Registry, [https://docs.github.com/en/packages/guides/configuring-gradle-for-use-with-github-packages#installing-a-package][see the GitHub docs]

## Usage

As the Datastax Mappers provide no support for schemagen, this module is supposed to be a drop-in module to augment existing functionality provided by the existing Datastax libraries.

Assume you have the following bean for which you would like to generate the schema:

```java
import java.util.Date;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import com.mridang.jacksandra.annotations.OrderedClusteringColumn;
import com.mridang.jacksandra.types.FrozenList;

@Entity(defaultKeyspace = "playcart")
@CqlName("imagerelations")
public class CassandraProductSimilarity {

    @SuppressWarnings("DefaultAnnotationParam")
    @PartitionKey(0)
    @CqlName("merchant")
    public String merchant;

    @SuppressWarnings("DefaultAnnotationParam")
    @OrderedClusteringColumn(isAscending = false, value = 0)
    @CqlName("createdat")
    public Date createdAt;

    @OrderedClusteringColumn(isAscending = true, value = 1)
    @CqlName("productid")
    public String productId;

    @CqlName("related")
    public FrozenList<CassandraRelation> productRelations;

    @CqlName("productrelation")
    public static class CassandraRelation {

        @CqlName("merchant")
        public String merchant;

        @CqlName("productid")
        public String productId;

        @CqlName("score")
        public Float score;
    }
}
```

To generate the schema for the bean described above, you would run:

```scala
    val mapper = new CassandraJavaBeanMapper[CassandraProductSimilarity]()
    val createSchema: String = mapper.generateMappingProperties
```

to yield the DDL:

```sql
CREATE 
  TYPE 
IF NOT 
EXISTS productrelation 
     ( score FLOAT
     , productid TEXT
     , merchant TEXT
     );

CREATE 
 TABLE 
IF NOT 
EXISTS imagerelations 
     ( merchant TEXT
     , shardkey TINTINT
     , createdat TEXT
     , productid TEXT
     , related LIST<productrelation>
     , PRIMARY KEY
       ( ( merchant
         , shardkey
          )
       , createdat
       , productid
       )
    )
  WITH CLUSTERING 
 ORDER 
    BY 
     ( createdat DESC
     , productid ASC
     );
```

### Collections

Jacksandra supports all collection types including the "frozen" variants.

#### Lists

Any property that derives from `java.util.List` will be mapped as a `LIST` data type. If you require a "frozen" representation, use `FrozenList` when possible.

#### Sets

Any property that derives from `java.util.Set` will be mapped as a `SET` data type. If you require a "frozen" representation, use `FrozenSet` when possible.

#### Other

Any property that derives from `java.util.Collection` will be mapped as a `LIST` data type. If you require a "frozen" representation, use any collection type simply implement the `Frozen` interface.

### Partition Keys

### Clustering Columns

Use the `@ClusteringColumn` annotation or the `@OrderedClusteringColumn` to denote that a column is a part of the clustering key.

### Static Columns

Use the `@Static` annotation to denote static columns


## License

Apache License

Copyright (c) 2021 Mridang Agarwalla

[see the GitHub docs]: https://docs.github.com/en/packages/guides/configuring-gradle-for-use-with-github-packages#installing-a-package
