const Student =require('../models/student.js');
const getStudents=async(req,res)=>{
        try{
        const students = await Student.find({});
        res.status(200).json(students);
    }catch(error){
        res.status(500).json({message:error.message});
    }
}
const getStudent = async (req, res) => {
  try {
    const { id } = req.params;
    const student = await Student.findById(id);
    res.status(200).json(student);
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
};

const createStudent = async (req, res) => {
  try {
    const student = await Student.create(req.body);
    res.status(200).json(student);
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
};

const updateStudent = async (req, res) => {
  try {
    const { id } = req.params;

    const student = await Student.findByIdAndUpdate(id, req.body);

    if (!student) {
      return res.status(404).json({ message: "Student not found" });
    }

    const updatedStudent = await Student.findById(id);
    res.status(200).json(updatedStudent);
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
};

const deleteStudent = async (req, res) => {
  try {
    const { id } = req.params;

    const student = await Student.findByIdAndDelete(id);

    if (!student) {
      return res.status(404).json({ message: "Student not found" });
    }

    res.status(200).json({ message: "Student deleted successfully" });
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
};
const updateStudentByCin = async (req, res) => {
  try {
    const { cin } = req.params;

    const student = await Student.findOneAndUpdate(
      { cin: cin },
      req.body,
      { new: true } // retourne l'étudiant mis à jour
    );

    if (!student) {
      return res.status(404).json({ message: "Student not found with this CIN" });
    }

    res.status(200).json(student);

  } catch (error) {
    res.status(500).json({ message: error.message });
  }
};
const deleteStudentByCin = async (req, res) => {
  try {
    const { cin } = req.params;

    const student = await Student.findOneAndDelete({ cin: cin });

    if (!student) {
      return res.status(404).json({ message: "Student not found with this CIN" });
    }

    res.status(200).json({ message: "Student deleted successfully" });

  } catch (error) {
    res.status(500).json({ message: error.message });
  }
};
const getStudentByCin = async (req, res) => {
  try {
    const { cin } = req.params;

    const student = await Student.findOne({ cin: cin });

    if (!student) {
      return res.status(404).json({ message: "Student not found with this CIN" });
    }

    res.status(200).json(student);

  } catch (error) {
    res.status(500).json({ message: error.message });
  }
};
const getStudentByNomPrenom = async (req, res) => {
  try {
    const { nom, prenom } = req.query;

    const student = await Student.findOne({ nom: nom, prenom: prenom });

    if (!student) {
      return res.status(404).json({ message: "Student not found with this name" });
    }

    res.status(200).json(student);

  } catch (error) {
    res.status(500).json({ message: error.message });
  }
};


module.exports = {
  getStudents,
  getStudent,
  createStudent,
  updateStudent,
  deleteStudent,
  updateStudentByCin,
  deleteStudentByCin,
  getStudentByCin,
  getStudentByNomPrenom,
};

