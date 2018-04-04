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
 * 可拓展新较好，可以在原有代码基础上拓展其他爬虫项目
     
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

## V0.0.2 添加序列化代理功能
 为了避免每次启动都要重新爬取代理，添加一个序列化代理的功能，当下次启动时先从本地文件中获取已经序列化的代理
 * 实现技术：
   1.在ProxyPool中添加一个Set属性，每当一个代理测试通过可用后存入到set中，没十分钟进行一次序列化<br>
   2.采用了一个读写锁技术，这里的情景是写操作远多于读操作所以使用sychronized或者ReentrantLock对效率的影响并不是特别大 <br>
### 解耦
   今天爬虫是发现，如何爬取代理和其他爬虫功能同时运行会导致同一时间过多线程在运行，正好之前也完成了代理序列化功能所以计划后面讲爬取代理功能模块分离出来，这样也可以单独拿出来用。所以今天将代理爬取和其他功能解耦
## v0.0.3 添加爬取知乎用户模块
 前面有说过因为豆瓣不论以什么方式只能获取500条记录，所以可爬取的数据还是比较少的，本想爬取豆瓣用户但是需要登陆认证我没有搞定，所以就改知乎了
 
 * 实现功能
   爬取知乎用户
 * 实现方式
  <br>
   1.知乎用户关注列表不需要我们必须登录，这就免去了登录这块麻烦的地方；<br>
   2.通过fiddler查看获取请求api以及请求头；知乎有一个auth认证这个也可以通过谷歌浏览器来获取，获取方式如下：![获取知乎auth方法](https://github.com/shanyao19940801/douban-spider/blob/master/douban-spider/src/main/resources/img/getauth.PNG "获取知乎auth")<br>
   3.获取auth后将其放到请求头头中
        <br>HttpGet request = new HttpGet(url);
        <br>request.setHeader("authorization","oauth " + ZhiHuConfig.authorization);
    <br>
    4.关于代理的获取
     前面有提到过将代理获取功能分离出来的事情，所以这里就没有动态获取代理而是单独启动一个线程去反序列化之前已经获取的代理，具体请看：
     [StartClass](https://github.com/shanyao19940801/douban-spider/blob/master/douban-spider/src/main/java/com/yao/spider/zhihu/task/ZhiHuUserListTask.java)
### 流程图
