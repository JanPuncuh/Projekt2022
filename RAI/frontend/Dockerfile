FROM node:14.4.0
WORKDIR /app
COPY package.json /app
run npm install
run npm i --save moment
COPY . /app
CMD npm start
EXPOSE 3000
