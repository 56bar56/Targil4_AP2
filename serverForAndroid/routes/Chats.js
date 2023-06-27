import express  from 'express';
import {getChats,postChats} from '../controllers/Chats.js'
import {getMessages} from '../controllers/Messages.js'
import {postMessages} from '../controllers/Messages.js'
const router=express.Router();
router.get('/',getChats);
router.post('/',postChats);
router.get('/:id/Messages',getMessages);
router.post('/:id/Messages',postMessages);

export default router;