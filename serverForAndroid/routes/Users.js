import express  from 'express';
import {postUsers,getUsers} from '../controllers/Users.js'
const router=express.Router();
router.post('/',postUsers);
router.get('/:id', getUsers);
export default router;