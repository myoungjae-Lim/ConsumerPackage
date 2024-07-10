# ConsumerPackage
Personal Repository Based on Apache Kafka Consumer <br>
- This README.md is written in KOREAN <br>

### 개요
해당 레포지토리는 Apache Kafka 아키텍처의 Consumer Group 모듈을 기반으로 하고 있습니다.<br>
Apache Kafka 서버에 발행되는 대용량의 데이터를 OPC 서버로 전송하는 목적을 가지고 있습니다.<br>
(OPC 기능은 현재 구현되지 않았습니다) <br>

### 목표
- 단일 PC에서 7파티션으로부터 동시에 각각 50MB의 데이터를 매 초 수신 및 처리
- 작업의 일괄 소요 시간을 1초 내외로 교정 <br>

### 구성
- Spring Boot
- Kafka Springframework
- Aspectj(aspectjrt, aspectjweaver)
- LMAX Disruptor
- ini4j
- Log4j2 <br>

### 조직도
- (조직도는 구성 요소의 변경에 따라 수정될 수 있습니다)
![스크린샷 2024-07-10 084817](https://github.com/hooniegit/ConsumerPackage/assets/130134750/1480c5d7-b9a5-4e38-a176-a7acce2702e8)
