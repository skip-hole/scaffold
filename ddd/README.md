# 领域驱动设计目录结构

interfaces              接口层

    handler             外部事件接受层
    rest                restful 协议
        controller      rest控制器
        dto             数据传输层
            request     controller 输入数据
            response    controller 输出数据
    rpc                 rpc 协议层
        ......
application             应用层

    bound               外部服务对接层
        adapter         外部服务名称
            handler     外部事件接受层
            service     外部服务
            dto    
    internal            内部服务
        service         应用服务 聚合领域模型和领域服务  
domain                  领域层

    command             命令
    event               领域事件
    model               领域模型
    service             领域服务
    repository          仓储层接口
    factory             工厂类 数据转换
infrastructure          基础设施

    repository#impl     仓储层实现
    configs             配置类           
   