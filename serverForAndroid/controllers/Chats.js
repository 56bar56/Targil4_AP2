import Chats from '../models/Chats.js'
import jwt from "jsonwebtoken"
const key="our key";
export async function getChats(req,res) {
    if (req.headers.authorization) {
        // Extract the token from that header
        const token = req.headers.authorization.split(" ")[1];
        try {
        // Verify the token is valid
        const data = jwt.verify(token, key);
        const username= data.username; 
        const chats= await Chats.getChats(username);
        if(chats===false) {
            res.status(401).send("Invalid Token");
        } else {
            res.status(200).send(chats);
        }
        } catch (err) {
        res.status(401).send("Invalid Token");
        }
    }
    else {
     res.status(403).send('Token required');
    }     

}

export async function postChats(req,res) {
    if (req.headers.authorization) {
        // Extract the token from that header
        const token = req.headers.authorization.split(" ")[1];
        try {
        // Verify the token is valid
        const data = jwt.verify(token, key);
        const username= data.username;  
        let ret= await Chats.postChats(username,req.body.username);
        if(ret===1) {
            res.status(401).send("Invalid Token");
        } else {
            if (ret===2) {
                res.status(409).send("No such user");
            } else {
                res.status(200).send(ret);
            }
        }
    
        } catch (err) {

        res.status(401).send("Invalid Token");
        return
        }
    }
    else {

     res.status(403).send('Token required');
     return
    }    
}
 
