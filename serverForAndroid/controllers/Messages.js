import Messages from '../models/Messages.js'
import jwt from "jsonwebtoken"
const key="our key";
export async function getMessages(req,res) {
    if (req.headers.authorization) {
        // Extract the token from that header
        const token = req.headers.authorization.split(" ")[1];
       try {
        // Verify the token is valid
        const data = jwt.verify(token, key);
        const username= data.username; 
        const returnValue= await Messages.getMessages(username,req.params.id);
        if(returnValue===2) {
            res.status(401).send("Invalid Token");
        } else {
            if(returnValue===3) {
                res.status(409).send("we dont have this id");
            } else {
                res.status(200).send(returnValue);
            }
        }
        
        } catch (err) {
        res.status(401).send("Invalid Token");
        }
    }
    else {
        res.status(403).send('Token required');
    }    
}


export async function postMessages(req,res) {
    if (req.headers.authorization) {
        // Extract the token from that header
        const token = req.headers.authorization.split(" ")[1];
        try {
        // Verify the token is valid
        const data = jwt.verify(token, key);
        const username= data.username;  
        const retval= await Messages.postMessages(username,req.params.id, req.body.msg);
       if(retval===1) {
        res.status(200).send('sent');
       } else {
            if(retval===2) {
                res.status(401).send("Invalid Token");
            } else {
                res.status(409).send("we dont have this id");
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

