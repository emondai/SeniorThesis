import java.io.*;
import java.util.*;
import org.json.simple.*;
import au.com.bytecode.opencsv.CSVReader;
import org.apache.commons.io.FileUtils;
import com.google.gson.*;

public class JSONcreator {
    public static void main(String[] args) {
        try{
            CSVReader reader = new CSVReader(new FileReader("SampleData.csv"));
            JSONArray list = new JSONArray();

            String currentPlayer;
            String currentTeamPossession;
            ArrayList<String[]> lines = (ArrayList) reader.readAll();
            int x=0,y=0;
            int totalTime =0;
            int timeSinceLast = 0;

            for(int i = 26; i<lines.size()-2;i++)//i starts at 26 because relevant match data doesn't start until line 27 in file.
            {
                String[] nextLine = lines.get(i);
                //nextLine[] is an array of values from the current line in the file
                String action = nextLine[0];//Will be either pass, goal, etc.
                int duration = Integer.parseInt(nextLine[2]);
                String homeTeam = nextLine[3];
                String homePlayer = nextLine[4];
                String awayTeam = nextLine[5];
                String awayPlayer = nextLine[7];
                totalTime += duration;

                if(!nextLine[6].isEmpty())
                {   String[] pos = nextLine[6].split(",");//Positions are in "x,y" format.
                    x = Integer.parseInt(pos[0]);
                    y = Integer.parseInt(pos[1]);
                    x = (x>0) ? (60-x) : (60+Math.abs(x));//Add or Sub 60 to convert from proprietary soccer grid coordinates to standard java 2d plane coordinates
                    y = (y>0) ? (45-y) : (45+Math.abs(y));//Add or Sub 45 to convert from proprietary soccer grid coordinates  to standard java 2d plane coordinates
                }

                /*
                if(action.contains("Offensive side 1st half") || action.contains("Offensive side second half"))//This is the team that starts with the ball at the start of each half
                {
                    lastPlayer = (!homePlayer.isEmpty()) ? homePlayer : awayPlayer;
                    lastTeamPossesion = (!homeTeam.isEmpty())  ? homeTeam : awayTeam;
                }
                */
                if(action.contains("Pass"))
                {
                    //If the Home Player has a value then his/her name is stored, if the value is empty then the Away Player's name is stored.
                    //The same logic is used for determining if the Home or Away team has the ball.
                    currentPlayer = (!homePlayer.isEmpty()) ? homePlayer : awayPlayer;
                    currentTeamPossession = (!homeTeam.isEmpty())  ? homeTeam : awayTeam;

                    String nextTeamPossession = (!lines.get(i+1)[3].isEmpty()) ? lines.get(i+1)[3] : lines.get(i+1)[5];//See which team has ball next
                    String receivingPlayer = null;
                    int nextX=-1,nextY=-1;
                    int timeOfPossession = 0;

                    if(lines.get(i+1)[0].contains("Pass"))//If the next line is "Pass" then the player attempted to pass the ball without moving from the spot where the ball was received.
                    {

                        if(currentTeamPossession.equals(nextTeamPossession))//This means that a player succesfully passed to his/her teammate.
                        {
                            receivingPlayer = (!lines.get(i+1)[4].isEmpty()) ? lines.get(i+1)[4] : lines.get(i+1)[7];
                            String[] pos = lines.get(i+1)[6].split(",");//Positions are in "x,y" format.
                            nextX = Integer.parseInt(pos[0]);
                            nextY = Integer.parseInt(pos[1]);
                            nextX = (nextX>0) ? (60-nextX) : (60+Math.abs(nextX));//Add or Sub 60 to convert from soccer grid coords to java coords
                            nextY = (nextY>0) ? (45-nextY) : (45+Math.abs(nextY));//Add or Sub 45 to convert from soccer grid coords to java coords
                            timeOfPossession = duration;
                        }
                        else//Pass was incomplete or intercepted.
                            receivingPlayer = null;

                    }
                    else if(lines.get(i+1)[0].contains("Gain Possession"))//This is the location where the player received the ball.
                    {
                        if(currentTeamPossession.equals(nextTeamPossession))//This means that a player succesfully received the ball from to his/her teammate.
                        {
                            receivingPlayer = (!lines.get(i+1)[4].isEmpty()) ? lines.get(i+1)[4] : lines.get(i+1)[7];
                            String[] pos = lines.get(i+1)[6].split(",");//Positions are in "x,y" format.
                            nextX = Integer.parseInt(pos[0]);
                            nextY = Integer.parseInt(pos[1]);
                            nextX = (nextX>0) ? (60-nextX) : (60+Math.abs(nextX));//Add or Sub 60 to convert from soccer grid coords to java coords
                            nextY = (nextY>0) ? (45-nextY) : (45+Math.abs(nextY));//Add or Sub 45 to convert from soccer grid coords to java coords
                            timeOfPossession = duration + Integer.parseInt(lines.get(i+1)[2]);
                        }
                        else//Pass was incomplete or intercepted.
                        {
                            receivingPlayer = null;
                        }
                    }
                    else if(lines.get(i+1)[0].contains("Restricted"))//Sometimes the camera's view is blocked on TV (or replays are shown) so we skip until the game is in view again.
                        continue;

                    //The actions that just occurred are place into a JSON object.
                    Map obj= new LinkedHashMap();
                    obj.put("team",currentTeamPossession);
                    obj.put("passer",currentPlayer);
                    obj.put("receiver",receivingPlayer);
                    if(action.contains("Air"))
                        obj.put("type","air");
                    else
                        obj.put("type","ground");
                    obj.put("sendX",x);
                    obj.put("sendY",y);
                    obj.put("receiveX",nextX);
                    obj.put("receiveY",nextY);
                    obj.put("timeOfPossession",timeOfPossession);
                    obj.put("timeElapsed",totalTime);
                    list.add(obj);//JSON object is added to list of JSON objects

                    timeSinceLast = totalTime - timeSinceLast;//duration of action is recorded
                }
                else if(action.contains("Shot"))//The player attempts to take a shot
                {
                    currentPlayer = (!homePlayer.isEmpty()) ? homePlayer : awayPlayer;
                    currentTeamPossession = (!homeTeam.isEmpty())  ? homeTeam : awayTeam;

                    //The actions that just occurred are place into a JSON object.
                    Map obj= new LinkedHashMap();
                    obj.put("team",currentTeamPossession);
                    obj.put("passer",currentPlayer);
                    obj.put("receiver",null);
                    //If the next line in the file is "Kick Off" then the player has scored and it is recorded as a goal.
                    if(lines.get(i+1)[0].contains("Goal") && lines.get(i+2)[0].contains("Kick Off"))
                        obj.put("type","goal");
                    else
                        obj.put("type","shot");
                    obj.put("sendX",x);
                    obj.put("sendY",y);
                    obj.put("receiveX",-1);
                    obj.put("receiveY",-1);
                    obj.put("timeOfPossession",duration);
                    obj.put("timeElapsed",totalTime);
                    list.add(obj);//JSON object is added to list of JSON objects

                    timeSinceLast = totalTime - timeSinceLast; //duration of action is recorded
                }
            }

            JSONObject obj = new JSONObject();
            obj.put("actions", list);//JSON Array is created.

            //I realized that JSON-simple doesn't have pretty print so I just used gson at the end. If I were to rewrite this application, I would do it all with one library.
            Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
            JsonParser jp = new JsonParser();
            JsonElement je = jp.parse(obj.toJSONString());
            String prettyJsonString = gson.toJson(je);
            //Pretty print JSON: http://stackoverflow.com/questions/4105795/pretty-print-json-in-java
            
            System.out.println(prettyJsonString);
            FileUtils.writeStringToFile(new File("output.json"),prettyJsonString);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
