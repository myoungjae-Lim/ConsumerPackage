# ConsumerPackage
Personal Repository Based on Apache Kafka Consumer <br>
Will Transport Datas to OPC Server. <br>

### Goal
- Consume Compressed Message(30,000 Tag Grouped) from 64 Partitions <br>
- Run Tasks for Each 1 Seconds <br>

### Structure
- Spring Boot
- Kafka Springframework
- Aspectj(aspectjrt, aspectjweaver)
- LMAX Disruptor
- grpc : Not Added Yet..
- ini4j
- Log4j2 <br>

### Applications
- Consumer: Kafka Consumer Based on @Service Annotation
- ConsumerPackage: Kafka Consumer Based on Multi-Threads (Classic) <br>

# Run
### Settings
1. Update 'hosts' file
``` bash
# Windows
Notepad(Administrator) > Open > C:\Windows\System32\drivers\etc

# MacOS, Linux
sudo vi /etc/hosts
```
``` bash
# Write KAFKA Broker's Domain Name
# ex) 123.123.45.67  Workspace
000.000.000.000    <DomainName>
```

2. VM arguments
``` bash
-Xms512m 
-Xmx9192m # 1GB is Enough 
-XX:+UnlockExperimentalVMOptions # Importatn
-XX:+UseZGC # Important
-XX:+HeapDumpOnOutOfMemoryError 
-XX:HeapDumpPath=Path\To\Your\HeapDump\dump.hprof 
```