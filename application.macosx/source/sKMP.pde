class SearchKMP {

  int[] fail;
  int startIndex;
  int stopIndex;

  int starter(String songstring, String small) {
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


  int preprocess (String songstring, String small) {
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

  int searchkmp (String songstring, String small) {
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




















































