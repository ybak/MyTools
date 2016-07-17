# 熔断器Hystrix的使用与原理
## 前言

在分布式系统中经常会出现一个基础服务不可用,造成整个系统不可用的服务雪崩现象. 为了应对服务雪崩, 我们经常会使用手动服务降级. 而Hystrix的出现,给我们提供了另一种选择.
## 服务雪崩效应的定义
服务雪崩效应是一种因 **服务提供者** 的不可用导致 **服务调用者** 的不可用,并将不可用 **逐渐放大** 的过程.

![图片描述][1]

上图中, A为服务提供者, B为A的服务调用者, C和D是B的服务调用者. 当A的不可用,引起B的不可用,并逐渐放大C和D时, 服务雪崩就形成了.

## 服务雪崩效应的形成过程
将服务雪崩的参与者简化为 **服务提供者** 和 **服务调用者**. 服务雪崩产生的过程可以分为以下三个阶段:

1. 服务提供者不可用
2. 重试加大流量
3. 服务调用者不可用

![图片描述][2]

服务雪崩的每个阶段都由不同的原因造成:

服务不可用的原因有:
 - 硬件故障
 - 程序Bug
 - 缓存击穿
 - 用户大量请求

硬件故障可能为硬件损坏造成的服务器主机宕机, 网络硬件故障造成的服务提供者的不可访问.  
缓存击穿一般发生在缓存应用重启所有缓存清空时,或者短时间内大量缓存失效时,造成超出服务提供者承受范围内的请求,引起服务提供者的不可用.  
在秒杀和大促开始前,如果准备不充分,用户也会发起大量请求也可能会造成服务提供者的不可用

重试的原因有:
 - 用户重试
 - 代码逻辑重试

在服务提供者不可用后, 用户忍受不了界面上长时间的等待,会不断刷新页面甚至提交表单.  
服务调用端的会存在大量服务超时后的重试逻辑.  
这些重试都会进一步加大请求流量.

服务调用者不可用的主要原始是:
 - 同步等待造成的资源耗尽

当服务调用者使用同步调用时, 会产生大量的等待线程占用系统资源. 当线程资源被耗尽时,服务调用者提供的服务也将不可用. 于是服务雪崩效应产生了.

## 服务雪崩的应对策略
针对造成服务雪崩的不同原因, 可以使用不同的策略应对:
1. 流量控制
2. 改进缓存模式
3. 服务自动扩容
4. 服务调用者降级服务

流量控制的具体措施包括:
 - 网关限流
 - 用户交互限流
 - 关闭重试

因为nginx的高性能, 目前一线互联网公司大量采用nginx+lua的网关进行流量控制, 由此发扬光大的openResty也被大量采用.  
用户交互限流的具体措施有: 1. 采用加载动画,提高用户的忍耐等待时间.  2. 提交按钮添加强制等待时间机制.

改进缓存模式的措施包括:
 - 缓存预加载
 - 同步改为异步刷新

服务自动扩容的措施主要有:
 - AWS的auto scaling

服务调用者降级服务的措施包括:
 - 资源隔离
 - 对依赖服务进行分类
 - 不可用服务调用快速失败

资源隔离主要是对调用服务的线程池进行隔离.  
我们根据具体业务,将依赖服务分为: 强依赖和若依赖. 强依赖服务不可用会导致当前业务中止,而弱依赖服务的不可用不会导致当前业务的中止.  
不可用服务调用快速失败一般通过 **超时机制**, **熔断器** 和熔断后的 **降级方法** 来实现.

## 使用Hystrix预防服务雪崩
**Hystrix** [hɪst'rɪks]的中文含义是豪猪, 因其背上长满了刺,而拥有自我保护能力. 而Netflix的Hystrix组件同样拥有自我保护整体系统的能力.
**Hystrix** 是一个帮助解决分布式系统交互时超时处理和容错的类库.

Hystrix的设计原则包括:
 - 资源隔离
 - 熔断器
 - 命令模式

#### 资源隔离
货船为了进行防止漏水和火灾的扩散,会将货仓分隔为多个, 如下图所示:
![图片描述][3]  
这种资源隔离减少风险的方式被称为:Bulkheads(舱壁隔离模式).  
Hystrix将同样的模式运用到了服务调用者上.

在一个高度服务化的系统中,我们实现的一个业务逻辑通常会依赖多个服务,比如:  
商品详情展示服务会依赖商品服务, 价格服务, 商品评论服务.如图所示:  
![图片描述][4]

调用三个依赖服务的线程会共享商品详情服务的线程池.当其中的商品评论服务不可用时,会出现线程池里的所有线程都在等待响应被阻塞,从而造成服务雪崩.如图所示:  
![图片描述][5]  

通过Hystrix我们将每个依赖的服务进分配固定的线程池,进行资源隔离, 从而避免服务雪崩.  
![图片描述][6]

#### 熔断器模式
熔断器模式定义了熔断器开关相互转换的逻辑:  
![图片描述][7]  
服务的健康状况= 请求失败个数 / 请求失败率.  
熔断器开关由关闭到打开的状态转换是通过当前服务健康状况和设定阈值比较决定的.  
1. 当熔断器关闭时, 允许请求通过熔断器.  如果当前健康状况高于设定阈值, 继续保持关闭. 如果当前健康状况低于设定阈值, 则切换为代开状态.  
2. 当熔断器打开时, 禁止请求通过.  
3. 当熔断器处于打开状态, 一段时间过后,熔断器自动进入半开状态, 只允许一个请求通过. 当该请求成功时, 熔断器关闭,允许接下来的请求通过.若该请求失败,熔断器继续保持打开, 不允许接下来的请求通过.  

熔断器的开关能保证Hystrix实现异常服务调用的快速失败. 并能在一段时间后继续侦测请求结果, 提供恢复服务调用的可能.

#### 命令模式

Hystrix使用命令模式(继承HystrixCommand类)来包裹具体的服务调用逻辑(run方法),并在命令模式中添加了服务调用失败后的降级逻辑(getFallback).
我们在Command的构造方法中可以定义当前服务的线程池和熔断器的相关参数, 如以下代码所示:  
```java
public class Service1HystrixCommand extends HystrixCommand<Response> {
  private Service1 service;
  private Request request;

  public Service1HystrixCommand(Service1 service, Request request){
    supper(
      Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ServiceGroup"))
          .andCommandKey(HystrixCommandKey.Factory.asKey("servcie1query"))
          .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("service1ThreadPool"))
          .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
            .withCoreSize(20))//服务线程池数量
          .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
            .withCircuitBreakerErrorThresholdPercentage(60)//熔断器关闭到打开阈值
            .withCircuitBreakerSleepWindowInMilliseconds(3000)//熔断器打开到关闭的时间窗长度
      ))
      this.service = service;
      this.request = request;
    );
  }

  @Override
  protected Response run(){
    return service1.call(request);
  }

  @Override
  protected Response getFallback(){
    return Response.dummy();
  }
}
```
在使用了Command模式构建了服务对象之后,我们依赖的服务便拥有了熔断器和线程池功能了.  
![图片描述][9]  
#### Hystrix的内部处理逻辑
下图为Hystrix服务对象工作的内部逻辑:  
![图片描述][8]  
具体执行流程为:  
1. 使用Hystrix的Command对象,并执行调用.
2. Hystrix检查当前服务的熔断器开关是否开启, 若开启, 执行降级服务getFallback方法.
3. 若熔断器开关关闭, Hystrix检查当前服务的线程池是否能接受新请求, 若超过线程池服务能力, 执行降级服务getFallback方法.
4. 若线程池接受请求, Hystrix开始执行服务调用具体逻辑
5. 若服务执行失败, 执行降级服务getFallback方法, 并将执行结果上报Metrics更新服务健康状况.
6. 若服务执行超时, 执行降级服务getFallback方法, 并将执行结果上报Metrics更新服务健康状况.
7. 若服务执行成功, 返回正常结果.
8. 若服务降级方法getFallback执行成功, 返回降级结果.
9. 若服务降级方法getFallback执行失败, 执行抛出异常.

## Hystrix metrix的实现
Hystrix的Metrics中保存了当前服务的健康状况, 比如服务调用总次数和服务调用失败次数等, 从而能计算出当前服务的调用失败率, Hystrix用改值和设定的阈值比较来决定熔断器是否由关闭切换到打开状态.它的实现非常重要.  

#### 1.4之前的滑动窗口实现
Hystrix在这些版本中的使用自己定义的滑动窗口数据结构来记录当前时间窗的各种事件(成功,失败,超时,线程池拒绝等)的计数.  
事件产生时, 数据结构根据当前时间确定使用旧桶还是创建新桶来计数, 并在桶中对计数器经行修改.  
这些修改是多线程并发执行的, 代码中有不少加锁操作,逻辑较为复杂.  
![图片描述][10]  

#### 1.5之后的滑动窗口实现
Hystrix在这些版本中开始使用RxJava的Observable.window()实现滑动窗口.
RxJava的window使用后台线程创建新桶, 避免了并发创建桶的问题.
同时RxJava的单线程无锁特性也保证了计数变更时的线程安全. 从而使代码更加简洁.  
以下为我使用RxJava的window方法实现的一个简易滑动窗口Metrics, 短短几行代码便能完成统计功能:
```java
@Test
public void timeWindowTest() throws Exception{
  Observable<Integer> source = Observable.interval(50, TimeUnit.MILLISECONDS).map(i -> RandomUtils.nextInt(2));
  source.window(1, TimeUnit.SECONDS).subscribe(window -> {
    int[] metrics = new int[2];
    window.subscribe(i -> metrics[i]++,
      InternalObservableUtils.ERROR_NOT_IMPLEMENTED,
      () -> System.out.println("窗口Metrics:" + JSON.toJSONString(metrics)));
  });
  TimeUnit.SECONDS.sleep(3);
}
```

## 总结
通过使用Hystrix,我们就能方便的防止雪崩效应, 同时具有自动降级和自动恢复服务的效果.

  [1]: /images/hystrix/雪崩效应.png
  [2]: /images/hystrix/简化雪崩.png
  [3]: /images/hystrix/舱壁隔离.png
  [4]: /images/hystrix/服务正常.png
  [5]: /images/hystrix/服务调用失败.png
  [6]: /images/hystrix/异常服务隔离.png
  [7]: /images/hystrix/熔断器开关.png
  [8]: /images/hystrix/执行流程.png
  [9]: /images/hystrix/命令模式.png
  [10]: /images/hystrix/滑动窗口.png
