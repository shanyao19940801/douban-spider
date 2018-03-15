# 豆瓣爬虫

豆瓣爬虫是一个基于爬取豆瓣各种信息的项目

## 项目初衷
  因为之前在学习并发编程，拜读了《并发变成实战》以及《并发编程艺术》后决定写个项目来巩固一下学到的东西，所以就有写一个多线程爬虫的项目的想法，当然还有另一个原因我觉得爬取大量别人数据好爽啊（难道我是潜在的偷窥狂？！）
## V0.0.1爬取豆瓣电影
  当前版本可以爬取豆瓣电影，后续计划添加音乐，书籍等爬虫功能，最终目标完成一个通用功能只需要一些简单的配置就可以爬取任何信息，甚至其他网站信息（注意：发现豆瓣反扒方式相当无解，无论你以什么方式去查询什么信息只展示前500，所以只能通过各种标签尽可能的爬取更多的信息）>
### 项目主要功能特点
 * 使用HTTP代理
     <br>项目首先会到ip网站获取大量ip，使用的这些ip创建虚拟代理突破网页单个代理访问限制问题
     <br>编写的时候我特意将爬取代理的功能和其他功能解耦了，所以这个功能可以单独拿出来用哦
 * 多线程，高并发进行,效率更快
     
### 爬取的数据
 * 下面是爬取的电影信息（部分信息需要深度爬虫才能获取，本项目中该功能已实现，但暂时未去爬取）
![电影](https://github.com/shanyao19940801/douban-spider/blob/master/douban-spider/src/main/resources/img/movedata.PNG "豆瓣电影数据示例")
### 开发工具以及框架
 * java开发工具：IntelliJ IDEA
 * 项目管理：Maven
 * 版本管理：GitHub
 * 使用数据库： MYSQL5.7
 * 持久层框架：Mybatis
 * 第三方库
    <br>HttpClient4.5-网络请求
    <br>Jsoup-html标签解析
    <br>c3p0 数据库连接池
### 持久化配置
 * 本项目使用MYSQL数据库来保存数据，运行项目之前请自行安装mysql，并在[jdbc.properties](https://github.com/shanyao19940801/douban-spider/blob/master/douban-spider/src/main/resources/jdbc.properties)中配置自己的数据库信息
 
### Quick Start
Run With [StartClass](https://github.com/shanyao19940801/douban-spider/blob/master/douban-spider/src/main/java/com.yao/douban/StartClass.java)

### 流程图
