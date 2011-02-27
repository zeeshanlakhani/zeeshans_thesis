//imports
import com.melka.echonest.*;
import com.myjavatools.web.*;

//vars
String APIKey = "CX9TSTJJKT23LNKYW";
String[] tracklist; 
String[] songlist; 
int oneCount = 0; 

//object vars
EchoNest nest;
ENTrack t;
String[] eachTrack = new String[30];
//String[] eachReal = new String[30];
String[] oneMin = new String[eachTrack.length];
//PrintWriter mainText;
int next = 0;


void setup() {
  tracklist = getFiles();
  songlist = getChords();
  //mainText = createWriter("mainText.txt"); 

  println("press b to start");

}

void draw() {


}

String[] getChords() {
  java.io.File folder = new java.io.File(dataPath(""));

  // this is the filter (returns true if file's extension is .jpg)
  java.io.FilenameFilter txtFilter = new java.io.FilenameFilter() {
    boolean accept(File dir, String name) {
      return name.toLowerCase().endsWith(".txt");
    }
  };
  // list the files in the data folder
  String[] filenames = folder.list(txtFilter);
  return filenames;
}

String[] getFiles() {
  java.io.File folder = new java.io.File(dataPath(""));

  // this is the filter (returns true if file's extension is .mp3op6  KFFFFFFFFFFGasy)
  java.io.FilenameFilter mp3Filter = new java.io.FilenameFilter() {
    boolean accept(File dir, String name) {
      return name.toLowerCase().endsWith(".mp3");
    }
  };
  // list the files in the data folder
  String[] filenames = folder.list(mp3Filter);
  return filenames;
}

void getBeats(int a) {
  //println(tracklist[a] + " " +    "track no." + " " + a);
  EchoNest nest = new EchoNest(this,APIKey,tracklist[a]/*,"/Users/zeeshanlakhani/Echoplex (Costrophobia Mix).mp3"*/);
  if (a == tracklist.length - 1) {
    println("last track"); 
  }
}

void ENTrackLoaded(ENTrack track) {
  t = track;
  println("done");
  eachTrack[next - 1] = compBeats(t);
  //  eachReal[next - 1] = getReals();
  //println(eachTrack);
  //println(eachTrack[next-1].charAt(0));
  println("press f to flush it or b for the next track");
  //println(eachTrack);
  //  mainText.println(eachTrack);
  //  mainText.println("*****" + " ");
  //  mainText.println("");
  //eachTrack[1] = "*****";
  saveStrings("mainText_db2.txt",eachTrack);
  saveStrings("oneMin_db2.txt",oneMin);

  //println(oneMin);
}

void keyPressed() {
  if (key == 'b') {
    getBeats(next);
    redraw();
    next++;
  }
  if (key == 'f') {
    //    mainText.flush();
    //    mainText.close();
    exit();
  } 
}

String compBeats(ENTrack t) {
  println(songlist[next - 1]);
  String[] filelines = loadStrings(songlist[next - 1]);
  //println(filelines);
  //println(t.beats.length);
  String[] letters = new String[filelines.length];
  String[] chordals = new String[filelines.length];
  float[] numbers = new float[filelines.length];    
  float[] allbeats = new float[t.beats.length];

  String[] realchords = new String[t.beats.length];
  String[] downchords = new String[t.beats.length];

  for (int j = 0; j < filelines.length; j++) {
    String temp[] = split(filelines[j]," ");
    letters[j] = temp[0];
    chordals[j] = temp[1];
    numbers[j] = float(temp[2]);
    //println(numbers[j]);
  }

  int counter = 0; 
  for (int i = 0; i < t.beats.length; i++) {
    String[] tempstring = new String[0];
    String[] tempreals = new String[0];
    allbeats[i] = t.beats[i].time;
    for (int k = 0 + counter; k < numbers.length; k++) {
      if (k == 0) {
        println("yes"); 
      }
      if (i == 0) {
        if((numbers[k] >= 0) && (numbers[k] <= allbeats[i])) {
          tempstring = append(tempstring,letters[k]);
          tempreals = append(tempreals, chordals[k]);
          counter++;
        }
        else if(tempstring.length == 0) {
          downchords[i] = "";
          realchords[i] = "";
          break; 
        }
        else {
          //println(tempstring.length + "black");
          downchords[i] = maxfreq(tempstring);
          //println(downchords[i] + " " + i);
          realchords[i] = maxfreq(tempreals);
          break; 
        }
      }
      else if (i > 0) {
        if ((numbers[k] >= allbeats[i - 1]) && (numbers[k] <= allbeats[i])) {
          //println(allbeats[i-1] +" " +  "and" + " " + allbeats[i]);
          tempstring = append(tempstring,letters[k]);
          //println(numbers[k]);
          tempreals = append(tempreals, chordals[k]);
          counter++;
          if (k == numbers.length - 1) {
            for (int end = i+1; end < t.beats.length; end++) {
              downchords[i+1] = ""; 
              realchords[i+1] = "";
            }
            downchords[i] = maxfreq(tempstring);
            realchords[i] = maxfreq(tempreals);
            break;
          }
        }
        else {
          //println(counter);
          downchords[i] = maxfreq(tempstring);
          //println(tempstring);
          realchords[i] = maxfreq(tempreals);
          break; 
        }
      }
    }
  }
  for (int i = 0; i < allbeats.length; i++) {
    //println(allbeats[i]);
    if (allbeats[i] >= 60.0 || i == allbeats.length - 1) {
      oneMin[oneCount] = Integer.toString(i);
      println(oneCount + "," + oneMin[oneCount]);
      if (i == oneMin.length - 1) {
        break; 
      }
      oneCount++;
      break;
    }
  }
  String reduced = arrayToString(downchords,"");
  String reducedreals = arrayToString(realchords,"");

  //println(reducedreals.length());
  return reduced;
}

String maxfreq(String[] temp) {
  HashMap hmcount = new HashMap();
  for (int i = 0; i < temp.length; i++) {
    if(hmcount.containsKey(temp[i])) { 
      hmcount.put(temp[i],(Integer)hmcount.get(temp[i]) + (Integer)1);
    }
    else {
      hmcount.put(temp[i],1); 
    }
  }

  Iterator i = hmcount.entrySet().iterator();
  int check = 0; 
  String outit = "";
  while (i.hasNext()) {
    Map.Entry me = (Map.Entry)i.next();
    if ((Integer)me.getValue() > (Integer) check) {
      check = (Integer)me.getValue(); 
      outit = (String)me.getKey();
    }
  }
  return outit;
}

String arrayToString(String[] a, String separator) {
  //println(a.length);
  StringBuffer result = new StringBuffer();
  if (a.length > 0) {
    result.append(a[0]);
    for (int i=1; i<a.length; i++) {
      result.append(separator);
      result.append(a[i]);
    }
  }
  return result.toString();
}




























































































