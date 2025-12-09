const mongoose = require("mongoose");

const StudentSchema = mongoose.Schema(
  {
     nom: {
    type: String,
    required: [true, "Please enter last name"],
  },

  prenom: {
    type: String,
    required: [true, "Please enter first name"],
  },

  cin: {
    type: String,
    required: [true, "Please enter CIN"],
    unique: true,
    match: [/^[0-9]{8}$/, "CIN must contain exactly 8 digits"],
  },

  email: {
    type: String,
    required: [true, "Please enter email"],
    unique: true,
    match: [/^\S+@\S+\.\S+$/, "Invalid email format"],
  },

  telephone: {
    type: String,
    required: [true, "Please enter phone number"],
    match: [/^[0-9]{8}$/, "Phone number must contain exactly 8 digits"],
  },

  niveau: {
    type: String,
    required: [true, "Please enter level"],
  },

  genre: {
    type: String,
    required: [true, "Please enter gender"],
    enum: ["Masculin", "Féminin"],
  },

  dateDeNaissance: {
    type: Date,
    required: [true, "Please enter date of birth"],
  },
  },
  {
    timestamps: true,
  }
);

const Student =mongoose.model("Student",StudentSchema);
module.exports=Student;