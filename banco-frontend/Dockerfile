### --- COMPILAR ANGULAR --- ###
FROM node:20 AS build
WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .
RUN npm run build --prod

### --- SERVIR CON NGINX --- ###
FROM nginx:1.25-alpine

# Copiar build Angular
COPY --from=build /app/dist/banco-frontend/browser /usr/share/nginx/html

# Redirección para navegación Angular
COPY ./nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
