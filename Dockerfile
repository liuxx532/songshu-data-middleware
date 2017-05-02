FROM harbor.product.co-mall/library/java:8

#add file.war
ADD target/songshu-data-middleware-0.0.1-SNAPSHOT.war /opt/songshu-data-middleware-0.0.1-SNAPSHOT.war
ADD entrypoint.sh /opt/entrypoint.sh

EXPOSE 8083

ENTRYPOINT ["/opt/entrypoint.sh"]
