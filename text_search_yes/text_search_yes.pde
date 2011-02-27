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

    downloadSong = new GButton(this,"Play Original Song",width/2 - 85,75,20,20);
    downloadString = new GButton(this,"View Original's Chords",width/2 - 100,118,20,20);

    btnEnter = new GButton(this, "Search",(width/2) - (100/2) - 10,270,100,30);
    btnEnter.setColorScheme(GCScheme.GREY_SCHEME);

    filenames = getFiles();
    finSearch = new String[0];
    finny = new HashMap();

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

    findIt = new SearchKMP();
    leftidx = new int[chords.length];
    rightidx = new int[chords.length];

    setMapL = new TreeSet();
    setMapR = new TreeSet();
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

  public void createWindows() {
    EndSearch = true;
    try {
      Robot r = new Robot();
      r.mouseMove(0,0);
    } 
    catch(AWTException e) {
    }
  }


  public void handleButtonEvents(GButton button) {
    String enteredText = searchF.getText().replaceAll("\\s","");
    if((button.getText().equals("Search") && !enteredText.equals("")) && (button.eventType == GButton.CLICKED)) {
      searchF.setColorScheme(GCScheme.GREY_SCHEME);
      usrSub = stringOutput();
      runSearch();
      timer = second();
    }

    if (button.getText().equals("Play Original Song") && (button.eventType == GButton.CLICKED)) {
      open("/Users/zeeshanlakhani/Documents/Processing/text_search_yes/data/echoplex_orig.m3u");
    } 
    if (button.getText().equals("View Original's Chords") && (button.eventType == GButton.CLICKED)) {
      open("/Users/zeeshanlakhani/Documents/Processing/text_search_yes/data/output_first chord.pdf");
    }
  }

  void keyPressed() {
    String enteredText = searchF.getText().replaceAll("\\s","");
    if ((!enteredText.equals("")) && (key == ENTER)) {
      //start();
      searchF.setColorScheme(GCScheme.GREY_SCHEME);
      usrSub = stringOutput();
      runSearch();
      timer = second();
    }
  }
  String stringOutput() {
    String stringtest = searchF.getText();
    stringtest = stringtest.replaceAll("\\s","");

    String temp1; 
    String temp2;

    String[] stringArry = new String[0];
    for (int i = 0; i < stringtest.length(); i++) {
      if (i == stringtest.length() - 1) {
        stringArry = append(stringArry,replacer(Character.toString(stringtest.charAt(i))));
        String check = replacer(Character.toString(stringtest.charAt(i)));
        if (check.equals("error")) {
          return "error";
        }
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

  String replacer(String note) {
    if (keymap.containsKey(note)) {
      String symbol = keymap.get(note).toString();
      return symbol;
    }
    else {
      return "error";
    }
  }

  void themapSelf() {
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

  String[] getFiles() {
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

  String arrayToString(String[] a, String separator) {
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

  void searchfunc(String main) {
    for (int i = 0; i < chords.length; i++) {
      leftidx[i] = findIt.starter(chords[i],main);
      if (leftidx[i] != -1) {
        finSearch = append(finSearch,filenames[i]);
        finny.put(filenames[i],i);
      }
      rightidx[i] = leftidx[i] == -1 ?  -1 : leftidx[i] + main.length() - 1;
    }
  }

  void runSearch() {
    String[] leftRun = new String[0];
    String[] rightRun = new String[0];
    if (usrSub.equals("error")) {
      searchF.setColorScheme(GCScheme.YELLOW_SCHEME);
      searchF.setText("Music Chords Only");
    }
    else {
      searchfunc(usrSub);
      for (int i = 0; i < chords.length; i++) {
        if (leftidx[i] == -1 && rightidx[i] == -1) {
          continue;
        }
        else if (leftidx[i] == 0) {
          rightRun = append(rightRun,chords[i].substring(rightidx[i] + 1,chords[i].length()));
        }
        else if (rightidx[i] == chords[i].length() - 1) {
          leftRun = append(leftRun,chords[i].substring(0,leftidx[i]));
        }
        else {
          leftRun = append(leftRun,chords[i].substring(0,leftidx[i]));
          rightRun = append(rightRun,chords[i].substring(rightidx[i] + 1,chords[i].length()));
        }
      }

      if (leftRun.length == 0 && rightRun.length == 0) {
        searchF.setColorScheme(GCScheme.YELLOW_SCHEME);
        searchF.setText("No Matches. Try Again");
      }
      else {
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

  void remover() {
    frame.hide();
  }
}


UIWindow uiWindow;
Drawer drawer;


void setup( ) {
  drawer = new Drawer();
  SearchWindow();
  zoomer = new ZoomPan(this);
}

void SearchWindow() {
  uiWindow = new UIWindow(500,470); 
  frame.setTitle("And Rescue");
}

void Manifest(LinkedHashMap layoutL,LinkedHashMap layoutR,String[] theSongs,String[] rights, String[] lefts) {
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

void draw() {
  if (EndSearch == true && go == true) {
    drawer.mouseReleased();
    if (mousePressed == true) {
      drawer.mousePressed();
    }
    if (keyPressed == true) {
      camera(width/2.0, height/2.0, (height/2.0) / tan(PI*60.0 / 360.0), width/2, height/2.0, 0, 0, 1, 0);
    }
    else {
      zoomer.transform();
    }
    drawer.draw();
  }
}

void mouseMoved() {
  if (keyPressed == true) {
  }
  else {
    if (mouseX >= width || mouseY >= height || mouseX <= 0 || mouseY <= 0) {
      camera(width/2.0, height/2.0, (height/2.0) / tan(PI*60.0 / 360.0), width/2, height/2.0, 0, 0, 1, 0);
    }
  }
}

