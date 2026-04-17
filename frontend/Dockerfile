# Node環境
FROM node:20-alpine

WORKDIR /app

# 依存関係インストール
COPY package.json package-lock.json ./
RUN npm install vite --save-dev

# ソースコピー
COPY . .

# 開発サーバー起動
CMD ["npm", "run", "dev"]