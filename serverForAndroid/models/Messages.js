import { MongoClient, ObjectId } from 'mongodb';
import admin from 'firebase-admin';
import serviceAccount from '../targil4-ap2-firebase-adminsdk-wwcm3-8d57648f8e.json' assert { type: "json" };
admin.initializeApp({
    credential: admin.credential.cert(serviceAccount)
  });
// Initialize the Firebase Admin SDK
    function getCurrentTime() {
    const currentTime = new Date();
    return currentTime;
  }
  async function sendPushNotification(fireBaseToken, message,username) {
    try {
        // Create the notification payload
        let title= "New message from " + username;
        const payload = {
            notification: {
                title: title,
                body: message,
            },
            token:fireBaseToken
        };
        // Send the notification to the recipient token
        const response = await admin.messaging().send(payload);
    } catch (error) {
        console.error('Error sending push notification:', error);
    }
}

async function getMessages(username,id) {
    const client= new MongoClient('mongodb://127.0.0.1:27017');
    let masseges=[];
    let returnSituation=1;
    try {
        const db= client.db("whatsapp");
        const usersCollection=db.collection("users");
        const user=await usersCollection.findOne({"username": username});
        if(user===null) {
            returnSituation=2;
        } else {
            const chatsCollection=db.collection("chats");
            const messgaesCollection=db.collection("messages");
            const objectId = new ObjectId(id);
            let chat = await chatsCollection.findOne({_id: objectId});
            if(chat===null) {
                returnSituation=3;
            } else {
                for(let i=0; i<chat.msgId.length;i++) {
                    let oneMassege= await messgaesCollection.findOne({_id : chat.msgId[i]});
                    masseges.push(oneMassege);
                }
            }
        }
    }
    finally {
        await client.close(); 
        if(returnSituation===1) {
            return masseges;
        } else {
            return returnSituation;
        }
    }
  
}
async function postMessages(userName,id,content) {
    const client= new MongoClient('mongodb://127.0.0.1:27017');
    let returnSituation=1;
    try {
        const db= client.db("whatsapp");
        const usersCollection=db.collection("users");
        const fireBaseCollection=db.collection("fireBase");
        const user=await usersCollection.findOne({"username": userName});
        if(user===null) {
            returnSituation=2;
        } else {
            const chatsCollection=db.collection("chats");
            const messgaesCollection=db.collection("messages");
            const chat =await chatsCollection.findOne({_id: new ObjectId(id)});
            if(chat===null) {
                returnSituation=3;
            } else {
                let fireBaseObject;
                if(chat.user1.username===userName) {
                     fireBaseObject= await fireBaseCollection.findOne({"username": chat.user2.username});
                } else {
                     fireBaseObject= await fireBaseCollection.findOne({"username": chat.user1.username});
                }

                if(!(fireBaseObject===null)) {
                    sendPushNotification(fireBaseObject.token,content,userName);
                }
                const time= getCurrentTime();
                const result=await messgaesCollection.insertOne({"sender" : {"username" : userName}, "content" : content, "created" : time});
                const update = { $push: { msgId: result.insertedId } };
                await chatsCollection.updateOne( { _id: new ObjectId(id)}, update);
                await chatsCollection.updateOne( { _id: new ObjectId(id) }, {$set : {lastMessage : {"id" : result.insertedId, "created" : time, "content" : content }}}); 
            }     
        }
    }
    finally {
        await client.close();    
        return returnSituation;
    }
  
}

export default {
    getMessages,
    postMessages
}
