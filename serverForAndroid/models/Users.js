import { MongoClient } from 'mongodb';
 async function postUsers(username,password,displayName,profilePic) {
    let answer=true;
    const client= new MongoClient('mongodb://127.0.0.1:27017');
    try {
        const db= client.db("whatsapp");
        const users=db.collection("users");
        let result = await users.findOne({username: username});
        if(result===null) {
            await users.insertOne({username : username, password : password, displayName : displayName, profilePic : profilePic  });
        } else {
            answer=false;
        }

    }
    finally{
        await client.close();    
        return answer;
    }
} 

 async function getUsers(id) {
    const client= new MongoClient('mongodb://127.0.0.1:27017');
    let result;
    try {
        const db= client.db("whatsapp");
        const users=db.collection("users");
         result = await users.findOne({"username": id});
    }
    finally{
        await client.close();  
        if(result===null) {
            return false;
        } else {
            return {"username" : result.username, "displayName" : result.displayName, "profilePic" : result.profilePic}; 
        }
    }
}


export default {
    postUsers,
    getUsers
}