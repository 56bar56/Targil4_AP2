import { MongoClient } from 'mongodb';
import jwt from "jsonwebtoken";
const key="our key";
 async function getToken(username,password) {
    let answer=false;
    const client= new MongoClient('mongodb://127.0.0.1:27017');
    try {
        const db= client.db("whatsapp");
        const users=db.collection("users");
        const query = { username, password };
        let result = await users.findOne(query);
        if(result===null) {
            answer=false;
        } else {
            answer=true;
        }

    }
    finally{
        await client.close();    
         if(answer) {
            const data = { username: username };
            const token = jwt.sign(data, key);
            return token;
        } else {
        return 'Incorrect username and/or password'
      }
    }
}   


export default {
    getToken
}