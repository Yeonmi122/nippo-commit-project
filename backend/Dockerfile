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
CMD ["java", "-jar", "gradle/wrapper/gradle-wrapper.jar"]