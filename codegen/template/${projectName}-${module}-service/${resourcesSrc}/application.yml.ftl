<#macro $ value>${r"$"}{${value}}</#macro>
server:
  port: ${servicePort}
  servlet:
    encoding:
      charset: UTF-8
      enabled: true
      force: true
  
spring:
  main:
    allow-bean-definition-overriding: true # 是否允许覆盖已定义的 Bean
  application:
    name: ${projectName}-${module}-service
  jackson:
    date-format: java.text.SimpleDateFormat
  servlet:
    multipart:
      max-file-size: 200MB
      max-request-size: 256MB
  datasource:
    url: ${dbUrl}
    username: ${dbUsername}
    password: ${dbPassword}
    driver-class-name: com.mysql.cj.jdbc.Driver
  cache:
    type: redis
  redis:
    # cluster:
    #   nodes:
    #   max-redirects:
    host: localhost
    port: 6379
    lettuce:
      pool:
        max-active: 8
        max-wait: -1ms
        max-idle: 8
        min-idle: 0

mybatis:
  type-aliases-package: org.xi.maple.persistence.persistence.mapper
  mapper-locations: classpath:mapper/*.xml
  configuration:
    cache-enabled: false
    lazy-loading-enabled: false
    multiple-result-sets-enabled: true
    use-column-label: true
    use-generated-keys: false
    default-executor-type: simple
    default-statement-timeout: 25000
    map-underscore-to-camel-case: true
    call-setters-on-nulls: true

pagehelper:
  reasonable: true
  params: "count=countSql"
  support-methods-arguments: true

info:
  app:
    name: <@$ 'spring.application.name'/>

maple:
  aspect:
    controller-method-pattern: "execution(public * ${modulePackage}.controller..*.*(..))"
  json-format:
    date-time-format: yyyy-MM-dd HH:mm:ss
    date-format: yyyy-MM-dd
    time-format: HH:mm:ss