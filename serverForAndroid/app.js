import express from 'express'
import bodyParser from 'body-parser'
import routerToken from './routes/Tokens.js'
import routerChats from './routes/Chats.js'
import routerUsers from './routes/Users.js'
import { MongoClient, ObjectId } from 'mongodb';
import http from 'http';
import { Server } from 'socket.io';
const server= express();
//const server2 = http.createServer(server);
const io = new Server(8080,{
    cors : {origin: ["http://localhost:5000"]}
});

server.use("/",express.static('public'));
server.use("/register",express.static('public'));

server.use(express.json());
server.use(bodyParser.urlencoded({extended: true}));
server.use('/api/Tokens',routerToken);
server.use('/api/Chats',routerChats);
server.use('/api/Users',routerUsers);
io.on('connection', (socket) => {
    socket.on('msg', async (chatid) => {
        let user1;
        let user2;
        const client= new MongoClient('mongodb://127.0.0.1:27017');
        let masseges=[];
        try{
            const db= client.db("whatsapp");
            const chatsCollection=db.collection("chats");
            const messgaesCollection=db.collection("messages");
            const objectId = new ObjectId(chatid);
            let chat = await chatsCollection.findOne({_id: objectId});
            if(chat===null) {

                const returnSituation=3;
            } else {
                user1 = chat.user1.username;
                user2 = chat.user2.username;
                for(let i=0; i<chat.msgId.length;i++) {
                    let oneMassege= await messgaesCollection.findOne({_id : chat.msgId[i]});
                    masseges.push(oneMassege);
                }
            }
        }
        finally{
            const answer = {'user1': user1, 'user2':user2, 'msg': masseges};
            socket.broadcast.emit( 'msg' , answer);
        }
    });
});
server.listen(5000);