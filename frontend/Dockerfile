ARG DOCKER_REGISTRY=docker.io

FROM ${DOCKER_REGISTRY}/library/node:18-alpine AS build

WORKDIR /app

COPY package.json package-lock.json ./

RUN npm config set registry https://mirrors.cloud.tencent.com/npm/ && \
    npm ci --quiet

COPY . .

RUN npm run build

FROM ${DOCKER_REGISTRY}/library/nginx:alpine

COPY nginx.conf /etc/nginx/nginx.conf

COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80