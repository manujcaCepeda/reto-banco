# banco-frontend/Dockerfile
# Stage 1: build angular
FROM node:20-alpine AS build
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm ci
COPY . .
RUN npm run build -- --configuration=production

# Stage 2: nginx serve
FROM nginx:alpine
COPY --from=build /app/dist/tu-app-angular /usr/share/nginx/html
# Opcional: tu propio conf de nginx:
# COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
