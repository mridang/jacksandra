# Jacksandra

Jacksandra (aptly named) is a Jackson-based module for working with Cassandra rows. At the time of writing, Jacksandra can only be used for schema generation using Jackson, but a future version will add support for ser-deser to types to the corresponding "row" objects. 

## Installation

Unfortunately, Jacksandra is not available in any public Maven repositories except the GitHub Package Registry. For more information on how to install packages from the GitHub Package Registry, [https://docs.github.com/en/packages/guides/configuring-gradle-for-use-with-github-packages#installing-a-package][see the GitHub docs]

## Usage

As the Datastax Mappers provide no support for schemagen, this module is supposed to be a drop-in module to augment existing functionality provided by the existing Datastax libraries.

### Collections

#### Lists

Use `FrozenList` when possible

#### Sets

Use `FrozenList` when possible

#### Other

Implement the `Frozen` interface when possible

### Partition Keys

### Clustering Columns

Use the `@ClusteringColumn` annotation or the `@OrderedClusteringColumn` to denote that a column is a part of the clustering key.

### Static Columns

Use the `@Static` annotation to denote static columns


## License

Apache License

Copyright (c) 2021 Mridang Agarwalla

[see the GitHub docs]: https://docs.github.com/en/packages/guides/configuring-gradle-for-use-with-github-packages#installing-a-package
