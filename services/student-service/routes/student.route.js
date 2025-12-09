const express = require("express");
const router =express.Router();
const Student =require('../models/student.js');

const {getStudents,getStudent, createStudent, updateStudent, deleteStudent,updateStudentByCin,deleteStudentByCin,getStudentByCin,getStudentByNomPrenom} =require('../controllers/student.controller.js');
router.get("/",getStudents);
router.get("/search", getStudentByNomPrenom);
router.get("/:id", getStudent);

router.post("/", createStudent);

// update 
router.put("/:id", updateStudent);
router.put("/cin/:cin", updateStudentByCin);

// delete 
router.delete("/:id", deleteStudent);
router.delete("/cin/:cin", deleteStudentByCin);

router.get("/cin/:cin", getStudentByCin);





module.exports=router;