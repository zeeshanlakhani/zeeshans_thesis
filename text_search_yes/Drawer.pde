class Drawer {

  final float S_LENGTH = 0;
  final float S_STRENGTH = 0.0;
  final float S_DAMP = 0.5;
  final float A_MIN = 0;
  final float A_STRENGTH = 0.0;
  final float S_SCALAR = .1;
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
  }

  void setup(LinkedHashMap layoutL,LinkedHashMap layoutR,String usrSub,String[] sSongs, String[] rights, String[] lefts) {

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

    Integer[] tempL = new Integer[tMap2.size()];
    tString2 = new String[tMap2.size()];
    tStringttt = new String[tMap2.size()];
    arraycopy(tMap2.values().toArray(),tempL);
    arraycopy(tMap2.keySet().toArray(),tString2);
    arraycopy(tMap2.keySet().toArray(),tStringttt);
    tStringttt = Xchange(tStringttt);
    tString2 = XchangeLeft(tString2);

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


    Integer[] temper = new Integer[tMap.size()];
    tString = new String[tMap.size()];
    arraycopy(tMap.values().toArray(),temper);
    arraycopy(tMap.keySet().toArray(),tString);

    tString = Xchange(tString);
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

    datavis1 = new ParticleSystem(0.0,0.0,0.0,0.5);
    datavis2 = new ParticleSystem(0.0,0.0,0.0,0.5);

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

    println("Right stream");
    println(tMaprray);
    println("Left Stream");
    println(tMaprray2);
    println("index issue right");
    println(findMap);
    println("index issue left");
    println(findMap2);

    rightV = new int[0];
    rightS = new String[0];
    leftV = new int[0];
    leftS = new String[0];
    /****************Right******************/
    for (int i = 0; i < chordsR.length; i++) {
      for (int e = 1; e < tString.length; e++) {
        if (chordsR[i].length() >= tString[e].length()) {
          qSearch = Results.starter(chordsR[i].substring(0,tString[e].length()),tString[e]);

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
        if (chordsL[i].length() >= tStringttt[e].length()) {
          qSearch = Results.starter(chordsL[i].substring(0,tStringttt[e].length()),tStringttt[e]);

          if (qSearch > -1) {
            println("LEFT");
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

  void draw () {
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

    drawLeft();
    drawRight();
  }

  void drawRight() {
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
          text(usrSub, runV.position().x() - textWidth(usrSub), runV.position().y());
          stroke(0);
          strokeWeight(3);
          line(runV.position().x() - textWidth(usrSub) + .5,runV.position().y() + textAscent() + .2,runV.position().x() - .5,runV.position().y() + textAscent() + .2);
        }
        else {
          textSize(nHold[i]);
          Integer retIT = (Integer)findMap.get(i);
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
            line(runPre.position().x(),start_p[i],runPre.position().x(),start_p[i] + textAscent() + 0.3);
          }
          ellipseMode(CENTER);
          fill(100);
          stroke(0);
          ellipse(runPre.position().x(),start_p[i] + textAscent()/2,.5,.5);
          stroke(255,hit);
          strokeWeight(1);
          line(runPre.position().x(),start_p[i],runV.position().x(),runV.position().y());
          float nadd = textAscent();

          fill(255);
          textAlign(CENTER,CENTER);
          textSize(.5);
          text(i,runPre.position().x(),runV.position().y() + nadd/2);
          textAlign(LEFT,TOP);
        }
      }
    }
  }


  float createParticleRight(int i,HashMap q, float maxW) { 
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
            node.position().set(nodePre.position().x() + textWidth(tString[i]),prev.position().y() + rest + 0.1,0);
            makeJoint(datavis1,nodePre,node,S_STRENGTH,S_DAMP, textWidth(tString[i]));
            pTemp.remove(pTemp.size() - 1);
            pTemp.add(node);
          }
          else {
            Particle prev = (Particle)(pTemp.get(pTemp.size() - 1));
            Particle up = datavis1.getParticle((Integer)q.get(i));
            textSize(nHold[(Integer)q.get(i)]);
            float rest = textAscent();
            textSize(nHold[i]);
            if (prev.position().y() > up.position().y() + rest) {
              node.position().set(nodePre.position().x() + textWidth(tString[i]),prev.position().y() + 0.1,0);
            }
            else {
              node.position().set(nodePre.position().x() + textWidth(tString[i]),up.position().y() + rest + 0.1,0);
            }
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


  void firstrun() {
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


  void drawLeft() {
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
          runPre = datavis2.getParticle(retIT);
          getMeCol(tMaprray2[i]);

          text(tString2[i], runPre.position().x() - ZERO, runV.position().y());
          textSize(nHold2[i]);
          stroke(255,hit);
          if (i == 1) {
            line(runPre.position().x() - ZERO,start_p2[0],runPre.position().x() - ZERO,start_p2[0] + textAscent());
            stroke(0);
            fill(100);
            ellipseMode(CENTER);
            ellipse(runPre.position().x() - ZERO,start_p2[1] + textAscent()/2,.5,.5);
            stroke(255,hit);
            strokeWeight(1);
            line(runPre.position().x() - ZERO,start_p2[0],runV.position().x() - ZERO,runV.position().y());
            float nadd = textAscent();

            fill(255);
            textAlign(CENTER,CENTER);
            textSize(.4);
            text(i,runPre.position().x() - ZERO,start_p2[1] + nadd/2);
            textAlign(RIGHT,TOP);
          }
          else {
            textSize(nHold2[i]);
            line(runPre.position().x() - ZERO,start_p2[i],runPre.position().x() - ZERO,start_p2[i] + textAscent() + 0.3);
            stroke(0);
            fill(100);
            ellipseMode(CENTER);
            ellipse(runPre.position().x() - ZERO,start_p2[i] + textAscent()/2,.5,.5); 
            stroke(255,hit);
            strokeWeight(1);
            line(runPre.position().x() - ZERO,start_p2[i],runV.position().x() - ZERO,runV.position().y());
            float nadd = textAscent();

            fill(255);
            textAlign(CENTER,CENTER);
            textSize(.4);
            text(i,runPre.position().x() - ZERO,start_p2[i] + nadd/2);
            textAlign(RIGHT,TOP);
          }
        }
      }
    }
  }


  float createParticleLeft(int i,HashMap q, float maxW) { 
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
        node.position().set(nodePre.position().x() - textWidth(tString2[i]),nodePre.position().y(),0);
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
            node.position().set(nodePre.position().x() - textWidth(tString2[i]),prev.position().y() + rest + 0.3,0);
            makeJoint(datavis2,nodePre,node,S_STRENGTH,S_DAMP, textWidth(tString2[i]));
            pTemp2.remove(pTemp2.size() - 1);
            pTemp2.add(node);
          }
          else {
            Particle prev= (Particle)(pTemp2.get(pTemp2.size() - 1));
            Particle up = datavis2.getParticle((Integer)q.get(i));
            textSize(nHold2[(Integer)q.get(i)]);
            float rest = textAscent();
            textSize(nHold2[i]);
            if (prev.position().y() > up.position().y() + rest) {
              node.position().set(nodePre.position().x() - textWidth(tString2[i]),prev.position().y() + 0.3,0);
            }
            else {
              node.position().set(nodePre.position().x() - textWidth(tString2[i]),up.position().y() + rest + 0.3,0);
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

  void makeJoint(ParticleSystem PS,Particle a, Particle b, float strength, float damp, float lengther) {
    Spring s = PS.makeSpring(a,b,strength,damp,lengther);
  }

  void distancer(ParticleSystem PS,Particle a, Particle b) {
    PS.makeAttraction(a,b,A_STRENGTH, A_MIN);
  }


  void mouseReleased() {
    hit = 0;
  }

  void mousePressed() {
    hit = 255;
  }


  int[] getCount(int[] maxer) {
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
  void updateCentroid()
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
    centroidX = xMin + (0.5*deltaX) - (textWidth(usrSub)/2);
    centroidY = yMin + 0.5*deltaY;

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


  HashMap pusher(int[] gunner) {
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
    return r1;
  }

  void getMeCol(int num) {
    colorMode(RGB);
    if (maxBr_x == 0) 
      maxBr_x = 1;

    ev1 = 255/maxBr_x;

    if (maxBr_x <= 3)
      rc = ev1 * num;
    else if (maxBr_x > 3 && maxBr_x <= 10)
      rc = ev1 * num;
    else 
      rc = rc = ev1 * num * (-2.2);

    if (maxBr_x >= 5) 
      gc = ev1 * num/2;
    else 
      gc = 0;

    if (maxBr_x >= 10) 
      bc = ev1 * num/2;
    else
      bc = 2 + num;
    fill(rc,gc,bc);
  }

  float[] textSizer(ParticleSystem PS,int[] Map_array) {
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


  void Revert() {
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
  }

  String[] Xchange(String[] tList) {
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

  String XchangeS(String tList) {
    StringBuffer sb = new StringBuffer();
    for (int s = 0; s < tList.length(); s++) {
      if (keymap.containsKey(str(tList.charAt(s)))) {
        sb.append(keymap.get(str(tList.charAt(s))));
      }
    }
    tList = sb.toString();
    return tList;
  }

  String[] XchangeLeft(String[] tList) {
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

  String reverseit(String current) {
    StringBuffer buffer = new StringBuffer(current);
    buffer = buffer.reverse();
    String reverseString = buffer.toString(); 
    return reverseString;
  }

  void readMe() {
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

