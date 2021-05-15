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

@Entity(defaultKeyspace = "mykeyspace")
@CqlName("brandsimilarities")
public class BrandSimilarities {

    @SuppressWarnings("DefaultAnnotationParam")
    @PartitionKey(0)
    @CqlName("brand")
    public String brand;

    @SuppressWarnings("DefaultAnnotationParam")
    @OrderedClusteringColumn(isAscending = false, value = 0)
    @CqlName("createdat")
    public Date createdAt;

    @OrderedClusteringColumn(isAscending = true, value = 1)
    @CqlName("skuid")
    public String skuId;

    @CqlName("related")
    public FrozenList<Relation> productRelations;

    @CqlName("relation")
    public static class Relation {

        @CqlName("brand")
        public String brand;

        @CqlName("skuid")
        public String skuId;

        @CqlName("score")
        public Float score;
    }
}
```

To generate the schema for the bean described above, you would run:

```scala
    val mapper = new CassandraJavaBeanMapper[BrandSimilarities]()
    val createSchema: String = mapper.generateMappingProperties
```

to yield the DDL:

```sql
CREATE 
  TYPE 
IF NOT 
EXISTS relation 
     ( score FLOAT
     , skuid TEXT
     , brand TEXT
     );

CREATE 
 TABLE 
IF NOT 
EXISTS brandsimilarities 
     ( brand TEXT
     , shardkey TINTINT
     , createdat TEXT
     , skuid TEXT
     , related LIST<FROZEN<relation>>
     , PRIMARY KEY
       ( ( brand
         , shardkey
          )
       , createdat
       , skuid
       )
    )
  WITH CLUSTERING 
 ORDER 
    BY 
     ( createdat DESC
     , skuid ASC
     );
```

### Collections

Jacksandra supports all collection types including the "frozen" variants.

#### Lists

Any property that derives from `java.util.List` will be mapped 
as a `LIST` data type. If you require a "frozen" representation, 
use `FrozenList` when possible.

#### Sets

Any property that derives from `java.util.Set` will be mapped 
as a `SET` data type. If you require a "frozen" representation, 
use `FrozenSet` when possible.

#### Other

### Maps

At the time of writing Jacksandra does not support mapping map types.

Any property that derives from `java.util.Collection` will be mapped 
as a `LIST` data type. If you require a "frozen" representation, 
use any collection type simply implement the `Frozen` interface.

### Partition Keys

Use the `@PartitionKey` annotation to denote the partition keys. 

One more properties of a schema must have a `@PartitionKey` annotation.

### Clustering Columns

Use the `@ClusteringColumn` annotation or the `@OrderedClusteringColumn` to denote that a column is a part of the clustering key. 
The custom  `@OrderedClusteringColumn` annotation has been added as 
the `com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn` annotation provided by 
the Datastax libraries don't support specifying the clustering order.

A schema may or may not have a `@OrderedClusteringColumn` annotation or the `@ClusteringColumn` 
annotation at all static columns are optional.

### Static Columns

Use the `@StaticColumn` annotation to denote static columns. The custom `@StaticColumn`
annotation is provided as there doesn't seem to be corresponding annotation in
the Datastax libraries.

A schema may or may not have a `@StaticColumn` annotation at all static columns are optional.


## License

MIT License

Copyright (c) 2021 Mridang Agarwalla

[see the GitHub docs]: https://docs.github.com/en/packages/guides/configuring-gradle-for-use-with-github-packages#installing-a-package
