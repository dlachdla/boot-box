# 베이스 이미지
FROM amazoncorretto:17
# 작성자 라벨 생성
LABEL maintainer "ryujeongho<ryujh0326@gmail.com>"
# 버전 라벨 생성
LABEL version "1.0.0"
# build시에만 사용할 변수 선언
ARG JAR_FILE_PATH=build/libs/*.jar
ARG PROD_DB_PASSWORD
ARG PROD_DB_URL
ARG PROD_DB_USERNAME
ARG PROD_REDIS_HOST
ARG PROD_REDIS_PORT
ARG PROD_REDIS_PASSWORD
ARG IMP_CODE
ARG IMP_API_KEY
ARG IMP_API_SECRETKEY
ARG api_kmdb_key
ARG api_kobis_key
ARG api_tmdb_key
ARG api_youtube_key
# root 디렉토리에 app.jar복사
COPY ${JAR_FILE_PATH} app.jar
# 지갑 파일을 루트하위에 복사
COPY wallets/Wallet_WHI0M2VF43SUZ8LY /Wallet_WHI0M2VF43SUZ8LY
# docker 이미지에 저장할 환경변수
ENV PROD_DB_PASSWORD=${PROD_DB_PASSWORD}
ENV PROD_DB_URL=${PROD_DB_URL}
ENV PROD_DB_USERNAME=${PROD_DB_USERNAME}
ENV PROD_REDIS_HOST=${PROD_REDIS_HOST}
ENV PROD_REDIS_PORT=${PROD_REDIS_PORT}
ENV PROD_REDIS_PASSWORD=${PROD_REDIS_PASSWORD}
ENV IMP_CODE=${IMP_CODE}
ENV IMP_API_KEY=${IMP_API_KEY}
ENV IMP_API_SECRETKEY=${IMP_API_SECRETKEY}
ENV API_KMDB_KEY=${api_kmdb_key}
ENV API_KOBIS_KEY=${api_kobis_key}
ENV API_TMDB_KEY=${api_tmdb_key}
ENV API_YOUTUBE_KEY=${api_youtube_key}
# 컨테이너 실행 시 app.jar 자동 실행 (프로필 설정)
ENTRYPOINT ["java", "-jar", "/app.jar"]
