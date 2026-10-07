# Secure Campus Complaint & Issue Tracker

## Complete Prototype: Stages 1-6

Stage 1 contains the basic OOP model, Stage 2 adds file-based authentication,
Stage 3 adds complaint operations, Stage 4 adds file persistence, and Stages 5-6 add Swing screens:

- `User`, `Student`, and `Admin`
- `Complaint` and its three child classes
- `ComplaintStatus` enum
- `Authenticatable` and `ComplaintOperations` interfaces
- Custom checked exception classes
- A console-based `Main` class that demonstrates the model
- `data/users.txt` for educational prototype user storage
- `AuthService` for login validation and logout
- Student and administrator role detection
- `ComplaintService` for creating and managing complaints
- Automatic complaint IDs such as `C001`
- Category-based complaint subclass creation
- Complaint search and student complaint filtering
- Complaint status updates
- `FileService` for saving, loading, and deleting complaints
- Student Swing dashboard and complaint creation form
- Admin Swing dashboard with search, details, status update, and delete

## Run the console demonstration

## Compile and run from the project folder

```text
javac -d out -sourcepath src src/Main.java
java -cp out Main
```

## Run the Swing application

In PowerShell, run these commands separately:

```powershell
javac -d out -sourcepath src src/Main.java
if ($?) { java -cp out Main gui }
```

Login details:

```text
Student: aarav@campus.edu / student123
Admin:   admin@campus.edu / admin123
```

Student complaints are stored in `data/complaints.txt`. The admin can search,
view, update, and delete complaints.

## Security note

This is an educational prototype. Passwords in `data/users.txt` are plain text and
must not be used this way in production. For the final security upgrade, add the JJWT
dependency, hash passwords, and validate signed tokens in a dedicated `JWTService`.
