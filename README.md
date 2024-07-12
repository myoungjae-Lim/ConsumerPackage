# ConsumerPackage
Personal Repository Based on Apache Kafka Consumer <br>
(This README.md is written in KOREAN) <br>

# 개요
해당 레포지토리는 Apache Kafka 아키텍처의 Consumer Group 모듈을 기반으로 하고 있습니다.<br>
Apache Kafka 서버에 발행되는 대용량의 데이터를 OPC 서버로 전송하는 목적을 가지고 있습니다.<br>
(OPC 기능은 현재 구현되지 않았습니다) <br>

### 목표
- 단일 PC에서 7파티션으로부터 동시에 각각 30000 Tag 데이터를 매 초 수신 및 처리
- 작업의 일괄 소요 시간을 1초 내외로 교정 <br>

### 구성
- Spring Boot
- Kafka Springframework
- Aspectj(aspectjrt, aspectjweaver)
- LMAX Disruptor
- ini4j
- Log4j2 <br>

### 사전 설정
1. hosts 파일 업데이트
``` bash
# Windows의 경우
메모장(관리자 권한으로 실행) > 열기 > C:\Windows\System32\drivers\etc

# MacOS, Linux의 경우
sudo vi /etc/hosts
```
``` bash
# Kafka 서버가 사용하는 도메인 이름을 입력합니다.
# 예) 123.123.45.67  Workspace
000.000.000.000    <DomainName>
```
