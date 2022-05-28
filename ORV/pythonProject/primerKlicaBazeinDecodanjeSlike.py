import base64
from pymongo import MongoClient
uri = 'mongodb+srv://test:test@projekt.0habu.mongodb.net/myFirstDatabase?retryWrites=true&w=majority'
client = MongoClient(uri)
db = client.myFirstDatabase
coll1 = db.photos


for document in coll1.find({"username":"qwe"}, {}):  #Tukaj z find UUID ki bo podan kot parameter ko se bo klicala ta funckija
    string = bytes((document["image"]), encoding='utf-8')
    with open("slikaZaObdelavo.png", "wb") as fh:
        fh.write(base64.decodebytes(string))

