# Node環境
FROM node:20-alpine

WORKDIR /app

# 依存関係インストール
COPY package*.json ./
RUN npm install

# ソースコピー
COPY . .

# 開発サーバー起動
CMD ["npm", "run", "dev"]