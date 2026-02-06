# Quick Setup Guide

## Prerequisites Check
- [ ] Java JDK 11+ installed (`java -version`)
- [ ] Maven installed (`mvn -version`)
- [ ] MySQL Server running (`mysql --version`)

## Step-by-Step Setup

### 1. Database Setup (5 minutes)

```bash
# Login to MySQL
mysql -u root -p

# Create database and tables
source database/schema.sql;

# Or from command line:
mysql -u root -p < database/schema.sql
```

### 2. Configuration (2 minutes)

```bash
# Copy example config file
cp src/main/resources/database.properties.example src/main/resources/database.properties

# Edit database.properties with your MySQL credentials
# Update: db.username and db.password
```

### 3. Build Project (2 minutes)

```bash
# Download dependencies and compile
mvn clean compile
```

### 4. Run Application (1 minute)

```bash
# Option 1: Using Maven
mvn exec:java -Dexec.mainClass="com.bank.ui.BankManagementSystem"

# Option 2: After compilation
java -cp "target/classes;target/dependency/*" com.bank.ui.BankManagementSystem
```

## Testing the Application

### Sample Test Flow:

1. **Register Customer**
   - First Name: John
   - Last Name: Doe
   - Email: john.doe@test.com
   - Phone: 1234567890
   - Address: 123 Main St
   - DOB: 1990-01-15

2. **Create Account**
   - Customer ID: (from step 1)
   - Account Number: ACC001
   - Type: 1 (SAVINGS)

3. **Deposit Money**
   - Account Number: ACC001
   - Amount: 5000
   - Description: Initial deposit

4. **View Balance**
   - Account Number: ACC001

5. **View Transaction History**
   - Account Number: ACC001

## Troubleshooting

### Issue: "ClassNotFoundException: com.mysql.cj.jdbc.Driver"
**Solution**: Run `mvn clean compile` to download MySQL connector

### Issue: "Access denied for user"
**Solution**: Check `database.properties` credentials

### Issue: "Unknown database 'bank_management'"
**Solution**: Run `database/schema.sql` to create database

### Issue: "Connection refused"
**Solution**: Ensure MySQL server is running

## Project Structure Overview

```
BankManagement/
├── database/
│   └── schema.sql              # Run this first!
├── src/main/
│   ├── java/com/bank/
│   │   ├── config/             # Database configuration
│   │   ├── dao/                # Data access layer
│   │   ├── exception/          # Custom exceptions
│   │   ├── model/              # Entity classes
│   │   ├── service/            # Business logic
│   │   ├── ui/                 # Main application
│   │   └── util/               # Utilities
│   └── resources/
│       └── database.properties # Configure this!
├── pom.xml                     # Maven config
└── README.md                   # Full documentation
```

## Team Members Quick Reference

- **Member 1**: Database, DAO, Config, Utils
- **Member 2**: Models, CustomerService, AccountService  
- **Member 3**: TransactionService, Exceptions
- **Member 4**: UI, Documentation, Testing

## Next Steps After Setup

1. Review the code structure
2. Test all menu options
3. Try edge cases (insufficient balance, etc.)
4. Review README.md for detailed documentation
5. Check CONTRIBUTING.md for coding standards

---

**Total Setup Time**: ~10 minutes  
**Ready to Code!** 🚀
