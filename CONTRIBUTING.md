# Contributing Guidelines

## Team Workflow

This document outlines the contribution guidelines for the Bank Management System project.

## Project Structure Assignment

### Member 1: Database & DAO Layer
- Database schema design (`database/schema.sql`)
- Database connection utilities (`com.bank.util.DatabaseConnection`)
- Database configuration (`com.bank.config.DatabaseConfig`)
- All DAO classes (`com.bank.dao.*`)

### Member 2: Models & Customer/Account Services
- Model classes (`com.bank.model.*`)
- CustomerService (`com.bank.service.CustomerService`)
- AccountService (`com.bank.service.AccountService`)

### Member 3: Transaction Service & Exceptions
- TransactionService (`com.bank.service.TransactionService`)
- Exception classes (`com.bank.exception.*`)

### Member 4: UI & Documentation
- Main application (`com.bank.ui.BankManagementSystem`)
- README.md
- Testing and integration

## Code Standards

1. **Naming Conventions**
   - Classes: PascalCase (e.g., `CustomerService`)
   - Methods: camelCase (e.g., `createAccount`)
   - Constants: UPPER_SNAKE_CASE (e.g., `MAX_BALANCE`)

2. **Code Formatting**
   - Use 4 spaces for indentation
   - Maximum line length: 100 characters
   - Always add JavaDoc comments for public methods

3. **Commit Messages**
   - Format: `[Module] Description`
   - Example: `[DAO] Add getAccountByNumber method`
   - Example: `[Service] Implement transfer functionality`

4. **Testing**
   - Test all CRUD operations
   - Test exception scenarios
   - Verify transaction rollback

## Git Workflow

1. Create feature branch: `git checkout -b feature/your-feature`
2. Make changes and commit
3. Push to remote: `git push origin feature/your-feature`
4. Create pull request for review

## Review Checklist

Before submitting code:
- [ ] Code compiles without errors
- [ ] No hardcoded credentials
- [ ] Exception handling implemented
- [ ] JavaDoc comments added
- [ ] Tested with sample data
