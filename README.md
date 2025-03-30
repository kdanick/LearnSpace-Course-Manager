# 🚀 LearnSpace  

Hey there! 👋 This is my **first Java Swing project**, and I hope it's not too bad! 😄  

**LearnSpace** is a **course management software** where lecturers can **enroll students** into the courses they teach. The system also includes an **admin user** who manages users and oversees course assignments.  

I originally worked on this project as my **final semester 2 project** for **Object-Oriented Programming (OOP)** in my **1st year**. It was a great learning experience. Felt like sharing it so you can explore, improve, or even contribute! 🚀  

---

## 📌 Getting Started  

### 1️⃣ Install Dependencies  
Before running the project, you'll need to grab a few dependencies:  

- **Download JFreeChart** → [JFreeChart Download](https://sourceforge.net/projects/jfreechart/)  
- **Download PostgreSQL JDBC Driver** → [PostgreSQL JDBC](https://jdbc.postgresql.org/)  
- Add these `.jar` files to your project's dependencies (if not automatically detected by your IDE).  
  - If you're using **IntelliJ IDEA**, it should pick them up from the `.iml` file.  

---

### 2️⃣ Set Up the Database  
Since LearnSpace connects to a **PostgreSQL database**, you'll need to set it up first:  

1. **Install PostgreSQL** and make sure it's running.  
2. Create the database and populate it with the schema and sample data:  
   ```bash
   psql -U your_username -d your_database -f db/schema.sql
   psql -U your_username -d your_database -f db/seed.sql
   ```

---

### 3️⃣ Configure Database Connection  
To connect the project to your database, update `DB_Connect.java` inside the `DatabaseManager` package with your actual database details:  

```java
private static final String URL = "jdbc:postgresql://localhost:5432/Your-database-name"; // Use your actual database name
private static final String USER = "your-username"; // Your actual username
private static final String PASSWORD = "Your-database-password"; // Your actual password
```

---

### 4️⃣ Run the Project  
Now you're all set! 🎉  

- Run `DatabaseGUI.java` inside the `DatabaseManager` package.  
- Log in using one of the credentials from your database.  
- Explore the UI and interact with the database!  

---

## 🤝 Contributors  

This project was a **team effort**, and I want to acknowledge everyone who contributed to making LearnSpace what it is! 🌟  

👤 **[Danick Kirenga](https://github.com/kdanick)** – *Developer*  
👤 **[Kinyanjui Dean](https://github.com/Kinyanjui-Dean)** – *Developer*  
👤 **[Madina Sued](https://github.com/Madina2028)** – *Developer*   
👤 **[Umutoni Elsa](https://github.com/Umutoni-elsa)** – *Developer*  

Want to improve LearnSpace? Check out the [issues](https://github.com/kdanick/LearnSpace-Course-Manager/issues) and submit a pull request! 🚀  

---

## 🔥 Want to Contribute?  

While I don't have any future plans for this project, **you're welcome to explore it, tweak it, or even improve it!**  
If you’d like to contribute:  

1. **Fork the repo** on GitHub  
2. **Clone your fork** and experiment with the code  
3. If you make improvements, feel free to **open a pull request**!  

No pressure, just have fun with it! 🎉  

---

Easy does it! 🌚🎶
