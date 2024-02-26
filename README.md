# Tiny Token SpringBoot Starter (SpringBoot 3.x Lite Version) 轻量级权限认证SpringBoot启动器(SpringBoot 3.x 精简版)

[![License](https://img.shields.io/github/license/ALI1416/tiny-token-spring-boot-starter?label=License)](https://www.apache.org/licenses/LICENSE-2.0.txt)
[![Java Support](https://img.shields.io/badge/Java-8+-green)](https://openjdk.org/)
[![Maven Central](https://img.shields.io/maven-central/v/cn.404z/tiny-token-spring-boot-starter?label=Maven%20Central)](https://mvnrepository.com/artifact/cn.404z/tiny-token-spring-boot-starter)
[![Tag](https://img.shields.io/github/v/tag/ALI1416/tiny-token-spring-boot-starter?label=Tag)](https://github.com/ALI1416/tiny-token-spring-boot-starter/tags)
[![Repo Size](https://img.shields.io/github/repo-size/ALI1416/tiny-token-spring-boot-starter?label=Repo%20Size&color=success)](https://github.com/ALI1416/tiny-token-spring-boot-starter/archive/refs/heads/v3.lite.zip)

[![Java CI](https://github.com/ALI1416/tiny-token-spring-boot-starter/actions/workflows/ci.yml/badge.svg?branch=lite)](https://github.com/ALI1416/tiny-token-spring-boot-starter/actions/workflows/ci.yml)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=ALI1416_tiny-token-spring-boot-starter&metric=coverage)
![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=ALI1416_tiny-token-spring-boot-starter&metric=reliability_rating)
![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=ALI1416_tiny-token-spring-boot-starter&metric=sqale_rating)
![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=ALI1416_tiny-token-spring-boot-starter&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=ALI1416_tiny-token-spring-boot-starter)

## 简介

轻量级权限认证SpringBoot实现，使用Redis、雪花ID、Base62等技术

### 支持版本

- [主线版本](https://github.com/ALI1416/tiny-token-spring-boot-starter/tree/master)
- [兼容模式](https://github.com/ALI1416/tiny-token-spring-boot-starter/tree/low) 支持`SpringBoot 2.7.0`以下版本
- [精简版](https://github.com/ALI1416/tiny-token-spring-boot-starter/tree/lite) 去除`id`字段、`拓展内容`字段、新增`用户名密码校验`
- [兼容模式 精简版](https://github.com/ALI1416/tiny-token-spring-boot-starter/tree/low.lite)
- [基本认证版](https://github.com/ALI1416/tiny-token-spring-boot-starter/tree/auth) 仅提供`Basic Authentication`认证
- [SpringBoot 3.x版](https://github.com/ALI1416/tiny-token-spring-boot-starter/tree/v3) 支持`SpringBoot 3.0.0`及以上版本
- [SpringBoot 3.x 精简版](https://github.com/ALI1416/tiny-token-spring-boot-starter/tree/v3.lite)
- [SpringBoot 3.x 基本认证版](https://github.com/ALI1416/tiny-token-spring-boot-starter/tree/v3.auth)

## 依赖导入

```xml
<dependency>
  <groupId>cn.404z</groupId>
  <artifactId>tiny-token-spring-boot-starter</artifactId>
  <version>1.6.3.v3.lite</version>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
  <version>3.2.2</version>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-redis</artifactId>
  <version>3.2.2</version>
</dependency>
<dependency>
  <groupId>cn.404z</groupId>
  <artifactId>id-spring-boot-autoconfigure</artifactId>
  <version>3.1.2</version>
</dependency>
```

## 使用方法

```java
// 注入
private final T4s t4s;
// 用户名和密码是否正确
t4s.isCorrect(username, password);
// 设置token(token使用16位随机字符串 过期时间使用默认值)
t4s.setToken();
// 获取token(当前Context 不判断是否有效)
t4s.getToken();
// 获取token(当前Context 判断是否有效)
t4s.getTokenValid();
// 删除(当前Context)
t4s.deleteByToken();
// 设置过期时间(当前Context 过期时间使用默认值)
t4s.expire();
// 设置永不过期(当前Context)
t4s.persist();
// 获取信息(当前Context)
t4s.getInfoByToken();
```

更多请见[测试](./test/tiny-token-spring-boot-starter-test)

## 更新日志

[点击查看](./CHANGELOG.md)

## 关于

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="https://www.404z.cn/images/about.dark.svg">
  <img alt="About" src="https://www.404z.cn/images/about.light.svg">
</picture>
