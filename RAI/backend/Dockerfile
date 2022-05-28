FROM node:14.4.0
WORKDIR /app
COPY package.json /app
run npm install
run npm install --save cors
run npm i node-base64-image --save
COPY . /app
CMD npm start
EXPOSE 3001
