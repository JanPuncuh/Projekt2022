FROM node:14.4.0
WORKDIR /app
COPY package.json /app
run npm install
run npm install --save cors
COPY . /app
CMD npm start
EXPOSE 3001
