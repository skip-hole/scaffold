# scaffold
这是一个微服务脚手架项目

##  实时覆盖率

### 字节码增强
-javaagent:/Users/zhanghui/Documents/agent/jacoco/lib/jacocoagent.jar=includes=*,output=tcpserver,port=6300,address=localhost,append=true

### 生成覆盖率文件
java -jar /Users/zhanghui/Documents/agent/jacoco/lib/jacococli.jar dump --address localhost --port 6300 --destfile ./jacoco_tcp_01.exec

### 生成html报告
java -jar /Users/zhanghui/Documents/agent/jacoco/lib/jacococli.jar report ./jacoco_tcp_01.exec --classfiles ./classes --sourcefiles ../src/main/java --html report01