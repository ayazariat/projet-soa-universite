const express = require("express")
require("dotenv").config();
const mongoose = require("mongoose");
const Student =require('./models/student.js');
const studentRoute=require("./routes/student.route.js");
const app = express()
app.use(express.json())
app.use(express.urlencoded({extended:false}));


//routes
app.use("/api/students",studentRoute);
app.get("/", (req, res) => {
  res.send("Hello from Node API server updated");
});

// MongoDB Connection
mongoose
  .connect(process.env.MONGO_URI)
  .then(() => {
    console.log("Connected to database!");
    app.listen(process.env.PORT || 3000, () => {
      console.log("Server is running on port 3000");
    });
  })
  .catch((error) => {
    console.log("Connection failed!", error);
  })
  

  