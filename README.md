# sch_sw_notice
순천향대 포털 공지 와 SW산업단 공지 올려주는 디코봇

## 사용법
### 1. token 발급
https://discord.com/developers/applications
### 2. .env 파일
```shell
BOT_TOKEN={YOUR_BOT_TOKEN}
DB_PASSWORD={DB_PW}
DB_NAME={DB_NAME}
```
### 3. jar 파일 배치
app이란 폴더를 만들고 [release](https://github.com/found-cake/sch_sw_notice/releases/latest)에 있는 최신 jar파일을 넣는다.
### 4. docker 실행
```shell
docker-compose -f docker-compose.prod.yml up
```
java 에서 mysql을 연결 못해 처음에 오류가 날 수 있습니다.
