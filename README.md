# Sudoku Android App

Preprosta Sudoku Android aplikacija, izdelana v Java z uporabo Android Studio.  
Aplikacija omogoča generiranje Sudoku igre, vnos števil, preverjanje pravilnosti in zaznavanje zmage.

---

# Funkcionalnosti

- Generiranje naključnega Sudokuja
- Backtracking algoritem za generiranje in reševanje Sudoku mreže
- Označevanje napačnih vnosov
- Označevanje izbrane vrstice, stolpca in celice
- Preprečevanje spreminjanja originalnih števil
- Gumb za brisanje števil
- Win dialog ob pravilno rešeni igri
- Edge-to-edge Android UI

---

# Tehnologije

- Java
- Android Studio
- Android SDK
- Custom View (`SudokuBoard`)
- Canvas risanje
- Backtracking algoritem

---

# Struktura projekta

com.example.sudoku
│
├── MainActivity.java
├── Sudoku.java
├── SudokuBoard.java
│
├── res/
│ ├── layout/
│ ├── values/
│ └── drawable/

---

# Kako deluje aplikacija

## 1. Generiranje Sudokuja

- Napolnijo se diagonalni 3x3 bloki
- Uporabi se backtracking za dokončanje mreže
- Odstrani se določeno število števil (težavnost)
- Shrani se stanje originalnih (fixed) celic

---

## 2. Igranje

- Uporabnik izbere celico
- Klikne številko (1–9) ali erase
- Sistem preveri pravilnost vnosa
- Napačne vrednosti se označijo rdeče

---

## 3. Preverjanje zmage

Igra je končana, ko:
- so vsa polja zapolnjena
- ni napačnih vnosov

Prikaže se dialog:
