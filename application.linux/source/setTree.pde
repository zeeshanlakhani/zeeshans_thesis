class TreeSet {


  LinkedHashMap findMapL(String[] leftStrings,String usrSub) {
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

  LinkedHashMap findMapR(String[] rightStrings,String usrSub) {
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


  String reverseit(String current) {
    StringBuffer buffer = new StringBuffer(current);
    buffer = buffer.reverse();
    String reverseString = buffer.toString(); 
    return reverseString;
    //println(test);
    //println(reverseString);
  }

  void insertStrings(String[] roots, RadixTreeImpl tree) {
    for (int i = 0; i < roots.length; i++) {
      tree.insert(roots[i],roots[i]);
    }

  }

}





































