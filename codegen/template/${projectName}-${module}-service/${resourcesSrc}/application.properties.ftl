<#macro $ value>${r"$"}{${value}}</#macro>
server.port=${servicePort}
server.servlet.encoding.charset=UTF-8
server.servlet.encoding.enabled=true
server.servlet.encoding.force=true

# ?????????? Bean
spring.main.allow-bean-definition-overriding: true
spring.application.name=${projectName}-${module}-service
spring.jackson.date-format=java.text.SimpleDateFormat

spring.servlet.multipart.max-file-size=200MB
spring.servlet.multipart.max-request-size=256MB

# region datasoruce
spring.datasource.url=${dbUrl}
spring.datasource.username=${dbUsername}
spring.datasource.password=${dbPassword}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
# endregion
#
# region redis
spring.cache.type=redis
spring.redis.host=localhost
spring.redis.port=6379
#spring.redis.cluster.nodes=
#spring.redis.cluster.max-redirects=
spring.redis.lettuce.pool.max-active=8
spring.redis.lettuce.pool.max-wait=-1ms
spring.redis.lettuce.pool.max-idle=8
spring.redis.lettuce.pool.min-idle=0
# endregion
#
# region mybatis
mybatis.type-aliases-package=${modulePackage}.persistence.mapper
mybatis.mapper-locations=classpath:mapper/*.xml
mybatis.configuration.cache-enabled=false
mybatis.configuration.lazy-loading-enabled=false
mybatis.configuration.multiple-result-sets-enabled=true
mybatis.configuration.use-column-label=true
mybatis.configuration.use-generated-keys=false
mybatis.configuration.default-executor-type=simple
mybatis.configuration.default-statement-timeout=25000
mybatis.configuration.map-underscore-to-camel-case=true
mybatis.configuration.call-setters-on-nulls=true
# endregion
#
# region pagehelper
pagehelper.reasonable=true
pagehelper.params="count=countSql"
pagehelper.support-methods-arguments=true
# endregion

info.app.name=<@$ 'spring.application.name'/>

maple.aspect.controller-method-pattern=execution(public * ${modulePackage}.controller..*.*(..))
maple.json-format.date-time-format=yyyy-MM-dd HH:mm:ss
maple.json-format.date-format=yyyy-MM-dd
maple.json-format.time-format=HH:mm:ss
