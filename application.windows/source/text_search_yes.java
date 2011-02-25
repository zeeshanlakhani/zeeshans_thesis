import processing.core.*; 
import processing.xml.*; 

import org.gicentre.processing.utils.proj.*; 
import org.gicentre.processing.utils.zoom.*; 
import org.gicentre.processing.utils.multisketch.*; 
import org.gicentre.processing.utils.colour.*; 
import org.gicentre.processing.utils.move.*; 
import org.gicentre.processing.utils.*; 
import ddf.minim.*; 
import ddf.minim.signals.*; 
import ddf.minim.analysis.*; 
import ddf.minim.effects.*; 
import java.awt.Robot; 
import guicomponents.*; 
import ds.tree.*; 
import java.awt.Frame; 
import java.awt.AWTException; 
import damkjer.ocd.*; 
import processing.opengl.*; 
import org.apache.commons.lang.mutable.*; 
import org.apache.commons.lang.math.*; 
import org.apache.commons.lang.exception.*; 
import org.apache.commons.lang.text.*; 
import org.apache.commons.lang.*; 
import org.apache.commons.lang.time.*; 
import org.apache.commons.lang.builder.*; 
import traer.physics.*; 
import java.util.Enumeration; 
import controlP5.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class text_search_yes extends PApplet {

































boolean EndSearch = false;
boolean go = false;
GTextField searchF;
GButton btnEnter;
GButton downloadSong;
GButton downloadString;	
GWindow window;

GLabel toplabel;
GLabel label;
GLabel labelnotes;
GLabel labelnotes1;
GLabel labelnotes2;
GLabel example;

HashMap keymap;
String usrSub;
String[] chords;
String[] realChords;
String[] Beatchords;
String[] IntVals;
String[] filenames;
String[] finSearch;
HashMap finny;
SearchKMP findIt;
int[] leftidx;
int[] rightidx;
TreeSet setMapL;
TreeSet setMapR;
LinkedHashMap layoutL = new LinkedHashMap();
LinkedHashMap layoutR = new LinkedHashMap();
//GHorzSlider songSlider;
int timer;
String songstorage = "/Users/zeeshanlakhani/Documents/Processing/echonest_reduc/data/";
PrintWriter originals;
ZoomPan zoomer;

public class UIWindow extends PApplet
{
  Frame frame;
  int width;
  int height;

  UIWindow (int w, int h)
  {
    width = w;
    height = h;
    frame = new Frame( );
    frame.setBounds(0,0,width, height);
    frame.setLocation(0,700);
    frame.add(this);
    frame.setTitle("Search");
    this.init( );
    frame.show( );
  }

  synchronized public void setup() {
    try {
      Robot r = new Robot();
      r.mouseMove(190,590);
    } 
    catch(AWTException e) {
    }

    String s1 = "";

    keymap = new HashMap();
    themapSelf();

    GComponent.globalColor = GCScheme.getColor(this,GCScheme.GREY_SCHEME);
    GComponent.globalFont = GFont.getFont(this, "Georgia", 16);
    G4P.setColorScheme(this, GCScheme.RED_SCHEME);
    G4P.messagesEnabled(false);

    searchF = new GTextField(this, s1,(width/2) - (250/2) - 10,220,250,25);
    searchF.setBorder(2);
    searchF.setOpaque(true);
    searchF.setColorScheme(GCScheme.GREY_SCHEME);


    toplabel = new GLabel(this, "DATABASE: NIN - Echonest (Remixes)",(width/2) - (300/2) - 5, 20,300,0);
    label = new GLabel(this, "Chord Pattern Searcher",(width/2) - (200/2), 200, 200, 0);
    labelnotes = new GLabel(this,"*Use CAPS for major chords",(width/2) - (250/2) + 10, 320, 250, 0);
    labelnotes1 = new GLabel(this,"*Use non-caps for minor chords",(width/2) - (250/2) + 10, 340, 250, 0);
    labelnotes2 = new GLabel(this,"*Use sharps (#'s), not flats",(width/2) - (250/2) + 10, 360, 250, 0);
    example = new GLabel(this, "example: C E G A# c e g a#", (width/2) - (250/2) + 15, 400, 250, 0);

    //  songLength = (285231 * 2)/1000;
    downloadSong = new GButton(this,"Play Original Song",width/2 - 85,75,20,20);
    downloadString = new GButton(this,"View Original's Chords",width/2 - 100,118,20,20);
    //  songSlider = new GHorzSlider(this, 110, 120, songLength , 30);
    //  songSlider.setLimits(0,0,songLength);
    //  songSlider.setBorder(2);
    //  songSlider.setColorScheme(GCScheme.GREY_SCHEME);
    //  songSlider.setValue(0);

    btnEnter = new GButton(this, "Search",(width/2) - (100/2) - 10,270,100,30);
    btnEnter.setColorScheme(GCScheme.GREY_SCHEME);

    filenames = getFiles();
    finSearch = new String[0];
    finny = new HashMap();
    //println(getFiles());
    //chords = loadStrings("/Users/zeeshanlakhani/Documents/Processing/text_search_yes/data/testtext.txt");
    Beatchords = loadStrings("/Users/zeeshanlakhani/Documents/Processing/text_search_yes/data/mainText_db2.txt");
    IntVals = loadStrings("/Users/zeeshanlakhani/Documents/Processing/echonest_reduc/oneMin_db2.txt");
    chords = new String[Beatchords.length];
    println(Beatchords);

    for (int i = 0; i < Beatchords.length; i++) {
      if (Beatchords[i].length() > Integer.parseInt(IntVals[i])) {
        chords[i] =  Beatchords[i].substring(0,Integer.parseInt(IntVals[i]) + 1);
      }
      else {
        chords[i] =  Beatchords[i].substring(0,Integer.parseInt(IntVals[i]));
      }
    }

    //realChords = loadStrings("/Users/zeeshanlakhani/Documents/Processing/text_search_yes/data/chordText.txt");
    //println(realChords);

    findIt = new SearchKMP();
    leftidx = new int[chords.length];
    rightidx = new int[chords.length];

    setMapL = new TreeSet();
    setMapR = new TreeSet();
    //minim = new Minim(this);
    //player = minim.loadFile(songstorage + filenames[0] + ".mp3", 2048);
    //    originals = createWriter("original.pdf");
    //    originals.print(chords[0]);
    //    originals.flush(); // Writes the remaining data to the file
    //    originals.close(); // Finishes the file
  }

  synchronized public void draw() {
    background(200);

    if (searchF.getText().equals("Music Chords Only") || searchF.getText().equals("No Matches. Try Again")) {
      int stopper = second();
      if (stopper >= timer + 3) {
        searchF.setText(""); 
        searchF.setTextAlign(0);
      }
    }
  }

  public void createWindows(){
    //G4P.disableAutoDraw();
    EndSearch = true;
    try {
      Robot r = new Robot();
      r.mouseMove(0,0);
    } 
    catch(AWTException e) {
    }


  }


  public void handleButtonEvents(GButton button){
    String enteredText = searchF.getText().replaceAll("\\s","");
    if((button.getText().equals("Search") && !enteredText.equals("")) && (button.eventType == GButton.CLICKED)){
      searchF.setColorScheme(GCScheme.GREY_SCHEME);
      usrSub = stringOutput();
      runSearch();
      timer = second();
      //      println(finSearch);
      //      println("\n" + layoutL);
      //      println("\n" + layoutR);
      //btnEnter.setVisible(false);
    }

    if (button.getText().equals("Play Original Song") && (button.eventType == GButton.CLICKED)) {
      open("/Users/zeeshanlakhani/Documents/Processing/text_search_yes/data/echoplex_orig.m3u");
    } 
    if (button.getText().equals("View Original's Chords") && (button.eventType == GButton.CLICKED)) {
      open("/Users/zeeshanlakhani/Documents/Processing/text_search_yes/data/output_first chord.pdf");

    }
  }

  public void keyPressed() {
    String enteredText = searchF.getText().replaceAll("\\s","");
    if ((!enteredText.equals("")) && (key == ENTER)) {
      //start();
      searchF.setColorScheme(GCScheme.GREY_SCHEME);
      usrSub = stringOutput();
      runSearch();
      timer = second();
      //println(finSearch);
      /*println("\n" + layoutL);
       println("\n" + layoutR);*/
    }
  }
  public String stringOutput() {
    String stringtest = searchF.getText();
    //println (stringtest);
    //stringtest = stringtest.toUpperCase();
    stringtest = stringtest.replaceAll("\\s","");

    //String[] temp1 = new String[1];
    //String[] temp2 = new String[1];
    String temp1; 
    String temp2;

    String[] stringArry = new String[0];
    for (int i = 0; i < stringtest.length(); i++) {
      //println(i);
      if (i == stringtest.length() - 1) {
        stringArry = append(stringArry,replacer(Character.toString(stringtest.charAt(i))));
        String check = replacer(Character.toString(stringtest.charAt(i)));
        if (check.equals("error")) {
          return "error"; 
        }
        //counter = 0;
      }
      else {
        if(stringtest.charAt(i+1) == '#') {
          temp1 = Character.toString(stringtest.charAt(i));
          temp2 = Character.toString(stringtest.charAt(i+1));
          stringArry = append(stringArry,replacer(temp1 + temp2));
          if (replacer(temp1 + temp2).equals("error")) {
            return "error"; 
          }
          i++;
        }
        else {
          stringArry = append(stringArry,replacer(Character.toString(stringtest.charAt(i))));
          String check = replacer(Character.toString(stringtest.charAt(i)));
          if (check.equals("error")) {
            return "error"; 
          }
        }
      }
    }
    return arrayToString(stringArry,"");
  }

  public String replacer(String note) {
    if (keymap.containsKey(note)) {
      String symbol = keymap.get(note).toString();
      return symbol;
    }
    else {
      return "error"; 
    }
  }

  public void themapSelf() {
    keymap.put("C","A");
    keymap.put("C#","B");
    keymap.put("D","C");
    keymap.put("D#","D");
    keymap.put("E","E");
    keymap.put("F","F");
    keymap.put("F#","G");
    keymap.put("G","H");
    keymap.put("G#","I");
    keymap.put("A","J");
    keymap.put("A#","K");
    keymap.put("B","L");
    keymap.put("c","M");
    keymap.put("c#","N");
    keymap.put("d","O");
    keymap.put("d#","P");
    keymap.put("e","Q");
    keymap.put("f","R");
    keymap.put("f#","S");
    keymap.put("g","T");
    keymap.put("g#","U");
    keymap.put("a","V");
    keymap.put("a#","W");
    keymap.put("b","X");
  }

  public String[] getFiles() {
    java.io.File folder = new java.io.File("/Users/zeeshanlakhani/Documents/Processing/echonest_reduc/data");

    // this is the filter (returns true if file's extension is .jpg)
    java.io.FilenameFilter txtFilter = new java.io.FilenameFilter() {
      public boolean accept(File dir, String name) {
        return name.toLowerCase().endsWith(".txt");
      }
    };
    // list the files in the data folder
    String[] filenames = folder.list(txtFilter);
    for (int i = 0; i < filenames.length; i++) {
      filenames[i] = filenames[i].substring(0, filenames[i].lastIndexOf('.'));
    }
    return filenames;
  }

  public String arrayToString(String[] a, String separator) {
    StringBuffer result = new StringBuffer();
    if (a.length > 0) {
      result.append(a[0]);
      for (int i=1; i<a.length; i++) {
        result.append(separator);
        result.append(a[i]);
      }
    }
    //println(result.toString());
    return result.toString();
  }

  public void searchfunc(String main) {
    for (int i = 0; i < chords.length; i++) {
      leftidx[i] = findIt.starter(chords[i],main);
      if (leftidx[i] != -1) {
        finSearch = append(finSearch,filenames[i]);
        finny.put(filenames[i],i);
      }
      rightidx[i] = leftidx[i] == -1 ?  -1 : leftidx[i] + main.length() - 1;
    }
  }

  public void runSearch() {
    String[] leftRun = new String[0];
    String[] rightRun = new String[0];
    if (usrSub.equals("error")) {
      searchF.setColorScheme(GCScheme.YELLOW_SCHEME);
      searchF.setText("Music Chords Only");
    }
    else {
      searchfunc(usrSub);
      for (int i = 0; i < chords.length; i++) {
        //println("\n" + leftidx[i] + " " + ":" + " " + rightidx[i]); 
        if (leftidx[i] == -1 && rightidx[i] == -1) {
          //println(leftidx[i] + " " + ":" + " " + rightidx[i]);
          continue;
        }
        else if (leftidx[i] == 0) {
          rightRun = append(rightRun,chords[i].substring(rightidx[i] + 1,chords[i].length()));
        }
        else if (rightidx[i] == chords[i].length() - 1) {
          leftRun = append(leftRun,chords[i].substring(0,leftidx[i]));
        }
        else {
          //println(leftidx[i] + " " + ":" + " " + rightidx[i]);
          leftRun = append(leftRun,chords[i].substring(0,leftidx[i]));
          rightRun = append(rightRun,chords[i].substring(rightidx[i] + 1,chords[i].length()));
        }
      }

      if (leftRun.length == 0 && rightRun.length == 0) {
        searchF.setColorScheme(GCScheme.YELLOW_SCHEME);
        searchF.setText("No Matches. Try Again"); 
      }
      else {
        //println(usrSub);
        layoutL = setMapL.findMapL(leftRun,usrSub);
        layoutR = setMapR.findMapR(rightRun,usrSub);
        createWindows();
        searchF.setText("");  
        Manifest(layoutL,layoutR,finSearch,rightRun,leftRun);
        println(finny);
        finSearch = new String[0];
        finny = new HashMap();
        LinkedHashMap layoutL = new LinkedHashMap();
        LinkedHashMap layoutR = new LinkedHashMap();
      }
    }
  }

  public void remover() {
    frame.hide(); 
  }
}


UIWindow uiWindow;
Drawer drawer;


public void setup( ) {
  drawer = new Drawer();
  SearchWindow();
  zoomer = new ZoomPan(this);

}

public void SearchWindow() {
  uiWindow = new UIWindow(500,470); 
  frame.setTitle("And Rescue");
}

public void Manifest(LinkedHashMap layoutL,LinkedHashMap layoutR,String[] theSongs,String[] rights, String[] lefts) {
  //  println(layoutL);
  //  println(layoutR);
  //  println("Search Results");
  //  println(theSongs);
  //  println("Search REsults");
  drawer.setup(layoutL,layoutR,usrSub,theSongs, rights, lefts);
  drawer.Revert();
  realChords = drawer.Xchange(chords);
  println(realChords);
  go = true;
  frame.setLocation(50,100);
  frame.setAlwaysOnTop(true);
  uiWindow.hide();
  uiWindow.remover();



}

public void draw() {
  if (EndSearch == true && go == true) {
    drawer.mouseReleased();
    if (mousePressed == true) {
      drawer.mousePressed();
    }
    if (keyPressed == true){
      camera(width/2.0f, height/2.0f, (height/2.0f) / tan(PI*60.0f / 360.0f), width/2, height/2.0f, 0, 0, 1, 0);
    }
    else {
      zoomer.transform();  
    }
    drawer.draw();  
  }
}

public void mouseMoved() {
  if (keyPressed == true) {

  }
  else {
    if (mouseX >= width || mouseY >= height || mouseX <= 0 || mouseY <= 0) {
      //println("Yeah");
      camera(width/2.0f, height/2.0f, (height/2.0f) / tan(PI*60.0f / 360.0f), width/2, height/2.0f, 0, 0, 1, 0);
    }
  }
}








































































































































































































































































































































class Drawer  {

  final float S_LENGTH = 0;
  final float S_STRENGTH = 0.0f;
  final float S_DAMP = 0.5f;
  final float A_MIN = 0;
  final float A_STRENGTH = 0.0f;
  final float S_SCALAR = .1f;
  float maxDR = 0;
  float maxDL = 0;
  boolean Safe = true;
  SearchKMP Results;
  int qSearch = 0;
  String[] songNames;
  String[] chordsL;
  String[] chordsR;
  int[] rightV;
  String[] rightS;
  int[] leftV;
  String[] leftS;
  String[] tStringttt;

  int hit = 0;
  float ev1;
  float rc;
  float gc; 
  float bc;
  float alphac = 0; 

  LinkedHashMap tMap;
  LinkedHashMap findMap;

  LinkedHashMap tMap2;
  LinkedHashMap findMap2;

  HashMap keymap;

  int[] tMaprray;
  int[] tMaprray2;
  int maxBr_x;
  int maxBr_x2;
  int[] nHold;
  int[] nHold2;
  int nHMAX;

  int start1;
  int start2;
  int vars1 = -1;
  int vars2 = -1;
  boolean timer1 = true;
  boolean timer2 = false;
  boolean pRunner = true;
  boolean creation = true;
  String[] tString;
  String[] tString2;

  ParticleSystem datavis1;
  ParticleSystem datavis2;
  float scale = 1;
  float centroidX = 0; 
  float centroidY = 0;

  PFont font;
  PFont fontL;
  float[] start_p;
  float[] start_p2;
  int maxL;
  int maxR;
  int maxLR;

  int node_counter;
  int node_counter2;
  ZArrayList pTemp;
  ZArrayList pTemp2;
  HashMap popL;
  HashMap popR;
  float maxDelta;

  Drawer () {
    size(1200,600,OPENGL);
    background(200);
    frameRate(25);
    //    hint(ENABLE_OPENGL_4X_SMOOTH);
    //    hint(DISABLE_OPENGL_ERROR_REPORT);
  }

  public void setup(LinkedHashMap layoutL,LinkedHashMap layoutR,String usrSub,String[] sSongs, String[] rights, String[] lefts) {
    //println(findMap2);

    keymap = new HashMap();
    Revert();
    Results = new SearchKMP();
    songNames = sSongs;
    chordsR = Xchange(rights);
    chordsL = Xchange(lefts);

    /********************************************************************************************************************/
    /*LEFT*/
    findMap2 = new LinkedHashMap();
    tMap2 = layoutL;
    //tMap2 = new LinkedHashMap();
    //println(tMap2);
    //    tMap2.put("AA",0);
    //    tMap2.put("CCCCCCC",1);
    //    tMap2.put("DDDDD",2);
    //    tMap2.put("RRR",2);
    //    tMap2.put("DDD",1);
    //    tMap2.put("RRRRR",2);
    //    tMap2.put("RRR*II",3);
    //    tMap2.put("RARARARARARRRRRRRRRRRRRRRRRRR",4);
    //    tMap2.put("R1AGSGS",5);


    Integer[] tempL = new Integer[tMap2.size()];
    tString2 = new String[tMap2.size()];
    tStringttt = new String[tMap2.size()];
    arraycopy(tMap2.values().toArray(),tempL);
    arraycopy(tMap2.keySet().toArray(),tString2);
    arraycopy(tMap2.keySet().toArray(),tStringttt);
    tStringttt = Xchange(tStringttt);
    tString2 = XchangeLeft(tString2);

    //println(tString2);

    tMaprray2 = new int[tMap2.size()];
    tMaprray2 = ArrayUtils.toPrimitive(tempL);
    start_p2 = new float[tMaprray2.length];
    maxBr_x2 = max(tMaprray2);
    nHold2 = getCount(tMaprray2);
    println("WWWWWWWWWWW");
    println(nHold2);

    /*////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////*/
    /*RIGHT*/
    findMap = new LinkedHashMap();
    tMap = layoutR;
    //tMap = new LinkedHashMap();
    //println(tMap);
    //    tMap.put("J",1);
    //    tMap.put("JJJJJJJJJJJJJJ",2);
    //    tMap.put("BA",3);
    //    tMap.put("AAAAAAAAAAAAJJJJJJJJLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLT",3);
    //    tMap.put("TTTTTTTT",2);
    //    tMap.put("RRRRAQ",2);
    //    tMap.put("ABBABA",3);
    //    tMap.put("UUUU",3);
    //    tMap.put("RRRRR",4);
    //    tMap.put("RRR*IIIIIIIIIIIIII",4);
    //    tMap.put("RA",5);
    //    tMap.put("R1AGSGS",5);
    //    tMap.put("BBABBABA",2);


    Integer[] temper = new Integer[tMap.size()];
    tString = new String[tMap.size()];
    arraycopy(tMap.values().toArray(),temper);
    arraycopy(tMap.keySet().toArray(),tString);

    tString = Xchange(tString);
    //println(tString);
    tMaprray = new int[tMap.size()];
    tMaprray = ArrayUtils.toPrimitive(temper);
    start_p = new float[tMaprray.length];
    maxBr_x = max(tMaprray);
    nHold = getCount(tMaprray);

    /*/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////*/

    start1 = millis();
    start2 = millis();

    font = loadFont("Helvetica-112.vlw"); 
    fontL = loadFont("HelveticaNeue-Italic-112.vlw");
    textFont(font);
    textMode(MODEL);

    datavis1 = new ParticleSystem(0.0f,0.0f,0.0f,0.5f);
    datavis2 = new ParticleSystem(0.0f,0.0f,0.0f,0.5f);

    firstrun();
    if (tMaprray.length > 1) {
      popR = pusher(tMaprray);
      popL = pusher(tMaprray2);
      for (int pcle = 1; pcle < tMaprray.length; pcle++) {
        maxDR = createParticleRight(pcle,popR,maxDR);
      }
      for (int pcle = 1; pcle < tMaprray2.length; pcle++) {
        maxDL = createParticleLeft(pcle,popL,maxDL); 
      }
      start_p = textSizer(datavis1,tMaprray);
      start_p2 = textSizer(datavis2,tMaprray2);
      firstrun();
    }

    maxL = tMaprray2.length - 1;
    maxR = tMaprray.length - 1;

    if (maxL > maxR)
      maxLR = maxL;
    else
      maxLR = maxR;

    if (max(nHold2) >max(nHold))
      nHMAX = max(nHold2);
    else
      nHMAX = max(nHold);

    //maxDelta = maxDR + maxDL;
    //println(maxDL + "," + maxDR);
    println("Right stream");
    println(tMaprray);
    println("Left Stream");
    println(tMaprray2);
    println("index issue right");
    println(findMap);
    println("index issue left");
    println(findMap2);
    //    println("chordsR");
    //    println(chordsR);
    //    println("chordsL");
    //    println(chordsL);
    //println("strings");
    //println(tString);

    rightV = new int[0];
    rightS = new String[0];
    leftV = new int[0];
    leftS = new String[0];
    /****************Right******************/
    for (int i = 0; i < chordsR.length; i++) {
      for (int e = 1; e < tString.length; e++) {
        //println("yes");
        //println(chordsR[e]);
        if (chordsR[i].length() >= tString[e].length()) {
          qSearch = Results.starter(chordsR[i].substring(0,tString[e].length()),tString[e]);
          //println(qSearch + "," + i + "," + songNames[e]);

          if (qSearch > -1) {
            println("RIGHT");
            println(qSearch + "," + tString[e].length() + "," + e + "," + songNames[i]);
            rightV = append(rightV,e);
            rightS = append(rightS,songNames[i]);
            if (chordsR[i].length() > qSearch + tString[e].length() ) {
              chordsR[i] = chordsR[i].substring(qSearch + tString[e].length());
            }
            else {
              break; 
            }
          }
          else {
            for (int a = e + 1; a < tString.length ; a++) {
              if (tMaprray[a] ==  e) {
                e = a - 1;
                break; 
              }
            }
          }
        }
      }
    }

    /******************************LEFT***************************/
    for (int i = 0; i < chordsL.length; i++) {
      for (int e = 1; e < tStringttt.length; e++) {
        //println("yes");
        //println(chordsR[e]);
        if (chordsL[i].length() >= tStringttt[e].length()) {
          qSearch = Results.starter(chordsL[i].substring(0,tStringttt[e].length()),tStringttt[e]);
          //println(qSearch + "," + i + "," + songNames[e]);

          if (qSearch > -1) {
            println("LEFT");
            //println(qSearch + "," + tStringttt[e].length() + "," + e + "," + songNames[i]);
            leftV = append(leftV,e);
            leftS = append(leftS,songNames[i]);
            if (chordsL[i].length() > qSearch + tStringttt[e].length() ) {
              chordsL[i] = chordsL[i].substring(qSearch + tStringttt[e].length());
            }
            else {
              break; 
            }
          }
          else {
            for (int a = e + 1; a < tStringttt.length ; a++) {
              if (tMaprray2[a] ==  e) {
                e = a - 1;
                break; 
              }
            }
          }
        }
      }
    }   

    readMe();

  }

  public void draw () {
    datavis1.tick();
    datavis2.tick();
    background(200);
    smooth();


    if (millis() - start1 >= (10) && timer1 == true) {
      if (vars1 == maxLR || vars2 == maxLR) {
        timer1 = false;
      }
      if (vars1 != maxR) {
        vars1++; 
        start1 = millis();
      }
      if (vars2 != maxL) {
        vars2++;
        start1 = millis();
      }

      if (tMaprray.length > 1 && vars1 > 0) {
        createParticleRight(vars1,popR,0);
      }
      if (tMaprray2.length > 1 && vars2 > 0) {
        createParticleLeft(vars2,popL,0);
      } 
    }

    if ( datavis1.numberOfParticles() > 1 || datavis2.numberOfParticles() > 1) {
      updateCentroid();
    }

    translate(width/2, height/2 );


    scale(scale); 

    translate(-centroidX, -centroidY);

    //println(mouseX + "," + -centroidX);
    //println(centroidX + "," + "$" + centroidY+ "," + scale);
    //println(tMaprray2.length + "," + tMaprray.length + "," +  "*" + scale);

    drawLeft();
    drawRight(); 

    //println((maxDL + maxDR) - width);

  }

  public void drawRight() {
    textAlign(LEFT,TOP);
    strokeWeight(1);
    if (vars1 < datavis1.numberOfParticles()) {
      for (int i = 0; i <= vars1; i++) {
        Particle runV = datavis1.getParticle(i);
        Particle runPre = datavis1.getParticle(i);

        if (i == 0) {
          textSize(nHMAX);
          fill(255);
          if (Safe == true) {
            usrSub = XchangeS(usrSub); 
            Safe = false;
          }
          //println(usrSub);
          text(usrSub, runV.position().x() - textWidth(usrSub), runV.position().y());
          stroke(0);
          strokeWeight(3);
          line(runV.position().x() - textWidth(usrSub) + .5f,runV.position().y() + textAscent() + .2f,runV.position().x() - .5f,runV.position().y() + textAscent() + .2f);

        }
        else {
          textSize(nHold[i]);
          Integer retIT = (Integer)findMap.get(i);
          //println(i + "," + "*" + retIT);
          runPre = datavis1.getParticle(retIT);
          getMeCol(tMaprray[i]);

          text(tString[i], runPre.position().x(), runV.position().y());

          stroke(255,hit);
          if (i == 1) {
            textSize(nHMAX);
            float ZH = textAscent();
            textSize(nHold[i]);
            strokeWeight(1);
            line(runPre.position().x(),start_p[0],runPre.position().x(),start_p[0] + ZH);
          }
          else {
            line(runPre.position().x(),start_p[i],runPre.position().x(),start_p[i] + textAscent() + 0.3f);
          }
          ellipseMode(CENTER);
          fill(100);
          stroke(0);
          ellipse(runPre.position().x(),start_p[i] + textAscent()/2,.5f,.5f);
          stroke(255,hit);
          strokeWeight(1);
          line(runPre.position().x(),start_p[i],runV.position().x(),runV.position().y());
          float nadd = textAscent();

          //numbers
          fill(255);
          textAlign(CENTER,CENTER);
          textSize(.5f);
          text(i,runPre.position().x(),runV.position().y() + nadd/2);
          textAlign(LEFT,TOP);   
        }
      }
    }
  }


  public float createParticleRight(int i,HashMap q, float maxW) { 
    textAlign(LEFT,TOP);  
    Particle node = datavis1.makeParticle(nHold[i] + 1,0,0,0);
    Particle nodePre = datavis1.getParticle(0);

    if (datavis1.numberOfParticles() > 1) {

      textSize(nHold[i]);
      maxW+= textWidth(tString[i]);


      if (tMaprray[i] >= tMaprray[i-1] + 1) {
        findMap.put(datavis1.numberOfParticles() - 1,node_counter);
        nodePre = datavis1.getParticle(node_counter);
        textSize(nHold[i]);
        node.position().set(nodePre.position().x() + textWidth(tString[i]),nodePre.position().y(),0);
        pTemp.add(node);
        //distancer(datavis1,nodePre,node);
        makeJoint(datavis1,nodePre,node,S_STRENGTH,S_DAMP,textWidth(tString[i]));
        node_counter++;
      }
      else {
        int tempnode = 0; 
        for (int j = i - 1; j >=0; j--) {
          if (tMaprray[j] == tMaprray[i]) {
            tempnode = j;
            break;
          }
        }
        if (findMap.containsKey(tempnode)) {
          node_counter = (int)(Integer)findMap.get(tempnode);
          findMap.put(datavis1.numberOfParticles() - 1,node_counter);
          nodePre = datavis1.getParticle(node_counter);
          if (tMaprray[i] >= tMaprray[i-1]) {
            Particle prev = (Particle)(pTemp.get(pTemp.size() - 1));
            textSize(nHold[i - 1]);
            float rest = textAscent();
            textSize(nHold[i]);
            node.position().set(nodePre.position().x() + textWidth(tString[i]),prev.position().y() + rest + 0.1f,0);
            //distancer(datavis1,nodePre,node);
            makeJoint(datavis1,nodePre,node,S_STRENGTH,S_DAMP, textWidth(tString[i]));
            pTemp.remove(pTemp.size() - 1);
            pTemp.add(node);
          }
          else {
            Particle prev = (Particle)(pTemp.get(pTemp.size() - 1));
            Particle up = datavis1.getParticle((Integer)q.get(i));
            //println((Integer)q.get(i));
            textSize(nHold[(Integer)q.get(i)]);
            float rest = textAscent();
            textSize(nHold[i]);
            if (prev.position().y() > up.position().y() + rest) {
              node.position().set(nodePre.position().x() + textWidth(tString[i]),prev.position().y() + 0.1f,0);
            }
            else {
              node.position().set(nodePre.position().x() + textWidth(tString[i]),up.position().y() + rest + 0.1f,0);
            }
            //distancer(datavis1,nodePre,node);
            makeJoint(datavis1,nodePre,node,S_STRENGTH,S_DAMP,  textWidth(tString[i]));
            pTemp.removeRange(tMaprray[node_counter + 1],pTemp.size());
            pTemp.add(node);
          }
          node_counter = datavis1.numberOfParticles() - 1;
        }
      }
    }
    return maxW;
  }


  public void firstrun() {
    datavis1.clear();
    datavis2.clear();
    node_counter = 0;
    node_counter2 = 0;
    pTemp = new ZArrayList();
    pTemp2 = new ZArrayList();
    Particle ONE = datavis1.makeParticle(nHold[0] + 1,0,0,0);
    Particle TWO = datavis2.makeParticle(nHold2[0] + 1,0,0,0);
    pTemp.add(ONE);
    pTemp2.add(TWO);
  }


  public void drawLeft() {
    //scale(-1,1);
    textAlign(RIGHT,TOP);
    strokeWeight(1);
    if (vars2 < datavis2.numberOfParticles()) {
      for (int i = 0; i <= vars2; i++) {
        Particle runV = datavis2.getParticle(i);
        Particle runPre = datavis2.getParticle(i);
        if (i == 0) {
          continue;
        }
        else {
          textSize(nHMAX);
          float ZERO = textWidth(usrSub);
          textSize(nHold2[i]);
          Integer retIT = (Integer)findMap2.get(i);
          //println(retIT);
          //println(tMaprray);
          runPre = datavis2.getParticle(retIT);
          getMeCol(tMaprray2[i]);

          text(tString2[i], runPre.position().x() - ZERO , runV.position().y());
          textSize(nHold2[i]);
          stroke(255,hit);
          if (i == 1) {
            line(runPre.position().x() - ZERO,start_p2[0],runPre.position().x() - ZERO,start_p2[0] + textAscent());
            stroke(0);
            fill(100);
            ellipseMode(CENTER);
            ellipse(runPre.position().x() - ZERO,start_p2[1] + textAscent()/2,.5f,.5f);
            stroke(255,hit);
            strokeWeight(1);
            line(runPre.position().x() - ZERO,start_p2[0],runV.position().x() - ZERO,runV.position().y());
            float nadd = textAscent();

            //numbers
            fill(255);
            textAlign(CENTER,CENTER);
            textSize(.4f);
            text(i,runPre.position().x() - ZERO,start_p2[1] + nadd/2);
            textAlign(RIGHT,TOP);  

          }
          else {
            textSize(nHold2[i]);
            line(runPre.position().x() - ZERO,start_p2[i],runPre.position().x() - ZERO,start_p2[i] + textAscent() + 0.3f);
            stroke(0);
            fill(100);
            ellipseMode(CENTER);
            ellipse(runPre.position().x() - ZERO,start_p2[i] + textAscent()/2,.5f,.5f); 
            stroke(255,hit);
            strokeWeight(1);
            line(runPre.position().x() - ZERO,start_p2[i],runV.position().x() - ZERO,runV.position().y());
            float nadd = textAscent();

            //numbers
            fill(255);
            textAlign(CENTER,CENTER);
            textSize(.4f);
            text(i,runPre.position().x() - ZERO,start_p2[i] + nadd/2);
            textAlign(RIGHT,TOP);  
          }
        }
      }
    }
  }


  public float createParticleLeft(int i,HashMap q, float maxW) { 
    textAlign(RIGHT,TOP);  
    Particle node = datavis2.makeParticle(nHold2[i] + 1,0,0,0);
    Particle nodePre = datavis2.getParticle(0);

    if (datavis2.numberOfParticles() > 1) {

      textSize(nHold2[i]);
      maxW+= textWidth(tString2[i]);

      if (tMaprray2[i] >= tMaprray2[i-1] + 1) {
        findMap2.put(datavis2.numberOfParticles() - 1,node_counter2);
        nodePre = datavis2.getParticle(node_counter2);
        textSize(nHold2[i]);
        // println(nodePre.position().x() - textWidth(tString2[i]));
        node.position().set(nodePre.position().x() - textWidth(tString2[i]) ,nodePre.position().y(),0);
        pTemp2.add(node);
        makeJoint(datavis2,nodePre,node,S_STRENGTH,S_DAMP,textWidth(tString2[i]));
        node_counter2++;
      }
      else {
        int tempnode = 0; 
        for (int j = i - 1; j >=0; j--) {
          if (tMaprray2[j] == tMaprray2[i]) {
            tempnode = j;
            break;
          }
        }
        if (findMap2.containsKey(tempnode)) {
          node_counter2 = (int)(Integer)findMap2.get(tempnode);
          findMap2.put(datavis2.numberOfParticles() - 1,node_counter2);
          nodePre = datavis2.getParticle(node_counter2);
          if (tMaprray2[i] >= tMaprray2[i-1]) {
            Particle prev = (Particle)(pTemp2.get(pTemp2.size() - 1));
            textSize(nHold2[i - 1]);
            float rest = textAscent();
            textSize(nHold2[i]);
            node.position().set(nodePre.position().x() - textWidth(tString2[i]) ,prev.position().y() + rest + 0.3f,0);
            makeJoint(datavis2,nodePre,node,S_STRENGTH,S_DAMP, textWidth(tString2[i]));
            pTemp2.remove(pTemp2.size() - 1);
            pTemp2.add(node);
          }
          else {
            Particle prev= (Particle)(pTemp2.get(pTemp2.size() - 1));
            Particle up = datavis2.getParticle((Integer)q.get(i));
            //println((Integer)q.get(i));
            textSize(nHold2[(Integer)q.get(i)]);
            float rest = textAscent();
            textSize(nHold2[i]);
            //println(rest + "," + textAscent());
            if (prev.position().y() > up.position().y() + rest) {
              node.position().set(nodePre.position().x() - textWidth(tString2[i]),prev.position().y() + 0.3f,0);
            }
            else {
              node.position().set(nodePre.position().x() - textWidth(tString2[i]),up.position().y() + rest + 0.3f,0);
            }
            makeJoint(datavis2,nodePre,node,S_STRENGTH,S_DAMP,  textWidth(tString2[i]));
            pTemp2.removeRange(tMaprray2[node_counter2 + 1],pTemp2.size());        
            pTemp2.add(node);
          }
          node_counter2 = datavis2.numberOfParticles() - 1;
        }
      }
    }
    return maxW;
  }

  public void makeJoint(ParticleSystem PS,Particle a, Particle b, float strength, float damp, float lengther) {
    Spring s = PS.makeSpring(a,b,strength,damp,lengther);
  }

  public void distancer(ParticleSystem PS,Particle a, Particle b) {
    PS.makeAttraction(a,b,A_STRENGTH, A_MIN);
  }



  //  void mouseMoved() {
  //    if (mouseX > width - 10.0 || mouseY > height - 10.0 || mouseX < 10.0 || mouseY < 10.0 || mouseX == 0 || mouseY == 0) {
  //      println("Yeah");
  //      camera(width/2.0, height/2.0, (height/2.0) / tan(PI*60.0 / 360.0), width/2, height/2.0, 0, 0, 1, 0);
  //    }
  //    else
  //      camera(mouseX, height/2.0,mouseY, mouseX, height/2.0, 0, 0, 1, 0);
  //
  //    println(mouseX + "," + mouseY);
  //  }

  public void mouseReleased() {
    hit = 0;
  }

  public void mousePressed() {
    hit = 255; 
  }


  public int[] getCount(int[] maxer) {
    int count = 0; 
    int[] nB = new int[maxer.length];
    int[] nH = new int[maxer.length];

    for (int i = 0; i < maxer.length; i++) {
      for (int j = 1 + i; j < maxer.length; j++) {
        if (maxer[j] == maxer[i] + 1) {
          count++;
        }
        else if (maxer[j] == maxer[i]) {
          break; 
        }
      }
      nB[i] = count;
      count = 0;  
    }

    for (int t = 1; t < nB.length; t++) {
      //      if (nB[t] == 1)
      //        nB[t] = 0; 
    }

    int sum = 0;
    for (int each = 0; each < maxer.length; each++) {
      sum = nB[each];
      for (int k = each + 1; k < maxer.length; k++) {
        if (maxer[k] <= maxer[each]) {
          break; 
        }
        else {
          sum += nB[k]; 
        }
      }
      if (sum == 0) {
        nH[each] = 1;
      }
      else if (sum == 1) {
        //println("roro");
        nH[each] = 2; 
      }
      else {
        nH[each] = sum + 2; 
      }
      sum = 0;
    }
    return nH;
  }

  /***********************************************************/
  public void updateCentroid()
  {

    float 
      xMax = Float.NEGATIVE_INFINITY, 
    xMin = Float.POSITIVE_INFINITY, 
    yMin = Float.POSITIVE_INFINITY, 
    yMax = Float.NEGATIVE_INFINITY;

    for ( int i = 0; i < datavis1.numberOfParticles() - 1; ++i )
    {

      Particle p = datavis1.getParticle( i );
      xMax = max( xMax, p.position().x());
      xMin = min( xMin, p.position().x());
      yMin = min( yMin, p.position().y() );
      yMax = max( yMax, p.position().y() );
    }

    for ( int a = 0; a < datavis2.numberOfParticles() - 1; ++a )
    {
      Particle c = datavis2.getParticle( a );
      xMax = max( xMax, c.position().x());
      xMin = min( xMin, c.position().x());
      yMin = min( yMin, c.position().y() );
      yMax = max( yMax, c.position().y() );
    }


    float deltaX = xMax-xMin;
    float deltaY = yMax-yMin;

    textSize(nHMAX);
    centroidX = xMin + (0.5f*deltaX) - (textWidth(usrSub)/2);
    centroidY = yMin + 0.5f*deltaY;

    if ( deltaY > deltaX )
      scale = height/(deltaY + 150);
    else
      scale = (width)/(deltaX + 150);
  }


  /*********************************************************************************************************/

  public class ZArrayList extends ArrayList {
    public void removeRange(int fromIndex, int toIndex) {
      super.removeRange(fromIndex, toIndex);
    }
  }


  public HashMap pusher(int[] gunner) {
    HashMap r1 = new HashMap();
    for (int counter = gunner.length - 1; counter > 0; counter--) {
      int minIT = -1;
      for (int b = counter - 1; b >= 0; b--) {
        if (gunner[b] == gunner[counter]) {
          minIT = b;
          break;
        }
      }
      if (minIT != counter && minIT != -1) {
        r1.put(counter,minIT);
      }
    }
    //println(r1);
    return r1;
  }

  public void getMeCol(int num) {
    colorMode(RGB);
    if (maxBr_x == 0) 
      maxBr_x = 1;

    ev1 = 255/maxBr_x;

    if (maxBr_x <= 3)
      rc = ev1 * num;
    else if (maxBr_x > 3 && maxBr_x <= 10)
      rc = ev1 * num;
    else 
      rc = rc = ev1 * num * (-2.2f);

    if (maxBr_x >= 5) 
      gc = ev1 * num/2;
    else 
      gc = 0;

    if (maxBr_x >= 10) 
      bc = ev1 * num/2;
    else
      bc = 2 + num;
    //println(num +"," +rc+","+ev1);
    fill(rc,gc,bc);
  }

  public float[] textSizer(ParticleSystem PS,int[] Map_array) {
    float[] starts = new float[Map_array.length];
    float[] stops = new float[Map_array.length];

    for (int i = 0; i < Map_array.length; i++) {
      if (i == 0 && Map_array.length > 1) {
        starts[i] = PS.getParticle(i+1).position().y();
      }
      else if (i == Map_array.length - 1) {
        starts[i] = PS.getParticle(i).position().y();
        stops[i] = PS.getParticle(i).position().y();
      }
      else {
        starts[i] = PS.getParticle(i).position().y();
      }
      for (int j = i + 1; j < Map_array.length; j++) {
        if (Map_array[i] >= Map_array[j]) {
          stops[i] = PS.getParticle(j - 1).position().y();
          break;
        } 
        else if (j == tMaprray.length - 1) {
          stops[i] = PS.getParticle(j).position().y();
        }
      }
    }
    return starts;
  }


  public void Revert() {
    keymap.put("A","C");
    keymap.put("B","C#");
    keymap.put("C","D");
    keymap.put("D","D#");
    keymap.put("E","E");
    keymap.put("F","F");
    keymap.put("G","F#");
    keymap.put("H","G");
    keymap.put("I","G#");
    keymap.put("J","A");
    keymap.put("K","A#");
    keymap.put("L","B");
    keymap.put("M","Cm");
    keymap.put("N","C#m");
    keymap.put("O","Dm");
    keymap.put("P","D#m");
    keymap.put("Q","Em");
    keymap.put("R","Fm");
    keymap.put("S","F#m");
    keymap.put("T","Gm");
    keymap.put("U","G#m");
    keymap.put("V","Am");
    keymap.put("W","A#m");
    keymap.put("X","Bm");
    //println(keymap);
  }

  public String[] Xchange(String[] tList) {
    for (int s = 0; s < tList.length; s++) {
      StringBuffer sb = new StringBuffer();
      String[] splitter = split(tList[s],"*");
      tList[s] = splitter[0];
      for (int l = 0; l < tList[s].length(); l++) {
        if (keymap.containsKey(str(tList[s].charAt(l)))) {
          sb.append(keymap.get(str(tList[s].charAt(l))));
        }
      }
      tList[s] = sb.toString();
    }
    return tList;
  }

  public String XchangeS(String tList) {
    StringBuffer sb = new StringBuffer();
    for (int s = 0; s < tList.length(); s++) {
      if (keymap.containsKey(str(tList.charAt(s)))) {
        sb.append(keymap.get(str(tList.charAt(s))));
      }
    }
    tList = sb.toString();
    return tList;
  }

  public String[] XchangeLeft(String[] tList) {
    for (int s = 0; s < tList.length; s++) {
      StringBuffer sb = new StringBuffer();
      String[] splitter = split(tList[s],"*");
      tList[s] = splitter[0];
      tList[s] = reverseit(tList[s]);
      for (int l = 0; l < tList[s].length(); l++) {
        if (keymap.containsKey(str(tList[s].charAt(l)))) {
          sb.append(keymap.get(str(tList[s].charAt(l))));
        }
      }
      tList[s] = sb.toString();
    }
    return tList;
  }

  public String reverseit(String current) {
    StringBuffer buffer = new StringBuffer(current);
    buffer = buffer.reverse();
    String reverseString = buffer.toString(); 
    return reverseString;
    //println(test);
    //println(reverseString);
  }

  public void readMe() {
    String[] pathR = new String[rightV.length];
    String[] pathL = new String[leftV.length];
    for (int i = 0; i < rightV.length; i++) {
      pathR[i] = rightS[i] + "-" + Integer.toString(rightV[i]);
    }
    for (int i = 0; i < leftV.length; i++) {
      pathL[i] = leftS[i] + "-" + Integer.toString(leftV[i]);
    }

    saveStrings("pathLeft.txt",pathL);
    saveStrings("pathRight.txt",pathR);
  }



}










































































































































































































































































































































































































































































































































































































































































































































































































































































class SearchKMP {

  int[] fail;
  int startIndex;
  int stopIndex;

  public int starter(String songstring, String small) {
    //String songstring = "AAACABCABCARRRRRAAAAAAAAAAANNNNNNNNOOOOO"; 
    //String small = "CA";

    stopIndex = preprocess(songstring, small);
    startIndex = searchkmp(songstring, small);

    //stopper(startIndex,stopIndex);

    //println("\n" + startIndex + " " + ":" + " " + (startIndex + stopIndex - 1));

    return startIndex;
  }

  //  int stopper(int startIndex,int stopIndex) {
  //    
  //  }


  public int preprocess (String songstring, String small) {
    fail = new int[small.length()];
    fail[0] = 0;
    int j = 0;

    for (int i = 1; i < small.length(); i++) {
      while ((j > 0) && (small.charAt(j) != small.charAt(i))) { 
        j = fail[j - 1]; 
      }
      if (small.charAt(j) == small.charAt(i)) { 
        j++; 
      }
      fail[i] = j;
    }

    for (int i = 0; i < fail.length; i++) {
      //print(fail[i] + " "); 
    }
    return fail.length;
  }

  public int searchkmp (String songstring, String small) {
    int i = 0; 
    int j = 0;
    //int saver;


    while (i < songstring.length()) {
      if (songstring.charAt(i) == small.charAt(j)) {
        if(j == small.length() - 1) {
          //j = -1;
          return (i - small.length() + 1);
          //println("\n" + saver);
        }      
        i++;
        j++;
      }
      else if(j > 0) {
        j = fail[j - 1]; 
      }
      else {
        i++; 
      }
    }
    return - 1;
  }
}




















































class TreeSet {


  public LinkedHashMap findMapL(String[] leftStrings,String usrSub) {
    RadixTreeImpl tree = new RadixTreeImpl();
    LinkedHashMap temptree = new LinkedHashMap();
    LinkedHashMap maptree = new LinkedHashMap();

    for (int k = 0; k < leftStrings.length; k++) {
      leftStrings[k] = reverseit(leftStrings[k]);
    }
    insertStrings(leftStrings, tree);
    println("\nPRINTING LEFT TREE");
    temptree = tree.display();
    //println("*" + temptree);
    maptree.put("*",0);
    maptree.putAll(temptree);
    maptree.remove("");
    //println("Left SET");
    //println(maptree.keySet());
    return maptree;
  }

  public LinkedHashMap findMapR(String[] rightStrings,String usrSub) {
    RadixTreeImpl tree = new RadixTreeImpl();
    LinkedHashMap temptree = new LinkedHashMap();
    LinkedHashMap maptree = new LinkedHashMap();

    insertStrings(rightStrings, tree);
    println("\nPRINTING RIGHT TREE");
    temptree = tree.display();
    maptree.put("*",0);
    maptree.putAll(temptree);
    maptree.remove("");
    //println("Right SET");
    //println(maptree.keySet());
    return maptree;
  }


  public String reverseit(String current) {
    StringBuffer buffer = new StringBuffer(current);
    buffer = buffer.reverse();
    String reverseString = buffer.toString(); 
    return reverseString;
    //println(test);
    //println(reverseString);
  }

  public void insertStrings(String[] roots, RadixTreeImpl tree) {
    for (int i = 0; i < roots.length; i++) {
      tree.insert(roots[i],roots[i]);
    }

  }

}





































  static public void main(String args[]) {
    PApplet.main(new String[] { "--present", "--bgcolor=#666666", "--stop-color=#cccccc", "text_search_yes" });
  }
}
