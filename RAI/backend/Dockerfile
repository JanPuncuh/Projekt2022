FROM node:14.4.0
WORKDIR /app
COPY package.json /app
run npm install
run npm install --save cors
run npm i await-spawn -S

run apt-get update
run apt-get install -y python3-pip
run apt-get install python-pip -y

run pip install pybase64
run pip install pymongo[srv]
run pip install scikit-build
run pip2 install opencv-python==4.2.0.32
run pip install pathlib
run pip install scikit-learn
run pip install pandas

run pip3 install scikit-learn-pipeline-utils
run pip3 install scikit-build
run pip3 install pymongo[srv]
run pip3 install opencv-python==4.2.0.32


COPY . /app

CMD npm start

EXPOSE 3001


