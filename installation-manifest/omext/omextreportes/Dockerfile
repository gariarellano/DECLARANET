FROM node:14.10.0

RUN mkdir /app
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm install --production

COPY . .

EXPOSE 5000
CMD npm start