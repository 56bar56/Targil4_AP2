import { MongoClient } from 'mongodb';

async function getChats(userName) {
    const client= new MongoClient('mongodb://127.0.0.1:27017');
    let returnChats=[];
    let tokenOk=true;
    try {
        const db= client.db("whatsapp");
        const usersCollection=db.collection("users");
        let user= await usersCollection.findOne({"username": userName});
        if(user===null) {
            tokenOk=false;
        } else {
            const chatsCollection=db.collection("chats");
            let chat1 = await chatsCollection.find({"user1.username": userName}).toArray();
            let chat2 = await chatsCollection.find({"user2.username": userName}).toArray();
            let chats=[...chat1, ...chat2];
            for (let i=0; i<chats.length;i++) {
                let chat;
                if(chats[i].user1.username===userName) {
                    chat={"id": chats[i]._id,  "user" : chats[i].user2, "lastMessage" : chats[i].lastMessage }
                } else {
                    chat={"id": chats[i]._id, "user" : chats[i].user1, "lastMessage" : chats[i].lastMessage }
                }
                returnChats.push(chat);
            }
        }
    }
    finally {
        await client.close(); 
        if(tokenOk) {
            return returnChats;
        }
        return false;
    }
}
async function postChats(userName, newUser) {
    const client= new MongoClient('mongodb://127.0.0.1:27017');
    let id;
    let con;
    let returnSituation=1;
    try {
        const db= client.db("whatsapp");
        const user=db.collection("users");
        const chat=db.collection("chats");
        let inCon=await user.findOne({"username": userName});
        if(inCon===null) {
            returnSituation=2;
        } else {
            con = await user.findOne({"username": newUser});
            if(con===null) {
                returnSituation=3;
            } else {
                await chat.insertOne({"user1" : {"username" : userName, "displayName" : inCon.displayName, "profilePic" : inCon.profilePic},
                "user2" : {"username" : newUser, "displayName" : con.displayName, "profilePic" : con.profilePic}, 
                "lastMessage" : null, "msgId" : []});
                let newChat = await chat.findOne({"user1.username": userName});
                id= newChat._id;
            }
        }
    }
    finally {
        await client.close();  
        if(returnSituation===1) {
            return {"id": id, "user" : {"username" : newUser, "displayName" : con.displayName, "profilePic" : con.profilePic}};  
        } else {
            if(returnSituation===2) {
                return 1;
            } else {
                return 2;
            }
        }
    }
}

export default {
    getChats,
    postChats
}