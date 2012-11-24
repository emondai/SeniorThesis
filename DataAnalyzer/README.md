Overview
===
This C# program parses the JSON file from my [JSONCreator](https://github.com/emondai/SeniorThesis/tree/master/JSONCreator) and contains some methods for retrieving certain statistics about the match. One of the sample methods that is included returns a Dictionary of every player and their pass completion percentage.

Although the data will be stored out in a convenient Dictionary data structure, printing the statistics would look something like this:
<h2>Sample Output:</h2>
```
    Cesc Fabregas 4-> Missed: 19, Completed: 60
    R. van Persie 10-> Missed: 9, Completed: 28
    J. Wilshere 19-> Missed: 11, Completed: 52
    B. Sagna 3-> Missed: 2, Completed: 51
    L. Koscielny 6-> Missed: 5, Completed: 32
    G. Clichy 22-> Missed: 8, Completed: 37
    S. Nasri 8-> Missed: 7, Completed: 40
    D. Drogba 11-> Missed: 10, Completed: 12
    F. Lampard 8-> Missed: 10, Completed: 30
    F. Malouda 15-> Missed: 5, Completed: 12
    J. Obi Mikel 12-> Missed: 3, Completed: 16
    J. Terry 26-> Missed: 14, Completed: 60
    B. Ivanovic 2-> Missed: 13, Completed: 40
    P. Cech 1-> Missed: 12, Completed: 8
    M. Essien 5-> Missed: 11, Completed: 42
    J. Djourou 20-> Missed: 5, Completed: 31
    A. Song 17-> Missed: 6, Completed: 59
    T. Walcott 14-> Missed: 5, Completed: 14
    S. Kalou 21-> Missed: 1, Completed: 11
    Paulo Ferreira 19-> Missed: 3, Completed: 12
    L. Fabianski 21-> Missed: 13, Completed: 2
    A. Cole 3-> Missed: 4, Completed: 25
    Ramires 7-> Missed: 4, Completed: 15
    G. Kakuta 44-> Missed: 8, Completed: 12
    Jose Bosingwa 17-> Missed: 3, Completed: 6
    A. Diaby 2-> Missed: 3, Completed: 4
    M. Chamakh 29-> Missed: 0, Completed: 7
    T. Rosicky 7-> Missed: 0, Completed: 4
```

