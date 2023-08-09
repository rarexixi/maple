<#macro mapperEl$ value>${r"${"}${value}}</#macro>
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.xi.maple</groupId>
        <artifactId>maple-spring-dependencies</artifactId>
        <version>0.0.1-SNAPSHOT</version>
        <relativePath/>
    </parent>

    <artifactId>${projectName}-${module}-models</artifactId>
    <name>${projectName}-${module}-models</name>

    <dependencies>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
        <dependency>
            <groupId>org.xi.maple</groupId>
            <artifactId>maple-common</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
    </dependencies>

</project>