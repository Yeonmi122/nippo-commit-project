# ベースイメージ（Java）
FROM eclipse-temurin:21-jdk

# 作業ディレクトリ
WORKDIR /app

# プロジェクトコピー
COPY . .

# ビルド（jar作成）
RUN chmod +x gradlew
RUN ./gradlew build -x test

# 実行
# CMD ["java", "-jar", "gradle/wrapper/gradle-wrapper.jar"]

# 実行 (이 부분을 수정하세요!)
# 보통 build/libs 폴더 안에 '프로젝트명-0.0.1-SNAPSHOT.jar' 형태로 생성됩니다.
CMD ["java", "-jar", "build/libs/backend-0.0.1-SNAPSHOT.jar"]