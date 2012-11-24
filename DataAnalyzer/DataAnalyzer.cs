using System;
using System.Collections.Generic;
using System.Linq;
using Newtonsoft.Json;
using System.Text;
using System.IO;
using System.Collections;

namespace SeniorThesis
{
    class DataAnalyzer
    {
        //The JSON file will be parsed as an array of 'actions'
        public class ActionList
        {
            public action[] actions;
        }

        //Each action consists of the following properties:
        public class action
        {
            public string team;
            public string passer;
            public string receiver;
            public string type;
            public int sendX;
            public int sendY;
            public int receiveX;
            public int receiveY;
            public int timeOfPossession;
            public int timeElapsed;
        }

        static void Main(string[] args)
        {
            string jsonFile = File.ReadAllText(@"C:\data.json");
            ActionList actionArray = JsonConvert.DeserializeObject<ActionList>(jsonFile); //Parses JSON file. All elements can now be accessed by actionArray
            var actionList = new List<action>(actionArray.actions);//actionList is created from the action array

            var sample = new List<string>();
            sample.Add("T. Rosicky 7");//A sample list is created and a player is added to it.

            //Each player's passes and incomplete passes will now be printed except for those in the sample list (which will be filtered out)
            passCompletion(filterList(actionList, sample));
            
            Console.WriteLine("\nPress any key to continue...");
            Console.ReadKey();
        }

        //This method determines how many incomplete and complete passes each player had.
        public static Dictionary<string, double[]> passCompletion(List<action> actionList)
        {
            
            var passes = new Dictionary<string, double[]>();//First index of double[] array is incomplete passes, second index is completed passes
            
            foreach (action a in actionList)
            {
                Console.WriteLine(a.passer + " -> " + a.receiver + " by " + a.type);
                
                if (a.receiver != null && (a.type.Equals("air") || a.type.Equals("ground")))
                {
                    if (passes.ContainsKey(a.passer))
                        passes[a.passer][1]++;
                    else
                        passes.Add(a.passer, (new double[2] {0,1}) );
                }

                if (a.receiver == null && (a.type.Equals("air") || a.type.Equals("ground")))
                {
                    if (passes.ContainsKey(a.passer))
                        passes[a.passer][0]++;
                    else
                        passes.Add(a.passer, (new double[2] { 1, 0 }));
                }
            }

            foreach (var player in passes)
                Console.WriteLine(player.Key + "-> Missed: " + player.Value[0] + ", Completed: "+ player.Value[1]);//Prints list of all players and their pass %

            var passPercentage = new Dictionary<string, double>();
            foreach (var player in passes)
                passPercentage[player.Key] = (player.Value[1]/(player.Value[0]+player.Value[1]));

            foreach (var player in passPercentage)
                Console.WriteLine(player.Key + " -> " + string.Format("{0:0.0%}", player.Value));

            return passes;
        }

        //This method calculates the Pass Completion % of each player
        public static Dictionary<string, double> passPercentage(List<action> actionList)
        {
            var passes = passCompletion(actionList);
            var passPercentage = new Dictionary<string, double>();

            foreach (var player in passes)
                passPercentage[player.Key] = (player.Value[1] / (player.Value[0] + player.Value[1]));

            foreach (var player in passPercentage)
                Console.WriteLine(player.Key + " -> " + string.Format("{0:0.0%}", player.Value));

            return passPercentage;
        }

        //This method removes any players in the 'names' list. It is useful for filtering out certain players whose stats you don't care about.
        public static List<action> filterList(List<action> actionList, List<string> names)
        {
            var namesToRemove = new HashSet<string>(names);
            actionList.RemoveAll(x => namesToRemove.Contains(x.passer));
            return actionList;
        }

        

    }
}
