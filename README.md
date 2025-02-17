# Password Generator

## Overview
This project is a **Password Generator** built using **JavaFX**. It allows users to generate strong passwords based on their preferences and saves them securely in a file. The application also includes features such as clipboard copying, theme toggling, and duplicate website detection.

## Features
- **Generate Secure Passwords**: Users can specify the password length and select character types (uppercase, lowercase, numbers, special characters).
- **Avoid Duplicate Entries**: The application checks if a password for a given website already exists and prompts the user before overwriting it.
- **Clipboard Support**: The generated password can be copied to the clipboard with a single click.
- **Dark Mode & Light Mode**: Users can toggle between themes for a better visual experience.
- **Error Handling & User Feedback**: The application provides alerts and feedback messages for better usability.

### Running the Application
Everything is explained in [release](https://github.com/Ilic02/Password_Generator/releases/tag/v1.0.0-password-generator) tab.

## Project Structure
```
password-generator/
│── src/
│   ├── Main.java
│   ├── FileHandler.java
│   ├── PasswordGenerator.java
│   ├── ThemeManager.java
│   ├── AlertHelper.java
│   ├── ClipboardHelper.java
│   ├── Passwords.txt (ignored in .gitignore)
│── styles/
│   ├── light-theme.css
│   ├── dark-theme.css
│── README.md
│── .gitignore
```

## Usage
1. Enter a website name.
2. Specify the desired password length (8-24 characters).
3. Select character sets to include.
4. Click **Generate** to create a password.
5. Copy the password using **Copy to Clipboard**.
6. Saved passwords are stored in `src/Passwords.txt`.

## License
This project is licensed under the **MIT License**. Feel free to use and modify it as needed.

