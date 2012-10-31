Overview
===
This java program takes a [StatDNA](http://www.statdna.com) CSV data file and converts it to a more human readable JSON file. Only the relevant information needed for animations and passing/shooting statistics is retained.

<h2>Sample Input:</h2>

```
Name,Position,Duration,Home Team,Home Player,Away Team,Field Position,Away Player,Unsure,Shot Location,Pressure,Body Position,Line,Goalie Position,Shot Power
Pass Ground,3033150,3000,Arsenal,Cesc Fabregas 4,,"0,1",,,,No Pressure,Sideways,Line 0,,
Pass Ground (1),3033470,3000,Arsenal,R. van Persie 10,,"1,-2",,,,Marked,Sideways,Line 0,,
Gain Possession,3034110,1500,Arsenal,J. Wilshere 19,,"13,8",,,,Marked,Front,Line 0,,
```

<h2>Sample Output:</h2>
```json
    {
      "team": "Arsenal",
      "passer": "Cesc Fabregas 4",
      "receiver": "R. van Persie 10",
      "type": "ground",
      "sendX": 60,
      "sendY": 44,
      "receiveX": 59,
      "receiveY": 47,
      "timeOfPossession": 3000,
      "timeElapsed": 3000
    }
      ,
    {
      "team": "Arsenal",
      "passer": "R. van Persie 10",
      "receiver": "J. Wilshere 19",
      "type": "ground",
      "sendX": 59,
      "sendY": 47,
      "receiveX": 47,
      "receiveY": 37,
      "timeOfPossession": 4500,
      "timeElapsed": 6000
    }
```

